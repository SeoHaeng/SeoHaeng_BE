package com.seohaeng.backend.domain.bookChallenge.service;

import com.seohaeng.backend.domain.bookChallenge.converter.BookChallengeConverter;
import com.seohaeng.backend.domain.bookChallenge.dto.BookChallengeRequestDTO;
import com.seohaeng.backend.domain.bookChallenge.entity.BookChallengeProof;
import com.seohaeng.backend.domain.bookChallenge.entity.BookChallengeProofImage;
import com.seohaeng.backend.domain.bookChallenge.repository.BookChallengeProofImageRepository;
import com.seohaeng.backend.domain.bookChallenge.repository.BookChallengeProofRepository;
import com.seohaeng.backend.domain.place.entity.Place;
import com.seohaeng.backend.domain.place.entity.repository.PlaceRepository;
import com.seohaeng.backend.domain.user.entity.User;
import com.seohaeng.backend.domain.user.repository.UserRepository;
import com.seohaeng.backend.global.apiPayload.code.status.ErrorStatus;
import com.seohaeng.backend.global.apiPayload.exception.handler.PlaceHandler;
import com.seohaeng.backend.global.apiPayload.exception.handler.UserHandler;
import com.seohaeng.backend.global.aws.s3.AmazonS3Manager;
import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookChallengeCommandService {

    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;
    private final BookChallengeProofRepository bookChallengeProofRepository;
    private final BookChallengeProofImageRepository bookChallengeProofImageRepository;
    private final AmazonS3Manager amazonS3Manager;

    @Transactional
    public void createBookChallengeProof(
            BookChallengeRequestDTO.createBookChallengeProof request,
            Long userId,
            List<MultipartFile> images) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        Place place = placeRepository.findById(request.getBookStoreId()).
                orElseThrow(() -> new PlaceHandler(ErrorStatus.PLACE_NOT_FOUND));

        BookChallengeProof bookChallengeProof = BookChallengeConverter.toBookChallengeProof(request, user, place);
        bookChallengeProofRepository.save(bookChallengeProof);

        if(images != null && !images.isEmpty()){
            for (MultipartFile image : images) {
                try{
                    final String uuid = UUID.randomUUID().toString();
                    final String keyName = amazonS3Manager.generateBookChallengeProofKeyName(uuid);
                    final String imageUrl = amazonS3Manager.uploadFile(keyName, image);

                    BookChallengeProofImage bookChallengeProofImage = BookChallengeProofImage.builder()
                            .imageUrl(imageUrl)
                            .bookChallengeProof(bookChallengeProof)
                            .build();
                    bookChallengeProofImageRepository.save(bookChallengeProofImage);
                }catch(IOException e){
                    throw new RuntimeException("파일 업로드 오류입니다.");
                }
            }
        }
    }
}