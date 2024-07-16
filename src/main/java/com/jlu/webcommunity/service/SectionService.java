package com.jlu.webcommunity.service;

import com.jlu.webcommunity.entity.Section;

import java.util.List;

public interface SectionService {
    Section getSection(Long id);
    List<Section> getSections();
}
