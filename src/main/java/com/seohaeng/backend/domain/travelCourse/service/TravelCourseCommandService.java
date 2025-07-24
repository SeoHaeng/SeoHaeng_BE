package com.seohaeng.backend.domain.travelCourse.service;

import com.seohaeng.backend.domain.place.entity.Place;
import com.seohaeng.backend.domain.place.entity.repository.PlaceRepository;
import com.seohaeng.backend.domain.travelCourse.converter.TravelCourseConverter;
import com.seohaeng.backend.domain.travelCourse.dto.travelCourseRequestDTO;
import com.seohaeng.backend.domain.travelCourse.entity.Region;
import com.seohaeng.backend.domain.travelCourse.entity.TravelCourse;
import com.seohaeng.backend.domain.travelCourse.entity.TravelCourseRegion;
import com.seohaeng.backend.domain.travelCourse.entity.TravelCourseSchedule;
import com.seohaeng.backend.domain.travelCourse.repository.RegionRepository;
import com.seohaeng.backend.domain.travelCourse.repository.TravelCourseRegionRepository;
import com.seohaeng.backend.domain.travelCourse.repository.TravelCourseRepository;
import com.seohaeng.backend.domain.travelCourse.repository.TravelCourseScheduleRepository;
import com.seohaeng.backend.domain.user.entity.User;
import com.seohaeng.backend.domain.user.repository.UserRepository;
import com.seohaeng.backend.global.apiPayload.code.status.ErrorStatus;
import com.seohaeng.backend.global.apiPayload.exception.handler.PlaceHandler;
import com.seohaeng.backend.global.apiPayload.exception.handler.RegionHandler;
import com.seohaeng.backend.global.apiPayload.exception.handler.UserHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TravelCourseCommandService {

    private final UserRepository userRepository;

    private final TravelCourseRepository travelCourseRepository;
    private final TravelCourseScheduleRepository travelCourseScheduleRepository;
    private final TravelCourseRegionRepository travelCourseRegionRepository;

    private final RegionRepository regionRepository;
    private final PlaceRepository placeRepository;

    @Transactional
    public Long createTravelCourse (Long userId, travelCourseRequestDTO.CreateTravelCourseDTO request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        TravelCourse travelCourse = TravelCourseConverter.toTravelCourse(request, user);
        travelCourseRepository.save(travelCourse);

        List<travelCourseRequestDTO.travelCourseScheduleDTO> travelCourseScheduleDTOList
                = request.getTravelCourseScheduleList();

        List<TravelCourseSchedule> schedules = new ArrayList<>();
        for (travelCourseRequestDTO.travelCourseScheduleDTO scheduleDTO : travelCourseScheduleDTOList) {
            Place travlePlace = placeRepository.findById(scheduleDTO.getPlaceId())
                    .orElseThrow(() -> new PlaceHandler(ErrorStatus.PLACE_NOT_FOUND));
            schedules.add(TravelCourseConverter.toTravelCourseSchedule(scheduleDTO, travlePlace, travelCourse));
        }
        travelCourseScheduleRepository.saveAll(schedules);

        List<TravelCourseRegion> regions = new ArrayList<>();
        for (Long regionId : request.getRegionIdList()) {
            Region travelRegion = regionRepository.findById(regionId)
                    .orElseThrow(() -> new RegionHandler(ErrorStatus.REGION_NOT_FOUND));
            regions.add(TravelCourseConverter.toTravelCourseRegion(travelRegion, travelCourse));
        }
        travelCourseRegionRepository.saveAll(regions);

        return travelCourse.getId();
    }
}