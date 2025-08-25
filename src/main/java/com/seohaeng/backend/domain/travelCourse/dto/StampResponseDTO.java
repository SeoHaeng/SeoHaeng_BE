package com.seohaeng.backend.domain.travelCourse.dto;

import lombok.*;

import java.time.LocalDate;

public class StampResponseDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GetMyStampResponseDTO {
        private Long userId;
        private Integer totalStampCount;
        private GetMyStampListDTO stampList;
        private RegionImagesDTO regionImageList;
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetMyStampListDTO {
        private LocalDate chuncheon = null;
        private LocalDate wonju = null;
        private LocalDate gangneung = null;
        private LocalDate donghae = null;
        private LocalDate taebaek = null;
        private LocalDate sokcho = null;
        private LocalDate samcheok = null;
        private LocalDate hongcheon = null;
        private LocalDate hoengseong = null;
        private LocalDate yeongwol = null;
        private LocalDate pyeongchang = null;
        private LocalDate jeongseon = null;
        private LocalDate cheorwon = null;
        private LocalDate hwacheon = null;
        private LocalDate yanggu = null;
        private LocalDate inje = null;
        private LocalDate goseong = null;
        private LocalDate yangyang = null;
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegionImagesDTO {
        private String chuncheon = null;
        private String wonju = null;
        private String gangneung = null;
        private String donghae = null;
        private String taebaek = null;
        private String sokcho = null;
        private String samcheok = null;
        private String hongcheon = null;
        private String hoengseong = null;
        private String yeongwol = null;
        private String pyeongchang = null;
        private String jeongseon = null;
        private String cheorwon = null;
        private String hwacheon = null;
        private String yanggu = null;
        private String inje = null;
        private String goseong = null;
        private String yangyang = null;
    }
}