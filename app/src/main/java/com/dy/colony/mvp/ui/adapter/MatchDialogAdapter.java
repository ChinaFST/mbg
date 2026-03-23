package com.dy.colony.mvp.ui.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dy.colony.R;
import com.dy.colony.greendao.beans.FGGDTestItem;
import com.dy.colony.greendao.beans.FoodItemAndStandard;
import com.dy.colony.greendao.beans.JTJTestItem;


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
public class MatchDialogAdapter<T> extends BaseQuickAdapter<T, BaseViewHolder> {

    public MatchDialogAdapter(int layoutResId, @Nullable List<T> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, T t) {
        helper.setText(R.id.value, (helper.getPosition() + 1) + "")
                .setGone(R.id.sampleseg, false);

        if (t instanceof JTJTestItem) {
            JTJTestItem t1 = (JTJTestItem) t;
            helper.setText(R.id.value1, t1.getProjectName())
                    .setText(R.id.value2, t1.getTestMethod() == 1 ? mContext.getString(R.string.method_xiaoxian)  :mContext.getString(R.string.method_bise) )
                    .setText(R.id.value3, R.string.camera_module)
                    .setGone(R.id.value3, false)
                    .setGone(R.id.value4, false);

        }
        if (t instanceof FGGDTestItem) {
            FGGDTestItem t1 = (FGGDTestItem) t;
            int method = Integer.parseInt(t1.getMethod());
            switch (method) {
                case 0:
                    helper.setText(R.id.value2, mContext.getString(R.string.mothod1));
                    break;
                case 1:
                    helper.setText(R.id.value2, mContext.getString(R.string.mothod2));
                    break;
                case 2:
                    helper.setText(R.id.value2, mContext.getString(R.string.mothod3));
                    break;
                case 3:
                    helper.setText(R.id.value2, mContext.getString(R.string.mothod4));
                    break;
            }
            helper.setText(R.id.value1, t1.getProject_name())
                    .setGone(R.id.value3, false)
                    .setGone(R.id.value4, false);
        } else if (t instanceof FoodItemAndStandard) {
            FoodItemAndStandard t1 = (FoodItemAndStandard) t;
            helper.setText(R.id.value1, t1.getStandardName())
                    .setText(R.id.value2, t1.getCheckSign())
                    .setText(R.id.value3, t1.getStandardValue())
                    .setText(R.id.value4, t1.getCheckValueUnit())
                    .setGone(R.id.sampleseg, true)
                    .setText(R.id.sampleseg, t1.getSampleName());
        }
    }
}
