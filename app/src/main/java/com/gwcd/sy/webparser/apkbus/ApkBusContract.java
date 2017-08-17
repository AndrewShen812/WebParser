package com.gwcd.sy.webparser.apkbus;

import java.util.List;

/**
 * Created by Lenovo on 2017/8/17.
 */

public interface ApkBusContract {

    interface Model {
        void getBanners();
        void getBlogs();
    }

    interface View {
        void showBanners(List<Banner> banners);
        void showBlogs(List<Blog> blogs);
    }

    interface Presenter {
        void getBanners(List<Banner> banners);
        void getBlogs(List<Blog> blogs);
    }
}
