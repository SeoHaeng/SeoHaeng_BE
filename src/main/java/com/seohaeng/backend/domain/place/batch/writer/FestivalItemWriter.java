package com.seohaeng.backend.domain.place.batch.writer;

import com.seohaeng.backend.domain.place.batch.dto.FestivalSyncResult;
import com.seohaeng.backend.domain.place.repository.PlaceImageRepository;
import com.seohaeng.backend.domain.place.repository.PlaceRepository;
import com.seohaeng.backend.domain.place.repository.attribute.FestivalAttributeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

@Slf4j
@RequiredArgsConstructor
public class FestivalItemWriter implements ItemWriter<FestivalSyncResult> {

    private final PlaceRepository placeRepository;
    private final FestivalAttributeRepository festivalAttributeRepository;
    private final PlaceImageRepository placeImageRepository;

    @Override
    public void write(Chunk<? extends FestivalSyncResult> chunk) {
        placeRepository.saveAll(chunk.getItems().stream().map(FestivalSyncResult::place).toList());
        festivalAttributeRepository.saveAll(chunk.getItems().stream().map(FestivalSyncResult::attr).toList());
        placeImageRepository.saveAll(chunk.getItems().stream().flatMap(r -> r.images().stream()).toList());
        log.info("[Writer] 축제 {}건 저장 완료", chunk.getItems().size());
    }
}
