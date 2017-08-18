package com.gwcd.sy.webparser.apkbus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gwcd.sy.webparser.PageFragment;
import com.gwcd.sy.webparser.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Lenovo on 2017/8/17.
 */

public class ApkBusFragment extends PageFragment implements ApkBusContract.View {

    @BindView(R.id.vp_apk_bus_banner)
    ViewPager mBannerPager;
    @BindView(R.id.rv_blog_list)
    RecyclerView mRvBlogList;
    private ApkBusPresenter mPresenter;
    private int[] bgColors;

    public ApkBusFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new ApkBusPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, rootView);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.shape_blog_list_divider));
        mRvBlogList.addItemDecoration(itemDecoration);
        mRvBlogList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        bgColors = new int[]{getResources().getColor(R.color.blog_bg1),
                getResources().getColor(R.color.blog_bg2),
                getResources().getColor(R.color.blog_bg3),
                getResources().getColor(R.color.blog_bg4),
                getResources().getColor(R.color.blog_bg5),
                getResources().getColor(R.color.blog_bg6),
                getResources().getColor(R.color.blog_bg7),
                getResources().getColor(R.color.blog_bg8),
                getResources().getColor(R.color.blog_bg9),
                getResources().getColor(R.color.blog_bg10)};
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("--sy", "ui thread id:" + android.os.Process.myTid());
        mPresenter.loadBanners();
        mPresenter.loadBlogs();
    }

    @Override
    public void showBanners(List<Banner> banners) {
        final List<ImageView> bannerViews = new ArrayList<>(banners.size());
        for (Banner banner : banners) {
            Log.d("--sy", banner.toString());
            ImageView ivBanner = new ImageView(getContext());
            ivBanner.setLayoutParams(new ViewPager.LayoutParams());
            Glide.with(getContext()).load(banner.imgUrl).into(ivBanner);
            bannerViews.add(ivBanner);
        }
        mBannerPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return bannerViews.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(bannerViews.get(position), 0);// 添加页卡
                return bannerViews.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(bannerViews.get(position));
            }
        });
    }

    private BlogListAdater mBlogListAdater;

    @Override
    public void showBlogs(final List<Blog> blogs) {
        for (Blog blog : blogs) {
            Log.d("--sy", blog.toString());
        }
        if (mBlogListAdater == null) {
            mBlogListAdater = new BlogListAdater(blogs);
            mRvBlogList.setAdapter(mBlogListAdater);
        } else {
            mBlogListAdater.setBlogData(blogs);
        }
        mBlogListAdater.notifyDataSetChanged();
    }

    private class BlogListAdater extends RecyclerView.Adapter<BlogListHolder> {

        List<Blog> blogData;

        public BlogListAdater(List<Blog> blogData) {
            this.blogData = blogData;
        }

        public void setBlogData(List<Blog> blogData) {
            this.blogData = blogData;
        }

        @Override
        public BlogListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View rooView = LayoutInflater.from(getContext()).inflate(R.layout.layout_blog_list_item, parent, false);
            return new BlogListHolder(rooView);
        }

        @Override
        public void onBindViewHolder(BlogListHolder holder, int position) {
            Blog blog = blogData.get(position);
            Glide.with(getContext()).load(blog.userHeadUrl).into(holder.mIvIcon);
            holder.mTvName.setText(blog.userName);
            holder.mTvTitle.setText(blog.title);
            holder.mTvResume.setText(blog.resume);
            holder.mTvTime.setText(blog.time);
            holder.mTvStat.setText(blog.readCount + "  " + blog.commentCount + "  " + blog.favorCount);
            holder.mRoot.setBackgroundColor(bgColors[position % bgColors.length]);
        }

        @Override
        public int getItemCount() {
            return blogData.size();
        }
    }

    class BlogListHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_blog_list_user_icon)
        ImageView mIvIcon;
        @BindView(R.id.tv_blog_list_user)
        TextView mTvName;
        @BindView(R.id.tv_blog_list_title)
        TextView mTvTitle;
        @BindView(R.id.tv_blog_list_resume)
        TextView mTvResume;
        @BindView(R.id.tv_blog_list_time)
        TextView mTvTime;
        @BindView(R.id.tv_blog_list_read_stat)
        TextView mTvStat;
        View mRoot;

        public BlogListHolder(View itemView) {
            super(itemView);
            mRoot = itemView;
            ButterKnife.bind(this, itemView);
        }
    }
}
