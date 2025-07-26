package com.seohaeng.backend.domain.travelCourse.service;

import com.seohaeng.backend.domain.travelCourse.converter.StampConverter;
import com.seohaeng.backend.domain.travelCourse.dto.StampResponseDTO;
import com.seohaeng.backend.domain.travelCourse.entity.Stamp;
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

        List<Stamp> stampList = user.getStampList();
        for (Stamp stamp : stampList) {
            String regionName = stamp.getRegion().getRegionName();

            if ("춘천".equals(regionName)) stampListDTO.setChuncheon(stamp.getStampedDate());
            else if ("원주".equals(regionName)) stampListDTO.setWonju(stamp.getStampedDate());
            else if ("강릉".equals(regionName)) stampListDTO.setGangneung(stamp.getStampedDate());
            else if ("동해".equals(regionName)) stampListDTO.setDonghae(stamp.getStampedDate());
            else if ("태백".equals(regionName)) stampListDTO.setTaebaek(stamp.getStampedDate());
            else if ("속초".equals(regionName)) stampListDTO.setSokcho(stamp.getStampedDate());
            else if ("삼척".equals(regionName)) stampListDTO.setSamcheok(stamp.getStampedDate());
            else if ("홍천".equals(regionName)) stampListDTO.setHongcheon(stamp.getStampedDate());
            else if ("횡성".equals(regionName)) stampListDTO.setHoengseong(stamp.getStampedDate());
            else if ("영월".equals(regionName)) stampListDTO.setYeongwol(stamp.getStampedDate());
            else if ("평창".equals(regionName)) stampListDTO.setPyeongchang(stamp.getStampedDate());
            else if ("정선".equals(regionName)) stampListDTO.setJeongseon(stamp.getStampedDate());
            else if ("철원".equals(regionName)) stampListDTO.setCheorwon(stamp.getStampedDate());
            else if ("화천".equals(regionName)) stampListDTO.setHwacheon(stamp.getStampedDate());
            else if ("양구".equals(regionName)) stampListDTO.setYanggu(stamp.getStampedDate());
            else if ("인제".equals(regionName)) stampListDTO.setInje(stamp.getStampedDate());
            else if ("고성".equals(regionName)) stampListDTO.setGoseong(stamp.getStampedDate());
            else if ("양양".equals(regionName)) stampListDTO.setYangyang(stamp.getStampedDate());
        }
        return StampConverter.toGetMyStampResponseDTO(user, stampList.size(), stampListDTO);
    }
}