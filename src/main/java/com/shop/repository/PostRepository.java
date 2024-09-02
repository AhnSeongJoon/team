package com.shop.repository;

import com.shop.entity.Post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    /*검색기능-1*/
    Page<Post> findByTitleContaining(String keyword, Pageable pageable);


}
