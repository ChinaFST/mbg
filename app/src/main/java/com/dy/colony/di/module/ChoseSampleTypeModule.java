package com.dy.colony.di.module;

import android.app.AlertDialog;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dmax.dialog.SpotsDialog;

import com.dy.colony.R;
import com.dy.colony.mvp.contract.ChoseSampleTypeContract;
import com.dy.colony.mvp.model.ChoseSampleTypeModel;
import com.dy.colony.mvp.model.entity.base.BaseSimple33Message;
import com.dy.colony.mvp.ui.adapter.ChoseSimple33Adapter;
import com.jess.arms.di.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;

@Module
public abstract class ChoseSampleTypeModule {

    @Binds
    abstract ChoseSampleTypeContract.Model bindChoseSampleTypeModel(ChoseSampleTypeModel model);


    @ActivityScope
    @Provides
    static RecyclerView.LayoutManager provideLayoutManager(ChoseSampleTypeContract.View view) {
        return new GridLayoutManager(view.getActivity(), 1);
    }


    @ActivityScope
    @Provides
    static List<BaseSimple33Message> provideSimple33_KJFW() {
        return new ArrayList<>();
    }


    @ActivityScope
    @Provides
    static ChoseSimple33Adapter provideChoseSimple33AdapterW(ChoseSampleTypeContract.View view, List<BaseSimple33Message> list) {
        return new ChoseSimple33Adapter(R.layout.chose_simple33_item, list, view.getActivity());
    }

    @ActivityScope
    @Provides
    static AlertDialog getSportDialog(ChoseSampleTypeContract.View view) {
        return new SpotsDialog.Builder().setContext(view.getActivity()).setCancelable(false).build();
    }

    @ActivityScope
    @Provides
    static AlertDialog.Builder getAlertDialog(ChoseSampleTypeContract.View view) {
        return new AlertDialog.Builder(view.getActivity());
    }
}