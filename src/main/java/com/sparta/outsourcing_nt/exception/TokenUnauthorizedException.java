package com.sparta.outsourcing_nt.exception;

    public class TokenUnauthorizedException extends RuntimeException {
        public TokenUnauthorizedException(String message) {
            super(message);
        }
}
