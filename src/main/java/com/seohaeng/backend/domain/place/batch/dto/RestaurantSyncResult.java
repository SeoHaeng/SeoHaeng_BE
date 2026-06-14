package com.seohaeng.backend.domain.place.batch.dto;

import com.seohaeng.backend.domain.place.entity.place.Place;
import com.seohaeng.backend.domain.place.entity.place.PlaceImage;
import com.seohaeng.backend.domain.place.entity.placeAttribute.RestaurantAttribute;

import java.util.List;

public record RestaurantSyncResult(Place place, RestaurantAttribute attr, List<PlaceImage> images) {}
