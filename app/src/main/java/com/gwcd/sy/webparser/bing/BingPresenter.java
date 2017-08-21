package com.gwcd.sy.webparser.bing;

import android.text.TextUtils;

/**
 * Created by sy on 2017/8/21.<br>
 * Function: <br>
 * Creator: sy<br>
 * Create time: 2017/8/21 9:06<br>
 * Revise Record:<br>
 * 2017/8/21: 创建并完成初始实现<br>
 */

public class BingPresenter implements BingContract.Presenter {
    private BingContract.View mView;
    private BingContract.Model mModel;

    public BingPresenter(BingContract.View mView) {
        this.mView = mView;
        mModel = new BingModel(this);
    }

    @Override
    public void getBingBg() {
        mModel.getBingBg();
    }

    @Override
    public void showBingBg(String bgUrl) {
        if (!TextUtils.isEmpty(bgUrl)) {
            mView.showBingBg(bgUrl);
        } else {
            mView.showErrorMsg("BgUrl is empty.");
        }
    }

    @Override
    public void showErrorMsg(String errMsg) {
        mView.showErrorMsg(errMsg);
    }
}
