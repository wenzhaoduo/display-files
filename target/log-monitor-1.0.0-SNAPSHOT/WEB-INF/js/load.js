window.onscroll = function(){

    var top_div = document.getElementById("top_div"); //拿到top_div这个部件对象的引用
    var isOver = document.getElementById("isOver");

    var docH = document.documentElement.clientHeight;//页面高度
    var contentH = document.documentElement.scrollHeight;//内容高度
    var scrollTop = document.documentElement.scrollTop || document.body.scrollTop; //滚动高度


    if(scrollTop / contentH <= 0.1 && scrollTop / contentH > 0 && $(isOver).text() == "false") {

        var curPage = document.getElementById("curPage");
        var pos = document.getElementById("pos");
        var logs = document.getElementById("logs");


        $.ajax({
            async:false,
            type:'get', //get 不如 post 安全，能传递值的字节大小也不如 post。
            url:'/loadLog',
            data: {curPage: $(curPage).text(), pos: $(pos).text()}, //向后台发送的数据
            dataType: "json", //dataType 是指定后台返回值的类型，如果不写，则默认是text。
            contentType: "application/json; charset=utf-8",
            success:function(msg){
                var existingDiv = document.getElementById((msg.curPage - 1).toString());
                var newDiv = $( "<div id=" + msg.curPage + "></div>" );
                $(existingDiv).before(newDiv);

                var curDiv = document.getElementById(msg.curPage.toString());
                curPage.innerHTML = msg.curPage;
                pos.innerHTML = msg.pos;
                isOver.innerHTML = msg.isOver;
                // var ol = document.createElement("ol");
                // curDiv.appendChild(ol);
                var str = "";
                msg.content.forEach(function (e) {
                    str += e + "<br>";
                    // var li = document.createElement("li");
                    // var textNode = document.createTextNode(e);
                    // li.appendChild(textNode);
                    // ol.appendChild(li);
                });
                var textNode = document.createTextNode(str);
                $(curDiv).append(str);

                // curDiv.scrollTop = curDiv.scrollHeight;
                // window.scrollTo(0,9999999);
            },

            error:function(msg){
                alert("Failed！");
            }
        });
    } else {
        top_div.style.display = "none";
    }
}