package com.qd.cjb.service;

import com.qd.cjb.common.utils.PagedResult;
import com.qd.cjb.pojo.Videos;

/**
 * @version : 1.0
 * @auther : hjx
 * @Date : 2021/2/25
 * @Description : nuc.edu.hjx.service
 */
public interface VideoService {

    /**
     * 保存视频
     * @param video
     */
    void saveVideo(Videos video);

    /**
     * 返回视频的分页信息
     * @param page
     * @param pageSize
     * @return PagedResult
     */
    PagedResult getAllVideos(Integer page, Integer pageSize);

    /**
     * 对关键词进行查询
     * @param page
     * @param pageSize
     * @return
     */
    PagedResult getqueryVideos(Integer page,Integer pageSize,String desc);

    /**
     * 点赞数增加
     * @param userId
     * @param videoId
     */
    void addLikeCounts(String userId, String videoId);

    /**
     * 点赞数减少
     * @param userId
     * @param videoId
     */
    void reduceLikeCounts(String userId, String videoId);

    PagedResult queryMyVideos(Integer page, Integer pageSize, String userId);

    PagedResult queryMyLikes(Integer page, Integer pageSize, String userId);

    PagedResult queryMyFollows(Integer page, Integer pageSize, String userId);
}
