package com.dy.colony.mvp.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.apkfuns.logutils.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dy.colony.MyAppLocation;
import com.dy.colony.app.utils.DiaLogUtils;
import com.dy.colony.greendao.DBHelper;
import com.dy.colony.greendao.beans.FGGDTestItem;
import com.dy.colony.greendao.beans.JTJTestItem;
import com.dy.colony.mvp.model.entity.base.BaseProjectMessage;
import com.dy.colony.mvp.model.entity.base.GalleryBean;
import com.dy.colony.mvp.ui.adapter.ChoseProjectMessageAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.dy.colony.di.component.DaggerChoseProjectComponent;
import com.dy.colony.mvp.contract.ChoseProjectContract;
import com.dy.colony.mvp.presenter.ChoseProjectPresenter;

import com.dy.colony.R;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;

public class ChoseProjectActivity extends BaseActivity<ChoseProjectPresenter> implements ChoseProjectContract.View {
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
    @BindView(R.id.parent_gallery)
    LinearLayout mParentGallery;
    @BindView(R.id.sc_gallery)
    HorizontalScrollView mScGallery;
    @Inject
    AlertDialog mSportDialog;
    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    List<BaseProjectMessage> mTestItemList;
    @Inject
    ChoseProjectMessageAdapter mAdapter;
    @BindView(R.id.cb_all)
    CheckBox mCbAll;
    @BindView(R.id.sp01)
    Spinner mSp01;
    @BindView(R.id.sp02)
    Spinner mSp02;
    @BindView(R.id.sp03)
    Spinner mSp03;
    @BindView(R.id.btn_determine)
    Button mBtnDetermine;
    @BindView(R.id.linearLayout5)
    LinearLayout mLinearLayout5;
    @BindView(R.id.search_view)
    MaterialSearchView mSearchView;
    @BindView(R.id.toolbar_container)
    FrameLayout mToolbarContainer;
    private Map<Integer, CheckBox> mMap = new HashMap<>();
    private String mFrom;
    private int mIndex;
    private boolean isSeaching;
    private String keyWord;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerChoseProjectComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_choseproject; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        Intent intent = getIntent();
        mFrom = intent.getStringExtra("from");
        mIndex = getIntent().getIntExtra("index", 0);
        initRecyclerView();
        initGallery();
        LogUtils.d(mFrom);
        mPresenter.loadProject(mFrom);
        mAdapter.setEmptyView(R.layout.recycle_empty_layout, (ViewGroup) mRecyclerView.getParent());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //选择了检测项目
                LogUtils.d("选择了检测项目");
                if ("fggd".equals(mFrom)) {
                    checkChecked(mAdapter.getData().get(position), MyAppLocation.myAppLocation.mSerialDataService.mFGGDGalleryBeanList);
                } else if (mFrom.contains("jtj")) {
                    checkChecked(mAdapter.getData().get(position), MyAppLocation.myAppLocation.mSerialDataService.mJTJGalleryBeanList);
                }
                finish();

            }
        });

        initCheckBox();

        initSearcchView();
    }

    private void initRecyclerView() {
        ArmsUtils.configRecyclerView(mRecyclerView, mLayoutManager);
    }
    private void initGallery() {
        List<GalleryBean> list;
        if ("fggd".equals(mFrom)) {
            list = MyAppLocation.myAppLocation.mSerialDataService.mFGGDGalleryBeanList;
            for (int i = 0; i < list.size(); i++) {
                GalleryBean bean = list.get(i);
                int num = bean.getGalleryNum();
                // LogUtils.d(num + "--" + mIndex);
                if (bean.getState() != 1) {
                    CheckBox box = new CheckBox(getActivity());
                    box.setPadding(0, 10, 10, 10);
                    box.setText(getString(R.string.channel_label, num ));
                    if (mIndex == num) {
                        box.setChecked(true);
                        box.setEnabled(false);
                    }
                    mParentGallery.addView(box);
                    mMap.put(num, box);
                }
            }
        } else if (mFrom.contains("jtj")) {
            list = MyAppLocation.myAppLocation.mSerialDataService.mJTJGalleryBeanList;
            //mIndex 分光模块和干式农残 传过来的是通道序号 胶体金传过来的是通道列表的下标（通道名称可能会存在一样或者不连贯 1，3 or 2，4 or 1，1）
            //必须是相同的模块。不同模块的检测项目参数是不一样的 判定标准也不一样 不能选错 （模块混合使用的情况）
            //LogUtils.d(list);
            GalleryBean bean = list.get(mIndex);
            int model = bean.getJTJModel();
            int cardModel = bean.getJTJCardModel();
            for (int i = 0; i < list.size(); i++) {
                GalleryBean bean1 = list.get(i);
                if (model == bean1.getJTJModel() && cardModel == bean1.getJTJCardModel() && bean1.getState() != 1) {
                    /*if (bean.getJTJCardModel()==3){
                      continue;
                    }*/
                    CheckBox box = new CheckBox(getActivity());
                    box.setPadding(0, 10, 10, 10);
                    int gallery = bean1.getGalleryNum();
                    box.setText(getString(R.string.channel_label, gallery ));
                    //box.setText("通道" + gallery);

                    if (i == mIndex) {
                        box.setChecked(true);
                        box.setEnabled(false);
                    }
                    mParentGallery.addView(box);
                    mMap.put(i, box);
                }


            }


        }


        //LogUtils.d(mMap);
        if (mMap.size() < 2) {
            mScGallery.setVisibility(View.GONE);
        }
    }

    private void checkChecked(BaseProjectMessage item, List<GalleryBean> list) {
        LogUtils.d(item);
        if (item instanceof FGGDTestItem) {
            FGGDTestItem item1 = (FGGDTestItem) item;
            item1.setPriority(System.currentTimeMillis());
            DBHelper.getFGGDTestItemDao(getActivity()).update(item1);
        } else if (item instanceof JTJTestItem) {
            JTJTestItem item1 = (JTJTestItem) item;
            item1.setPriority(System.currentTimeMillis());
            DBHelper.getJTJTestItemDao(getActivity()).update(item1);
        }

        for (int key : mMap.keySet()) {
            CheckBox box = mMap.get(key);
            if (box.isChecked()) {
                GalleryBean bean;
                if (mFrom.contains("jtj") || mFrom.contains("myyg")|| mFrom.contains("external")) {
                    bean = list.get(key);
                } else {
                    bean = list.get(key - 1);
                }

                bean.setProjectMessage(item);
            }
        }
    }

    private void initCheckBox() {
        mCbAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                for (int key : mMap.keySet()) {
                    CheckBox box = mMap.get(key);
                    if (box.isEnabled()) {
                        box.setChecked(isChecked);
                    }

                }

            }
        });

    }

    private void initSearcchView() {
        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mPresenter.loadProject(mFrom,query);
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
                mSearchView.setQuery(keyWord, false);
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
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
    public void onBackPressed() {
        if (isSeaching) {
            isSeaching = false;
            keyWord = "";
            mPresenter.loadProject(mFrom);
            mToolbarTitle.setText(R.string.ChoseProjectActivity);
        } else {
            if (mSearchView.isSearchOpen()) {
                mSearchView.closeSearch();
            } else {
                super.onBackPressed();
            }
        }

    }
}