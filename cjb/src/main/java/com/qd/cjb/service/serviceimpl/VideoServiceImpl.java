package com.qd.cjb.service.serviceimpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qd.cjb.common.idworker.Sid;
import com.qd.cjb.common.utils.PagedResult;
import com.qd.cjb.mapper.UsersLikeVideosMapper;
import com.qd.cjb.mapper.UsersMapper;
import com.qd.cjb.mapper.VideosMapper;
import com.qd.cjb.mapper.VideosUserMapper;
import com.qd.cjb.pojo.UsersLikeVideos;
import com.qd.cjb.pojo.Videos;
import com.qd.cjb.pojo.vo.VideosVo;
import com.qd.cjb.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import java.util.List;

/**
 * @version : 1.0
 * @auther : hjx
 * @Date : 2021/2/25
 * @Description : nuc.edu.hjx.service.serviceImpl
 */
@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private Sid sid;
    @Autowired
    private VideosMapper videosMapper;
    @Autowired
    private VideosUserMapper videosUserMapper;
    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private UsersLikeVideosMapper usersLikeVideosMapper;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveVideo(Videos video) {
        String id = sid.nextShort();
        video.setId(id);
        videosMapper.insertSelective(video);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public PagedResult getAllVideos(Integer page, Integer pageSize) {
        //对访问数据库进行拦截分页
        PageHelper.startPage(page,pageSize);
        List<VideosVo> videos = videosUserMapper.queryAllVideos();
        //获取PageHelper分页的数据
        PageInfo<VideosVo> pageInfo = new PageInfo<>(videos);
        PagedResult pagedResult = new PagedResult();

        pagedResult.setPage(page);
        //设置总页数
        pagedResult.setTotal(pageInfo.getPages());
        //设置总记录数
        pagedResult.setRecords(pageInfo.getTotal());
        pagedResult.setRows(videos);
        return pagedResult;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public PagedResult getqueryVideos(Integer page, Integer pageSize,String desc) {
        PageHelper.startPage(page,pageSize);
        List<VideosVo> videos = videosUserMapper.queryVideoByDesc(desc);
        //获取PageHelper分页的数据
        PageInfo<VideosVo> pageInfo = new PageInfo<>(videos);
        PagedResult pagedResult = new PagedResult();
        //设置页数
        pagedResult.setPage(page);
        //设置总页数
        pagedResult.setTotal(pageInfo.getPages());
        //设置总记录数
        pagedResult.setRecords(pageInfo.getTotal());
        pagedResult.setRows(videos);
        return pagedResult;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void addLikeCounts(String userId, String videoId) {

        String likeId = sid.nextShort();
        UsersLikeVideos like = new UsersLikeVideos();
        like.setId(likeId);
        like.setUserId(userId);
        like.setVideoId(videoId);
        usersLikeVideosMapper.insert(like);

        usersMapper.addLikeCounts(userId);
        videosMapper.addLikeVideo(videoId);

    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void reduceLikeCounts(String userId, String videoId) {

        //按照索引删除其中一个id另一个ID也会删除
        Example example = new Example(UsersLikeVideos.class);
        Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId",userId);
        criteria.andEqualTo("videoId",videoId);
        usersLikeVideosMapper.deleteByExample(example);
        usersMapper.reduceLikeCounts(userId);
        videosMapper.reduceLikeVideo(videoId);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedResult queryMyVideos(Integer page, Integer pageSize, String userId) {
        PageHelper.startPage(page,pageSize);
        List<VideosVo> videos = videosUserMapper.queryMyVideos(userId);
        //获取PageHelper分页的数据
        PageInfo<VideosVo> pageInfo = new PageInfo<>(videos);
        PagedResult pagedResult = new PagedResult();
        //设置页数
        pagedResult.setPage(page);
        //设置总页数
        pagedResult.setTotal(pageInfo.getPages());
        //设置总记录数
        pagedResult.setRecords(pageInfo.getTotal());
        pagedResult.setRows(videos);
        return pagedResult;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedResult queryMyLikes(Integer page, Integer pageSize, String userId) {
        PageHelper.startPage(page,pageSize);
        List<VideosVo> videos = videosUserMapper.queryMyLikes(userId);
        //获取PageHelper分页的数据
        PageInfo<VideosVo> pageInfo = new PageInfo<>(videos);
        PagedResult pagedResult = new PagedResult();
        //设置页数
        pagedResult.setPage(page);
        //设置总页数
        pagedResult.setTotal(pageInfo.getPages());
        //设置总记录数
        pagedResult.setRecords(pageInfo.getTotal());
        pagedResult.setRows(videos);
        return pagedResult;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedResult queryMyFollows(Integer page, Integer pageSize, String userId) {
        PageHelper.startPage(page,pageSize);
        List<VideosVo> videos = videosUserMapper.queryMyFollows(userId);
        //获取PageHelper分页的数据
        PageInfo<VideosVo> pageInfo = new PageInfo<>(videos);
        PagedResult pagedResult = new PagedResult();
        //设置页数
        pagedResult.setPage(page);
        //设置总页数
        pagedResult.setTotal(pageInfo.getPages());
        //设置总记录数
        pagedResult.setRecords(pageInfo.getTotal());
        pagedResult.setRows(videos);
        return pagedResult;
    }

}
