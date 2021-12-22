const app = getApp()

Page({
  data: {
    // 用于分页的属性
    totalPage: 1,
    page:1,
    videoList:[],

    screenWidth: 350,
    serverurl: "",

    searchContent: ""
  },

  /**
   * 加载初始界面
   * @param {获取界面参数} param 
   */
  onLoad: function(params) {
    var me = this;
    var screenWidth = wx.getSystemInfoSync().screenWidth;
    var searchContent = params.search;
    var issaverecord = params.isSaveRecord;
    console.log(searchContent);
    me.setData({
      screenWidth: screenWidth,
      searchContent: searchContent
    })
    
    var page = me.data.page;
    if(issaverecord == 1) {
      me.getqueryVideos(page);
    } else {
      me.getAllVideos(page);
    }
  },
  /**
   * 获取所有视频
   * @param {获取分页页数} page 
   */
  getAllVideos: function(page) {
    var me = this;
    var serverurl = app.serverurl;
    wx.showLoading({
      title: '加载中',
    })
    wx.request({
      url: serverurl + '/video/showallvideo?page=' + page,
      method:'POST',
      success(res) {
        wx.hideLoading({
          success: (res) => {200},
        })
        //停止缓存圆圈
        wx.hideNavigationBarLoading({
          success: (res) => {200},
        })
        wx.stopPullDownRefresh({
          success: (res) => {200},
        })
        console.log(res.data);
        if(page == 1) {
          me.setData({
            videoList:[]
          })
        }
        var videoList = res.data.data.rows;
        var newvideoList = me.data.videoList;
        me.setData({
          videoList:newvideoList.concat(videoList),
          page:page,
          totalPage:res.data.data.total,
          serverurl:serverurl
        })
      }
    })
  },
  /**
   * 获取搜索视频
   * @param {获取分页页数}} page 
   */
  getqueryVideos: function(page) {
    var me = this;
    var serverurl = app.serverurl;
    wx.showLoading({
      title: '加载中...',
    })
    wx.request({
      url: serverurl + '/video/queryvideo?page=' + page,
      method:'POST',
      data: {
        videoDesc: me.data.searchContent
      },
      success(res) {
        wx.hideLoading({
          success: (res) => {200},
        })
        //停止缓存圆圈
        wx.hideNavigationBarLoading({
          success: (res) => {200},
        })
        wx.stopPullDownRefresh({
          success: (res) => {200},
        })
        console.log(res.data);
        if(page == 1) {
          me.setData({
            videoList:[]
          })
        }
        if(res.data.data.record) {
          wx.showToast({
            title: '没有相关记录',
          })
          wx.redirectTo({
            url: '../index/index',
          })
        }
        var videoList = res.data.data.rows;
        var newvideoList = me.data.videoList;
        me.setData({
          videoList:newvideoList.concat(videoList),
          page:page,
          totalPage:res.data.data.total,
          serverurl:serverurl
        })
      }
    })
  },
  /**
   * 上拉刷新
   */
  onPullDownRefresh: function () {
    wx.showNavigationBarLoading({
      success: (res) => {},
    })
    this.getAllVideos(1);
  },
  /**
   * 下拉获取下一页
   */
  onReachBottom: function () {
    var me = this;
    var currentPage = me.data.page;
    var totalPage = me.data.totalPage;
    if (currentPage == totalPage) {
      wx.showToast({
        title: '视频已经到底！',
        icon: 'none'
      })
    }
    var page = currentPage + 1;
    me.getAllVideos(page);
  },
  /**
   * 视频展示
   * @param {获取界面参数} e 
   */
  showVideoInfo: function (e) {
    var me = this;
    var videoLsit = me.data.videoList;
    var arrindex = e.target.dataset.arrindex;
    var videoInfo = JSON.stringify(videoLsit[arrindex]);

    wx.redirectTo({
      url: '../videoInfo/videoInfo?videoInfo=' + videoInfo,
    })

  }
})
