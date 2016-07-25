<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>

<html>
<head>
    <title>View Log</title>
</head>
<body>
    <c:forEach var="loglist" items="${files}">
        <div id=${loglist}> <a href="/viewLog/${loglist}">${loglist} <br></a></div>
    </c:forEach>
</body>
</html>
