package com.qf.service;

import com.qf.entity.Goods;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface ISearchService {

    boolean insertSolr(@RequestBody Goods goods);

    List<Goods> query(String keyword);
}
