package com.seohaeng.backend.domain.bookChallenge.controller;

import com.seohaeng.backend.domain.bookChallenge.dto.BookChallengeRequestDTO;
import com.seohaeng.backend.domain.bookChallenge.service.BookChallengeCommandService;
import com.seohaeng.backend.global.apiPayload.ApiResponse;
import com.seohaeng.backend.global.aws.s3.AmazonS3Manager;
import com.seohaeng.backend.global.security.handler.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/book-challenge")

@RequiredArgsConstructor
public class BookChallengeController {

    private final BookChallengeCommandService bookChallengeCommandService;
    private final AmazonS3Manager amazonS3Manager;

    @Operation(
            summary = "북챌린지 인증 게시글 생성 API",
            description = "사용자가 선택한 서점 ID, 선물 메시지, 도서 정보, 챌린지 내용, 챌린지 이미지 파일을 첨부하여 업로드합니다.")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<String> registerBookChallenge (
            @AuthUser Long userId,
            @RequestPart("request") BookChallengeRequestDTO.createBookChallengeProof request,
            @RequestPart(value = "images", required = false) List<MultipartFile> images){

        if (images != null && images.size() > 10) {
            throw new IllegalArgumentException("이미지는 최대 10장까지 업로드할 수 있습니다.");
        }
        bookChallengeCommandService.createBookChallengeProof(request,userId,images);
        return ApiResponse.onSuccess("북챌린지 인증이 등록되었습니다.");
    }
}