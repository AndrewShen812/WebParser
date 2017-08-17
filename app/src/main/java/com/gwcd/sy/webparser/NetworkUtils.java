package com.gwcd.sy.webparser;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Lenovo on 2017/8/18.
 */

public class NetworkUtils {

    public static void get(final String url, final Callback callback) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(url).build();
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
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull String s) {
                if (callback != null) {
                    callback.onSuccess(s);
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                if (callback != null) {
                    callback.onError(e);
                }
            }

            @Override
            public void onComplete() {
            }
        });
    }

    public interface Callback {
        void onSuccess(String response);

        void onError(Throwable throwable);
    }
}
