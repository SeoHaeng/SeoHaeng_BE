package com.seohaeng.backend.domain.common.converter;

import com.seohaeng.backend.domain.common.dto.BookSearchResponseDTO;
import com.seohaeng.backend.domain.common.dto.CommonResponseDTO;

import java.util.List;

public class CommonConverter {

    public static CommonResponseDTO.bookSearchResultDTO toCommonResponseDTO(BookSearchResponseDTO.BookItemDTO bookItem){
        return CommonResponseDTO.bookSearchResultDTO.builder()
                .title(bookItem.getTitle())
                .author(bookItem.getAuthor())
                .pubDate(bookItem.getPubdate())
                .bookImage(bookItem.getImage())
                .build();
    }

    public static CommonResponseDTO.bookSearchResultListDTO tobookSearchResultListDTO(
            List<CommonResponseDTO.bookSearchResultDTO> bookSearchResultDTOList){
        return CommonResponseDTO.bookSearchResultListDTO.builder()
                .bookSearchResults(bookSearchResultDTOList)
                .build();
    }
}