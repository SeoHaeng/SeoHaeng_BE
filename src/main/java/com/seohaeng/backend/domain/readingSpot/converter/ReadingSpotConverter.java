package com.seohaeng.backend.domain.readingSpot.converter;

import com.seohaeng.backend.domain.readingSpot.dto.ReadingSpotRequestDTO;
import com.seohaeng.backend.domain.readingSpot.entity.ReadingSpot;
import com.seohaeng.backend.domain.user.entity.User;

public class ReadingSpotConverter {

    public static ReadingSpot toReadingSpot (
            ReadingSpotRequestDTO.ReadingSpotCreateRequestDTO request,
            User user) {
        return ReadingSpot.builder()
                .user(user)
                .address(request.getAddress())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .content(request.getContent())
                .title(request.getTitle())
                .templateId(request.getTemplateId())
                .bookTitle(request.getBookTitle())
                .bookAuthor(request.getBookAuthor())
                .bookPubDate(request.getBookPubDate())
                .bookImageUrl(request.getBookImage())
                .opened(request.isOpened())
                .build();
    }
}
