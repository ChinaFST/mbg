package com.dy.colony.mvp.presenter;

import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.webkit.URLUtil;

import com.apkfuns.logutils.LogUtils;
import com.dy.colony.BuildConfig;
import com.dy.colony.Constants;
import com.dy.colony.MyAppLocation;
import com.dy.colony.R;
import com.dy.colony.app.utils.FileIOUtils;
import com.dy.colony.app.utils.JxlUtils;
import com.dy.colony.mvp.model.entity.OutMoudle;
import com.dy.colony.mvp.model.entity.UpdateFileMessage;
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

import com.dy.colony.mvp.contract.EditorProjectContract;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.RxLifecycleUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

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
    AlertDialog.Builder mAlertDialog;

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

    public void checkNewVersion(String from) {
        /*RetrofitUrlManager.getInstance().putDomain("xxx", BuildConfig.DEVICE_UPDATA_URL);
        mModel.upgradeFile(BuildConfig.DEVICE_UPDATA_NAME)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    mRootView.showLoading();
                }).subscribeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    mRootView.hideLoading();
                }).compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<UpdateFileMessage>(mErrorHandler) {
                    @Override
                    public void onNext(UpdateFileMessage message) {
                        mRootView.hideLoading();
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
                        if ("fggd".equals(from)) {
                            linkurl = result.getFgItemlink();
                            //local = Constants.FGITEMLINK;
                        } else if (from.contains("jtj")) {
                            linkurl = result.getJtjlink();
                            //local = Constants.JTJLINK;
                        }
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
                        mRootView.makeDialogNewVersion(filename, local, from, linkurl, message);

                        //ArmsUtils.snackbarText("请稍后重试");
                    }
                });*/
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
                        mRootView.inputProject(file, filename, from);

                    }
                });

    }

    public void inPutFGGDItem(List<String> list, boolean b) {
        JxlUtils.inToXlsFggd(list, b)
                .doOnSubscribe(disposable -> {
                    mRootView.showSportDialog(mApplication.getString(R.string.importdata));
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    mRootView.hideSportDialog();
                    mRootView.RefreshList();
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<List<String>>(mErrorHandler) {
                    @Override
                    public void onNext(List<String> strings) {
                        String s = "";
                        for (int i = 0; i < strings.size(); i++) {
                            s = s + strings.get(i) + "\r\n";
                        }
                        mRootView.hideSportDialog();
                        ArmsUtils.snackbarText(s);
                    }

                });
    }

    public void inPutJTJitem(List<String> list, boolean b) {
        //JxlUtils.inPutJTJItem(list,b);
        JxlUtils.inToXlsJtj(list, b)
                .doOnSubscribe(disposable -> {
                    mRootView.showSportDialog(MyAppLocation.myAppLocation.getString(R.string.importdata));
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    mRootView.hideSportDialog();
                    mRootView.RefreshList();
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<List<String>>(mErrorHandler) {
                    @Override
                    public void onNext(List<String> strings) {
                        String s = "";
                        for (int i = 0; i < strings.size(); i++) {
                            s = s + strings.get(i) + "\r\n";
                        }
                        mRootView.hideSportDialog();
                        ArmsUtils.snackbarText(s);
                    }
                });
    }

    public void makeCheckDialog(List<String> list, String mFrom) {
        //mAlertDialog.setIcon(R.mipmap.ic);
        mAlertDialog.setTitle(R.string.hint);
        mAlertDialog.setMessage(R.string.skipitemmessage);
        mAlertDialog.setPositiveButton(MyAppLocation.myAppLocation.getString(R.string.replace), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //导入检测项目
                if ("fggd".equals(mFrom)) {
                    inPutFGGDItem(list, false);
                } else if (mFrom.contains("jtj")) {
                    inPutJTJitem(list, false);
                }

            }
        });
        mAlertDialog.setNegativeButton(MyAppLocation.myAppLocation.getString(R.string.keep), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //导入检测项目
                if ("fggd".equals(mFrom)) {
                    inPutFGGDItem(list, true);
                } else if (mFrom.contains("jtj")) {
                    inPutJTJitem(list, true);
                }
            }
        });
        mAlertDialog.show();
    }

    public void outPutItem(String path, String filename, String sheetname, int i) {
        switch (i) {
            case 1:
                mModel.getFGGDJXLs()
                        .subscribeOn(Schedulers.io())
                        .doOnSubscribe(disposable -> {
                            mRootView.showSportDialog(mApplication.getString(R.string.preparingdata));
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
                                if (moudles.size() <= 1) {
                                    ArmsUtils.snackbarText(mApplication.getString(R.string.nodatatoexport));
                                }
                                out(path, filename, sheetname, moudles);
                            }

                        });
                break;
            case 2:
                mModel.getJTJJXLs()
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
                                if (moudles.size() <= 1) {
                                    mRootView.showSportDialog(MyAppLocation.myAppLocation.getString(R.string.nodatatoexport));
                                } else {
                                    out(path, filename, sheetname, moudles);
                                }
                            }

                        });
                break;

        }

    }
    private void out(String path, String filename, String sheetname, List<OutMoudle> moudles) {
        JxlUtils.outTo_xls_1Sheet(path, filename, sheetname, moudles).subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    mRootView.showMessage(mApplication.getString(R.string.exportingdata));
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
                    }
                    @Override
                    public void onComplete() {
                        mRootView.hideSportDialog();

                    }

                });
    }
}