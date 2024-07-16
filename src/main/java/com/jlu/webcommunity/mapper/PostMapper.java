package com.jlu.webcommunity.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jlu.webcommunity.entity.Post;
import com.jlu.webcommunity.entity.vo.SelectPostByPageVo;
import com.jlu.webcommunity.util.PageParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PostMapper extends BaseMapper<Post> {
    List<SelectPostByPageVo> selectPostByPage
            (@Param("sectionId") Long sectionId, @Param("userId") Long userId,
             @Param("top") Boolean top, @Param("pageParam") PageParam pageParam);
    Integer selectPostCount
            (@Param("sectionId") Long sectionId, @Param("userId") Long userId, @Param("top") Boolean top);
}
