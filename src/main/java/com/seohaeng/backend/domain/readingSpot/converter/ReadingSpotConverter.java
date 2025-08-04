package com.seohaeng.backend.domain.readingSpot.converter;

import com.seohaeng.backend.domain.readingSpot.dto.ReadingSpotRequestDTO;
import com.seohaeng.backend.domain.readingSpot.dto.ReadingSpotResponseDTO;
import com.seohaeng.backend.domain.readingSpot.entity.ReadingSpot;
import com.seohaeng.backend.domain.readingSpot.entity.ReadingSpotComment;
import com.seohaeng.backend.domain.user.entity.User;
import org.springframework.data.domain.Page;

import java.util.List;

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

    public static ReadingSpotResponseDTO.GetReadingSpotResponseDTO toGetReadingSpotResponseDTO(
            ReadingSpot readingSpot, List<String> readingSpotImages) {
        return ReadingSpotResponseDTO.GetReadingSpotResponseDTO.builder()
                .readingSpotId(readingSpot.getId())
                .address(readingSpot.getAddress())
                .latitude(readingSpot.getLatitude())
                .longitude(readingSpot.getLongitude())
                .templateId(readingSpot.getTemplateId())
                .title(readingSpot.getTitle())
                .content(readingSpot.getContent())
                .bookTitle(readingSpot.getBookTitle())
                .bookAuthor(readingSpot.getBookAuthor())
                .bookImage(readingSpot.getBookImageUrl())
                .bookPubDate(readingSpot.getBookPubDate())
                .likes(readingSpot.getLikes())
                .scraps(readingSpot.getScraps())
                .opened(readingSpot.isOpened())
                .readingSpotImages(readingSpotImages)
                .build();
    }

    public static ReadingSpotComment toReadingSpotComment (
            ReadingSpotRequestDTO.ReadingSpotCommentCreateRequestDTO request, User user, ReadingSpot readingSpot) {
        return ReadingSpotComment.builder()
                .content(request.getContent())
                .user(user)
                .readingSpot(readingSpot)
                .build();
    }

    public static ReadingSpotResponseDTO.GetReadingSpotCommentResponseDTO toGetReadingSpotCommentResponseDTO(ReadingSpotComment readingSpotComment) {
        return ReadingSpotResponseDTO.GetReadingSpotCommentResponseDTO.builder()
                .commentId(readingSpotComment.getId())
                .commentContent(readingSpotComment.getContent())
                .createdAt(readingSpotComment.getCreatedAt().toLocalDate())
                .userId(readingSpotComment.getUser().getId())
                .build();
    }

    public static ReadingSpotResponseDTO.GetReadingSpotCommentListResponseDTO toGetReadingSpotCommentListResponseDTO(
            List<ReadingSpotResponseDTO.GetReadingSpotCommentResponseDTO> readingSpotCommentResponseDTOs, Page<ReadingSpotComment> readingSpotCommentPage) {
        return ReadingSpotResponseDTO.GetReadingSpotCommentListResponseDTO.builder()
                .listSize(readingSpotCommentResponseDTOs.size())
                .totalElements(readingSpotCommentPage.getTotalElements())
                .totalPage(readingSpotCommentPage.getTotalPages())
                .isFirst(readingSpotCommentPage.isFirst())
                .isLast(readingSpotCommentPage.isLast())
                .comments(readingSpotCommentResponseDTOs)
                .build();
    }
}