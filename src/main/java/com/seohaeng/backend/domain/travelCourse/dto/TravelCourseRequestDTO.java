package com.seohaeng.backend.domain.travelCourse.dto;

import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

public class TravelCourseRequestDTO {

    @Getter
    public static class CreateTravelCourseDTO {
        private LocalDate startDate;
        private LocalDate endDate;
        private String travelCourseTitle;
        private List<Long> regionIdList;
        private List<travelCourseScheduleDTO> travelCourseScheduleList;
        private Boolean isPublic;
    }

    @Getter
    public static class travelCourseScheduleDTO {
        private LocalDate day;
        private Integer orderInday;
        private Long placeId;
    }
}