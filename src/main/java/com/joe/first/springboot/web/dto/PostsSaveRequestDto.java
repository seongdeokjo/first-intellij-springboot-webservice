package com.joe.first.springboot.web.dto;

import com.joe.first.springboot.domain.posts.Posts;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// Entity 클래스와 거의 유사한 dto 클래스를 생성한 이유 -> Entity 클래스를 Request/Response 클래스로 사용하지 않기 위해
// Entity 클래스는 데이터 베이스와 맞닿은 핵심 클래스 이기 때문에 Entity 클래스를 기준으로 테이블이 생성,스키마가 변경된다.
// Request 와 Response 용 dto는 view를 위한 클래스라 자주 변경이 필요하다.
// view Layer와 DB Layer의 역활 분리를 철저하게 하는 것이 좋다.
@Getter
@NoArgsConstructor
public class PostsSaveRequestDto {
    private String title;
    private String content;
    private String author;

    @Builder
    public PostsSaveRequestDto(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }


    public Posts toEntity() {
        return Posts.builder()
                .tilte(title)
                .content(content)
                .author(author)
                .build();
    }
}
