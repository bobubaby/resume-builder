package com.example.resume.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Resume {

    private Long id;
    private Long userId;
    private String title;
    private String theme;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
