package com.seohaeng.backend.domain.bookChallenge.controller;

import com.seohaeng.backend.domain.bookChallenge.dto.BookChallengeRequestDTO;
import com.seohaeng.backend.domain.bookChallenge.dto.BookChallengeResponseDTO;
import com.seohaeng.backend.domain.bookChallenge.service.BookChallengeCommandService;
import com.seohaeng.backend.domain.bookChallenge.service.BookChallengeQueryService;
import com.seohaeng.backend.global.apiPayload.ApiResponse;
import com.seohaeng.backend.global.security.handler.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/v1/book-challenges")
@RequiredArgsConstructor
public class BookChallengeController {

    private final BookChallengeCommandService bookChallengeCommandService;
    private final BookChallengeQueryService bookChallengeQueryService;

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

    @Operation(
            summary = "북챌린지 인증 게시글 개별 조회 API",
            description = "북챌린지 인증 게시글을 개별 상세 조회합니다. 조회하고자 하는 북챌린지 게시글의 ID를 넘겨주세요")
    @GetMapping("/{bookChallengeProofId}")
    public ApiResponse<BookChallengeResponseDTO.getBookChallenge> getBookChallengeProof (@PathVariable Long bookChallengeProofId){
        BookChallengeResponseDTO.getBookChallenge result = bookChallengeQueryService.getBookChallenge(bookChallengeProofId);
        return ApiResponse.onSuccess(result);
    }

    @Operation(
            summary = "북챌린지 인증 게시글 전체 조회 API",
            description = """
        북챌린지 인증 게시글 목록을 페이징 처리하여 조회합니다.
        Parameter:
        - `page`: 페이지 번호 (1부터 시작)
        - `size`: 한 페이지당 게시글 개수 (기본값: 10)
        - `sort`: 정렬 기준 (`latest`: 최신순, `popular`: 인기순) 정확히 입력해야합니다.
    """
    )
    @GetMapping
    public ApiResponse<BookChallengeResponseDTO.getBookChallengeListDTO> getBookChallengeProofs (
            @RequestParam(name = "page", defaultValue = "1") @Min(1)Integer page,
            @RequestParam(name = "size", defaultValue = "10") @Min(1)Integer size,
            @RequestParam(name = "sort", defaultValue = "latest") String sort
    ){
        BookChallengeResponseDTO.getBookChallengeListDTO result = bookChallengeQueryService.getBookChallengeList(page, size, sort);
        return ApiResponse.onSuccess(result);
    }
}