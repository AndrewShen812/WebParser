package com.gwcd.sy.webparser.bing;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gwcd.sy.webparser.PageFragment;
import com.gwcd.sy.webparser.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sy on 2017/8/21.<br>
 * Function: <br>
 * Creator: sy<br>
 * Create time: 2017/8/21 9:02<br>
 * Revise Record:<br>
 * 2017/8/21: 创建并完成初始实现<br>
 */

public class BingFragment extends PageFragment implements BingContract.View{

    @BindView(R.id.iv_bing_bg)
    ImageView mIvBingBg;

    private BingPresenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new BingPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_bing, container, false);
        ButterKnife.bind(this, rootView);
        mPresenter.getBingBg();
        return rootView;
    }

    @Override
    public void showBingBg(String bgUrl) {
        Glide.with(this).load(bgUrl).into(mIvBingBg);
    }

    @Override
    public void showErrorMsg(String errMsg) {
        Toast.makeText(getContext(), "获取图片地址失败。" + errMsg, Toast.LENGTH_SHORT).show();
    }
}
