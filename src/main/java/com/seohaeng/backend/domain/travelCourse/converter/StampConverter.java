package com.seohaeng.backend.domain.travelCourse.converter;

import com.seohaeng.backend.domain.travelCourse.dto.StampResponseDTO;
import com.seohaeng.backend.domain.user.entity.User;

public class StampConverter {

    public static StampResponseDTO.GetMyStampResponseDTO toGetMyStampResponseDTO(
            User user, Integer total, StampResponseDTO.GetMyStampListDTO getMyStampListDTO) {
        return StampResponseDTO.GetMyStampResponseDTO.builder()
                .userId(user.getId())
                .totalStampCount(total)
                .stampList(getMyStampListDTO)
                .build();
    }
}