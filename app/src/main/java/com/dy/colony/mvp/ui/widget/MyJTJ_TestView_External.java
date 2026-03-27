package com.dy.colony.mvp.ui.widget;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Looper;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.apkfuns.logutils.LogUtils;
import com.daimajia.swipe.SwipeLayout;
import com.dy.colony.BuildConfig;
import com.dy.colony.MyAppLocation;
import com.dy.colony.R;
import com.dy.colony.app.utils.BitmapUtils;
import com.dy.colony.greendao.beans.Detection_Record_FGGD_NC;
import com.dy.colony.mvp.model.entity.base.BaseProjectMessage;
import com.dy.colony.mvp.model.entity.base.BaseSampleMessage;
import com.dy.colony.mvp.model.entity.base.GalleryBean;
import com.dy.colony.mvp.model.entity.eventbus.ExternTestMessageBean;
import com.dy.colony.mvp.ui.activity.ChoseProjectActivity;
import com.dy.colony.mvp.ui.activity.ChoseSampleActivity;
import com.dy.colony.mvp.ui.activity.ChoseUnitActivity;
import com.dy.colony.mvp.ui.activity.JTJ_TestActivity;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.material.textfield.TextInputLayout;
import com.jess.arms.utils.ArmsUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * ━━━━━━神兽出没━━━━━━
 * 　　　┏┓　　　┏┓
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　　　　　┃
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　　　　　┃
 * 　　┃　　　┻　　　┃
 * 　　┃　　　　　　　┃
 * 　　┗━┓　　　┏━┛Code is far away from bug with the animal protecting
 * 　　　　┃　　　┃              神兽保佑,代码无bug
 * 　　　　┃　　　┃
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * <p>
 * Created by wangzhenxiong on 2019-04-29.
 */
public class MyJTJ_TestView_External extends BaseJTJTestView implements SurfaceHolder.Callback, GalleryBean.onJTJResultRecive {
    private Context mContext;
    private Typeface mTf;
    private ViewHolder mViewHolder;
    private int mIndex;
    private SurfaceHolder mSurfaceHolder;
    private boolean showTestResultNumber = false;
    private Unbinder mBind;

    @Override
    public GalleryBean getGallery() {
        return MyAppLocation.myAppLocation.mSerialDataService.mJTJGalleryBeanList.get(mIndex);
    }

    @Override
    public void setQrcodeMessage(String qrcodeMessage) {

    }

    public MyJTJ_TestView_External(@NonNull Context context, int index) {
        super(context);
        mIndex = index;
        mContext = context;
        init();
    }


    public void init() {
        getGallery().setJTJResultReciverListener(this);
        mTf = Typeface.createFromAsset(mContext.getAssets(), "OpenSans-Bold.ttf");
        View inflate = null;
        int jtjCardModel = getGallery().getJTJCardModel();
        LogUtils.d(jtjCardModel);
        inflate = LayoutInflater.from(mContext).inflate(R.layout.myjtjtest_view_p_newui_layout_300_external, this);

        mViewHolder = new ViewHolder(inflate);
        mBind = ButterKnife.bind(this);
        //初始化SurfaceView
        initSurfaceView();
        //初始化显示信息
        mViewHolder.mGalleryNum.setText(getGallery().getGalleryNum() + "");
        //初始化点击事件
        initClickListener();
        initChartView();
        lineData.setValueTypeface(mTf);
        LogUtils.d("??");
        initMessage();
    }

    private void initChartView() {
        // no description text
        mViewHolder.mChart.getDescription().setEnabled(false);

        // chart.setDrawHorizontalGrid(false);
        //
        // enable / disable grid background
        mViewHolder.mChart.setDrawGridBackground(false);
//        chart.getRenderer().getGridPaint().setGridColor(Color.WHITE & 0x70FFFFFF);

        // enable touch gestures
        mViewHolder.mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mViewHolder.mChart.setDragEnabled(true);
        mViewHolder.mChart.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        mViewHolder.mChart.setPinchZoom(false);

        //mViewHolder.mChart.setBackgroundColor(Color.rgb(0, 200, 245));

        // set custom chart offsets (automatic offset calculation is hereby disabled)
        mViewHolder.mChart.setViewPortOffsets(8, 0, 8, 0);

        // add data

        // get the legend (only possible after setting data)
        Legend l = mViewHolder.mChart.getLegend();
        l.setEnabled(false);

        mViewHolder.mChart.getAxisLeft().setEnabled(false);
        mViewHolder.mChart.getAxisLeft().setSpaceTop(40);
        mViewHolder.mChart.getAxisLeft().setSpaceBottom(40);
        mViewHolder.mChart.getAxisRight().setEnabled(false);

        mViewHolder.mChart.getXAxis().setEnabled(false);
    }

