package com.seohaeng.backend.global.apiPayload.exception.handler;

import com.seohaeng.backend.global.apiPayload.code.BaseErrorCode;
import com.seohaeng.backend.global.apiPayload.exception.GeneralException;

public class TravelCourseHandler  extends GeneralException {
    public TravelCourseHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
