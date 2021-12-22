package com.qd.cjb.api.controller;

import com.qd.cjb.common.utils.JSONResult;
import com.qd.cjb.common.utils.MD5Utils;
import com.qd.cjb.pojo.Users;
import com.qd.cjb.pojo.vo.UsersVo;
import com.qd.cjb.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @version : 1.0
 * @author : hjx
 * @Date : 2021/1/24
 * @Description : nuc.edu.hjx.Controller
 */
@RestController
@Api(value = "用户登录注册接口", tags = "注册登录接口测试")
public class RegisterLoginController extends BatisController{

    @Autowired
    private UserService userService;

    /**
     * 用户注册
     * @param user
     * @return JSONResult
     * @throws Exception
     */
    @ApiOperation(value = "用户注册" ,notes = "用户注册的接口")
    @PostMapping("/register")
    public JSONResult Register(@RequestBody Users user) throws Exception {

        //判断用户名和密码不为空
        if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword())) {
            return JSONResult.errorMsg("用户名或密码为空！");
        }
        //判断用户名是否存在
        boolean usernameIsExist = userService.queryUsernameIsExist(user.getUsername());
        //保存用户
        if (!usernameIsExist) {
            user.setNickname(user.getUsername());
            user.setPassword(MD5Utils.getMD5Str(user.getPassword()));
            user.setFansCounts(0);
            user.setFollowCounts(0);
            user.setReceiveLikeCounts(0);
            userService.saveUser(user);
        } else {
            return JSONResult.errorMsg("用户名已经存在！");
        }
        user.setPassword("");
        UsersVo usersVo = getUserToken(user);
        return JSONResult.ok(usersVo);
    }

    /**
     * 用户登录
     * @param user
     * @return JSONResult
     * @throws Exception
     */
    @ApiOperation(value = "用户登录", notes = "用户登录的接口")
    @PostMapping("/login")
    public JSONResult Login(@RequestBody Users user) throws Exception {
        String username = user.getUsername();
        String password = user.getPassword();

        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return JSONResult.errorMsg("用户名密码为空！");
        }
        Users result = userService.queryUserForLogin(username,MD5Utils.getMD5Str(password));

        if (result != null) {
            result.setPassword("");
            UsersVo usersVo = getUserToken(result);
            return JSONResult.ok(usersVo);
        }else {
            return JSONResult.errorMsg("密码错误！");
        }
    }

    /**
     * 获取user令牌
     * @param user
     * @return UserVo
     */
    public UsersVo getUserToken(Users user) {
        String uniquetoken = UUID.randomUUID().toString();
        redisOperator.set(USER_SESSION + ":" + user.getId(),uniquetoken, 1000*60*60*24);
        UsersVo uservo = new UsersVo();
        BeanUtils.copyProperties(user,uservo);
        uservo.setUsertoken(uniquetoken);
        return uservo;
    }

    /**
     * 用户注销
     * @param userId
     * @return JSONResult
     * @throws Exception
     */
    @ApiOperation(value = "用户注销", notes = "用户注销的接口")
    @ApiImplicitParam(name = "userId",value = "用户Id",required = true,dataType = "String",paramType = "query")
    @PostMapping("/logout")
    public JSONResult Logout(String userId) throws Exception {

        if (redisOperator.del(USER_SESSION + ":" + userId)) {
            return JSONResult.ok();
        }else {
            return JSONResult.errorMsg("注销失败，请重试！");
        }
    }
}
