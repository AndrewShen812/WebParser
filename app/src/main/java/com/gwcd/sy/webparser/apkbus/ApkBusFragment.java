package com.gwcd.sy.webparser.apkbus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gwcd.sy.webparser.PageFragment;
import com.gwcd.sy.webparser.R;

import java.util.List;

/**
 * Created by Lenovo on 2017/8/17.
 */

public class ApkBusFragment extends PageFragment implements ApkBusContract.View{

    private TextView mTvLabel;
    private ApkBusPresenter mPresenter;

    public ApkBusFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new ApkBusPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mTvLabel = (TextView) rootView.findViewById(R.id.tv_label);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.loadBanners();
    }

    @Override
    public void showBanners(List<Banner> banners) {
        for (Banner banner : banners) {
            mTvLabel.append(banner.toString() + "\n");
        }
    }
}
