package com.wangzs.base.bean;

import java.util.List;

/**
 * @Description
 * @Date 2022/4/27 027 11:47
 * @Created by wangzs
 */
public class RxPageResultBean<T> extends RxBean {


    private int hasMore;
    private String nextOffset;
    private List<T> list;

    public boolean hasMore() {
        return hasMore == 1;
    }

    public int getHasMore() {
        return hasMore;
    }

    public void setHasMore(int hasMore) {
        this.hasMore = hasMore;
    }

    public String getNextOffset() {
        return nextOffset;
    }

    public void setNextOffset(String nextOffset) {
        this.nextOffset = nextOffset;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

}
