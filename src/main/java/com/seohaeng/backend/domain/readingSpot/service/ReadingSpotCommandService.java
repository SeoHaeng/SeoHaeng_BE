package com.seohaeng.backend.domain.readingSpot.service;

import com.seohaeng.backend.domain.readingSpot.ReadingSpotRepository.ReadingSpotImageRepository;
import com.seohaeng.backend.domain.readingSpot.ReadingSpotRepository.ReadingSpotRepository;
import com.seohaeng.backend.domain.readingSpot.converter.ReadingSpotConverter;
import com.seohaeng.backend.domain.readingSpot.dto.ReadingSpotRequestDTO;
import com.seohaeng.backend.domain.readingSpot.dto.ReadingSpotResponseDTO;
import com.seohaeng.backend.domain.readingSpot.entity.ReadingSpot;
import com.seohaeng.backend.domain.readingSpot.entity.ReadingSpotImage;
import com.seohaeng.backend.domain.user.entity.User;
import com.seohaeng.backend.domain.user.repository.UserRepository;
import com.seohaeng.backend.global.apiPayload.code.status.ErrorStatus;
import com.seohaeng.backend.global.apiPayload.exception.handler.UserHandler;
import com.seohaeng.backend.global.aws.s3.AmazonS3Manager;
import com.seohaeng.backend.global.security.handler.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReadingSpotCommandService {

    private final UserRepository userRepository;
    private final AmazonS3Manager amazonS3Manager;
    private final ReadingSpotRepository readingSpotRepository;
    private final ReadingSpotImageRepository readingSpotImageRepository;

    @Transactional
    public ReadingSpotResponseDTO.CreateReadingSpotResponseDTO createReadingSpot(
            @AuthUser Long userId, List<MultipartFile> images,
            ReadingSpotRequestDTO.ReadingSpotCreateRequestDTO request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        ReadingSpot newReadingSpot = ReadingSpotConverter.toReadingSpot(request,user);
        readingSpotRepository.save(newReadingSpot);

        List<ReadingSpotImage> readingSpotImageList = new ArrayList<>();
        if(images != null && !images.isEmpty()){
            for (MultipartFile image : images) {
                final String uuid = UUID.randomUUID().toString();
                final String keyName = amazonS3Manager.generateReadingSpotKeyName(uuid);
                final String imageUrl = amazonS3Manager.uploadFile(keyName, image);

                ReadingSpotImage newReadingSpotImage = ReadingSpotImage.builder()
                        .imageUrl(imageUrl)
                        .readingSpot(newReadingSpot)
                        .build();
                readingSpotImageList.add(newReadingSpotImage);
            }
            readingSpotImageRepository.saveAll(readingSpotImageList);
        }
        return new ReadingSpotResponseDTO.CreateReadingSpotResponseDTO(newReadingSpot.getId());
    }
}
