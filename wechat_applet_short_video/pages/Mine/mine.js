var videoutil = require('../../utils/video')
const app = getApp()

Page({
  data: {
    faceUrl: '',
    publisherId: '',
    isMe: true,
    isFollow: false,

    videoSelClass: "video-info",
    isSelectedWork: "video-info-selected",
    isSelectedLike: "",
    isSelectedFollow: "",

    myVideoList: [],
    myVideoPage: 1,
    myVideoTotal: 1,

    likeVideoList: [],
    likeVideoPage: 1,
    likeVideoTotal: 1,

    followVideoList: [],
    followVideoPage: 1,
    followVideoTotal: 1,

    myWorkFalg: false,
    myLikesFalg: true,
    myFollowFalg: true

  },
  /**
   * 加载个人信息界面
   */
  onLoad: function(params) {
    var me = this;
    var user = app.getUserCache();
    var userId = user.id;
    var publisherId = params.publisherId;
    console.log(publisherId)
    if(publisherId != null && publisherId != undefined && publisherId != '') {
      userId = publisherId;
      me.setData({
        isMe: false,
        userId: publisherId
      })
    }
    var serverurl = app.serverurl;
    wx.showLoading({
      title: '显示个人信息',
    })
    if(user == null || user == undefined || user == '') {
      wx.navigateTo({
        url: '../userLogin/userLogin',
      })
    } else {
      wx.request({
      url: serverurl + '/user/queryuser?userId=' + userId + '&fansId=' + user.id,
      method: 'POST',
      header: {
        'content-type': 'application/json',
        'userId': user.id,
        'userToken': user.userToken
      },
      success: function(res) {
        wx.hideLoading({
          success: (res) => {200},
        })
        if(res.data.status == 200) {
          var userinfo = res.data.data;
          var face = "../../img/imags/fengjing.jpg";
          if(userinfo.faceImage != null && userinfo.faceImage != '' && userinfo.faceImage != undefined) {
            face = serverurl + userinfo.faceImage;
          }
          me.setData({
            faceUrl: face,
            fansCounts: userinfo.fansCounts,
            followCounts: userinfo.followCounts,
            receiveLikeCounts: userinfo.receiveLikeCounts,
            nickname: userinfo.nickname,
            isFollow: userinfo.isfollow
          });
          me.doSelectWork();
        } else if (res.data.status == 502) {
          wx.showToast({
            title: res.data.msg,
            icon: "none",
            duration: 3000
          })
          wx.redirectTo({
            url: '../userLogin/userLogin',
          })
          
        }
       }
    })
    }
  },
  /**
   * 注销用户（缓存注销）
   */
  logout: function() {

    var user = app.getUserCache();
    var serverurl = app.serverurl;
    wx.showLoading({
      title: '注销中...',
    })
    console.log(user.id);
    // 调用后端
    wx.request({
      url: serverurl + '/logout?userId=' + user.id,
      method: "POST",
      header: {'content-type': 'application/json'},
      success: function (res) {
        console.log(res.data);
        wx.hideLoading({
          success: (res) => {200},
        })
        if (res.data.status == 200) {
          wx.showToast({
            title: '注销成功',
            icon: 'success',
            duration: 3000
          });
          // 注销以后，清空缓存
          wx.removeStorageSync("userInfo")
          // 页面跳转
          wx.redirectTo({
            url: '../userLogin/userLogin',
          })
        }
      }
    })
  },
  /**
   * 上传头像
   */
  uploadFace: function() {
    var me = this;
    wx.chooseImage({
      count: 1,
      sizeType: ['compressed'],
      sourceType: ['album', 'camera'],
      success (res) {
        wx.showLoading({
          title: '上传中...',
        })
        // tempFilePath可以作为img标签的src属性显示图片
        const tempFilePaths = res.tempFilePaths
        var serverurl = app.serverurl
        var user = app.getUserCache();
        wx.uploadFile({
          filePath: tempFilePaths[0],
          name: 'file',
          url: serverurl + '/user/uploadface?userId=' + user.id,
          success: function(res) {
            wx.hideLoading({
              success: (res) => {200},
            })
            var data = JSON.parse(res.data);
            if(data.status == 200) {
              wx.showLoading({
                title: '上传成功',
                icon: 'success',
                duration: 1000
              })
              var face = data.data;
              me.setData({
                faceUrl: serverurl + face
              });
            }else if(data.status == 500){
              wx.showLoading({
                title: data.msg,
                icon:'none'
              })
            }
          }
        })
      }
    })
  },
  /**
   * 上传视频
   */
  uploadVideo: function() {
    videoutil.uploadVideo();
  },
  /**
   * 关注与取消关注
   * @param {获取用户信息} e 
   */
  followMe: function(e) {
    var me = this;
    var user = app.getUserCache();
    var publisherId = me.data.publisherId;
    var serverurl = app.serverurl;
    var follow = e.currentTarget.dataset.followtype;
    if (follow == '1') {
      var url = '/user/fans?userId=' + user.id +'&fansId=' + publisherId; 
      wx.showLoading({
        title: '已关注',
      })
    } else if (follow == '0'){
      var url = '/user/unfans?userId=' + user.id +'&fansId=' + publisherId; 
      wx.showLoading({
        title: '取消关注',
      })
    }
    wx.request({
      url: serverurl + url,
      method: 'POST',
      header: {
        'content-type': 'application/json',
        'userId': user.id,
        'userToken': user.userToken
      },
      success: function(res) {
        wx.hideLoading({
          success: (res) => {200},
        })
        if (follow = '1') {
          me.setData({
            isFollow: true,
            fansCounts: ++me.data.fansCounts
          })
        } else {
          me.setData({
            isFollow: false,
            fansCounts: --me.data.fansCounts
          })
        }
      }
    })
  },
  /**
   * 我的作品
   */
  doSelectWork: function() {
    var me = this;
    me.setData({
      isSelectedWork: "video-info-selected",
      isSelectedLike: "",
      isSelectedFollow: "",
      myWorkFalg: false,
      myLikesFalg: true,
      myFollowFalg: true,

      myVideoList: [],
      myVideoPage: 1,
      myVideoTotal: 1,

      likeVideoList: [],
      likeVideoPage: 1,
      likeVideoTotal: 1,

      followVideoList: [],
      followVideoPage: 1,
      followVideoTotal: 1,
    })
    me.getMyVideo(1);
  },
  /**
   * 我的收藏
   */
  doSelectLike: function() {
    var me = this;
    me.setData({
      isSelectedWork: "",
      isSelectedLike: "video-info-selected",
      isSelectedFollow: "",
      myWorkFalg: true,
      myLikesFalg: false,
      myFollowFalg: true,
      myVideoList: [],
      myVideoPage: 1,
      myVideoTotal: 1,

      likeVideoList: [],
      likeVideoPage: 1,
      likeVideoTotal: 1,

      followVideoList: [],
      followVideoPage: 1,
      followVideoTotal: 1,
    })
    me.getMyLike(1);
  },
  /**
   * 我的关注
   */
  doSelectFollow: function() {
    var me = this;
    me.setData({
      isSelectedWork: "",
      isSelectedLike: "",
      isSelectedFollow: "video-info-selected",
      myWorkFalg: true,
      myLikesFalg: true,
      myFollowFalg: false,

      myVideoList: [],
      myVideoPage: 1,
      myVideoTotal: 1,

      likeVideoList: [],
      likeVideoPage: 1,
      likeVideoTotal: 1,

      followVideoList: [],
      followVideoPage: 1,
      followVideoTotal: 1,
    })
    me.getMyFollow(1);
  },
  /**
   * 获取我上传的视频
   * @param {} page 
   */
  getMyVideo: function (page) {
    var me = this;
    var userId = me.data.userId;
    wx.showLoading({
      title: '加载中'
    })
    var serverurl = app.serverurl;
    wx.request({
      url: serverurl + '/video/myvideo?userId=' + userId + '&page=' + page,
      method: 'POST',
      success: function(res) {
        var myVideoList = res.data.data.rows;
        wx.hideLoading({
          success: (res) => {200},
        })
        var newVideoList = me.data.myVideoList;
        me.setData({
          myVideoList: newVideoList.concat(myVideoList),
          myVideoPage: page,
          myVideoTotal: res.data.data.total,
          serverurl: serverurl
        });
      }
    })
  },
  /**
   * 获取我点赞的视频
   * @param {} page 
   */
  getMyLike: function (page) {
    var me = this;
    var userId = me.data.userId;
    wx.showLoading({
      title: '加载中'
    })
    var serverurl = app.serverurl;
    wx.request({
      url: serverurl + '/video/mylike?userId=' + userId + '&page=' + page,
      method: 'POST',
      success: function(res) {
        var likeVideoList = res.data.data.rows;
        wx.hideLoading({
          success: (res) => {200},
        })
        var newVideoList = me.data.myVideoList;
        me.setData({
          likeVideoList: newVideoList.concat(likeVideoList),
          likeVideoPage: page,
          likeVideoTotal: res.data.data.total,
          serverurl: serverurl
        });
      }
    })
  },
  /**
   * 获取我关注的人
   * @param {} page 
   */
  getMyFollow: function (page) {
    var me = this;
    var userId = me.data.userId;
    wx.showLoading({
      title: '加载中'
    })
    var serverurl = app.serverurl;
    wx.request({
      url: serverurl + '/video/myfollow?userId=' + userId + '&page=' + page,
      method: 'POST',
      success: function(res) {
        console.log(res.data);
        var followVideoList = res.data.data.rows;
        wx.hideLoading({
          success: (res) => {200},
        })
        var newVideoList = me.data.followVideoList;
        me.setData({
          myVideoList: newVideoList.concat(followVideoList),
          myVideoPage: page,
          myVideoTotal: res.data.data.total,
          serverurl: serverurl
        });
      }
    })
  },
  /**
   * 底部触发显示其他视频
   */
  onReachBottom: function() {
    var me = this;
    var myWorkFalg = me.data.myFollowFalg;
    var myLikesFalg = me.data.myLikesFalg;
    var myFollowFalg = me.data.myFollowFalg;
    if (!myWorkFalg) {
       var currentPage = me.data.myVideoPage;
       var totalPage = me.data.myVideoTotal;
       if (currentPage == totalPage) {
          wx.showToast({
            title: '视频到底！',
            icon: 'none'
          });
          return;
       }
       var page = currentPage + 1;
       me.getMyVideo(page);
    } else if (!myLikesFalg) {
      var currentPage = me.data.likeVideoPage;
      var totalPage = me.data.likeVideoTotal;
      if (currentPage == totalPage) {
         wx.showToast({
           title: '视频到底！',
           icon: 'none'
         });
         return;
      }
      var page = currentPage + 1;
      me.getMyLike(page);
    } else if (!myFollowFalg) {
      var currentPage = me.data.likeVideoPage;
      var totalPage = me.data.likeVideoTotal;
      if (currentPage == totalPage) {
         wx.showToast({
           title: '视频到底！',
           icon: 'none'
         });
         return;
      }
      var page = currentPage + 1;
      me.getMyFollow(page);
    }
  },
  /**
   * 显示视频
   * @param {界面信息} e 
   */
  showVideo: function(e) {
    var me = this;
    var myWorkFalg = me.data.myWorkFalg;
    var myLikesFalg = me.data.myLikesFalg;
    var myFollowFalg = me.data.myFollowFalg;
    if (!myWorkFalg) {
      var videoList = me.data.myVideoList;
    } else if (!myLikesFalg) {
      var videoList = me.data.likeVideoList;
    } else if (!myFollowFalg) {
      var videoList = me.data.followVideoList;
    }
    var arrindex = e.target.dataset.arrindex;
    var videoInfo = JSON.stringify(videoList[arrindex]);
    wx.redirectTo({
      url: '../videoInfo/videoInfo?videoInfo=' + videoInfo,
    })
  }
})
