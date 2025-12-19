package com.walkingpet.common.core.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 统一响应结果
 */
@Data
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 错误码，0表示成功
     */
    private Integer error;

    /**
     * 响应数据
     */
    private T body;

    /**
     * 返回信息
     */
    private String message;

    /**
     * 是否成功
     */
    private Boolean success;

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setError(0);
        result.setBody(data);
        result.setMessage("操作成功");
        result.setSuccess(true);
        return result;
    }

    public static <T> Result<T> success(T data, String message) {
        Result<T> result = new Result<>();
        result.setError(0);
        result.setBody(data);
        result.setMessage(message);
        result.setSuccess(true);
        return result;
    }

    public static <T> Result<T> fail(String message) {
        Result<T> result = new Result<>();
        result.setError(1);
        result.setBody(null);
        result.setMessage(message);
        result.setSuccess(false);
        return result;
    }

    public static <T> Result<T> fail(Integer error, String message) {
        Result<T> result = new Result<>();
        result.setError(error);
        result.setBody(null);
        result.setMessage(message);
        result.setSuccess(false);
        return result;
    }
}
