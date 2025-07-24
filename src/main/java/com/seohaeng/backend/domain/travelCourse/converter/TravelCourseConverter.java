package com.seohaeng.backend.domain.travelCourse.converter;

import com.seohaeng.backend.domain.place.entity.Place;
import com.seohaeng.backend.domain.travelCourse.dto.TravelCourseRequestDTO;
import com.seohaeng.backend.domain.travelCourse.entity.*;
import com.seohaeng.backend.domain.user.entity.User;

public class TravelCourseConverter {

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

    public static TravelCourse toTravelCourse(
            TravelCourseRequestDTO.CreateTravelCourseDTO travelCourseDTO,
            User user
    ){
        return TravelCourse.builder()
                .travelCourseTitle(travelCourseDTO.getTravelCourseTitle())
                .travelCourseStartDate(travelCourseDTO.getStartDate())
                .travelCourseEndDate(travelCourseDTO.getEndDate())
                .user(user)
                .build();
    }

    public static TravelCourseSchedule toTravelCourseSchedule(
            TravelCourseRequestDTO.travelCourseScheduleDTO travelCourseScheduleDTO,
            Place place,
            TravelCourse travelCourse){
        return TravelCourseSchedule.builder()
                .day(travelCourseScheduleDTO.getDay())
                .orderInDay(travelCourseScheduleDTO.getOrderInday())
                .travelCourse(travelCourse)
                .place(place)
                .build();
    }

    public static TravelCourseRegion toTravelCourseRegion(Region region, TravelCourse travelCourse){
        return TravelCourseRegion.builder()
                .region(region)
                .travelCourse(travelCourse)
                .build();
    }
}