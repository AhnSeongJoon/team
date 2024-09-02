package com.shop.service;



import com.shop.entity.Post;
import com.shop.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
@Transactional
@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;



    // 포스트 서비스에서 사용할 이미지 업로드 메서드
    public String uploadImage(MultipartFile imageFile) throws IOException {
        // 예시로 간단하게 이미지를 저장하고 URL 을 반환하는 로직을 추가합니다.

        // 이미지를 저장할 디렉토리 경로 설정 (실제로는 설정 파일에서 관리하는 것이 좋습니다)
        String uploadDir = "/path/to/upload/directory/";

        // 원본 파일 이름 가져오기
        String originalFilename = imageFile.getOriginalFilename();

        // 저장할 파일 경로 설정
        String filePath = uploadDir + originalFilename;

        // 이미지 파일을 지정된 경로에 저장
        File dest = new File(filePath);
        imageFile.transferTo(dest);

        // 저장된 이미지의 URL 반환 (실제 서버 환경에 따라 URL 형식을 적절히 조정하세요)
        return "/uploaded-images/" + originalFilename;
    }


    // 제목을 기준으로 게시물 검색 후 페이지네이션 처리
    public Page<Post> findByTitle(String keyword, Pageable pageable) {
        return postRepository.findByTitleContaining(keyword, pageable);
    }
    // 게시물의 조회수를 증가시키는 메소드
    @Transactional
    public void incrementViewCount(Long id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            post.setViewCount(post.getViewCount() + 1);
            postRepository.save(post);
        } else {
            throw new EntityNotFoundException("id " + id + "에 해당하는 게시물을 찾을 수 없습니다.");
        }
    }


    // 게시물을 업데이트하는 메소드
    @Transactional
    public void updatePost(Long id, Post updatedPost) {
        Post existingPost = postRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("id " + id + "에 해당하는 게시물을 찾을 수 없습니다."));
        existingPost.setTitle(updatedPost.getTitle());
        existingPost.setContent(updatedPost.getContent());
        // 작성일자와 조회수는 변경하지 않음
        postRepository.save(existingPost);
    }
}
