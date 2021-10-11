package com.joe.first.springboot.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joe.first.springboot.domain.posts.Posts;
import com.joe.first.springboot.domain.posts.PostsRepository;
import com.joe.first.springboot.web.dto.PostsSaveRequestDto;
import com.joe.first.springboot.web.dto.PostsUpdateRequestDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
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

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @BeforeEach
    public void setup(){
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @AfterEach
    public void tearDown() {
        postsRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "USER") // 인증된 모의(가짜) 사용자를 만들어서 사용 , roles에 권한을 추가할 수 있다. 
                                // 즉, 이 어노테이션으로 인해 ROLE_USER 권한을 가진 사용자가 API를 요청하는 것과 동일한 효과를 가진다
    public void Posts_등록된다() throws Exception{
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
        mvc.perform(post(url) // mvc.perform() -> 생성된 MockMvc를 통해 API를 테스트
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        //then

        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getContent()).isEqualTo(content);

    }

    @Test
    @WithMockUser(roles = "USER")
    public void Posts_수정된다() throws Exception{
        // given
        // 임의의 게시글 등록
        Posts savedPosts = postsRepository.save(Posts.builder()
                .title("title")
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
        mvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        //then 결과
//        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK); // 전송상태 확인
//        assertThat(responseEntity.getBody()).isGreaterThan(0L); // 게시글 등록 상태 확인

        List<Posts> all = postsRepository.findAll(); // 등록된 게시글 가져오기
        assertThat(all.get(0).getTitle()).isEqualTo(expectedTitle); // 수정 변수와 실제로 db에서 수정 명령이 잘 되었는지 확인
        assertThat(all.get(0).getContent()).isEqualTo(expectedContent);
    }

}
