package com.mmall.controller.portal;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServiceResponse;
import com.mmall.service.IProductService;
import com.mmall.vo.ProductDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: dazou
 * @Description:
 * @Date: Create in 下午5:07 18/11/14
 */

@Controller
@RequestMapping("/product/")
public class ProductController {
    @Autowired
    private IProductService iProductService;

    //前端商品详情、列表、搜索、动态排序
    @RequestMapping("detail.do")
    @ResponseBody
    public ServiceResponse<ProductDetailVo> detail(Integer productId) {
        return iProductService.getProductDetail(productId);
    }

    @RequestMapping(value = "/{productId}", method = RequestMethod.GET)
    @ResponseBody
    public ServiceResponse<ProductDetailVo> detailRESTFul(@PathVariable Integer productId) {
        return iProductService.getProductDetail(productId);
    }

    //用户端根据关键字和产品分类号进行搜索
    @RequestMapping("list.do")
    @ResponseBody
    public ServiceResponse<PageInfo> list(@RequestParam(value = "keyword", required = false)String keyword,
                                          @RequestParam(value = "categoryId", required = false)Integer categoryId,
                                          @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                          @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                          @RequestParam(value = "orderBy", defaultValue = "") String orderBy){
        return iProductService.getProductByKeywordCategory(keyword,categoryId,pageNum,pageSize,orderBy);
    }

    //http://www.happymmall.com/product/手机/100012/1/10/price_asc
    //每一项参数都是必须要填的，因为RESFul格式要准确定位到一个服务端资源，这与需求不一致，因为keyword、categoryId只需一项就可以查询
    @RequestMapping(value = "/{keyword}/{categoryId}/{pageNum}/{pageSize}/{orderBy}", method = RequestMethod.GET)
    @ResponseBody
    public ServiceResponse<PageInfo> listRESFul(@PathVariable(value = "keyword")String keyword,
                                                @PathVariable(value = "categoryId")Integer categoryId,
                                                @PathVariable(value = "pageNum") int pageNum,
                                                @PathVariable(value = "pageSize") int pageSize,
                                                @PathVariable(value = "orderBy") String orderBy){
        return iProductService.getProductByKeywordCategory(keyword,categoryId,pageNum,pageSize,orderBy);
    }

    //以下两种方式的请求会出现模棱两可的结果，因为在客户端输入内容后，服务端并不知道第一个数据是categoryId还是keyword
    //http://www.happymmall.com/product/100012/1/10/price_asc
    @RequestMapping(value = "/{categoryId}/{pageNum}/{pageSize}/{orderBy}", method = RequestMethod.GET)
    @ResponseBody
    public ServiceResponse<PageInfo> listRESFulBadcase(@PathVariable(value = "categoryId")Integer categoryId,
                                                @PathVariable(value = "pageNum") int pageNum,
                                                @PathVariable(value = "pageSize") int pageSize,
                                                @PathVariable(value = "orderBy") String orderBy){
        return iProductService.getProductByKeywordCategory("",categoryId,pageNum,pageSize,orderBy);
    }

    //http://www.happymmall.com/product/手机/1/10/price_asc
    @RequestMapping(value = "/{keyword}/{pageNum}/{pageSize}/{orderBy}", method = RequestMethod.GET)
    @ResponseBody
    public ServiceResponse<PageInfo> listRESFulBadcase(@PathVariable(value = "keyword")String keyword,
                                                       @PathVariable(value = "pageNum") int pageNum,
                                                       @PathVariable(value = "pageSize") int pageSize,
                                                       @PathVariable(value = "orderBy") String orderBy){
        return iProductService.getProductByKeywordCategory(keyword,null,pageNum,pageSize,orderBy);
    }

    //以下两种方式是正确的，在出现模棱两可的异常时，可以自定义资源占位解决这个问题
    //http://www.happymmall.com/product/手机/1/10/price_asc
    @RequestMapping(value = "/keyword/{keyword}/{pageNum}/{pageSize}/{orderBy}", method = RequestMethod.GET)
    @ResponseBody
    public ServiceResponse<PageInfo> listRESFul(@PathVariable(value = "keyword")String keyword,
                                                       @PathVariable(value = "pageNum") int pageNum,
                                                       @PathVariable(value = "pageSize") int pageSize,
                                                       @PathVariable(value = "orderBy") String orderBy){
        return iProductService.getProductByKeywordCategory(keyword,null,pageNum,pageSize,orderBy);
    }

    //http://www.happymmall.com/product/100012/1/10/price_asc
    @RequestMapping(value = "/categoryId/{categoryId}/{pageNum}/{pageSize}/{orderBy}", method = RequestMethod.GET)
    @ResponseBody
    public ServiceResponse<PageInfo> listRESFul(@PathVariable(value = "categoryId")Integer categoryId,
                                                       @PathVariable(value = "pageNum") int pageNum,
                                                       @PathVariable(value = "pageSize") int pageSize,
                                                       @PathVariable(value = "orderBy") String orderBy){
        return iProductService.getProductByKeywordCategory("",categoryId,pageNum,pageSize,orderBy);
    }
}
