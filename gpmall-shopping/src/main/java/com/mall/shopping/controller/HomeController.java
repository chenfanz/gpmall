package com.mall.shopping.controller;

import com.mall.commons.result.ResponseData;
import com.mall.commons.result.ResponseUtil;
import com.gpmall.shopping.IContentService;
import com.gpmall.shopping.IHomeService;
import com.gpmall.shopping.constants.ShoppingRetCode;
import com.gpmall.shopping.dto.HomePageResponse;
import com.gpmall.shopping.dto.NavListResponse;
import com.gpmall.user.annotation.Anoymous;
import com.mall.shopping.IContentService;
import com.mall.shopping.IHomeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *  ciggar
 * create-date: 2019/7/23-17:06
 */
@RestController
@RequestMapping("/shopping")
@Api(tags = "HomeController", description = "导航控制层")
public class HomeController {

    @Reference(timeout = 3000,check = false)
    IContentService contentService;

    @Reference(timeout = 3000,check = false)
    IHomeService iHomeService;

    @Anoymous
    @GetMapping("/navigation")
    @ApiOperation("导航")
    public ResponseData navigation(){
        NavListResponse response=contentService.queryNavList();
        if(response.getCode().equals(ShoppingRetCode.SUCCESS.getCode())) {
            return new ResponseUtil().setData(response.getPannelContentDtos());
        }
        return new ResponseUtil().setErrorMsg(response.getMsg());
    }

    @Anoymous
    @GetMapping("/homepage")
    @ApiOperation("主页")
    public ResponseData homepage(){
        HomePageResponse response=iHomeService.homepage();
        if(response.getCode().equals(ShoppingRetCode.SUCCESS.getCode())) {
            return new ResponseUtil().setData(response.getPanelContentItemDtos());
        }
        return new ResponseUtil().setErrorMsg(response.getMsg());
    }


}
