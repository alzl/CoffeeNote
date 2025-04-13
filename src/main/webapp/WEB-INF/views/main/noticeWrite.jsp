<%@page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="_csrf" content="${_csrf.token}" />
	<meta name="_csrf_header" content="${_csrf.headerName}" />
    <title>Coffee Note</title>
    <link rel="stylesheet" type="text/css" href="/css/noticeWrite.css"/>
</head>
<body>
<div id="noticeWrite-container">
    <h2 id="notice-write-title">공지사항 작성</h2>
    <form action="saveNotice" method="post">
        <div id="noticeWrite-header">
            <input type="text" id="noticeWrite-title-write" placeholder="제목을 입력하세요." name="title" required>
        </div>

        <textarea id="noticeWrite-content-write" placeholder="내용을 입력하세요." name="content" required></textarea>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

        <div class="button-container"> <!-- 버튼 컨테이너 추가 -->
            <a id="notice-return" href="noticeList">목록으로 돌아가기</a>
            <button id="notice-save-button" type="submit">저장</button>
        </div>
    </form>
</div>
</body>
</html>
