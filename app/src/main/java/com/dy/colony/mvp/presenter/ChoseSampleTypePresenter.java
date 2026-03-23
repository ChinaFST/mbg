package com.dy.colony.mvp.presenter;

import android.app.Application;

import com.dy.colony.R;
import com.dy.colony.mvp.model.entity.base.BaseSimple33Message;
import com.dy.colony.mvp.ui.adapter.ChoseSimple33Adapter;
import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import io.reactivex.android.schedulers.AndroidSchedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

import javax.inject.Inject;

import com.dy.colony.mvp.contract.ChoseSampleTypeContract;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.RxLifecycleUtils;

import java.util.List;

@ActivityScope
public class ChoseSampleTypePresenter extends BasePresenter<ChoseSampleTypeContract.Model, ChoseSampleTypeContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;
    @Inject
    ChoseSimple33Adapter mAdapter;
    @Inject
    List<BaseSimple33Message> mList;

    @Inject
    public ChoseSampleTypePresenter(ChoseSampleTypeContract.Model model, ChoseSampleTypeContract.View rootView) {
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

    public void loadData() {
        mModel.load()
                .doOnSubscribe(disposable -> {
                    mRootView.showSportDialog(mRootView.getActivity().getString(R.string.loading));
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    mRootView.hideSportDialog();
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<List<? extends BaseSimple33Message>>(mErrorHandler) {
                    @Override
                    public void onNext(List<? extends BaseSimple33Message> messages) {
                        mList.clear();
                        mList.addAll(messages);
                        mAdapter.notifyDataSetChanged();
                    }
                });

    }

    public void checksimple(BaseSimple33Message next) {
        mModel.tonull();
        String s = mModel.checkPcode(next);
        mRootView.settitle(s);
    }

    public void checkData(BaseSimple33Message message) {
        checksimple(message);
        mModel.checkData(message)
                .doOnSubscribe(disposable -> {
                    mRootView.showSportDialog(mRootView.getActivity().getString(R.string.loading));
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    mRootView.hideSportDialog();
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<List<? extends BaseSimple33Message>>(mErrorHandler) {
                    @Override
                    public void onNext(List<? extends BaseSimple33Message> messages) {
                        if (messages.size() == 0) {
                            ArmsUtils.snackbarText(mRootView.getActivity().getString(R.string.dialog_message_nomordata));
                        } else {
                            mList.clear();
                            mList.addAll(messages);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    public void backleve() {
        List<BaseSimple33Message> data = mAdapter.getData();
        if (data.size()==0){
            ArmsUtils.snackbarText("没有数据了");
            return;
        }
        mModel.checkDatabackleve(data.get(0))
                .doOnSubscribe(disposable -> {
                    mRootView.showSportDialog(mRootView.getActivity().getString(R.string.loading));
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    mRootView.hideSportDialog();
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<List<? extends BaseSimple33Message>>(mErrorHandler) {
                    @Override
                    public void onNext(List<? extends BaseSimple33Message> messages) {
                        if (messages.size() == 0) {
                            ArmsUtils.snackbarText(mRootView.getActivity().getString(R.string.dialog_message_top));
                        } else {
                            checksimple(messages.get(0));
                            mList.clear();
                            mList.addAll(messages);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}