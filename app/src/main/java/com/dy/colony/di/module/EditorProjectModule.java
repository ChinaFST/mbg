package com.dy.colony.di.module;

import android.app.AlertDialog;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dmax.dialog.SpotsDialog;

import com.dy.colony.mvp.contract.EditorProjectContract;
import com.dy.colony.mvp.model.EditorProjectModel;
import com.jess.arms.di.scope.ActivityScope;

@Module
public abstract class EditorProjectModule {

    @Binds
    abstract EditorProjectContract.Model bindEditorProjectModel(EditorProjectModel model);

    @ActivityScope
    @Provides
    static RecyclerView.LayoutManager provideLayoutManager(EditorProjectContract.View view) {
        return new GridLayoutManager(view.getActivity(), 2);
    }

    @ActivityScope
    @Provides
    static AlertDialog.Builder getAlertDialog(EditorProjectContract.View view) {
        return new AlertDialog.Builder(view.getActivity());
    }

    @ActivityScope
    @Provides
    static AlertDialog getSportDialog(EditorProjectContract.View view) {
        return new SpotsDialog.Builder().setContext(view.getActivity()).setCancelable(false).build();
    }
}