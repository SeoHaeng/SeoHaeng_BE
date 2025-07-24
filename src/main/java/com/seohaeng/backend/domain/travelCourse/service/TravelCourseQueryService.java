package com.seohaeng.backend.domain.travelCourse.service;

import com.seohaeng.backend.domain.travelCourse.converter.TravelCourseConverter;
import com.seohaeng.backend.domain.travelCourse.dto.TravelCourseResponseDTO;
import com.seohaeng.backend.domain.travelCourse.entity.TravelCourse;
import com.seohaeng.backend.domain.travelCourse.entity.TravelCourseSchedule;
import com.seohaeng.backend.domain.travelCourse.repository.TravelCourseRepository;
import com.seohaeng.backend.domain.user.entity.User;
import com.seohaeng.backend.domain.user.repository.UserRepository;
import com.seohaeng.backend.global.apiPayload.code.status.ErrorStatus;
import com.seohaeng.backend.global.apiPayload.exception.handler.TravelCourseHandler;
import com.seohaeng.backend.global.apiPayload.exception.handler.UserHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TravelCourseQueryService {

    private final TravelCourseRepository travelCourseRepository;
    private final UserRepository userRepository;

    public TravelCourseResponseDTO.GetTravelCourseResponseDTO getTravelCourse(Long travelCourseId) {
        TravelCourse travelCourse = travelCourseRepository.findWithRegionById(travelCourseId)
                .orElseThrow(() -> new TravelCourseHandler(ErrorStatus.TRAVEL_COURSE_NOT_FOUND));

        Long userId = travelCourse.getUser().getId();

        List<TravelCourseSchedule> scheduleList = travelCourseRepository.findWithCourseById(travelCourseId)
                .map(TravelCourse::getTravelCourseScheduleList)
                .orElse(Collections.emptyList());

        List<String> regionNames = travelCourse.getTravelCourseRegionList()
                .stream()
                .map(r -> r.getRegion().getRegionName())
                .collect(Collectors.toList());

        List<TravelCourseResponseDTO.GetTravelCourseScheduleResponseDayDTO> scheduleDays =
                groupAndConvertSchedules(scheduleList);

        return TravelCourseConverter.toGetTravelCourseResponseDTO(
                userId,
                travelCourse,
                regionNames,
                scheduleDays
        );
    }

    public List<TravelCourseResponseDTO.GetTravelCourseListItemDTO> getTravelCourseList(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        List<TravelCourse> travelCourses = travelCourseRepository.findAllByUser(user);

        return travelCourses.stream()
                .map(this::getTravelCourseListItem)
                .collect(Collectors.toList());
    }

    private TravelCourseResponseDTO.GetTravelCourseListItemDTO getTravelCourseListItem(TravelCourse travelCourse) {
        long days = ChronoUnit.DAYS.between(
                travelCourse.getTravelCourseStartDate(),
                travelCourse.getTravelCourseEndDate());
        String duration = days + "박 " + (days + 1) + "일";

        // TODO: 외부 이미지 API 연동 시 교체 필요 (현재는 임시 이미지 URL 하드코딩)
        String imageUrl = "https://example";

        List<String> regionNames = travelCourse.getTravelCourseRegionList()
                .stream()
                .map(r -> r.getRegion().getRegionName())
                .collect(Collectors.toList());

        return TravelCourseConverter.toGetTravelCourseListItemDTO(travelCourse, imageUrl, duration, regionNames);
    }

    private List<TravelCourseResponseDTO.GetTravelCourseScheduleResponseDayDTO> groupAndConvertSchedules(
            List<TravelCourseSchedule> schedules) {

        Map<LocalDate, List<TravelCourseSchedule>> groupedByDay = schedules.stream()
                .sorted(Comparator.comparing(TravelCourseSchedule::getDay))
                .collect(Collectors.groupingBy(
                        TravelCourseSchedule::getDay,
                        LinkedHashMap::new,
                        Collectors.toList()
                ));

        AtomicLong dayCounter = new AtomicLong(1);

        return groupedByDay.entrySet().stream()
                .map(entry -> {
                    LocalDate date = entry.getKey();
                    List<TravelCourseSchedule> dailySchedules = entry.getValue();
                    dailySchedules.sort(Comparator.comparing(TravelCourseSchedule::getOrderInDay));

                    List<TravelCourseResponseDTO.GetTravelCourseScheduleResponseDTO> scheduleDTOs = dailySchedules.stream()
                            .map(s -> TravelCourseConverter.toGetTravelCourseScheduleResponseDTO(
                                    s.getOrderInDay(), s.getPlace().getId()))
                            .collect(Collectors.toList());

                    return TravelCourseConverter.toGetTravelCourseScheduleResponseDayDTO(
                            dayCounter.getAndIncrement(),
                            date,
                            scheduleDTOs
                    );
                })
                .collect(Collectors.toList());
    }
}