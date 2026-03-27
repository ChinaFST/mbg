package com.dy.colony.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.dy.colony.R;
import com.dy.colony.di.component.DaggerMainComponent;
import com.dy.colony.mvp.contract.MainContract;
import com.dy.colony.mvp.presenter.MainPresenter;
import com.dy.colony.mvp.ui.activity.EditorProjectActivity;
import com.dy.colony.mvp.ui.activity.FGGD_TestActivity;
import com.dy.colony.mvp.ui.activity.JTJ_TestActivity;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;

import butterknife.BindView;
import butterknife.OnClick;

public class MainFragment extends BaseFragment<MainPresenter> implements MainContract.View {
    @BindView(R.id.card_1)
    CardView card_1;
    @BindView(R.id.card_2)
    CardView card_2;
    @BindView(R.id.editor_project_fggd)
    ImageButton editor_project_fggd;
    @BindView(R.id.editor_project_jtj)
    ImageButton editor_project_jtj;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerMainComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        initClickListener();
    }

    private void initClickListener() {
        card_1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (editor_project_fggd.getVisibility() == View.GONE) {
                    editor_project_fggd.setVisibility(View.VISIBLE);
                } else {
                    editor_project_fggd.setVisibility(View.GONE);
                }

                return true;
            }
        });
        card_2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (editor_project_jtj.getVisibility() == View.GONE) {
                    editor_project_jtj.setVisibility(View.VISIBLE);
                } else {
                    editor_project_jtj.setVisibility(View.GONE);
                }
                return true;
            }
        });
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public void showMessage(@NonNull String message) {

    }

    @OnClick({R.id.card_1, R.id.card_2, R.id.card_3, R.id.card_4, R.id.editor_project_fggd, R.id.editor_project_jtj})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.card_1:
                launchActivity(new Intent(getActivity(), FGGD_TestActivity.class));
                break;
            case R.id.card_2:
                launchActivity(new Intent(getActivity(), JTJ_TestActivity.class));
                break;
            case R.id.card_3:
                launchActivity(new Intent(getActivity(), JTJ_TestActivity.class).putExtra("type", 1));
                break;
            case R.id.card_4:
                launchActivity(new Intent(getActivity(), JTJ_TestActivity.class).putExtra("type", 2));
                break;
            case R.id.editor_project_fggd:
                Intent intent = new Intent(mContext, EditorProjectActivity.class);
                intent.putExtra("from", "fggd");
                launchActivity(intent);
                break;
            case R.id.editor_project_jtj:
                Intent intent1 = new Intent(mContext, EditorProjectActivity.class);
                intent1.putExtra("from", "jtj");
                launchActivity(intent1);
                break;
            default:
                break;
        }
    }
}