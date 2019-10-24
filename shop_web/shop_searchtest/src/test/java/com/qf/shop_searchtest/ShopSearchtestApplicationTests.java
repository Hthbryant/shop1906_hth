package com.qf.shop_searchtest;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

    @RunWith(SpringRunner.class)
    @SpringBootTest
    public class ShopSearchtestApplicationTests {

        @Autowired
        private SolrClient solrClient;

        @Test
        public void del() {

            try {
                solrClient.deleteByQuery("*:*");
                solrClient.commit();
            } catch (SolrServerException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        @Test
        public void delById(){
            try {
                solrClient.deleteById("8");
                solrClient.commit();
            } catch (SolrServerException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
