package com.lec.spring.service;

import com.lec.spring.domain.Book;
import com.lec.spring.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// @Transactional 선언은 '클래스' 혹은 '메소드' 에 선언 가능.
//   @Transactional 이 선언된 메소드가 호출되면
//   트랜잭션을 시작하고
//       - 메소드가 정상적으로 리턴 하게 되면 Commit 실행 (변경내역이 DB 저장, INSERT, UPDATE, DELETE <- DML)
//       - 중간예 예외 발생되면 Rollback

@RequiredArgsConstructor
@Service
public class BookService {
    private final BookRepository bookRepository;

    @Transactional
    public Book 저장하기 (Book book){
        return bookRepository.save(book);   //INSERT
    }


    //DML 아님에도 @Transactional 을 붙인 이유 --> 정합성을 유지하기 위해서
    // 1. JPA에 '변경감지' 라는 기능이 있는데. readOnly = true 를 하면 '변경감지' 동작을 안함 (내부 연산 기능 줄임)
    // 2. SELECT 에도 Transactional 이 붙어 있으니
    //  -> update 시의 정합성을 유지해줌.
    //  -> insert 의 유령 데이터 현상 (팬텀현상)은 못막음.


    // 부정합 이란?  그리고 update 시의 정합성
    //     DB 의 10000원
    //    select 하여 10000원 가져온뒤,  이를 사용하여 무언가 긴시간이 걸리는 ~연산작업 (ex: 정산작업) 을 하는중이라 하자.
    //    그 사이에 누군가(다른 사람이) 20000원으로 바꾸었다면?
    //    정산작업중에 통계를 내기위해 다시 select 했더니? -> 20000 이 되었다면.  이를 '부정합' 이라 합니다
    //    @Transactional 을 걸어두면,  작업이 끝나는 동안 select 결과는 처음과 같이 10000 이다
    //    외부의 다른이가 20000원으로 바꾸었다 해도, 나의 transaction 동안은 10000원이 되는 것이다.

        // 팬텀현상이라 한다!
    // select A:월:10000  가져와서 정산작업중
    // 그사이에 누군가 A:화:20000 을 INSERT 했다면?
    // 정산작업중에 다시 select A:주중데이터 (통계) 를 내려 할때 아까는 없었던 A:화:20000 이 반영되게 된다
    // 이를 팬텀 현상이라 한다
    //  @Transactional 으로 못 막는다.
    @Transactional(readOnly = true)
    public Book 한건가져오기(Long id){    //SELECT
        return bookRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("id를 확인해주세요"));
    }

    @Transactional(readOnly = true)
    public List<Book> 모두가져오기(){
        return bookRepository.findAll();
    }
    @Transactional
    public Book 수정하기(Book book){
        Book bookEntity = bookRepository.findById(book.getId()).orElseThrow(() -> new IllegalArgumentException("id 를 확인해주세요"));

        bookEntity.setTitle(book.getTitle());
        bookEntity.setAuthor(book.getAuthor());

        // 리턴하고 메소드 종료될때  -> 즉 transaction 이 종료될때, -> dirty check 하여 DB에 update 발생 (flush) -> commit
        return bookEntity;
    }

    @Transactional
    public String 삭제하기(Long id){
        bookRepository.deleteById(id);
        return "ok";
    }


}
