package com.demo.app.entity.common;

/**
 * Created by xiongcaesar on 2018/4/29.
 *
 */
public class Result<T> {

    private int code; // -1，1

    private String message;

    private T data;

    private Page page;

    public static <T> Result build(int code, String message, T data) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    public static <T> Result success(String message, T data) {
        return build(1, message, data);
    }

    public static Result success(String message) {
        return build(1, message, null);
    }

    public static <T> Result success(T data) {
        return build(1, "success", data);
    }

    public static Result fail(String message) {
        return build(-1, message, null);
    }

    public static <T> Result fail(String message, T data) {
        return build(-1, message, data);
    }

    public Result setCurrentPage(int currentPage) {
        this.getPage().setCurrentPage(currentPage);
        return this;
    }

    public Result setPageSize(int pageSize) {
        this.getPage().setPageSize(pageSize);
        return this;
    }

    public Result setTotalPage(int totalSize, int pageSize) {
        if (pageSize == 0) {
            this.getPage().setTotalPage(0);
            return this;
        }
        int totalPage = totalSize / pageSize;
        if (totalSize % pageSize > 0) {
            totalPage += 1;
        }
        this.getPage().setTotalPage(totalPage);
        return this;
    }

    public Result setTotalSize(int totalSize) {
        this.getPage().setTotalSize(totalSize);
        return this;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Page getPage() {
        if (this.page == null) {
            this.page = new Page();
        }
        return this.page;
    }

    public void setPage(Page page) {
        this.page = page;
    }
}
