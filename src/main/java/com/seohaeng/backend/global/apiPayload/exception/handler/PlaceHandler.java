package com.seohaeng.backend.global.apiPayload.exception.handler;

import com.seohaeng.backend.global.apiPayload.code.BaseErrorCode;
import com.seohaeng.backend.global.apiPayload.exception.GeneralException;

public class PlaceHandler extends GeneralException {
    public PlaceHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
