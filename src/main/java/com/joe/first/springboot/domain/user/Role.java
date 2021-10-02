package com.joe.first.springboot.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    // spring security에서는 권한 코드에 항상 ROLE이 앞에 있어야 한ㄷ,
    GUEST("ROLE_GUEST","손님"),
    USER("ROLE_USER","일반 사용자");

    private final String key;
    private final String title;
}
