package com.sparta.outsourcing_nt.util.result;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;


@Getter
@JsonPropertyOrder({"statusCode", "message", "data"})
public class ApiResult<T> {
    private final String statusCode;
    private final String message;
    private final T data;

    public ApiResult(String statusCode, String message, T data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResult<T> success(String message,T data) {
        return new ApiResult<>("Success", message ,data);
    }
    public static <T> ApiResult<T> success(String message) {
        return new ApiResult<>("Success", message ,null);
    }

}