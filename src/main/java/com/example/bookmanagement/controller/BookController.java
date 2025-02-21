package com.example.bookmanagement.controller;

import com.example.bookmanagement.model.Book;
import com.example.bookmanagement.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import jakarta.validation.Valid;

@Controller
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @ModelAttribute("book")
    public Book bookModelAttribute() {
        return new Book();
    }

    @GetMapping({"/", ""})
    public String showIndex(Model model) {
        try {
            model.addAttribute("book", new Book());
            List<Book> books = bookService.getAllBooks();
            model.addAttribute("books", books);
            return "index";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to load books: " + e.getMessage());
            model.addAttribute("books", new ArrayList<>());
            model.addAttribute("book", new Book());
            return "index";
        }
    }

    @PostMapping("/books")
    public String registerBook(@Valid @ModelAttribute("book") Book book, 
                             BindingResult result,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Please correct the errors below");
            return "redirect:/";
        }
        try {
            bookService.saveBook(book);
            redirectAttributes.addFlashAttribute("success", "Book registered successfully!");
            return "redirect:/";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to save book: " + e.getMessage());
            return "redirect:/";
        }
    }

    @GetMapping("/books/search")
    public String searchBooks(@RequestParam(required = false) String name,
                            @RequestParam(required = false) String author,
                            Model model) {
        try {
            List<Book> searchResults;
            if (name != null && !name.isEmpty()) {
                searchResults = bookService.findBooksByName(name);
            } else if (author != null && !author.isEmpty()) {
                searchResults = bookService.findBooksByAuthor(author);
            } else {
                searchResults = bookService.getAllBooks();
            }
            model.addAttribute("books", searchResults);
            return "index";
        } catch (Exception e) {
            model.addAttribute("error", "Search failed: " + e.getMessage());
            model.addAttribute("books", List.of());
            return "index";
        }
    }

    @PostMapping("/books/{id}")
    public String updateBook(@PathVariable Long id,
                           @Valid @ModelAttribute("book") Book book,
                           BindingResult result,
                           Model model) {
        if (result.hasErrors()) {
            model.addAttribute("books", bookService.getAllBooks());
            return "index";
        }
        try {
            bookService.updateBook(id, book);
            return "redirect:/";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to update book: " + e.getMessage());
            model.addAttribute("books", bookService.getAllBooks());
            return "index";
        }
    }

    @PostMapping("/books/{id}/delete")
    public String deleteBook(@PathVariable Long id, Model model) {
        try {
            bookService.deleteBook(id);
            return "redirect:/";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to delete book: " + e.getMessage());
            model.addAttribute("books", bookService.getAllBooks());
            return "index";
        }
    }

    @PostMapping("/books/delete-all")
    public String deleteAllBooks(Model model) {
        try {
            bookService.deleteAllBooks();
            return "redirect:/";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to delete all books: " + e.getMessage());
            model.addAttribute("books", bookService.getAllBooks());
            return "index";
        }
    }
}
