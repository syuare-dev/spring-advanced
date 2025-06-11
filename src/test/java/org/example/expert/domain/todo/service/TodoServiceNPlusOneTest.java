package org.example.expert.domain.todo.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@Transactional
class TodoServiceNPlusOneTest {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    EntityManagerFactory emf;

    // 테스트 세팅
    @BeforeEach
    void setUp() {

        // User, Todo 5개 생성
        for (int i=1; i<=5; i++) {
            User user = new User("test" + i + "@email.com", "pw123", UserRole.USER);
            userRepository.save(user);

            Todo todo = new Todo("테스트 제목" + i, "테스트 내용" + i, "테스트 날씨", user);
            todoRepository.save(todo);
        }
    }


    /**
     * N+1 테스트 -> 에러 발생
     */
    @Test
    void nPlusOneError() {

        // 쿼리 수 확인
        SessionFactory sf = emf.unwrap(SessionFactory.class);
        sf.getStatistics().setStatisticsEnabled(true);

        System.out.println("===== N+1 문제 발생 테스트 =====");
        Page<Todo> todos = todoRepository.findAllByOrderByModifiedAtDesc(PageRequest.of(0,5));

        for (Todo todo : todos) {
            System.out.println("제목: " + todo.getTitle());
            System.out.println("유저이메일: " + todo.getUser().getEmail());
        }

        System.out.println("쿼리 실행 수: " + sf.getStatistics().getPrepareStatementCount());

        System.out.println("===== N+1 문제 발생 테스트 종료 =====");

    }

    @Test
    void nPlusOneErrorResolve() {

        // 쿼리 수 확인
        SessionFactory sf = emf.unwrap(SessionFactory.class);
        sf.getStatistics().setStatisticsEnabled(true);

        System.out.println("===== N+1 발생 테스트 =====");
        Page<Todo> todos = todoRepository.findAllWithUserOrderByModifiedAtDesc(PageRequest.of(0,5));

        for (Todo todo : todos) {
            System.out.println("제목: " + todo.getTitle());
            System.out.println("유저이메일: " + todo.getUser().getEmail());
        }

        System.out.println("쿼리 실행 수: " + sf.getStatistics().getPrepareStatementCount());

        System.out.println("===== N+1 발생 테스트 종료 =====");

    }
}