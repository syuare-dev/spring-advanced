package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    // 기존 코드 > 테스트 코드를 위해 코드 삭제 X
    @Query("SELECT t FROM Todo t LEFT JOIN FETCH t.user u ORDER BY t.modifiedAt DESC")
    Page<Todo> findAllByOrderByModifiedAtDesc(Pageable pageable);

    // N+1 문제 -> 수정 코드
    @EntityGraph(attributePaths = "user")
    @Query("SELECT t FROM Todo t ORDER BY t.modifiedAt DESC") // N+1 문제 발생하도록
    Page<Todo> findAllWithUserOrderByModifiedAtDesc(Pageable pageable);

    // N+1 문제 -> 수정 코드
    @EntityGraph(attributePaths = "user")
    @Query("SELECT t FROM Todo t WHERE t.id = :todoId")
    Optional<Todo> findByIdWithUser(@Param("todoId") Long todoId);

    int countById(Long todoId);
}
