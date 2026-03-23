package com.dy.colony.mvp.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.apkfuns.logutils.LogUtils;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.dy.colony.MyAppLocation;
import com.dy.colony.greendao.DBHelper;
import com.dy.colony.greendao.beans.Company_Point;
import com.dy.colony.greendao.beans.Company_Point_Unit;
import com.dy.colony.greendao.beans.Detection_Record_FGGD_NC;
import com.dy.colony.greendao.daos.Company_PointDao;
import com.dy.colony.mvp.model.entity.base.BaseUntilMessage;
import com.dy.colony.mvp.model.entity.base.GalleryBean;
import com.dy.colony.mvp.model.entity.eventbus.UnitsMessageBean;
import com.dy.colony.mvp.ui.adapter.ChoseUnitsMessageAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.dy.colony.di.component.DaggerChoseUnitComponent;
import com.dy.colony.mvp.contract.ChoseUnitContract;
import com.dy.colony.mvp.presenter.ChoseUnitPresenter;

import com.dy.colony.R;
import com.paginate.Paginate;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import dmax.dialog.SpotsDialog;

import static com.jess.arms.utils.Preconditions.checkNotNull;

public class ChoseUnitActivity extends BaseActivity<ChoseUnitPresenter> implements ChoseUnitContract.View, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.toolbar_back)
    RelativeLayout mToolbarBack;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbarly)
    AppBarLayout mToolbarly;
    @BindView(R.id.cb_all)
    CheckBox mCbAll;
    @BindView(R.id.parent_gallery)
    LinearLayout mParentGallery;
    @BindView(R.id.sc_gallery)
    HorizontalScrollView mScGallery;
    @BindView(R.id.chose_units_recycle)
    RecyclerView mChoseUnitsRecycle;
    /*@BindView(R.id.search_view)
    MaterialSearchView mSearchView;*/
    @BindView(R.id.toolbar_container)
    FrameLayout mToolbarContainer;

    @BindView(R.id.seachotherpla_layout)
    LinearLayout mSeachOtherplaLayout;
    @BindView(R.id.keyword)
    AutoCompleteTextView mKeyword;
    @BindView(R.id.btn_seach)
    Button mBtnSeach;
    AlertDialog mSportDialog;

    @Inject
    List<MultiItemEntity> res;
    @Inject
    ChoseUnitsMessageAdapter mAdapter;
    @Inject
    GridLayoutManager manager;
    @BindView(R.id.re_swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private String mFrom;
    private int mIndex;
    private Map<Integer, CheckBox> mMap = new HashMap<>();
    private boolean isSeaching = false;
    private boolean isSeaching_online = false;
    private String keyWord = "";
    private Paginate mPaginate;
    private boolean isLoadingMore;
    private boolean hasLoadedAllItems = false;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerChoseUnitComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_choseunit; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

        Intent intent = getIntent();
        mFrom = intent.getStringExtra("from");
        mIndex = intent.getIntExtra("index", -1); //其实是通道号，当下标用需要-1
        initBase();

        initPaginate();
        initCheckBox();
        initSearcchView();
        mPresenter.loadMore(true);
    }

    private void initBase() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        ArmsUtils.configRecyclerView(mChoseUnitsRecycle, manager);
        mAdapter.setEmptyView(R.layout.recycle_empty_layout, (ViewGroup) mChoseUnitsRecycle.getParent());
        mChoseUnitsRecycle.setAdapter(mAdapter);

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
                    if (isSeaching_online) {
                        return;
                    }
                    if (isSeaching) {
                        mPresenter.seach(keyWord, false);

                    } else {
                        mPresenter.loadMore(false );
                    }


                }

                @Override
                public boolean isLoading() {
                    //LogUtils.d("isLoading"+isLoadingMore);
                    return isLoadingMore;
                }

                @Override
                public boolean hasLoadedAllItems() {
                    // LogUtils.d("hasLoadedAllItems");
                    return hasLoadedAllItems;
                }
            };

            mPaginate = Paginate.with(mChoseUnitsRecycle, callbacks)
                    .setLoadingTriggerThreshold(0)
                    .build();
            mPaginate.setHasMoreDataToLoad(false);
        }
    }

    private void initCheckBox() {
        if ("fggd".equals(mFrom)) {
            List<GalleryBean> list = MyAppLocation.myAppLocation.mSerialDataService.mFGGDGalleryBeanList;
            initGallery(list);
        } else if ("jtj".equals(mFrom)) {
            List<GalleryBean> list = MyAppLocation.myAppLocation.mSerialDataService.mJTJGalleryBeanList;
            initGallery(list);
        }
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

    private void initGallery(List<GalleryBean> list) {
        if (mFrom.contains("jtj")) {
            //list = MyAppLocation.myAppLocation.mSerialDataService.mJTJGalleryBeanList;
            //mIndex 分光模块和干式农残 传过来的是通道序号 胶体金传过来的是通道列表的下标（通道名称可能会存在一样或者不连贯 1，3 or 2，4 or 1，1）
            //必须是相同的模块。不同模块的检测项目参数是不一样的 判定标准也不一样 不能选错 （模块混合使用的情况）
            //LogUtils.d(list);
            GalleryBean bean = list.get(mIndex);
            int model = bean.getJTJModel();
            for (int i = 0; i < list.size(); i++) {
                GalleryBean bean1 = list.get(i);
                if (model == bean1.getJTJModel() && bean1.getState() != 1) {

                    CheckBox box = new CheckBox(getActivity());
                    box.setPadding(0, 10, 10, 10);
                    int gallery = bean1.getGalleryNum();
                    //hole
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
            for (int i = 0; i < list.size(); i++) {
                GalleryBean bean = list.get(i);
                int num = bean.getGalleryNum();
                if (bean.getState() != 1) {
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

    private void initSearcchView() {
        /*mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mPresenter.seach(query, true);
                isSeaching = true;
                keyWord = query;
                mToolbarTitle.setText(String.format(getString(R.string.seachresult),keyWord));
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
        });*/
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UnitsMessageBean bean) {
        BaseUntilMessage untilMessage = null;
        switch (bean.tag) {
            case 5:
                if (null != bean.mCompany_point_down) {
                    untilMessage = bean.mCompany_point_down;
                } else {
                    ArmsUtils.snackbarText("EventBus参数传递错误");
                }

                break;
            case 6:
                if (null != bean.mCompany_point_unit_down) {
                    untilMessage = bean.mCompany_point_unit_down;
                } else {
                    ArmsUtils.snackbarText("EventBus参数传递错误");
                }
                break;
        }
        if (null == untilMessage) {
            return;
        }
        LogUtils.d(mFrom);

        switch (mFrom) {
            case "fggd":
                setCheckUnits(untilMessage, MyAppLocation.myAppLocation.mSerialDataService.mFGGDGalleryBeanList);
                break;
            case "jtj":
                setCheckUnits(untilMessage, MyAppLocation.myAppLocation.mSerialDataService.mJTJGalleryBeanList);
                break;

        }
    }


    private void setCheckUnits(BaseUntilMessage down, List<GalleryBean> galleryBeans) {
        LogUtils.d(down);
        if (down instanceof Company_Point) {
            Company_Point down1 = (Company_Point) down;
            down1.setPriority(System.currentTimeMillis());
            DBHelper.getCompany_PointDao(getActivity()).update(down1);


        } else if (down instanceof Company_Point_Unit) {
            Company_Point_Unit down1 = (Company_Point_Unit) down;
            down1.setPriority(System.currentTimeMillis());
            DBHelper.getCompany_Point_UnitDao(getActivity()).update(down1);
            String id = down1.getRegId();
            Company_PointDao dao = DBHelper.getCompany_PointDao(getActivity());
            List<Company_Point> list = dao.queryBuilder().where(Company_PointDao.Properties.RegId.eq(id)).list();
            if (!list.isEmpty()) {
                Company_Point point = list.get(0);
                point.setPriority(System.currentTimeMillis());
                dao.update(point);
            }
        }

        if (mFrom.contains("jtj")) {
            LogUtils.d(mMap.keySet());
            for (int key : mMap.keySet()) {
                CheckBox box = mMap.get(key);
                if (box.isChecked()) {
                    GalleryBean bean = galleryBeans.get(key);
                    bean.setUntilMessage(down);
                }
            }


        } else {
            for (int key : mMap.keySet()) {
                CheckBox box = mMap.get(key);
                if (box.isChecked()) {
                    GalleryBean bean = galleryBeans.get(key - 1);

                    bean.setUntilMessage(down);

                }
            }
        }


        killMyself();
    }

    @Override
    public void sethasLoadedAllItemstrue() {
        hasLoadedAllItems = true;
        View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.footview_layout, null);
        TextView id = (TextView) inflate.findViewById(R.id.showmessage);
        id.setText(getString(R.string.nomore_data));
        mAdapter.removeAllFooterView();
        mAdapter.addFooterView(inflate);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void sethasLoadedAllItemsfase() {
        hasLoadedAllItems = false;


    }

    /**
     * 开始加载更多
     */
    @Override
    public void startLoadMore() {
        isLoadingMore = true;
    }

    @Override
    public void showLoading() {
        // LogUtils.d("showLoading");
        mSwipeRefreshLayout.setRefreshing(true);

    }

    @Override
    public void hideLoading() {

        mSwipeRefreshLayout.setRefreshing(false);

    }

    /**
     * 结束加载更多
     */
    @Override
    public void endLoadMore() {
        isLoadingMore = false;
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
    public void onRefresh() {
        if (isSeaching) {
            mPresenter.seach(keyWord, true);
        } else {
            mPresenter.loadMore(true);
        }

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
}