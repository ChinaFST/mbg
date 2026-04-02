package com.dy.colony.mvp.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.apkfuns.logutils.LogUtils;
import com.dy.colony.Constants;
import com.dy.colony.R;
import com.dy.colony.app.utils.DataUtils;
import com.dy.colony.app.utils.JxlUtils;
import com.dy.colony.di.component.DaggerSampleMessageComponent;
import com.dy.colony.mvp.contract.SampleMessageContract;
import com.dy.colony.mvp.model.entity.base.BaseSampleMessage;
import com.dy.colony.mvp.presenter.SampleMessagePresenter;
import com.dy.colony.mvp.ui.adapter.SampleMessageAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.paginate.Paginate;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import dmax.dialog.SpotsDialog;

import static com.jess.arms.utils.Preconditions.checkNotNull;

public class SampleMessageActivity extends BaseActivity<SampleMessagePresenter> implements SampleMessageContract.View, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.toolbar_back)
    RelativeLayout mToolbarBack;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbarly)
    AppBarLayout mToolbarly;


    @BindView(R.id.relativeLayout8)
    LinearLayout mRelativeLayout8;
    @BindView(R.id.lv)
    RecyclerView mLv;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.showpage)
    TextView mShowpage;
    @BindView(R.id.search_view)
    MaterialSearchView mSearchView;
    @BindView(R.id.toolbar_container)
    FrameLayout mToolbarContainer;
    @BindView(R.id.group)
    LinearLayout mGroup;

    @Inject
    RecyclerView.LayoutManager mLayoutManager;

    AlertDialog mSportDialog;
    @Inject
    SampleMessageAdapter mAdapter;
    @Inject
    List<BaseSampleMessage> mList;

    @Inject
    AlertDialog.Builder mAlertDialog;

    public boolean isSeaching = false;
    public String keyWord;
    private Paginate mPaginate;
    private boolean isLoadingMore;
    private boolean hasLoadedAllItems;

    @Override
    protected void onRestart() {
        super.onRestart();
        mPresenter.lodaMore(true);
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerSampleMessageComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_samplemessage; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        initRecycle();
        mAdapter.setEmptyView(R.layout.emptyview, (ViewGroup) mLv.getParent());
        mLv.setAdapter(mAdapter);
        initPaginate();
        initSearcchView();
    }

    private void initPaginate() {
        if (mPaginate == null) {
            Paginate.Callbacks callbacks = new Paginate.Callbacks() {
                @Override
                public void onLoadMore() {
                    LogUtils.d("onLoadMore");
                    if (isLoadingMore) {
                        return;
                    }
                    if (isSeaching) {
                        mPresenter.seach(keyWord, false);
                    } else {
                        mPresenter.lodaMore(false);
                    }

                }

                @Override
                public boolean isLoading() {
                    LogUtils.d("isLoading");
                    return isLoadingMore;
                }

                @Override
                public boolean hasLoadedAllItems() {
                    LogUtils.d("hasLoadedAllItems");
                    return hasLoadedAllItems;
                }
            };

            mPaginate = Paginate.with(mLv, callbacks)
                    .setLoadingTriggerThreshold(0)
                    .build();
            mPaginate.setHasMoreDataToLoad(false);
        }
    }


    private void initRecycle() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        ArmsUtils.configRecyclerView(mLv, mLayoutManager);
        mLv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        //触摸时操作
                        mShowpage.setVisibility(View.VISIBLE);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        //移动时操作
                        mShowpage.setVisibility(View.VISIBLE);
                        break;
                    case MotionEvent.ACTION_UP:
                        //离开时操作
                        mShowpage.setVisibility(View.GONE);
                        break;
                }
                return false;
            }
        });
    }


    private void initSearcchView() {

        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mPresenter.seach(query, true);
                isSeaching = true;
                keyWord = query;
                mToolbarTitle.setText(String.format(getString(R.string.seachresult), keyWord));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                LogUtils.d(newText);
                return false;
            }
        });

        mSearchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                LogUtils.d("onSearchViewShown");
                mSearchView.setQuery(keyWord, false);
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
                LogUtils.d("onSearchViewClosed");
            }
        });

    }

    //主动调用刷新
    @Override
    public void onRefreshList() {
        if (isSeaching) {
            mPresenter.seach(keyWord, true);
        } else {
            mPresenter.lodaMore(true);
        }
    }


    //控件触发刷新
    @Override
    public void onRefresh() {

        if (isSeaching) {
            mPresenter.seach(keyWord, true);
        } else {
            mPresenter.lodaMore(true);
        }

    }

    @Override
    public void showLoading() {

        mSwipeRefreshLayout.setRefreshing(true);

    }

    @Override
    public void hideLoading() {

        mSwipeRefreshLayout.setRefreshing(false);

    }

    /**
     * 开始加载更多
     */
    @Override
    public void startLoadMore() {
        isLoadingMore = true;
    }

    /**
     * 结束加载更多
     */
    @Override
    public void endLoadMore() {
        isLoadingMore = false;
    }

    @Override
    public void showAlertDialog() {
        if (!mSportDialog.isShowing()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mSportDialog.show();
                }
            });
        }
    }

    @Override
    public void hindAlertDialog() {
        if (mSportDialog.isShowing()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mSportDialog.dismiss();
                }
            });
        }
    }

    @Override
    public void setShowPgeText(String s) {
        mShowpage.setText(s + "");
    }

    @Override
    public void sethasLoadedAllItemstrue() {
        hasLoadedAllItems = true;
        View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.footview_layout, null);
        mAdapter.removeAllFooterView();
        mAdapter.addFooterView(inflate);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showSportDialog(String title) {
        //尝试先消除上次的dialog 避免弹框不消失
        if (mSportDialog != null) {
            if (mSportDialog.isShowing()) {
                mSportDialog.dismiss();
            }
        }
        LogUtils.d(title);
        mSportDialog = new SpotsDialog.Builder().setContext(getActivity()).build();
        mSportDialog.setMessage(title);
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
    public void sethasLoadedAllItemsfase() {
        hasLoadedAllItems = false;


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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.sample_toobar_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        mSearchView.setMenuItem(item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.menu_new_testitem:
                launchActivity(new Intent(getActivity(), EdtorSampleActivity.class).putExtra("data", 1));
                break;
            case R.id.menu_output_testitem:
                if (mAdapter.getData().isEmpty()) {
                    ArmsUtils.snackbarText(getString(R.string.nodatatoexport));
                } else {
                    JxlUtils.openFileChose_Folder(getActivity());
                }
                break;
            case R.id.menu_input_testitem:
                JxlUtils.openFileChose_File(getActivity());
                break;

        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == Constants.FOLDER_REQUESTCODE) {
            //文件夹选择模式，需要获取选择的文件夹路径 导出
            String path = data.getStringExtra("path");
            mPresenter.outPutJxl_other(path, getString(R.string.sampleandproject) + DataUtils.getFIleNameNowtimeyyymmddhhmmss() + ".xls", getString(R.string.sampleandproject));
        } else if (requestCode == Constants.FILE_REQUESTCODE) {
            List<String> list = data.getStringArrayListExtra("paths");
            LogUtils.d(list);

            if (mList.isEmpty()) { //导入
                mPresenter.inPutJxl(list, true);
            } else {
                makeChoseDialog(list);//当前数据集如果不是空的弹框提示用户是否需要保留当前数据
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void makeChoseDialog(List<String> list) {
        mAlertDialog.setTitle(getString(R.string.hint));
        mAlertDialog.setMessage(R.string.skipsamplemessage);
        mAlertDialog.setPositiveButton(R.string.replace, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //导入检测项目
                mPresenter.inPutJxl(list, false);
            }
        });
        mAlertDialog.setNegativeButton(R.string.keep, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //导入检测项目
                mPresenter.inPutJxl(list, true);

            }
        });
        mAlertDialog.show();
    }

    @Override
    public Activity getActivity() {
        return this;
    }


    @Override
    public void onBackPressed() {
        if (isSeaching) {
            isSeaching = false;
            keyWord = "";
            mPresenter.lodaMore(true);
            mToolbarTitle.setText(R.string.title_samplemessage);
        } else {
            if (mSearchView.isSearchOpen()) {
                mSearchView.closeSearch();
            } else {
                super.onBackPressed();
            }
        }

    }
}