package com.dy.colony.di.module;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

import com.dy.colony.MyAppLocation;
import com.dy.colony.R;
import com.dy.colony.mvp.contract.FGGDAdjustingContract;
import com.dy.colony.mvp.model.FGGDAdjustingModel;
import com.dy.colony.mvp.model.entity.base.GalleryBean;
import com.dy.colony.mvp.ui.adapter.FGGDAdjustAdapter;
import com.jess.arms.di.scope.ActivityScope;

import java.util.List;

@Module
public abstract class FGGDAdjustingModule {

    @Binds
    abstract FGGDAdjustingContract.Model bindFGGDAdjustingModel(FGGDAdjustingModel model);

    @ActivityScope
    @Provides
    static RecyclerView.LayoutManager provideLayoutManager(FGGDAdjustingContract.View view) {
        return new GridLayoutManager(view.getActivity(), 1);
    }

    @ActivityScope
    @Provides
    static List<GalleryBean> providesList(){
        return MyAppLocation.myAppLocation.mSerialDataService.mFGGDGalleryBeanList;
    }

    @ActivityScope
    @Provides
    static FGGDAdjustAdapter providesGSNCAdapter(List<GalleryBean> list){
        return new FGGDAdjustAdapter(R.layout.fggd_adjusting_item_layout, list);
    }
}