package com.dy.colony.di.module;

import android.app.AlertDialog;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dy.colony.MyAppLocation;
import com.dy.colony.R;
import com.dy.colony.mvp.contract.FGGD_TestContract;
import com.dy.colony.mvp.model.FGGD_TestModel;
import com.dy.colony.mvp.model.entity.base.GalleryBean;
import com.dy.colony.mvp.ui.adapter.FGGDAdapter;
import com.jess.arms.di.scope.ActivityScope;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.List;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dmax.dialog.SpotsDialog;

@Module
public abstract class FGGD_TestModule {

    @Binds
    abstract FGGD_TestContract.Model bindFGGD_TestModel(FGGD_TestModel model);

    @ActivityScope
    @Provides
    static RecyclerView.LayoutManager provideLayoutManager(FGGD_TestContract.View view) {
        return new GridLayoutManager(view.getActivity(), 1);
    }

    @ActivityScope
    @Provides
    static RxPermissions providesrxpermissions(FGGD_TestContract.View view) {
        return new RxPermissions((FragmentActivity) view.getActivity());
    }

    @ActivityScope
    @Provides
    static List<GalleryBean> provideUserList() {
        return MyAppLocation.myAppLocation.mSerialDataService.mFGGDGalleryBeanList;
    }

    @ActivityScope
    @Provides
    static FGGDAdapter provideUserAdapter(List<GalleryBean> list) {
        return new FGGDAdapter(R.layout.fggd_recycle_item_hidetaskmessage, list);
    }

    @ActivityScope
    @Provides
    static AlertDialog.Builder getAlertDialog(FGGD_TestContract.View view) {
        return new AlertDialog.Builder(view.getActivity());
    }

    @ActivityScope
    @Provides
    static AlertDialog getSportDialog(FGGD_TestContract.View view) {
        return new SpotsDialog.Builder().setContext(view.getActivity()).setCancelable(false).build();
    }
}