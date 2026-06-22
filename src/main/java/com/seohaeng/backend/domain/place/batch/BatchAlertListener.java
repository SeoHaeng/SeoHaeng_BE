package com.seohaeng.backend.domain.place.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
public class BatchAlertListener implements JobExecutionListener {

    private final RestClient restClient;

    @Value("${batch.webhook.url:}")
    private String webhookUrl;

    public BatchAlertListener(RestClient.Builder builder) {
        this.restClient = builder.build();
    }

    private static final int COLOR_BLUE  = 0x3498DB;  // 시작
    private static final int COLOR_GREEN = 0x2ECC71;  // 완료
    private static final int COLOR_RED   = 0xE74C3C;  // 실패

    @Override
    public void beforeJob(JobExecution jobExecution) {
        String jobName = jobExecution.getJobInstance().getJobName();
        log.info("[Batch 시작] Job: {}", jobName);

        if (webhookUrl.isBlank()) return;
        sendEmbed(embed("🚀 배치 시작", COLOR_BLUE,
                List.of(field("Job", jobName, true)),
                Instant.now()));

    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            sendSuccessAlert(jobExecution);
        } else if (jobExecution.getStatus() == BatchStatus.FAILED) {
            sendFailureAlert(jobExecution);
        }
    }

    private void sendSuccessAlert(JobExecution jobExecution) {
        String jobName = jobExecution.getJobInstance().getJobName();
        String duration = formatDuration(jobExecution);

        List<Map<String, Object>> fields = new java.util.ArrayList<>();
        fields.add(field("소요시간", duration, true));
        jobExecution.getStepExecutions().forEach(step ->
                fields.add(field(step.getStepName(),
                        "처리 " + step.getWriteCount() + "건 / 스킵 " + step.getSkipCount() + "건",
                        false)));

        log.info("[Batch 완료] Job: {} | 소요시간: {}", jobName, duration);
        if (webhookUrl.isBlank()) return;
        sendEmbed(embed("✅ 배치 완료", COLOR_GREEN, fields, Instant.now()));
    }

    private void sendFailureAlert(JobExecution jobExecution) {
        String jobName = jobExecution.getJobInstance().getJobName();

        String failedStep = jobExecution.getStepExecutions().stream()
                .filter(step -> step.getStatus() == BatchStatus.FAILED)
                .map(step -> step.getStepName() + " (스킵: " + step.getSkipCount() + "건)")
                .findFirst().orElse("알 수 없음");

        String cause = jobExecution.getStepExecutions().stream()
                .filter(step -> step.getStatus() == BatchStatus.FAILED)
                .flatMap(step -> step.getFailureExceptions().stream())
                .map(Throwable::getMessage)
                .filter(Objects::nonNull)
                .findFirst()
                .orElseGet(() -> jobExecution.getAllFailureExceptions().stream()
                        .map(Throwable::getMessage)
                        .filter(Objects::nonNull)
                        .findFirst()
                        .orElse("원인 불명"));

        log.error("[Fatal Exception] Job: {} | Step: {} | 원인: {}", jobName, failedStep, cause);
        if (webhookUrl.isBlank()) return;
        sendEmbed(embed("❌ 배치 실패", COLOR_RED,
                List.of(field("Job", jobName, true),
                        field("Step", failedStep, true),
                        field("원인", cause, false)),
                Instant.now()));
    }

    private Map<String, Object> embed(String title, int color, List<Map<String, Object>> fields, Instant timestamp) {
        return Map.of(
                "title", title,
                "color", color,
                "fields", fields,
                "timestamp", timestamp.toString()
        );
    }

    private Map<String, Object> field(String name, String value, boolean inline) {
        return Map.of("name", name, "value", value, "inline", inline);
    }

    private String formatDuration(JobExecution jobExecution) {
        if (jobExecution.getStartTime() == null || jobExecution.getEndTime() == null)
            return "알 수 없음";
        Duration d = Duration.between(jobExecution.getStartTime(), jobExecution.getEndTime());
        return d.toMinutes() + "분 " + (d.toSeconds() % 60) + "초";
    }

    private void sendEmbed(Map<String, Object> embed) {
        try {
            restClient.post()
                    .uri(webhookUrl)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("embeds", List.of(embed)))
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            log.error("[Error] 웹훅 전송 실패: {}", e.getMessage());
        }
    }
}
