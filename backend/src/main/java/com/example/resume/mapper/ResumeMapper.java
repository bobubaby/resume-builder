package com.example.resume.mapper;

import com.example.resume.entity.Resume;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ResumeMapper {

    @Insert("INSERT INTO resume(user_id, title, theme, create_time, update_time) "
            + "VALUES(#{userId}, #{title}, #{theme}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Resume resume);

    @Select("SELECT * FROM resume WHERE user_id = #{userId} ORDER BY update_time DESC")
    List<Resume> selectByUserId(Long userId);

    @Select("SELECT * FROM resume WHERE id = #{id}")
    Resume selectById(Long id);

    @Update("UPDATE resume SET title = #{title}, theme = #{theme}, update_time = NOW() WHERE id = #{id}")
    int update(Resume resume);

    @Delete("DELETE FROM resume WHERE id = #{id}")
    int delete(Long id);
}
