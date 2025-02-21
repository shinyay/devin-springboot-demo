package com.example.bookmanagement.service;

import com.example.bookmanagement.model.Book;
import java.util.List;

public interface BookService {
    Book saveBook(Book book);
    List<Book> getAllBooks();
    Book getBookById(Long id);
    List<Book> findBooksByName(String name);
    List<Book> findBooksByAuthor(String author);
    Book updateBook(Long id, Book book);
    void deleteBook(Long id);
    void deleteAllBooks();
}
