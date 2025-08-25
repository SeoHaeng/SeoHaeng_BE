package com.seohaeng.backend.domain.travelCourse.service;

import com.seohaeng.backend.domain.travelCourse.converter.StampConverter;
import com.seohaeng.backend.domain.travelCourse.dto.StampResponseDTO;
import com.seohaeng.backend.domain.travelCourse.entity.Region;
import com.seohaeng.backend.domain.travelCourse.entity.Stamp;
import com.seohaeng.backend.domain.travelCourse.repository.RegionRepository;
import com.seohaeng.backend.domain.user.entity.User;
import com.seohaeng.backend.domain.user.repository.UserRepository;
import com.seohaeng.backend.global.apiPayload.code.status.ErrorStatus;
import com.seohaeng.backend.global.apiPayload.exception.handler.UserHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StampQueryService {

    private final UserRepository userRepository;

    public StampResponseDTO.GetMyStampResponseDTO getMyStamp(Long userId) {
        User user = userRepository.findWithStampListById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        StampResponseDTO.GetMyStampListDTO stampListDTO = new StampResponseDTO.GetMyStampListDTO();
        StampResponseDTO.RegionImagesDTO regionImagesDTO = new StampResponseDTO.RegionImagesDTO();

        List<Stamp> stampList = user.getStampList();
        for (Stamp stamp : stampList) {
            String regionName = stamp.getRegion().getRegionName();
            String regionImage = stamp.getRegion().getRegionImage();

            switch (regionName) {
                case "춘천" -> {
                    stampListDTO.setChuncheon(stamp.getStampedDate());
                    regionImagesDTO.setChuncheon(regionImage);
                }
                case "원주" -> {
                    stampListDTO.setWonju(stamp.getStampedDate());
                    regionImagesDTO.setWonju(regionImage);
                }
                case "강릉" -> {
                    stampListDTO.setGangneung(stamp.getStampedDate());
                    regionImagesDTO.setGangneung(regionImage);
                }
                case "동해" -> {
                    stampListDTO.setDonghae(stamp.getStampedDate());
                    regionImagesDTO.setDonghae(regionImage);
                }
                case "태백" -> {
                    stampListDTO.setTaebaek(stamp.getStampedDate());
                    regionImagesDTO.setTaebaek(regionImage);
                }
                case "속초" -> {
                    stampListDTO.setSokcho(stamp.getStampedDate());
                    regionImagesDTO.setSokcho(regionImage);
                }
                case "삼척" -> {
                    stampListDTO.setSamcheok(stamp.getStampedDate());
                    regionImagesDTO.setSamcheok(regionImage);
                }
                case "홍천" -> {
                    stampListDTO.setHongcheon(stamp.getStampedDate());
                    regionImagesDTO.setHongcheon(regionImage);
                }
                case "횡성" -> {
                    stampListDTO.setHoengseong(stamp.getStampedDate());
                    regionImagesDTO.setHoengseong(regionImage);
                }
                case "영월" -> {
                    stampListDTO.setYeongwol(stamp.getStampedDate());
                    regionImagesDTO.setYeongwol(regionImage);
                }
                case "평창" -> {
                    stampListDTO.setPyeongchang(stamp.getStampedDate());
                    regionImagesDTO.setPyeongchang(regionImage);
                }
                case "정선" -> {
                    stampListDTO.setJeongseon(stamp.getStampedDate());
                    regionImagesDTO.setJeongseon(regionImage);
                }
                case "철원" -> {
                    stampListDTO.setCheorwon(stamp.getStampedDate());
                    regionImagesDTO.setCheorwon(regionImage);
                }
                case "화천" -> {
                    stampListDTO.setHwacheon(stamp.getStampedDate());
                    regionImagesDTO.setHwacheon(regionImage);
                }
                case "양구" -> {
                    stampListDTO.setYanggu(stamp.getStampedDate());
                    regionImagesDTO.setYanggu(regionImage);
                }
                case "인제" -> {
                    stampListDTO.setInje(stamp.getStampedDate());
                    regionImagesDTO.setInje(regionImage);
                }
                case "고성" -> {
                    stampListDTO.setGoseong(stamp.getStampedDate());
                    regionImagesDTO.setGoseong(regionImage);
                }
                case "양양" -> {
                    stampListDTO.setYangyang(stamp.getStampedDate());
                    regionImagesDTO.setYangyang(regionImage);
                }
            }
        }

        return StampConverter.toGetMyStampResponseDTO(user, stampList.size(), stampListDTO, regionImagesDTO);
    }
}