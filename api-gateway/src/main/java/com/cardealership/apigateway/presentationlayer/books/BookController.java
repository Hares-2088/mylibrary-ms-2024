package com.cardealership.apigateway.presentationlayer.books;

import com.cardealership.apigateway.businesslayer.books.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
@Slf4j
public class BookController {

    BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<BookResponseModel>> getBooks(){
        return ResponseEntity.status(HttpStatus.OK).body(bookService.getAllBooks());
    }

    @GetMapping(value = "/{bookId}", produces = "application/json")
    public ResponseEntity<BookResponseModel> getBook(@PathVariable String bookId){
        return ResponseEntity.status(HttpStatus.OK).body(bookService.getBookByBookId(bookId));
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<BookResponseModel> createBook(@RequestBody BookRequestModel bookRequestModel){
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.createBook(bookRequestModel));
    }

    @PutMapping(value = "/{bookId}", produces = "application/json", consumes = "application/json")
    public ResponseEntity<Void> updateBook(@RequestBody BookRequestModel bookRequestModel,
                                                        @PathVariable String bookId){
        bookService.updateBook(bookId, bookRequestModel);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @DeleteMapping(value = "/{bookId}")
    public ResponseEntity<BookResponseModel> deleteBook(@PathVariable String bookId){
        bookService.deleteBook(bookId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
