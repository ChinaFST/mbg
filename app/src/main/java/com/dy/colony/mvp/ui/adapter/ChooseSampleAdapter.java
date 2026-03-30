package com.dy.colony.mvp.ui.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dy.colony.R;
import com.dy.colony.greendao.DBHelper;
import com.dy.colony.greendao.beans.FoodItemAndStandard;
import com.dy.colony.mvp.model.entity.base.BaseSampleMessage;
import com.dy.colony.mvp.ui.activity.EdtorSampleActivity;
import com.jess.arms.integration.AppManager;


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
 * Created by wangzhenxiong on 2019/3/14.
 */
public class ChooseSampleAdapter extends BaseQuickAdapter<BaseSampleMessage, BaseViewHolder> {
    private Context mContext;

    public ChooseSampleAdapter(int layoutResId, @Nullable List<? extends BaseSampleMessage> data, Context context) {
        super(layoutResId, (List<BaseSampleMessage>) data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, BaseSampleMessage item) {

        int position = helper.getAdapterPosition() + 1;
        // LogUtils.d(position);
        //LogUtils.d(item);
        if (item instanceof FoodItemAndStandard) {
            FoodItemAndStandard item1 = (FoodItemAndStandard) item;
            helper.setText(R.id.xuhao, position + "")
                    .setText(R.id.samplename, item1.getSampleName())
                    .setText(R.id.testprojectname, item1.getItemName())
                    .setText(R.id.standnum, item1.getStandardName())
                    .setText(R.id.standvalue, item1.getCheckSign() + item1.getStandardValue() + item1.getCheckValueUnit());
            helper.setGone(R.id.sample_delete, false)
                    .setVisible(R.id.sample_edtor, true);

        }

        helper.addOnClickListener(R.id.sample_edtor,R.id.sample_delete);

        helper.setOnClickListener(R.id.sample_edtor, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item instanceof FoodItemAndStandard) {
                    Intent intent = new Intent(mContext, EdtorSampleActivity.class).putExtra("data", 2);
                    Bundle extras = new Bundle();
                    extras.putParcelable("databean", (FoodItemAndStandard) item);
                    intent.putExtras(extras);
                    mContext.startActivity(intent);
                }

            }
        }).setOnClickListener(R.id.sample_delete, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Activity activity = AppManager.getAppManager().getCurrentActivity();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new AlertDialog.Builder(activity)
                                .setTitle(R.string.hint)
                                .setMessage(R.string.areyousuretodeletesample)
                                .setNegativeButton(mContext.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).setPositiveButton(mContext.getString(R.string.delete), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                BaseSampleMessage message = mData.get(helper.getAdapterPosition());
                                if (message instanceof FoodItemAndStandard) {
                                    DBHelper.getFoodItemAndStandardDao(mContext).delete(((FoodItemAndStandard) message));
                                    mData.remove(message);
                                    notifyDataSetChanged();
                                }
                            }
                        }).show();
                    }
                });

            }
        });

    }


}
