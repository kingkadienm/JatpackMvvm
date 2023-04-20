package com.wangzs.base.base.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.StringUtils;
import com.wangzs.base.R;

import java.util.ArrayList;
import java.util.List;


/**
 * @Description 通用的RecyclerView的Adapter
 * @Date 2022/4/27 027 11:30
 * @Created by wangzs
 */

public abstract class CommonRecyclerAdapter<T> extends RecyclerView.Adapter<ViewHolder> {
    private static final String TAG = CommonRecyclerAdapter.class.getSimpleName();
    protected Context mContext;
    protected List<T> mData;
    protected LayoutInflater mInflater;
    protected static final int DATA_VIEW = 0; //有数据
    protected static final int EMPTY_VIEW = 1;//空数据
    protected static final int LOADING_VIEW = 2;//加载
    protected static final int ERROR_VIEW = 3;//错误
    protected static final int NO_NET_VIEW = 4;//没网
    protected int mCurrentViewType = DATA_VIEW;//当前view类型
    private View.OnClickListener mListener;
    private String errorMsg;


    protected long mCurrentTime;// 服务器时间

    public void setCurrentTime(long currentTime) {
        this.mCurrentTime = currentTime;
    }

    public CommonRecyclerAdapter(Context context, List<T> data) {
        this.mContext = context;
        this.mData = data;
        this.mInflater = LayoutInflater.from(context);
        if (mData == null) {
            mData = new ArrayList<>();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mCurrentViewType == LOADING_VIEW) {
            return R.layout.layout_item_loading;
        } else if (mCurrentViewType == EMPTY_VIEW
                || mCurrentViewType == ERROR_VIEW
                || mCurrentViewType == NO_NET_VIEW) {
            return R.layout.layout_item_common_no_data;
        } else {
            return getLayoutId(mData.get(position), position);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(viewType, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (mCurrentViewType == LOADING_VIEW) {
            // DO NOTHING
            return;
        }
        if (mCurrentViewType == EMPTY_VIEW
                || mCurrentViewType == ERROR_VIEW
                || mCurrentViewType == NO_NET_VIEW) {
            // 图片
            ImageView imageView = holder.getView(R.id.iv_load_no_data_image);
            imageView.setImageResource(getImageBtType(mCurrentViewType));
            // 描述
            TextView descTv = holder.getView(R.id.tv_load_no_data_desc);
            descTv.setText(getDescBtType(mCurrentViewType, errorMsg));
            // 按钮
            TextView reloadTv = holder.getView(R.id.tv_load_no_data_re_load_btn);
            if (mCurrentViewType == EMPTY_VIEW) {
                // 用onClickListener控制是否显示下方点击重新加载的文字
                if (mListener == null) {
                    reloadTv.setVisibility(View.GONE);
                    reloadTv.setOnClickListener(null);
                } else {
                    reloadTv.setVisibility(View.VISIBLE);
                    reloadTv.setOnClickListener(mListener);
                }
            } else if (mCurrentViewType == ERROR_VIEW || mCurrentViewType == NO_NET_VIEW) {
                reloadTv.setVisibility(View.VISIBLE);
                reloadTv.setOnClickListener(mListener);
            }
        } else {
            convert(holder, position, mData.get(position));
        }
    }

    private int getImageBtType(int type) {
        if (type == EMPTY_VIEW) {
            return R.mipmap.icon_empty;
        } else   if (type == ERROR_VIEW) {
            return R.mipmap.icon_load_error;
        } else if (type == NO_NET_VIEW) {
            return R.mipmap.icon_network;
        }
        return 0;
    }

    private String getDescBtType(int type, String tips) {
        if (type == EMPTY_VIEW) {
            return StringUtils.isEmpty(tips) ? mContext.getString(R.string.str_no_data) : tips;
        } else if (type == ERROR_VIEW) {
            return StringUtils.isEmpty(tips) ? mContext.getString(R.string.server_net_error) : tips;
        } else if (type == NO_NET_VIEW) {
            return mContext.getString(R.string.str_net_woring);
        }
        return "";
    }

    @Override
    public int getItemCount() {
        if (mCurrentViewType == DATA_VIEW) {
            return mData == null ? 0 : mData.size();
        } else {
            return 1;
        }
    }

    protected abstract int getLayoutId(T item, int position);

    public abstract void convert(ViewHolder holder, int position, T item);

    public void showLoadingView() {
        this.mCurrentViewType = LOADING_VIEW;
        this.notifyDataSetChanged();
    }

    /**
     * 显示无数据的布局，listener为null的时候，不显示下方点击重新加载的问题，非null的时候显示
     */
    public void showEmptyView(View.OnClickListener listener) {
        this.mListener = listener;
        this.mCurrentViewType = EMPTY_VIEW;
        this.notifyDataSetChanged();
    }

    public void showErrorView(String errorMsg, View.OnClickListener onClickListener) {
        this.mListener = onClickListener;
        this.mCurrentViewType = ERROR_VIEW;
        this.errorMsg = errorMsg;
        this.notifyDataSetChanged();
    }

    public void showNoNetView(View.OnClickListener onClickListener) {
        this.mListener = onClickListener;
        this.mCurrentViewType = NO_NET_VIEW;
        this.notifyDataSetChanged();
    }

    public void setNewData(List<T> list) {
        mData.clear();
        if (list != null) {
            mData.addAll(list);
        }
        showDataList();
    }

    public void showDataList() {
        this.mCurrentViewType = DATA_VIEW;
        this.notifyDataSetChanged();
    }

    public void remove(int position) {
        mData.remove(position);
        notifyDataSetChanged();
    }

    public void remove(T t) {
        mData.remove(t);
        notifyDataSetChanged();
    }

    public void removeAll() {
        mData.clear();
        notifyDataSetChanged();
    }

    public void add(T t) {
        mData.add(t);
        notifyDataSetChanged();
    }

    public void addLast(T e) {
        add(e);
    }

    public void addFirst(T e) {
        mData.add(0, e);
        notifyDataSetChanged();
    }

    public void clear() {
        mData.clear();
        notifyDataSetChanged();
    }

    public List<T> getDataList() {
        return mData;
    }
}
