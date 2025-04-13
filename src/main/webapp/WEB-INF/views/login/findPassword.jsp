<%@page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="_csrf" content="${_csrf.token}" />
	<meta name="_csrf_header" content="${_csrf.headerName}" />	
    <title>비밀번호 찾기</title>
    <link rel="stylesheet" type="text/css" href="/css/findPassword.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    
</head>
<body>
	<div class="findPassword-password-recovery-container">
    <h1 id="findPassword-password-recovery-title">비밀번호 찾기</h1>
    <div class="findPassword-input-group">        
    <input type="text" id="userId" placeholder="아이디 입력">
    </div>
    <div class="findPassword-input-group"> 
    <input type="text" id="email" placeholder="이메일 입력">
    <button id="findPassword-recover-password-button" onclick="resetPassword()">비밀번호 찾기</button>
    </div>
    <div id="result" class="imsi"></div>
    </div>
</body>
<script type="text/javascript" src="/js/findPassword.js"></script>
</html>
