package com.gwcd.sy.webparser.apkbus;

/**
 * Created by Lenovo on 2017/8/17.
 */

public class Banner {

    public String url;
    public String title;
    public String imgUrl;

    public Banner(String url, String title, String imgUrl) {
        this.url = url;
        this.title = title;
        this.imgUrl = imgUrl;
    }

    @Override
    public String toString() {
        return "Banner{" +
                "url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                '}';
    }
}
