package com.lec.spring.controller;

import com.lec.spring.domain.Book;
import com.lec.spring.service.BookService;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class BookController {

    private final BookService bookService;

    @GetMapping("/")
    public ResponseEntity<?> home(){
        return new ResponseEntity<>("ok", HttpStatus.OK); //200
    }
    //TODO

    @PostMapping("/book")
    public ResponseEntity<?> save(@RequestBody Book book){
        return new ResponseEntity<>(bookService.저장하기(book), HttpStatus.CREATED); //201

    }
    //TODO

    @GetMapping("/book/{id}")
    public ResponseEntity<?> detail(@PathVariable Long id){
        return new ResponseEntity<>(bookService.한건가져오기(id), HttpStatus.OK);
    }
    //TODO

    @GetMapping("/book")
    public ResponseEntity<?> findAll(){
        return new ResponseEntity<>(bookService.모두가져오기(), HttpStatus.OK);
    }
    //TODO

    @PutMapping("/book")
    public ResponseEntity<?> update(@RequestBody Book book){
        return new ResponseEntity<>(bookService.수정하기(book), HttpStatus.OK);
    }
    //TODO

    @DeleteMapping("/book/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        return new ResponseEntity<>(bookService.삭제하기(id), HttpStatus.OK);
    }
    //TODO`

}
