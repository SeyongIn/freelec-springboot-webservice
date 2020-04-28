package com.insd.study.springboot.domain.posts;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter                                                         // 모든 필드의 Getter 메소드 자동 생성
@NoArgsConstructor                                              // 기본 생성자 자동 추가
/* @Entity : 테이블과 매칭될 클래스임을 나타냄 (Entity 클래스)
 * 기본적으로 카멜케이스 이름을 언더스코어 네이밍의 테이블과 매칭
 * Ex) SalesManager.java >> sales_manager table
 * Entity 클래스 : 절대 Setter 메소드를 만들지 않음. 필드 값 변경이 필요하면 그 목적과 의도에 맞는 메소드를 추가함
 * */
@Entity
public class Posts extends BaseTimeEntity {
    @Id                                                         // 해당 테이블의 PK 필드를 나타냄
    @GeneratedValue(strategy = GenerationType.IDENTITY)         // PK 생성 규칙, GenerationType.IDENTITY > auto_increment
    private Long id;

    @Column(length = 500, nullable = false)                     // @Column 테이블의 칼럼
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)        // 칼럼의 속성을 정의할 대 사용
    private String content;

    private String author;                                      // @Column 선언 하지 않아도 모든 필드는 칼럼이 됨

    /* @Builder : 해당 클래스의 빌더 패턴 클래스 생성
     * 생성자 상단에 선언 시 생성자에 포함된 필드만 빌더에 포함
     */
    @Builder
    public Posts(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
