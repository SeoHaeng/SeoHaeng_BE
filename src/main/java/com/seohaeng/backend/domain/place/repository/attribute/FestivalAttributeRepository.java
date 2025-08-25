package com.seohaeng.backend.domain.place.repository.attribute;

import com.seohaeng.backend.domain.place.entity.place.Place;
import com.seohaeng.backend.domain.place.entity.placeAttribute.FestivalAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface FestivalAttributeRepository extends JpaRepository<FestivalAttribute, Long> {
    Optional<FestivalAttribute> findByPlace(Place place);

    @Query("SELECT f FROM FestivalAttribute f JOIN FETCH f.place WHERE f.startDate <= :currentDate AND f.endDate >= :currentDate")
    List<FestivalAttribute> findOngoingFestivals(@Param("currentDate") LocalDate currentDate);
}
