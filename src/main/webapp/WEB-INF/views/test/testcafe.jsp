<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert Cafe</title>
<style>
    .form-container, .list-container {
        border: 1px solid #000;
        padding: 20px;
        margin-bottom: 20px;
    }
</style>
</head>
<body>
<h1>test Cafes</h1>

<div class="form-container">
<h2>insert cafe</h2>
    <form action="${pageContext.request.contextPath}/test/cafeinsert" method="post">
        <label for="cafeId">Cafe ID:</label>
        <input type="text" id="cafeId" name="cafeId" required><br><br>

        <label for="x">X Coordinate:</label>
        <input type="text" id="x" name="x" required><br><br>

        <label for="y">Y Coordinate:</label>
        <input type="text" id="y" name="y" required><br><br>
        
        <label for="placeName">placeName:</label>
        <input type="text" id="placeName" name="placeName" required><br><br>

        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        <input type="submit" value="Create Cafe">
    </form>
    <!-- 메시지 출력 -->
    <c:if test="${not empty message}">
        <p style="color:green">${message}</p>
    </c:if>
    <c:if test="${not empty error}">
        <p style="color:red">${error}</p>
    </c:if>
</div>

<div class="list-container">
    <h2>All Cafes</h2>
    <c:if test="${not empty cafeList}">
        <ul>
            <c:forEach var="cafe" items="${cafeList}">
                <li>
                    ID: ${cafe.cafeId}, X: ${cafe.x}, Y: ${cafe.y}, placeName: ${cafe.placeName}
                    <form action="${pageContext.request.contextPath}/test/cafedelete" method="post" style="display:inline;">
                        <input type="hidden" name="cafeId" value="${cafe.cafeId}">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                        <input type="submit" value="Delete">
                    </form>
                </li>
            </c:forEach>
        </ul>
    </c:if>
    <c:if test="${empty cafeList}">
        <p>No cafes available.</p>
    </c:if>
</div>
</body>
</html>
