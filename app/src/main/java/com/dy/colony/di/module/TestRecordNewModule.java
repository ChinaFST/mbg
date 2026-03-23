package com.dy.colony.di.module;

import android.app.AlertDialog;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dmax.dialog.SpotsDialog;

import com.dy.colony.R;
import com.dy.colony.greendao.beans.Detection_Record_FGGD_NC;
import com.dy.colony.mvp.contract.TestRecordContract;
import com.dy.colony.mvp.contract.TestRecordNewContract;
import com.dy.colony.mvp.model.TestRecordNewModel;
import com.dy.colony.mvp.ui.adapter.TestRecordAdapter;
import com.jess.arms.di.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;

@Module
public abstract class TestRecordNewModule {

    @Binds
    abstract TestRecordNewContract.Model bindTestRecordNewModel(TestRecordNewModel model);

    @ActivityScope
    @Provides
    static RecyclerView.LayoutManager provideLayoutManager(TestRecordNewContract.View view) {
        return new GridLayoutManager(view.getActivity(), 1);
    }


    @ActivityScope
    @Provides
    static AlertDialog getSportDialog(TestRecordNewContract.View view){
        return new SpotsDialog.Builder().setContext(view.getActivity()).setCancelable(false).build();
    }




    @ActivityScope
    @Provides
    static List<Detection_Record_FGGD_NC> provideUserList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    static TestRecordAdapter provideUserAdapter(List<Detection_Record_FGGD_NC> list, TestRecordNewContract.View view){
        return new TestRecordAdapter(R.layout.testrecord_item_layout ,list);
    }
}