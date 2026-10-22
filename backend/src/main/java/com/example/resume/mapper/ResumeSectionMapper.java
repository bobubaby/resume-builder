package com.example.resume.mapper;

import com.example.resume.entity.ResumeSection;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ResumeSectionMapper {

    @Insert("INSERT INTO resume_section(resume_id, type, title, content, sort_order, visible) "
            + "VALUES(#{resumeId}, #{type}, #{title}, #{content}, #{sortOrder}, #{visible})")
    int insert(ResumeSection section);

    @Select("SELECT * FROM resume_section WHERE resume_id = #{resumeId} ORDER BY sort_order ASC")
    List<ResumeSection> selectByResumeId(Long resumeId);

    @Delete("DELETE FROM resume_section WHERE resume_id = #{resumeId}")
    int deleteByResumeId(Long resumeId);
}
