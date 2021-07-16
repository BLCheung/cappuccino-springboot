package com.blcheung.missyou.api.v1;

import com.blcheung.missyou.model.SaleExplain;
import com.blcheung.missyou.service.SaleExplainServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sale_explain")
public class SaleExplainController {
    @Autowired
    private SaleExplainServices saleExplainServices;

    /**
     * 商品补充说明
     *
     * @return
     */
    @GetMapping("/all")
    public List<SaleExplain> getAllSaleExplain() { return this.saleExplainServices.getAllSaleExplain(); }
}
