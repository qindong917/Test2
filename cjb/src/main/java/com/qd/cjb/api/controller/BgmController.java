package com.qd.cjb.api.controller;

import com.qd.cjb.common.utils.JSONResult;
import com.qd.cjb.service.BgmService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @version : 1.0
 * @auther : hjx
 * @Date : 2021/2/1
 * @Description : nuc.edu.hjx.controller
 */
@RestController
@RequestMapping("/bgm")
@Api(value = "背景音乐接口", tags = "背景音乐的测试接口")
public class BgmController {

    @Autowired
    private BgmService bgmService;

    /**
     * 查询所有的bgm
     * @return List
     */
    @ApiOperation(value = "查询所有的背景音乐", notes = "查询所有gbm的接口")
    @PostMapping("/bgmlist")
    public JSONResult queryAllBgm() {
        return JSONResult.ok(bgmService.queryAllBgm());
    }

}
