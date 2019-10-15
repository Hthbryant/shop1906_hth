package com.qf.service;

import com.qf.entity.Goods;
import org.springframework.web.bind.annotation.RequestBody;

public interface ISearchService {

    boolean insertSolr(@RequestBody Goods goods);
}
