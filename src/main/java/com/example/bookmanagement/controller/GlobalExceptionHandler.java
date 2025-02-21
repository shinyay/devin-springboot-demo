package com.example.bookmanagement.controller;

import java.util.ArrayList;
import com.example.bookmanagement.model.Book;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
        model.addAttribute("books", new ArrayList<>());
        model.addAttribute("error", "An unexpected error occurred: " + e.getMessage());
        return "index";
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public String handleEntityNotFoundException(EntityNotFoundException e, Model model) {
        model.addAttribute("books", new ArrayList<>());
        model.addAttribute("error", "Resource not found: " + e.getMessage());
        return "index";
    }
}
