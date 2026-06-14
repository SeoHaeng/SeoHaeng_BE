package com.seohaeng.backend.domain.place.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class TourBatchScheduler {

    private final JobLauncher jobLauncher;
    private final Job tourPipelineBatchJob;

    @Scheduled(cron = "0 0 4 * * ?")
    public void runPipeline() {
        log.info("Tour 데이터 파이프라인 배치 시작");
        try {
            JobParameters params = new JobParametersBuilder()
                    .addLocalDateTime("triggeredAt", LocalDateTime.now())
                    .toJobParameters();
            jobLauncher.run(tourPipelineBatchJob, params);
            log.info("Tour 데이터 파이프라인 배치 완료");
        } catch (Exception e) {
            log.error("Tour 데이터 파이프라인 배치 실패", e);
        }
    }
}
