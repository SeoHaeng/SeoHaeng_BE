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

    private List<AreaBasedSearchResponseDTO.PlaceItem> items = Collections.emptyList();
    private int index = 0;
    private int pageNo = 1;
    private boolean done = false;

    private final RetryTemplate retryTemplate = RetryTemplate.builder()
            .maxAttempts(3)
            .exponentialBackoff(1000, 2, 10000)
            .retryOn(TourApiRetryableException.class)
            .build();

    @Override
    public void beforeStep(StepExecution stepExecution) {
        index = 0;
        pageNo = 1;
        done = false;
        items = Collections.emptyList();
    }

    @Override
    public AreaBasedSearchResponseDTO.PlaceItem read() {
        if (done) return null;

        if (index >= items.size()) {
            fetchNextPage();
            if (items.isEmpty()) {
                done = true;
                return null;
            }
            index = 0;
        }

        return items.get(index++);
    }

    private void fetchNextPage() {
        int currentPage = pageNo++;
        AreaBasedSearchResponseDTO response = retryTemplate.execute(
                ctx -> tourApiClient.searchAreaBasedPlaces(contentTypeId, currentPage)
        );
        if (response == null
                || response.getResponse().getBody().getItems() == null
                || response.getResponse().getBody().getItems().getItem() == null
                || response.getResponse().getBody().getItems().getItem().isEmpty()) {
            log.info("[Reader] contentTypeId={} pageNo={} 데이터 소진", contentTypeId, currentPage);
            items = Collections.emptyList();
            return;
        }
        items = response.getResponse().getBody().getItems().getItem();
        log.info("[Reader] contentTypeId={} pageNo={} 아이템 {}개 로드", contentTypeId, currentPage, items.size());
    }
}
