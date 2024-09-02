package com.shop.controller;


import com.shop.entity.Post;
import com.shop.repository.PostRepository;
import com.shop.service.PostService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.UUID;


@Controller
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostService postService;

    @Value("${uploadpath}")
    String uploadPath;

    // 게시판 목록 조회 (페이지네이션 추가)
    @GetMapping("/")
    public String index(@PageableDefault(size = 3, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable,
                        Model model) {

        Page<Post> postPage = postRepository.findAll(pageable);
        long totalPosts = postRepository.count(); // 전체 게시물 수


        // 순번 계산
        int startCount = (int) (pageable.getOffset() + 1);
        model.addAttribute("posts", postPage.getContent());
        model.addAttribute("currentPage", pageable.getPageNumber()); //현재 페이지 번호
        model.addAttribute("totalPages", postPage.getTotalPages());
        model.addAttribute("totalPosts", totalPosts); // 전체 게시물 수를 모델에 추가
        model.addAttribute("startCount", startCount); // 순번 시작 값

        return "post/index"; // templates 폴더 아래에 post/index.html 파일이 있어야 합니다.
    }


    // 게시물 작성 폼
    @GetMapping("/write")
    public String writeForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            // 인증되지 않은 사용자일 경우 로그인 페이지로 리다이렉트
            return "redirect:/members/login?error=unauthorized"; // 로그인 페이지 URL 에 추가적인 에러 파라미터를 전달할 수 있습니다.
        }
        model.addAttribute("post", new Post());
        return "post/write"; // templates 폴더 아래에 post/write.html 파일이 있어야 합니다.
    }

    // 게시물 작성 처리
    @PostMapping("/write")
    public String write(@ModelAttribute Post post,
                        @RequestParam("imageFile") MultipartFile imageFile,
                        RedirectAttributes redirectAttributes) {
        post.setCreatedDate(LocalDateTime.now()); // 작성일 설정

        try {
            // 이미지 파일이 업로드된 경우
            if (!imageFile.isEmpty()) {
                String fileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
                Path filePath = Paths.get(uploadPath.replace("file:///", ""), fileName); // file:/// 제거
                Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                post.setImgUrl(fileName); // 이미지 파일 이름을 imgUrl 로 설정
            } else {
                // 이미지 파일이 업로드되지 않은 경우
                post.setImgUrl(""); // 빈 문자열로 설정하거나 null 로 설정 가능
            }

            postRepository.save(post); // 게시물 저장
            return "redirect:/posts/"; // 글 작성 후 메인 페이지로 리다이렉트
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "파일 업로드 중 오류가 발생했습니다.");
            return "redirect:/posts/write"; // 실패 시 다시 작성 페이지로 리다이렉트
        }
    }

    // 이미지 경로 요청 처리
    @GetMapping("/images/{imgUrl}")
    public String getImage(@PathVariable String imgUrl) {
        // 이미지 경로에 대한 처리를 여기에 추가 (실제 경로에 맞게 수정 필요)
        return "redirect:/images/" + imgUrl; // 실제 이미지 경로로 리다이렉트
    }


    // 개별 게시물 조회
    @GetMapping("/{id}")
    public String read(@PathVariable Long id, Model model) {
        model.addAttribute("post", postRepository.findById(id).orElse(null));
        return "post/read"; // templates 폴더 아래에 post/read.html 파일이 있어야 합니다.
    }

    // 게시물 수정 폼
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("post", postRepository.findById(id).orElse(null));
        return "post/edit"; // templates 폴더 아래에 post/edit.html 파일이 있어야 합니다.
    }

    // 게시물 수정 처리
    @PostMapping("/{id}/edit")
    public String edit(@PathVariable Long id,
                       @ModelAttribute Post post,
                       @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                       RedirectAttributes redirectAttributes) {
        // 기존 게시물 정보 가져오기
        Post existingPost = postRepository.findById(id).orElse(null);
        if (existingPost == null) {
            return "redirect:/posts/";
        }

        try {
            // 이미지 파일이 업로드된 경우
            if (imageFile != null && !imageFile.isEmpty()) {


                // 새 이미지 파일 업로드
                String fileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
                Path filePath = Paths.get(uploadPath.replace("file:///", ""), fileName); // file:/// 제거
                Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                existingPost.setImgUrl(fileName); // 이미지 파일 이름을 imgUrl 로 설정
            }

            // 제목과 내용 업데이트
            existingPost.setTitle(post.getTitle());
            existingPost.setContent(post.getContent());

            // 게시물 저장
            postRepository.save(existingPost);

            return "redirect:/posts/";
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "파일 업로드 중 오류가 발생했습니다.");
            return "redirect:/posts/" + id + "/edit"; // 실패 시 다시 수정 페이지로 리다이렉트
        }
    }

    // 게시물 삭제 처리
    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        postRepository.deleteById(id);
        return "redirect:/posts/";
    }

    // 게시판 메인 페이지
    @GetMapping("/post")
    public String showPostPage(Model model) {
        model.addAttribute("showWriteButton", true); // 글 작성 버튼 보이도록 모델에 속성 추가
        return "post/index"; // templates 폴더 아래에 post/index.html 파일이 있어야 합니다.
    }
    // 제목으로 게시물 검색
    @GetMapping("/search")
    public String searchPosts(@RequestParam("keyword") String keyword,
                              @PageableDefault(size = 3, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable,
                              Model model) {
        Page<Post> searchResults = postService.findByTitle(keyword, pageable);
        long totalPosts = postRepository.count(); // 전체 게시물 수
        // 순번 계산
        int startCount = (int) (pageable.getOffset() + 1);
        model.addAttribute("posts", searchResults.getContent()); // 현재 페이지의 게시물 목록을 가져옴
        model.addAttribute("currentPage", pageable.getPageNumber());
        model.addAttribute("totalPages", searchResults.getTotalPages());
        model.addAttribute("totalPosts", totalPosts); // 전체 게시물 수를 모델에 추가
        model.addAttribute("keyword", keyword); // 검색어를 모델에 추가
        model.addAttribute("startCount", startCount); // 순번 시작 값
        return "post/index"; // 게시물 목록을 보여줄 index.html 페이지
    }


    @PostMapping("/increaseViewCount/{id}")
    public ResponseEntity<Void> increaseViewCount(@PathVariable Long id) {
        try {
            postService.incrementViewCount(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}

