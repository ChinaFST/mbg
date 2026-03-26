package com.dy.colony.mvp.presenter;

import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;

import androidx.annotation.StringRes;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;

import com.apkfuns.logutils.LogUtils;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.dy.colony.BuildConfig;
import com.dy.colony.Constants;
import com.dy.colony.MyAppLocation;
import com.dy.colony.R;
import com.dy.colony.app.service.UpLoadIntentService;
import com.dy.colony.app.utils.DataUtils;
import com.dy.colony.app.utils.DiaLogUtils;
import com.dy.colony.app.utils.FileUtils;
import com.dy.colony.greendao.DBHelper;
import com.dy.colony.greendao.beans.Detection_Record_FGGD_NC;
import com.dy.colony.greendao.beans.JTJPoint;
import com.dy.colony.greendao.daos.Detection_Record_FGGD_NCDao;
import com.dy.colony.greendao.daos.JTJPointDao;
import com.dy.colony.mvp.ui.adapter.TestRecordAdapter;
import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

import javax.inject.Inject;

import com.dy.colony.mvp.contract.TestRecordNewContract;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.RxLifecycleUtils;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@ActivityScope
public class TestRecordNewPresenter extends BasePresenter<TestRecordNewContract.Model, TestRecordNewContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;
    @Inject
    List<Detection_Record_FGGD_NC> mList;
    @Inject
    TestRecordAdapter mAdapter;

    private int page = 0;
    private int seach_page = 0;

    private int preEndIndex;
    private int seach_preEndIndex;

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreat() {
        loadMore(true);
    }

    @Inject
    public TestRecordNewPresenter(TestRecordNewContract.Model model, TestRecordNewContract.View rootView) {
        super(model, rootView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
        this.mList = null;
        this.mAdapter = null;
    }

    public void seach(String keyWord, String startTime, String stopTime, String testMoudle, String testResult, boolean pullToRefresh) {

        LogUtils.d(startTime + " " + stopTime);
        if (pullToRefresh) {
            //下拉刷新
            seach_page = 0;
            // LogUtils.d("--" + seach_page);

        } else {   //上拉加载
            seach_page++;
            //LogUtils.d("++" + seach_page);
        }


        QueryBuilder<Detection_Record_FGGD_NC> builder = DBHelper.getDetection_Record_FGGD_NCDao(MyAppLocation.myAppLocation).queryBuilder();
        if (!keyWord.isEmpty()) {
            builder = builder.whereOr(
                    Detection_Record_FGGD_NCDao.Properties.Samplename.like("%" + keyWord + "%"),
                    Detection_Record_FGGD_NCDao.Properties.Samplenum.like("%" + keyWord + "%"),
                    Detection_Record_FGGD_NCDao.Properties.Inspector.like("%" + keyWord + "%"),
                    Detection_Record_FGGD_NCDao.Properties.Testsite.like("%" + keyWord + "%"),
                    Detection_Record_FGGD_NCDao.Properties.Prosecutedunits.like("%" + keyWord + "%"),
                    Detection_Record_FGGD_NCDao.Properties.Prosecutedunits_adress.like("%" + keyWord + "%"),
                    Detection_Record_FGGD_NCDao.Properties.Test_method.like("%" + keyWord + "%"),
                    Detection_Record_FGGD_NCDao.Properties.Test_project.like("%" + keyWord + "%"),
                    Detection_Record_FGGD_NCDao.Properties.SerialNumber.like("%" + keyWord + "%"));
        }

        if (!ArmsUtils.getString(MyAppLocation.myAppLocation, R.string.all).equals(startTime) && !"全部".equals(startTime)) {
            Long starte = DataUtils.getNowtimeYYIMMIDD(startTime);
            Long stop = DataUtils.getNowtimeYYIMMIDD(stopTime);

            //LogUtils.d(aLong);
            builder = builder.where(Detection_Record_FGGD_NCDao.Properties.Testingtime.between(starte, stop + 86400000));
        }

        if (!ArmsUtils.getString(MyAppLocation.myAppLocation, R.string.all).equals(testMoudle)) {
            builder = builder.where(Detection_Record_FGGD_NCDao.Properties.Test_moudle.like("%" + testMoudle + "%"));
        }
        if (!ArmsUtils.getString(MyAppLocation.myAppLocation, R.string.all).equals(testResult)) {

            if (ArmsUtils.getString(MyAppLocation.myAppLocation, R.string.other).equals(testResult)) {
                builder = builder.where(Detection_Record_FGGD_NCDao.Properties.Decisionoutcome.notEq(ArmsUtils.getString(MyAppLocation.myAppLocation, R.string.ok)))
                        .where(Detection_Record_FGGD_NCDao.Properties.Decisionoutcome.notEq(ArmsUtils.getString(MyAppLocation.myAppLocation, R.string.ng)));
            } else {
                builder = builder.where(Detection_Record_FGGD_NCDao.Properties.Decisionoutcome.like(testResult));
            }

        }


        QueryBuilder<Detection_Record_FGGD_NC> finalBuilder = builder;
        mModel.getDetection_Record_FGGD_NC_Seach(builder, seach_page, Constants.page_num)
                .subscribeOn(Schedulers.io())
                //.retryWhen(new RetryWithDelay(3, 2))
                .doOnSubscribe(disposable -> {
                    if (pullToRefresh) {
                        mRootView.showLoading();//显示下拉刷新的进度条
                    } else {
                        mRootView.startLoadMore();//显示上拉加载更多的进度条
                    }
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    if (pullToRefresh) {
                        mRootView.hideLoading();//隐藏下拉刷新的进度条
                    } else {
                        mRootView.endLoadMore();//隐藏上拉加载更多的进度条
                    }
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<List<Detection_Record_FGGD_NC>>(mErrorHandler) {
                    @Override
                    public void onNext(List<Detection_Record_FGGD_NC> standards) {
                        LogUtils.d(standards);

                        if (pullToRefresh) {
                            mList.clear();//如果是下拉刷新则清空列表
                        }
                        seach_preEndIndex = mList.size();//更新之前列表总长度,用于确定加载更多的起始位置
                        mList.addAll(standards);
                        for (int i = 0; i < mList.size(); i++) {
                            Detection_Record_FGGD_NC entity = mList.get(i);

                            ((Detection_Record_FGGD_NC) entity).setSn((i + 1) + "");

                        }
                        if (standards.size() < Constants.page_num) {
                            mRootView.sethasLoadedAllItemstrue();
                        } else {
                            mRootView.sethasLoadedAllItemsfase();
                        }
                        if (pullToRefresh) {
                            mAdapter.notifyDataSetChanged();
                        } else {
                            mAdapter.notifyItemRangeInserted(seach_preEndIndex, standards.size());
                        }
                        long count = finalBuilder.count();
                        long ll;
                        if (count % Constants.page_num == 0) {
                            ll = count / Constants.page_num;
                        } else {
                            ll = count / Constants.page_num + 1;
                        }
                        mRootView.setShowPgeText(mApplication.getString(R.string.general) + ll + mApplication.getString(R.string.page) + count + mApplication.getString(R.string.item));
                    }


                });

    }

    public void loadMore(boolean pullToRefresh) {


        if (pullToRefresh) {
            //下拉刷新

            page = 0;
            LogUtils.d("--" + page);


        } else {   //上拉加载
            page++;
            LogUtils.d("++" + page);
        }


        mModel.getDetection_Record_FGGD_NC_LoadMore(page, Constants.page_num)
                .subscribeOn(Schedulers.io())
                //.retryWhen(new RetryWithDelay(2, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe(disposable -> {
                    if (pullToRefresh) {
                        mRootView.showLoading();//显示下拉刷新的进度条
                    } else {
                        mRootView.startLoadMore();//显示上拉加载更多的进度条
                    }
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    if (pullToRefresh) {
                        mRootView.hideLoading();//隐藏下拉刷新的进度条
                    } else {
                        mRootView.endLoadMore();//隐藏上拉加载更多的进度条

                    }
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<List<Detection_Record_FGGD_NC>>(mErrorHandler) {
                    @Override
                    public void onNext(List<Detection_Record_FGGD_NC> standards) {
                        if (pullToRefresh) {
                            mList.clear();//如果是下拉刷新则清空列表
                        }
                        preEndIndex = mList.size();//更新之前列表总长度,用于确定加载更多的起始位置
                        if (mList.size() == 0) {
                            for (int i = 0; i < 6; i++) {
                                Detection_Record_FGGD_NC detection_record_fggd_nc = new Detection_Record_FGGD_NC();
                                detection_record_fggd_nc.setSamplename("测试" + i);
                                detection_record_fggd_nc.setTest_project("测试项目" + i);
                                detection_record_fggd_nc.setTestresult("测试结果" + i);
                                detection_record_fggd_nc.setTestingtime(1774251098982L);
                                detection_record_fggd_nc.setInspector("检测人员" + i);
                                mList.add(detection_record_fggd_nc);
                            }

                        }
                        mList.addAll(standards);
                        for (int i = 0; i < mList.size(); i++) {
                            Detection_Record_FGGD_NC entity = mList.get(i);

                            ((Detection_Record_FGGD_NC) entity).setSn((i + 1) + "");

                        }
                        if (standards.size() < Constants.page_num) {
                            mRootView.sethasLoadedAllItemstrue();
                        } else {
                            mRootView.sethasLoadedAllItemsfase();
                        }
                        if (pullToRefresh) {
                            mAdapter.notifyDataSetChanged();
                        } else {
                            mAdapter.notifyItemRangeInserted(preEndIndex, standards.size());

                        }
                        long count = DBHelper.getDetection_Record_FGGD_NCDao(mRootView.getActivity()).count();
                        long ll;
                        if (count % Constants.page_num == 0) {
                            ll = count / Constants.page_num;
                        } else {
                            ll = count / Constants.page_num + 1;
                        }
                        mRootView.setShowPgeText(mApplication.getString(R.string.general) + ll + mApplication.getString(R.string.page) + count + mApplication.getString(R.string.item));
                    }


                });
    }

    public void deleteData() {
        List<Detection_Record_FGGD_NC> checkData = new ArrayList<>();
        List<Detection_Record_FGGD_NC> data = mAdapter.getData();
        for (int i = 0; i < data.size(); i++) {
            Detection_Record_FGGD_NC entity = data.get(i);
            Detection_Record_FGGD_NC nc = (Detection_Record_FGGD_NC) entity;
            if (nc.checkState) {
                checkData.add(nc);
            }

        }
        if (checkData.size() == 0) {
            ArmsUtils.snackbarText(mApplication.getString(R.string.hint_pleaceselecedeletedata));
            return;
        }
        makeDeleteChose(checkData);
    }

    private void makeDeleteChose(List<Detection_Record_FGGD_NC> checkData) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mRootView.getActivity());
        dialog.setTitle(R.string.hint);
        dialog.setMessage(mApplication.getString(R.string.areyousuredelete) + checkData.size() + mApplication.getString(R.string.testresultitem));
        dialog.setPositiveButton(MyAppLocation.myAppLocation.getString(R.string.delete), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mRootView.showSportDialog(MyAppLocation.myAppLocation.getString(R.string.deleteing));
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        for (int i = 0; i < checkData.size(); i++) {
                            Detection_Record_FGGD_NC entity = checkData.get(i);
                            String code = entity.getSysCode();
                            String moudle = entity.getTest_Moudle();
                            if (moudle.contains(mRootView.getActivity().getString(R.string.JTJ_TestMoudle_P))) {
                                FileUtils.deleteBitmapByUUID_LEVEL1(code);
                                FileUtils.deleteBitmapByUUID_LEVEL2(code);


                            }
                            DBHelper.getDetection_Record_FGGD_NCDao(mRootView.getActivity()).delete(entity);
                            mList.remove(entity);
                            JTJPointDao dao = DBHelper.getJTJPointDao(mRootView.getActivity());

                            List<JTJPoint> list = dao.queryBuilder().where(JTJPointDao.Properties.Uuid.eq(code)).list();
                            for (int j = 0; j < list.size(); j++) {
                                dao.delete(list.get(j));
                            }

                        }
                        mRootView.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mRootView.hideSportDialog();
                                mRootView.onRefresh();
                                //loadMore(true);
                            }
                        });
                    }
                }, 0);

            }
        });
        dialog.setNegativeButton(MyAppLocation.myAppLocation.getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dialog.show();
    }

    public void uploadData() {
        List<Detection_Record_FGGD_NC> checkData = new ArrayList<>();
        List<Detection_Record_FGGD_NC> isuped = new ArrayList<>();
        List<Detection_Record_FGGD_NC> erro = new ArrayList<>();
        List<Detection_Record_FGGD_NC> data = mAdapter.getData();
        List<String> decisionoutcomeList = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            Detection_Record_FGGD_NC entity = data.get(i);

            Detection_Record_FGGD_NC nc = (Detection_Record_FGGD_NC) entity;
            if (nc.checkState) {
                String decisionoutcome = nc.getDecisionoutcome();

                if (!getString(R.string.ok).equals(decisionoutcome) && !getString(R.string.ng).equals(decisionoutcome)) {
                    ArmsUtils.snackbarText(getString(R.string.testresult) + "：" + getString(R.string.Upload_not_supported));
                    continue;
                }

                // LogUtils.d(new Gson().toJson(nc));
                checkData.add(nc);

                if (nc.getIsupload() == 1) {
                    //checkData.remove(nc);
                    isuped.add(nc);
                }


            }


        }
        if (checkData.size() == 0) {
            ArmsUtils.snackbarText(getString(R.string.sel_need_test_record));
            return;
        }

        if (!BuildConfig.DEBUG) {
            if (isuped.size() == checkData.size()) {
                ArmsUtils.snackbarText(getString(R.string.selected_data_has_upload));
                return;
            }
        }
        if (!decisionoutcomeList.isEmpty() && decisionoutcomeList.contains(getString(R.string.ng))) {
            showHint(checkData);
            return;
        }


        //list 倒叙
        Collections.reverse(checkData);


        uploadByIntentService(checkData);


    }

    private void showHint(List<Detection_Record_FGGD_NC> checkData) {
        DiaLogUtils.showAlert(mRootView.getActivity(), getString(R.string.hint), getString(R.string.contain_positive_data), getString(R.string.str_continue), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Collections.reverse(checkData);
                uploadByIntentService(checkData);
            }
        }, mRootView.getActivity().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).show();
    }

    private String getString(@StringRes int id) {
        return mRootView.getActivity().getString(id);
    }

    private void uploadByIntentService(List<Detection_Record_FGGD_NC> data) {
        LogUtils.d("uploadByIntentService");
        for (int i = 0; i < data.size(); i++) {
            Detection_Record_FGGD_NC nc = data.get(i);
            UpLoadIntentService.startUpLoad(mRootView.getActivity(), nc.getId());
        }
    }
}