package com.dy.colony.mvp.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.apkfuns.logutils.LogUtils;
import com.dy.colony.Constants;
import com.dy.colony.MyAppLocation;
import com.dy.colony.R;
import com.dy.colony.di.component.DaggerJTJ_TestComponent;
import com.dy.colony.greendao.beans.Detection_Record_FGGD_NC;
import com.dy.colony.greendao.beans.JTJTestItem;
import com.dy.colony.greendao.daos.JTJTestItemDao;
import com.dy.colony.mvp.contract.JTJ_TestContract;
import com.dy.colony.mvp.model.entity.base.BaseProjectMessage;
import com.dy.colony.mvp.model.entity.base.GalleryBean;
import com.dy.colony.mvp.model.entity.eventbus.ExternTestMessageBean;
import com.dy.colony.mvp.presenter.JTJ_TestPresenter;
import com.dy.colony.mvp.ui.widget.BaseJTJTestView;
import com.dy.colony.mvp.ui.widget.MyJTJ_TestView_External;
import com.dy.colony.mvp.ui.widget.MyJavaCameraView;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.Utils;
import org.opencv.core.Mat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.jess.arms.utils.Preconditions.checkNotNull;
import static org.opencv.android.CameraBridgeViewBase.CAMERA_ID_FRONT;

