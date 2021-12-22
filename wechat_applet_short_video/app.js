// app.js
App({
  serverurl:"https://cjb123.mynatapp.cc",
  
  setUserCache: function (user) {
    wx.setStorageSync('userInfo', user)
  },
  getUserCache:function() {
    return wx.getStorageSync('userInfo')
  },
  reportReasonArray: [
    "色情低俗",
    "政治敏感",
    "涉嫌诈骗",
    "辱骂谩骂",
    "广告垃圾",
    "诱导分享",
    "过于暴力",
    "违法违纪",
    "其他原因"
  ]
})
