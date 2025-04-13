<%@page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="_csrf" content="${_csrf.token}" />
	<meta name="_csrf_header" content="${_csrf.headerName}" />
    <title>Coffee Note</title>
    <link rel="stylesheet" type="text/css" href="/css/noticeView.css"/>
<script>
    // window.onload 이벤트를 사용하여 페이지 로드 후 실행될 함수를 설정
    //window.onload = function() { <-- 이거 쓰면 부모페이지 - 자식페이지 간에 onload 중첩이 안됨
    window.addEventListener('load', function() {
        console.log("currentPage:"+${currentPage});
    });
</script>
</head>
<body>
<div id="noticeView-container">

        <h2 id="noticeView-title">제목: ${notice.title}</h2>
		<!-- 글 내용을 표시할 div를 추가하고 스타일 적용 -->
        <!-- 작성일을 포맷팅하여 출력 -->
    <c:set var="formattedDate" value="${fn:substring(notice.createdDate, 0, 10)}" /> <!-- YYYY-MM-DD -->
    <c:set var="timePart" value="${fn:substring(notice.createdDate, 11, 16)}" /> <!-- HH:MM -->
    <h2 id="noticeView-date">작성일: ${formattedDate} / ${timePart}</h2>
        

    
<div id="noticeView-content">${notice.content}</div>

<!-- "목록으로 돌아가기" 링크에 페이지 번호 추가 -->
<a id="notice-return" href="noticeList">목록으로 돌아가기</a>
</div>
</body>
</html>
