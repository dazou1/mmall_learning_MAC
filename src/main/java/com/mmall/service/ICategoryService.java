package com.mmall.service;

import com.mmall.common.ServiceResponse;
import com.mmall.pojo.Category;

import java.util.List;

/**
 * @Author: dazou
 * @Description:
 * @Date: Create in 上午11:06 18/11/13
 */
public interface ICategoryService {
    ServiceResponse addCategory(String categoryName, Integer parentId);

    ServiceResponse updateCategoryName(Integer categoryId, String categoryName);

    ServiceResponse<List<Category>> getChildrenParallelCategory(Integer categoryId);

    ServiceResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId);

}
