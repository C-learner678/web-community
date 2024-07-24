package com.jlu.webcommunity.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jlu.webcommunity.entity.Post;
import com.jlu.webcommunity.entity.vo.GetPostByPageVo;
import com.jlu.webcommunity.core.PageParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PostMapper extends BaseMapper<Post> {
    List<GetPostByPageVo> selectPostByPage
            (@Param("sectionId") Long sectionId, @Param("userId") Long userId,
             @Param("top") Boolean top, @Param("text") String text, @Param("pageParam") PageParam pageParam);
    Integer selectPostCount
            (@Param("sectionId") Long sectionId, @Param("userId") Long userId,
             @Param("top") Boolean top, @Param("text") String text);
    List<GetPostByPageVo> selectPostByIds
            (@Param("postIds") List<Long> postIds, @Param("pageParam") PageParam pageParam);
}
