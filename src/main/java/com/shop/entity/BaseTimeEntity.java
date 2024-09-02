package com.shop.entity;


import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(value = {AuditingEntityListener.class}) //Auditing 을 하기 위해 엔티티 리스너 추가
@MappedSuperclass // 부모 클래스를 상속받는 자식 클래스에 매핑 정보만 제공
@Getter
@Setter
public class BaseTimeEntity {
@CreatedDate // 생성시 자동 저장
    @Column(updatable = false)
    private LocalDateTime regTime; // 등록일

@LastModifiedDate // 변경시 자동 저장
    private  LocalDateTime updateTime; // 수정일
}

