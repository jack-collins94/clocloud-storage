package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Files;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {

    @Select("SELECT * FROM FILES WHERE userId = #{userId} AND fileName = #{fileName}")
    List<Files> selectByNameAndUserId(Integer userId, String fileName);

    @Select("SELECT * FROM FILES WHERE fileId = #{fileId}")
    Files selectById(Integer fileId);

    @Select("SELECT * FROM FILES WHERE fileName = #{fileName}")
    Files getFileByName(String fileName);

    @Insert("INSERT INTO FILES (fileName, contentType, fileSize, userId, fileData) VALUES(#{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    int insertFile(Files file);

    @Select("SELECT * FROM FILES")
    List<Files> selectAllFiles();

    @Select("SELECT * FROM FILES WHERE userId = #{userId}")
    List<Files> selectAllFilesByUID(Integer userId);

    @Delete("DELETE FROM FILES WHERE fileId = #{fileId}")
    int deleteFile(Integer fileId);


}
