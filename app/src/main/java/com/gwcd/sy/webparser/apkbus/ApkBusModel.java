package com.gwcd.sy.webparser.apkbus;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Lenovo on 2017/8/17.
 */

public class ApkBusModel implements ApkBusContract.Model {

    private static final String APK_BUS_URL = "http://www.apkbus.com/";

    private ApkBusContract.Presenter mPresenter;

    public ApkBusModel(ApkBusContract.Presenter mPresenter) {
        this.mPresenter = mPresenter;
    }

    @Override
    public List<Banner> getBanners() {
        final List<Banner> bannerList = new ArrayList<>();
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url("http://www.apkbus.com/").build();
                Call call = client.newCall(request);
                try {
                    Response response = call.execute();
                    e.onNext(new String(response.body().bytes()));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                e.onComplete();
            }
        })
        .flatMap(new Function<String, Observable<Banner>>() {
            @Override
            public Observable<Banner> apply(@NonNull final String s) throws Exception {
                return Observable.create(new ObservableOnSubscribe<Banner>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<Banner> emitter) throws Exception {
                        Document doc = Jsoup.parse(s);
                        Element ele = doc.getElementById("theTarget");
                        Elements elements = ele.children();
                        String url, title, imgUrl;
                        for (Element e : elements) {
                            Elements a = e.getElementsByTag("a");
                            url = a.attr("href");
                            title = a.select("span").text();
                            imgUrl = a.select("img").attr("src");
                            if (!imgUrl.contains("http")) {
                                imgUrl = APK_BUS_URL + imgUrl;
                            }
                            if (!url.isEmpty() || !imgUrl.isEmpty()) {
                                emitter.onNext(new Banner(url, title, imgUrl));
                            }
                        }
                        emitter.onComplete();
                    }
                });
            }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<Banner>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull Banner s) {
                bannerList.add(s);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                mPresenter.getBanners(bannerList);
            }

            @Override
            public void onComplete() {
                mPresenter.getBanners(bannerList);
            }
        });
        return null;
    }
}
