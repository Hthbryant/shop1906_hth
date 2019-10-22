$.ajax({
    url:"http://localhost:16666/sso/isLogin",
    type: "POST",
    success:function (data) {
        if(data.code=="0000"){
            $("#pid").html(data.data.username+"您好，欢迎来到<b>ShopCZ商城</b><a href=\"/sso/logout\">注销</a>");
        }else{
            $("#pid").html(" [<a onclick='mylogin();'>登录</a>] [<a href=\"http://localhost:16666/sso/toRegister\">注册</a>]");
        }
    },
    dataType: "json"
})
function mylogin() {
    var returnUrl = location.href;
    returnUrl = encodeURIComponent(returnUrl);
    location.href="http://localhost:16666/sso/toLogin?returnUrl="+returnUrl;
}
