package com.dy.colony.mvp.ui.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dy.colony.R;
import com.dy.colony.greendao.beans.Detection_Record_FGGD_NC;
import com.dy.colony.mvp.model.entity.base.GalleryBean;

import java.text.DecimalFormat;
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
public class FGGDAdjustAdapter extends BaseQuickAdapter<GalleryBean, BaseViewHolder> {

    List<GalleryBean> mData;

    public FGGDAdjustAdapter(int layoutResId, @Nullable List<GalleryBean> data) {
        super(layoutResId, data);
        mData = data;
    }

    @Override
    protected void convert(BaseViewHolder helper, GalleryBean item) {
        if (item instanceof Detection_Record_FGGD_NC) {
            int wave1_start = item.getDzh_wave1_start();
            int wave2_start = item.getDzh_wave2_start();
            int wave3_start = item.getDzh_wave3_start();
            int wave4_start = item.getDzh_wave4_start();
            int wave1 = item.getWave1();
            int wave2 = item.getWave2();
            int wave3 = item.getWave3();
            int wave4 = item.getWave4();
            String lu1 = new DecimalFormat("0.0000").format(item.getLuminousness1());
            String lu2 = new DecimalFormat("0.0000").format(item.getLuminousness2());
            String lu3 = new DecimalFormat("0.0000").format(item.getLuminousness3());
            String lu4 = new DecimalFormat("0.0000").format(item.getLuminousness4());
            String abs1 = new DecimalFormat("0.0000").format(item.getAbsorbance1());
            String abs2 = new DecimalFormat("0.0000").format(item.getAbsorbance2());
            String abs3 = new DecimalFormat("0.0000").format(item.getAbsorbance3());
            String abs4 = new DecimalFormat("0.0000").format(item.getAbsorbance4());
            helper.setText(R.id.tv_gallerynum, item.getGalleryNum() + "")
                    .setText(R.id.contro_ad1, wave1_start == 0 ? "" : wave1_start + "")
                    .setText(R.id.contro_ad2, wave2_start == 0 ? "" : wave2_start + "")
                    .setText(R.id.contro_ad3, wave3_start == 0 ? "" : wave3_start + "")
                    .setText(R.id.contro_ad4, wave4_start == 0 ? "" : wave4_start + "")
                    .setText(R.id.now_ad1, wave1 == 0 ? "" : wave1 + "")
                    .setText(R.id.now_ad2, wave2 == 0 ? "" : wave2 + "")
                    .setText(R.id.now_ad3, wave3 == 0 ? "" : wave3 + "")
                    .setText(R.id.now_ad4, wave4 == 0 ? "" : wave4 + "")
                    .setText(R.id.countdown_time, item.getRemainingtime() + "")
                    .setText(R.id.transmittance1, lu1.equals("0.0000") ? "" : lu1)
                    .setText(R.id.transmittance2, lu2.equals("0.0000") ? "" : lu2)
                    .setText(R.id.transmittance3, lu3.equals("0.0000") ? "" : lu3)
                    .setText(R.id.transmittance4, lu4.equals("0.0000") ? "" : lu4)
                    .setText(R.id.absorbance1, abs1.equals("0.0000") && wave1 == 0 ? "" : abs1)
                    .setText(R.id.absorbance2, abs2.equals("0.0000") && wave2 == 0 ? "" : abs2)
                    .setText(R.id.absorbance3, abs3.equals("0.0000") && wave3 == 0 ? "" : abs3)
                    .setText(R.id.absorbance4, abs4.equals("0.0000") && wave4 == 0 ? "" : abs4);


        }
    }


}
