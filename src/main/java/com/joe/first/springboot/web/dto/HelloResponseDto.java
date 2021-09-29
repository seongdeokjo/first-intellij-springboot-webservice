package com.joe.first.springboot.web.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor // 선언된 모든 final 필드가 포함된 생성자르 생성 , final이 없는 필드는 생성자에 포함 x
public class HelloResponseDto {
    private final String name;
    private final int amount;
}
