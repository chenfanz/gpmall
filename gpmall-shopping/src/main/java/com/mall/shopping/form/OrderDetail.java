package com.mall.shopping.form;

import com.gpmall.order.dto.OrderItemDto;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 *  ciggar
 * create-date: 2019/8/8-14:02
 */
@Data
@ApiModel
public class OrderDetail {
    private String userName;
    private BigDecimal orderTotal;
    private long userId;
    private List<OrderItemDto> goodsList;
    private String tel;
    private String streetName;
}
