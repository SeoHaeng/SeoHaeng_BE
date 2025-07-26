package com.seohaeng.backend.domain.travelCourse.converter;

import com.seohaeng.backend.domain.place.entity.Place;
import com.seohaeng.backend.domain.travelCourse.dto.TravelCourseRequestDTO;
import com.seohaeng.backend.domain.travelCourse.dto.TravelCourseResponseDTO;
import com.seohaeng.backend.domain.travelCourse.entity.*;
import com.seohaeng.backend.domain.user.entity.User;

import java.time.LocalDate;
import java.util.List;

public class TravelCourseConverter {

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

    public static TravelCourseResponseDTO.GetTravelCourseResponseDTO toGetTravelCourseResponseDTO(
            Long memberId,
            TravelCourse travelCourse,
            List<String> travelRegions,
            List<TravelCourseResponseDTO.GetTravelCourseScheduleResponseDayDTO> schedules
    ){
        return TravelCourseResponseDTO.GetTravelCourseResponseDTO.builder()
                .travelCourseId(travelCourse.getId())
                .memberId(memberId)
                .courseTitle(travelCourse.getTravelCourseTitle())
                .travelRegions(travelRegions)
                .startDate(travelCourse.getTravelCourseStartDate())
                .endDate(travelCourse.getTravelCourseEndDate())
                .schedules(schedules)
                .build();
    }

    public static TravelCourseResponseDTO.GetTravelCourseScheduleResponseDTO toGetTravelCourseScheduleResponseDTO(
            Integer orderInday,
            Long placeId
    ){
        return TravelCourseResponseDTO.GetTravelCourseScheduleResponseDTO.builder()
                .orderInday(orderInday)
                .placeId(placeId)
                .build();
    }

    public static TravelCourseResponseDTO.GetTravelCourseScheduleResponseDayDTO toGetTravelCourseScheduleResponseDayDTO(
            Long day,
            LocalDate date,
            List<TravelCourseResponseDTO.GetTravelCourseScheduleResponseDTO> schedules
    ){
        return TravelCourseResponseDTO.GetTravelCourseScheduleResponseDayDTO.builder()
                .day(day)
                .date(date)
                .schedules(schedules)
                .build();
    }

    public static TravelCourseResponseDTO.GetTravelCourseListItemDTO toGetTravelCourseListItemDTO(
            TravelCourse travelCourse, String image, String duration, List<String> travelRegions){
        return TravelCourseResponseDTO.GetTravelCourseListItemDTO.builder()
                .title(travelCourse.getTravelCourseTitle())
                .travelCourseId(travelCourse.getId())
                .memberId(travelCourse.getUser().getId())
                .imageUrl(image)
                .startDate(travelCourse.getTravelCourseStartDate())
                .endDate(travelCourse.getTravelCourseEndDate())
                .duration(duration)
                .travelRegions(travelRegions)
                .build();
    }

    public static TravelCourseResponseDTO.LastVisitDTO toLastVisitDTO(Long userId, LocalDate date, Long daysAgo){
        return TravelCourseResponseDTO.LastVisitDTO.builder()
                .userId(userId)
                .lastVisitDate(date)
                .daysAgo(daysAgo)
                .build();
    }
}