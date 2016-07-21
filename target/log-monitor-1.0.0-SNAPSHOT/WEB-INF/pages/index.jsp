<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>

<html>

<head>
    <title>mmmmmmmmmmmmmmmmmm</title>
    <spring:url value="/js/load.js" var="testJS"/>
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
        ol {list-style: none;}
    </style>
</head>

<body>
    <div id="top_div"><a href="#top">返回底部</a></div>
    <div id="curPage">${curPage}</div>
    <div id="pos">${pos}</div>
    <div id="isOver">${isOver}</div>
    <div id=${curPage}>
        <c:forEach var="loglist" items="${content}">
            ${loglist}<br>
        </c:forEach>
        <%--<ol id="logs${curPage}">--%>
            <%--<c:forEach var="loglist" items="${content}">--%>
                <%--<li>${loglist}<br></li>--%>
            <%--</c:forEach>--%>
        <%--</ol>--%>
    </div>

    <script type="text/javascript">
        var oDiv = document.getElementById(${curPage});
        oDiv.scrollTop = oDiv.scrollHeight;
        window.scrollTo(0,9999999);
    </script>
</body>
</html>