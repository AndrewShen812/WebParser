package com.gwcd.sy.webparser.bing;

import android.util.Log;

import com.gwcd.sy.webparser.NetworkUtils;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by sy on 2017/8/21.<br>
 * Function: <br>
 * Creator: sy<br>
 * Create time: 2017/8/21 9:07<br>
 * Revise Record:<br>
 * 2017/8/21: 创建并完成初始实现<br>
 */

public class BingModel implements BingContract.Model {

    public static final String BING_URL = "http://cn.bing.com";
    private static final String TAG = "BingModel";

    private BingContract.Presenter mPresenter;

    public BingModel(BingContract.Presenter mPresenter) {
        this.mPresenter = mPresenter;
    }

    @Override
    public void getBingBg() {
        NetworkUtils.get(BING_URL, new NetworkUtils.Callback() {
            @Override
            public void onSuccess(String response) {
                Observable.just(response)
                .subscribeOn(Schedulers.newThread())
                .map(new Function<String, String>() {
                    @Override
                    public String apply(@NonNull String s) throws Exception {
                        int startIndex = s.indexOf("g_img={url: \"");
                        int endindex = s.indexOf(".jpg", startIndex);
                        String bgUrl = null;
                        if (startIndex != -1 && endindex != -1) {
                            bgUrl = s.substring(startIndex + 13, endindex + 4);
                            if (!bgUrl.contains("http")) {
                                bgUrl = BING_URL + bgUrl;
                            }
                        }
                        Log.d(TAG, "bgUrl:" + bgUrl);
                        return bgUrl;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        mPresenter.showBingBg(s);
                    }
                });
            }

            @Override
            public void onError(Throwable throwable) {
                mPresenter.showErrorMsg(throwable.getMessage());
            }
        });
    }
}
