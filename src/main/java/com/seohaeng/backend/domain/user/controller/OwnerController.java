package com.seohaeng.backend.domain.user.controller;

import com.seohaeng.backend.domain.bookChallenge.dto.BookChallengeRequestDTO;
import com.seohaeng.backend.domain.bookChallenge.service.BookChallengeCommandService;
import com.seohaeng.backend.global.apiPayload.ApiResponse;
import com.seohaeng.backend.global.security.handler.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/v1/owner")
@RequiredArgsConstructor
public class OwnerController {

    private final BookChallengeCommandService bookChallengeCommandService;

    @Operation(
            summary = "사장님 페이지 전용 - 북챌린지 생성 API",
            description = """
        사장님 전용 페이지에서 북챌린지를 생성하는 API입니다.
        - 사장님이 챌린지 참여자의 서행 어플 닉네임과, 선물받은 / 선물한 책 정보를 어플로 전달하여 정보를 저장합니다.
        - 해당 정보는 북챌린지 탭 상단에 표시되며, 북챌린지 게시글을 작성할때 사용됩니다.
        - 챌린저가 바로 이전 챌린지에서 북챌린지 인증 글을 아직 작성하지 않았다면 새롭게 참여가 불가능합니다.
    """
    )
    @PostMapping("/book-challenge")
    public ApiResponse<String> saveNewBookChallenge (
            @RequestBody BookChallengeRequestDTO.saveBookChallenge request,
            @AuthUser Long userId){
        bookChallengeCommandService.saveNewBookChallenge(userId, request);
        return ApiResponse.onSuccess("북챌린지 생성이 완료 되었습니다.");
    }
}