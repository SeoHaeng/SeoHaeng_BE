package com.seohaeng.backend.domain.place.service;

import com.seohaeng.backend.domain.place.converter.PlaceConverter;
import com.seohaeng.backend.domain.place.dto.PlaceResponseDTO;
import com.seohaeng.backend.domain.place.entity.Place;
import com.seohaeng.backend.domain.place.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaceQueryService {

    private final PlaceRepository placeRepository;

    public PlaceResponseDTO.placeListDto findBookChallengePlaces(Integer page, Integer size) {

        PageRequest pageRequest = PageRequest.of(page-1, size);
        Page<Place> bookChallengePlaces = placeRepository.findAllByBookChallengeStatusTrue(pageRequest);
        List<PlaceResponseDTO.placeDto> placeDtoList = bookChallengePlaces.getContent().stream()
                .map(PlaceConverter::toplaceDto).collect(Collectors.toList());

        return PlaceConverter.toplaceListDto(bookChallengePlaces, placeDtoList);
    }
}
