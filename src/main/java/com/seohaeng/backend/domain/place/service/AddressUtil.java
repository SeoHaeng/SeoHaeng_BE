package com.seohaeng.backend.domain.place.service;

import com.seohaeng.backend.global.apiPayload.code.status.ErrorStatus;
import com.seohaeng.backend.global.apiPayload.exception.handler.PlaceHandler;

import java.util.LinkedHashMap;
import java.util.Map;

public class AddressUtil {

    private static final Map<String, Integer> CITY_MAP = new LinkedHashMap<>();

    static {
        CITY_MAP.put("강릉", 1);
        CITY_MAP.put("속초", 2);
        CITY_MAP.put("춘천", 3);
        CITY_MAP.put("원주", 4);
        CITY_MAP.put("동해", 5);
        CITY_MAP.put("태백", 6);
        CITY_MAP.put("삼척", 7);
        CITY_MAP.put("홍천", 8);
        CITY_MAP.put("횡성", 9);
        CITY_MAP.put("영월", 10);
        CITY_MAP.put("평창", 11);
        CITY_MAP.put("정선", 12);
        CITY_MAP.put("철원", 13);
        CITY_MAP.put("화천", 14);
        CITY_MAP.put("양구", 15);
        CITY_MAP.put("인제", 16);
        CITY_MAP.put("고성", 17);
        CITY_MAP.put("양양", 18);
    }

    /**
     * 주소 문자열에서 강원도 시 이름을 찾아 매핑된 번호 반환
     * @param address 주소 문자열
     * @return 시 코드 (없으면 PlaceHandler 예외 발생)
     */
    public static Long getCityCode(String address) {
        if (address == null || address.isBlank()) return null;
        for (Map.Entry<String, Integer> entry : CITY_MAP.entrySet()) {
            if (address.contains(entry.getKey())) {
                return entry.getValue().longValue();
            }
        }
        throw new PlaceHandler(ErrorStatus.REGION_NOT_GANGWON);
    }
}