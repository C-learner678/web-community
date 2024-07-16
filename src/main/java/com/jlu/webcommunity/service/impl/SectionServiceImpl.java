package com.jlu.webcommunity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jlu.webcommunity.constant.RedisConstant;
import com.jlu.webcommunity.dao.SectionDao;
import com.jlu.webcommunity.entity.Post;
import com.jlu.webcommunity.entity.Section;
import com.jlu.webcommunity.service.SectionService;
import com.jlu.webcommunity.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class SectionServiceImpl implements SectionService {
    @Autowired
    private SectionDao sectionDao;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public Section getSection(Long id) {
        if(redisUtil.hasKey(RedisConstant.sectionKey + id)){
            return (Section) redisUtil.get(RedisConstant.sectionKey + id);
        }
        Section section = sectionDao.getById(id);
        if(section == null || section.getDeleted()){
            return null;
        }
        redisUtil.set(RedisConstant.sectionKey + id, section, 86400);
        return section;
    }

    @Override
    public List<Section> getSections(){
        if(redisUtil.hasKey(RedisConstant.sectionsKey)){
            return (List<Section>) redisUtil.get(RedisConstant.sectionsKey);
        }
        QueryWrapper<Section> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("deleted", false);
        queryWrapper.orderByAsc("create_time");
        List<Section> sections = sectionDao.list(queryWrapper);
        redisUtil.set(RedisConstant.sectionsKey, sections, 86400);
        return sections;
    }
}
