package com.gwcd.sy.webparser.apkbus;

import java.util.List;

/**
 * Created by Lenovo on 2017/8/17.
 */

public class ApkBusPresenter implements ApkBusContract.Presenter {
    private ApkBusContract.View mApkBusView;
    private ApkBusContract.Model mModel;

    public ApkBusPresenter(ApkBusContract.View mApkBusView) {
        this.mApkBusView = mApkBusView;
        mModel = new ApkBusModel(this);
    }

    public void loadBanners() {
        mModel.getBanners();
    }

    public void loadBlogs() {
        mModel.getBlogs();
    }

    @Override
    public void getBanners(List<Banner> banners) {
        mApkBusView.showBanners(banners);
    }

    @Override
    public void getBlogs(List<Blog> blogs) {
        mApkBusView.showBlogs(blogs);
    }
}
