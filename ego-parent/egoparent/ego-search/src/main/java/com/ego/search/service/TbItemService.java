package com.ego.search.service;

import org.apache.solr.client.solrj.SolrServerException;

import java.io.IOException;
import java.util.Map;

public interface TbItemService {
    /**
     * solr数据初始化
     */
    void init() throws IOException, SolrServerException;

    /**
     * 分页查询
     */
    Map<String, Object> selByQuery(String query, int page, int rows) throws IOException, SolrServerException;

    /**
     * 新增商品到solr
     */
    int add(Map<String, Object> map, String desc) throws IOException, SolrServerException;
}
