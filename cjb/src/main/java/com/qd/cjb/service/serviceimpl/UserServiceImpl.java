package com.qd.cjb.service.serviceimpl;

import com.qd.cjb.common.idworker.Sid;
import com.qd.cjb.mapper.UsersFansMapper;
import com.qd.cjb.mapper.UsersLikeVideosMapper;
import com.qd.cjb.mapper.UsersMapper;
import com.qd.cjb.pojo.Users;
import com.qd.cjb.pojo.UsersFans;
import com.qd.cjb.pojo.UsersLikeVideos;
import com.qd.cjb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import java.util.List;

/**
 * @version : 1.0
 * @author : hjx
 * @Date : 2021/1/24
 * @Description : nuc.edu.hjx.service.serviceImpl
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private UsersLikeVideosMapper usersLikeVideosMapper;
    @Autowired
    private UsersFansMapper usersFansMapper;
    @Autowired
    private Sid sid;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean queryUsernameIsExist(String username) {
        Users user = new Users();
        user.setUsername(username);
        Users result = usersMapper.selectOne(user);
        return result == null ? false : true;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveUser(Users user) {
        String userid = sid.nextShort();
        user.setId(userid);
        usersMapper.insert(user);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserForLogin(String username, String password) {
        Example example = new Example(Users.class);
        Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username",username);
        criteria.andEqualTo("password",password);
        Users user = usersMapper.selectOneByExample(example);
        return user;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateUser(Users user) {
        Example example = new Example(Users.class);
        Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id",user.getId());
        usersMapper.updateByExampleSelective(user,example);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserInfo(String userId) {
        Example example = new Example(Users.class);
        Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id",userId);
        Users users = usersMapper.selectOneByExample(example);
        return users;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean isLikeVideo(String userId, String videoId) {
        if (userId == null && videoId == null) {
            return false;
        }
        Example example = new Example(UsersLikeVideos.class);
        Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId",userId);
        criteria.andEqualTo("videoId",videoId);
        List<UsersLikeVideos> list= usersLikeVideosMapper.selectByExample(example);
        if (list != null && list.size() > 0) {
            return true;
        }
        return false;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void becameFans(String userId, String fansId) {
        String id = sid.nextShort();
        UsersFans usersFans = new UsersFans();
        usersFans.setId(id);
        usersFans.setUserId(userId);
        usersFans.setFanId(fansId);
        usersFansMapper.insert(usersFans);
        usersMapper.addFans(fansId);
        usersMapper.addFollow(userId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void cancelAttention(String userId, String fansId) {
        Example example = new Example(UsersFans.class);
        Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId",userId);
        criteria.andEqualTo("fanId",fansId);
        usersFansMapper.deleteByExample(example);
        usersMapper.reduceFans(fansId);
        usersMapper.reduceFollow(userId);
    }

    @Override
    public boolean isFollow(String userId, String fansId) {
        Example example = new Example(UsersFans.class);
        Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId",userId);
        criteria.andEqualTo("fanId",fansId);
        List<UsersFans> list = usersFansMapper.selectByExample(example);
        if (!list.isEmpty() && list.size() > 0) {
            return true;
        }
        return false;
    }

}
