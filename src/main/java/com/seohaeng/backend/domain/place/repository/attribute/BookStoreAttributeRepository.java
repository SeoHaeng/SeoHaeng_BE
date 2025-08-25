package com.seohaeng.backend.domain.place.repository.attribute;

import com.seohaeng.backend.domain.place.entity.placeAttribute.BookStoreAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookStoreAttributeRepository extends JpaRepository<BookStoreAttribute, Long> {

    @Query("SELECT b FROM BookStoreAttribute b LEFT JOIN FETCH b.place p WHERE b.bookStay = true")
    List<BookStoreAttribute> findAllBookStay();

    @Query("SELECT b FROM BookStoreAttribute b LEFT JOIN FETCH b.place p WHERE b.bookCafe = true")
    List<BookStoreAttribute> findAllBookCafe();

    @Query("SELECT b FROM BookStoreAttribute b LEFT JOIN FETCH b.place p WHERE p.placeType = 'BOOKSTORE'")
    List<BookStoreAttribute> findAllBookstore();

    @Query("SELECT b FROM BookStoreAttribute b LEFT JOIN FETCH b.place p WHERE p.placeType = 'SPACE_BOOKMARK'")
    List<BookStoreAttribute> findAllSpaceBookmark();

}
