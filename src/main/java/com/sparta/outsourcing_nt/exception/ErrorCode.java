package com.sparta.outsourcing_nt.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    INVALID_FORMAT(HttpStatus.BAD_REQUEST, "ERR001","요청 값의 형식이 맞지 않습니다.");
    STORE_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "ERR017", "소유할 수 있는 가게의 최대 개수를 초과했습니다.");

    private final HttpStatus httpStatus;
    private final String status;
    private final String message;
}
