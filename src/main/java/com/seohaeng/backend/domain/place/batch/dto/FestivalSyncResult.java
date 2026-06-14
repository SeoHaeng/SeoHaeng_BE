package com.seohaeng.backend.domain.place.batch.dto;

import com.seohaeng.backend.domain.place.entity.place.Place;
import com.seohaeng.backend.domain.place.entity.place.PlaceImage;
import com.seohaeng.backend.domain.place.entity.placeAttribute.FestivalAttribute;

import java.util.List;

public record FestivalSyncResult(Place place, FestivalAttribute attr, List<PlaceImage> images) {}
