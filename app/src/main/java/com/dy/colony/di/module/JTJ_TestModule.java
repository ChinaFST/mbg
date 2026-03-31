package com.dy.colony.di.module;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.hardware.usb.UsbManager;
import android.view.LayoutInflater;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dmax.dialog.SpotsDialog;

import com.dy.colony.R;
import com.dy.colony.mvp.contract.JTJ_TestContract;
import com.dy.colony.mvp.model.JTJ_TestModel;
import com.dy.colony.mvp.model.entity.base.GalleryBean;
import com.jess.arms.di.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;

@Module
public abstract class JTJ_TestModule {

    @Binds
    abstract JTJ_TestContract.Model bindJTJ_TestModel(JTJ_TestModel model);

    @ActivityScope
    @Provides
    static List<GalleryBean> getGalleryBeano(){
        return  new ArrayList<>();
    }

    @ActivityScope
    @Provides
    static AlertDialog getSportDialog(JTJ_TestContract.View view) {
        return new SpotsDialog.Builder().setContext(view.getActivity()).setCancelable(true).build();
    }
    @ActivityScope
    @Provides
    static Dialog getDialog(JTJ_TestContract.View view) {
        return new Dialog(view.getActivity());
    }

    @ActivityScope
    @Provides
    static UsbManager getUsbManager (JTJ_TestContract.View view) {
        return (UsbManager) view.getActivity().getSystemService(Context.USB_SERVICE);
    }

    @ActivityScope
    @Provides
    static  AlertDialog.Builder getAlertDialog (JTJ_TestContract.View view) {
        return  new AlertDialog.Builder(view.getActivity());
    }

    @ActivityScope
    @Provides
    static RecyclerView.LayoutManager provideLayoutManager(JTJ_TestContract.View view) {
        return new GridLayoutManager(view.getActivity(), 1);
    }
}