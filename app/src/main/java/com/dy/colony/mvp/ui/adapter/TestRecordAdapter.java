package com.dy.colony.mvp.ui.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.apkfuns.logutils.LogUtils;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.dy.colony.Constants;
import com.dy.colony.MyAppLocation;
import com.dy.colony.R;
import com.dy.colony.greendao.beans.Detection_Record_FGGD_NC;
import com.jess.arms.utils.ArmsUtils;


import java.util.ArrayList;
import java.util.Arrays;
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
 * Created by wangzhenxiong on 2019-05-27.
 */
public class TestRecordAdapter extends BaseQuickAdapter<Detection_Record_FGGD_NC, BaseViewHolder> {
    List<Detection_Record_FGGD_NC> mList;

    public TestRecordAdapter(int layoutResId,   @Nullable List<Detection_Record_FGGD_NC> data) {
        super(layoutResId,data);
        mList = data;

    }

    @Override
    protected void convert(BaseViewHolder helper, Detection_Record_FGGD_NC item) {
        // LogUtils.d(itemparent);

        LogUtils.d(item);
        String test_project = item.getTest_project();

        String moudle = item.getTest_Moudle();
        helper.setText(R.id.testresult, item.getTestresult() + item.getCov_unit());
        if (moudle.contains(mContext.getString(R.string.JTJ_TestMoudle_P))) {
            helper.setText(R.id.testresult, item.getTestresult());
        }
        if (test_project.equals("大米新鲜度")) {
            helper.setText(R.id.testresult, item.getTestresult());
        }

        if (item.getRetest() == 1) {
            helper.setVisible(R.id.retest_icn, true);
        } else {
            helper.setVisible(R.id.retest_icn, false);
        }
        //int i = mList.indexOf(item);
        String sn = item.getSn();
        helper.setText(R.id.num,   sn)
                .setText(R.id.samplename, item.getSamplename())
                .setText(R.id.Inspectedunit, item.getProsecutedunits())
                .setText(R.id.testproject, test_project)
                .setText(R.id.testtime, item.getdfTestingtimeyy_mm_dd_hh_mm_ss())
                .setText(R.id.testpeople, item.getInspector())
                .setChecked(R.id.ckbox, item.checkState)
                .setOnCheckedChangeListener(R.id.ckbox, new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        ((Detection_Record_FGGD_NC) mData.get(helper.getPosition())).checkState = isChecked;
                        //item.checkState = isChecked;
                    }
                })
                .setOnClickListener(R.id.detailmessage, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                                /*Intent intent = new Intent(mContext, TestRecordMessageActivity.class);
                                Bundle extras = new Bundle();
                                extras.putParcelable("data", item);
                                intent.putExtras(extras);
                                mContext.startActivity(intent);*/
                    }
                });
        String decisionoutcome = item.getDecisionoutcome();


        if (mContext.getString(R.string.ok).equals(decisionoutcome) || mContext.getString(R.string.yin).equals(decisionoutcome)) {
            helper.setBackgroundRes(R.id.testresult_icn, R.drawable.ic_ok);
        } else if (mContext.getString(R.string.ng).equals(decisionoutcome) || mContext.getString(R.string.yang).equals(decisionoutcome)) {
            helper.setBackgroundRes(R.id.testresult_icn, R.drawable.ic_not_ok);
        } else if (mContext.getString(R.string.keyi).equals(decisionoutcome)) {
            helper.setBackgroundRes(R.id.testresult_icn, R.drawable.keyi);
        } else {
            //helper.setBackgroundRes(R.id.testresult_icn, R.drawable.keyi);
            helper.getView(R.id.testresult_icn).setBackground(null);
        }


        helper.setVisible(R.id.updatamessage, false);


        int isupload = item.getIsupload();
        if (isupload == 1) {
            helper.setBackgroundRes(R.id.layout_parent, R.color.coloruploadsuccess);
        } else {
            helper.setBackgroundRes(R.id.layout_parent, R.color.tm00);
        }


    }


}
