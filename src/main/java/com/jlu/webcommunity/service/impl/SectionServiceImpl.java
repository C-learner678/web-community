package com.jlu.webcommunity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jlu.webcommunity.constant.RedisConstant;
import com.jlu.webcommunity.dao.SectionDao;
import com.jlu.webcommunity.entity.Section;
import com.jlu.webcommunity.service.SectionService;
import com.jlu.webcommunity.core.RedisClient;
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
    private RedisClient redisClient;

    @Override
    public Section getSection(Long id) {
        if(redisClient.hasKey(RedisConstant.sectionKey + id)){
            return (Section) redisClient.get(RedisConstant.sectionKey + id);
        }
        Section section = sectionDao.getById(id);
        if(section == null || section.getDeleted()){
            return null;
        }
        redisClient.set(RedisConstant.sectionKey + id, section, 86400);
        return section;
    }

    @Override
    public List<Section> getSections(){
        if(redisClient.hasKey(RedisConstant.sectionsKey)){
            return (List<Section>) redisClient.get(RedisConstant.sectionsKey);
        }
        QueryWrapper<Section> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("deleted", false);
        queryWrapper.orderByAsc("create_time");
        List<Section> sections = sectionDao.list(queryWrapper);
        redisClient.set(RedisConstant.sectionsKey, sections, 86400);
        return sections;
    }
}
