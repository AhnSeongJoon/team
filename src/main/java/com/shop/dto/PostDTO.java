package com.shop.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PostDTO {
    private Long id;
    private String title;
    private String content;
    private String authorName; // 작성자 이름을 저장하기 위한 필드
    private int viewCount;
    private LocalDateTime createdDate;

    public PostDTO(Long id, String title, String content, String authorName, int viewCount, LocalDateTime createdDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.authorName = authorName;
        this.viewCount = viewCount;
        this.createdDate = createdDate;
    }

    // 각 필드에 대한 getter 메서드
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getAuthorName() {
        return authorName;
    }

    public int getViewCount() {
        return viewCount;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
}