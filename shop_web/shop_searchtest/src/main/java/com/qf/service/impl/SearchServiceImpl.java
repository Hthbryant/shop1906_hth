package com.qf.service.impl;

import com.qf.entity.Goods;
import com.qf.service.ISearchService;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SearchServiceImpl implements ISearchService {


    @Autowired
    private SolrClient solrClient;

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
}
