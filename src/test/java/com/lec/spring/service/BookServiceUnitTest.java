package com.lec.spring.service;

import com.lec.spring.domain.Book;
import com.lec.spring.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * @Service 의 단위 테스트
 *   Service 는 business logic 의 기능 담당.
 *   Service 에 필요한(관련된) 것들만 메모리에(IoC) 띄어 테스트
 *   Service 는 Repository 가 필요하다.
 *   어떻게 Repository 를 사용하여 테스트할수 있을까?
 *
 *   만약 Repository를 메모리에 올리면 그건 '단위 테스트' 가 아니다.
 *
 *   그래서 Repository 를 => 가짜 객체로 만들어 올릴수 있다.
 *            MockitoExtension 을 통해 가짜객체를 올릴수 있는 환경 제공
 *
 *   MockitoExtension 환경에서의 주요 annotation 들
 *
 *    @Mock 와 @InjectMocks
 *       Spring IoC 가 아니라, MockitoExtension 환경에 bean 생성.
 *
 *    @InjectMocks
 *        해당 객체에 Test 내 @Mock 으로 등록된 모든 bean 들을 주입받게 한다.
 *
 *   결론적으로 Service 의 단위테스트는 스프링 환경이 필요없다.
 */

@ExtendWith(MockitoExtension.class)
public class BookServiceUnitTest {
    @InjectMocks
    private BookService bookService;
    // ↑ Spring IoC 안에 생성되는게 아닌지라...
    //   BookService 안의 BookRepository 는 null 일텐데?
    //   어떻게 BookService 에 BookRepository 를 주입할수 있을까?

    @Mock
    private BookRepository bookRepository;


    //Service의 단위테스트는 스프링 환경이 필요없다.
    @Test
    public void 저장하기_테스트(){

        //given
        Book book = new Book();
        book.setTitle("콜록콜록");
        book.setAuthor("유인아");

        //when
        //stub - 동작지정
        when(bookRepository.save(book)).thenReturn(book);

        //Test 실행
        Book bookEntity = bookService.저장하기(book);

        //then
        assertEquals(bookEntity, book);

    }


}
