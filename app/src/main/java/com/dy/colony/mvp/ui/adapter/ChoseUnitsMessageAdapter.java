package com.dy.colony.mvp.ui.adapter;

import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.IExpandable;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.dy.colony.R;
import com.dy.colony.greendao.beans.Company_Point;
import com.dy.colony.greendao.beans.Company_Point_Unit;
import com.dy.colony.mvp.model.entity.eventbus.UnitsMessageBean;


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
public class ChoseUnitsMessageAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    public static final int TYPE_LEVEL_0 = 0;
    public static final int TYPE_PERSON = 1;
    private boolean isOnlyExpandOne = false;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public ChoseUnitsMessageAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(TYPE_LEVEL_0, R.layout.chose_units_item_lv0_layout);
        addItemType(TYPE_PERSON, R.layout.chose_units_item_lv1_layout);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity item) {
        switch (helper.getItemViewType()) {
            case TYPE_LEVEL_0:
                if (item instanceof Company_Point) {
                    Company_Point item1 = (Company_Point) item;
                    helper.setText(R.id.item0_regname, item1.getRegName())
                            .setText(R.id.number, (helper.getPosition() + 1) + "")
                            .setText(R.id.item0_regpeople, item1.getContactMan())
                            .setText(R.id.item0_adress, item1.getRegAddress());

                    helper.setOnClickListener(R.id.arrow, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int pos = helper.getAdapterPosition();
                            Log.d(TAG, "Level 0 item pos: " + pos);
                            if (item1.isExpanded()) {
                                collapse(pos, false);

                            } else if (isOnlyExpandOne) {
                                IExpandable willExpandItem = (IExpandable) getData().get(pos);
                                for (int i = getHeaderLayoutCount(); i < getData().size(); i++) {
                                    IExpandable expandable = (IExpandable) getData().get(i);
                                    if (expandable.isExpanded()) {
                                        collapse(i);
                                    }
                                }
                                expand(getData().indexOf(willExpandItem) + getHeaderLayoutCount());
                            } else {
                                expand(pos, false);
                            }
                        }
                    }).setOnClickListener(R.id.item0_check, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            UnitsMessageBean event = new UnitsMessageBean(5);
                            event.mCompany_point_down = item1;
                            EventBus.getDefault().post(event);
                        }
                    });
                    helper.setImageResource(R.id.arrow, item1.isExpanded() ? R.drawable.arrow_b : R.drawable.arrow_r);
                }


                break;
            case TYPE_PERSON:
                if (item instanceof Company_Point_Unit) {
                    Company_Point_Unit item2 = (Company_Point_Unit) item;
                    helper.setText(R.id.item1_cdname, item2.getCdName())
                            .setText(R.id.number, (helper.getAdapterPosition() + 1) + "")
                            .setText(R.id.item1_cdnumber, item2.getCdIdNum())
                            .setText(R.id.item1_cdpeople, item2.getCiContMan())
                            .setText(R.id.item1_cdphone, item2.getCiContType())
                            .setOnClickListener(R.id.item1_check, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    UnitsMessageBean event = new UnitsMessageBean(6);
                                    event.mCompany_point_unit_down = item2;
                                    EventBus.getDefault().post(event);
                                }
                            });
                }

                break;
        }
    }
}