    private void initSurfaceView() {
        mSurfaceHolder = mViewHolder.mSurfaceview.getHolder();
        mSurfaceHolder.addCallback(this);
        mViewHolder.mSurfaceview.setZOrderOnTop(true);
        // mSurfaceHolder.setFormat(PixelFormat.TRANSLUCENT);
        mViewHolder.mSurfaceview.setZOrderMediaOverlay(true);

        //mViewHolder.mSurfaceview.setBackgroundColor(R.color.tm55);
    }

    @Override
    public void startTest() {

        int state = getGallery().getState();
        if (state == 1) {
            ArmsUtils.snackbarText("该通道正在检测，请稍后");
            return;
        }
        if (mBitmaps.size() < 1) {
            ArmsUtils.snackbarText("图像正在接收，请稍后");
        }
        //判断检测所需信息是否完整
        if (checkMessageState()) {
            boolean data = mViewHolder.mChart.isEmpty();
            if (!data) {
                post(new Runnable() {
                    @Override
                    public void run() {
                        LogUtils.d("主线程--" + (Looper.myLooper() == Looper.getMainLooper()));
                        mViewHolder.mChart.clear();
                        mViewHolder.mChart.setBackground(null);
                    }
                });

            }
            //当出现一些临界情况如快速点击开始检测按钮，最后的任务会导致执行失败，在提交最后一个任务时  getGallery().checkData_External中的FileUtils.saveBitmaplevel1中
            //保存的bitmap可能会释放掉
            ArmsUtils.obtainAppComponentFromContext(MyAppLocation.myAppLocation).executorService().submit(new Runnable() {
                @Override
                public void run() {
                    LogUtils.d("obtainAppComponentFromContext");

                    /*boolean haveCard = PictureToolUtils.isHaveCard_External(mBitmaps.get(0), getGallery().getGalleryNum());
                    LogUtils.d(haveCard);
                    if (!haveCard) {
                        getGallery().setState(3);
                        ArmsUtils.snackbarText("请插入胶体金卡");
                        return;
                    }*/
                    getGallery().checkData_External(mBitmaps);

                }
            });
        } else {
            ArmsUtils.snackbarText("请选择检测项目和样品名称");
        }
    }

