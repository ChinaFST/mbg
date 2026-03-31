package com.dy.colony.mvp.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.Toolbar;

import com.apkfuns.logutils.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.dy.colony.R;
import com.dy.colony.app.utils.BitmapUtils;
import com.dy.colony.app.utils.FileUtils;
import com.dy.colony.app.utils.QRCodeUtils;
import com.dy.colony.di.component.DaggerTestRecordMessageComponent;
import com.dy.colony.greendao.DBHelper;
import com.dy.colony.greendao.beans.Company_Point;
import com.dy.colony.greendao.beans.Company_Point_Unit;
import com.dy.colony.greendao.beans.Detection_Record_FGGD_NC;
import com.dy.colony.greendao.beans.FGGDTestItem;
import com.dy.colony.greendao.beans.FoodItemAndStandard;
import com.dy.colony.greendao.beans.JTJPoint;
import com.dy.colony.greendao.beans.JTJTestItem;
import com.dy.colony.greendao.daos.Company_PointDao;
import com.dy.colony.greendao.daos.Company_Point_UnitDao;
import com.dy.colony.greendao.daos.FGGDTestItemDao;
import com.dy.colony.greendao.daos.FoodItemAndStandardDao;
import com.dy.colony.greendao.daos.JTJPointDao;
import com.dy.colony.greendao.daos.JTJTestItemDao;
import com.dy.colony.mvp.contract.TestRecordMessageContract;
import com.dy.colony.mvp.model.entity.base.BaseProjectMessage;
import com.dy.colony.mvp.presenter.TestRecordMessagePresenter;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.material.appbar.AppBarLayout;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

import static com.jess.arms.utils.Preconditions.checkNotNull;

