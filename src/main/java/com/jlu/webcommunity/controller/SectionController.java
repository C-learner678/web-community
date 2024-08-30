package com.jlu.webcommunity.controller;

import com.jlu.webcommunity.entity.Section;
import com.jlu.webcommunity.entity.dto.section.GetSectionDto;
import com.jlu.webcommunity.enums.StatusCodeEnum;
import com.jlu.webcommunity.core.result.Result;
import com.jlu.webcommunity.service.SectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/section")
public class SectionController {
    @Autowired
    private SectionService sectionService;

    //获取某个版块的信息
    @PostMapping("getSection")
    public Result getSection(@RequestBody GetSectionDto dto){
        Section section = sectionService.getSection(dto.getSectionId());
        if(section != null){
            return Result.success(section);
        }
        return Result.fail(StatusCodeEnum.SECTION_NOT_EXIST);
    }

    //获取各个版块的信息
    @PostMapping("getSections")
    public Result getSections(){
        List<Section> sections = sectionService.getSections();
        return Result.success(sections);
    }
}
