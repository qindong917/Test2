/**
 * 上传视频
 */
function uploadVideo(){
  //选择上传短视频
  wx.chooseVideo({
    camera: ['album','camera'],
    camera: 'back',
    success(res) {
      var height = res.height;
      var width = res.width;
      var duration = res.duration;
      var videourl = res.tempFilePath;
      var coverurl = res.thumbTempFilePath;
      if(duration > 60) {
        wx.showToast({
          title: '视频不能超过一分钟',
          icon: 'none',
          duration: 2000
        })
      }else if(duration < 1) {
        wx.showToast({
          title: '视频不能少于一秒',
          icon: 'none',
          duration: 2000 
        })
      }else {
        wx.navigateTo({
          url: '../chooseBgm/chooseBgm?height=' + height 
          + '&width=' + width
          + '&duration=' + duration
          + '&videourl=' + videourl
          + '&coverurl=' + coverurl,
        })
      }
    }
  })
}

module.exports = {
  uploadVideo: uploadVideo
}