public class TestRecordMessageActivity extends BaseActivity<TestRecordMessagePresenter> implements TestRecordMessageContract.View {
    @Inject
    RxPermissions mRxPermissions;
    @BindView(R.id.toolbar_back)
    RelativeLayout mToolbarBack;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbarly)
    AppBarLayout mToolbarly;
    @BindView(R.id.groupmenu)
    RadioGroup mGroupmenu;
    @BindView(R.id.groupmessage)
    LinearLayout mGroupmessage;
    @BindView(R.id.qrcode)
    RadioButton mQrcode;
    @BindView(R.id.samplename)
    RadioButton mSamplename;
    @BindView(R.id.testproject)
    RadioButton mTestproject;
    @BindView(R.id.testresult)
    RadioButton mTestresult;
    @BindView(R.id.teststandnum)
    RadioButton mTeststandnum;
    @BindView(R.id.inspectedunit)
    RadioButton mInspectedunit;
    @BindView(R.id.testsite)
    RadioButton mTestsite;
    @BindView(R.id.testunit)
    RadioButton mTestunit;
    @BindView(R.id.testmethod)
    RadioButton mTestmethod;
    @BindView(R.id.chartline)
    RadioButton mChartline;
    private Intent mIntent;
    private Detection_Record_FGGD_NC mData;
    //private TextureMapView mMapView;
    private TextView mTextView_method;
    private TextView mTextView_project;
    private TextView mTextView_sample;
    private ImageView mQrcodeView;
    private TextView mTextView_TestUnit;
    private TextView mTextView_TestResult;
    private ImageView mImageJTJ;
    private LineChart mLineChart;
    private JTJPointDao mJTJPointDao;
    private Typeface mTf;
    private PDFView mPDFView;
    @Inject
    AlertDialog mSportdialog;
    private File mPdfFile;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerTestRecordMessageComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_testrecordmessage; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mTf = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Bold.ttf");
        mIntent = getIntent();
        if (mIntent == null) {
            finish();
            ArmsUtils.snackbarText(getString(R.string.parerror));
        }
        Bundle extras = mIntent.getExtras();
        if (extras == null) {
            finish();
            ArmsUtils.snackbarText(getString(R.string.parerror));
        }
        mData = (Detection_Record_FGGD_NC) extras.get("data");
        if (mData == null) {
            finish();
            ArmsUtils.snackbarText(getString(R.string.parerror));
        }
        String moudle = mData.getTest_Moudle();
        if (moudle.equals(getString(R.string.JTJ_TestMoudle_P))) {
            mChartline.setVisibility(View.VISIBLE);
            mJTJPointDao = DBHelper.getJTJPointDao(getActivity());

        } else {
            mChartline.setVisibility(View.GONE);
        }

        setTextMessage();
        mGroupmenu.check(R.id.qrcode);
        setQrcode();
        mGroupmenu.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                updateContent(checkedId);
            }
        });
        mToolbarTitle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                View view = View.inflate(getActivity(), R.layout.change_result_layout, null);
                EditText result = (EditText) view.findViewById(R.id.result);
                EditText judgment = (EditText) view.findViewById(R.id.judgment);
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

                dialog.setTitle(R.string.enter_test_value);

                dialog.setView(view);
                dialog.setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String testresult = result.getText().toString();
                        String decisionoutcome = judgment.getText().toString();
                        mData.setTestresult(testresult);
                        mData.setDecisionoutcome(decisionoutcome);
                        DBHelper.getDetection_Record_FGGD_NCDao(getActivity()).insertOrReplace(mData);
                        mTextView_TestResult = null;
                        creatQrcodeView();
                    }
                });
                dialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                return false;
            }
        });
    }

    @OnClick({R.id.qrcode, R.id.samplename, R.id.testproject, R.id.testmethod, R.id.testresult, R.id.teststandnum, R.id.inspectedunit, R.id.testsite, R.id.testunit, R.id.chartline})
    public void onMenuClick(View view) {
        mQrcode.setChecked(view.getId() == R.id.qrcode);
        mSamplename.setChecked(view.getId() == R.id.samplename);
        mTestproject.setChecked(view.getId() == R.id.testproject);
        mTestmethod.setChecked(view.getId() == R.id.testmethod);
        mTestresult.setChecked(view.getId() == R.id.testresult);
        mTeststandnum.setChecked(view.getId() == R.id.teststandnum);
        mInspectedunit.setChecked(view.getId() == R.id.inspectedunit);
        mTestsite.setChecked(view.getId() == R.id.testsite);
        mTestunit.setChecked(view.getId() == R.id.testunit);
        mChartline.setChecked(view.getId() == R.id.chartline);

        mGroupmenu.check(view.getId());
        updateContent(view.getId());
    }

    private void updateContent(int checkedId) {
        switch (checkedId) {
            case R.id.qrcode:
                setQrcode();
                break;
            case R.id.samplename:
                setSample();
                break;
            case R.id.testproject:
                setProject();
                break;
            case R.id.testmethod:
                setTestMethod();
                break;
            case R.id.testresult:
                setTestResult();
                break;
            case R.id.teststandnum:
                setTeststandnum();
                break;
            case R.id.inspectedunit:
                setInspectedunit();
                break;
            case R.id.testsite:
                setTestsite();
                break;
            case R.id.testunit:
                setTestunit();
                break;
            case R.id.chartline:
                setTestChartLine();
                break;
        }
    }

    private void setTextMessage() {
        mSamplename.setText(appendStr(R.string.samplename, mData.getSamplename()));
        mTestproject.setText(appendStr(R.string.testproject, mData.getTest_project()));
        String method = mData.getTest_method();
        if ("0".equals(method)) {
            mTestmethod.setText(R.string.print_mothed1);
        } else if ("1".equals(method)) {
            mTestmethod.setText(R.string.print_mothed2);
        } else if ("2".equals(method)) {
            mTestmethod.setText(R.string.print_mothed3);
        } else if ("3".equals(method)) {
            mTestmethod.setText(R.string.print_mothed4);
        } else {
            if (StringUtils.isEmpty(method)) {
                getTestMethod();
            } else {
                mTestmethod.setText(appendStr(R.string.method, method));
            }
        }
        mTestresult.setText(appendStr(R.string.testresult, mData.getTestresult()));
        mTeststandnum.setText(appendStr(R.string.judgment_criteria, mData.getStand_num()));
        mInspectedunit.setText(appendStr(R.string.inspectedunit, mData.getProsecutedunits()));
        mTestsite.setText(appendStr(R.string.testplace, mData.getTestsite()));
        mTestunit.setText(appendStr(R.string.detect_unit, mData.getTest_unit_name()));

    }

    private String appendStr(@StringRes int id, String content) {
        String string = getString(id);
        return string + "：" + content;
    }

    private void getTestMethod() {
        String testproject = mData.getUnique_testproject();
        String moudle = mData.getTest_Moudle();
        String tag = mData.getPlatform_tag();
        String value = "";

        if (moudle.equals(ArmsUtils.getString(getActivity(), R.string.FGGD_TestMoudle))) {
            List<FGGDTestItem> list = DBHelper.getFGGDTestItemDao(getActivity()).queryBuilder().where(FGGDTestItemDao.Properties.Unique_testproject.eq(testproject)).list();
            if (list.size() == 0) {
                value = getString(R.string.query_fail);
            } else {
                FGGDTestItem item = list.get(0);
                value = item.getUseMethod();
            }

        }


        mTestmethod.setText(appendStr(R.string.method, value));

    }

    @SuppressLint("SetTextI18n")
    private void setTestChartLine() {
        mGroupmessage.removeAllViews();
        if (mLineChart == null) {

            creatTestChartLine();
        }
        if (null != mImageJTJ) {
            mGroupmessage.addView(mImageJTJ);
        }

        TextView view = new TextView(getActivity());
        view.setText(mData.getReservedfield10() + "\r\n" + getString(R.string.txt_red_green_blue));

        mGroupmessage.addView(mLineChart);
    }


    private void setTestunit() {
        mGroupmessage.removeAllViews();
        if (mTextView_TestUnit == null) {
            creatTestUnitView();
        }
        mGroupmessage.addView(mTextView_TestUnit);
    }


    private void setTestsite() {
        mGroupmessage.removeAllViews();
        //map.setMaxAndMinZoomLevel(1,1);
        //options.mapStatus(msu);
        /*if (mMapView == null) {
            creatMapView();
        }
        mGroupmessage.addView(mMapView);*/
    }

    /*private void creatMapView() {
        BaiduMapOptions options = new BaiduMapOptions()
                .overlookingGesturesEnabled(false).rotateGesturesEnabled(false)
                // .scaleControlEnabled(false) //是否显示比例尺控件
                .scrollGesturesEnabled(false).zoomGesturesEnabled(false)
                .zoomControlsEnabled(false).compassEnabled(false);
        mMapView = new TextureMapView(getActivity(), options);
        BaiduMap map = mMapView.getMap();
        LatLng ll = new LatLng(mData.getLatitude(), mData.getLongitude());
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(ll);
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.dingwei_location);
        OverlayOptions option = new MarkerOptions()
                .position(ll)
                .icon(bitmap);
        map.animateMapStatus(msu);
        map.addOverlay(option);
        OverlayOptions overlayOptions = new CircleOptions()
                .center(ll)
                //设置圆的颜色
                .fillColor(Color.parseColor("#201c3d6f"))
                //设置边缘线的颜色
                .stroke(new Stroke(1, Color.parseColor("#ffffff")))
                //设置半径
                .radius(30);
        map.addOverlay(overlayOptions);
        MapStatusUpdate update = MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(20).build());
        map.setMapStatus(update);
    }*/

    private void setInspectedunit() {
        mGroupmessage.removeAllViews();
        TextView tv = new TextView(getActivity());
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        tv.setTextColor(Color.BLACK);
        tv.setWidth(RelativeLayout.LayoutParams.WRAP_CONTENT);
        String s = null;
        String beunit = mData.getUnique_beunit();

        List<Company_Point> list = DBHelper.getCompany_PointDao(getActivity()).queryBuilder().where(Company_PointDao.Properties.RegId.eq(beunit)).list();
        List<Company_Point_Unit> list1 = DBHelper.getCompany_Point_UnitDao(getActivity()).queryBuilder().where(Company_Point_UnitDao.Properties.CdId.eq(beunit)).list();
        if (list.size() == 0 && list1.size() == 0) {
            s = getString(R.string.query_fail);
        }
        if (list.size() != 0) {
            Company_Point point = list.get(0);
            s = point.toMyStringMessage();
        }
        if (list1.size() != 0) {
            Company_Point_Unit point = list1.get(0);
            s = point.toMyStringMessage();
        }


        tv.setText(s);
        mGroupmessage.addView(tv);
    }

    private void setTeststandnum() {
        mGroupmessage.removeAllViews();
        if (null == mPDFView) {

            String standNum = mData.getStand_num();
            String tag = mData.getPlatform_tag();

           /* String path = ArmsUtils.getString(getActivity(), R.string.app_localdata_address) + "/BiaoZhunKu";
            if (FileUtils.isFileExists(path)) {
                mPresenter.requestfile(path, standNum);
            } else {
                fundFail(getString(R.string.standard_file_not_found));
                ArmsUtils.snackbarText(getString(R.string.standard_file_not_found));
            }*/


        } else {
            creatPDFView(mPdfFile);
        }


    }

    @Override
    public void fundSuccess(File file) {
        if (null == file) {
            ArmsUtils.snackbarText(getString(R.string.file_is_empty));
            return;
        }
        mPdfFile = file;
        creatPDFView(mPdfFile);
    }

    @Override
    public void fundFail(String errmessage) {
        mGroupmessage.removeAllViews();
        TextView child = new TextView(getActivity());
        child.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        child.setTextColor(Color.BLACK);
        child.setWidth(RelativeLayout.LayoutParams.WRAP_CONTENT);
        child.setText(mData.getStand_num() + "\r\n" + errmessage);
        mGroupmessage.addView(child);
    }

    @Override
    public RxPermissions getRxPermissions() {
        return mRxPermissions;
    }

    private void creatPDFView(File file) {
        if (null == file) {
            return;
        }
        //mGroupmessage.removeAllViews();
        if (null == mPDFView) {
            mPDFView = new PDFView(getActivity(), null);
        }

        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                //判断PDF文件格式是否正确
                // emitter.onNext(check(file.getAbsolutePath()));
                emitter.onNext(true);
            }
        }).doOnError(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                ArmsUtils.snackbarText(throwable.getMessage());
                fundFail(throwable.getMessage());
            }
        })
                .subscribeOn(Schedulers.io())
                //.subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new ErrorHandleSubscriber<Boolean>(ArmsUtils.obtainAppComponentFromContext(this).rxErrorHandler()) {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            mPDFView
                                    .fromFile(file)
                                    .enableSwipe(true)
                                    .load();
                        } else {
                            ArmsUtils.snackbarText(getString(R.string.pdf_file_corruption));
                            fundFail(getString(R.string.pdf_file_corruption));
                        }

                    }
                });

        mGroupmessage.addView(mPDFView, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        LogUtils.d(mGroupmessage.getChildCount());

    }

    private void setTestResult() {
        mGroupmessage.removeAllViews();
        if (mTextView_TestResult == null) {
            creatTestResultView();
        }
        mGroupmessage.addView(mTextView_TestResult);
    }


    private void setTestMethod() {
        mGroupmessage.removeAllViews();

        if (mTextView_method == null) {
            creatMethodView();
        }
        mGroupmessage.addView(mTextView_method);
    }


    private void setProject() {
        mGroupmessage.removeAllViews();
        if (mTextView_project == null) {
            creatProjectView();
        }
        mGroupmessage.addView(mTextView_project);
    }

    private void setSample() {
        mGroupmessage.removeAllViews();
        if (mTextView_sample == null) {
            creatSampleView();
        }
        mGroupmessage.addView(mTextView_sample);
    }


    private void setQrcode() {
        mGroupmessage.removeAllViews();
        if (mQrcodeView == null) {
            creatQrcodeView();
        }
        mGroupmessage.addView(mQrcodeView);
        mGroupmessage.addView(mTextView_Qrcode);
    }

    private void creatTestResultView() {
        mTextView_TestResult = new TextView(getActivity());
        mTextView_TestResult.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        mTextView_TestResult.setTextColor(Color.BLACK);
        mTextView_TestResult.setWidth(RelativeLayout.LayoutParams.WRAP_CONTENT);
        String value;

        String testresult = mData.getTestresult();
        String cov = mData.getCov();
        String unit = mData.getCov_unit();
        String symbol = mData.getSymbol();
        String decisionoutcome = mData.getDecisionoutcome();
        double dilutionratio = mData.getDilutionratio();
        double everyresponse = mData.getEveryresponse();
        String controlvalue = mData.getControlvalue();
        String s = "";
        String s1 = "";
        String s2 = "";

        s = (symbol) + (cov) + (unit);
        s1 = testresult;


        s2 = decisionoutcome;


        value = getString(R.string.testresult) + "：" + s1 + "\r\n" +
                getString(R.string.limitvalue) + "：" + s + "\r\n" +
                getString(R.string.jujerment) + "：" + s2 + "\r\n" +
                getString(R.string.dilution_factor) + dilutionratio + "\r\n" +
                getString(R.string.number_reaction_drops) + everyresponse + "\r\n" +
                getString(R.string.controlvalue) + controlvalue;
        mTextView_TestResult.setText(value);
    }

    private void creatMethodView() {
        mTextView_method = new TextView(getActivity());
        mTextView_method.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        mTextView_method.setTextColor(Color.BLACK);
        mTextView_method.setWidth(RelativeLayout.LayoutParams.WRAP_CONTENT);
        String testproject = mData.getUnique_testproject();
        String moudle = mData.getTest_Moudle();
        String tag = mData.getPlatform_tag();
        String value = "";
        BaseProjectMessage item;

        if (moudle.equals(ArmsUtils.getString(getActivity(), R.string.FGGD_TestMoudle))) {
            List<FGGDTestItem> list = DBHelper.getFGGDTestItemDao(getActivity()).queryBuilder().where(FGGDTestItemDao.Properties.Unique_testproject.eq(testproject)).list();
            if (list.size() == 0) {
                value = getString(R.string.query_fail);
            } else {
                item = list.get(0);
                value = item.toMyStringMethod();
            }

        }


        mTextView_method.setText(value);
    }

    private void creatProjectView() {
        mTextView_project = new TextView(getActivity());
        mTextView_project.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        mTextView_project.setTextColor(Color.BLACK);
        mTextView_project.setWidth(RelativeLayout.LayoutParams.WRAP_CONTENT);
        String testproject = mData.getUnique_testproject();
        String moudle = mData.getTest_Moudle();
        String tag = mData.getPlatform_tag();
        String value = null;
        BaseProjectMessage item;

        if (moudle.equals(ArmsUtils.getString(getActivity(), R.string.JTJ_TestMoudle_P))) {
            List<JTJTestItem> list = DBHelper.getJTJTestItemDao(getActivity()).queryBuilder().where(JTJTestItemDao.Properties.Unique_testproject.eq(testproject)).list();
            if (list.size() == 0) {
                value = getString(R.string.query_fail);
            } else {
                item = list.get(0);
                value = item.toMyStringProject();
            }

        } else if (moudle.equals(ArmsUtils.getString(getActivity(), R.string.FGGD_TestMoudle))) {
            List<FGGDTestItem> list = DBHelper.getFGGDTestItemDao(getActivity()).queryBuilder().where(FGGDTestItemDao.Properties.Unique_testproject.eq(testproject)).list();
            if (list.size() == 0) {
                value = getString(R.string.query_fail);
            } else {
                item = list.get(0);
                value = item.toMyStringProject();
            }

        }


        LogUtils.d("project   " + value);
        mTextView_project.setText(value);
    }

    private void creatTestChartLine() {
        mImageJTJ = new ImageView(getActivity());

        Bitmap bitmap;
        bitmap = FileUtils.getBitmapByUUID_LEVEL1(mData.getSysCode());
        if (null == bitmap) {
            bitmap = FileUtils.getBitmapByUUID_LEVEL2(mData.getSysCode());
        }
        LogUtils.d(mData);

        if (mData.getTest_moudle().contains(getString(R.string.JTJ_TestMoudle_P))) {
            List<Bitmap> list = new ArrayList<>();
            list.add(bitmap);
            Bitmap bitmap0 = FileUtils.getBitmapByUUID_LEVEL2(mData.getSysCode());
            if (null != bitmap0) {
                list.add(bitmap0);
            }
            Bitmap bitmap1 = FileUtils.getBitmapByUUID_LEVEL2(mData.getSysCode() + 1);
            if (null != bitmap1) {
                list.add(bitmap1);
            }
            Bitmap bitmap2 = FileUtils.getBitmapByUUID_LEVEL2(mData.getSysCode() + 2);
            if (null != bitmap2) {
                list.add(bitmap2);
            }
            Bitmap bitmap3 = FileUtils.getBitmapByUUID_LEVEL2(mData.getSysCode() + 3);
            if (null != bitmap3) {
                list.add(bitmap3);
            }
            Bitmap bitmap4 = FileUtils.getBitmapByUUID_LEVEL2(mData.getSysCode() + 4);
            if (null != bitmap4) {
                list.add(bitmap4);
            }
            Bitmap bitmap5 = FileUtils.getBitmapByUUID_LEVEL2(mData.getSysCode() + 5);
            if (null != bitmap5) {
                list.add(bitmap5);
            }
            Bitmap bitmap6 = FileUtils.getBitmapByUUID_LEVEL2(mData.getSysCode() + 6);
            if (null != bitmap6) {
                list.add(bitmap6);
            }
            bitmap = BitmapUtils.mergeBitmap(list);
        }


        mImageJTJ.setImageBitmap(bitmap);
        mLineChart = new LineChart(getActivity());

        mLineChart.setLayoutParams(new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        mLineChart.setMinimumHeight(200);

        if (mData.getTest_moudle().contains(getString(R.string.JTJ_TestMoudle_P))) {
            List<List<Float>> listArrayList = new ArrayList<>();
            for (int j = -1; j < 6; j++) {
                List<JTJPoint> list;
                if (j == -1) {
                    list = mJTJPointDao.queryBuilder().where(JTJPointDao.Properties.Uuid.eq(mData.getSysCode())).list();
                } else {
                    list = mJTJPointDao.queryBuilder().where(JTJPointDao.Properties.Uuid.eq(mData.getSysCode() + "" + (j + 1))).list();
                }
                LogUtils.d(list);
                if (list.size() != 0) {
                    JTJPoint point = list.get(0);
                    String data = point.getPointData();
                    String[] split = data.split(",");
                    List<Float> floatList = new ArrayList<>();
                    for (int i = 0; i < split.length; i++) {
                        floatList.add(Float.parseFloat(split[i]));
                    }
                    listArrayList.add(floatList);
                }
            }
            LineData data1 = getLineChartData(listArrayList, mLineChart, 0, 0);
            data1.setValueTypeface(mTf);
            setupChart(mLineChart, data1);

        } else {

            String code = mData.getSysCode();
            LogUtils.d(code);
            List<JTJPoint> list = mJTJPointDao.queryBuilder().where(JTJPointDao.Properties.Uuid.eq(code)).list();
            if (list.size() != 0) {
                JTJPoint point = list.get(0);
                String data = point.getPointData();
                String[] split = data.split(",");
                List<Float> floatList = new ArrayList<>();
                for (int i = 0; i < split.length; i++) {
                    floatList.add(Float.parseFloat(split[i]));
                }
                List<List<Float>> listArrayList = new ArrayList<>();
                listArrayList.add(floatList);
                LineData data1;

                data1 = getLineChartData(listArrayList, mLineChart, 0, 0);

                data1.setValueTypeface(mTf);
                setupChart(mLineChart, data1);

            }


        }


    }


    private void setupChart(LineChart chart, LineData data) {
        LogUtils.d(chart.toString());
        LogUtils.d(data.toString());
        LineDataSet dataSetByIndex = (LineDataSet) data.getDataSetByIndex(0);
        LogUtils.d(dataSetByIndex);
        if (dataSetByIndex == null) {
            return;
        }
        dataSetByIndex.setCircleHoleColor(Color.rgb(0, 200, 245));


        chart.setDrawGridBackground(false);

        chart.getDescription().setEnabled(false);


        chart.setDrawBorders(false);

        chart.getAxisRight().setEnabled(false);
        YAxis left = chart.getAxisLeft();

        left.setEnabled(true);
        left.setDrawAxisLine(true);
        left.setDrawGridLines(false);
        XAxis axis = chart.getXAxis();
        axis.setDrawAxisLine(true);
        axis.setDrawGridLines(false);

        // enable touch gestures
        chart.setTouchEnabled(true);

        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(false);
        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setDrawInside(false);
        l.setEnabled(false);
        chart.resetTracking();
        chart.invalidate();


        // add data
        chart.setData(data);


        //chart.setMarker(new MyMaker());

    }


    private LineData getLineChartData(List<List<Float>> bytes, LineChart mChart, int v1, int v2) {
        LogUtils.d(bytes.size());
        LineData data = new LineData();
        for (int j = 0; j < bytes.size(); j++) {
            List<Float> floats = bytes.get(j);
            ArrayList<Integer> colors = new ArrayList<Integer>();
            ArrayList<Entry> values = new ArrayList<>();

            for (int i = 0; i < floats.size(); i++) {

                values.add(new Entry(i, floats.get(i)));
            }


            // create a dataset and give it a type
            LineDataSet set1;
            // set1.setFillAlpha(110);
            // set1.setFillColor(Color.RED);


            if (mChart.getData() != null &&
                    mChart.getData().getDataSetCount() > 0) {
                mChart.getData().removeDataSet(0);
            }
            // create a dataset and give it a type
            set1 = new LineDataSet(values, "");

            set1.setDrawIcons(false);

            set1.setColor(Color.BLACK);
            /******************************/
            //循环判断y值，并向颜色集合中添加对应的颜色
            for (int i = 0; i < floats.size(); i++) {
                if (j == 0) {
                    colors.add(Color.RED);
                } else if (j == 1) {
                    colors.add(Color.GREEN);
                } else if (j == 2) {
                    colors.add(Color.BLUE);
                } else if (j == 3) {
                    colors.add(Color.BLACK);
                } else if (j == 4) {
                    colors.add(Color.CYAN);
                } else if (j == 5) {
                    colors.add(Color.YELLOW);
                }

            }


            set1.setCircleColors(colors);


            set1.setLineWidth(1.75f);
            set1.setCircleRadius(4f);
            set1.setCircleHoleRadius(1.5f);
            //set1.setColor(Color.WHITE);
            set1.setColors(colors);
            //set1.setCircleColor(Color.WHITE);
            set1.setHighLightColor(Color.GREEN);
            set1.setDrawValues(true);

            // create a data object with the data sets

            data.addDataSet(set1);
        }

        return data;
    }

    private void creatSampleView() {
        mTextView_sample = new TextView(getActivity());
        mTextView_sample.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        mTextView_sample.setTextColor(Color.BLACK);
        mTextView_sample.setWidth(RelativeLayout.LayoutParams.WRAP_CONTENT);
        //tv.setGravity(Gravity.CENTER);
        /*LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mGroupmessage.getLayoutParams());
        params.gravity=Gravity.CENTER;
        tv.setLayoutParams(params);*/
        String sample = mData.getUnique_sample();
        String tag = mData.getPlatform_tag();
        String s = "";
        if ("0".equals(tag)) {
            List<FoodItemAndStandard> list = DBHelper.getFoodItemAndStandardDao(getActivity()).queryBuilder().where(FoodItemAndStandardDao.Properties.CheckId.eq(sample)).list();
            if (list.size() != 0) {
                s = list.get(0).toMyString();
            }
        }


        mTextView_sample.setText("".equals(s) ? getString(R.string.local_sample_not_found) : s);

    }

    TextView mTextView_Qrcode;

    private void creatQrcodeView() {
        mQrcodeView = new ImageView(getActivity());
        String content = mData.toMyString();
        Bitmap bitmap = QRCodeUtils.generateBitmap(content, 400, 400);
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.ic);
        Bitmap bitmap1 = QRCodeUtils.addLogo(bitmap, bmp);
        mQrcodeView.setImageBitmap(bitmap1);
        mTextView_Qrcode = new TextView(getActivity());
        mTextView_Qrcode.setText(content);
        mTextView_Qrcode.setWidth(RelativeLayout.LayoutParams.WRAP_CONTENT);
        mTextView_Qrcode.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        //mMapView.setGravity(Gravity.CENTER);
        mTextView_Qrcode.setTextColor(Color.BLACK);

    }

    private void creatTestUnitView() {
        mTextView_TestUnit = new TextView(getActivity());
        mTextView_TestUnit.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        mTextView_TestUnit.setTextColor(Color.BLACK);
        mTextView_TestUnit.setWidth(RelativeLayout.LayoutParams.WRAP_CONTENT);
        mTextView_TestUnit.setText(getString(R.string.detect_unit_name) + mData.getTest_unit_name() + "\r\n" + getString(R.string.detect_unit_id) + mData.getTest_unit_id() + "\r\n");
    }

    @Override
    public void showLoading() {
        if (!mSportdialog.isShowing()) {
            mSportdialog.show();
        }
    }

    @Override
    public void hideLoading() {
        if (mSportdialog.isShowing()) {
            mSportdialog.dismiss();
        }
    }

    @Override
    public Activity getActivity() {
        WeakReference<Activity> abcWeakRef = new WeakReference<Activity>(this);

        return abcWeakRef.get();
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
}