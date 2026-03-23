package com.dy.colony.di.module;

import android.app.AlertDialog;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dmax.dialog.SpotsDialog;

import com.dy.colony.R;
import com.dy.colony.greendao.DBHelper;
import com.dy.colony.greendao.daos.FGGDTestItemDao;
import com.dy.colony.greendao.daos.JTJTestItemDao;
import com.dy.colony.mvp.contract.ChoseProjectContract;
import com.dy.colony.mvp.model.ChoseProjectModel;
import com.dy.colony.mvp.model.entity.base.BaseProjectMessage;
import com.dy.colony.mvp.ui.adapter.ChoseProjectMessageAdapter;
import com.jess.arms.di.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;

@Module
public abstract class ChoseProjectModule {

    @Binds
    abstract ChoseProjectContract.Model bindChoseProjectModel(ChoseProjectModel model);

    @ActivityScope
    @Provides
    static RecyclerView.LayoutManager provideLayoutManager(ChoseProjectContract.View view) {

            return new GridLayoutManager(view.getActivity(), 2);

    }

    @ActivityScope
    @Provides
    static List<BaseProjectMessage> provideGetFGGDTestItemList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    static ChoseProjectMessageAdapter provideGetChoseProjectMessageAdapter(List<BaseProjectMessage> list, ChoseProjectContract.View view) {

            return new ChoseProjectMessageAdapter(R.layout.fggd_chose_testitem_item_layout, list, view.getActivity());

    }


    @ActivityScope
    @Provides
    static JTJTestItemDao provideGetJTJestItemDao(ChoseProjectContract.View view) {
        return DBHelper.getJTJTestItemDao(view.getActivity());
    }

    @ActivityScope
    @Provides
    static FGGDTestItemDao provideGetFGGDTestItemDao(ChoseProjectContract.View view) {
        return DBHelper.getFGGDTestItemDao(view.getActivity());
    }

    @ActivityScope
    @Provides
    static AlertDialog getSportDialog(ChoseProjectContract.View view) {
        return new SpotsDialog.Builder().setContext(view.getActivity()).setCancelable(true).build();
    }
}