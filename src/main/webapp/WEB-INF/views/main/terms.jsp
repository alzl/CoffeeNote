<%@page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Coffee Note</title>
    <!-- 스타일시트 링크 -->
      <link rel="stylesheet" type="text/css" href="/css/terms.css"/>
</head>	
<body>
<div id="terms-container">
    <h2 id="terms-title">이용약관</h2>
    <div id="terms-content">
        <h3 class="terms-content-title">제 1 조 (목적)</h3>
        이 약관은 '커피노트' (이하 "회사")가 제공하는 모든 서비스의 이용 조건 및 절차, 회사와 이용자의 권리와 의무, 책임사항 등을 규정함을 목적으로 합니다.

        <h3 class="terms-content-title">제 2 조 (용어의 정의)</h3>
        이 약관에서 사용하는 용어의 정의는 다음과 같습니다:
        <ol>
            <li>"서비스"란 회사가 제공하는 카페 정보 검색 및 관련 서비스 전체를 의미합니다.</li>
            <li>"이용자"란 이 약관에 따라 회사의 서비스를 이용하는 자를 의미합니다.</li>
            <li>"회원"은 회사와 이용계약을 체결하고 ID를 부여받은 자를 의미합니다.</li>
        </ol>

        <h3 class="terms-content-title">제 3 조 (서비스 이용)</h3>
        회사는 이용자에게 다음과 같은 서비스를 제공합니다:
        <ol>
            <li>카페 정보 제공 및 검색 기능</li>
            <li>사용자 리뷰 및 평점 기능</li>
            <li>방문 기록 및 히스토리 기능</li>
            <li>북마크 및 추천 기능</li>
        </ol>

        <h3 class="terms-content-title">제 4 조 (회원 가입 및 관리)</h3>
        <ol>
            <li>이용자는 서비스 이용을 위해 회사가 제공하는 절차에 따라 회원 가입을 해야 합니다.</li>
            <li>회원은 자신의 ID 및 비밀번호를 관리할 책임이 있으며, 이를 제3자에게 양도하거나 대여할 수 없습니다.</li>
        </ol>

        <h3 class="terms-content-title">제 5 조 (콘텐츠 및 저작권)</h3>
        <ol>
            <li>서비스 내의 모든 콘텐츠는 회사 또는 저작권자가 소유하며, 무단으로 복제, 배포, 전송할 수 없습니다.</li>
            <li>이용자는 서비스 내의 콘텐츠를 개인적인 용도로만 사용할 수 있으며, 상업적인 용도로 사용할 수 없습니다.</li>
        </ol>

        <h3 class="terms-content-title">제 6 조 (서비스의 변경 및 중지)</h3>
        <ol>
            <li>회사는 서비스의 전부 또는 일부를 변경하거나 중지할 수 있습니다.</li>
            <li>서비스의 변경 또는 중지로 인한 이용자의 피해에 대해 회사는 책임을 지지 않습니다.</li>
        </ol>

        <h3 class="terms-content-title">제 7 조 (책임의 한계)</h3>
        <ol>
            <li>회사는 서비스 이용과 관련하여 발생하는 직접적, 간접적 손해에 대해 책임지지 않습니다.</li>
            <li>이용자가 제공한 정보의 정확성에 대해 회사는 보장하지 않습니다.</li>
        </ol>

        <h3 class="terms-content-title">제 8 조 (개인정보 보호)</h3>
        <ol>
            <li>회사는 개인정보 보호를 위해 최선을 다하며, 개인정보 처리방침에 따라 정보를 관리합니다.</li>
            <li>이용자는 개인정보 처리방침을 숙지하고 동의한 후 서비스 이용을 시작해야 합니다.</li>
        </ol>

        <h3 class="terms-content-title">제 9 조 (서비스 이용의 해지)</h3>
        <ol>
            <li>이용자는 언제든지 서비스 이용을 해지할 수 있습니다.</li>
            <li>회사는 해지 요청을 받은 후 일정 기간 내에 서비스를 중지하며, 해지에 따른 책임은 이용자에게 있습니다.</li>
        </ol>

        <h3 class="terms-content-title">제 10 조 (분쟁 해결 및 법적 책임)</h3>
        <ol>
            <li>이 약관과 관련된 분쟁은 회사의 본사 소재지 법원을 제1심 법원으로 합니다.</li>
            <li>회사와 이용자 간의 모든 분쟁에 대해서는 관련 법령에 따라 해결합니다.</li>
        </ol>

        <br>
        <span class="terms-highlight">이용약관에 동의하시면 서비스를 계속 이용하실 수 있습니다.</span>
    </div>
</div>
</body>
</html>
