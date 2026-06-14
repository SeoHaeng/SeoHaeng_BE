package com.seohaeng.backend.domain.place.batch.writer;

import com.seohaeng.backend.domain.place.batch.dto.TouristSpotSyncResult;
import com.seohaeng.backend.domain.place.repository.PlaceImageRepository;
import com.seohaeng.backend.domain.place.repository.PlaceRepository;
import com.seohaeng.backend.domain.place.repository.attribute.TouristSpotAttributeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

@Slf4j
@RequiredArgsConstructor
public class TouristSpotItemWriter implements ItemWriter<TouristSpotSyncResult> {

    private final PlaceRepository placeRepository;
    private final TouristSpotAttributeRepository touristSpotAttributeRepository;
    private final PlaceImageRepository placeImageRepository;

    @Override
    public void write(Chunk<? extends TouristSpotSyncResult> chunk) {
        placeRepository.saveAll(chunk.getItems().stream().map(TouristSpotSyncResult::place).toList());
        touristSpotAttributeRepository.saveAll(chunk.getItems().stream().map(TouristSpotSyncResult::attr).toList());
        placeImageRepository.saveAll(chunk.getItems().stream().flatMap(r -> r.images().stream()).toList());
        log.info("[Writer] 관광지 {}건 저장 완료", chunk.getItems().size());
    }
}
