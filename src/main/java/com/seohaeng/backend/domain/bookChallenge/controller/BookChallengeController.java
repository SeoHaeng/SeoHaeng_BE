package com.seohaeng.backend.domain.bookChallenge.controller;

import com.seohaeng.backend.domain.bookChallenge.dto.BookChallengeRequestDTO;
import com.seohaeng.backend.domain.bookChallenge.dto.BookChallengeResponseDTO;
import com.seohaeng.backend.domain.bookChallenge.service.BookChallengeCommandService;
import com.seohaeng.backend.domain.bookChallenge.service.BookChallengeQueryService;
import com.seohaeng.backend.global.apiPayload.ApiResponse;
import com.seohaeng.backend.global.apiPayload.code.status.SuccessStatus;
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
            summary = "북챌린지 인증글 생성을 위한 참여 중인 북챌린지 정보 불러오기 API",
            description = """
        현재 사용자의 북챌린지 인증 글 작성을 위해, 현재 사용자가 진행 중인 북챌린지의 도서 정보를 불러오는 API입니다.
        - "북챌린지 인증 글 작성시" 도서 정보와 서점 이름을 불러오는데 사용됩니다.
        - "사용자가 현재 참여 중인 북 챌린지"의 선물받은 / 선물한 책 정보를 불러옵니다.
    """
    )
    @GetMapping("/inprogress-info")
    public ApiResponse<BookChallengeResponseDTO.saveBookChallenge> getInprogressBookChallengeInfo (
            @AuthUser Long userId){
        BookChallengeResponseDTO.saveBookChallenge result
                = bookChallengeQueryService.getInprogressBookChallengeInfo(userId);
        return ApiResponse.onSuccess(result);
    }

    @Operation(
            summary = "북챌린지 인증 게시글 생성 API",
            description = "사용자가 선택한 서점 ID, 선물 메시지, 도서 정보, 챌린지 내용, 챌린지 이미지 파일을 첨부하여 업로드합니다.")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<String> registerBookChallenge (
            @AuthUser Long userId,
            @RequestParam Long bookChallengeId,
            @RequestPart("request") BookChallengeRequestDTO.createBookChallengeProof request,
            @RequestPart(value = "images", required = false) List<MultipartFile> images){

        if (images != null && images.size() > 10) {
            throw new IllegalArgumentException("이미지는 최대 10장까지 업로드할 수 있습니다.");
        }
        bookChallengeCommandService.createBookChallengeProof(bookChallengeId,request,userId,images);
        return ApiResponse.onSuccess("북챌린지 인증이 등록되었습니다.");
    }

    @Operation(
            summary = "북챌린지 인증 게시글 개별 조회 API",
            description = "북챌린지 인증 게시글을 개별 상세 조회합니다. 조회하고자 하는 북챌린지 게시글의 ID를 넘겨주세요")
    @GetMapping("/{bookChallengeProofId}")
    public ApiResponse<BookChallengeResponseDTO.getBookChallenge> getBookChallengeProof (
            @AuthUser Long userId,
            @PathVariable Long bookChallengeProofId){
        BookChallengeResponseDTO.getBookChallenge result
                = bookChallengeQueryService.getBookChallenge(userId, bookChallengeProofId);
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
            @AuthUser Long userId,
            @RequestParam(name = "page", defaultValue = "1") @Min(1)Integer page,
            @RequestParam(name = "size", defaultValue = "10") @Min(1)Integer size,
            @RequestParam(name = "sort", defaultValue = "latest") String sort
    ){
        BookChallengeResponseDTO.getBookChallengeListDTO result = bookChallengeQueryService.getBookChallengeList(page, size, sort, userId);
        return ApiResponse.onSuccess(result);
    }

    @Operation(
            summary = "북챌린지 인증 게시글 삭제 API",
            description = "북챌린지 인증 게시글을 삭제합니다. 본인이 작성한 글만 삭제가 가능하며, 삭제하고자 하는 북챌린지 게시글의 ID를 넘겨주세요."
    )
    @DeleteMapping("/{bookChallengeProofId}")
    public ApiResponse<String> deleteChallengeProof (@AuthUser Long userId, @PathVariable Long bookChallengeProofId){
        bookChallengeCommandService.deleteBookChallengeProof(userId, bookChallengeProofId);
        return ApiResponse.onSuccess("북챌린지 인증 게시글 삭제가 왼료되었습니다.");
    }

    @Operation(
            summary = "북챌린지 인증 댓글 생성 API",
            description = "특정 북챌린지 인증 게시글에 댓글을 작성합니다. 댓글을 작성할 게시글 ID를 전달해주세요."
    )
    @PostMapping("/{bookChallengeProofId}/comments")
    public ApiResponse<String> registerBookChallengeComment(
            @AuthUser Long userId,
            @PathVariable Long bookChallengeProofId,
            @RequestBody BookChallengeRequestDTO.createBookChallengeProofComment request
    ) {
        bookChallengeCommandService.createBookChallengeProofComment(userId, bookChallengeProofId, request);
        return ApiResponse.onSuccess("북챌린지 댓글 작성이 완료되었습니다.");
    }

    @Operation(
            summary = "특정 북챌린지 인증 게시글 댓글 전체 조회 API",
            description = """
        북챌린지 인증 게시글의 댓글 목록을 페이징 처리하여 조회합니다. 기본으로 최신순 조회됩니다.
        Parameter:
        - `page`: 페이지 번호 (1부터 시작)
        - `size`: 한 페이지당 게시글 개수 (기본값: 20)
    """
    )
    @GetMapping("/{bookChallengeProofId}/comments")
    public ApiResponse<BookChallengeResponseDTO.getBookChallengeCommentListDTO> getBookChallengeProofComments (
            @PathVariable Long bookChallengeProofId,
            @RequestParam(name = "page", defaultValue = "1") @Min(1)Integer page,
            @RequestParam(name = "size", defaultValue = "20") @Min(1)Integer size
            ){
        BookChallengeResponseDTO.getBookChallengeCommentListDTO result =
                bookChallengeQueryService.getBookChallengeCommentList(page, size, bookChallengeProofId);
        return ApiResponse.onSuccess(result);
    }

    @Operation(
            summary = "북챌린지 인증 게시글 좋아요 토글 API",
            description = "특정 북챌린지 인증 게시글에 좋아요를 추가하거나 취소하고, 현재 좋아요 수를 반환합니다." +
                    "누르면 좋아요가 없으면 +1, 이미 눌러져 있으면 취소되어 좋아요 수가 -1 됩니다. 좋아요를 토글할 게시글 ID를 전달해주세요."
    )
    @PostMapping("/{bookChallengeProofId}/like")
    public ApiResponse<BookChallengeResponseDTO.getBookChallengeLikeInfoDTO> toggleLikeBookChallengeProof(@AuthUser Long userId, @PathVariable Long bookChallengeProofId) {
        BookChallengeResponseDTO.getBookChallengeLikeInfoDTO result =
                bookChallengeCommandService.toggleBookChallengeProofLike(userId, bookChallengeProofId);
        return ApiResponse.of(SuccessStatus.BOOK_CHALLENGE_PROOF_LIKE_TOGGLED,result);
    }
}