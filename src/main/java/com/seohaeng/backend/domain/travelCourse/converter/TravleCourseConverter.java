package com.seohaeng.backend.domain.travelCourse.converter;

import com.seohaeng.backend.domain.travelCourse.entity.Stamp;
import com.seohaeng.backend.domain.user.entity.User;

public class TravleCourseConverter {

    public static Stamp toStamp(User joinUser) {
        return Stamp.builder()
                .user(joinUser)
                .chuncheon(false)
                .wonju(false)
                .gangneung(false)
                .donghae(false)
                .taebaek(false)
                .sokcho(false)
                .samcheok(false)
                .hongcheon(false)
                .hoengseong(false)
                .yeongwol(false)
                .pyeongchang(false)
                .jeongseon(false)
                .cheorwon(false)
                .hwacheon(false)
                .yanggu(false)
                .inje(false)
                .goseong(false)
                .yangyang(false)
                .build();
    }
}
