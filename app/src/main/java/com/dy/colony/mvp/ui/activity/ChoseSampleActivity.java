package com.dy.colony.mvp.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.apkfuns.logutils.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dy.colony.Constants;
import com.dy.colony.MyAppLocation;
import com.dy.colony.greendao.DBHelper;
import com.dy.colony.greendao.beans.Detection_Record_FGGD_NC;
import com.dy.colony.greendao.beans.FoodItemAndStandard;
import com.dy.colony.mvp.model.entity.base.BaseProjectMessage;
import com.dy.colony.mvp.model.entity.base.BaseSampleMessage;
import com.dy.colony.mvp.model.entity.base.BaseUntilMessage;
import com.dy.colony.mvp.model.entity.base.GalleryBean;
import com.dy.colony.mvp.ui.adapter.ChooseSampleAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.dy.colony.di.component.DaggerChoseSampleComponent;
import com.dy.colony.mvp.contract.ChoseSampleContract;
import com.dy.colony.mvp.presenter.ChoseSamplePresenter;

import com.dy.colony.R;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.paginate.Paginate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;

public class ChoseSampleActivity extends BaseActivity<ChoseSamplePresenter> implements ChoseSampleContract.View {

    @BindView(R.id.toolbar_back)
    RelativeLayout mToolbarBack;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.search_view)
    MaterialSearchView mSearchView;
    @BindView(R.id.toolbar_container)
    FrameLayout mToolbarContainer;
    @BindView(R.id.toolbarly)
    AppBarLayout mToolbarly;
    @BindView(R.id.cb_all)
    CheckBox mCbAll;
    @BindView(R.id.parent_gallery)
    LinearLayout mParentGallery;
    @BindView(R.id.sc_gallery)
    HorizontalScrollView mScGallery;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.showpage)
    TextView mShowpage;

    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    ChooseSampleAdapter mAdapter;
    @Inject
    AlertDialog mAlertDialog;
    @Inject
    List<BaseUntilMessage> mBaseUntilMessage;
    private String mFrom;
    private int mIndex;
    private Map<Integer, CheckBox> mMap = new HashMap<>();
    public boolean isSeaching = false;
    public String keyWord;
    private Paginate mPaginate;
    private boolean isLoadingMore;
    private boolean hasLoadedAllItems = false;
    private String mProjectName = "";

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerChoseSampleComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_chosesample; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        Intent intent = getIntent();
        mFrom = intent.getStringExtra("from");
        mIndex = intent.getIntExtra("index", -1); //其实是通道号，当下标用需要-1
        initRecyclerView();

        initCheckBox();
        initPaginate();
        initSearcchView();
        mPresenter.lodaMore(mProjectName, true, mFrom);
    }

    private void initRecyclerView() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isSeaching) {
                    mPresenter.seachFromFoodItemAndStand(keyWord, mProjectName, true, mFrom);
                } else {
                    mPresenter.lodaMore(mProjectName, true, mFrom);
                }

            }
        });
        mAdapter.setEmptyView(R.layout.recycle_empty_layout, (ViewGroup) mRecyclerview.getParent());
        mRecyclerview.setAdapter(mAdapter);
        ArmsUtils.configRecyclerView(mRecyclerview, mLayoutManager);
        mRecyclerview.setOnTouchListener(new View.OnTouchListener() {
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

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                switch (mFrom) {
                    case "fggd":
                        setCheckSampleName(mAdapter.getData().get(position), MyAppLocation.myAppLocation.mSerialDataService.mFGGDGalleryBeanList);
                        break;
                    case "jtj":
                        setCheckSampleName(mAdapter.getData().get(position), MyAppLocation.myAppLocation.mSerialDataService.mJTJGalleryBeanList);
                        break;
                }
                killMyself();
            }
        });
    }


    private void setCheckSampleName(BaseSampleMessage sample, List<GalleryBean> galleryBeans) {
        LogUtils.d(sample);
        //LogUtils.d(galleryBeans);
        //LogUtils.d(mMap);
        if (sample instanceof FoodItemAndStandard) {
            FoodItemAndStandard sample1 = (FoodItemAndStandard) sample;
            sample1.setPriority(System.currentTimeMillis());
            DBHelper.getFoodItemAndStandardDao(getActivity()).update(sample1);
        }
        LogUtils.d(mMap);
        for (int key : mMap.keySet()) {
            CheckBox box = mMap.get(key);
            if (box.isChecked()) {
                GalleryBean bean;
                if (mFrom.contains("jtj")) {
                    bean = galleryBeans.get(key);
                } else {
                    bean = galleryBeans.get(key - 1);
                }

                bean.setSampleMessage(sample);
                LogUtils.d(bean);
            }
        }

    }


    /**
     * @param list
     * @param name
     */
    private void initGallery(List<GalleryBean> list, String name) {
        LogUtils.d(list);
        LogUtils.d(name);
        if (mFrom.contains("jtj")) {
            //list = MyAppLocation.myAppLocation.mSerialDataService.mJTJGalleryBeanList;
            //mIndex 分光模块和干式农残 传过来的是通道序号 胶体金传过来的是通道列表的下标（通道名称可能会存在一样或者不连贯 1，3 or 2，4 or 1，1）
            //必须是相同的模块。不同模块的检测项目参数是不一样的 判定标准也不一样 不能选错 （模块混合使用的情况）
            //LogUtils.d(list);
            GalleryBean bean = list.get(mIndex);
            int model = bean.getJTJCardModel();
            for (int i = 0; i < list.size(); i++) {
                GalleryBean bean1 = list.get(i);
                String project = ((Detection_Record_FGGD_NC) bean1).getTest_project();

                if (model == bean1.getJTJCardModel() && bean1.getState() != 1 && project.equals(name)) {
                    CheckBox box = new CheckBox(getActivity());
                    box.setPadding(0, 10, 10, 10);
                    int gallery = bean1.getGalleryNum();

                    box.setText(getString(R.string.channel_label, gallery));
                    if (i == mIndex) {
                        box.setChecked(true);
                        box.setEnabled(false);
                    }
                    mParentGallery.addView(box);
                    mMap.put(i, box);
                }


            }
        } else {
            //list = MyAppLocation.myAppLocation.mSerialDataService.mZJSGalleryBeanList;
            for (int i = 0; i < list.size(); i++) {
                GalleryBean bean = list.get(i);
                int num = bean.getGalleryNum();
                // LogUtils.d(num + "--" + mIndex);
                String project = ((Detection_Record_FGGD_NC) bean).getTest_project();

                LogUtils.d(project);
                LogUtils.d(name);

                if (bean.getState() != 1 && project.equals(name)) {
                    CheckBox box = new CheckBox(getActivity());
                    box.setPadding(0, 10, 10, 10);
                    box.setText(getString(R.string.channel_label, num));
                    if (mIndex == num) {
                        box.setChecked(true);
                        box.setEnabled(false);
                    }
                    mParentGallery.addView(box);
                    mMap.put(num, box);
                }


            }
        }

        if (mMap.size() < 2) {
            mScGallery.setVisibility(View.GONE);
        }
    }

    private void initCheckBox() {
        List<GalleryBean> list = null;
        GalleryBean bean = null;
        if ("fggd".equals(mFrom)) {
            list = MyAppLocation.myAppLocation.mSerialDataService.mFGGDGalleryBeanList;
            bean = list.get(mIndex - 1);
            mBaseUntilMessage.clear();
            mBaseUntilMessage.add(bean.getUntilMessage());
            BaseProjectMessage message = bean.getProjectMessage();
            mProjectName = message.getProjectName();
        } else if ("jtj".equals(mFrom)) {
            list = MyAppLocation.myAppLocation.mSerialDataService.mJTJGalleryBeanList;
            //LogUtils.d(list);
            LogUtils.d(mIndex);
            bean = list.get(mIndex);
            mBaseUntilMessage.clear();
            mBaseUntilMessage.add(bean.getUntilMessage());
            //LogUtils.d(bean);

            BaseProjectMessage message = bean.getProjectMessage();
            mProjectName = message.getProjectName();

        }


        initGallery(list, mProjectName);


        mCbAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                LogUtils.d(mMap);
                LogUtils.d(mIndex);
                for (int key : mMap.keySet()) {
                    CheckBox box = mMap.get(key);
                    if (box.isEnabled()) {
                        box.setChecked(isChecked);
                    }

                }
            }
        });
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
                        mPresenter.seachFromFoodItemAndStand(keyWord, mProjectName, false, mFrom);
                    } else {
                        mPresenter.lodaMore(mProjectName, false, mFrom);
                    }

                }

                @Override
                public boolean isLoading() {
                    LogUtils.d("isLoading" + isLoadingMore);
                    return isLoadingMore;
                }

                @Override
                public boolean hasLoadedAllItems() {
                    LogUtils.d("hasLoadedAllItems" + hasLoadedAllItems);
                    return hasLoadedAllItems;
                }
            };

            mPaginate = Paginate.with(mRecyclerview, callbacks)
                    .setLoadingTriggerThreshold(0)
                    .build();
            mPaginate.setHasMoreDataToLoad(false);
        }
    }

    private void initSearcchView() {
        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                mPresenter.seachFromFoodItemAndStand(query, mProjectName, true, mFrom);


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

    @Override
    public void showLoading() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        mSwipeRefreshLayout.setRefreshing(false);
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
        if (!mAlertDialog.isShowing()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAlertDialog.show();
                }
            });
        }
    }

    @Override
    public void hindAlertDialog() {
        if (mAlertDialog.isShowing()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAlertDialog.dismiss();
                }
            });
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void setShowPgeText(String s) {
        mShowpage.setText(s + "");
    }

    @Override
    public void sethasLoadedAllItemstrue() {
        LogUtils.d("sethasLoadedAllItemstrue");
        hasLoadedAllItems = true;
        View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.footview_layout, null);
        TextView id = (TextView) inflate.findViewById(R.id.showmessage);
        id.setText(R.string.nomore_data);
        mAdapter.removeAllFooterView();
        mAdapter.addFooterView(inflate);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        mPresenter.lodaMore(mProjectName, true, mFrom);


    }

    @Override
    public void sethasLoadedAllItemsfase() {
        LogUtils.d("sethasLoadedAllItemsfase");
        hasLoadedAllItems = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.chosesample_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        MenuItem newitem = menu.findItem(R.id.action_new);
        newitem.setVisible(false);
        mSearchView.setMenuItem(item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.action_new:
                Intent intent = new Intent(getActivity(), EdtorSampleActivity.class);
                intent.putExtra("ProjectName", mProjectName);
                intent.putExtra("data", 1);
                launchActivity(intent);
                break;

        }

        return true;
    }

    @Override
    public void onBackPressed() {
        if (isSeaching) {
            isSeaching = false;
            keyWord = "";
            mPresenter.lodaMore(mProjectName, true, mFrom);
            mToolbarTitle.setText(R.string.ChoseSampleActivity);
        } else {
            if (mSearchView.isSearchOpen()) {
                mSearchView.closeSearch();
            } else {
                super.onBackPressed();
            }
        }

    }
}