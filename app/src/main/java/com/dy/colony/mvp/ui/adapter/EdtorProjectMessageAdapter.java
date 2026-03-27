package com.dy.colony.mvp.ui.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dy.colony.R;
import com.dy.colony.greendao.DBHelper;
import com.dy.colony.greendao.beans.FGGDTestItem;
import com.dy.colony.greendao.beans.JTJTestItem;
import com.dy.colony.mvp.model.entity.base.BaseProjectMessage;
import com.dy.colony.mvp.ui.activity.FGGD_NewTestItemActivity;
import com.dy.colony.mvp.ui.activity.JTJ_NewTestItemActivity;
import com.jess.arms.utils.ArmsUtils;


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
public class EdtorProjectMessageAdapter extends BaseQuickAdapter<BaseProjectMessage, BaseViewHolder> {
    private Context mContext;

    public EdtorProjectMessageAdapter(int layoutResId, @Nullable List<?> data, Context context) {
        super(layoutResId, (List<BaseProjectMessage>) data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, BaseProjectMessage item1) {

        helper.setText(R.id.title, item1.getProjectName())
                .setText(R.id.editText20, item1.getMethod())
                .setText(R.id.version, item1.getVersion());


        if (item1 instanceof FGGDTestItem) {
            FGGDTestItem item = (FGGDTestItem) item1;
            String method = item1.getMethod();
            if ("0".equals(method)) {
                method = mContext.getString(R.string.mothod1);
            } else if ("1".equals(method)) {
                method = mContext.getString(R.string.mothod2);
            } else if ("2".equals(method)) {
                method = mContext.getString(R.string.mothod3);
            } else if ("3".equals(method)) {
                method = mContext.getString(R.string.mothod4);
            }
            helper.setText(R.id.editText20, method)
                    .setOnClickListener(R.id.edtor_item, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            makeCheckPasswordDialog(item, 1);


                        }
                    }).setOnClickListener(R.id.delete_item, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    makeCheckPasswordDialog(item, 2);

                }
            });
        } else if (item1 instanceof JTJTestItem) {
            JTJTestItem item = (JTJTestItem) item1;

            helper.setOnClickListener(R.id.edtor_item, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    makeCheckPasswordDialog(item, 3);

                }
            }).setOnClickListener(R.id.delete_item, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    makeCheckPasswordDialog(item, 4);

                }
            });
            String type = item.getItem_type();
            helper.setText(R.id.itemtype, R.string.camera_module);

        }


    }

    /**
     * @param item 需要比对检测项目
     * @param tag  1编辑分光检测项目 2删除分光检测项目 3编辑胶体金检测项目 4删除胶体金检测项目 5编辑重金属检测项目 6删除重金属检测项目
     */
    private void makeCheckPasswordDialog(BaseProjectMessage item, int tag) {
        AlertDialog dialog = new AlertDialog.Builder(mContext).create();
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_passwordcheck_layout, null);
        EditText editText = (EditText) view.findViewById(R.id.dialog_password);
        view.findViewById(R.id.dialog_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = editText.getText().toString();
                switch (tag) {
                    case 1:
                        FGGDTestItem item1 = (FGGDTestItem) item;
                        if (s1.equals(item1.getPassword())) {
                            Intent intent = new Intent(mContext, FGGD_NewTestItemActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("data", item1);
                            intent.putExtras(bundle);
                            dialog.dismiss();
                            ArmsUtils.startActivity(intent);
                        } else {
                            ArmsUtils.snackbarText(mContext.getString(R.string.passworderro));
                        }

                        break;
                    case 2:
                        FGGDTestItem item2 = (FGGDTestItem) item;
                        if (s1.equals(item2.getPassword())) {
                            DBHelper.getFGGDTestItemDao(mContext).delete(item2);
                            mData.remove(item2);
                            notifyDataSetChanged();
                            dialog.dismiss();
                            ArmsUtils.snackbarText(mContext.getString(R.string.deletesuccess));

                        } else {
                            ArmsUtils.snackbarText(mContext.getString(R.string.passworderro));
                        }

                        break;
                    case 3:
                        JTJTestItem item3 = (JTJTestItem) item;
                        if (s1.equals(item3.getPassword())) {
                            Intent intent = new Intent(mContext, JTJ_NewTestItemActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("data", item3);
                            intent.putExtras(bundle);
                            dialog.dismiss();
                            ArmsUtils.startActivity(intent);
                        } else {
                            ArmsUtils.snackbarText(mContext.getString(R.string.passworderro));
                        }

                        break;
                    case 4:
                        JTJTestItem item4 = (JTJTestItem) item;
                        if (s1.equals(item4.getPassword())) {
                            DBHelper.getJTJTestItemDao(mContext).delete(item4);
                            mData.remove(item4);
                            notifyDataSetChanged();
                            dialog.dismiss();
                            ArmsUtils.snackbarText(mContext.getString(R.string.deletesuccess));
                        } else {
                            ArmsUtils.snackbarText(mContext.getString(R.string.passworderro));
                        }
                        break;

                }

            }
        });
        view.findViewById(R.id.dialog_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        //dialog.setIcon(R.mipmap.ic);
        dialog.setTitle(mContext.getString(R.string.login_password_hint));
        dialog.setView(view);
        dialog.setCancelable(true);
        dialog.show();
    }

}
