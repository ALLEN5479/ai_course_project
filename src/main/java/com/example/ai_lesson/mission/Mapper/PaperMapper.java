package com.example.ai_lesson.mission.Mapper;

import com.example.ai_lesson.mission.Domain.Paper;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PaperMapper {
    // 查询试卷列表
    @Select("SELECT * FROM paper")
    List<Paper> selectPaperList();

    // 根据ID查询试卷
    @Select("SELECT * FROM paper WHERE paper_id = #{paperId}")
    Paper selectPaperById(int paperId);

    // 新增试卷
    @Insert("INSERT INTO paper(paper_name) VALUES(#{paperName})")
    @Options(useGeneratedKeys = true, keyProperty = "paper_id")
    int insertPaper(Paper paper);

    // 更新试卷
    @Update("UPDATE paper SET paper_name=#{paperName} WHERE paper_id=#{paperId}")
    int updatePaper(Paper paper);

    // 删除试卷
    @Delete("DELETE FROM paper WHERE paper_id=#{paperId}")
    int deletePaper(int paperId);
}
