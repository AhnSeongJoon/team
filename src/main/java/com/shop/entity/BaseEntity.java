package com.shop.entity;


import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(value = {AuditingEntityListener.class})
@MappedSuperclass // 부모 클래스를 상속받는 자식 클래스에 매핑 정보만 제공
@Getter
public class BaseEntity extends BaseTimeEntity { // 등록일 수정일은 상속
    @CreatedBy
    @Column(updatable = false)
    private String createdBy; // 등록자

    @LastModifiedBy
    private String modifiedBy; // 수정자
}
