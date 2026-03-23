package com.dy.colony.mvp.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.apkfuns.logutils.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dy.colony.mvp.model.entity.base.BaseSimple33Message;
import com.dy.colony.mvp.ui.adapter.ChoseSimple33Adapter;
import com.google.android.material.appbar.AppBarLayout;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.dy.colony.di.component.DaggerChoseSampleTypeComponent;
import com.dy.colony.mvp.contract.ChoseSampleTypeContract;
import com.dy.colony.mvp.presenter.ChoseSampleTypePresenter;

import com.dy.colony.R;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;

import static com.jess.arms.utils.Preconditions.checkNotNull;

public class ChoseSampleTypeActivity extends BaseActivity<ChoseSampleTypePresenter> implements ChoseSampleTypeContract.View {
    @BindView(R.id.toolbar_back)
    RelativeLayout mToolbarBack;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbarly)
    AppBarLayout mToolbarly;
    @BindView(R.id.linearLayout34)
    LinearLayout mLinearLayout34;
    @BindView(R.id.food33)
    RecyclerView mFood33;

    @Inject
    AlertDialog mSportDialog;
    @Inject
    AlertDialog.Builder mAlertDialog;
    @Inject
    RecyclerView.LayoutManager mManager;

    @Inject
    ChoseSimple33Adapter mAdapter;
    @Inject
    List<BaseSimple33Message> mList;


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BaseSimple33Message baseSimple33Message) {
        Intent data = new Intent();
        Bundle extras = new Bundle();
        extras.putParcelable("data", baseSimple33Message);
        data.putExtras(extras);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerChoseSampleTypeComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_chosesampletype; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        initRecycleView();
        mPresenter.loadData();
    }

    private void initRecycleView() {
        ArmsUtils.configRecyclerView(mFood33, mManager);
        mAdapter.setEmptyView(R.layout.emptyview, (ViewGroup) mFood33.getParent());
        mFood33.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                BaseSimple33Message message = mAdapter.getData().get(position);
                mPresenter.checkData(message);
            }
        });
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void showSportDialog(String title) {
        LogUtils.d(title);
        mSportDialog = new SpotsDialog.Builder().setContext(getActivity()).build();
        mSportDialog.setMessage((title));
        mSportDialog.show();
    }

    @Override
    public void hideSportDialog() {
        if (mSportDialog != null) {
            if (mSportDialog.isShowing()) {
                mSportDialog.dismiss();
            }
        }
    }

    @Override
    public void settitle(String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // mToolbarTitle.setText(s);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.chosefood33_toobar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {

            case R.id.menu_backleve_testitem:
                mPresenter.backleve();
                break;
        }

        return true;
    }
}