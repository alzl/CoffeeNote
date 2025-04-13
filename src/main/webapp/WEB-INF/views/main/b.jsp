<%@page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Onload Alert Example</title>
    <script>
        // window.onload 이벤트를 사용하여 페이지 로드 후 실행될 함수를 설정
        window.addEventListener('load', function() {
            alert("안녕 b.jsp에요");
            console.log("b.jsp onload");
        });
    </script>
</head>
<body>
    <h1>b 페이지에 오신 것을 환영합니다!</h1>
    <p>이 페이지는 로드될 때 '안녕' 메시지를 표시합니다.</p>
</body>
</html>
