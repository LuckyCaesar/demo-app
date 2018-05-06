package com.demo.app.entity.common;

/**
 * Created by xiongcaesar on 2018/4/29.
 *
 */
public class Page {

    private int currentPage;

    private int pageSize;

    private int totalPage;

    private int totalSize;

    public Page() {
        this.pageSize = 10;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }
}
