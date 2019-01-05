package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServiceResponse;
import com.mmall.vo.OrderVo;

import java.util.Map;

/**
 * @Author: dazou
 * @Description:
 * @Date: Create in 下午4:57 18/11/23
 */
public interface IOrderService {

    ServiceResponse pay(Long orderNo, Integer userId, String path);

    ServiceResponse aliCallback(Map<String, String> params);

    ServiceResponse queryOrderPayStatus(Integer userId, Long orderNo);

    ServiceResponse createOrder(Integer userId, Integer shippingId);

    ServiceResponse<String> cancelOrder(Integer userId, Long orderNo);

    ServiceResponse getOrderCartProduct(Integer userId);

    ServiceResponse<OrderVo> getOrderDetail(Integer userId, Long orderNo);

    ServiceResponse<PageInfo> getOrderList(Integer userId, int pageNum, int pageSize);

    //backend
    ServiceResponse<PageInfo> manageList(int pageNum, int pageSize);

    ServiceResponse<OrderVo> manageDetail(Long orderNo);

    ServiceResponse<PageInfo> manageSearch(Long orderNo, int pageNum, int pageSize);

    ServiceResponse<String> manageSendGoods(Long orderNo);

    //hour个小时以内未付款的订单，进行关闭
    void closeOrder(int hour);
}
