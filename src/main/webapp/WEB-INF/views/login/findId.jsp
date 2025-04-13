<%@page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="_csrf" content="${_csrf.token}" />
	<meta name="_csrf_header" content="${_csrf.headerName}" />
    <title>아이디 찾기</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <link rel="stylesheet" type="text/css" href="/css/findId.css"/>
</head>
<body>
<div class="findId-id-recovery-container">
    <h1 id="findId-id-recovery-title">아이디 찾기</h1>
    <div class="findId-input-group">
    <input type="text" id="email" placeholder="이메일 입력">
    </div>
    <button  id="findId-recover-id-button" onclick="findUserId()">아이디 찾기</button>
    <div id="result"></div>
</div>
</body>
<script src="/js/findId.js"></script>
</html>
