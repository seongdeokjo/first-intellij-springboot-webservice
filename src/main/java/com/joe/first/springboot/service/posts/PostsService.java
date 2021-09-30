package com.joe.first.springboot.service.posts;

import com.joe.first.springboot.domain.posts.Posts;
import com.joe.first.springboot.domain.posts.PostsRepository;
import com.joe.first.springboot.web.dto.PostsResponseDto;
import com.joe.first.springboot.web.dto.PostsSaveRequestDto;
import com.joe.first.springboot.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        Posts posts = postsRepository.findById(id).orElseThrow(
                            () -> new IllegalArgumentException("해당 게시글이 없습니다. id="+ id));
        // 데이터 베이스에 쿼리를 날리는 부분이 없다, -> JPA의 영속성 컨텍스트 때문이다.
        // 영속성 컨텍스트란, 엔티티를 영구 저장하는 환경으로 일종의 논리적 개념이며
        // JPA의 핵심 내용은 엔티티가 영속성 컨텍스트에 포함되어 있냐 아니냐로 갈린다.
        posts.update(requestDto.getTitle(), requestDto.getContent());

        return id;
    }

    public PostsResponseDto findById(Long id){
        Posts entity = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        return new PostsResponseDto(entity);
    }
}
