package com.joe.first.springboot.domain.posts;

import org.springframework.data.jpa.repository.JpaRepository;

// 보통 mybatis 등에서 dao라고 불리는 DBLayer 접근자 
// JPA 에선 Repository라고 부르며 인터페이스로 생성한다. JpaRepository<클래스,pk타입>을 상속하면 기본적인 CRUD 가능
// 단, Entity 클래스와 기본 Entry Repository는 함께 위치해야 한다.
public interface PostsRepository extends JpaRepository<Posts,Long> {
}
