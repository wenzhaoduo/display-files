<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>

<html>

<head>
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
        #row {
            display: none;
        }
        #pos {
            display: none;
        }
        #isOver {
            display: none;
        }
    </style>
</head>

<body>
<%--<a name="top">顶部<a>--%>
    <div id="top_div"><a href="#top">返回顶部</a></div>
    <div id="curPage">${curPage}</div>
    <div id="row">${row}</div>
    <div id="pos">${pos}</div>
    <div id="isOver">${isOver}</div>
    <div id="logs">
        <c:forEach var="loglist" items="${content}">
            ${loglist}<br>
        </c:forEach>
    </div>

    <script type="text/javascript">
        var oDiv = document.getElementById('logs');
        oDiv.scrollTop = oDiv.scrollHeight;
        window.scrollTo(0,9999999);
    </script>
</body>
</html>