package com.lec.spring.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lec.spring.domain.Book;
import com.lec.spring.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @SpringBootTest
 *   https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/test/context/SpringBootTest.html
 *   ● Spring Boot 통합 테스트. (모든 bean 들이 IoC 에 올리고 테스트)
 *
 *   ● custom Environment properties  사용가능
 *
 *   ● webEnvironment mode 제공
 *       https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/test/context/SpringBootTest.WebEnvironment.html
 *       MOCK : (디폴트) 실제 tomcat 을 올리는게 아니라 '다른 (가짜)tomcat' 으로 테스트
 *       RANDOM_PORT : 실제 tomcat + 임의 Port 로 테스트.
 *       DEFINED_PORT : 실제 tomcat + application.properties 에 정의된 port
 *
 *   ● TestRestTemplate, WebTestClient bean 제공 ->  웹서버 기능 테스트용!
 */

/**
 * Mockito
 *    Controller 에 request 하여 테스트 할수 있는 라이브러리
 *
 *    @AutoConfigureMockMvc 가 있어야 MockMvc 객체들이 IoC 되어 사용할수 있다.
 */


@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc  // MockMvc bean 을 IoC 에 등록
@Transactional  // 각각의 테스트 함수가 종료될때마다 Transaction 을 rollback 해줌.
public class BookControllerIntegratedTest {

    // Mockito 라이브러리
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Test
    public void save_테스트() {
        log.info("save_테스트() 시작 ==============================");
    }

    @Test
    public void 저장하기테스트() throws Exception {
        log.info("테스트() 시작 ==============================");

        // ■ given : 테스트를 하기위한 준비
        Book book = new Book(null, "스프링 따라하기", "유인아");
        String content = new ObjectMapper().writeValueAsString(book);  // JSON <- Java
        log.info(content);

        // 통합테스트에선 실제 Service bean 이 IoC 된다.
        // 그래서 stub 이 필요없다.


        // 실제 테스트 실행
        ResultActions resultActions = mockMvc.perform(post("/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON)  // 응답은 이것이어야 함.
        );
        // 실행후 perform() 의 결과로 '응답' 을 받을 수 있다

        // 위 ResultAction 에 대한 기대값을 받을 수 있다.

        // ■ then : 검증
        resultActions
                // 기대하는 결과
                .andExpect(status().isCreated())  // 201 응답
                .andExpect(jsonPath("$.title").value("스프링 따라하기"))
                // JsonPath : Json 객체를 탐색하기 위한 표준화된 방법
                //   SpringBoot 에는 이미 의존성이 자동으로 설정 되어 있다. → (spring-boot-starter-test 에 이미 포함된 라이브러리)
                //   JsonPath Online Evaluator 들 :
                //     https://jsonpath.com/,
                //     https://www.javainuse.com/jsonpath

                // 그 다음 행동 지정
                .andDo(MockMvcResultHandlers.print())  // 결과를 콘솔에 출력
        ;

        // ↑ given, when, then 을 통해  Service 나 Repository 없이 (신경쓰지 않고도)
        // Controller 에 대해서 동작 테스트를 할수 있다!
    }

    @Test
    public void findAll_테스트()throws Exception{

        //■given
        // 통합테스트에선 given 데이터가 필요없다.


        //■when
        ResultActions resultActions = mockMvc.perform(get("/book")
                .accept(MediaType.APPLICATION_JSON));

        //■then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.[0].title").value("가연이가 밥먹었다"))
                .andDo(MockMvcResultHandlers.print());
    }

}