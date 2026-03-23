package com.dy.colony.mvp.presenter;

import android.app.Application;

import com.apkfuns.logutils.LogUtils;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.dy.colony.Constants;
import com.dy.colony.MyAppLocation;
import com.dy.colony.greendao.beans.Company_Point;
import com.dy.colony.greendao.beans.Detection_Record_FGGD_NC;
import com.dy.colony.mvp.model.entity.base.GalleryBean;
import com.dy.colony.mvp.ui.adapter.ChoseUnitsMessageAdapter;
import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

import javax.inject.Inject;

import com.dy.colony.mvp.contract.ChoseUnitContract;
import com.jess.arms.utils.RxLifecycleUtils;

import java.util.List;

@ActivityScope
public class ChoseUnitPresenter extends BasePresenter<ChoseUnitContract.Model, ChoseUnitContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;
    @Inject
    List<MultiItemEntity> res;
    @Inject
    ChoseUnitsMessageAdapter mAdapter;

    private int seach_page;
    private int page;
    private int seach_preEndIndex;

    @Inject
    public ChoseUnitPresenter(ChoseUnitContract.Model model, ChoseUnitContract.View rootView) {
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


    public void seach(String query, boolean pullToRefresh) {
        LogUtils.d("seach");

        if (pullToRefresh) {
            //下拉刷新
            seach_page = 0;
            // LogUtils.d("--" + seach_page);

        } else {   //上拉加载
            seach_page++;
            //LogUtils.d("++" + seach_page);
        }
        mModel.seachData(seach_page, Constants.page_num_unit, query)
                .subscribeOn(Schedulers.io())
                //.retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe(disposable -> {
                    //mRootView.showSportDialog(mRootView.getActivity().getString(R.string.seaching));//显示下拉刷新的进度条
                    if (pullToRefresh) {
                        mRootView.showLoading();//显示下拉刷新的进度条
                    } else {
                        mRootView.startLoadMore();//显示上拉加载更多的进度条
                    }
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    //mRootView.hideSportDialog();
                    if (pullToRefresh) {
                        mRootView.hideLoading();//隐藏下拉刷新的进度条
                    } else {
                        mRootView.endLoadMore();//隐藏上拉加载更多的进度条
                    }
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<List<Company_Point>>(mErrorHandler) {
                    @Override
                    public void onNext(List<Company_Point> points) {

                        LogUtils.d(query);
                        LogUtils.d(points);
                        if (pullToRefresh) {
                            res.clear();//如果是下拉刷新则清空列表
                        }
                        seach_preEndIndex = res.size();//更新之前列表总长度,用于确定加载更多的起始位置
                        res.addAll(points);
                        if (points.size() < Constants.page_num_unit) {
                            mRootView.sethasLoadedAllItemstrue();
                        } else {
                            mRootView.sethasLoadedAllItemsfase();
                        }
                        if (pullToRefresh) {
                            mAdapter.notifyDataSetChanged();
                        } else {
                            mAdapter.notifyItemRangeInserted(seach_preEndIndex, points.size());
                        }

                    }
                });


    }

    public void loadMore(boolean pullToRefresh) {
        LogUtils.d("loadMore");
        LogUtils.d(pullToRefresh);

        if (pullToRefresh) {
            //下拉刷新
            page = 0;
            // LogUtils.d("--" + page);

        } else {   //上拉加载
            page++;
            // LogUtils.d("++" + page);
        }
        mModel.loadMore(page, Constants.page_num_unit)
                .subscribeOn(Schedulers.io())
                //.retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe(disposable -> {
                    // mRootView.showSportDialog(mRootView.getActivity().getString(R.string.loading_data));//显示下拉刷新的进度条
                    if (pullToRefresh) {
                        mRootView.showLoading();//显示下拉刷新的进度条
                    } else {
                        mRootView.startLoadMore();//显示上拉加载更多的进度条
                    }
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    // mRootView.hideSportDialog();
                    if (pullToRefresh) {
                        mRootView.hideLoading();//隐藏下拉刷新的进度条
                    } else {
                        mRootView.endLoadMore();//隐藏上拉加载更多的进度条
                    }
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<List<Company_Point>>(mErrorHandler) {
                    @Override
                    public void onNext(List<Company_Point> points) {
                        //LogUtils.d(points);
                        if (pullToRefresh) {
                            res.clear();//如果是下拉刷新则清空列表
                        }
                        seach_preEndIndex = res.size();//更新之前列表总长度,用于确定加载更多的起始位置
                        res.addAll(points);
                        if (points.size() < Constants.page_num_unit) {
                            mRootView.sethasLoadedAllItemstrue();
                        } else {
                            mRootView.sethasLoadedAllItemsfase();
                        }
                        if (pullToRefresh) {
                            mAdapter.notifyDataSetChanged();
                        } else {
                            mAdapter.notifyItemRangeInserted(seach_preEndIndex, points.size());
                        }
                    }
                });
    }


}