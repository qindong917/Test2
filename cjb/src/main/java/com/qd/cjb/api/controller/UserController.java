package com.qd.cjb.api.controller;

import com.qd.cjb.common.utils.JSONResult;
import com.qd.cjb.pojo.Users;
import com.qd.cjb.pojo.vo.PublishVoByLike;
import com.qd.cjb.pojo.vo.UsersVo;
import com.qd.cjb.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @version : 1.0
 * @author : hjx
 * @Date : 2021/1/29
 * @Description : nuc.edu.hjx.Controller
 */
@RestController
@RequestMapping("/user")
@Api(value = "用户相关业务", tags = "用户相关业务的接口")
public class UserController extends BatisController{

    @Autowired
    private UserService userService;


    @ApiOperation(value = "用户头像上传", notes = "文件上传接口")
    @ApiImplicitParam(name = "userId",value = "用户Id",required = true,dataType = "String",paramType = "query")
    @PostMapping("/uploadface")
    public JSONResult uploadFile(String userId, @RequestParam("file") MultipartFile file) {

        if (StringUtils.isBlank(userId)) {
            return JSONResult.errorMsg("账号不能为空！");
        }
        String uploadDB = "/user/" + userId + "/face/";
        String filename = file.getOriginalFilename();
        if (file != null) {
            if (StringUtils.isNotBlank(filename)) {
                File face = new File(USER_PATH + uploadDB + filename);
                if (face.getParentFile() !=null && !face.getParentFile().isDirectory()) {
                    face.getParentFile().mkdirs();
                }
                try {
                    FileCopyUtils.copy(file.getInputStream(), new FileOutputStream(USER_PATH + uploadDB + filename));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Users user = new Users();
                user.setId(userId);
                user.setFaceImage(uploadDB + filename);
                userService.updateUser(user);
                return JSONResult.ok(uploadDB + filename);
            }
            return JSONResult.errorMsg("上传头像失败！");
        }
        return JSONResult.errorMsg("上传头像失败！");
    }

    @ApiOperation(value = "查询用户信息", notes = "查询用户接口")
    @ApiImplicitParam(name = "userId",value = "用户Id",required = true,dataType = "String",paramType = "query")
    @PostMapping("/queryuser")
    public JSONResult queryUserInfo(String userId, String fansId) {
        if(StringUtils.isBlank(userId)) {
            return JSONResult.errorMsg("用户账号不能为空！");
        }
        Users users = userService.queryUserInfo(userId);
        UsersVo usersVo = new UsersVo();
        BeanUtils.copyProperties(users,usersVo);
        //userId为视频的发布者而fansId为此时的登录用户
        usersVo.setIsfollow(userService.isFollow(fansId,userId));
        return JSONResult.ok(usersVo);
    }

    @ApiOperation(value = "显示用户是否喜欢",notes = "显示喜欢接口")
    @PostMapping("/likevideo")
    public JSONResult isLikeVideo(String loginId, String publishId, String videoId) {
        if (StringUtils.isBlank(loginId)) {
            return JSONResult.errorMsg("请重新登录");
        }
        Users user = userService.queryUserInfo(publishId);
        UsersVo usersVo = new UsersVo();
        BeanUtils.copyProperties(user,usersVo);
        boolean islike = userService.isLikeVideo(loginId,videoId);

        PublishVoByLike publishVoByLike = new PublishVoByLike();
        publishVoByLike.setUsersVo(usersVo);
        publishVoByLike.setIslike(islike);
        return JSONResult.ok(publishVoByLike);
    }

    @ApiOperation(value = "关注视频", notes = "关注视频接口")
    @PostMapping("/fans")
    public JSONResult becameUserFans(String userId, String fansId) {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(fansId)) {
            return JSONResult.errorMsg("请重新登录");
        }
        userService.becameFans(userId,fansId);
        return JSONResult.ok();
    }

    @ApiOperation(value = "取消关注", notes = "取消关注接口")
    @PostMapping("/unfans")
    public JSONResult cancelAttention(String userId, String fansId) {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(fansId)) {
            return JSONResult.errorMsg("请重新登录");
        }
        userService.cancelAttention(userId,fansId);
        return JSONResult.ok();
    }
}
