package com.seohaeng.backend.domain.place.batch;

import com.seohaeng.backend.domain.place.batch.exception.TourApiFatalException;
import com.seohaeng.backend.domain.place.batch.exception.TourApiRetryableException;
import com.seohaeng.backend.domain.place.dto.tourapiResponseDTO.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class TourApiClient {

    @Value("${tourapi.api.client-id}")
    private String serviceKey;

    private final RestClient restClient;

    private static final String AREA_BASED_SEARCH_URL = "https://apis.data.go.kr/B551011/KorService2/areaBasedList2";
    private static final String DETAIL_INTRO_URL = "https://apis.data.go.kr/B551011/KorService2/detailCommon2";
    private static final String DETAIL_COMMON_URL = "https://apis.data.go.kr/B551011/KorService2/detailIntro2";
    private static final String DETAIL_REPEAT_URL = "https://apis.data.go.kr/B551011/KorService2/detailInfo2";

    public TourApiClient(RestClient.Builder builder) {
        this.restClient = builder.build();
    }

    public AreaBasedSearchResponseDTO searchAreaBasedPlaces(int contentTypeId, int pageNo) {
        String url = AREA_BASED_SEARCH_URL + "?numOfRows=2000&pageNo=" + pageNo +
                "&MobileOS=AND&MobileApp=seohaeng&_type=json&contentTypeId=" + contentTypeId +
                "&lDongRegnCd=51&serviceKey=" + serviceKey;
        try {
            AreaBasedSearchResponseDTO response = restClient.get()
                    .uri(URI.create(url))
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (req, res) ->
                            classifyAndThrow(StreamUtils.copyToString(res.getBody(), StandardCharsets.UTF_8)))
                    .body(AreaBasedSearchResponseDTO.class);
            log.info("[TourAPI] 지역 기반 검색 응답 완료");
            return response;
        } catch (TourApiFatalException | TourApiRetryableException e) {
            throw e;
        } catch (Exception e) {
            log.warn("[TourAPI] 지역 기반 검색 실패 - contentTypeId: {}, 원인: {}", contentTypeId, e.getMessage());
            return null;
        }
    }

    public DetailCommonResponseDTO getDetailCommonInfo(String contentId) {
        String url = DETAIL_INTRO_URL + "?serviceKey=" + serviceKey +
                "&contentId=" + contentId + "&MobileOS=WEB&MobileApp=SeoHaeng&_type=json";
        DetailCommonResponseDTO response = restClient.get()
                .uri(URI.create(url))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (req, res) ->
                        classifyAndThrow(StreamUtils.copyToString(res.getBody(), StandardCharsets.UTF_8)))
                .body(DetailCommonResponseDTO.class);
        log.info("[TourAPI] 공통 정보 응답 완료");
        return response;
    }

    public DetailRepeatResponseDTO getDetailRepeatInfo(int contentTypeId, String contentId) {
        String url = DETAIL_REPEAT_URL + "?serviceKey=" + serviceKey +
                "&contentId=" + contentId + "&contentTypeId=" + contentTypeId + "&MobileOS=WEB&MobileApp=SeoHaeng&_type=json";
        DetailRepeatResponseDTO response = restClient.get()
                .uri(URI.create(url))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (req, res) ->
                        classifyAndThrow(StreamUtils.copyToString(res.getBody(), StandardCharsets.UTF_8)))
                .body(DetailRepeatResponseDTO.class);
        log.info("[TourAPI] 반복 정보 응답 완료");
        return response;
    }

    public DetailIntroResponse getDetailIntroInfo(int contentTypeId, String contentId) {
        Class<? extends DetailIntroResponse> responseClass = getDetailIntroResponseClass(contentTypeId);
        String url = DETAIL_COMMON_URL + "?serviceKey=" + serviceKey +
                "&contentId=" + contentId + "&contentTypeId=" + contentTypeId + "&MobileOS=WEB&MobileApp=SeoHaeng&_type=json";
        DetailIntroResponse response = restClient.get()
                .uri(URI.create(url))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (req, res) ->
                        classifyAndThrow(StreamUtils.copyToString(res.getBody(), StandardCharsets.UTF_8)))
                .body(responseClass);
        log.info("[TourAPI] 소개 정보 응답 완료");
        return response;
    }

    private void classifyAndThrow(String code) {
        switch (code.trim()) {
            case "LIMITED_NUMBER_OF_SERVICE_REQUESTS_EXCEEDS_ERROR",
                 "DEADLINE_HAS_EXPIRED_ERROR",
                 "SERVICE_KEY_IS_NOT_REGISTERED_ERROR",
                 "UNREGISTERED_IP_ERROR",
                 "SERVICE_ACCESS_DENIED_ERROR",
                 "NO_OPENAPI_SERVICE_ERROR" -> {
                log.error("[Fatal Exception] Tour API 재시도 불가 사유: {}", code);
                throw new TourApiFatalException(code);
            }
            default -> throw new TourApiRetryableException(code);
        }
    }

    private Class<? extends DetailIntroResponse> getDetailIntroResponseClass(int contentTypeId) {
        return switch (contentTypeId) {
            case 12 -> DetailIntroTouristResponseDTO.class;
            case 15 -> DetailIntroFestivalResponseDTO.class;
            case 39 -> DetailIntroRestaurantResponseDTO.class;
            default -> throw new IllegalArgumentException("지원하지 않는 콘텐츠 타입입니다: " + contentTypeId);
        };
    }
}
