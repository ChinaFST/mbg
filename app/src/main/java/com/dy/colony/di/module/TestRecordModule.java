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
import com.dy.colony.mvp.model.TestRecordModel;
import com.dy.colony.mvp.ui.adapter.TestRecordAdapter;
import com.jess.arms.di.scope.FragmentScope;

import java.util.ArrayList;
import java.util.List;

@Module
public abstract class TestRecordModule {

    @Binds
    abstract TestRecordContract.Model bindTestRecordModel(TestRecordModel model);

    @FragmentScope
    @Provides
    static RecyclerView.LayoutManager provideLayoutManager(TestRecordContract.View view) {
        return new GridLayoutManager(view.getActivity(), 1);
    }


    @FragmentScope
    @Provides
    static AlertDialog getSportDialog(TestRecordContract.View view){
        return new SpotsDialog.Builder().setContext(view.getActivity()).setCancelable(false).build();
    }




    @FragmentScope
    @Provides
    static List<Detection_Record_FGGD_NC> provideUserList() {
        return new ArrayList<>();
    }

    @FragmentScope
    @Provides
    static TestRecordAdapter provideUserAdapter(List<Detection_Record_FGGD_NC> list, TestRecordContract.View view){
        return new TestRecordAdapter(R.layout.testrecord_item_layout ,list);
    }
}