package com.dy.colony.mvp.contract;

import android.app.Activity;

import com.dy.colony.mvp.model.entity.base.BaseSampleMessage;
import com.jess.arms.mvp.IView;
import com.jess.arms.mvp.IModel;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import io.reactivex.Observable;

public interface ChoseSampleContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        Activity getActivity();

        void startLoadMore();

        void endLoadMore();

        void showAlertDialog();

        void hindAlertDialog();

        void setShowPgeText(String s);

        void sethasLoadedAllItemsfase();

        void sethasLoadedAllItemstrue();
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {

        Observable<List<?extends BaseSampleMessage>> getSample(int lastpage, int pagenum, QueryBuilder<?extends BaseSampleMessage> builder, String from);

    }
}