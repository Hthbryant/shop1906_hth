$(function () {
    $.ajax({
        url:"http://localhost:16666/cart/getCart",
        type:"post",
        success:function (data) {
            if(data!=null && data.length!=0){
                var html = "";
                html += "<ul>"
                for(var i= 0;i<data.length;i++){
                    html += "<li style='height: auto;width: 100%'>";
                    html += "<img src='"+data[i].goods.fengmian+"' style='width: 100px;height: 80px;'/>";
                    html += "<span style='margin-right: 20px'>"+data[i].goods.subject+"</span>";
                    html += "<span style='color:red;'>"+data[i].goods.price+"</span>";
                    html += "</li>";
                }
                html += "</ul>"
                $("#showshop").append(html);
                $("#cartnum").html(data.length);
                $("#cartnum").css("color","red");
            }else{
                $("#showshop").append("<p>您还没添加商品哦，赶快去添加吧</p>");
            }
        },
        dataType:"json"
    })
})