package com.dy.colony.di.module;

import android.app.AlertDialog;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dmax.dialog.SpotsDialog;

import com.dy.colony.R;
import com.dy.colony.mvp.contract.ChoseSampleContract;
import com.dy.colony.mvp.model.ChoseSampleModel;
import com.dy.colony.mvp.model.entity.base.BaseSampleMessage;
import com.dy.colony.mvp.model.entity.base.BaseUntilMessage;
import com.dy.colony.mvp.ui.adapter.ChooseSampleAdapter;
import com.jess.arms.di.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;

@Module
public abstract class ChoseSampleModule {

    @Binds
    abstract ChoseSampleContract.Model bindChoseSampleModel(ChoseSampleModel model);

    @ActivityScope
    @Provides
    static RecyclerView.LayoutManager provideLayoutManager(ChoseSampleContract.View view) {
        return new GridLayoutManager(view.getActivity(), 1);
    }

    @ActivityScope
    @Provides
    static List<BaseSampleMessage> provideUserList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    static ChooseSampleAdapter provideUserAdapter(List<BaseSampleMessage> list, ChoseSampleContract.View view) {
        return new ChooseSampleAdapter(R.layout.chose_samplemessage_item, list, view.getActivity());
    }
    @ActivityScope
    @Provides
    static List<BaseUntilMessage> getCompany_Point() {
        return new ArrayList<>();
    }
    @ActivityScope
    @Provides
    static AlertDialog getSportDialog(ChoseSampleContract.View view) {
        return new SpotsDialog.Builder().setContext(view.getActivity()).setCancelable(false).build();
    }

    @ActivityScope
    @Provides
    static AlertDialog.Builder getAlertDialog(ChoseSampleContract.View view) {
        return new AlertDialog.Builder(view.getActivity());
    }
}