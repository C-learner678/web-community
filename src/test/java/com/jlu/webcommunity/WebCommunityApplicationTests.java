package com.jlu.webcommunity;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Slf4j
class WebCommunityApplicationTests {
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Test
    public void test() {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        MultiMatchQueryBuilder multiMatchQueryBuilder
                = QueryBuilders.multiMatchQuery("九九九","title", "content");
        searchSourceBuilder.query(multiMatchQueryBuilder);
        //searchSourceBuilder.size(10);
        SearchRequest searchRequest = new SearchRequest(new String[]{"post"}, searchSourceBuilder);
        SearchResponse searchResponse = null;
        try {
            searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        searchSourceBuilder.query(multiMatchQueryBuilder);
        SearchHits hits = searchResponse.getHits();
        SearchHit[] hitsList = hits.getHits();
        List<Long> ids = new ArrayList<>();
        for (SearchHit documentFields : hitsList) {
            ids.add(Long.parseLong(documentFields.getId()));
        }
        if (ObjectUtils.isEmpty(ids)) {

        }
        System.out.println();
    }
}
