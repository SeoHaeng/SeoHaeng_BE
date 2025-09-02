package com.seohaeng.backend.domain.place.util;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MarkerUtils {

    public static <T, R> List<R> filterAndConvert(List<T> source,
                                                  Function<T, Double> latGetter,
                                                  Function<T, Double> lngGetter,
                                                  Function<T, R> mapper,
                                                  double minLat, double minLng, double maxLat, double maxLng,
                                                  int limit) {
        return source.stream()
                .filter(item -> latGetter.apply(item) != null && lngGetter.apply(item) != null)
                .filter(item -> isWithinViewport(latGetter.apply(item), lngGetter.apply(item),
                        minLat, minLng, maxLat, maxLng))
                .map(mapper)
                .limit(limit)
                .collect(Collectors.toList());
    }

    private static boolean isWithinViewport(double lat, double lng,
                                            double minLat, double minLng,
                                            double maxLat, double maxLng) {
        return lat >= minLat && lat <= maxLat && lng >= minLng && lng <= maxLng;
    }

}
