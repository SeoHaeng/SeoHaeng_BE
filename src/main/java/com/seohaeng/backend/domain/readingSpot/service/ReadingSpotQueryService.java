package com.seohaeng.backend.domain.readingSpot.service;

import com.seohaeng.backend.domain.readingSpot.ReadingSpotRepository.ReadingSpotRepository;
import com.seohaeng.backend.domain.readingSpot.converter.ReadingSpotConverter;
import com.seohaeng.backend.domain.readingSpot.dto.ReadingSpotResponseDTO;
import com.seohaeng.backend.domain.readingSpot.entity.ReadingSpot;
import com.seohaeng.backend.domain.readingSpot.entity.ReadingSpotImage;
import com.seohaeng.backend.global.apiPayload.code.status.ErrorStatus;
import com.seohaeng.backend.global.apiPayload.exception.handler.ReadingSpotHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReadingSpotQueryService {

    private final ReadingSpotRepository readingSpotRepository;

    public ReadingSpotResponseDTO.GetReadingSpotResponseDTO getReadingSpot(Long readingSpotId) {

        ReadingSpot readingSpot = readingSpotRepository.findWithReadingSpotImagesById(readingSpotId)
                .orElseThrow(() -> new ReadingSpotHandler(ErrorStatus.READING_SPOT_NOT_FOUND));

        List<String> imageList = readingSpot.getReadingSpotImageList()
                .stream().map(ReadingSpotImage::getImageUrl).collect(Collectors.toList());

        return ReadingSpotConverter.toGetReadingSpotResponseDTO(readingSpot, imageList);
    }
}
