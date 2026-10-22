package com.example.resume.entity;

import lombok.Data;

@Data
public class ResumeSection {

    private Long id;
    private Long resumeId;
    /** basic / education / experience / project / skill / markdown */
    private String type;
    private String title;
    /** JSON 字符串，结构随 type 不同 */
    private String content;
    private Integer sortOrder;
    private Integer visible;
}
