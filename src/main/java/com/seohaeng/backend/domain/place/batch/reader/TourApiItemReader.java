package com.seohaeng.backend.domain.place.batch.reader;

import com.seohaeng.backend.domain.place.batch.exception.TourApiRetryableException;
import com.seohaeng.backend.domain.place.dto.tourapiResponseDTO.AreaBasedSearchResponseDTO;
import com.seohaeng.backend.domain.place.batch.TourApiClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemReader;
import org.springframework.retry.support.RetryTemplate;

import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class TourApiItemReader implements ItemReader<AreaBasedSearchResponseDTO.PlaceItem>, StepExecutionListener {

    private final TourApiClient tourApiClient;
    private final int contentTypeId;

    private List<AreaBasedSearchResponseDTO.PlaceItem> items;
    private int index = 0;

    private final RetryTemplate retryTemplate = RetryTemplate.builder()
            .maxAttempts(3)
            .exponentialBackoff(1000, 2, 10000)
            .retryOn(TourApiRetryableException.class)
            .build();

    @Override
    public void beforeStep(StepExecution stepExecution) {
        index = 0;
        AreaBasedSearchResponseDTO response = retryTemplate.execute(
                ctx -> tourApiClient.searchAreaBasedPlaces(contentTypeId, 1)
        );
        if (response == null
                || response.getResponse().getBody().getItems() == null
                || response.getResponse().getBody().getItems().getItem() == null
                || response.getResponse().getBody().getItems().getItem().isEmpty()) {
            log.warn("[Reader] contentTypeId={} 데이터 없음", contentTypeId);
            items = Collections.emptyList();
            return;
        }
        items = response.getResponse().getBody().getItems().getItem();
        log.info("[Reader] contentTypeId={} 아이템 {}개 로드 완료", contentTypeId, items.size());
    }

    @Override
    public AreaBasedSearchResponseDTO.PlaceItem read() {
        return index < items.size() ? items.get(index++) : null;
    }
}
