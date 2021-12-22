const app = getApp();

Page({

  data: {
  },
  /**
   * 获取真实地址返回登录前页面
   * @param {返回之前页面信息} params 
   */
  onLoad: function(params) {
    var me = this;
    var realurl = params.realurl;
    if(realurl != null) {
      realurl = realurl.replace(/#/g,"?");
      realurl = realurl.replace(/@/g,"=");
    }
    me.realurl = realurl;
  },

  /**
   * 登录操作
   * @param {获取界面信息} e 
   */
  doLogin: function(e) {
    var me = this;
    var form = e.detail.value;
    var username = form.username;
    var password = form.password;

    if(username.length == 0 || password.length == 0) {
      wx.showToast({
        title: '用户名和密码不能为空',
        icon: 'none',
        duration: 3000
      })
    }else {
      var serverurl = app.serverurl;
      wx.showLoading({
        title: '登录中...',
      })
      wx.request({
        url: serverurl + '/login',
        method: 'POST',
        data: {
          username: username,
          password: password
        },
        header: {
          'content-type': 'application/json'
        },
        success: function(res) {
          console.log(res.data); 
          wx.hideLoading({
            success: (res) => {200},
          })
          if(res.data.status == 200) {
            wx.showToast({
              title: '用户登录成功',
              icon:'success',
              duration: 3000
            })
            app.setUserCache(res.data.data);
            var realurl = me.realurl;
            console.log(realurl);
            //转到之前浏览的视频界面
            if(realurl == null || realurl == undefined || realurl == '') {
              wx.navigateTo({
                url: '../Mine/mine',
              })
            } else {
              wx.navigateTo({
                url: realurl,
              })
            }

          }else if(res.data.status == 500) {
            wx.showToast({
              title: res.data.msg,
              icon: 'none',
              duration: 3000
            })
          }
        }
      })
    }
  },
  /**
   * 转到注册界面
   */
  goRegister: function() {
    wx.navigateTo({
      url: '../userRegist/userRegist',
    })
  }
})