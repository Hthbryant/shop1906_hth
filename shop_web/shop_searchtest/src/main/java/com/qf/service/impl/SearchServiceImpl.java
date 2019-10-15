package com.qf.service.impl;

import com.qf.entity.Goods;
import com.qf.service.ISearchService;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class SearchServiceImpl implements ISearchService {


    @Autowired
    private SolrClient solrClient;

    /**
     * 将商品信息插入索引库
     * @param goods
     * @return
     */
    @Override
    public boolean insertSolr(Goods goods) {

        SolrInputDocument document = new SolrInputDocument();
        document.setField("id",goods.getId());
        document.setField("subject",goods.getSubject());
        document.setField("info",goods.getInfo());
        document.setField("storage",goods.getStorage());
        document.setField("price",goods.getPrice().doubleValue());
        document.setField("images",goods.getFengmian());

        try {
            solrClient.add(document);
            solrClient.commit();
            return true;
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public List<Goods> query(String keyword) {

        SolrQuery solrQuery = new SolrQuery();
        if(keyword == null || keyword == ""){
            solrQuery.setQuery("*:*");
        }else{
            solrQuery.setQuery("subject:"+keyword+" || info:"+keyword);
        }


        try {
            QueryResponse response = solrClient.query(solrQuery);
            SolrDocumentList results = response.getResults();

            List<Goods> goodsList = new ArrayList<>();
            for (SolrDocument document : results) {

                Goods goods = new Goods();

                goods.setId(Integer.parseInt(document.getFieldValue("id")+""));
                goods.setSubject(document.getFieldValue("subject")+"");
                goods.setFengmian(document.getFieldValue("images")+"");
                goods.setPrice(BigDecimal.valueOf((double)document.getFieldValue("price")));
                goods.setStorage((int)document.getFieldValue("storage"));

                goodsList.add(goods);
            }

            return goodsList;
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }
}
