<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>

<html>

<head>
    <title>mmmmmmmmmmmmmmmmmm</title>
    <spring:url value="/js/load.js" var="testJS"/>
    <spring:url value="/css/logio.css" var="testCSS"/>
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
    <script src="${testJS}"></script>


    <style type="text/css">
        #top_div {
            position: fixed;
            bottom: 10px;
            right: 0;
            display: none;
        }
        #curPage {
            display: none;
        }
        #pos {
            display: none;
        }
        #isOver {
            display: none;
        }
        #totalLines {
            display: none;
        }
        ol {list-style: none;}
    </style>

    <%--<link rel="stylesheet" href="./css/logio.css" type="text/css">--%>
    <link rel="stylesheet" href="${testCSS}" type="text/css">
</head>

<body>
    <div id="top_div"><a href="#top">返回底部</a></div>
    <div id="curPage">${curPage}</div>
    <div id="pos">${pos}</div>
    <div id="isOver">${isOver}</div>
    <div id="totalLines">${totalLines}</div>
    <div id="log_screens">
        <div class="log_screens">
            <div class="log_screen">
                <div class="messages">
                    <div id=${curPage} class="msg">
                        <c:forEach var="loglist" items="${content}" varStatus="step">
                            <font color="red">${totalLines - curPage * 500 + step.index + 1}</font> &nbsp &nbsp &nbsp ${loglist}<br>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div><a href="/home/mi/log">test</a></div>

    <script type="text/javascript">
        <%--var oDiv = document.getElementById(${curPage});--%>
        <%--oDiv.scrollTop = oDiv.scrollHeight;--%>
        window.scrollTo(0, 9999999);
        console.log (${totalLines});
    </script>
</body>
</html>