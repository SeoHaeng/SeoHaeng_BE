package com.seohaeng.backend.domain.place.util;

import com.seohaeng.backend.domain.place.dto.tourapiResponseDTO.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import java.net.URI;

@Component
@RequiredArgsConstructor
@Slf4j
public class TourApiClient {

    @Value("${tourapi.api.client-id}")
    private String serviceKey;

    private final String AREA_BASED_SEARCH_URL = "https://apis.data.go.kr/B551011/KorService2/areaBasedList2"; // 키워드검색
    private final String DETAIL_INTRO_URL = "https://apis.data.go.kr/B551011/KorService2/detailCommon2"; // 공통정보
    private final String DETAIL_COMMON_URL = "https://apis.data.go.kr/B551011/KorService2/detailIntro2"; // 소개정보
    private final String DETAIL_REPEAT_URL = "https://apis.data.go.kr/B551011/KorService2/detailInfo2"; // 반복정보

    // 지역 기반 검색
    public AreaBasedSearchResponseDTO searchAreaBasedPlaces(int contentTypeId, int pageNo){
        WebClient webClient = WebClient.builder()
                .baseUrl(AREA_BASED_SEARCH_URL)
                .build();

        String url = AREA_BASED_SEARCH_URL + "?numOfRows=1000&pageNo=" + pageNo +
                "&MobileOS=AND&MobileApp=seohaeng&_type=json&contentTypeId=" + contentTypeId + 
                "&areaCode=32&serviceKey=" + serviceKey;
        
        URI uri = URI.create(url);

        AreaBasedSearchResponseDTO response = webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(AreaBasedSearchResponseDTO.class)
                .block();

        log.info("[TourAPI] 지역 기반 검색 응답 완료");
        return response;
    }

    // 공통 정보
    public DetailCommonResponseDTO getDetailCommonInfo(Long contentId){
        WebClient webClient = WebClient.builder()
                .baseUrl(DETAIL_INTRO_URL)
                .build();

        String url = DETAIL_INTRO_URL + "?serviceKey=" + serviceKey +
                "&contentId=" + contentId + "&MobileOS=WEB&MobileApp=SeoHaeng&_type=json";
        
        URI uri = URI.create(url);
        
        DetailCommonResponseDTO response = webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(DetailCommonResponseDTO.class)
                .block();
        log.info("[TourAPI] 공통 정보 응답 완료");
        return response;
    }

    // 반복 정보
    public DetailRepeatResponseDTO getDetailRepeatInfo(int contentTypeId, Long contentId){
        WebClient webClient = WebClient.builder()
                .baseUrl(DETAIL_REPEAT_URL)
                .build();

        String url = DETAIL_REPEAT_URL + "?serviceKey=" + serviceKey +
                "&contentId=" + contentId + "&contentTypeId=" + contentTypeId + "&MobileOS=WEB&MobileApp=SeoHaeng&_type=json";
        
        URI uri = URI.create(url);
        
        DetailRepeatResponseDTO response = webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(DetailRepeatResponseDTO.class)
                .block();
        log.info("[TourAPI] 반복 정보 응답 완료");
        return response;
    }

    // 소개 정보
    public DetailIntroResponse getDetailIntroInfo(int contentTypeId, Long contentId){
        WebClient webClient = WebClient.builder()
                .baseUrl(DETAIL_COMMON_URL)
                .build();

        Class<? extends DetailIntroResponse> responseClass = getDetailIntroResponseClass(contentTypeId);

        String url = DETAIL_COMMON_URL + "?serviceKey=" + serviceKey +
                "&contentId=" + contentId + "&contentTypeId=" + contentTypeId + "&MobileOS=WEB&MobileApp=SeoHaeng&_type=json";
        
        URI uri = URI.create(url);
        
        DetailIntroResponse response = webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(responseClass)
                .block();
        log.info("[TourAPI] 소개 정보 응답 완료");
        return response;
    }

    private Class<? extends DetailIntroResponse> getDetailIntroResponseClass(int contentTypeId) {
        return switch (contentTypeId) {
            case 12 -> DetailIntroTouristResponseDTO.class;    // 관광지
            case 15 -> DetailIntroFestivalResponseDTO.class;   // 축제
            case 39 -> DetailIntroRestaurantResponseDTO.class; // 음식점
            default -> throw new IllegalArgumentException("지원하지 않는 콘텐츠 타입입니다: " + contentTypeId);
        };
    }
}
