package com.seohaeng.backend.domain.travelCourse.dto;

import lombok.*;

public class StampResponseDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GetMyStampResponseDTO {
        private Long userId;
        private Integer totalStampCount;
        private GetMyStampListDTO stampList;
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetMyStampListDTO {
        private Boolean chuncheon = false;
        private Boolean wonju = false;
        private Boolean gangneung = false;
        private Boolean donghae = false;
        private Boolean taebaek = false;
        private Boolean sokcho = false;
        private Boolean samcheok = false;
        private Boolean hongcheon = false;
        private Boolean hoengseong = false;
        private Boolean yeongwol = false;
        private Boolean pyeongchang = false;
        private Boolean jeongseon = false;
        private Boolean cheorwon = false;
        private Boolean hwacheon = false;
        private Boolean yanggu = false;
        private Boolean inje = false;
        private Boolean goseong = false;
        private Boolean yangyang = false;
    }
}