    private void makeNewResultDialog(GalleryBean item, int i) {
        android.app.AlertDialog.Builder mAlertDialog = new android.app.AlertDialog.Builder(mContext);
        View view2 = View.inflate(mContext, R.layout.dialog_newresult_multcard_layout, null);
        Button save = (Button) view2.findViewById(R.id.save);
        Button cancle = (Button) view2.findViewById(R.id.cancle);
        LinearLayout card1 = (LinearLayout) view2.findViewById(R.id.card1);
        LinearLayout card2 = (LinearLayout) view2.findViewById(R.id.card2);
        LinearLayout card3 = (LinearLayout) view2.findViewById(R.id.card3);
        LinearLayout card4 = (LinearLayout) view2.findViewById(R.id.card4);
        Spinner result1 = (Spinner) view2.findViewById(R.id.result1);
        Spinner result2 = (Spinner) view2.findViewById(R.id.result2);
        Spinner result3 = (Spinner) view2.findViewById(R.id.result3);
        Spinner result4 = (Spinner) view2.findViewById(R.id.result4);

        Spinner decome1 = (Spinner) view2.findViewById(R.id.decome1);
        Spinner decome2 = (Spinner) view2.findViewById(R.id.decome2);
        Spinner decome3 = (Spinner) view2.findViewById(R.id.decome3);
        Spinner decome4 = (Spinner) view2.findViewById(R.id.decome4);
        result1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                decome1.setSelection(position, true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        result2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                decome2.setSelection(position, true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        result3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                decome3.setSelection(position, true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        result4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                decome4.setSelection(position, true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (i == 0) {
            card2.setVisibility(View.GONE);
            card3.setVisibility(View.GONE);
            card4.setVisibility(View.GONE);
        } else if (i == 3) {
            card4.setVisibility(View.GONE);
        }
        mAlertDialog.setTitle("提示");
        mAlertDialog.setView(view2);
        android.app.AlertDialog dialog = mAlertDialog.create();
        dialog.show();
        save.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String s_result1 = result1.getSelectedItem().toString();
                String s_result2 = result2.getSelectedItem().toString();
                String s_result3 = result3.getSelectedItem().toString();
                String s_result4 = result4.getSelectedItem().toString();
                String s_decome1 = decome1.getSelectedItem().toString();
                String s_decome2 = decome2.getSelectedItem().toString();
                String s_decome3 = decome3.getSelectedItem().toString();
                String s_decome4 = decome4.getSelectedItem().toString();
                if (i == 0) {
                    if (s_result1.isEmpty()
                            || s_decome1.isEmpty()) {
                        ArmsUtils.snackbarText("请填写结果信息");
                        return;
                    }
                } else if (i == 3) {
                    if (s_result1.isEmpty() || s_result2.isEmpty() || s_result3.isEmpty()
                            || s_decome1.isEmpty() || s_decome2.isEmpty() || s_decome3.isEmpty()) {
                        ArmsUtils.snackbarText("请填写结果信息");
                        return;
                    }
                } else if (i == 4) {
                    if (s_result1.isEmpty() || s_result2.isEmpty() || s_result3.isEmpty() || s_result4.isEmpty()
                            || s_decome1.isEmpty() || s_decome2.isEmpty() || s_decome3.isEmpty() || s_decome4.isEmpty()) {
                        ArmsUtils.snackbarText("请填写结果信息");
                        return;
                    }
                }

                //item.newResult(s_result1, s_result2, s_result3, s_result4, s_decome1, s_decome2, s_decome3, s_decome4, i);

            }
        });
        cancle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void autoResult(GalleryBean item) {
        Detection_Record_FGGD_NC nc = (Detection_Record_FGGD_NC) item;
        LogUtils.d(nc.getTestresult() + "---" + nc.getDecisionoutcome() + "---" + getGallery().getJTJCardModel());
        //item.newResult(nc.getTestresult(), nc.getDecisionoutcome(), nc.getTest_method(), nc.getTest_project(), nc.getSysCode(), 1);
    }

    boolean clickStart = false;

    private void initClickListener() {
        mViewHolder.mSwipe.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new ExternTestMessageBean(10, mIndex));
            }
        });
        //开始检测
        mViewHolder.mJtjStartTest.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                clickStart = true;
                for (int i = 0; i < mBitmaps.size(); i++) {
                    mBitmaps.get(i).recycle();
                }
                if (!mViewHolder.mChart.isEmpty()) {
                    mViewHolder.mChart.clearValues();
                }
                mViewHolder.mChart.clear();
                mViewHolder.mChart.clearAnimation();
                mViewHolder.mChart.setBackground(null);
                mBitmaps.clear();
                lineData.clearValues();
            }


        });
        mViewHolder.mJtjStartTest.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                BaseSampleMessage sampleMessage = getGallery().getSampleMessage();
                BaseProjectMessage projectMessage = getGallery().getProjectMessage();
                if (sampleMessage == null) {
                    ArmsUtils.snackbarText(mContext.getString(R.string.pleacechoseitemfirst));
                    return true;
                }
                if (projectMessage == null) {
                    ArmsUtils.snackbarText(mContext.getString(R.string.pleacechoseitemfirst));
                    return true;
                }
                makeNewResultDialog(getGallery(), getGallery().getJTJCardModel());
                return true;
            }
        });


        //清除所选的检测信息
        mViewHolder.mBtnClean.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewHolder.mSwipe.close();
                int state = getGallery().getState();
                if (state == 1) {
                    ArmsUtils.snackbarText(mContext.getString(R.string.testing_wait));
                    return;
                }
                getGallery().cleanJTJ();
                //setPicture(BitmapFactory.decodeResource(getResources(), R.drawable.camera));
                if (!mViewHolder.mChart.isEmpty()) {
                    mViewHolder.mChart.clear();
                    mViewHolder.mChart.setBackground(null);

                }
                //reFresh();
            }
        });
        mViewHolder.mBtnClean.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mViewHolder.mSwipe.close();
                getGallery().cleanJTJNotJudgeState();
                //setPicture(BitmapFactory.decodeResource(getResources(), R.drawable.camera));
                if (!mViewHolder.mChart.isEmpty()) {
                    mViewHolder.mChart.clear();
                    mViewHolder.mChart.setBackground(null);

                }
                return false;
            }
        });

    }

    private void autoResult() {
        //判断检测所需信息是否完整
        if (checkMessageState()) {
            autoResult(getGallery());
        } else {
            ArmsUtils.snackbarText("请选择检测项目和样品名称");
        }
    }


    @Override
    public boolean checkMessageState() {
        String samplename = ((Detection_Record_FGGD_NC) getGallery()).getSamplename();
        if (null == getGallery().getProjectMessage()) {
            //ArmsUtils.snackbarText("请选择检测项目");
            return false;
        }

        if ("".equals(samplename)) {
            // ArmsUtils.snackbarText("请选择或输入样品");
            return false;
        }

        return true;
    }

    @Override
    public void postInvalidate() {
        super.postInvalidate();
    }


    LineData lineData = new LineData();

    private LineData getData(List<Float> bytes, LineChart mChart, int v1, int v2, LineDataSet set1) {
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int i = 0; i < bytes.size(); i++) {
            if ((v1 <= i - 10 && i <= v1 + 30)) {
                colors.add(Color.GREEN);
            } else if ((v2 <= i - 10 && i < v2 + 30)) {
                colors.add(Color.RED);
            } else {
                colors.add(Color.BLUE);
            }

        }
        ArrayList<Entry> values = new ArrayList<>();
        ArrayList<Entry> yVals = new ArrayList<>();
        if (null == bytes) {
            bytes = new ArrayList<>();
        }
        for (int i = 0; i < bytes.size(); i++) {
            Entry e = new Entry(i, bytes.get(i));
            if (i == 20 || i == 60) {
                yVals.add(e);
            }
            values.add(e);
        }

        if (set1 == null) {
            set1 = new LineDataSet(values, "");
        } else {
            set1.clear();
            set1.setValues(values);
        }
        set1.setDrawIcons(false);
        set1.setColor(Color.BLACK);
        //循环判断y值，并向颜色集合中添加对应的颜色

        set1.setCircleColors(colors);
        set1.setLineWidth(1.75f);
        set1.setCircleRadius(4f);
        set1.setCircleHoleRadius(1.5f);
        set1.setColors(colors);
        set1.setHighLightColor(Color.GREEN);
        set1.setDrawValues(true);
        lineData.addDataSet(set1);
        return lineData;
    }

    private int mSurfaceState;

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //LogUtils.d("surfaceCreated");
        //显示上次的检测结果，只能放这里 放init里刷新不了 因为只有当 surfaceCreated 触发后才能进行绘制
        mSurfaceState = 1;

        /*Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.camera);
        setPicture(bitmap);*/
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mSurfaceState = 2;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mSurfaceState = 3;
    }

    @Override
    public void reFresh() {
        LogUtils.d("reFresh");
        //初始化显示信息
        EventBus.getDefault().post(new ExternTestMessageBean(0, mIndex));
    }

    @Override
    public void showTestResultNumber(boolean isshow) {
        showTestResultNumber = isshow;

        if (showTestResultNumber) {
            mViewHolder.mTestResult.setVisibility(VISIBLE);
        } else {
            mViewHolder.mTestResult.setVisibility(GONE);
        }
        invalidate();
    }

    private int background1 = (R.drawable.chart_wuxiao_0);
    private int background2 = (R.drawable.chart_wuxiao_1);
    private int background3 = (R.drawable.chart_yang);
    private int background4 = (R.drawable.chart_yin);
    private int background5 = (R.drawable.chart_yang_b);
    private int background6 = (R.drawable.chart_yin_b);
    private int background7 = (R.drawable.chart_keyi7);
    private int background8 = (R.drawable.chart_keyi8);


    @Override
    public void onReciverSuccess(List<Float> userfuldata, double[] data) {
        LogUtils.d("结果回调");
    }

    @Override
    public void onReciverSuccess(List<List<Float>> userfuldata, List<double[]> data) {
        LogUtils.d("结果回调");
        if (mContext == null || ((JTJ_TestActivity) mContext).isFinishing()) {
            LogUtils.d("onReciverSuccess,activity已经销毁");
            return;
        }
        getGallery().setState(2);
        ((JTJ_TestActivity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (getGallery().getJTJCardModel() == 3) {
                    initDataMessage3();
                } else if (getGallery().getJTJCardModel() == 4) {
                    initDataMessage3();
                } else {
                    initDataMessage();
                }
                EventBus.getDefault().post(new ExternTestMessageBean(0, mIndex));

            }
        });
    }

    private List<LineDataSet> setList = new ArrayList<>();

    private void initsetlist(Set<Integer> integers) {
        if (setList.size() < integers.size()) {
            setList.add(new LineDataSet(new ArrayList<>(), ""));
            initsetlist(integers);
        } else {
            return;
        }

    }

    private void initDataMessage3() {
        LogUtils.d("onReciverSuccess");
        Map<Integer, double[]> hashMap_multiCard = getGallery().getHashMap_MultiCard();
        Map<Integer, List<Float>> hashMap_userfuldata = getGallery().getHashMap_userfuldata();
        Set<Integer> integers = hashMap_multiCard.keySet();
        LogUtils.d(hashMap_userfuldata);
        initsetlist(integers);
        StringBuffer stringBuffer = new StringBuffer();
        for (Integer integer : integers) {
            double[] data1 = hashMap_multiCard.get(integer);
            getData(hashMap_userfuldata.get(integer), mViewHolder.mChart, ((Double) data1[0]).intValue(), ((Double) data1[2]).intValue(), setList.get(integer - 1));
            if (showTestResultNumber) {
                double v = data1[3] / data1[1];
                stringBuffer.append(integer + "  C原始值:" + new DecimalFormat("##0.000").format(data1[1])
                        + "  T原始值:" + new DecimalFormat("##0.000").format(data1[3])
                        + "  T/C值" + new DecimalFormat("##0.000").format(v) + "\r\n");
            } else {
                mViewHolder.mTestResult.setText("");
            }
        }
        LogUtils.d(lineData);
        mViewHolder.mChart.setData(lineData);
        if (showTestResultNumber) {
            mViewHolder.mTestResult.setText(stringBuffer.toString());
        } else {
            mViewHolder.mTestResult.setText("");
        }
        mViewHolder.mChart.invalidate();
    }

    private void initDataMessage() {
        LogUtils.d("onReciverSuccess");
        Map<Integer, double[]> hashMap_multiCard = getGallery().getHashMap_MultiCard();
        Map<Integer, List<Float>> hashMap_userfuldata = getGallery().getHashMap_userfuldata();
        Set<Integer> integers = hashMap_multiCard.keySet();
        LogUtils.d(hashMap_userfuldata);
        initsetlist(integers);
        for (Integer integer : integers) {
            double[] data1 = hashMap_multiCard.get(integer);

            getData(hashMap_userfuldata.get(integer), mViewHolder.mChart, ((Double) data1[0]).intValue(), ((Double) data1[2]).intValue(), setList.get(integer - 1));

            //setupChart(mViewHolder.mChart, lineData);

            //((LineDataSet) lineData.getDataSetByIndex(0)).setCircleHoleColor(Color.rgb(0, 200, 245));

            mViewHolder.mChart.setData(lineData);

            if (showTestResultNumber) {
                double v = data1[3] / data1[1];
                mViewHolder.mTestResult.setText("C原始值:" + new DecimalFormat("##0.000").format(data1[1])
                        + "  T原始值:" + new DecimalFormat("##0.000").format(data1[3])
                        + "  T/C值" + new DecimalFormat("##0.000").format(v));
            } else {
                mViewHolder.mTestResult.setText("");
            }
            switch (getGallery().getBackgroundResous()) {
                case 1:
                    mViewHolder.mChart.setBackgroundResource(background1);
                    break;
                case 2:
                    mViewHolder.mChart.setBackgroundResource(background2);
                    break;
                case 3:
                    mViewHolder.mChart.setBackgroundResource(background3);
                    break;
                case 4:
                    mViewHolder.mChart.setBackgroundResource(background4);
                    break;
                case 5:
                    mViewHolder.mChart.setBackgroundResource(background5);
                    break;
                case 6:
                    mViewHolder.mChart.setBackgroundResource(background6);
                    break;
                case 7:
                    mViewHolder.mChart.setBackgroundResource(background7);
                    break;
                case 8:
                    mViewHolder.mChart.setBackgroundResource(background8);
                    break;
            }
            //invalidate();
        }
        if (BuildConfig.DEBUG) {
            /*new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                        ((JTJTest_BX1Activity) mContext).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (mViewHolder == null || mViewHolder.mChart == null) {
                                    return;
                                }
                                clickStart = true;
                                for (int i = 0; i < mBitmaps.size(); i++) {
                                    mBitmaps.get(i).recycle();
                                }
                                if (!mViewHolder.mChart.isEmpty()) {
                                    mViewHolder.mChart.clearValues();
                                }
                                mViewHolder.mChart.clear();
                                mViewHolder.mChart.clearAnimation();
                                mViewHolder.mChart.setBackground(null);
                                mBitmaps.clear();
                                lineData.clearValues();

                            }
                        });

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();*/
        }
    }

    @OnClick({R.id.choseproject, R.id.samplename_btn, R.id.unit_btn})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.choseproject:
                //选择检测项目
                if (judgeState(0)) return;

                intent = new Intent(mContext, ChoseProjectActivity.class);
                intent.putExtra("from", "jtj_1");
                intent.putExtra("index", mIndex);
                mContext.startActivity(intent);

                break;
            case R.id.samplename_btn:
                //选择样品名称
                if (judgeState(1)) return;

                intent = new Intent(mContext, ChoseSampleActivity.class);
                intent.putExtra("from", "jtj");
                intent.putExtra("index", mIndex);
                mContext.startActivity(intent);

                break;

            case R.id.unit_btn:
                //选择被检单位
                if (judgeState(0)) return;
                showEditUnitsDialog();
                break;


        }
    }

    private boolean judgeState(int type) {
        GalleryBean gallery = getGallery();

        if (gallery.getState() == 1) {
            ArmsUtils.snackbarText(mContext.getString(R.string.testing_wait));
            return true;
        }
        if (type == 1) {
            if (null == gallery.getProjectMessage()) {
                ArmsUtils.snackbarText(mContext.getString(R.string.place_choseproject));
                return true;
            }
        }
        return false;
    }

    private void showEditUnitsDialog() {
        Detection_Record_FGGD_NC galleryBean = (Detection_Record_FGGD_NC) getGallery();
        FrameLayout container = new FrameLayout(mContext);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dimen_20dp);
        params.rightMargin = getResources().getDimensionPixelSize(R.dimen.dimen_20dp);
        params.topMargin = getResources().getDimensionPixelSize(R.dimen.dimen_10dp);

        final EditText editText = new EditText(mContext);
        editText.setLayoutParams(params);
        String prosecutedunits = galleryBean.getProsecutedunits();
        editText.setText(prosecutedunits);
        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        editText.setHint(mContext.getString(R.string.enter_inspect_unit));
        editText.setSingleLine(true);
        // 自动把光标移到末尾
        if (prosecutedunits != null) {
            editText.setSelection(prosecutedunits.length());
        }

        container.addView(editText);

        // 2. 构建对话框
        AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setTitle(R.string.enter_inspect_unit)
                .setView(container)
                .setPositiveButton(R.string.sure, (dialogInterface, i) -> {
                    String inputText = editText.getText().toString().trim();
                    galleryBean.setProsecutedunits(inputText);
                    mViewHolder.mUnitBtn.setText(inputText);
                })
                .setNegativeButton(R.string.cancel, null)
                .create();

        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();

        editText.postDelayed(() -> {
            InputMethodManager imm = (InputMethodManager)  mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                // 3. 显式调用 showSoftInput，并传入强制显示的标志
                // 注意：第一个参数必须是当前获得焦点的 View
                imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
            }
        }, 160);

        dialog.show();
    }

    private List<Bitmap> mBitmaps = new ArrayList<>();

    @Override
    public void onReciverSuccess(Bitmap bitmap) {
        //System.out.println("收到图片" + mIndex + "--" + clickStart);
        if (mContext == null || ((JTJ_TestActivity) mContext).isFinishing()) {
            LogUtils.d("收到图片,activity已经销毁");
            return;
        }
        setPicture(bitmap);
        //LogUtils.d("收到");
        if (clickStart) {
            clickStart = false;

            Bitmap copy = bitmap.copy(bitmap.getConfig(), true);
            if (copy == null) {
                ArmsUtils.snackbarText("图片拷贝失败！");
                return;
            }
            mBitmaps.add(copy);
            LogUtils.d("添加");
            startTest();
        }

    }

    private void setPicture(Bitmap bitmap) {
        if (null == bitmap) {
            return;
        }
        if (mSurfaceState == 3) {
            return;
        }
        if (null == mSurfaceHolder) {
            LogUtils.d("null==mSurfaceHolder");
            return;
        }
        if (!mSurfaceHolder.getSurface().isValid()) {
            LogUtils.w("SurfaceCheck: Surface is INVALID");
            return;
        }
        Canvas canvas = mSurfaceHolder.lockCanvas();
        if (null != canvas) {
            canvas.drawColor(Color.WHITE);
            int height1 = canvas.getHeight();
            int width1 = canvas.getWidth();
            Bitmap bitmap1 = BitmapUtils.resizeBitmap(bitmap, width1, height1);
            int height2 = bitmap1.getHeight();
            int width2 = bitmap1.getWidth();
            int left = (width1 - width2) / 2;
            int top = (height1 - height2) / 2;
            canvas.drawBitmap(bitmap1, left, top, null);
            bitmap1.recycle();
            bitmap1 = null;

            mSurfaceHolder.unlockCanvasAndPost(canvas);
        }
        if (bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
        //LogUtils.d("setPicture");
    }


    @Override
    public void onReciverfail() {
        ArmsUtils.snackbarText("图片接收失败，请重新扫描");
    }

    @Override
    public void onTimer(int timer) {

    }

    @Override
    public void onRefrish() {
        LogUtils.d("onRefrish");

        reFresh();
    }


    static class ViewHolder {
        @BindView(R.id.btn_clean)
        ImageButton mBtnClean;
        @BindView(R.id.surfaceview)
        SurfaceView mSurfaceview;
        @BindView(R.id.chart)
        LineChart mChart;
        @BindView(R.id.gallery_num)
        Button mGalleryNum;
        @BindView(R.id.swipe)
        SwipeLayout mSwipe;
        @BindView(R.id.jtj_starttest)
        ImageButton mJtjStartTest;
        @BindView(R.id.testresult)
        TextView mTestResult;
        @BindView(R.id.choseproject)
        Button mChoseproject;
        @BindView(R.id.samplename_btn)
        Button mSamplenameBtn;
        @BindView(R.id.unit_btn)
        Button mUnitBtn;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    // 接收事件
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onProjectSelected(ExternTestMessageBean bean) {
        // 如果你有多个 View 实例，这里需要判断一下是否是发给自己的
        // 比如：if (event.position == this.myPosition)
        if (bean.tag == 0) {
            /*if (bean.index != -1) {
                mIndex = bean.index;
            }
            LogUtils.d(mIndex);
            GalleryBean nowShowBean = MyAppLocation.myAppLocation.mSerialDataService.mJTJGalleryBeanList.get(mIndex);*/
            initMessage();
        }
    }

    @SuppressLint("SetTextI18n")
    private void initMessage() {
        Detection_Record_FGGD_NC bean = (Detection_Record_FGGD_NC) getGallery();
        mViewHolder.mChoseproject.setText(bean.getTest_project());
        mViewHolder.mSamplenameBtn.setText(bean.getSamplename());
        mViewHolder.mUnitBtn.setText(bean.getProsecutedunits());
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        mSurfaceHolder.removeCallback(this);
        //getGallery().removeJTJResultReciverListener();
        //mHandler.removeCallbacksAndMessages(null);
        EventBus.getDefault().unregister(this);
        if (mBind != null) {
            mBind.unbind();
        }
        for (int i = 0; i < mBitmaps.size(); i++) {
            Bitmap bitmap = mBitmaps.get(i);
            bitmap.recycle();
        }
        mBitmaps.clear();
        LogUtils.d("onDetachedFromWindow");
        if (!mViewHolder.mChart.isEmpty()) {
            mViewHolder.mChart.clearValues();
        }
        mViewHolder.mChart.clear();
        mViewHolder.mChart.clearAnimation();
        mViewHolder = null;
        super.onDetachedFromWindow();

    }
}
