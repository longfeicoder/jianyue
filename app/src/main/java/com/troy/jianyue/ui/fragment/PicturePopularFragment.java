package com.troy.jianyue.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.troy.jianyue.R;
import com.troy.jianyue.adapter.PictureAdapter;
import com.troy.jianyue.bean.Picture;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenlongfei on 15/5/8.
 */
public class PicturePopularFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private PictureAdapter mPictureAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private List<Picture> mPictureList = new ArrayList<Picture>();
    private static final int LIMIT = 5;
    private int mSkip = 0;
    private boolean mIsLoading;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActionBar.setTitle(mTitles[1]);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_picture, null);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mPictureAdapter = new PictureAdapter(getActivity(), mPictureList);
        mRecyclerView = (RecyclerView) root.findViewById(R.id.picture_recycler_view);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mPictureAdapter);
        mSwipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.picture_swiperefresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        return root;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadData();
        addListener();
    }

    private void addListener() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int firstVisibleItemPosition = mLinearLayoutManager.findFirstVisibleItemPosition();
                int lastVisibleItemPosition = mLinearLayoutManager.findLastVisibleItemPosition();
                int totalItem = mLinearLayoutManager.getItemCount();
                if (dy > 0) {
                    if (lastVisibleItemPosition == mPictureList.size() && !mIsLoading) {
                        Log.i("Troy", "加载更多");
                        loadMoreData();
                    }
                }
                super.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 1000);
    }

    private void loadData() {
        mPictureList.clear();
        requestServer();
    }

    private void loadMoreData() {
        mSkip = mPictureList.size();
        mIsLoading=true;
        requestServer();
    }

    private void requestServer() {
        AVQuery<Picture> pictureAVQuery = AVObject.getQuery(Picture.class);
        pictureAVQuery.setLimit(LIMIT);
        pictureAVQuery.setSkip(mSkip);
        pictureAVQuery.orderByDescending("createdAt");
        pictureAVQuery.findInBackground(new FindCallback<Picture>() {
            @Override
            public void done(List<Picture> list, AVException e) {
                if (e == null) {
                    mPictureList.addAll(list);
                    mPictureAdapter.notifyDataSetChanged();
                    mIsLoading=false;
                } else {

                }
            }

        });
    }
}