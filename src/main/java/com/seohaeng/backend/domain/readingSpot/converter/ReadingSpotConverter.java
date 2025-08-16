package com.seohaeng.backend.domain.readingSpot.converter;

import com.seohaeng.backend.domain.readingSpot.dto.ReadingSpotRequestDTO;
import com.seohaeng.backend.domain.readingSpot.dto.ReadingSpotResponseDTO;
import com.seohaeng.backend.domain.readingSpot.entity.ReadingSpot;
import com.seohaeng.backend.domain.readingSpot.entity.ReadingSpotComment;
import com.seohaeng.backend.domain.readingSpot.entity.ReadingSpotScrap;
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
                .opened(request.getOpened())
                .build();
    }

    public static ReadingSpotResponseDTO.GetReadingSpotResponseDTO toGetReadingSpotResponseDTO(
            ReadingSpot readingSpot, List<String> readingSpotImages, boolean isLiked, boolean isScraped) {
        return ReadingSpotResponseDTO.GetReadingSpotResponseDTO.builder()
                .userId(readingSpot.getUser().getId())
                .userNickname(readingSpot.getUser().getNickname())
                .userProfilImage(readingSpot.getUser().getImageUrl())
                .readingSpotId(readingSpot.getId())
                .address(readingSpot.getAddress())
                .latitude(readingSpot.getLatitude())
                .longitude(readingSpot.getLongitude())
                .createdAt(readingSpot.getCreatedAt().toLocalDate())
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
                .isLiked(isLiked)
                .isScraped(isScraped)
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

    public static ReadingSpotResponseDTO.GetReadingSpotItemResponseDTO toGetReadingSpotItemResponseDTO(
            ReadingSpot readingSpot, String mainImage) {
        return ReadingSpotResponseDTO.GetReadingSpotItemResponseDTO.builder()
                .title(readingSpot.getTitle())
                .templateId(readingSpot.getTemplateId())
                .imageUrl(mainImage)
                .readingSpotId(readingSpot.getId())
                .createdAt(readingSpot.getCreatedAt().toLocalDate())
                .address(readingSpot.getAddress())
                .latitude(readingSpot.getLatitude())
                .longitude(readingSpot.getLongitude())
                .build();
    }

    public static ReadingSpotResponseDTO.GetReadingSpotItemListResponseDTO toGetReadingSpotItemListResponseDTO(
            List<ReadingSpotResponseDTO.GetReadingSpotItemResponseDTO> readingSpotItemResponseDTOs, Page<ReadingSpot> readingSpotPage) {
        return ReadingSpotResponseDTO.GetReadingSpotItemListResponseDTO.builder()
                .listSize(readingSpotItemResponseDTOs.size())
                .totalElements(readingSpotPage.getTotalElements())
                .totalPage(readingSpotPage.getTotalPages())
                .isFirst(readingSpotPage.isFirst())
                .isLast(readingSpotPage.isLast())
                .scrapList(readingSpotItemResponseDTOs)
                .build();
    }

    public static ReadingSpotResponseDTO.GetReadingSpotItemListResponseDTO toGetReadingSpotScrapItemListResponseDTO(
            List<ReadingSpotResponseDTO.GetReadingSpotItemResponseDTO> readingSpotItemResponseDTOs, Page<ReadingSpotScrap> readingSpotScrapPage) {
        return ReadingSpotResponseDTO.GetReadingSpotItemListResponseDTO.builder()
                .listSize(readingSpotItemResponseDTOs.size())
                .totalElements(readingSpotScrapPage.getTotalElements())
                .totalPage(readingSpotScrapPage.getTotalPages())
                .isFirst(readingSpotScrapPage.isFirst())
                .isLast(readingSpotScrapPage.isLast())
                .scrapList(readingSpotItemResponseDTOs)
                .build();
    }

    public static ReadingSpotResponseDTO.GetReadingSpotDetailListResponseDTO toGetReadingSpotDetailListResponseDTO(
            List<ReadingSpotResponseDTO.GetReadingSpotResponseDTO> readingSpotResponseDTOs, Page<?> page) {
        return ReadingSpotResponseDTO.GetReadingSpotDetailListResponseDTO.builder()
                .listSize(readingSpotResponseDTOs.size())
                .totalElements(page.getTotalElements())
                .totalPage(page.getTotalPages())
                .isFirst(page.isFirst())
                .isLast(page.isLast())
                .readingSpotList(readingSpotResponseDTOs)
                .build();
    }
}