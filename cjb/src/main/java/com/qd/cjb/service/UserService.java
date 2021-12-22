package com.qd.cjb.service;

import com.qd.cjb.pojo.Users;

/**
 * @version : 1.0
 * @author : hjx
 * @Date : 2021/1/24
 * @Description : nuc.edu.hjx.service
 */

public interface UserService {

    /**
     * 判断是否存在用户
     * @return boolean
     */
    boolean queryUsernameIsExist(String username);

    /**
     * 保存用户
     */
    void saveUser(Users user);

    /**
     * 登录验证
     * @param username
     * @param password
     * @return
     */
    Users queryUserForLogin(String username, String password);

    /**
     * 上传用户头像
     * @param user
     */
    void updateUser(Users user);

    /**
     * 查询用户信息
     * @param userId
     * @return Users
     */
    Users queryUserInfo(String userId);

    /**
     * 判断用户是否喜欢视频
     * @param userId
     * @param videoId
     * @return
     */
    boolean isLikeVideo(String userId,String videoId);

    /**
     * 成为视频发布者粉丝，关注视频
     * @param userId
     * @param fansId
     * @return
     */
    void becameFans(String userId, String fansId);

    /**
     * 取消关注视频且不再是粉丝
     * @param userId
     * @param fansId
     * @return
     */
    void cancelAttention(String userId, String fansId);

    /**
     * 是否关注对方
     * @param userId
     * @param fansId
     * @return
     */
    boolean isFollow(String userId, String fansId);
}
