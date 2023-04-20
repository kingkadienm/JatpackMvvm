package com.wangzs.base.base.activity;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.wangzs.base.R;
import com.wangzs.base.base.adapter.CommonRecyclerAdapter;
import com.wangzs.base.base.adapter.ViewHolder;
import com.wangzs.base.bean.RxPageResultBean;
import com.wangzs.base.weight.CustomLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;


/**
 * @Description 分页Activity的基类
 * @Date 2022/4/27 027 13:15
 * @Created by wangzs
 */


public abstract class BasePageActivity<T> extends BaseActivity {

    protected XRecyclerView mRecyclerView;
    protected List<T> mData = new ArrayList<>();
    protected CommonRecyclerAdapter<T> mAdapter;
    protected int mCurrentPage = 1, mPageSize = 10;
    protected RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected int getLayoutResId() {
        return R.layout.base_page_fragment;
    }

    @Override
    protected void initView() {
        mRecyclerView = getRecyclerView();
        if (mRecyclerView != null) {
            mLayoutManager = getLayoutManager();
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
            mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
            mRecyclerView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);
            mRecyclerView.setPullRefreshEnabled(true);
            mRecyclerView.setLoadingMoreEnabled(false);
            mAdapter = getAdapter();
            SimpleItemAnimator animator = (SimpleItemAnimator) mRecyclerView.getItemAnimator();
            if (animator != null) {
                animator.setSupportsChangeAnimations(false);
            }
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.showLoadingView();

            mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
                @Override
                public void onRefresh() {
                    mCurrentPage = 1;
                    requestData(true, false);
                }

                @Override
                public void onLoadMore() {
                    requestData(false, false);
                }
            });
            // 控制为空和非空的时候每行数量
            if (mLayoutManager instanceof GridLayoutManager) {
                ((GridLayoutManager) mLayoutManager).setSpanSizeLookup(getSpanSizeLookup());
            }

        }
    }

    protected CommonRecyclerAdapter<T> getAdapter() {
        return new MyAdapter(mContext, mData);
    }

    protected GridLayoutManager.SpanSizeLookup getSpanSizeLookup() {
        return new GridLayoutManager.SpanSizeLookup() {
            // TODO 注意：当RecyclerView 又添加了Header和Footer之后，这里需要自定义，因为position是包括所有Header和
            //      footer之后生成的索引，需要去掉不必要的Header和Footer，XRecyclerView 默认带一个RefreshHeader和LoadFooter
            @Override
            public int getSpanSize(int position) {
                int spanCount = ((GridLayoutManager) mLayoutManager).getSpanCount();
                // 默认带有刷新的Header，索引会多1；默认带有LoadFooter,footer的索引会比dataSize多1
                if (position == 0 || position == mData.size() + 1) {// refreshHeader and Footer
                    return spanCount;
                } else {// 真正的显示条目，数据为空的时候占满
                    return mData.size() == 0 ? spanCount : 1;
                }
            }
        };
    }


    protected int getHeaderFooterCount() {
        return 0;
    }


    @Override
    protected void initData() {
        requestData(true, false);
    }

    private class MyAdapter extends CommonRecyclerAdapter<T> {

        public MyAdapter(Context context, List<T> data) {
            super(context, data);
        }

        @Override
        public int getItemViewType(int position) {

            return super.getItemViewType(position);
        }

        @Override
        protected int getLayoutId(T item, int position) {
            return getItemLayoutId();
        }

        @Override
        public void convert(ViewHolder holder, int position, T item) {
            convertView(holder, position, item);
        }
    }

    /**
     * 数据成功后统一处理
     */
    protected void onSuccessData(boolean isRefresh, List<T> list, boolean hasMore) {

        // 下拉刷新的时候清数据
        if (isRefresh) {
            mData.clear();
        }
        // 添加数据
        if (list != null) {
            makeShowData(list);
        }
        // 根据是否加载全部条数判断
        if (!hasMore) {// 已经加载全部条数
            if (isRefresh && mData.size() == 0) {// 下拉并且没数据的时候，显示无数据布局
                mAdapter.showEmptyView(null);
            } else {// 否则都显示没有更多了
                mAdapter.showDataList();
                mRecyclerView.setNoMore(true);
            }
            mRecyclerView.setLoadingMoreEnabled(false);
        } else {// 未加载全部条数，正常走数据加载和页面累加
            mAdapter.showDataList();
            // 开启上拉
            mRecyclerView.setLoadingMoreEnabled(true);
            mRecyclerView.setNoMore(false);
            // 页码自增
            mCurrentPage++;
        }
        // 停止刷新
        stopRefreshAndLoad(isRefresh);
    }

    protected void onSuccessData(boolean isRefresh, boolean scrollToTop, RxPageResultBean<T> result) {

        if (result == null) {
            mAdapter.showErrorView("", new ReloadClickListener());
            stopRefreshAndLoad(isRefresh);
            return;
        }
        onSuccessData(isRefresh, result.getList(), result.hasMore());
        if (scrollToTop) {
            mRecyclerView.scrollToPosition(0);
        }
    }

    /**
     * 数据失败后统一处理
     */
    protected void onFailData(boolean isRefresh) {

        if (mData.size() == 0) {
            mAdapter.showEmptyView(new ReloadClickListener());
        } else if (isRefresh) {
            mAdapter.showErrorView(null, new ReloadClickListener());
        }
        if (mRecyclerView != null) {
            stopRefreshAndLoad(isRefresh);
        }
    }

    /**
     * 数据失败后统一处理
     */
    protected void onNoNet(boolean isRefresh) {

        if (mData.size() == 0) {
            mAdapter.showNoNetView(new ReloadClickListener());
        }
        if (mRecyclerView != null) {
            stopRefreshAndLoad(isRefresh);
        }
    }

    /**
     * 停止刷新
     */
    protected void stopRefreshAndLoad(boolean isRefresh) {
        if (isRefresh) {
            mRecyclerView.refreshComplete();
        } else {
            mRecyclerView.loadMoreComplete();
        }
    }

    public class ReloadClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (mAdapter != null) {
                mAdapter.showLoadingView();
            }
            requestData(true, false);
        }
    }

    protected abstract int getItemLayoutId();

    protected abstract void convertView(ViewHolder holder, int position, T item);

    protected abstract void requestData(boolean isRefresh, boolean scrollToTop);

    protected void makeShowData(List<T> list) {
        mData.addAll(list);
    }

    protected RecyclerView.LayoutManager getLayoutManager() {
        return new CustomLinearLayoutManager(mContext);
    }

    protected XRecyclerView getRecyclerView() {
        return (XRecyclerView) findViewById(R.id.recycler_base_page_list);
    }
}