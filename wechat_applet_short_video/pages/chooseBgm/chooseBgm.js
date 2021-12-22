const app = getApp()

Page({
  data: {
    bgmList: [],
    videoParams: {}
  },
  /**
   * 加载bgm界面
   * @param {传回视频参数} params 
   */
  onLoad: function(params) {
    var me = this;
    me.setData({
      videoParams: params
    });
    var serverurl = app.serverurl;
    wx.showLoading({
      title: '加载歌曲',
    })
    wx.request({
      url: serverurl + '/bgm/bgmlist',
      header: {'content-type': 'application/json'},
      method: 'POST',
      success: function(res) {
        wx.hideLoading({
          success: (res) => {200},
        })
        if(res.data.status == 200) {
          var bgmList = res.data.data;
          me.setData({
            bgmList: bgmList,
            serverurl: serverurl
          });
        }
      }
    })
  },
  /**
   * 上传短视频
   * @param {上传合成视频参数} e 
   */
  uploadvideo: function(e) {
    var me = this;
    var user = app.getUserCache();
    var serverurl = app.serverurl;
    var bgmId = e.detail.value.bgmId;
    var desc = e.detail.value.desc;
    var height = me.data.videoParams.height;
    var width = me.data.videoParams.width;
    var duration = me.data.videoParams.duration;
    var videourl = me.data.videoParams.videourl;
    var coverurl = me.data.videoParams.coverurl;
    
    wx.showLoading({
      title: '上传中...',
    })
    wx.uploadFile({
      url: serverurl + '/video/uploadvideo',
      formData: {
        userId: user.id,
        bgmId: bgmId,
        videoSecound: duration,
        videoWidth: width,
        videoHeight: height,
        desc: desc
      },
      filePath: videourl,
      name: 'file',
      header: { 'content-type': 'multipart/form-data' },
      success(res) {
        var data = JSON.parse(res.data);
        wx.hideLoading({
          success: (res) => {200},
        })
        if(data.status == 200) {
          wx.showToast({
            title: '上传视频成功',
            icon: 'success',
            duration: 3000
          });
          //返回上一个界面
          wx.navigateBack({
            delta: 1
          })
        } else if (data.status == 502) {
          wx.showToast({
            title: res.data.msg,
            icon: "none",
            duration: 2000
          });
          wx.redirectTo({
            url: '../userLogin/userLogin',
          })
        } else {
          wx.showToast({
            title: '上传失败!',
            icon: "none"
          });
        }
      }
    })
  }
})

