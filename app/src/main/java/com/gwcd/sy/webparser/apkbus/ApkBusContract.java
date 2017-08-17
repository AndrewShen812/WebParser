package com.gwcd.sy.webparser.apkbus;

import java.util.List;

/**
 * Created by Lenovo on 2017/8/17.
 */

public interface ApkBusContract {

    interface Model {
        List<Banner> getBanners();
    }

    interface View {
        void showBanners(List<Banner> banners);
    }

    interface Presenter {
        void getBanners(List<Banner> banners);
    }
}
