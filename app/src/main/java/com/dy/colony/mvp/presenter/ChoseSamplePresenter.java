package com.dy.colony.mvp.presenter;

import android.app.Application;

import com.apkfuns.logutils.LogUtils;
import com.dy.colony.Constants;
import com.dy.colony.greendao.DBHelper;
import com.dy.colony.greendao.beans.Company_Point;
import com.dy.colony.greendao.daos.FoodItemAndStandardDao;
import com.dy.colony.mvp.model.entity.base.BaseSampleMessage;
import com.dy.colony.mvp.model.entity.base.BaseUntilMessage;
import com.dy.colony.mvp.ui.adapter.ChooseSampleAdapter;
import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

import javax.inject.Inject;

import com.dy.colony.mvp.contract.ChoseSampleContract;
import com.jess.arms.utils.RxLifecycleUtils;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

@ActivityScope
public class ChoseSamplePresenter extends BasePresenter<ChoseSampleContract.Model, ChoseSampleContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    List<BaseSampleMessage> mList;


    @Inject
    ChooseSampleAdapter mAdapter;

    private int page;
    private int seach_page;
    private int preEndIndex;
    private int seach_preEndIndex;

    @Inject
    public ChoseSamplePresenter(ChoseSampleContract.Model model, ChoseSampleContract.View rootView) {
        super(model, rootView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }


    public void lodaMore(String projectName, boolean b, String from) {

        LogUtils.d(projectName);
        QueryBuilder<? extends BaseSampleMessage> builder;


        if ("".equals(projectName)) {
            builder = DBHelper.getFoodItemAndStandardDao(mRootView.getActivity()).queryBuilder();
        } else {
            builder = DBHelper.getFoodItemAndStandardDao(mRootView.getActivity()).queryBuilder()
                    .where(FoodItemAndStandardDao.Properties.ItemName.like(projectName));
        }


        // LogUtils.d(builder.count());
        if (b) {
            //下拉刷新
            page = 0;
            LogUtils.d("--" + page);
            mRootView.sethasLoadedAllItemsfase();
        } else {   //上拉加载
            page++;
            LogUtils.d("++" + page);
        }
        QueryBuilder<? extends BaseSampleMessage> finalBuilder = builder;
        mModel.getSample(page, Constants.page_num, builder, from)
                .subscribeOn(Schedulers.io())
                //.retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe(disposable -> {
                    if (b) {
                        mRootView.showLoading();//显示下拉刷新的进度条
                    } else {
                        mRootView.startLoadMore();//显示上拉加载更多的进度条
                    }
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    if (b) {
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
                        if (b) {
                            mList.clear();//如果是下拉刷新则清空列表
                        }
                        preEndIndex = mList.size();//更新之前列表总长度,用于确定加载更多的起始位置
                        mList.addAll(standards);
                        if (standards.size() < Constants.page_num) {
                            mRootView.sethasLoadedAllItemstrue();
                        }
                        if (b) {
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
                        mRootView.setShowPgeText("共" + ll + "页，" + count + "条");
                    }


                });
    }


    public void seachFromFoodItemAndStand(String keyword, String projectName, boolean b, String from) {
        if (b) {
            //下拉刷新

            seach_page = 0;
            LogUtils.d("--" + seach_page);
            mRootView.sethasLoadedAllItemsfase();

        } else {   //上拉加载
            seach_page++;
            LogUtils.d("++" + seach_page);
        }

        QueryBuilder<? extends BaseSampleMessage> builder;
        if ("".equals(projectName)) {
            builder = DBHelper.getFoodItemAndStandardDao(mRootView.getActivity()).queryBuilder()
                    .whereOr(
                            FoodItemAndStandardDao.Properties.SampleName.like("%" + keyword + "%"),
                            FoodItemAndStandardDao.Properties.ItemName.like("%" + keyword + "%"),
                            FoodItemAndStandardDao.Properties.StandardName.like("%" + keyword + "%"));
        } else {
            builder = DBHelper.getFoodItemAndStandardDao(mRootView.getActivity()).queryBuilder()
                    .where(FoodItemAndStandardDao.Properties.ItemName.like(projectName)).whereOr(
                            FoodItemAndStandardDao.Properties.SampleName.like("%" + keyword + "%"),
                            FoodItemAndStandardDao.Properties.StandardName.like("%" + keyword + "%"));
        }


        LogUtils.d(builder.count());
        QueryBuilder<? extends BaseSampleMessage> finalBuilder = builder;
        mModel.getSample(seach_page, Constants.page_num, builder, from)
                .subscribeOn(Schedulers.io())
                //.retryWhen(new RetryWithDelay(3, 2))
                .doOnSubscribe(disposable -> {
                    if (b) {
                        mRootView.showLoading();//显示下拉刷新的进度条
                    } else {
                        mRootView.startLoadMore();//显示上拉加载更多的进度条
                    }
                }).subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    if (b) {
                        mRootView.hideLoading();//隐藏下拉刷新的进度条
                    } else {
                        mRootView.endLoadMore();//隐藏上拉加载更多的进度条
                    }
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<List<? extends BaseSampleMessage>>(mErrorHandler) {
                    @Override
                    public void onNext(List<? extends BaseSampleMessage> standards) {

                        mAdapter.removeAllFooterView();
                        if (b) {
                            mList.clear();//如果是下拉刷新则清空列表
                        }
                        seach_preEndIndex = mList.size();//更新之前列表总长度,用于确定加载更多的起始位置
                        mList.addAll(standards);
                        if (standards.size() < Constants.page_num) {
                            mRootView.sethasLoadedAllItemstrue();
                        }
                        if (b) {
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
                        mRootView.setShowPgeText("共" + ll + "页，" + count + "条");


                    }

                });
    }
}