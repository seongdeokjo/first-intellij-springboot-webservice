package com.joe.first.springboot.domain.posts;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
 // setter 생성 x -> 해당 클래스의 인스턴스 값들이 언제 어디서 변해야 하는지 코드상으로 명확하게 구분할 수 x , 차후 변경시 정말 복잡해짐
// 따라서 Entity 클래스에는 setter 메소드를 만들지 않는다.
@Getter
@NoArgsConstructor // 기본 생성자 자동 추가
@Entity // 테이블과 링크될 클래스임을 나타낸다. / 기본값으로 클래스의 카멜케이스 이름을 언더스코어 네이밍(_)으로 테이블 이름을 매칭
public class Posts {
    @Id // 해당 테이블의 pk 필드를 나타냄
    @GeneratedValue(strategy = GenerationType.IDENTITY) // pk의 생성규칙을 나타낸다. 스프링 부트 2.0버전에서는 이 옵션을 추가해야만 auto_increment가 된다.
    private Long id;

    @Column(length = 500, nullable = false) // 테이블의 칼럼을 나타내며 굳이 선언하지 않더라도 해당 클래스의 필드는 모두 칼럼이 된다.
                                            // 사용하는 이유 -> 기본값 외에 추가로 변경이 필요한 옵션이 있으면 사용
    private String title;

    @Column(columnDefinition = "TEXT" ,nullable = false)
    private String content;
    
    private String author;

    @Builder // 해당 클래스의 빌더 패턴 클래스를 생성 , 생성자 상단에 선언시 생성자에 포함된 필드만 빌더에 포함
    // 어느 필드에 어떤 값을 채워야 할지 명확하게 인지할 수 있다.
    public Posts(String title, String content, String author){
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public void update(String title,String content){
        this.title = title;
        this.content = content;
    }


}
