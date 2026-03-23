package com.dy.colony.di.module;

import android.app.AlertDialog;

import androidx.recyclerview.widget.GridLayoutManager;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dmax.dialog.SpotsDialog;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.dy.colony.mvp.contract.ChoseUnitContract;
import com.dy.colony.mvp.model.ChoseUnitModel;
import com.dy.colony.mvp.ui.adapter.ChoseUnitsMessageAdapter;
import com.jess.arms.di.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;

@Module
public abstract class ChoseUnitModule {

    @Binds
    abstract ChoseUnitContract.Model bindChoseUnitModel(ChoseUnitModel model);

    @ActivityScope
    @Provides
    static AlertDialog getSportDialog(ChoseUnitContract.View view) {
        return new SpotsDialog.Builder().setContext(view.getActivity()).setCancelable(false).build();
    }

    @ActivityScope
    @Provides
    static List<MultiItemEntity> getList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    static ChoseUnitsMessageAdapter getUnitsAdapter(List<MultiItemEntity> res) {
        return new ChoseUnitsMessageAdapter(res);

    }

    @ActivityScope
    @Provides
    static GridLayoutManager getGridLayoutManager(ChoseUnitContract.View view, ChoseUnitsMessageAdapter adapter) {
        GridLayoutManager manager = new GridLayoutManager(view.getActivity(), 1);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return adapter.getItemViewType(position) == ChoseUnitsMessageAdapter.TYPE_PERSON ? 1 : manager.getSpanCount();
            }
        });
        return manager;
    }
}