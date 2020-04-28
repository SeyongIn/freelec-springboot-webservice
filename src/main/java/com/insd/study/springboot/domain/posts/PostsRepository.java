package com.insd.study.springboot.domain.posts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/* JpaRepository<Posts, Long> : <Entity 클래스, PK 타입>
 * JpaRepository<Posts, Long> 를 상속하면 기본적인 CRUD 메소드가 자동으로 생성됨
 * Entity 클래스와 기본 Entity Repository는 함께 위치해야 함 (같은 package 안에 존재)
 */
public interface PostsRepository extends JpaRepository<Posts, Long> {
    /* SpringDataJpa에서 제공되지 않는 메소드는 아래와 같이 쿼리로 작성해도 됨.
    @Query("select p from Posts p order by p.id desc")
    List<Posts> findAllDesc();
     */
}
