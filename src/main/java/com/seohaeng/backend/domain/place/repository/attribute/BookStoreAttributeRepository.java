package com.seohaeng.backend.domain.place.repository.attribute;

import com.seohaeng.backend.domain.place.entity.placeAttribute.BookStoreAttribute;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookStoreAttributeRepository extends JpaRepository<BookStoreAttribute, Long> {
}
