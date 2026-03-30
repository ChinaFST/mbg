package com.dy.colony.mvp.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.apkfuns.logutils.LogUtils;
import com.dy.colony.Constants;
import com.dy.colony.R;
import com.dy.colony.di.component.DaggerEditorProjectComponent;
import com.dy.colony.greendao.DBHelper;
import com.dy.colony.greendao.beans.FGGDTestItem;
import com.dy.colony.greendao.beans.JTJTestItem;
import com.dy.colony.greendao.daos.FGGDTestItemDao;
import com.dy.colony.greendao.daos.JTJTestItemDao;
import com.dy.colony.mvp.contract.EditorProjectContract;
import com.dy.colony.mvp.model.entity.UpdateFileMessage;
import com.dy.colony.mvp.model.entity.base.BaseProjectMessage;
import com.dy.colony.mvp.presenter.EditorProjectPresenter;
import com.dy.colony.mvp.ui.adapter.EdtorProjectMessageAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.greenrobot.greendao.query.QueryBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import dmax.dialog.SpotsDialog;

import static com.jess.arms.utils.Preconditions.checkNotNull;

public class EditorProjectActivity extends BaseActivity<EditorProjectPresenter> implements EditorProjectContract.View {
    @BindView(R.id.toolbar_back)
    RelativeLayout mToolbarBack;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbarly)
    AppBarLayout mToolbarly;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @Inject
    RecyclerView.LayoutManager mLayoutManager;

    List<BaseProjectMessage> mData = new ArrayList<>();

    AlertDialog mSportDialog;
    @BindView(R.id.search_view)
    MaterialSearchView mSearchView;
    @BindView(R.id.toolbar_container)
    FrameLayout mToolbarContainer;


    private String mFrom;
    EdtorProjectMessageAdapter mAdapter;
    private boolean isSeaching;
    private String keyWord;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerEditorProjectComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    protected void onRestart() {
        LogUtils.d("onRestart");
        RefreshList();

        super.onRestart();
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_editorproject; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mFrom = getIntent().getStringExtra("from");
        initRecyclerView();

        mAdapter = new EdtorProjectMessageAdapter(R.layout.project_item_layout, mData, getActivity());
        mAdapter.setEmptyView(R.layout.emptyview, (ViewGroup) mRecyclerView.getParent());
        mRecyclerView.setAdapter(mAdapter);
        loadData();
        initSearcchView();
    }

    private void initSearcchView() {
        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                loadData(query);
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

    private void loadData(String... key) {
        mData.clear();
        if ("fggd".equals(mFrom)) {
            mToolbarTitle.setText(getString(R.string.moudleproject_fggd));
            FGGDTestItemDao dao = DBHelper.getFGGDTestItemDao(getActivity());
            if (key.length > 0) {
                mData.addAll(dao.queryBuilder().where(FGGDTestItemDao.Properties.Project_name.like("%" + key[0] + "%")).list());
            } else {
                mData.addAll(dao.loadAll());
            }

        } else if ("jtj".equals(mFrom)) {
            mToolbarTitle.setText(getString(R.string.moudleproject_jtj));
            //查看当前的胶体金模块是扫描还是摄像头，然后再去加载相应的检测项目
            checkLinkModelType(key);
        }
        mAdapter.notifyDataSetChanged();
    }


    private void checkLinkModelType(String... keyword) {
        List<JTJTestItem> jtjTestItems = null;
        QueryBuilder<JTJTestItem> builder = DBHelper.getJTJTestItemDao(getActivity()).queryBuilder();
        if (keyword.length > 0) {
            builder = builder.where(JTJTestItemDao.Properties.ProjectName.like("%" + keyword[0] + "%"));
        }
        jtjTestItems = builder.where(JTJTestItemDao.Properties.Item_type.eq("2")).list();

        mData.addAll(jtjTestItems);

    }


    private void initRecyclerView() {
        ArmsUtils.configRecyclerView(mRecyclerView, mLayoutManager);
    }

    @Override
    public void showLoading() {
       /* if (mSportDialog != null && !mSportDialog.isShowing()) {
            mSportDialog.show();
        }*/
    }

    @Override
    public void hideLoading() {
       /* if (mSportDialog != null && mSportDialog.isShowing()) {
            mSportDialog.dismiss();
        }*/
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
    public void RefreshList() {
        LogUtils.d(mFrom);
        if ("fggd".equals(mFrom)) {
            mData.clear();
            List<FGGDTestItem> items = DBHelper.getFGGDTestItemDao(getActivity()).loadAll();
            mData.addAll(items);
        } else if (mFrom.contains("jtj")) {
            mData.clear();
            QueryBuilder<JTJTestItem> builder = DBHelper.getJTJTestItemDao(getActivity()).queryBuilder();
            List<JTJTestItem> jtjTestItems = builder.where(JTJTestItemDao.Properties.Item_type.eq("4")).list();
            LogUtils.d(jtjTestItems);
            mData.addAll(jtjTestItems);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void makeDialogNewVersion(String filename, String local, String from, String linkurl, UpdateFileMessage message) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LogUtils.d("makeDialogNewVersion");
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
                builder.setPositiveButton(getString(R.string.download), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.downLoadProject(filename, from, linkurl);

                    }
                });
                builder.setNeutralButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setTitle(getString(R.string.projectversionnmessage))
                        .setMessage(getString(R.string.serviceversion) + filename + "\r\n" + getString(R.string.localversion) + (local.equals("") ? getString(R.string.whihoutnewversion) : local))
                        //.setIcon(R.mipmap.ic)
                        .setCancelable(false);
                androidx.appcompat.app.AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(true);//设置弹出框失去焦点是否隐藏
                dialog.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (isSeaching) {
            isSeaching = false;
            keyWord = "";
            loadData();
        } else {
            if (mSearchView.isSearchOpen()) {
                mSearchView.closeSearch();
            } else {
                super.onBackPressed();
            }
        }

    }

    @Override
    public void inputProject(File file, String filename, String from) {
        List<String> list = new ArrayList<>();
        list.add(file.getAbsolutePath());
        if ("fggd".equals(from)) {
            if (mData.isEmpty()) {
                //mPresenter.inPutFGGDItem(list, true);

            } else {
                //mPresenter.makeCheckDialog(list, from);

            }
        } else if (from.contains("jtj")) {
            if (mData.isEmpty()) {
                //mPresenter.inPutJTJitem(list, true);

            } else {
                //mPresenter.makeCheckDialog(list, from);

            }
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.editor_project_toobar_menu, menu);
        MenuItem item = menu.findItem(R.id.menu_seach);
        mSearchView.setMenuItem(item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.menu_new_testitem:
                if ("fggd".equals(mFrom)) {
                    launchActivity(new Intent(getActivity(), FGGD_NewTestItemActivity.class));
                } else if ("jtj".equals(mFrom)) {
                    launchActivity(new Intent(getActivity(), JTJ_NewTestItemActivity.class));
                }

                break;
            case R.id.menu_input_testitem:
                //JxlUtils.openFilechose_File(getActivity());
                break;
            case R.id.menu_output_testitem:
                //JxlUtils.openFilechose_Folder(getActivity());
                break;

            case R.id.action_updata:
                mPresenter.checkNewVersion(mFrom);
                break;
            default:
                break;
        }

        return true;
    }
}