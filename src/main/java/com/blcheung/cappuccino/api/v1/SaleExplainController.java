package com.blcheung.cappuccino.api.v1;

import com.blcheung.cappuccino.common.Result;
import com.blcheung.cappuccino.kit.ResultKit;
import com.blcheung.cappuccino.model.SaleExplain;
import com.blcheung.cappuccino.service.SaleExplainServices;
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
    public Result<List<SaleExplain>> getAllSaleExplain() {
        return ResultKit.resolve(this.saleExplainServices.getAllSaleExplain());
    }
}
