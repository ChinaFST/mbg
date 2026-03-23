package com.dy.colony.mvp.presenter;

import android.app.Application;

import com.apkfuns.logutils.LogUtils;
import com.dy.colony.mvp.model.entity.base.BaseProjectMessage;
import com.dy.colony.mvp.ui.adapter.ChoseProjectMessageAdapter;
import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

import javax.inject.Inject;

import com.dy.colony.mvp.contract.ChoseProjectContract;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.RxLifecycleUtils;

import java.util.List;

@ActivityScope
public class ChoseProjectPresenter extends BasePresenter<ChoseProjectContract.Model, ChoseProjectContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    List<BaseProjectMessage> mTestItemList;
    @Inject
    ChoseProjectMessageAdapter mAdapter;

    @Inject
    public ChoseProjectPresenter(ChoseProjectContract.Model model, ChoseProjectContract.View rootView) {
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

    public void loadProject(String from, String... keyword) {
        if ("fggd".equals(from)) {
            loaFGGDItem(0, keyword);
        } else if ("jtj_1".equals(from)) {
            loadJTJItem(1, keyword);
        } else {
            ArmsUtils.snackbarText("参数错误");
            mRootView.killMyself();
        }
    }

    private void loaFGGDItem(int i, String... keyword) {
        mModel.loadFGGDitem(i, keyword)
                .doOnSubscribe(disposable -> {
                    mRootView.showLoading();
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    mRootView.hideLoading();
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<List<?extends BaseProjectMessage>>(mErrorHandler) {
                    @Override
                    public void onNext(@NonNull List<?extends BaseProjectMessage> baseProjectMessages) {
                        LogUtils.d(baseProjectMessages.size());
                        mTestItemList.clear();
                        mTestItemList.addAll(baseProjectMessages);
                        mAdapter.notifyDataSetChanged();
                    }
                });

    }

    private void loadJTJItem(int i, String... keyword) {
        mModel.loadJTJitem(i, keyword)
                .doOnSubscribe(disposable -> {
                    mRootView.showLoading();
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    mRootView.hideLoading();
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<List<? extends BaseProjectMessage>>(mErrorHandler) {
                    @Override
                    public void onNext(@NonNull List<? extends BaseProjectMessage> baseProjectMessages) {
                        mTestItemList.clear();
                        mTestItemList.addAll(baseProjectMessages);
                        mAdapter.notifyDataSetChanged();
                    }
                });


    }
}