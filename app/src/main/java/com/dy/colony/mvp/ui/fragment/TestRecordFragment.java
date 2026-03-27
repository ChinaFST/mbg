package com.dy.colony.mvp.ui.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;


import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.apkfuns.logutils.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dy.colony.Constants;
import com.dy.colony.greendao.beans.Detection_Record_FGGD_NC;
import com.dy.colony.mvp.contract.IBackPressed;
import com.dy.colony.mvp.ui.activity.TestRecordMessageActivity;
import com.dy.colony.mvp.ui.activity.TestRecordNewActivity;
import com.dy.colony.mvp.ui.adapter.TestRecordAdapter;
import com.dy.colony.mvp.ui.widget.MyDatePickerDialog;
import com.google.android.material.appbar.AppBarLayout;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.dy.colony.di.component.DaggerTestRecordComponent;
import com.dy.colony.mvp.contract.TestRecordContract;
import com.dy.colony.mvp.presenter.TestRecordPresenter;

import com.dy.colony.R;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.paginate.Paginate;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;

import static com.jess.arms.utils.Preconditions.checkNotNull;

public class TestRecordFragment extends BaseFragment<TestRecordPresenter> implements TestRecordContract.View, SwipeRefreshLayout.OnRefreshListener, IBackPressed {
    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    AlertDialog mSportDialog;
    @Inject
    TestRecordAdapter mAdapter;
    @Inject
    List<Detection_Record_FGGD_NC> mDetection_record_fggd_ncs;
    @BindView(R.id.re_allcheck)
    CheckBox mAllcheck;
    @BindView(R.id.re_recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.re_swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.re_showpage)
    TextView mShowpage;
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
    @BindView(R.id.test_moudle)
    Spinner mTestMoudle;
    @BindView(R.id.layout_moudle)
    LinearLayout mLayoutMoudle;
    @BindView(R.id.test_project)
    Button mTestProject;
    @BindView(R.id.test_time)
    Button mTestTime;
    @BindView(R.id.layout_time)
    LinearLayout mLayoutTime;
    @BindView(R.id.test_result)
    Spinner mTestResult;
    @BindView(R.id.layout_result)
    LinearLayout mLayoutResult;
    @Inject
    List<Detection_Record_FGGD_NC> mList;
    private boolean mTestMoudle_flag = true;
    private boolean mTestResult_flag = true;
    private boolean isSeaching;
    private Paginate mPaginate;
    private String index;
    private String keyWord = "";
    private boolean isLoadingMore;
    private boolean hasLoadedAllItems;
    private MenuItem mMenuItemInput;
    private MenuItem mMenuItemDelete;
    private boolean firstLoad = true;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Detection_Record_FGGD_NC nc) {
        //更新UI
        String code = nc.getSysCode();
        List<Detection_Record_FGGD_NC> data = mAdapter.getData();
        for (int i = 0; i < data.size(); i++) {
            Detection_Record_FGGD_NC entity = data.get(i);
            if (((Detection_Record_FGGD_NC) entity).getSysCode().equals(code)) {
                mDetection_record_fggd_ncs.set(i, nc);
                mAdapter.notifyItemChanged(i);
                return;
            }

        }
    }

    public static TestRecordFragment newInstance() {
        TestRecordFragment fragment = new TestRecordFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerTestRecordComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_testrecord, container, false);
    }



    @Override
    public boolean onBackPressed() {
        // 核心判断：如果正在搜索且有关键词
        if (isSeaching && !TextUtils.isEmpty(keyWord)) {
            // 执行清空逻辑
            keyWord = "";
            isSeaching = false;
            mPresenter.loadMore(true);
            mToolbarTitle.setText(R.string.test_record);

            if (mSearchView != null&& mSearchView.isSearchOpen()) {
                mSearchView.closeSearch();
            }

            return true;
        }

        // 如果没有搜索内容，返回 false，让 Activity 去走“双击退出”
        return false;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        initRecycle();
        initToolbar();
        initSearcchView();
        initSpinner();
        mPresenter.loadMore(true);
        initPaginate();
    }

    private void initToolbar() {
        setHasOptionsMenu(true);
        if (getActivity() instanceof androidx.appcompat.app.AppCompatActivity) {
            androidx.appcompat.app.AppCompatActivity activity = (androidx.appcompat.app.AppCompatActivity) getActivity();
            activity.setSupportActionBar(mToolbar);
            if (activity.getSupportActionBar() != null) {
                // 隐藏默认的 App 名字标题
                activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
            }
        }
        mToolbarBack.setVisibility(View.GONE);
        mToolbarTitle.setText(R.string.test_record);
        mToolbarTitle.setTextColor(Color.WHITE);
        // 背景颜色已通过 XML 中的 Style (@style/AppTitleBarStyle) 统一设置为 @color/colorPrimaryDark
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.testrecord_toobar_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        mMenuItemDelete = menu.findItem(R.id.menu_delete_testitem);
        MenuItem upload_menu = menu.findItem(R.id.menu_upload_testitem);
        if (Constants.IS_OFFLINE_MODE) {
            upload_menu.setVisible(false);
        }
        mSearchView.setMenuItem(item);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_delete_testitem:
                mPresenter.deleteData();
                break;
            case R.id.menu_upload_testitem:
                mPresenter.uploadData();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void initSpinner() {
        customSpinner(1, mTestResult);
        customSpinner(0, mTestMoudle);

        //分光，干式农残，胶体金
        mTestMoudle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //spinner注册监听后会默认触发一次 onItemSelected
                if (position == 0) {
                    isSeaching = false;
                } else {
                    isSeaching = true;
                }
                if (mTestMoudle_flag) {
                    mTestMoudle_flag = false;
                    return;
                }
                String item = (String) parent.getSelectedItem();

                LogUtils.d(item);
                LogUtils.d(position + " " + id);
                mPresenter.seach(keyWord, starttime, stoptime, item, mTestResult.getSelectedItem().toString(), true);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        mTestResult.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    isSeaching = false;
                } else {
                    isSeaching = true;
                }
                if (mTestResult_flag) {
                    mTestResult_flag = false;
                    return;
                }
                String item = (String) parent.getSelectedItem();
                LogUtils.d(item);
                LogUtils.d(position + " " + id);
                mPresenter.seach(keyWord, starttime, stoptime, mTestMoudle.getSelectedItem().toString(), item, true);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void customSpinner(int type, Spinner spinner) {
        String[] moudles;
        if (type == 1) {
            moudles = getResources().getStringArray(R.array.results);
        } else {
            moudles = getResources().getStringArray(R.array.moudles);
        }

        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(getContext(), R.layout.spinner_item_main, moudles);
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter1);
    }

    private void initSearcchView() {
        starttime = getString(R.string.all);
        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                //为了避免 java.lang.IllegalArgumentException: Called attach on a child which is not detached: ViewHolder
                // mAdapter.getData_S().clear();
                // mAdapter.notifyDataSetChanged();

                mPresenter.seach(s, starttime, stoptime, mTestMoudle.getSelectedItem().toString(), mTestResult.getSelectedItem().toString(), true);
                isSeaching = true;
                keyWord = s;
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

    private void initRecycle() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        ArmsUtils.configRecyclerView(mRecyclerView, mLayoutManager);
        mAdapter.setEmptyView(R.layout.emptyview, (ViewGroup) mRecyclerView.getParent());
        mRecyclerView.setAdapter(mAdapter);
        //((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
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
                Detection_Record_FGGD_NC item = mAdapter.getData().get(position);
                Intent intent = new Intent(getActivity(), TestRecordMessageActivity.class);
                Bundle extras = new Bundle();
                extras.putParcelable("data", item);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
    }

    private void initPaginate() {
        if (mPaginate == null) {
            Paginate.Callbacks callbacks = new Paginate.Callbacks() {
                @Override
                public void onLoadMore() {
                    //LogUtils.d("onLoadMore");
                    if (firstLoad) {
                        LogUtils.d("firstLoad---");
                        firstLoad = false;
                        return;
                    }
                    if (isLoadingMore) {
                        return;
                    }
                    if (isSeaching) {
                        mPresenter.seach(keyWord, starttime, stoptime, mTestMoudle.getSelectedItem().toString(), mTestResult.getSelectedItem().toString(), false);

                    } else {
                        mPresenter.loadMore(false);
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

            mPaginate = Paginate.with(mRecyclerView, callbacks)
                    //预加载两页，在数据量大的时候加载会很慢
                    //.setLoadingTriggerThreshold(Constants.page_num * 2)
                    .setLoadingTriggerThreshold(0)
                    .build();
            mPaginate.setHasMoreDataToLoad(false);
        }
    }

    @Override
    public void setData(@Nullable Object data) {

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
            getActivity().runOnUiThread(new Runnable() {
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
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mSportDialog.dismiss();
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
        hasLoadedAllItems = true;
        View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.footview_layout, null);
        TextView id = (TextView) inflate.findViewById(R.id.showmessage);
        id.setText(getString(R.string.nomore_data));
        mAdapter.removeAllFooterView();
        mAdapter.addFooterView(inflate);

        mAdapter.notifyDataSetChanged();
        // hideLoading();
    }

    @Override
    public void sethasLoadedAllItemsfase() {
        hasLoadedAllItems = false;


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

    @Override
    public void showSportDialog(String title) {
        LogUtils.d(title);
        //尝试先消除上次的dialog 避免弹框不消失
        if (mSportDialog != null) {
            if (mSportDialog.isShowing()) {
                mSportDialog.dismiss();
            }
        }
        mSportDialog = new SpotsDialog.Builder().setContext(getActivity()).setCancelable(false).build();
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
    public void onRefresh() {

        if (isSeaching) {
            mPresenter.seach(keyWord, starttime, stoptime, mTestMoudle.getSelectedItem().toString(), mTestResult.getSelectedItem().toString(), true);
        } else {
            mPresenter.loadMore(true);
        }
        //下拉刷新时将全选按钮状态改为非选中，因为下拉刷新会情况当前list，list的初始选中状态是非选中
        mAllcheck.setChecked(false);
    }

    @OnClick({R.id.test_time, R.id.re_allcheck})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.test_time:
                choseTime1();
                break;
            case R.id.re_allcheck:
                LogUtils.d("全选");
                List<Detection_Record_FGGD_NC> data = mAdapter.getData();
                for (int i = 0; i < data.size(); i++) {
                    Detection_Record_FGGD_NC entity = data.get(i);
                    ((Detection_Record_FGGD_NC) entity).checkState = mAllcheck.isChecked();
                }
                mAdapter.notifyDataSetChanged();
                break;
        }
    }

    private String starttime;
    private String stoptime;

    private void choseTime1() {
        MyDatePickerDialog myDatePickerDialog = new MyDatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
        myDatePickerDialog.setOnImgDialogListener(new MyDatePickerDialog.OnImgDialogListener() {
            @Override
            public void onItemImg(int year1_start, int month1_start, int day1_start, int year1_stop, int month1_stop, int day1_stop, String type) {
                LogUtils.d(year1_start + "年" + month1_start + "月" + day1_start + "日" + year1_stop + "年" + month1_stop + "月" + day1_stop + "日" + type);
                isSeaching = true;
                Calendar calendarstart = Calendar.getInstance();
                calendarstart.set(year1_start, month1_start, day1_start);
                Calendar calendarstop = Calendar.getInstance();
                calendarstop.set(year1_stop, month1_stop, day1_stop);


                Date time1 = calendarstart.getTime();
                Date time2 = calendarstop.getTime();

                if (time1.getTime() > time2.getTime()) {
                    starttime = getTime(time2);
                    stoptime = getTime(time1);
                    mTestTime.setText(starttime + "-" + stoptime);
                } else if (time1.getTime() < time2.getTime()) {
                    starttime = getTime(time1);
                    stoptime = getTime(time2);
                    mTestTime.setText(starttime + "-" + stoptime);

                } else {
                    starttime = getTime(time1);
                    stoptime = getTime(time2);
                    mTestTime.setText(starttime);
                }


                mPresenter.seach(keyWord, starttime, stoptime, mTestMoudle.getSelectedItem().toString(), mTestResult.getSelectedItem().toString(), true);
            }

            @Override
            public void cancle() {
                isSeaching = false;
                starttime = getString(R.string.all);
                stoptime = "";
                mTestTime.setText(R.string.all);

                mPresenter.seach(keyWord, getString(R.string.all), stoptime, mTestMoudle.getSelectedItem().toString(), mTestResult.getSelectedItem().toString(), true);
            }
        });
        myDatePickerDialog.myShow();
    }


    private String getTime(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        return df.format(date);
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

    }
}