const app = getApp()

Page({
  data: {
    reasonType: "请选择原因",
    reportReasonArray: app.reportReasonArray,
    publishUserId:"",
    videoId:""
  },

  onLoad:function(params) {
    var me = this;

    var videoId = params.videoId;
    var publisherId = params.publisherId;

    console.log(videoId,publisherId)

    me.setData({
      publisherId: publisherId,
      videoId: videoId
    });
  },

  /**
   * 选择举报理由
   * @param {前端页面数据} e 
   */
  changeMe:function(e) {
    var me = this;

    var index = e.detail.value;
    var reasonType = app.reportReasonArray[index];

    me.setData({
      reasonType: reasonType
    });
  },

  /**
   * 提交举报信息
   * @param {前端页面数据} e 
   */
  submitReport:function(e) {
    var me = this;

    var reasonIndex = e.detail.value.reasonIndex;
    var reasonContent = e.detail.value.reasonContent;

    var user = app.getUserCache();

    if (reasonIndex == null || reasonIndex == '' || reasonIndex == undefined) {
      wx.showToast({
        title: '选择举报理由',
        icon: "none"
      })
      return;
    }

    var serverurl = app.serverurl;
    wx.request({
      url: serverurl + '/video/report',
      method: 'POST',
      data: {
        dealUserId: me.data.publisherId,
        dealVideoId: me.data.videoId,
        title: app.reportReasonArray[reasonIndex],
        content:reasonContent,
        userid: user.id
      },
      header: {
        'content-type': 'application/json', // 默认值
        'headerUserId': user.id,
        'headerUserToken': user.userToken
      },
      success:function(res) {
        wx.showToast({
          title: res.data.msg,
          duration: 2000,
          icon: 'none',
          success: function() {
            wx.navigateBack();
          }
        })
      }

    })

  }
    
})
