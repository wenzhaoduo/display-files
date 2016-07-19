window.onscroll = function(){

    var top_div = document.getElementById("top_div"); //拿到top_div这个部件对象的引用
    var isOver = document.getElementById("isOver");

    var docH = document.documentElement.clientHeight;//页面高度
    var contentH = document.documentElement.scrollHeight;//内容高度
    var scrollTop = document.documentElement.scrollTop || document.body.scrollTop; //滚动高度

    // alert($(isOver).text() == "false");

    if(scrollTop / contentH <= 0.1 && scrollTop / contentH > 0 && $(isOver).text() == "false") {

        var curPage = document.getElementById("curPage");
        var row = document.getElementById("row");
        var pos = document.getElementById("pos");

        top_div.style.display = "inline";
        $.ajax({
            async:false,
            type:'get', //get 不如 post 安全，能传递值的字节大小也不如 post。
            url:'/',
            data: {curPage: $(curPage).text(), row: $(row).text(), pos: $(pos).text()},
            // dataType: "json", //dataType 是指定后台返回值的类型，如果不写，则默认是text。
            contentType: "application/json; charset=utf-8",
            success:function(msg){
                alert(typeof(msg));
            },
            error:function(msg){
                alert("Failed！");
            }
        });
    } else {
        top_div.style.display = "none";
    }
}