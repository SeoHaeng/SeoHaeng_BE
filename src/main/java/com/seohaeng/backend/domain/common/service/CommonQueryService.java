package com.seohaeng.backend.domain.common.service;

import com.seohaeng.backend.domain.common.converter.CommonConverter;
import com.seohaeng.backend.domain.common.dto.BookSearchResponseDTO;
import com.seohaeng.backend.domain.common.dto.CommonResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommonQueryService {

    @Value("${cloud.naver.api.client-id}")
    private String NaverClientId;

    @Value("${cloud.naver.api.client-secret}")
    private String NaverClientSecret;

    private final String NAVER_API_URL = "https://openapi.naver.com/v1/search/book.json";

    private final RestTemplate restTemplate;

    public CommonResponseDTO.bookSearchResultListDTO bookSearch (
            String query,
            Integer display,
            Integer start,
            String sort){

        WebClient webClient = WebClient.builder()
                .baseUrl("https://openapi.naver.com")
                .defaultHeader("X-Naver-Client-Id", NaverClientId)
                .defaultHeader("X-Naver-Client-Secret", NaverClientSecret)
                .build();

        BookSearchResponseDTO.SearchResponseDTO responseJson = webClient.get()
                .uri(uriBuilder -> {
                    UriBuilder builder = uriBuilder
                            .path("/v1/search/book.json")
                            .queryParam("query", query);

                    if (display != null) builder.queryParam("display", display);
                    if (start != null) builder.queryParam("start", start);
                    if (sort != null) builder.queryParam("sort", sort);
                    return builder.build();
                })
                .retrieve()
                .bodyToMono(BookSearchResponseDTO.SearchResponseDTO.class)
                .block();

        List<BookSearchResponseDTO.BookItemDTO> BookItemDTOList = responseJson.getItems();

        List<CommonResponseDTO.bookSearchResultDTO> bookSearchResults  = BookItemDTOList.stream()
                .map(CommonConverter::toCommonResponseDTO)
                .collect(Collectors.toList());

        return CommonConverter.tobookSearchResultListDTO(bookSearchResults );
    }
}