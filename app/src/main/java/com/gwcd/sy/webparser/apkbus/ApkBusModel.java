package com.gwcd.sy.webparser.apkbus;

import android.util.Log;

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
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Lenovo on 2017/8/17.
 */

public class ApkBusModel implements ApkBusContract.Model {

    private static final String APK_BUS_URL = "http://www.apkbus.com/";

    private static final String APK_BUS_BLOG_URL = "http://www.apkbus.com/plugin.php?id=cxy_common_blog";

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
                .subscribeOn(Schedulers.newThread())
                .flatMap(new Function<String, Observable<Banner>>() {
                    @Override
                    public Observable<Banner> apply(@NonNull final String s) throws Exception {
                        return Observable.create(new ObservableOnSubscribe<Banner>() {
                            @Override
                            public void subscribe(@NonNull ObservableEmitter<Banner> emitter) throws Exception {
                                Log.d("--sy", "html parse thread id:" + android.os.Process.myTid());
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
    }

    @Override
    public void getBlogs() {
        final List<Blog> blogs = new ArrayList<>();
        NetworkUtils.get(APK_BUS_BLOG_URL, new NetworkUtils.Callback() {
            @Override
            public void onSuccess(final String response) {
                Observable.just(response)
                .subscribeOn(Schedulers.newThread())
                .flatMap(new Function<String, Observable<Blog>>() {
                    @Override
                    public Observable<Blog> apply(@NonNull final String s) throws Exception {
                        return Observable.create(new ObservableOnSubscribe<Blog>() {
                            @Override
                            public void subscribe(@NonNull ObservableEmitter<Blog> emitter) throws Exception {
                                Document doc = Jsoup.parse(s);
                                Elements rows = doc.getElementsByClass("row");
                                for (Element e : rows) {
                                    Blog blog = new Blog();
                                    Elements a = e.getElementsByTag("a");
                                    blog.url = a.attr("href");
                                    if (!blog.url.contains("http")) {
                                        blog.url = APK_BUS_URL + blog.url;
                                    }
                                    blog.title = a.select("h2").text();
                                    blog.resume = e.getElementsByClass("preview").text();
                                    blog.tags = new ArrayList<>();
                                    Elements tags = e.getElementsByClass("tags");
                                    if (!tags.isEmpty()) {
                                        tags = tags.get(0).children();
                                        for (Element tag : tags) {
                                            blog.tags.add(tag.select("button").text());
                                        }
                                    }
                                    Elements info = e.getElementsByClass("info");
                                    blog.userHeadUrl = info.select("img").attr("src");
                                    Elements spans = info.select("span");
                                    blog.userName = spans.get(0).text();
                                    blog.time = spans.get(2).text();
                                    blog.readCount = spans.get(3).text();
                                    blog.commentCount = spans.get(4).text();
                                    blog.favorCount = spans.get(5).text();
                                    emitter.onNext(blog);
                                }
                                emitter.onComplete();
                            }
                        });
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Blog>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Blog blog) {
                        blogs.add(blog);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mPresenter.getBlogs(blogs);
                    }

                    @Override
                    public void onComplete() {
                        mPresenter.getBlogs(blogs);
                    }
                });
            }

            @Override
            public void onError(Throwable throwable) {
                mPresenter.getBlogs(blogs);
            }
        });
    }
}
