package com.dy.colony.mvp.presenter;

import android.app.Application;

import com.apkfuns.logutils.LogUtils;
import com.dy.colony.BuildConfig;
import com.dy.colony.R;
import com.dy.colony.app.utils.FileIOUtils;
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
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import javax.inject.Inject;

import com.dy.colony.mvp.contract.EditorProjectContract;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.RxLifecycleUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@ActivityScope
public class EditorProjectPresenter extends BasePresenter<EditorProjectContract.Model, EditorProjectContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public EditorProjectPresenter(EditorProjectContract.Model model, EditorProjectContract.View rootView) {
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






    public void downLoadProject(String filename, String from, String url) {
        Observable.create(new ObservableOnSubscribe<File>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<File> emitter) throws Exception {
                AppComponent component = ArmsUtils.obtainAppComponentFromContext(mApplication);
                OkHttpClient client = component.okHttpClient();
                Request.Builder builder = new Request.Builder().url(url);
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
                        if (b){
                            emitter.onNext(file);
                            emitter.onComplete();
                        }
                    }else {
                        ArmsUtils.snackbarText(mApplication.getString(R.string.downloadfail));
                        emitter.onComplete();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    ArmsUtils.snackbarText("IO\r\n"+e.getMessage());
                    emitter.onComplete();
                }
            }
        }).subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    mRootView.showLoading();
                }).subscribeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    mRootView.hideLoading();
                }).compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ErrorHandleSubscriber<File>(mErrorHandler) {
                    @Override
                    public void onNext(@NonNull File file) {
                        LogUtils.d(file);
                        mRootView.inputProject(file,filename,from);

                    }
                });


    }
}