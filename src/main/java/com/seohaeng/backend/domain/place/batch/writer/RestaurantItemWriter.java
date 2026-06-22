package com.seohaeng.backend.domain.place.batch.writer;

import com.seohaeng.backend.domain.place.batch.dto.RestaurantSyncResult;
import com.seohaeng.backend.domain.place.repository.PlaceImageRepository;
import com.seohaeng.backend.domain.place.repository.PlaceRepository;
import com.seohaeng.backend.domain.place.repository.attribute.RestaurantAttributeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

@Slf4j
@RequiredArgsConstructor
public class RestaurantItemWriter implements ItemWriter<RestaurantSyncResult> {

    private final PlaceRepository placeRepository;
    private final RestaurantAttributeRepository restaurantAttributeRepository;
    private final PlaceImageRepository placeImageRepository;

    @Override
    public void write(Chunk<? extends RestaurantSyncResult> chunk) {
        placeRepository.saveAll(chunk.getItems().stream().map(RestaurantSyncResult::place).toList());
        restaurantAttributeRepository.saveAll(chunk.getItems().stream().map(RestaurantSyncResult::attr).toList());
        placeImageRepository.saveAll(chunk.getItems().stream().flatMap(r -> r.images().stream()).toList());
        log.info("[Writer] 음식점 {}건 저장 완료", chunk.getItems().size());
    }
}
