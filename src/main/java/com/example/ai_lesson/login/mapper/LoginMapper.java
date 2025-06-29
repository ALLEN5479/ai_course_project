package com.example.ai_lesson.login.mapper;

import com.example.ai_lesson.login.entity.LoginEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LoginMapper {

    @Insert("insert into user_msg(user_id,name,password,type) values(#{user_id},#{name},#{password},#{type})")
    int register(LoginEntity loginEntity);

    @Select("select * from user_msg where user_id=#{user_id} and password=#{password}")
    LoginEntity login(String user_id, String password);
}
