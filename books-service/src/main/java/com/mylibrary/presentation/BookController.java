package com.mylibrary.presentation;

import com.mylibrary.business.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<BookResponseModel>> getBooks(){
        return ResponseEntity.status(HttpStatus.OK).body(bookService.getBooks());
    }

    @GetMapping(value = "/{bookId}", produces = "application/json")
    public ResponseEntity<BookResponseModel> getBook(@PathVariable String bookId){
        return ResponseEntity.status(HttpStatus.OK).body(bookService.getBook(bookId));
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<BookResponseModel> createBook(@RequestBody  BookRequestModel bookRequestModel){
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.createBook(bookRequestModel));
    }

    @PutMapping(value = "/{bookId}", produces = "application/json", consumes = "application/json")
    public ResponseEntity<BookResponseModel> updateBook(@RequestBody BookRequestModel bookRequestModel,
                                                        @PathVariable String bookId){
        return ResponseEntity.status(HttpStatus.OK).body(bookService.updateBook(bookId, bookRequestModel));
    }

    @PatchMapping(value = "/{bookId}/reduceAvailability", produces = "application/json")
    public ResponseEntity<BookResponseModel> reduceBookAvailability(@PathVariable String bookId){
        return ResponseEntity.status(HttpStatus.OK).body(bookService.reduceBookAvailability(bookId));
    }

    //increase availability
    @PatchMapping(value = "/{bookId}/increaseAvailability", produces = "application/json")
    public ResponseEntity<BookResponseModel> increaseBookAvailability(@PathVariable String bookId){
        return ResponseEntity.status(HttpStatus.OK).body(bookService.increaseBookAvailability(bookId));
    }

    @DeleteMapping(value = "/{bookId}")
    public ResponseEntity<BookResponseModel> deleteBook(@PathVariable String bookId){
        bookService.deleteBook(bookId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
