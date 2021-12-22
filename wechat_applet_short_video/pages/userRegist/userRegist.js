const app = getApp();

Page({
  data: {
  },
  /**
   * 注册操作
   * @param {获取界面信息} e 
   */
  doRegister: function(e) {

    var form = e.detail.value;
    var username = form.username;
    var password = form.password;

    if(username.length == 0 || password.length == 0 || username == null 
      || password == null ) {
      wx.showToast({
        title: '用户名和密码不能为空',
        icon: 'none',
        duration: 3000
      })
    }else {
      var serverurl = app.serverurl;
      wx.request({
        url: serverurl + '/register',
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
          if(res.data.status == 200) {
            wx.showToast({
              title: '用户注册成功',
              icon:'success',
              duration: 3000
            })
            wx.redirectTo({
              url: '../userLogin/userLogin',
            })
            app.setUserCache(res.data.data);
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
  goLogin: function() {
    wx.navigateTo({
      url: '../userLogin/userLogin',
    })

  }

})