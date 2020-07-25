package com.javafx.repository.impl;

import com.javafx.entity.Book;
import com.javafx.repository.BookRepository;
import com.javafx.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public class BookRepositoryImpl extends CrudRepositoryImpl<Book, Integer> implements BookRepository {
}
