package com.dy.colony.mvp.ui.adapter;

import android.content.Context;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dy.colony.R;
import com.dy.colony.greendao.beans.FGGDTestItem;
import com.dy.colony.greendao.beans.JTJTestItem;
import com.dy.colony.mvp.model.entity.base.BaseProjectMessage;

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
public class ChoseProjectMessageAdapter extends BaseQuickAdapter<BaseProjectMessage, BaseViewHolder> {
    private Context mContext;
    private String mVersion = "";


    public ChoseProjectMessageAdapter(int layoutResId, @Nullable List<? extends BaseProjectMessage> data, Context context) {
        super(layoutResId, (List<BaseProjectMessage>) data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, BaseProjectMessage item1) {
        String mMethod = "";
        String mName = "";

        if (item1 instanceof FGGDTestItem) {
            FGGDTestItem item = (FGGDTestItem) item1;
            String method = item.getMethod();
            if ("0".equals(method)) {
                mMethod = mContext.getString(R.string.mothod1);
            } else if ("1".equals(method)) {
                mMethod = mContext.getString(R.string.mothod2);
            } else if ("2".equals(method)) {
                mMethod = mContext.getString(R.string.mothod3);
            } else if ("3".equals(method)) {
                mMethod = mContext.getString(R.string.mothod4);
            }
            mVersion = item.getVersion();
            mName = item.getProject_name();


        } else if (item1 instanceof JTJTestItem) {
            JTJTestItem item = (JTJTestItem) item1;
            String method = item.getTestMethod() + "";
            if ("1".equals(method)) {
                mMethod = mContext.getString(R.string.method_xiaoxian);
            } else if ("2".equals(method)) {
                mMethod = mContext.getString(R.string.method_bise);
            }

            mName = item.getProjectName();
            mVersion = item.getVersion();


        }
        helper.setText(R.id.local_project, mName)
                .setText(R.id.testmethod, mContext.getString(R.string.project_method) + mMethod)
                .setText(R.id.remarks, mContext.getString(R.string.project_remark) + (mVersion.equals("null") ? "" : mVersion));

    }

}
