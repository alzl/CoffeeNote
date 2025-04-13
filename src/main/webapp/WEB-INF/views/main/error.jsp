<%@page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error Page</title>
    <style>
        body {
            font-family: 'Arial', sans-serif;
            background-color: #f0f0f0;
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            color: #333;
        }
        .error-container {
            background-color: #ffffff;
            padding: 40px;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            text-align: center;
        }
        h1 {
            font-size: 3em;
            margin-bottom: 0.5em;
            color: #e74c3c;
        }
        h2 {
            font-size: 1.5em;
            margin-bottom: 1.5em;
        }
        p {
            font-size: 1.2em;
            margin-bottom: 2em;
        }
        button {
            background-color: #3498db;
            color: #ffffff;
            border: none;
            padding: 10px 20px;
            font-size: 1em;
            border-radius: 5px;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }
        button:hover {
            background-color: #2980b9;
        }
    </style>
</head>
<body>
    <div class="error-container">
        <h1>Error <%= request.getAttribute("errorCode") %></h1>
        <h2><%= request.getAttribute("errorMessage") %></h2>
        <p>죄송합니다. 요청하신 페이지를 처리하는 도중 오류가 발생했습니다.</p>
        <button onclick="location.href='/'">Main page</button>
    </div>
</body>
</html>
