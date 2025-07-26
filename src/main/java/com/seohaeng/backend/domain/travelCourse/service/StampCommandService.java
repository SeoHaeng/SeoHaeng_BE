package com.seohaeng.backend.domain.travelCourse.service;

import com.seohaeng.backend.domain.travelCourse.entity.Region;
import com.seohaeng.backend.domain.travelCourse.entity.Stamp;
import com.seohaeng.backend.domain.travelCourse.repository.StampRepository;
import com.seohaeng.backend.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class StampCommandService {

    private final StampRepository stampRepository;

    public void makeStamp(User user, Region travelRegion, LocalDate stampedDate) {
        Stamp stamp = stampRepository.findByUserAndRegion(user,travelRegion).orElse(null);
        if(stamp == null) {
            Stamp newStamp = Stamp.builder()
                    .user(user)
                    .stampedDate(stampedDate)
                    .region(travelRegion)
                    .build();
            stampRepository.save(newStamp);
        }
    }
}
