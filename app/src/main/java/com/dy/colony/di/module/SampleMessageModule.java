package com.dy.colony.di.module;

import android.app.AlertDialog;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dmax.dialog.SpotsDialog;

import com.dy.colony.R;
import com.dy.colony.mvp.contract.SampleMessageContract;
import com.dy.colony.mvp.model.SampleMessageModel;
import com.dy.colony.mvp.model.entity.base.BaseSampleMessage;
import com.dy.colony.mvp.ui.adapter.SampleMessageAdapter;
import com.jess.arms.di.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;

@Module
public abstract class SampleMessageModule {

    @Binds
    abstract SampleMessageContract.Model bindSampleMessageModel(SampleMessageModel model);

    @ActivityScope
    @Provides
    static RecyclerView.LayoutManager provideLayoutManager(SampleMessageContract.View view) {
        return new GridLayoutManager(view.getActivity(), 1);
    }


    @ActivityScope
    @Provides
    static AlertDialog getSportDialog(SampleMessageContract.View view) {
        return new SpotsDialog.Builder().setContext(view.getActivity()).setCancelable(false).build();
    }

    @ActivityScope
    @Provides
    static  AlertDialog.Builder getAlertDialog (SampleMessageContract.View view) {
        return  new AlertDialog.Builder(view.getActivity());
    }
    @ActivityScope
    @Provides
    static List<BaseSampleMessage> provideUserList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    static SampleMessageAdapter provideUserAdapter(List<BaseSampleMessage> list, SampleMessageContract.View view){
        return new SampleMessageAdapter(R.layout.samplemessage_item,list,view.getActivity());
    }
}