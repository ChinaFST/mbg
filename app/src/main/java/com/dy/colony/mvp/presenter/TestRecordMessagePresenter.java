package com.dy.colony.mvp.presenter;

import android.app.Application;

import com.apkfuns.logutils.LogUtils;
import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

import javax.inject.Inject;

import com.dy.colony.mvp.contract.TestRecordMessageContract;
import com.jess.arms.utils.PermissionUtil;
import com.jess.arms.utils.RxLifecycleUtils;

import java.io.File;
import java.util.List;

@ActivityScope
public class TestRecordMessagePresenter extends BasePresenter<TestRecordMessageContract.Model, TestRecordMessageContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public TestRecordMessagePresenter(TestRecordMessageContract.Model model, TestRecordMessageContract.View rootView) {
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

    public void requestfile(String path,String std_num) {
        //请求外部存储权限用于适配android6.0的权限管理机制
        PermissionUtil.externalStorage(new PermissionUtil.RequestPermission() {
            @Override
            public void onRequestPermissionSuccess() {
                //request permission success, do something.
                requestFromModel(path,std_num);
            }

            @Override
            public void onRequestPermissionFailure(List<String> permissions) {
                mRootView.showMessage("获取读写权限失败");
                mRootView.hideLoading();//隐藏下拉刷新的进度条
            }

            @Override
            public void onRequestPermissionFailureWithAskNeverAgain(List<String> permissions) {
                mRootView.showMessage("需要到设置更改权限");
                mRootView.hideLoading();//隐藏下拉刷新的进度条
            }
        }, mRootView.getRxPermissions(), mErrorHandler);
    }

    public void requestFromModel(String path,String std_name) {


        mModel.getFiles(path)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {

                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<List<File>>(mErrorHandler) {
                    @Override
                    public void onNext(List<File> files) {
                        for (int i = 0; i < files.size(); i++) {
                            File file = files.get(i);
                            String name = file.getName();
                            LogUtils.d(name);
                            LogUtils.d(std_name);

                            if (name.contains(std_name)){
                                mRootView.fundSuccess(file);
                                return;
                            }
                        }
                        mRootView.fundFail("未找到相关标准文件");
                    }
                });
    }

}