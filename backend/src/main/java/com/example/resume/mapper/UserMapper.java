package com.example.resume.mapper;

import com.example.resume.entity.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Insert("INSERT INTO user(username, password, nickname, token, create_time) "
            + "VALUES(#{username}, #{password}, #{nickname}, #{token}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);

    @Select("SELECT * FROM user WHERE username = #{username}")
    User selectByUsername(String username);

    @Select("SELECT * FROM user WHERE token = #{token}")
    User selectByToken(String token);

    @Select("SELECT * FROM user WHERE id = #{id}")
    User selectById(Long id);

    @Update("UPDATE user SET token = #{token} WHERE id = #{id}")
    int updateToken(@Param("id") Long id, @Param("token") String token);
}
