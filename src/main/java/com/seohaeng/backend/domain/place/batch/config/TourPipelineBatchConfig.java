package com.seohaeng.backend.domain.place.batch.config;

import com.seohaeng.backend.domain.place.batch.BatchAlertListener;
import com.seohaeng.backend.domain.place.batch.TourApiClient;
import com.seohaeng.backend.domain.place.batch.dto.FestivalSyncResult;
import com.seohaeng.backend.domain.place.batch.dto.RestaurantSyncResult;
import com.seohaeng.backend.domain.place.batch.dto.TouristSpotSyncResult;
import com.seohaeng.backend.domain.place.batch.exception.TourApiFatalException;
import com.seohaeng.backend.domain.place.batch.exception.TourApiRetryableException;
import com.seohaeng.backend.domain.place.batch.processor.FestivalItemProcessor;
import com.seohaeng.backend.domain.place.batch.processor.RestaurantItemProcessor;
import com.seohaeng.backend.domain.place.batch.processor.TouristSpotItemProcessor;
import com.seohaeng.backend.domain.place.batch.reader.TourApiItemReader;
import com.seohaeng.backend.domain.place.batch.writer.FestivalItemWriter;
import com.seohaeng.backend.domain.place.batch.writer.RestaurantItemWriter;
import com.seohaeng.backend.domain.place.batch.writer.TouristSpotItemWriter;
import com.seohaeng.backend.domain.place.dto.tourapiResponseDTO.AreaBasedSearchResponseDTO;
import com.seohaeng.backend.domain.place.repository.PlaceImageRepository;
import com.seohaeng.backend.domain.place.repository.PlaceRepository;
import com.seohaeng.backend.domain.place.repository.attribute.FestivalAttributeRepository;
import com.seohaeng.backend.domain.place.repository.attribute.RestaurantAttributeRepository;
import com.seohaeng.backend.domain.place.repository.attribute.TouristSpotAttributeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class TourPipelineBatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final TourApiClient tourApiClient;
    private final PlaceRepository placeRepository;
    private final PlaceImageRepository placeImageRepository;
    private final TouristSpotAttributeRepository touristSpotAttributeRepository;
    private final RestaurantAttributeRepository restaurantAttributeRepository;
    private final FestivalAttributeRepository festivalAttributeRepository;
    private final BatchAlertListener batchAlertListener;

    private static final int CHUNK_SIZE = 100;

    @Bean
    public Job tourPipelineBatchJob() {
        return new JobBuilder("TourPipelineBatchJob", jobRepository)
                .listener(batchAlertListener)
                .start(touristSpotSyncStep())
                .next(restaurantSyncStep())
                .next(festivalSyncStep())
                .build();
    }

    @Bean
    public Step touristSpotSyncStep() {
        TourApiItemReader reader = new TourApiItemReader(tourApiClient, 12);
        return new StepBuilder("touristSpotSyncStep", jobRepository)
                .<AreaBasedSearchResponseDTO.PlaceItem, TouristSpotSyncResult>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .processor(new TouristSpotItemProcessor(tourApiClient, placeRepository, placeImageRepository, touristSpotAttributeRepository))
                .writer(new TouristSpotItemWriter(placeRepository, touristSpotAttributeRepository, placeImageRepository))
                .listener(reader)
                .allowStartIfComplete(true)
                .startLimit(Integer.MAX_VALUE)
                .faultTolerant()
                .retry(TourApiRetryableException.class)
                .retryLimit(3)
                .backOffPolicy(tourApiBackOffPolicy())
                .skip(TourApiRetryableException.class)
                .skipLimit(10)
                .noRetry(TourApiFatalException.class)
                .noSkip(TourApiFatalException.class)
                .build();
    }

    @Bean
    public Step restaurantSyncStep() {
        TourApiItemReader reader = new TourApiItemReader(tourApiClient, 39);
        return new StepBuilder("restaurantSyncStep", jobRepository)
                .<AreaBasedSearchResponseDTO.PlaceItem, RestaurantSyncResult>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .processor(new RestaurantItemProcessor(tourApiClient, placeRepository, placeImageRepository, restaurantAttributeRepository))
                .writer(new RestaurantItemWriter(placeRepository, restaurantAttributeRepository, placeImageRepository))
                .listener(reader)
                .allowStartIfComplete(true)
                .startLimit(Integer.MAX_VALUE)
                .faultTolerant()
                .retry(TourApiRetryableException.class)
                .retryLimit(3)
                .backOffPolicy(tourApiBackOffPolicy())
                .skip(TourApiRetryableException.class)
                .skipLimit(10)
                .noRetry(TourApiFatalException.class)
                .noSkip(TourApiFatalException.class)
                .build();
    }

    @Bean
    public Step festivalSyncStep() {
        TourApiItemReader reader = new TourApiItemReader(tourApiClient, 15);
        return new StepBuilder("festivalSyncStep", jobRepository)
                .<AreaBasedSearchResponseDTO.PlaceItem, FestivalSyncResult>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .processor(new FestivalItemProcessor(tourApiClient, placeRepository, placeImageRepository, festivalAttributeRepository))
                .writer(new FestivalItemWriter(placeRepository, festivalAttributeRepository, placeImageRepository))
                .listener(reader)
                .allowStartIfComplete(true)
                .startLimit(Integer.MAX_VALUE)
                .faultTolerant()
                .retry(TourApiRetryableException.class)
                .retryLimit(3)
                .backOffPolicy(tourApiBackOffPolicy())
                .skip(TourApiRetryableException.class)
                .skipLimit(10)
                .noRetry(TourApiFatalException.class)
                .noSkip(TourApiFatalException.class)
                .build();
    }

    private ExponentialBackOffPolicy tourApiBackOffPolicy() {
        ExponentialBackOffPolicy backOff = new ExponentialBackOffPolicy();
        backOff.setInitialInterval(1000);
        backOff.setMultiplier(2.0);
        backOff.setMaxInterval(10000);
        return backOff;
    }
}
