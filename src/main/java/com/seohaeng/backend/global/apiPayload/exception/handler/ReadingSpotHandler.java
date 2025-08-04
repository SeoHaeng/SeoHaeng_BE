package com.seohaeng.backend.global.apiPayload.exception.handler;

import com.seohaeng.backend.global.apiPayload.code.BaseErrorCode;
import com.seohaeng.backend.global.apiPayload.exception.GeneralException;

public class ReadingSpotHandler extends GeneralException {
    public ReadingSpotHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
