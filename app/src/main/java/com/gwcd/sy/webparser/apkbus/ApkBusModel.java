package com.gwcd.sy.webparser.apkbus;

import com.gwcd.sy.webparser.NetworkUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

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
    public void getBanners() {
        final List<Banner> bannerList = new ArrayList<>();
        NetworkUtils.get(APK_BUS_URL, new NetworkUtils.Callback() {
            @Override
            public void onSuccess(String response) {
                Observable.just(response)
                        .subscribeOn(Schedulers.io())
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
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Banner>() {
                    @Override
                    public void accept(Banner banner) throws Exception {
                        bannerList.add(banner);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        mPresenter.getBanners(bannerList);
                    }
                });
            }

            @Override
            public void onError(Throwable throwable) {
                mPresenter.getBanners(bannerList);
            }
        });
        /**
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
         */
    }

    @Override
    public void getBlogs() {
    }
}
