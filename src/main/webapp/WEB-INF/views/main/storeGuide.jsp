<%@page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Coffee Note</title>
    <!-- 스타일시트 링크 -->
      <link rel="stylesheet" type="text/css" href="/css/storeGuide.css"/>
</head>	
<body>
<div id="storeGuide-container">
    <h2 id="storeGuide-title">입점 안내</h2>
    <div id="storeGuide-content">
        <h3 class="storeGuide-content-title">서울의 숨겨진 보석 같은 카페를 소개하세요!</h3>
        '커피노트'는 서울 곳곳의 독특하고 매력적인 카페들을 발굴하여, 카페를 사랑하는 많은 이들에게 소개하는 플랫폼입니다. 사장님의 카페가 가진 특별한 매력을 더 많은 사람들과 나누고 싶으신가요? '커피노트'에서 그 꿈을 실현해보세요!

        <h3 class="storeGuide-content-title">입점 시 혜택</h3>
        <ul>
            <li>효과적인 홍보: 커피노트를 통해 카페를 찾는 20~30대 청년층 및 다양한 연령대의 고객에게 카페를 효과적으로 홍보하세요.</li>
            <li>위치 기반 노출: 사용자가 현재 위치에서 가까운 카페를 검색할 때, 사장님의 카페가 상단에 노출될 수 있습니다.</li>
            <li>리뷰와 평점 관리: 방문자들의 실제 리뷰와 평점을 통해 신뢰를 구축하고, 더 많은 고객을 유치할 수 있습니다.</li>
            <li>트렌드 반영: 실시간 인기 순위와 추천 북마크 모음을 통해, 사장님의 카페를 트렌디한 공간으로 만들어보세요.</li>
        </ul>

        <h3 class="storeGuide-content-title">커피노트와 함께할 때, 사장님이 얻을 수 있는 가치</h3>
        <ul>
            <li>더 많은 방문자 유치: 독특한 분위기와 먹거리를 찾는 고객들이 사장님의 카페를 쉽게 발견할 수 있습니다.</li>
            <li>카페 문화 확산: 카페의 매력을 널리 알리고, 서울의 다양한 카페 문화를 함께 만들어 나갈 수 있습니다.</li>
            <li>사용자 참여 강화: 리뷰와 평점을 통한 사용자와의 소통으로, 더 나은 서비스와 경험을 제공할 수 있습니다.</li>
        </ul>

        <h3 class="storeGuide-content-title">지금 등록하세요</h3>
        지금 '커피노트'에 사장님의 카페를 등록하고, 서울의 카페 애호가들과 특별한 순간들을 함께 만들어보세요.
        <br>
        <br>

        <button class="highlight" onclick="location.href='/user/'">카페 홍보 게시글 작성하러 가기 << Click!! </button>

    </div>
</div>
</body>
</html>