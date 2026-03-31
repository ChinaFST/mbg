package com.dy.colony.mvp.presenter;

import android.app.Application;
import android.webkit.URLUtil;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;

import com.apkfuns.logutils.LogUtils;
import com.dy.colony.BuildConfig;
import com.dy.colony.Constants;
import com.dy.colony.MyAppLocation;
import com.dy.colony.R;
import com.dy.colony.app.utils.FileIOUtils;
import com.dy.colony.app.utils.JxlUtils;
import com.dy.colony.greendao.DBHelper;
import com.dy.colony.greendao.beans.FoodItemAndStandard;
import com.dy.colony.greendao.daos.FoodItemAndStandardDao;
import com.dy.colony.mvp.model.entity.OutMoudle;
import com.dy.colony.mvp.model.entity.UpdateFileMessage;
import com.dy.colony.mvp.model.entity.base.BaseSampleMessage;
import com.dy.colony.mvp.ui.adapter.SampleMessageAdapter;
import com.google.gson.Gson;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.retrofiturlmanager.RetrofitUrlManager;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import javax.inject.Inject;

import com.dy.colony.mvp.contract.SampleMessageContract;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.RxLifecycleUtils;

import org.greenrobot.greendao.query.QueryBuilder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@ActivityScope
public class SampleMessagePresenter extends BasePresenter<SampleMessageContract.Model, SampleMessageContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;
    @Inject
    Gson mGson;
    @Inject
    List<BaseSampleMessage> mList;
    @Inject
    SampleMessageAdapter mAdapter;
    private int page;
    private int seach_page;
    private int chose_page;

    private int preEndIndex;
    private int seach_preEndIndex;

    @Inject
    public SampleMessagePresenter(SampleMessageContract.Model model, SampleMessageContract.View rootView) {
        super(model, rootView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
        this.mGson = null;
        this.mList = null;
        this.mAdapter = null;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreat() {
        lodaMore(true);
    }


    public void seach(String s, boolean pullToRefresh) {
        if (pullToRefresh) {
            //下拉刷新
            seach_page = 0;
            LogUtils.d("--" + seach_page);
            mRootView.sethasLoadedAllItemsfase();
        } else {   //上拉加载
            seach_page++;
            LogUtils.d("++" + seach_page);
        }


        QueryBuilder<? extends BaseSampleMessage> builder = DBHelper.getFoodItemAndStandardDao(mRootView.getActivity()).queryBuilder().whereOr(
                FoodItemAndStandardDao.Properties.SampleName.like("%" + s + "%"),
                FoodItemAndStandardDao.Properties.ItemName.like("%" + s + "%"),
                FoodItemAndStandardDao.Properties.StandardName.like("%" + s + "%"));


        QueryBuilder<? extends BaseSampleMessage> finalBuilder = builder;
        mModel.getFoodItemAndStandard(seach_page, Constants.page_num, builder)
                .subscribeOn(Schedulers.io())
                //.retryWhen(new RetryWithDelay(3, 2))
                .doOnSubscribe(disposable -> {
                    if (pullToRefresh) {
                        mRootView.showLoading();//显示下拉刷新的进度条
                    } else {
                        mRootView.startLoadMore();//显示上拉加载更多的进度条
                    }
                }).subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    if (pullToRefresh) {
                        mRootView.hideLoading();//隐藏下拉刷新的进度条
                    } else {
                        mRootView.endLoadMore();//隐藏上拉加载更多的进度条

                    }
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView)).subscribe(
                new ErrorHandleSubscriber<List<? extends BaseSampleMessage>>(mErrorHandler) {
                    @Override
                    public void onNext(List<? extends BaseSampleMessage> standards) {
                        //mAdapter.removeAllFooterView();
                        if (pullToRefresh) {
                            mList.clear();//如果是下拉刷新则清空列表
                        }
                        seach_preEndIndex = mList.size();//更新之前列表总长度,用于确定加载更多的起始位置
                        mList.addAll(standards);
                        if (standards.size() < Constants.page_num) {
                            mRootView.sethasLoadedAllItemstrue();
                            //ArmsUtils.snackbarText("全部数据已加载完");
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


    public void lodaMore(boolean pullToRefresh) {
        QueryBuilder<? extends BaseSampleMessage> builder = null;


        builder = DBHelper.getFoodItemAndStandardDao(mRootView.getActivity()).queryBuilder();


        if (pullToRefresh) {
            //下拉刷新

            page = 0;
            LogUtils.d("--" + page);
            mRootView.sethasLoadedAllItemsfase();

        } else {   //上拉加载
            page++;
            LogUtils.d("++" + page);
        }
        QueryBuilder<? extends BaseSampleMessage> finalBuilder = builder;
        mModel.getFoodItemAndStandard(page, Constants.page_num, builder)
                .subscribeOn(Schedulers.io())
                //.retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
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
                .subscribe(new ErrorHandleSubscriber<List<? extends BaseSampleMessage>>(mErrorHandler) {
                    @Override
                    public void onNext(List<? extends BaseSampleMessage> standards) {
                        mAdapter.removeAllFooterView();
                        if (pullToRefresh) {
                            mList.clear();//如果是下拉刷新则清空列表
                        }
                        preEndIndex = mList.size();//更新之前列表总长度,用于确定加载更多的起始位置
                        mList.addAll(standards);
                        if (standards.size() < Constants.page_num) {
                            mRootView.sethasLoadedAllItemstrue();
                            //ArmsUtils.snackbarText("全部数据已加载完");
                        } else {
                            mRootView.sethasLoadedAllItemsfase();
                        }
                        if (pullToRefresh) {
                            mAdapter.notifyDataSetChanged();
                        } else {
                            mAdapter.notifyItemRangeInserted(preEndIndex, standards.size());
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

    public void inPutJxl(List<String> list, boolean b) {
        JxlUtils.inToXlsFoodAndItem(list, b)
                .doOnSubscribe(disposable -> {
                    mRootView.showSportDialog(MyAppLocation.myAppLocation.getString(R.string.importdata));
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    mRootView.hideSportDialog();
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<List<String>>(mErrorHandler) {
                    @Override
                    public void onNext(List<String> strings) {
                        String s = "";
                        for (int i = 0; i < strings.size(); i++) {
                            s = s + strings.get(i) + "\r\n";
                        }
                        ArmsUtils.snackbarText(s);
                        mRootView.onRefreshList();
                    }


                });
    }

    public void outPutJxl_other(String path, String filename, String sheetname) {

        mModel.getJXLs()
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    mRootView.showSportDialog(MyAppLocation.myAppLocation.getString(R.string.preparingdata));
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    mRootView.hideSportDialog();
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<List<OutMoudle>>(mErrorHandler) {
                    @Override
                    public void onNext(List<OutMoudle> moudles) {
                        mRootView.hideSportDialog();
                        if (moudles.size() <= 1) {
                            mRootView.showSportDialog(MyAppLocation.myAppLocation.getString(R.string.nodatatoexport));
                        } else {
                            out(path, filename, sheetname, moudles);
                        }
                    }

                });


    }

    private void out(String path, String filename, String sheetname, List<OutMoudle> moudles) {

        JxlUtils.outTo_xls_1Sheet(path, filename, sheetname, moudles).subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    mRootView.showSportDialog(MyAppLocation.myAppLocation.getString(R.string.exportingdata));
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    mRootView.hideSportDialog();
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<Boolean>(mErrorHandler) {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        LogUtils.d("onNext");
                    }

                    @Override
                    public void onComplete() {
                        LogUtils.d("onComplete");

                        mRootView.hideSportDialog();

                    }

                });

    }

    public void checkNewVersion() {
        /*RetrofitUrlManager.getInstance().putDomain("xxx", BuildConfig.DEVICE_UPDATA_URL);
        mModel.upgradeFile(BuildConfig.DEVICE_UPDATA_NAME)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    mRootView.showSportDialog(mApplication.getString(R.string.checking));
                }).subscribeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    mRootView.hideSportDialog();
                }).compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<UpdateFileMessage>(mErrorHandler) {
                    @Override
                    public void onNext(UpdateFileMessage message) {
                        LogUtils.d(message);
                        String code = message.getResultCode();
                        if (!"success1".equals(code)) {
                            ArmsUtils.snackbarText(message.getResultDescripe());
                            return;
                        }
                        UpdateFileMessage.ResultBean result = message.getResult();
                        if (null == result) {
                            ArmsUtils.snackbarText(mApplication.getString(R.string.withoutnewversionn));
                            return;
                        }
                        String linkurl = "";
                        String local = "";
                        linkurl = result.getFoodlink();
                        local = Constants.FOOLINKNAME;
                        LogUtils.d(local);
                        LogUtils.d(linkurl);

                        if (linkurl.isEmpty()) {
                            ArmsUtils.snackbarText(mApplication.getString(R.string.withoutnewversionn));
                            return;
                        }
                        if (!URLUtil.isValidUrl(linkurl)) {
                            ArmsUtils.snackbarText(mApplication.getString(R.string.fileaddressexception) + linkurl);
                            return;
                        }
                        String filename = null;
                        String[] split = linkurl.split("/");
                        filename = split[split.length - 1];
                        mRootView.makeDialogNewVersion(filename, local, linkurl, message);

                        //ArmsUtils.snackbarText("请稍后重试");
                    }
                });*/

    }

    public void downLoadProject(String filename, String linkurl) {
        /*Observable.create(new ObservableOnSubscribe<File>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<File> emitter) throws Exception {
                AppComponent component = ArmsUtils.obtainAppComponentFromContext(mApplication);
                OkHttpClient client = component.okHttpClient();
                Request.Builder builder = new Request.Builder().url(linkurl);
                try {
                    Response execute = client.newCall(builder.build()).execute();
                    ResponseBody body = execute.body();

                    if (execute.isSuccessful()) {
                        if (body.contentType().equals("application/vnd.ms-excel")) {
                            ArmsUtils.snackbarText(mApplication.getString(R.string.filetypeerro));
                            return;
                        }

                        InputStream stream = body.byteStream();
                        File file = new File("/data/data/" + BuildConfig.APPLICATION_ID + "/" + filename);
                        boolean b = FileIOUtils.writeFileFromIS(file, stream);
                        if (b) {
                            emitter.onNext(file);
                            emitter.onComplete();
                        }
                    } else {
                        ArmsUtils.snackbarText(mApplication.getString(R.string.downloadfail));
                        emitter.onComplete();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    ArmsUtils.snackbarText("IO\r\n" + e.getMessage());
                    emitter.onComplete();
                }
            }
        }).subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    mRootView.showSportDialog(MyAppLocation.myAppLocation.getString(R.string.downloading));
                }).subscribeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    mRootView.hideSportDialog();
                }).compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ErrorHandleSubscriber<File>(mErrorHandler) {
                    @Override
                    public void onNext(@NonNull File file) {
                        LogUtils.d(file);
                        mRootView.inputProject(file, filename);

                    }
                });*/
    }
}