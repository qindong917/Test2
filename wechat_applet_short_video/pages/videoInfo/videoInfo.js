var videoutil = require('../../utils/video')
const app = getApp()
Page({
  data: {
    faceUrl:"",
    videoUrl:"",
    objectFit: "cover",
    video: "",
    videoId: "",
    videoInfo: {},
    userLikeVideo: false,
    publisher:{}
  },
  videoCtx: {},
  /**
   * 查看视频的详情
   * @param {获取视频起始页的视频参数} params 
   */
  onLoad: function (params) {

    var me = this;
    me.videoCtx = wx.createVideoContext('myVideo', me);
    console.log(params);
    var serverurl = app.serverurl;
    var video = JSON.parse(params.videoInfo);
    var width = video.videoWidth;
    var height = video.videoHeight;
    var objectFit = "cover";
    if(width >= height) {
      objectFit = ""; 
    }
    me.setData({
      faceUrl:serverurl + video.faceImage,
      videoUrl:serverurl + video.videoPath,
      videoId: video.id,
      videoInfo: video,
      objectFit: objectFit
    }) 

    var user = app.getUserCache();
    
    if(user != null || user != undefined || user != '') {
  
      wx.request({
        url: serverurl + '/user/likevideo?loginId=' + user.id + '&publishId=' + video.userId + '&videoId=' + video.id,
        method: 'POST',
        success: function(res) {
 
          var publish = res.data.data;
          console.log(publish);
      
          me.setData({
            userLikeVideo:publish.islike,
            publisher:publish.usersVo,
          })
        }
      })
    }
  },
  /**
   * 监听页面显示
   */
  onShow: function () {
    var me = this;
    me.videoCtx.play();

  },
  /**
   * 监听页面隐藏
   */
  onHide: function () {
    var me = this;
    me.videoCtx.pause();
  },
  /**
   * 搜索界面跳转
   */
  showSearch: function() {
    wx.navigateTo({
      url: '../searchVideo/search',
    })
  },
  /**
   * 上传视频
   */
  uploadVideo: function() {
    var me = this;
    var user = app.getUserCache();
    var video = JSON.stringify(me.data.videoInfo);
    var realurl = "../videoInfo/videoInfo#videoInfo@" + video
    if(user == null || user == undefined || user == '') {
      wx.navigateTo({
        url: '../userLogin/userLogin?realurl=' + realurl,
      })
    } else {
      videoutil.uploadVideo();
    }
  },
  /**
   * 返回首页
   */
  showIndex: function() {
    wx.redirectTo({
      url: '../index/index',
    })
  },
  /**
   * 个人页面
   */
  showMine: function() {
    var user = app.getUserCache();
    
    if(user == null || user == undefined || user == '') {
      wx.navigateTo({
        url: '../userLogin/userLogin',
      })
    }
    wx.navigateTo({
      url: '../Mine/mine',
    })
  },
  /**
   * 点赞与取消点赞
   */
  likeVideoOrNot: function() {
    var me = this;
    var user = app.getUserCache();
    var videoId = me.data.videoId;
    var serverurl = app.serverurl;
    var like = me.data.userLikeVideo;
    if(user == null || user == undefined || user == '') {
      wx.navigateTo({
        url: '../userLogin/userLogin',
      })
    } else {
      
      var url = "/video/like";
      if (like) {
        url = "/video/unlike"   
      }
      wx.request({
        url: serverurl + url + "?userId=" + user.id + "&videoId=" + videoId,
        method: 'POST',
        header: {
          'content-type': 'application/json',
          'userId': user.id,
          'userToken': user.userToken
        },
        success (res) {
          
          me.setData({
            userLikeVideo: !like
          })
        }
      })
    }
  },
  /**
   * 显示视频发布者个人信息
   */
  showPublisher: function() {
    var me = this;
    var user = app.getUserCache();
    var video = me.data.videoInfo;
    var realurl = "../Mine/mine#publisherId@" + video.userId
    if(user == null || user == undefined || user == '') {
      wx.navigateTo({
        url: '../userLogin/userLogin?realurl=' + realurl,
      })
    } else {
      wx.navigateTo({
        url: '../Mine/mine?publisherId=' + video.userId,
      })
    }
  },
  shareMe: function()  {
    var me = this;
    wx.showActionSheet({
      itemList: ['视频下载','举报视频','分享给朋友'],
      success: function(res) {
        if (res.tapIndex == 0) {
          me.downloadVideo();
        }else if (res.tapIndex == 1) {

          var videoInfo = me.data.videoInfo;
          console.log(videoInfo)
          var realurl = '../videoInfo/videoInfo#videoInfo@' + videoInfo;
          var user = app.getUserCache();
          if (user == null || user == undefined || user == '') {
            wx.navigateTo({
              url: '../userLogin/userLogin#realurl@' + realurl,
            })
          } else {
            var publisherId = videoInfo.userId;
            var videoId = videoInfo.audioId;
            console.log(videoId)
            wx.navigateTo({
              url: '../report/report?publisherId=' + publisherId + '&videoId=' + videoId,
            })
          }
         
        } else if (res.tapIndex == 2) {
          wx.downloadFile({
            url: me.data.video, // 下载url
            success (res) {
              // 下载完成后转发
              wx.shareVideoMessage({
                videoPath: res.tempFilePath,
                success() {
                  wx.showToast({
                    title: '转发成功',
                  })
                },
                fail: console.error,
              })
            },
            fail: console.error,
          })
        }
      }
    })
  },

  /**
   * 视频下载
   */
  downloadVideo() {
    var me = this;
    var videourl = me.data.video;
    wx.showLoading({
      title: '下载中',
    })
    wx.downloadFile({
      url: videourl,
      success (res) {
        if (res.statusCode === 200) {
          wx.saveVideoToPhotosAlbum({
            filePath: res.tempFilePath,
            success (res) {
              console.log(res)
              console.log(res.errMsg)
              wx.hideLoading();
            }
    })
        }
      }
    })
   
  },
  onShrareAppMessage: function(res) {
    var me = this;
    var videoInfo = me.data.videoInfo;

    return {
      title: "短视频分享",
      path: "/pages/videoInfo/videoInfo?videoInfo=" + JSON.stringify(videoInfo)
    }

  }
})