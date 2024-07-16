package com.jlu.webcommunity.util;

import lombok.Data;

@Data
public class PageParam {
    private int offset;
    private int limit;

    public static PageParam getInstance(int pageNum, int pageSize){
        PageParam pageParam = new PageParam();
        pageParam.setLimit(pageSize);
        pageParam.setOffset((pageNum - 1) * pageSize);
        return pageParam;
    }
}
