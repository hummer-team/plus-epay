package com.panli.pay.facade.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class GoodsRequestDto {
    private List<GoodsDetail> goodsLists;

    @Data
    public static class GoodsDetail {
        private String merchantGoodsId;
        private String goodsName;
        private Integer quantity;
        private BigDecimal unitPrice;
    }
}
