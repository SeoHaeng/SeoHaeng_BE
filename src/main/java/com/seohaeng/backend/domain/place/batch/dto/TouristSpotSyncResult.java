package com.seohaeng.backend.domain.place.batch.dto;

import com.seohaeng.backend.domain.place.entity.place.Place;
import com.seohaeng.backend.domain.place.entity.place.PlaceImage;
import com.seohaeng.backend.domain.place.entity.placeAttribute.TouristSpotAttribute;

import java.util.List;

public record TouristSpotSyncResult(Place place, TouristSpotAttribute attr, List<PlaceImage> images) {}
