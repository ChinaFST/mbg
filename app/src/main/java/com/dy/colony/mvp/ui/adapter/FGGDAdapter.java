package com.dy.colony.mvp.ui.adapter;

import android.content.DialogInterface;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.apkfuns.logutils.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dy.colony.Constants;
import com.dy.colony.MyAppLocation;
import com.dy.colony.R;
import com.dy.colony.app.utils.DiaLogUtils;
import com.dy.colony.greendao.beans.Detection_Record_FGGD_NC;
import com.dy.colony.mvp.model.entity.base.BaseProjectMessage;
import com.dy.colony.mvp.model.entity.base.GalleryBean;
import com.dy.colony.mvp.model.entity.eventbus.FGTestMessageBean;
import com.jess.arms.utils.ArmsUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

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
 * 　　　　┃　　　┃    神兽保佑,代码无bug
 * 　　　　┃　　　┃
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * <p>
 * Created by wangzhenxiong on 2019/3/7.
 */
public class FGGDAdapter extends BaseQuickAdapter<GalleryBean, BaseViewHolder> {

    public FGGDAdapter(int layoutResId, @Nullable List<GalleryBean> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, GalleryBean item) {
        //显示设置检测项目，样品名称，被检单位，检测任务等信息
        Detection_Record_FGGD_NC item2 = (Detection_Record_FGGD_NC) item;
        int num = item.getGalleryNum();
        helper.setText(R.id.fg_item_btn_choseproject, item2.getTest_project())
                .setText(R.id.fg_item_gallerynum, num + "")
                .setText(R.id.fg_item_btn_chosesample, item2.getSamplename())
                .setText(R.id.fg_testresult, "".equals(item2.getTestresult()) ? "" : item2.getTestresult() + item2.getCov_unit())
                .setText(R.id.fg_item_chose_task, item2.getReservedfield6());
        if (item2.getTest_project().equals(mContext.getString(R.string.rice_freshness))) {
            helper.setText(R.id.fg_testresult, item2.getTestresult());
        }

        if (item.getDowhat() == 2) {
            BaseProjectMessage message = item.getProjectMessage();
            if (null != message) {
                String method = message.getMethod();
                //LogUtils.d(method);
                if ("0".equals(method) || "1".equals(method)) {
                   if (message.getControValue() != 0) {
                       helper.setText(R.id.fg_control_result, mContext.getString(R.string.controlvalue) + message.getControValue());
                   }
                }
            }

        }
        helper.setText(R.id.fg_item_chose_units, item2.getProsecutedunits());
        helper.setText(R.id.fg_item_chose_task, item2.getPlanName());
        helper.setText(R.id.bt_chose_units, item2.getProsecutedunits());

        //这两种方法不需要对照值，所以将对照按钮隐藏
        if (mContext.getString(R.string.mothod3).equals(item2.getTest_method()) || mContext.getString(R.string.mothod4).equals(item2.getTest_method())) {
            helper.setVisible(R.id.fg_item_btn_control, false);
        } else {
            helper.setVisible(R.id.fg_item_btn_control, true);
        }

        helper.addOnClickListener(R.id.bt_chose_units);
        helper.setOnTouchListener(R.id.fg_item_gallerynum, new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event1) {
                if (event1.getAction() == MotionEvent.ACTION_DOWN) {
                    postEvent(5, helper.getAdapterPosition());
                }

                return true;
            }
        }).setOnTouchListener(R.id.parent_layout, new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event1) {
                if (event1.getAction() == MotionEvent.ACTION_DOWN) {
                    postEvent(5, helper.getAdapterPosition());
                }

                return true;
            }
        })
                .setOnTouchListener(R.id.fg_item_btn_choseproject, new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event1) {
                        if (event1.getAction() == MotionEvent.ACTION_UP) {
                            //测试中不能操作
                            if (item.getState() == 1) {
                                ArmsUtils.snackbarText(mContext.getString(R.string.testinng));
                                return false;
                            }
                            postEvent(1, helper.getPosition());
                        }
                        return true;
                    }
                })
                .setOnTouchListener(R.id.fg_item_btn_chosesample, new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event1) {
                        if (event1.getAction() == MotionEvent.ACTION_UP) {
                            //测试中不能操作
                            if (item.getState() == 1) {
                                ArmsUtils.snackbarText(mContext.getString(R.string.testinng));
                                return false;
                            }
                            postEvent(2, helper.getPosition());
                        }
                        return true;
                    }
                })
                .setOnTouchListener(R.id.fg_item_chose_units, new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event1) {
                        if (event1.getAction() == MotionEvent.ACTION_UP) {
                            //测试中不能操作
                            if (item.getState() == 1) {
                                ArmsUtils.snackbarText(mContext.getString(R.string.testinng));
                                return false;
                            }
                            postEvent(3, helper.getPosition());
                        }
                        return true;
                    }
                })
                .setOnTouchListener(R.id.fg_item_chose_task, new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event1) {
                        if (event1.getAction() == MotionEvent.ACTION_UP) {
                            //测试中不能操作
                            if (item.getState() == 1) {
                                ArmsUtils.snackbarText(mContext.getString(R.string.testinng));
                                return false;
                            }
                            postEvent(4, helper.getPosition());
                        }
                        return true;
                    }
                })
                .setOnTouchListener(R.id.fg_item_btn_control, new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event1) {
                        if (event1.getAction() == MotionEvent.ACTION_UP) {
                            BaseProjectMessage message1 = item.getProjectMessage();

                            if (message1 == null) {
                                ArmsUtils.snackbarText(mContext.getString(R.string.place_choseproject));
                                return false;
                            }

                            if (item.getState() == 1) {
                                int dowhat = item.getDowhat();
                                if (dowhat == 1) {
                                    ArmsUtils.snackbarText(mContext.getString(R.string.sample_testing));
                                    return false;
                                }

                                item.stopFGGDTest();
                                //因为中途手动停止不会达到检查状态的条件，需要手动调用检查是否有在检测的通道
                                MyAppLocation.myAppLocation.mSerialDataService.checkFGState();
                                return false;
                            }


                            int wavelength = message1.getWavelength();
                            float luminousness = 0;
                            switch (wavelength) {
                                case 410:
                                    luminousness = item.getLuminousness1();
                                    break;
                                case 536:
                                    luminousness = item.getLuminousness2();
                                    break;
                                case 595:
                                    luminousness = item.getLuminousness3();
                                    break;
                                case 620:
                                    luminousness = item.getLuminousness4();
                                    break;
                            }
                            if (luminousness >= Constants.FGLIMITVALUE_LOW) {
                                makeDialog(item, 2);
                                return false;
                            }

                            item.startFGGDTest(2);


                        }
                        return true;
                    }
                })
                .setOnTouchListener(R.id.fg_item_btn_sample, new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event1) {
                        // TODO: 2020/7/30 判断检测条件是否满足
                        if (event1.getAction() == MotionEvent.ACTION_UP) {
                            if (item.getState() == 1) {
                                int dowhat = item.getDowhat();
                                if (dowhat == 2) {
                                    ArmsUtils.snackbarText(mContext.getString(R.string.doingcontrol));
                                    return false;
                                }
                                item.stopFGGDTest();
                                //因为中途手动停止不会达到检查状态的条件，需要手动调用检查是否有在检测的通道
                                MyAppLocation.myAppLocation.mSerialDataService.checkFGState();
                                return false;
                            }

                            BaseProjectMessage message1 = item.getProjectMessage();


                            //FGGDTestItem message = (FGGDTestItem) message1;
                            if (message1 == null) {
                                ArmsUtils.snackbarText(mContext.getString(R.string.place_choseproject));
                                return false;
                            }
                            String method2 = message1.getMethod();
                            if (method2.equals("0") || method2.equals("1")) {
                                if (message1.getControValue() == 0) {
                                    ArmsUtils.snackbarText(mContext.getString(R.string.controlled_needed_first));
                                    return false;
                                }
                            }
                            int wavelength = message1.getWavelength();
                            float luminousness = 0;
                            switch (wavelength) {
                                case 410:
                                    luminousness = item.getLuminousness1();
                                    break;
                                case 536:
                                    luminousness = item.getLuminousness2();
                                    break;
                                case 595:
                                    luminousness = item.getLuminousness3();
                                    break;
                                case 620:
                                    luminousness = item.getLuminousness4();
                                    break;
                            }
                            if (luminousness >= Constants.FGLIMITVALUE_LOW) {
                                makeDialog(item, 1);
                                return false;
                            }
                            startTest(item, 1);

                            //item.startTest();
                        }
                        return true;
                    }
                });


        String makecontrole = mContext.getString(R.string.makecontrole);
        String makesample = mContext.getString(R.string.makesample);
        if (item.getState() == 0) {  //等待测试 可能是刚进入界面或者清零后的状态
            helper.setBackgroundRes(R.id.parent_layout, R.drawable.background_item_gray);
            helper.setText(R.id.fg_item_btn_control, makecontrole)
                    .setText(R.id.fg_item_btn_sample, makesample);

        } else if (item.getState() == 1) {  //正在测试状态
            helper.setBackgroundRes(R.id.parent_layout, R.drawable.background_item_blue);
            if (item.getDowhat() == 1) {
                helper.setText(R.id.fg_item_btn_sample, "" + item.getRemainingtime())
                        .setText(R.id.fg_item_btn_control, makecontrole);
            } else {
                helper.setText(R.id.fg_item_btn_control, "" + item.getRemainingtime())
                        .setText(R.id.fg_item_btn_sample, makesample);
            }

        } else if (item.getState() == 2) {  //测试完成状态
            if (item.getDowhat() == 1) {
                String decisionoutcome = item2.getDecisionoutcome();
                if (mContext.getString(R.string.ok).equals(decisionoutcome)) {
                    helper.setBackgroundRes(R.id.parent_layout, R.drawable.background_item_green);
                    helper.setTextColor(R.id.fg_testresult, Color.parseColor("#17460C"));
                } else if (mContext.getString(R.string.ng).equals(decisionoutcome)) {
                    helper.setBackgroundRes(R.id.parent_layout, R.drawable.background_item_red);
                    helper.setTextColor(R.id.fg_testresult, Color.RED);
                } else {
                    helper.setBackgroundRes(R.id.parent_layout, R.drawable.background_item_yellow);
                    helper.setTextColor(R.id.fg_testresult, Color.YELLOW);
                }
            } else if (item.getDowhat() == 2) {
                helper.setBackgroundRes(R.id.parent_layout, R.drawable.background_item_green);
                helper.setTextColor(R.id.fg_testresult, Color.parseColor("#17460C"));

            }
            helper.setText(R.id.fg_item_btn_control, makecontrole)
                    .setText(R.id.fg_item_btn_sample, makesample);

        } else if (item.getState() == 3) { //测试失败（可能对照值不存在 测试参数错误）
            if (item.getDowhat() == 1) {
                helper.setBackgroundRes(R.id.parent_layout, R.drawable.background_item_red);
                helper.setText(R.id.fg_item_btn_control, makecontrole)
                        .setText(R.id.fg_item_btn_sample, makesample);
                helper.setTextColor(R.id.fg_testresult, Color.RED);
            } else if (item.getDowhat() == 2) {
                helper.setBackgroundRes(R.id.parent_layout, R.drawable.background_item_red);
                helper.setText(R.id.fg_item_btn_control, makecontrole)
                        .setText(R.id.fg_item_btn_sample, makesample);
                helper.setTextColor(R.id.fg_testresult, Color.RED);
            } else {

            }

        }

        BaseProjectMessage message = item.getProjectMessage();


        String value = String.format(mContext.getString(R.string.errormessage2), num + "");
        String value1 = String.format(mContext.getString(R.string.errormessage3), num + "");
        if (null != message) {
            //LogUtils.d(message);
            //LogUtils.d(((Detection_Record_FGGD_NC) item));
            int wavelength = message.getWavelength();

            double luminousness = 0;
            switch (wavelength) {
                case 410:
                    luminousness = item.getLuminousness1();
                    break;
                case 536:
                    luminousness = item.getLuminousness2();
                    break;
                case 595:
                    luminousness = item.getLuminousness3();
                    break;
                case 620:
                    luminousness = item.getLuminousness4();
                    break;
            }
            if (luminousness != 0) {
                if (luminousness < Constants.FGLIMITVALUE_LOW) {
                    //LogUtils.d("FGGD 1");
                    helper.setVisible(R.id.error_message, false);
                    helper.setBackgroundColor(R.id.parent_layoutforcolor, Color.argb(86, 2, 202, 250));
                } else if (luminousness > Constants.FGLIMITVALUE_HEIGHT()) {
                    //LogUtils.d("FGGD 2");
                    helper.setVisible(R.id.error_message, true).setText(R.id.error_message, value);
                    helper.setBackgroundColor(R.id.parent_layoutforcolor, Color.argb(50, 255, 0, 0));
                } else {
                    //LogUtils.d("FGGD 3");
                    helper.setVisible(R.id.error_message, false);
                    helper.setBackgroundColor(R.id.parent_layoutforcolor, Color.argb(0, 0, 0, 0));
                }
            } else {
                //LogUtils.d("FGGD 4");
                helper.setVisible(R.id.error_message, true)
                        .setText(R.id.error_message, value1);
            }


        } else {
            double luminousness1 = item.getLuminousness1();
            double luminousness2 = item.getLuminousness2();
            double luminousness3 = item.getLuminousness3();
            double luminousness4 = item.getLuminousness4();
            if ((luminousness1 != 0 && luminousness1 < Constants.FGLIMITVALUE_LOW) ||
                    (luminousness2 != 0 && luminousness2 < Constants.FGLIMITVALUE_LOW) ||
                    (luminousness3 != 0 && luminousness3 < Constants.FGLIMITVALUE_LOW) ||
                    (luminousness4 != 0 && luminousness4 < Constants.FGLIMITVALUE_LOW)) {
                LogUtils.d("FGGD 5");
                helper.setVisible(R.id.error_message, false);
                helper.setBackgroundColor(R.id.parent_layoutforcolor, Color.argb(86, 2, 202, 250));
            } else if (luminousness1 > Constants.FGLIMITVALUE_HEIGHT() ||
                    luminousness2 > Constants.FGLIMITVALUE_HEIGHT() ||
                    luminousness3 > Constants.FGLIMITVALUE_HEIGHT() ||
                    luminousness4 > Constants.FGLIMITVALUE_HEIGHT()) {
                LogUtils.d("FGGD 6");
                helper.setVisible(R.id.error_message, true).setText(R.id.error_message, value);
                helper.setBackgroundColor(R.id.parent_layoutforcolor, Color.argb(50, 255, 0, 0));
            } else {
                LogUtils.d("FGGD 7");
                helper.setVisible(R.id.error_message, false);
                helper.setBackgroundColor(R.id.parent_layoutforcolor, Color.argb(0, 0, 0, 0));
            }

        }


    }


    private void postEvent(int i, int position) {
        FGTestMessageBean event = new FGTestMessageBean(i);
        event.index = position;
        EventBus.getDefault().post(event);
    }

    private void makeDialog(GalleryBean item, int i) {
        DiaLogUtils.showAlert(mContext, mContext.getString(R.string.hint), mContext.getString(R.string.channel_undetected_hint), mContext.getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }, mContext.getString(R.string.continue_testing), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startTest(item, i);
            }
        });
    }

    private void startTest(GalleryBean item, int i) {
        if (i == 2) {
            item.startFGGDTest(2);
            return;
        }

        item.startFGGDTest(1);
    }


}
