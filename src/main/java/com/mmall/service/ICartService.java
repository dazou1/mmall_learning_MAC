package com.mmall.service;

import com.mmall.common.ServiceResponse;
import com.mmall.vo.CartVo;

/**
 * @Author: dazou
 * @Description:
 * @Date: Create in 下午2:47 18/11/20
 */
public interface ICartService {

    ServiceResponse<CartVo> add(Integer userId, Integer productId, Integer count);

    ServiceResponse<CartVo> update(Integer userId, Integer productId, Integer count);

    ServiceResponse<CartVo> deleteProduct(Integer userId, String productIds);

    ServiceResponse<CartVo> list(Integer userId);

    public ServiceResponse<CartVo> selectOrUnSelect(Integer userId,Integer productId, Integer checked);

    ServiceResponse<Integer> getCartProductCount(Integer userId);
}
