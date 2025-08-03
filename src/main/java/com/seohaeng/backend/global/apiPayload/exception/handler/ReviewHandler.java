package com.seohaeng.backend.global.apiPayload.exception.handler;

import com.seohaeng.backend.global.apiPayload.code.BaseErrorCode;
import com.seohaeng.backend.global.apiPayload.exception.GeneralException;

public class ReviewHandler extends GeneralException {
    public ReviewHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
