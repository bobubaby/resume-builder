package com.example.resume.vo;

import com.example.resume.entity.Resume;
import com.example.resume.entity.ResumeSection;
import lombok.Data;

import java.util.List;

@Data
public class ResumeDetailVO {

    private Resume resume;
    private List<ResumeSection> sections;
}
