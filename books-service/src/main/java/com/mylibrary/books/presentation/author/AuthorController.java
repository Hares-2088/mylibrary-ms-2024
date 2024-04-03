package com.mylibrary.books.presentation.author;

import com.mylibrary.books.business.author.AuthorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/authors")
public class AuthorController {

        AuthorService authorService;

        public AuthorController(AuthorService authorService) {
            this.authorService = authorService;
        }

        @GetMapping(produces = "application/json")
        public ResponseEntity<List<AuthorResponseModel>> getAuthors(){
            return ResponseEntity.status(HttpStatus.FOUND).body(authorService.getAuthors());
        }

        @GetMapping(value = "/{authorId}", produces = "application/json")
        public ResponseEntity<AuthorResponseModel> getAuthorByAuthorId(@PathVariable String authorId){
            return ResponseEntity.status(HttpStatus.FOUND).body(authorService.getAuthor(authorId));
        }

        @PostMapping(produces = "application/json", consumes = "application/json")
        public ResponseEntity<AuthorResponseModel> createAuthor(@RequestBody AuthorRequestModel authorRequestModel){
            return ResponseEntity.status(HttpStatus.CREATED).body(authorService.addAuthor(authorRequestModel));
        }
        @PutMapping(value = "/{authorId}", produces = "application/json", consumes = "application/json")
        public ResponseEntity<AuthorResponseModel> updateAuthor(@RequestBody AuthorRequestModel authorRequestModel,
                                                                @PathVariable String authorId){
            return ResponseEntity.status(HttpStatus.OK).body(authorService.updateAuthor(authorId, authorRequestModel));
        }

        @DeleteMapping(value = "/{authorId}")
        public ResponseEntity<AuthorResponseModel> deleteAuthor(@PathVariable String authorId){
            authorService.deleteAuthor(authorId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
}
