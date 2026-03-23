package com.dy.colony.mvp.ui.adapter;

import android.content.Context;
import android.view.View;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dy.colony.R;
import com.dy.colony.greendao.beans.Simple33;
import com.dy.colony.mvp.model.entity.base.BaseSimple33Message;


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
 * Created by wangzhenxiong on 2019/3/14.
 */
public class ChoseSimple33Adapter extends BaseQuickAdapter<BaseSimple33Message, BaseViewHolder> {

    private Context mContext;

    public ChoseSimple33Adapter(int layoutResId, List<? extends BaseSimple33Message> data, Context context) {
        super(layoutResId, (List<BaseSimple33Message>) data);
        mContext = context;

    }

    @Override
    protected void convert(BaseViewHolder helper, BaseSimple33Message item) {
        if (item instanceof Simple33) {
            Simple33 item1 = (Simple33) item;
            helper.setText(R.id.num, helper.getPosition() + 1 + "")
                    .setText(R.id.type_name, item1.getFoodName())
                    //.setText(R.id.other_name,item1.getFoodPCode())
                    .setOnClickListener(R.id.check, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EventBus.getDefault().post(item);
                        }
                    });
        }

    }

    @Override
    public void setOnItemClickListener(@Nullable OnItemClickListener listener) {
        super.setOnItemClickListener(listener);
    }
}
