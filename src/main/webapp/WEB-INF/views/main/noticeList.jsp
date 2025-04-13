<%@page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="_csrf" content="${_csrf.token}" />
    <meta name="_csrf_header" content="${_csrf.headerName}" />
    <title>Coffee Note</title>
    <link rel="stylesheet" type="text/css" href="/css/noticeList.css"/>
</head>
<body>
<div id="notice-container">
    <h2 id="notice-title">공지사항</h2>
    
    <table>
        <thead>
            <tr>
                <th>번호</th>
                <th>제목</th>
                <th>작성일</th>
                <sec:authorize access="hasAuthority('ADMIN')">
                <th>삭제여부</th>
                </sec:authorize>
            </tr>
        </thead>
        <tbody>
				<c:forEach var="notice" items="${notices}" varStatus="status">
				    <tr>
				        <td>${status.index + 1}</td> <!-- 번호 -->
				        <td><a href="noticeView?noticeId=${notice.noticeId}&page=${currentPage}">${notice.title}</a></td> <!-- 제목 -->
				        <td>
				            <c:set var="formattedDate" value="${fn:substring(notice.createdDate, 0, 10)}" /> <!-- YYYY-MM-DD -->
				            <c:set var="timePart" value="${fn:substring(notice.createdDate, 11, 16)}" /> <!-- HH:MM -->
				            ${formattedDate} / ${timePart} <!-- 포맷팅된 날짜 출력 -->
				        </td> <!-- 작성일 -->
				        <sec:authorize access="hasAuthority('ADMIN')">
				            <td>
				                <form action="deleteNotice" method="post" style="display:inline;">
				                    <input type="hidden" name="noticeId" value="${notice.noticeId}" />
				                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
				                    <button id="notice-delete-button" type="submit">X</button>
				                </form>
				            </td>
				        </sec:authorize>
				    </tr>
				</c:forEach>

        </tbody>
    </table>

    <div class="pagination">
        <!-- 처음으로(1페이지로) 이동하는 버튼 -->
        <c:if test="${currentPage > 3}">
            <a href="noticeList?page=1">처음</a>
        </c:if>

        <!-- 이전 페이지로 이동하는 버튼 -->
        <c:if test="${currentPage > 1}">
            <a href="noticeList?page=${currentPage - 1}">이전</a>
        </c:if>

        <!-- 현재 페이지를 기준으로 앞뒤로 최대 5개 페이지 번호만 표시 -->
        <c:set var="startPage" value="${currentPage - 2}" />
        <c:set var="endPage" value="${currentPage + 2}" />

        <c:if test="${startPage < 1}">
            <c:set var="endPage" value="${endPage + (1 - startPage)}" />
            <c:set var="startPage" value="1" />
        </c:if>

        <c:if test="${endPage > totalPages}">
            <c:set var="startPage" value="${startPage - (endPage - totalPages)}" />
            <c:set var="endPage" value="${totalPages}" />
        </c:if>

        <c:if test="${startPage < 1}">
            <c:set var="startPage" value="1" />
        </c:if>

        <!-- 페이지 번호를 동적으로 생성 -->
        <c:forEach begin="${startPage}" end="${endPage}" var="i">
            <a href="noticeList?page=${i}" class="${i == currentPage ? 'active' : ''}">${i}</a>
        </c:forEach>

        <!-- 다음 페이지로 이동하는 버튼 -->
        <c:if test="${currentPage < totalPages}">
            <a href="noticeList?page=${currentPage + 1}">다음</a>
        </c:if>

        <!-- 끝(마지막 페이지)으로 이동하는 버튼 -->
        <c:if test="${currentPage < totalPages - 2}">
            <a href="noticeList?page=${totalPages}">끝</a>
        </c:if>
    </div>
    
    <sec:authorize access="hasAuthority('ADMIN')">
    <p>관리자 권한이 확인되었습니다.</p>
    </sec:authorize>
    <sec:authorize access="!hasAuthority('ADMIN')">
    <p>관리자 권한이 없습니다.</p>
    </sec:authorize>
    <sec:authorize access="isAuthenticated()">
    <p>현재 사용자: ${sessionScope.SPRING_SECURITY_CONTEXT.authentication.name}</p>
    <p>권한: ${sessionScope.SPRING_SECURITY_CONTEXT.authentication.authorities}</p>
    </sec:authorize>

    <sec:authorize access="hasAuthority('ADMIN')">
        <div style="display: flex; justify-content: flex-end;">
            <a id="notice-write" href="noticeWrite">공지사항 작성</a>
        </div>
    </sec:authorize>
</div>
</body>
</html>
