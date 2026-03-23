package com.dy.colony.mvp.presenter;

import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apkfuns.logutils.LogUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dy.colony.Constants;
import com.dy.colony.R;
import com.dy.colony.greendao.beans.FoodItemAndStandard;
import com.dy.colony.mvp.model.entity.base.BaseSampleMessage;
import com.dy.colony.mvp.ui.adapter.DialogChoseProjectAdapter;
import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;

import javax.inject.Inject;

import com.dy.colony.mvp.contract.EdtorSampleContract;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.RxLifecycleUtils;

import java.util.ArrayList;
import java.util.List;

@ActivityScope
public class EdtorSamplePresenter extends BasePresenter<EdtorSampleContract.Model, EdtorSampleContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public EdtorSamplePresenter(EdtorSampleContract.Model model, EdtorSampleContract.View rootView) {
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

    public void loadLocaProject() {

        mModel.loadLocaProject("")
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(2, 1))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe(disposable -> {
                    mRootView.showLoading();
                }).subscribeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    mRootView.hideLoading();
                }).compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<List<String>>(mErrorHandler) {
                    @Override
                    public void onNext(List<String> messages) {
                        if (messages.size() == 0) {
                            ArmsUtils.snackbarText(mApplication.getString(R.string.ea_sampleerro1));
                            return;
                        }
                        mRootView.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                makeChoseProjectDialog(messages);
                            }
                        });

                    }
                });
    }

    private void makeChoseProjectDialog(List<String> messages) {
        String[] strings = new String[messages.size()];
        for (int i = 0; i < messages.size(); i++) {
            strings[i] = messages.get(i);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(mRootView.getActivity());
        //builder.setIcon(R.mipmap.ic);
        builder.setTitle(R.string.place_choseproject);

        builder.setSingleChoiceItems(strings, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mRootView.setChosedProject(strings[which]);
                dialog.dismiss();
            }
        });
        builder.show();

    }

    public void loadLocaStandNumber(String projectName) {
        LogUtils.d(projectName);

        mModel.loadLocaStandNumber(projectName)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(2, 1))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe(disposable -> {
                    mRootView.showLoading();
                }).subscribeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    mRootView.hideLoading();
                }).compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<List<FoodItemAndStandard>>(mErrorHandler) {
                    @Override
                    public void onNext(List<FoodItemAndStandard> messages) {
                        if (messages.size() == 0) {
                            ArmsUtils.snackbarText(mRootView.getActivity().getString(R.string.local_standards_not_found));
                            return;
                        }
                        if (messages.size() == 1) {
                            mRootView.setChosedStandNumber(messages.get(0));
                            return;
                        }
                        mRootView.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                makeChoseStandNumberDialog(messages);
                            }
                        });

                    }
                });


    }

    private void makeChoseStandNumberDialog(List<? extends BaseSampleMessage> messages) {
        String[] strings = new String[messages.size()];
        for (int i = 0; i < messages.size(); i++) {
            BaseSampleMessage baseSampleMessage = messages.get(i);
            if (baseSampleMessage instanceof FoodItemAndStandard) {
                FoodItemAndStandard standard = (FoodItemAndStandard) baseSampleMessage;
                strings[i] = standard.getStandardName() + "\r\n" + standard.getCheckSign() + standard.getStandardValue() + standard.getCheckValueUnit();
            }

        }
        AlertDialog.Builder builder = new AlertDialog.Builder(mRootView.getActivity());
        //builder.setIcon(R.mipmap.ic);
        builder.setTitle(R.string.place_choseproject);

        builder.setSingleChoiceItems(strings, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mRootView.setChosedStandNumber(messages.get(which));
                dialog.dismiss();
            }
        });
        builder.show();
    }

    boolean isSeach = false;

    public void makeDialogShowproject() {

        AlertDialog.Builder builder = new AlertDialog.Builder(mRootView.getActivity());
        //builder.setIcon(R.mipmap.ic);
        builder.setTitle(R.string.place_choseproject);
        View view2 = View.inflate(mRootView.getActivity(), R.layout.dialog_choseproject_layout, null);
        EditText keyword = (EditText) view2.findViewById(R.id.keyword);
        Button btnseach = (Button) view2.findViewById(R.id.btn_seach);
        RecyclerView recyclerView = (RecyclerView) view2.findViewById(R.id.re_recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mRootView.getActivity(), 1);
        ArmsUtils.configRecyclerView(recyclerView, gridLayoutManager);
        ArrayList<String> data = new ArrayList<>();
        DialogChoseProjectAdapter dialogChoseProjectAdapter = new DialogChoseProjectAdapter(R.layout.dialog_choseproject_layout_item, data);
        recyclerView.setAdapter(dialogChoseProjectAdapter);


        mModel.loadLocaProject("")
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(2, 1))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe(disposable -> {
                    mRootView.showLoading();
                }).subscribeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    mRootView.hideLoading();
                }).compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<List<String>>(mErrorHandler) {
                    @Override
                    public void onNext(List<String> messages) {
                        data.clear();
                        data.addAll(messages);
                        dialogChoseProjectAdapter.notifyDataSetChanged();

                    }
                });

        btnseach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSeach) {
                    isSeach = false;
                    keyword.setText("");
                    btnseach.setText(R.string.seach);
                    mModel.loadLocaProject("")
                            .subscribeOn(Schedulers.io())
                            .retryWhen(new RetryWithDelay(2, 1))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                            .doOnSubscribe(disposable -> {
                                mRootView.showLoading();
                            }).subscribeOn(AndroidSchedulers.mainThread())
                            .doFinally(() -> {
                                mRootView.hideLoading();
                            }).compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                            .subscribe(new ErrorHandleSubscriber<List<String>>(mErrorHandler) {
                                @Override
                                public void onNext(List<String> messages) {
                                    data.clear();
                                    data.addAll(messages);
                                    dialogChoseProjectAdapter.notifyDataSetChanged();

                                }
                            });
                } else {

                    String s = keyword.getText().toString();
                    if (s.isEmpty()) {
                        ArmsUtils.snackbarText(mRootView.getActivity().getString(R.string.please_enter_keyword));
                    } else {
                        isSeach = true;
                        mModel.loadLocaProject(s)
                                .subscribeOn(Schedulers.io())
                                .retryWhen(new RetryWithDelay(2, 1))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                                .doOnSubscribe(disposable -> {
                                    mRootView.showLoading();
                                }).subscribeOn(AndroidSchedulers.mainThread())
                                .doFinally(() -> {
                                    mRootView.hideLoading();
                                }).compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                                .subscribe(new ErrorHandleSubscriber<List<String>>(mErrorHandler) {
                                    @Override
                                    public void onNext(List<String> messages) {
                                        data.clear();
                                        data.addAll(messages);
                                        dialogChoseProjectAdapter.notifyDataSetChanged();

                                    }
                                });
                        btnseach.setText(mRootView.getActivity().getString(R.string.cancel));
                    }

                }
            }
        });


        builder.setView(view2);
        AlertDialog alertDialog = builder.create();
        dialogChoseProjectAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String s = data.get(position);
                mRootView.setChosedProject(s);
                alertDialog.dismiss();
            }
        });
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();

        Window window = alertDialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            int screenHeight = mRootView.getActivity().getResources().getDisplayMetrics().heightPixels;
            lp.height = (int) (screenHeight * 0.7);
            /*int screenWidth = mRootView.getActivity().getResources().getDisplayMetrics().widthPixels;
            lp.width = (int) (screenWidth * 0.9);*/
            window.setAttributes(lp);
        }
    }


}