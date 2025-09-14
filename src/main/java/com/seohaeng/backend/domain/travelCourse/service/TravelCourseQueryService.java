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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

        List<TravelCourseSchedule> scheduleList = travelCourseRepository.findWithCourseScheduleById(travelCourseId)
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

    public List<TravelCourseResponseDTO.GetTravelCourseListItemDTO> getPublicTravelCourseList(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<TravelCourse> travelCoursePage
                = travelCourseRepository.findAllByIsPublicTrueAndUserIdNotOrderByCreatedAtDesc(userId, pageable);

        return travelCoursePage.getContent().stream()
                .map(this::getTravelCourseListItem)
                .collect(Collectors.toList());
    }

    public TravelCourseResponseDTO.LastVisitDTO getLastVisit(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        LocalDate today = LocalDate.now();
        TravelCourse travelCourse = travelCourseRepository
                .findTopByUserAndTravelCourseStartDateLessThanEqualOrderByTravelCourseStartDateDesc(user, today)
                .orElse(null);

        if (travelCourse == null) {
            return TravelCourseConverter.toLastVisitDTO(user.getId(), null, null);
        }

        LocalDate lastVisitDate = travelCourse.getTravelCourseStartDate();
        long daysAgo = ChronoUnit.DAYS.between(lastVisitDate, today);

        return TravelCourseConverter.toLastVisitDTO(user.getId(), lastVisitDate, daysAgo);
    }

    private TravelCourseResponseDTO.GetTravelCourseListItemDTO getTravelCourseListItem(TravelCourse travelCourse) {
        long days = ChronoUnit.DAYS.between(
                travelCourse.getTravelCourseStartDate(),
                travelCourse.getTravelCourseEndDate());
        String duration = days + "박 " + (days + 1) + "일";

        String imageUrl = getFirstPlaceImageOrDefault(travelCourse);

        List<String> regionNames = travelCourse.getTravelCourseRegionList()
                .stream()
                .map(r -> r.getRegion().getRegionName())
                .collect(Collectors.toList());

        return TravelCourseConverter.toGetTravelCourseListItemDTO(travelCourse, imageUrl, duration, regionNames);
    }

    private String getFirstPlaceImageOrDefault(TravelCourse travelCourse) {
        return travelCourse.getTravelCourseScheduleList()
                .stream()
                .filter(schedule -> schedule.getDay().equals(travelCourse.getTravelCourseStartDate()))
                .sorted(Comparator.comparing(TravelCourseSchedule::getOrderInDay))
                .map(schedule -> schedule.getPlace())
                .filter(place -> !place.getPlaceImages().isEmpty())
                .map(place -> place.getPlaceImages().get(0).getImageUrl())
                .findFirst()
                .orElse("https://seohaeng-bucket.s3.ap-northeast-2.amazonaws.com/places/default2.png");
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