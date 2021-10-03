package com.joe.first.springboot.config.auth.dto;

import com.joe.first.springboot.domain.user.User;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {
    // 인증된 사용자 정보만 필요
    // 만약 User 클래스를 그대로 사용했다면 직렬화를 구현하지 않았다는 에러 발생
    // User 클래스에 직렬화 코드를 넣으면 ? User 클래스가 엔티티 이기 때문에 엔티티 클래스는 언제 다른 엔티티와 관계가 형성될지 모른다.
    // 성능 이슈, 부수효과 가 발생할 확률이 높다 그래서 대신 직렬화 기능으 가진 세션 dto 클래스를 하나 추가로 생성
    private String name;
    private String email;
    private String picture;

    public SessionUser(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
    }
}
