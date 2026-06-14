package com.seohaeng.backend.domain.common.service;

import com.seohaeng.backend.domain.common.converter.CommonConverter;
import com.seohaeng.backend.domain.common.dto.BookSearchResponseDTO;
import com.seohaeng.backend.domain.common.dto.CommonResponseDTO;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommonQueryService {

    @Value("${cloud.naver.api.client-id}")
    private String naverClientId;

    @Value("${cloud.naver.api.client-secret}")
    private String naverClientSecret;

    private final RestClient.Builder restClientBuilder;

    private RestClient naverRestClient;

    @PostConstruct
    public void init() {
        this.naverRestClient = restClientBuilder
                .baseUrl("https://openapi.naver.com")
                .defaultHeader("X-Naver-Client-Id", naverClientId)
                .defaultHeader("X-Naver-Client-Secret", naverClientSecret)
                .build();
    }

    public CommonResponseDTO.bookSearchResultListDTO bookSearch(
            String query,
            Integer display,
            Integer start,
            String sort) {

        BookSearchResponseDTO.SearchResponseDTO responseJson = naverRestClient.get()
                .uri(uriBuilder -> {
                    var builder = uriBuilder.path("/v1/search/book.json")
                            .queryParam("query", query);
                    if (display != null) builder.queryParam("display", display);
                    if (start != null) builder.queryParam("start", start);
                    if (sort != null) builder.queryParam("sort", sort);
                    return builder.build();
                })
                .retrieve()
                .body(BookSearchResponseDTO.SearchResponseDTO.class);

        List<CommonResponseDTO.bookSearchResultDTO> bookSearchResults = responseJson.getItems().stream()
                .map(CommonConverter::toCommonResponseDTO)
                .collect(Collectors.toList());

        return CommonConverter.tobookSearchResultListDTO(bookSearchResults);
    }
}
