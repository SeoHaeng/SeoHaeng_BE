package com.seohaeng.backend.domain.place.util;

import com.seohaeng.backend.domain.place.service.PipelineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PipelineRunner implements CommandLineRunner {

    private final PipelineService pipelineService;

    @Override
    public void run(String... args) throws Exception {
        log.info("애플리케이션 시작 - 데이터 파이프라인 실행 시작");

        try {
            // 관광지 데이터 파이프라인
            pipelineService.getAllTourSpotDatas();
            log.info("관광지 파이프라인 완료, 1분 대기 중...");
            Thread.sleep(60000);

            // 음식점 데이터 파이프라인
            pipelineService.getAllRestaurantDatas();
            log.info("음식점 파이프라인 완료, 1분 대기 중...");
            Thread.sleep(60000);

            // 축제 데이터 파이프라인
            pipelineService.getAllFestivalDatas();

            log.info("모든 데이터 파이프라인 실행 완료");
        } catch (Exception e) {
            log.error("데이터 파이프라인 실행 중 오류 발생", e);
        }
    }
}