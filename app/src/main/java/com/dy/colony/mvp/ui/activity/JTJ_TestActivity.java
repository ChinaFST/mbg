package com.dy.colony.mvp.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
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
                startTest();
                break;
            case R.id.iv_record:
                startActivity(new Intent(getActivity(), TestRecordNewActivity.class));
                //dealImage();
                break;
            default:
                break;


        }
    }

    private void dealImage() {
        //String path = "/storage/emulated/0/dayuan/ic_.jpg";
        //makeTransparentWithScale(this, path, "ic_001.png", 0.4f);
        String path = "/storage/emulated/0/dayuan/ic_splash.png";
        processAndSaveSplashIcon(this, path, "ic_splash_001.png");

    }

    /**
     * 处理原始 PNG 并保存到应用的 files 目录中
     *
     * @param context        上下文
     * @param inputPath      原图的绝对路径 (例如外部存储或缓存路径)
     * @param outputFileName 保存的文件名 (例如 "ic_splash_adaptive.png")
     * @return 成功保存后的文件对象，失败返回 null
     */
    public static File processAndSaveSplashIcon(Context context, String inputPath, String outputFileName) {
        // 1. 从路径加载原始位图
        Bitmap originalBitmap = BitmapFactory.decodeFile(inputPath);
        if (originalBitmap == null) return null;

        int origWidth = originalBitmap.getWidth();
        int origHeight = originalBitmap.getHeight();

        // 2. 计算正方形画布大小
        // 为了确保圆不切到 Logo，我们让画布比 Logo 长边大出约 40% (即 Logo 占比约 60%)
        int maxSide = Math.max(origWidth, origHeight);
        int targetSize = (int) (maxSide * 1.6f);

        // 3. 创建透明正方形画布
        Bitmap outputBitmap = Bitmap.createBitmap(targetSize, targetSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(outputBitmap);

        // 4. 计算居中位置
        int left = (targetSize - origWidth) / 2;
        int top = (targetSize - origHeight) / 2;

        // 5. 绘制（保持原图大小居中，四周留出空白）
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(originalBitmap, left, top, paint);

        // 6. 保存到应用的 /data/user/0/包名/files 目录
        File outputFile = new File(context.getFilesDir(), outputFileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(outputFile);
            outputBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            return outputFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException ignored) {
                }
            }
            // 释放内存
            originalBitmap.recycle();
            outputBitmap.recycle();
        }
    }


    /**
     * 带有缩放功能的透明处理方法
     *
     * @param scale 缩放比例 (0.0 < scale <= 1.0)
     */
    public String makeTransparentWithScale(Context context, String inputPath, String fileName, float scale) {
        // 1. 加载并缩放原图
        BitmapFactory.Options options = new BitmapFactory.Options();

        // 性能优化：如果缩放比例很小，先通过采样率减少内存占用
        if (scale < 0.5f) {
            options.inSampleSize = 2; // 缩小为原来的 1/2
        }

        Bitmap tempSource = BitmapFactory.decodeFile(inputPath, options);
        if (tempSource == null) return null;

        // 精确缩放：如果需要更精确的比例，使用 Matrix
        Bitmap source;
        if (scale != 1.0f) {
            int targetWidth = Math.round(tempSource.getWidth() * (scale * (options.inSampleSize > 0 ? options.inSampleSize : 1)));
            int targetHeight = Math.round(tempSource.getHeight() * (scale * (options.inSampleSize > 0 ? options.inSampleSize : 1)));
            source = Bitmap.createScaledBitmap(tempSource, targetWidth, targetHeight, true);
            if (tempSource != source) tempSource.recycle(); // 释放临时位图
        } else {
            source = tempSource;
        }

        int width = source.getWidth();
        int height = source.getHeight();

        // 2. 创建副本
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        // 3. 批量读取像素
        int[] pixels = new int[width * height];
        source.getPixels(pixels, 0, width, 0, 0, width, height);

        // 4. 透明化逻辑处理
        float maxLuminance = 250f;
        float minLuminance = 200f;
        int transparentWhite = Color.argb(0, 255, 255, 255);

        for (int i = 0; i < pixels.length; i++) {
            int color = pixels[i];
            int r = Color.red(color);
            int g = Color.green(color);
            int b = Color.blue(color);

            float luminance = 0.2126f * r + 0.7152f * g + 0.0722f * b;

            if (luminance > maxLuminance) {
                pixels[i] = transparentWhite;
            } else if (luminance < minLuminance) {
                pixels[i] = Color.argb(255, r, g, b);
            } else {
                float alphaScale = 1f - (luminance - minLuminance) / (maxLuminance - minLuminance);
                int alpha = (int) (255 * alphaScale);
                pixels[i] = Color.argb(alpha, r, g, b);
            }
        }

        // 5. 保存
        result.setPixels(pixels, 0, width, 0, 0, width, height);
        File outFile = new File(context.getFilesDir(), fileName);
        try (FileOutputStream fos = new FileOutputStream(outFile)) {
            result.compress(Bitmap.CompressFormat.PNG, 100, fos);
            return outFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (source != null && !source.isRecycled()) source.recycle();
            if (result != null && !result.isRecycled()) result.recycle();
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