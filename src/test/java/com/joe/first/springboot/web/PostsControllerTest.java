package com.joe.first.springboot.web;

import com.joe.first.springboot.domain.posts.Posts;
import com.joe.first.springboot.domain.posts.PostsRepository;
import com.joe.first.springboot.web.dto.PostsSaveRequestDto;
import com.joe.first.springboot.web.dto.PostsUpdateRequestDto;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// @WebMvcTest를 사용 x -> JPA 기능이 작동하지 않기 때문에, Controller,ControllerAdvice 등 외부 연동과 관련된 부분만 활성화 됨
// 따라서 JPA 기능까지 한번에 테스트 할 때에는 @SpringBootTest 와 TestRestTemplate을 사용해야 함
public class PostsControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private PostsRepository postsRepository;

    @After
    public void tearDown() {
        postsRepository.deleteAll();
    }

    @Test
    public void Posts_등록된다() {
        //given
        String title = "title";
        String content = "content";
        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                .title(title)
                .content(content)
                .author("author")
                .build();
        String url = "http://localhost:"+port+"/api/v1/posts";

        //when
        ResponseEntity<Long> responseEntity = testRestTemplate.postForEntity(url,requestDto,Long.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);
        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTilte()).isEqualTo(title);
        assertThat(all.get(0).getContent()).isEqualTo(content);

    }

    @Test
    public void Posts_수정된다() throws Exception{
        // given
        // 임의의 게시글 등록
        Posts savedPosts = postsRepository.save(Posts.builder()
                .tilte("title")
                .content("content")
                .author("author")
                .build());
        // 등록된 게시글의 번호 가져오기
        Long updateId = savedPosts.getId();
        // 수정 테스트할 변수 저장
        String expectedTitle = "title2";
        String expectedContent = "content2";
        // 수정 요청을 위한 dto 맵핑
        PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
                .title(expectedTitle)
                .content(expectedContent)
                .build();
        // url
        String url = "http://localhost:"+port+"/api/v1/posts/"+updateId;
        // 요청 엔티티 저장
        HttpEntity<PostsUpdateRequestDto> requestEntity = new HttpEntity<>(requestDto);

        //when
        // 수정 기능 수행
        ResponseEntity<Long> responseEntity = testRestTemplate.exchange(url, HttpMethod.PUT, requestEntity, Long.class);

        //then 결과
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK); // 전송상태 확인
        assertThat(responseEntity.getBody()).isGreaterThan(0L); // 게시글 등록 상태 확인

        List<Posts> all = postsRepository.findAll(); // 등록된 게시글 가져오기
        assertThat(all.get(0).getTilte()).isEqualTo(expectedTitle); // 수정 변수와 실제로 db에서 수정 명령이 잘 되었는지 확인
        assertThat(all.get(0).getContent()).isEqualTo(expectedContent);
    }

}