public class JTJ_TestActivity extends BaseActivity<JTJ_TestPresenter> implements JTJ_TestContract.View {
    @Inject
    List<GalleryBean> mList;
    @Inject
    AlertDialog mSportDialog;
    @Inject
    UsbManager mUsbManager;
    @Inject
    Dialog mDialog;
    @Inject
    JTJTestItemDao mJTJTestItemDao;

    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @BindView(R.id.toolbar_back)
    RelativeLayout mToolbarBack;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.group_chart)
    LinearLayout mGroupChart;
    @BindView(R.id.message_gallnum)
    TextView mMessageGallnum;
    @BindView(R.id.choseproject)
    Button mChoseproject;
    @BindView(R.id.samplename_btn)
    Button mSamplenameBtn;
    @BindView(R.id.samplename)
    AutoCompleteTextView mSamplename;
    @BindView(R.id.samplename_layout)
    TextInputLayout mSamplenameLayout;
    @BindView(R.id.samplenum)
    AutoCompleteTextView mSamplenum;
    @BindView(R.id.samplenum_layout)
    TextInputLayout mSamplenumLayout;
    @BindView(R.id.samplenum_btn)
    Button mSamplenumBtn;
    @BindView(R.id.unit_btn)
    Button mUnitBtn;
    @BindView(R.id.btn_change_sampleplace)
    Button mBtnChangeSampleplace;
    @BindView(R.id.message_method)
    AutoCompleteTextView mMessageMethod;
    @BindView(R.id.message_standnum)
    AutoCompleteTextView mMessageStandnum;
    @BindView(R.id.message_limitvalue)
    AutoCompleteTextView mMessageLimitvalue;
    @BindView(R.id.message_result)
    AutoCompleteTextView mMessageResult;
    @BindView(R.id.message_jujement)
    AutoCompleteTextView mMessageJujement;
    @BindView(R.id.message_testtime)
    AutoCompleteTextView mMessageTesttime;
    @BindView(R.id.message_testsite)
    AutoCompleteTextView mMessageTestsite;
    @BindView(R.id.message_testpeople)
    AutoCompleteTextView mMessageTestpeople;
    @BindView(R.id.start_test)
    FloatingActionButton mStartTest;
    @BindView(R.id.toolbarly)
    AppBarLayout mToolbarly;
    @BindView(R.id.myjavacam)
    MyJavaCameraView mJavacamrea;
    @BindView(R.id.retestdata)
    Button mRetestdata;
    @BindView(R.id.parent_layout)
    LinearLayout mParentLayout;

    private List<BaseJTJTestView> mJTJTestViews = new ArrayList<>();
    private GalleryBean nowShowBean;
    private int mIndex;

    private int type;
    private Mat mat;

    private Bitmap mBitmap1;
    private Bitmap mBitmap2;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerJTJ_TestComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_jtj_test; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    private boolean isShow = false;

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        if (getIntent() != null) {
            type = getIntent().getIntExtra(Constants.JUMP_PARAM_TYPE, 0);
        }

        initGallery();
        setLinstenter();
        initLongClickListener();
        initOnTouchListener();
        mToolbarTitle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (isShow) {
                    ArmsUtils.snackbarText(getString(R.string.hideresult));
                    isShow = false;
                } else {
                    ArmsUtils.snackbarText(getString(R.string.showresult));
                    isShow = true;
                }
                for (int i = 0; i < mJTJTestViews.size(); i++) {
                    mJTJTestViews.get(i).showTestResultNumber(isShow);
                }
                return true;
            }
        });
        mJavacamrea.setCameraPermissionGranted();
        mJavacamrea.setVisibility(SurfaceView.VISIBLE);
        mJavacamrea.setCvCameraViewListener(new CameraBridgeViewBase.CvCameraViewListener() {
            @Override
            public void onCameraViewStarted(int width, int height) {

            }

            @Override
            public void onCameraViewStopped() {

            }

            @Override
            public Mat onCameraFrame(Mat inputFrame) {
                if (mBitmap1 == null) {
                    mBitmap1 = Bitmap.createBitmap(inputFrame.width() / 2, inputFrame.height(), Bitmap.Config.ARGB_8888);
                }
                if (mBitmap2 == null) {
                    mBitmap2 = Bitmap.createBitmap(inputFrame.width() / 2, inputFrame.height(), Bitmap.Config.ARGB_8888);
                }
                if (mat != null) {
                    mat.release();
                }
                // mat = inputFrame.clone();
                //Log.d("wzx","  "+inputFrame.width() + " " + inputFrame.height());
                Mat mMat1 = inputFrame.submat(0, inputFrame.rows(), 0, inputFrame.cols() / 2);
                MyJTJ_TestView_External myJTJ_testView_external1 = (MyJTJ_TestView_External) mJTJTestViews.get(0);
                Utils.matToBitmap(mMat1, mBitmap1);
                myJTJ_testView_external1.onReciverSuccess(mBitmap1);
                Mat mMat2 = inputFrame.submat(0, inputFrame.rows(), inputFrame.cols() / 2, inputFrame.cols());
                MyJTJ_TestView_External myJTJ_testView_external2 = (MyJTJ_TestView_External) mJTJTestViews.get(1);
                Utils.matToBitmap(mMat2, mBitmap2);
                myJTJ_testView_external2.onReciverSuccess(mBitmap2);
                //Imgproc.rectangle(inputFrame, rect_show,new Scalar(255,0,0,255),2);
                return inputFrame;
            }
        });
        mJavacamrea.enableFpsMeter();
        mJavacamrea.setCameraIndex(CAMERA_ID_FRONT);

    }

    @Override
    public void onPause() {
        super.onPause();
        if (mJavacamrea != null) {
            mJavacamrea.disableView();
        }
        MyAppLocation.myAppLocation.mSerialDataService.mData_SerialControl.send(new byte[]{0x7E, 0x14, 0x01, 0x00, 0x00, (byte) 0xCC, (byte) 0xAA});
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mJavacamrea != null) {
            mJavacamrea.enableView();
        }
        MyAppLocation.myAppLocation.mSerialDataService.mData_SerialControl.send(new byte[]{0x7E, 0x14, 0x01, 0x00, 0x01, (byte) 0xCC, (byte) 0xAA});
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mJavacamrea != null) {
            mJavacamrea.disableView();
        }
    }

    private void initGallery() {
        mGroupChart.removeAllViews();
        mJTJTestViews.clear();
        MyAppLocation.myAppLocation.mSerialDataService.mJTJGalleryBeanList.clear();
        JTJTestItem project = getProject();

        for (int i = 0; i < 2; i++) {
            GalleryBean e = new Detection_Record_FGGD_NC();
            e.setGalleryNum(i + 1);
            e.setTestMoudle(2 + "");
            e.setJTJModel(1);
            e.setJTJCardModel(0);
            if (project != null) {
                ((Detection_Record_FGGD_NC) e).setProjectMessage(project);
            }
            MyAppLocation.myAppLocation.mSerialDataService.mJTJGalleryBeanList.add(e);
            MyJTJ_TestView_External p = new MyJTJ_TestView_External(this, i); //新建通道，将baan与该自定义view绑定
            mJTJTestViews.add(p);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mGroupChart.addView(p, layoutParams);//添加到视图容器
            LogUtils.d(e);
            LogUtils.d(mGroupChart.getChildCount());
        }

        EventBus.getDefault().post(new ExternTestMessageBean(0, mIndex));
    }


    private JTJTestItem getProject() {
        JTJTestItem projectMessage = null;
        if (type == 1) {
            List<JTJTestItem> list = mJTJTestItemDao.queryBuilder().where(JTJTestItemDao.Properties.ProjectName.eq(getString(R.string.halal_verification))).list();
            if (!list.isEmpty()) {
                projectMessage = list.get(0);
            }
        } else if (type == 2) {
            List<JTJTestItem> list = mJTJTestItemDao.queryBuilder().where(JTJTestItemDao.Properties.ProjectName.eq(getString(R.string.pork_alcohol))).list();
            if (!list.isEmpty()) {
                projectMessage = list.get(0);
            }
        }
        return projectMessage;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        EventBus.getDefault().post(new ExternTestMessageBean(0, mIndex));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ExternTestMessageBean bean) {
        LogUtils.d(bean);
        if (bean.tag == 0) {
            if (bean.index != -1) {
                mIndex = bean.index;
            }
            LogUtils.d(mIndex);
            nowShowBean = MyAppLocation.myAppLocation.mSerialDataService.mJTJGalleryBeanList.get(mIndex);
            initMessage(nowShowBean);
        }

    }

    @SuppressLint("SetTextI18n")
    private void initMessage(GalleryBean nowShowBean) {
        mMessageGallnum.setText(getString(R.string.gallery) + nowShowBean.getJTJ_MAC() + getString(R.string.detail_msg));
        Detection_Record_FGGD_NC bean = (Detection_Record_FGGD_NC) nowShowBean;

        mChoseproject.setText(bean.getTest_project());
        mChoseproject.setHint(getString(R.string.testproject));
        mSamplenameBtn.setText(bean.getSamplename());
        mSamplenumBtn.setText(bean.getSerialNumber());
        mUnitBtn.setText(bean.getProsecutedunits());
        mBtnChangeSampleplace.setText(bean.getSampleplace());
        mMessageMethod.setText(bean.getTest_method());
        mMessageStandnum.setText(bean.getStand_num());
        mMessageLimitvalue.setText(bean.getSymbol() + bean.getCov() + bean.getCov_unit());
        mMessageResult.setText(bean.getTestresult());
        mMessageJujement.setText(bean.getDecisionoutcome());
        mMessageTesttime.setText(bean.getdfTestingtimeyy_mm_dd_hh_mm_ss());
        mMessageTestsite.setText(bean.getTestsite());
        mMessageTestpeople.setText(bean.getInspector());
    }


    private void setLinstenter() {
        mSamplename.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString()) && mSamplenameLayout.getVisibility() == VISIBLE) {
                    ((Detection_Record_FGGD_NC) MyAppLocation.myAppLocation.mSerialDataService.mJTJGalleryBeanList.get(mIndex)).setSamplenameByself(s.toString());
                }

            }
        });
        mSamplenum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString()) && mSamplenumLayout.getVisibility() == VISIBLE) {
                    ((Detection_Record_FGGD_NC) MyAppLocation.myAppLocation.mSerialDataService.mJTJGalleryBeanList.get(mIndex)).setSerialNumber(s.toString());
                }

            }
        });

    }


    private void initLongClickListener() {
        mSamplenameBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (nowShowBean.getState() == 1) {
                    ArmsUtils.snackbarText(getString(R.string.testing));
                    return true;
                }
                if (null == nowShowBean.getProjectMessage()) {
                    ArmsUtils.snackbarText(getString(R.string.choseprojectFirst));
                    return true;
                }
                nowShowBean.removeSampleMessage();
                mSamplenameBtn.setVisibility(GONE);
                mSamplenameLayout.setVisibility(VISIBLE);
                return true;
            }
        });

        mSamplename.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (nowShowBean.getState() == 1) {
                    ArmsUtils.snackbarText(getString(R.string.testing));
                    return true;
                }
                mSamplenameLayout.setVisibility(GONE);
                mSamplenameBtn.setVisibility(VISIBLE);
                return true;
            }
        });

    }

    private void initOnTouchListener() {
        mBtnChangeSampleplace.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (null == nowShowBean) {
                        return true;
                    }
                    mPresenter.makeChoseSampleplace(mIndex);
                }
                return true;
            }
        });


    }


    @OnClick({R.id.choseproject, R.id.samplename_btn, R.id.samplenum_btn, R.id.unit_btn, R.id.start_test, R.id.iv_record})
    public void onClick(View view) {
        int state;
        Intent intent;
        switch (view.getId()) {
            case R.id.choseproject:
                //选择检测项目
                state = nowShowBean.getState();
                if (state == 1) {
                    ArmsUtils.snackbarText(getString(R.string.testing_wait));
                    return;
                }

                intent = new Intent(getActivity(), ChoseProjectActivity.class);
                intent.putExtra("from", "jtj");
                intent.putExtra("index", mIndex);
                startActivity(intent);

                break;
            case R.id.samplename_btn:
                //选择样品名称
                state = nowShowBean.getState();
                if (state == 1) {
                    ArmsUtils.snackbarText(getString(R.string.testing_wait));
                    return;
                }
                if (null == nowShowBean.getProjectMessage()) {
                    ArmsUtils.snackbarText(getString(R.string.place_choseproject));
                    return;
                }
                intent = new Intent(getActivity(), ChoseSampleActivity.class);
                intent.putExtra("from", "jtj");
                intent.putExtra("index", mIndex);
                startActivity(intent);

                break;
            case R.id.samplenum_btn:

                break;
            case R.id.unit_btn:
                //选择被检单位
                /*state = nowShowBean.getState();
                if (state == 1) {
                    ArmsUtils.snackbarText(getString(R.string.testing_wait));
                    return;
                }
                intent = new Intent(getActivity(), ChoseUnitActivity.class);
                intent.putExtra("from", "jtj");
                intent.putExtra("index", mIndex);
                startActivity(intent);*/
                break;

            case R.id.start_test:
                //mPresenter.makeChoseSeachMethodDialog(mJTJTestViews);
                startTest();
                break;
            case R.id.iv_record:
                startActivity(new Intent(getActivity(), TestRecordNewActivity.class));
                break;
            default:
                break;


        }
    }

    private void dealImage() {
        String path = "/storage/emulated/0/dayuan/001.jpg";
        makeTransparentSmooth(this, path, "001_1.png");
        String path1 = "/storage/emulated/0/dayuan/002.jpg";
        makeTransparentSmooth(this, path1, "002_1.png");
        String path2 = "/storage/emulated/0/dayuan/003.jpg";
        makeTransparentSmooth(this, path2, "003_1.png");
    }

    public String makeTransparentSmooth(Context context, String inputPath, String fileName) {
        // 1. 加载原图（注意：大图可能导致 OOME，建议在实际项目中根据需求做采样缩放）
        Bitmap source = BitmapFactory.decodeFile(inputPath);
        if (source == null) return null;

        // 2. 创建一个支持透明度的副本
        int width = source.getWidth();
        int height = source.getHeight();
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        // 3. 批量读取像素
        int[] pixels = new int[width * height];
        source.getPixels(pixels, 0, width, 0, 0, width, height);

        // 4. 定义平滑处理的亮度阈值
        // 亮度 > 250 的认为是纯背景（全透明）
        // 亮度 < 200 的认为是物体核心（不透明）
        // 200-250 之间进行线性 Alpha 映射（平滑边缘）
        float maxLuminance = 250f;
        float minLuminance = 200f;

        for (int i = 0; i < pixels.length; i++) {
            int color = pixels[i];
            int r = Color.red(color);
            int g = Color.green(color);
            int b = Color.blue(color);

            // 计算亮度 (感知亮度公式)
            float luminance = 0.2126f * r + 0.7152f * g + 0.0722f * b;

            if (luminance > maxLuminance) {
                // 纯白背景：全透明
                pixels[i] = Color.TRANSPARENT;
            } else if (luminance < minLuminance) {
                // 物体内部：保持原色，不透明
                // 注意：由于原本可能是 JPG，没有 Alpha，这里强制设为 255
                pixels[i] = Color.argb(255, r, g, b);
            } else {
                // 边缘过渡地带：根据亮度计算 Alpha
                // 亮度越高（越接近白色），Alpha 越小（越透明）
                float alphaScale = 1f - (luminance - minLuminance) / (maxLuminance - minLuminance);
                int alpha = (int) (255 * alphaScale);

                // 关键优化：为了防止边缘出现“白边”，我们可以轻微调暗边缘像素的 RGB
                // 这样半透明的像素就不会带着明显的白色底色
                pixels[i] = Color.argb(alpha, r, g, b);
            }
        }

        // 5. 写回并保存
        result.setPixels(pixels, 0, width, 0, 0, width, height);

        File outFile = new File(context.getFilesDir(), fileName);
        try (FileOutputStream fos = new FileOutputStream(outFile)) {
            // 必须 PNG 才能保存透明通道
            result.compress(Bitmap.CompressFormat.PNG, 100, fos);
            return outFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            // 及时回收大图内存
            if (!source.isRecycled()) source.recycle();
            if (!result.isRecycled()) result.recycle();
        }
    }


    private void startTest() {
        for (BaseJTJTestView mJTJTestView : mJTJTestViews) {
            mJTJTestView.startTest();
        }

    }

    @Override
    public void showSportDialog(String message) {
        mSportDialog.setMessage(message);
        if (!mSportDialog.isShowing()) {
            mSportDialog.show();
        }

    }

    @Override
    public void hideSportDialog() {
        if (mSportDialog.isShowing()) {
            mSportDialog.dismiss();
        }
    }

    @Override
    public Activity getActivity() {
        return this;
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