package com.seohaeng.backend.domain.readingSpot.service;

import com.seohaeng.backend.domain.readingSpot.repository.*;
import com.seohaeng.backend.domain.readingSpot.converter.ReadingSpotConverter;
import com.seohaeng.backend.domain.readingSpot.dto.ReadingSpotResponseDTO;
import com.seohaeng.backend.domain.readingSpot.entity.ReadingSpot;
import com.seohaeng.backend.domain.readingSpot.entity.ReadingSpotComment;
import com.seohaeng.backend.domain.readingSpot.entity.ReadingSpotImage;
import com.seohaeng.backend.domain.readingSpot.entity.ReadingSpotScrap;
import com.seohaeng.backend.domain.user.entity.User;
import com.seohaeng.backend.domain.user.repository.UserRepository;
import com.seohaeng.backend.global.apiPayload.code.status.ErrorStatus;
import com.seohaeng.backend.global.apiPayload.exception.handler.ReadingSpotHandler;
import com.seohaeng.backend.global.apiPayload.exception.handler.UserHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.seohaeng.backend.domain.readingSpot.converter.ReadingSpotConverter.toGetReadingSpotItemListResponseDTO;
import static com.seohaeng.backend.domain.readingSpot.converter.ReadingSpotConverter.toGetReadingSpotScrapItemListResponseDTO;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReadingSpotQueryService {

    private final UserRepository userRepository;
    private final ReadingSpotRepository readingSpotRepository;
    private final ReadingSpotImageRepository readingSpotImageRepository;
    private final ReadingSpotCommentRepository readingSpotCommentRepository;
    private final ReadingSpotScrapRepository readingSpotScrapRepository;

    public ReadingSpotResponseDTO.GetReadingSpotResponseDTO getReadingSpot(Long readingSpotId) {

        ReadingSpot readingSpot = readingSpotRepository.findWithReadingSpotImagesById(readingSpotId)
                .orElseThrow(() -> new ReadingSpotHandler(ErrorStatus.READING_SPOT_NOT_FOUND));
        List<String> imageList = readingSpot.getReadingSpotImageList()
                .stream().map(ReadingSpotImage::getImageUrl).collect(Collectors.toList());

        return ReadingSpotConverter.toGetReadingSpotResponseDTO(readingSpot, imageList);
    }

    public ReadingSpotResponseDTO.GetReadingSpotCommentListResponseDTO getReadingSpotCommentList(
            Long readingSpotId, Integer page, Integer size) {

        ReadingSpot readingSpot = readingSpotRepository.findWithReadingSpotImagesById(readingSpotId)
                .orElseThrow(() -> new ReadingSpotHandler(ErrorStatus.READING_SPOT_NOT_FOUND));

        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<ReadingSpotComment> readingSpotCommentPage = readingSpotCommentRepository.findAllByReadingSpot(readingSpot, pageRequest);

        List<ReadingSpotResponseDTO.GetReadingSpotCommentResponseDTO> result =
                readingSpotCommentPage.getContent().stream()
                        .map(ReadingSpotConverter::toGetReadingSpotCommentResponseDTO)
                        .collect(Collectors.toList());
        return ReadingSpotConverter.toGetReadingSpotCommentListResponseDTO(result, readingSpotCommentPage);
    }

    public ReadingSpotResponseDTO.GetReadingSpotItemListResponseDTO getMyScrapReadingSpotListResponseDTO(Long userId, Integer page, Integer size) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<ReadingSpotScrap> readingSpotScrapPage = readingSpotScrapRepository.findAllByUser(user, pageRequest);
        List<ReadingSpotScrap> readingSpotScrapList = readingSpotScrapPage.getContent();

        List<ReadingSpotResponseDTO.GetReadingSpotItemResponseDTO> readingSpotItemResponseDTOS = new ArrayList<>();

        for(ReadingSpotScrap readingSpotScrap : readingSpotScrapList) {
            ReadingSpot readingSpot = readingSpotScrap.getReadingSpot();
            String imageUrl = readingSpotImageRepository.findByReadingSpotAndIsMainTrue(readingSpot)
                    .map(ReadingSpotImage::getImageUrl)
                    .orElse("");
            ReadingSpotResponseDTO.GetReadingSpotItemResponseDTO dto = ReadingSpotConverter.toGetReadingSpotItemResponseDTO
                    (readingSpot, imageUrl);
            readingSpotItemResponseDTOS.add(dto);
        }
        return toGetReadingSpotScrapItemListResponseDTO(readingSpotItemResponseDTOS,readingSpotScrapPage);
    }

    public ReadingSpotResponseDTO.GetReadingSpotItemListResponseDTO getMyReadingSpotListResponseDTO(Long userId, Integer page, Integer size) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<ReadingSpot> readingSpotPage = readingSpotRepository.findAllByUser(user, pageRequest);
        List<ReadingSpot> readingSpotList = readingSpotPage.getContent();

        List<ReadingSpotResponseDTO.GetReadingSpotItemResponseDTO> readingSpotItemResponseDTOS = new ArrayList<>();

        for(ReadingSpot readingSpot : readingSpotList) {
            String imageUrl = readingSpotImageRepository.findByReadingSpotAndIsMainTrue(readingSpot)
                    .map(ReadingSpotImage::getImageUrl)
                    .orElse("");
            ReadingSpotResponseDTO.GetReadingSpotItemResponseDTO dto = ReadingSpotConverter.toGetReadingSpotItemResponseDTO
                    (readingSpot, imageUrl);
            readingSpotItemResponseDTOS.add(dto);
        }
        return toGetReadingSpotItemListResponseDTO(readingSpotItemResponseDTOS,readingSpotPage);
    }
}
