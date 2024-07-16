package com.lec.spring.repository;

import com.lec.spring.domain.Book;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Repository 의 단위테스트
 *   repository 만 테스트
 *   DB 관련된 Bean 들만 메모리에(IoC) 올리고 테스트
 *
 *
 * @DataJpaTest
 *   JPA 관련 bean 들만 메모리에 올린다. (ex: Repository 들)
 *
 * @AutoConfigureTestDatabase
 *   replace 옵션
 *      Replace.ANY : 가짜 DB로 테스트. (단위 테스트 할때는 이를 사용하자)
 *      Replace.NONE : 실제 DB로 테스트
 */
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@DataJdbcTest
public class BookRepositoryUnitTest {

    // 굳이 @Mock 할 필요 없다 <- IoC 에 등록되어 있으니까!

    @Autowired
    private BookRepository bookRepository;

    @Test
    public void save_테스트(){
        //given
        Book book = new Book(null, "베리베리", "유인아");

        //repository 는 stub 이 필요없다. repository 가 의존하는 객체가 없을테니까
        //when
        Book bookEntity = bookRepository.save(book);

        //then
        assertEquals("베리베리", bookEntity.getTitle());
        assertNotNull(bookEntity.getId());

    }



}
