package com.ego.search.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.commons.pojo.TbItemChild;
import com.ego.dubbo.service.TbItemCatDubboService;
import com.ego.dubbo.service.TbItemDescDubboService;
import com.ego.dubbo.service.TbItemDubboService;
import com.ego.pojo.TbItem;
import com.ego.pojo.TbItemCat;
import com.ego.pojo.TbItemDesc;
import com.ego.search.service.TbItemService;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TbItemServiceImpl implements TbItemService {
    @Reference
    private TbItemDubboService tbItemDubboServiceImpl;
    @Reference
    private TbItemCatDubboService tbItemCatDubboServiceImpl;
    @Reference
    private TbItemDescDubboService tbItemDescDubboServiceImpl;
    @Resource
    private CloudSolrClient solrClient;

    @Override
    public void init() throws IOException, SolrServerException {
        solrClient.deleteByQuery("*:*");
        //查询所有在售的商品
        List<TbItem> items = tbItemDubboServiceImpl.selAllByStatus((byte) 1);
        for (TbItem item : items) {
            //查询商品对应的类目信息
            TbItemCat cat = tbItemCatDubboServiceImpl.selById(item.getCid());
            //查询商品对应的描述信息
            TbItemDesc desc = tbItemDescDubboServiceImpl.selByItemId(item.getId());
            SolrInputDocument document = new SolrInputDocument();
            document.setField("id", item.getId());
            document.setField("item_title", item.getTitle());
            document.setField("item_sell_point", item.getSellPoint());
            document.setField("item_price", item.getPrice());
            document.setField("item_image", item.getImage());
            document.setField("item_category_name", cat.getName());
            document.setField("item_desc", desc.getItemDesc());
            document.setField("item_updated", desc.getUpdated());
            solrClient.add(document);
        }
        solrClient.commit();
    }

    @Override
    public Map<String, Object> selByQuery(String query, int page, int rows) throws IOException, SolrServerException {
        SolrQuery params = new SolrQuery();
        //设置分页条件
        params.setStart(rows * (page - 1));
        params.setRows(rows);
        //设置条件
        params.setQuery("item_keywords:" + query);
        //设置高亮
        params.setHighlight(true);
        params.addHighlightField("item_title");
        params.setHighlightSimplePre("<span style='color:red'/>");
        params.setHighlightSimplePost("</span");
        //添加排序
        params.setSort("item_updated", SolrQuery.ORDER.desc);
        QueryResponse response = solrClient.query(params);
        List<TbItemChild> listChild = new ArrayList<>();
        //未高亮内容
        SolrDocumentList listSolr = response.getResults();
        Map<String, Map<String, List<String>>> map = response.getHighlighting();
        for (SolrDocument document : listSolr) {
            TbItemChild child = new TbItemChild();
            child.setId(Long.parseLong(document.getFieldValue("id").toString()));
            List<String> list = map.get(document.getFieldValue("id")).get("item_title");
            if (list != null && list.size() > 0) {
                child.setTitle(list.get(0));
            } else {
                child.setTitle(document.getFieldValue("item_title").toString());
            }
            child.setPrice((Long) document.getFieldValue("item_price"));
            Object item_images = document.getFieldValue("item_image");
            child.setImages(item_images == null || item_images.equals("") ? new String[1] : item_images.toString().split(","));
            child.setSellPoint(document.getFieldValue("item_sell_point").toString());
            listChild.add(child);
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("itemList", listChild);
        resultMap.put("totalPages", listSolr.getNumFound() % rows == 0 ? listSolr.getNumFound() / rows : listSolr.getNumFound() / rows + 1);
        return resultMap;
    }

    @Override
    public int add(Map<String, Object> map, String desc) throws IOException, SolrServerException {
        SolrInputDocument document = new SolrInputDocument();
        document.setField("id", map.get("id"));
        document.setField("item_title", map.get("title"));
        document.setField("item_sell_point", map.get("sellPoint"));
        document.setField("item_price", map.get("price"));
        document.setField("item_image", map.get("image"));
        document.setField("item_category_name", tbItemCatDubboServiceImpl.selById((Integer) map.get("cid")).getName());
        document.setField("item_desc", desc);
        UpdateResponse response = solrClient.add(document);
        solrClient.commit();
        if (response.getStatus() == 0) {
            return 1;
        }
        return 0;
    }
}
