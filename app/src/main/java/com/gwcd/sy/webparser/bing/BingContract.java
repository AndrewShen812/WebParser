package com.gwcd.sy.webparser.bing;

/**
 * Created by sy on 2017/8/21.<br>
 * Function: <br>
 * Creator: sy<br>
 * Create time: 2017/8/21 9:00<br>
 * Revise Record:<br>
 * 2017/8/21: 创建并完成初始实现<br>
 */

public interface BingContract {

    interface Model {
        void getBingBg();
    }

    interface View {
        void showBingBg(String bgUrl);
        void showErrorMsg(String errMsg);
    }

    interface Presenter {
        void getBingBg();
        void showBingBg(String bgUrl);
        void showErrorMsg(String errMsg);
    }
}
