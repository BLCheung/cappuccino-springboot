package com.blcheung.cappuccino.service;

import com.blcheung.cappuccino.model.SaleExplain;
import com.blcheung.cappuccino.repository.SaleExplainRepository;
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
