package com.seohaeng.backend.domain.place.repository;

import com.seohaeng.backend.domain.place.entity.placeAttribute.BookStoreAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookStoreAttributeRepository extends JpaRepository<BookStoreAttribute, Long> {

    @Query("SELECT b FROM BookStoreAttribute b LEFT JOIN FETCH b.place p WHERE b.bookStay = true")
    List<BookStoreAttribute> findAllBookStay();

}
