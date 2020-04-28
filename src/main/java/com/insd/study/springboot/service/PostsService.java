package com.insd.study.springboot.service;

import com.insd.study.springboot.domain.posts.Posts;
import com.insd.study.springboot.domain.posts.PostsRepository;
import com.insd.study.springboot.web.dto.PostsListResponseDto;
import com.insd.study.springboot.web.dto.PostsResponseDto;
import com.insd.study.springboot.web.dto.PostsSaveRequestDto;
import com.insd.study.springboot.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.description.type.TypeDefinition;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor                                    // bombok을 사용하여 별도로 @Autowired를 사용하지 않음.
@Service
public class PostsService {
    public final PostsRepository postsRepository;           // final이 선언된 모든 필드를 인자로 하는 생성자 생성됨

    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        /* JPA의 영속성 컨텍스트
         * Spring Data JPA를 사용하면 JPA의 엔티티 매니저가 활성화됨
         * 이 상태에서 데이터 변경하면 트랜잭션이 끝나는 시점에 해당 테이블에 변경분을 반영함
         * 즉, Entity 객체의 값만 변경하면 별도로 Update 쿼리를 날릴 필요가 없다. (더티체킹[dirty checking] 개념)
         */
        posts.update(requestDto.getTitle(), requestDto.getContent());
        return id;
    }

    public PostsResponseDto findById(long id) {
        Posts entity = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        return new PostsResponseDto(entity);
    }

    @Transactional(readOnly = true)         // 트랜잭션 범위는 유지하되, 조회 기능만 남겨두어 조회속도가 개선 (조회용 메소드에 사용)
    public List<PostsListResponseDto> fineAllDesc() {
        return postsRepository.findAll().stream()
                .map(PostsListResponseDto::new)         // 람다식 =: .map(posts -> new PostsListResponseDto(posts))
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long id) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        /* deleleById 메소드를 이용하면 id로 삭제 가능하나
         * 존재하는 Posts 인지 확인을 위해 엔티티 조회 후 삭제 하였음.
         */
        postsRepository.delete(posts);
    }
}
