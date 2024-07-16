package com.jlu.webcommunity.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jlu.webcommunity.entity.Section;
import com.jlu.webcommunity.mapper.SectionMapper;
import org.springframework.stereotype.Repository;

@Repository
public class SectionDao extends ServiceImpl<SectionMapper, Section> {

}
