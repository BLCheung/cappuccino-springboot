package com.blcheung.missyou.service;

import com.blcheung.missyou.model.SaleExplain;
import com.blcheung.missyou.repository.SaleExplainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SaleExplainServices {
    @Autowired
    private SaleExplainRepository saleExplainRepository;

    /**
     * 获取所有商品补充说明
     *
     * @return
     */
    public List<SaleExplain> getAllSaleExplain() {
        return this.saleExplainRepository.findAll();
    }
}
