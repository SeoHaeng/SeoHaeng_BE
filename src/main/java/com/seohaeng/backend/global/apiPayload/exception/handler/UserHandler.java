package com.seohaeng.backend.global.apiPayload.exception.handler;

import com.seohaeng.backend.global.apiPayload.code.BaseErrorCode;
import com.seohaeng.backend.global.apiPayload.exception.GeneralException;

public class UserHandler extends GeneralException {

    public UserHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}