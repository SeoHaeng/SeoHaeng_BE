package com.seohaeng.backend.domain.travelCourse.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

public class TravelCourseResponseDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GetTravelCourseResponseDTO {
        private Long travelCourseId;
        private Long memberId;
        private String courseTitle;
        private LocalDate startDate;
        private LocalDate endDate;
        private List<String> travelRegions;
        private List<GetTravelCourseScheduleResponseDayDTO> schedules;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GetTravelCourseScheduleResponseDTO{
        private Integer orderInday;
        private Long placeId;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GetTravelCourseScheduleResponseDayDTO{
        private Long day;
        private LocalDate date;
        private List<GetTravelCourseScheduleResponseDTO> schedules;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GetTravelCourseListItemDTO {
        private String title;
        private Long travelCourseId;
        private Long memberId;
        private String imageUrl;
        private LocalDate startDate;
        private LocalDate endDate;
        private String duration;
        private List<String> travelRegions;
    }
}