package com.seohaeng.backend.global.apiPayload.exception.handler;

import com.seohaeng.backend.global.apiPayload.code.BaseErrorCode;
import com.seohaeng.backend.global.apiPayload.exception.GeneralException;

public class BookChallengeHandler extends GeneralException {
    public BookChallengeHandler(BaseErrorCode code) {
        super(code);
    }
}
