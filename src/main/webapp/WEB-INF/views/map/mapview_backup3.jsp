<!-- 북마크 누르면 북마크의 카페들 나오게 설정됨 -->
<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
    <meta name="_csrf" content="${_csrf.token}" />
    <meta name="_csrf_header" content="${_csrf.headerName}" />
<title>지도 검색 - 카페</title>
<style>
    #searchForm, #bookmarkListButton, #currentLocationButton, #bookmarkButton {
        position: absolute;
        z-index: 1;
        background-color: white;
        border-radius: 3px;
        padding: 5px;
        box-shadow: 0px 0px 5px rgba(0, 0, 0, 0.3);
        cursor: pointer;
    }

    #searchForm {
        top: 5px;
        left: 5px;
    }

    #bookmarkListButton {
        top: 10px;
        right: 110px;
    }

    #currentLocationButton {
        top: 10px;
        right: 10px;
        width: 40px;
        height: 40px;
        display: flex;
        align-items: center;
        justify-content: center;
    }

    #bookmarkButton {
        top: 10px;
        right: 60px;
        width: 40px;
        height: 40px;
        display: flex;
        align-items: center;
        justify-content: center;
    }

    #bookmarkModal, #bookmarkListModal, #cafeInfoModal {
        display: none;
        position: fixed;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
        z-index: 2;
        background-color: white;
        padding: 20px;
        border-radius: 5px;
        box-shadow: 0px 0px 15px rgba(0, 0, 0, 0.5);
    }

    #closeModalButton, #closeBookmarkListButton, #closeCafeInfoButton {
        position: absolute;
        top: 5px;
        right: 5px;
        cursor: pointer;
    }

    #listForm {
        position: absolute;
        top: 70px;
        left: 5px;
        z-index: 1;
        background-color: white;
        padding: 5px;
        border-radius: 3px;
        box-shadow: 0px 0px 5px rgba(0, 0, 0, 0.3);
        max-height: 200px;
        overflow-y: auto;
        width: 300px;
    }

    html, body {
        height: 100%;
        margin: 0;
        padding: 0;
    }

    #map {
        width: 100%;
        height: 100vh;
        position: relative;
    }

    .cafe-item, .bookmark-item {
        cursor: pointer;
        margin-bottom: 10px;
        border-bottom: 1px solid black;
    }

    .selected {
        background-color: #f0f0f0;
    }
</style>
<script src="https://dapi.kakao.com/v2/maps/sdk.js?appkey=67caf0129419f4cf4108be62dbe104d3&libraries=services"></script>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
<div id="map"></div>

<div id="bookmarkButton" onclick="showBookmarkModal()">★</div>
<div id="bookmarkListButton" onclick="showBookmarkListModal()">📋 북마크 리스트</div>
<div id="currentLocationButton" onclick="moveToCurrentLocation()">📍</div>

<div id="bookmarkModal">
    <div id="closeModalButton" onclick="hideBookmarkModal()">X</div>
    <h3>북마크 생성</h3>
    <form id="bookmarkForm" action="<c:url value='/map/bookmarkcreate'/>" method="post">
        <p>북마크에 저장할 카페를 리스트에서 선택해주세요</p>
        <input type="text" id="bookmarkTitle" name="title" placeholder="북마크 제목"><br>
        <textarea id="bookmarkContent" name="content" placeholder="북마크 설명"></textarea><br>
        <label><input type="checkbox" id="isPublicCheckbox" name="isPublic"> 공개 여부</label><br>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        <input type="hidden" id="selectedCafesInput" name="selectedCafes">
        <button type="submit">북마크 생성</button>
    </form>
</div>

<div id="bookmarkListModal">
    <div id="closeBookmarkListButton" onclick="hideBookmarkListModal()">X</div>
    <h3>북마크 리스트</h3>
    <ul id="bookmarkList" style="list-style: none; padding: 0;">
        <c:forEach var="bookmark" items="${bookmarkList}">
            <li class="bookmark-item" onclick="showCafesByBookmark(${bookmark.bookmarkId})">
                <strong>${bookmark.userName} (${bookmark.userId})</strong><br>
                제목: ${bookmark.title}<br>
                설명: ${bookmark.content}
            </li>
        </c:forEach>
    </ul>
</div>

<!-- 카페 정보 모달 -->
<div id="cafeInfoModal">
    <div id="closeCafeInfoButton" onclick="hideCafeInfoModal()">X</div>
    <h3>카페 정보</h3>
    <p id="cafeInfoContent"></p>
</div>

<div id="searchForm">
    <form action="/map/searchmap" method="post" accept-charset="UTF-8">
        <input type="text" name="keyword" id="keywordInput" placeholder="Search..." style="padding: 5px;" required>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        <button type="submit" style="padding: 5px;">Search</button>
    </form>
</div>

<div id="listForm">
    <c:choose>
        <c:when test="${not empty cafeDetailsList}">
            <ul style="list-style: none; padding: 0;">
                <c:forEach var="cafe" items="${cafeDetailsList}" varStatus="status">
                    <li class="cafe-item" onclick="moveToMarker(${status.index}); toggleCafeSelection(${status.index});">
                        <strong>${cafe.placeName}</strong><br>
                        주소: ${cafe.addressName}<br>
                        전화번호: ${cafe.phone}<br>
                        <a href="${cafe.placeUrl}" target="_blank">자세히 보기</a>
                    </li>
                </c:forEach>
            </ul>
        </c:when>
        <c:otherwise>
            <p>검색된 내용이 없습니다</p>
        </c:otherwise>
    </c:choose>
</div>

<script>
window.onload = function() {
    var mapContainer = document.getElementById('map'),
    mapOption = { 
        center: new kakao.maps.LatLng(37.556008, 126.972341),
        level: 3
    };

    var map = new kakao.maps.Map(mapContainer, mapOption);

    var markers = [];
    var selectedCafes = new Set();
    var cafeDetailsList = [];
    var isBookmarkModalOpen = false; // 북마크 모달이 열렸는지 여부

    <c:if test="${not empty cafeDetailsList}">
        var bounds = new kakao.maps.LatLngBounds();
        cafeDetailsList = [
            <c:forEach var="cafe" items="${cafeDetailsList}">
                {
                    cafeId: '${cafe.cafeId}',
                    x: '${cafe.x}',
                    y: '${cafe.y}',
                    placeName: '${cafe.placeName}',
                    addressName: '${cafe.addressName}',
                    phone: '${cafe.phone}',
                    placeUrl: '${cafe.placeUrl}'
                },
            </c:forEach>
        ];

        cafeDetailsList.forEach(function(cafe, index) {
            var latlng = new kakao.maps.LatLng(cafe.y, cafe.x);
            bounds.extend(latlng);

            var marker = new kakao.maps.Marker({
                map: map,
                position: latlng,
                title: cafe.placeName
            });

            // 마커 클릭 이벤트 추가 - 클로저 사용
            (function(marker, cafe) {
                kakao.maps.event.addListener(marker, 'click', function() {
                    console.log("마커 클릭됨: ", marker); // 마커 디버그 정보 출력
                    console.log("카페 정보: ", cafe); // 카페 정보 디버그 출력
                    
                    map.panTo(marker.getPosition());
                    showCafeInfoModal(cafe);
                });
            })(marker, cafe);

            markers.push(marker);
        });

        map.setBounds(bounds);
    </c:if>

    window.moveToMarker = function(index) {
        var position = markers[index].getPosition();
        map.panTo(position);
    };

    window.showBookmarkModal = function() {
        selectedCafes.clear(); // 북마크 모달 열 때 selectedCafes 초기화
        document.getElementById('bookmarkModal').style.display = 'block';
        isBookmarkModalOpen = true;
    };

    window.hideBookmarkModal = function() {
        document.getElementById('bookmarkModal').style.display = 'none';
        isBookmarkModalOpen = false;
        selectedCafes.clear(); // 북마크 모달 닫을 때 selectedCafes 초기화
        
        // 선택된 li 요소들의 회색 배경 초기화
        document.querySelectorAll('.cafe-item.selected').forEach(function(item) {
            item.classList.remove('selected');
        });
    };

    window.showBookmarkListModal = function() {
        document.getElementById('bookmarkListModal').style.display = 'block';
    };

    window.hideBookmarkListModal = function() {
        document.getElementById('bookmarkListModal').style.display = 'none';
    };

    window.showCafeInfoModal = function(cafe) {
        // 디버깅용 로그 추가
        console.log("카페 정보 모달 표시: ", cafe);
        
        // 개별 속성을 로그로 출력하여 확인
        console.log("카페 이름: ", cafe.placeName);
        console.log("카페 주소: ", cafe.addressName);
        console.log("카페 전화번호: ", cafe.phone);
        console.log("카페 URL: ", cafe.placeUrl);

        // 카페 정보 내용 생성 (명확한 문자열 결합 사용)
        var cafeInfoContent = 
            "<strong>이름:</strong> " + cafe.placeName + "<br>" +
            "<strong>주소:</strong> " + cafe.addressName + "<br>" +
            "<strong>전화번호:</strong> " + cafe.phone + "<br>" +
            "<strong>자세히 보기:</strong> <a href='" + cafe.placeUrl + "' target='_blank'>링크</a>";

        // 카페 정보 모달을 화면에 표시
        var cafeModal = document.getElementById('cafeInfoModal');
        cafeModal.style.display = 'block';
        console.log("카페의 content: " + cafeInfoContent);

        // 카페 정보 모달의 내용 설정
        document.getElementById('cafeInfoContent').innerHTML = cafeInfoContent;
    };

    window.hideCafeInfoModal = function() {
        document.getElementById('cafeInfoModal').style.display = 'none';
    };

    window.toggleCafeSelection = function(index) {
        if (!isBookmarkModalOpen) {
            // 북마크 모달이 열리지 않은 상태에서는 아무것도 하지 않음
            return;
        }

        if (selectedCafes.has(index)) {
            selectedCafes.delete(index);
            document.querySelectorAll('.cafe-item')[index].classList.remove('selected');
        } else {
            selectedCafes.add(index);
            document.querySelectorAll('.cafe-item')[index].classList.add('selected');
        }
    };

    document.getElementById('bookmarkForm').onsubmit = function() {
        var selectedCafeDetails = [];
        selectedCafes.forEach(function(index) {
            selectedCafeDetails.push({
                cafeId: cafeDetailsList[index].cafeId,
                x: cafeDetailsList[index].x,
                y: cafeDetailsList[index].y,
                placeName: cafeDetailsList[index].placeName
            });
        });

        document.getElementById('selectedCafesInput').value = JSON.stringify(selectedCafeDetails);
        return true;
    };

    window.moveToCurrentLocation = function() {
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(function(position) {
                var lat = position.coords.latitude;
                var lon = position.coords.longitude;

                var locPosition = new kakao.maps.LatLng(lat, lon); 
                map.panTo(locPosition);

                var geocoder = new kakao.maps.services.Geocoder();
                geocoder.coord2Address(lon, lat, function(result, status) {
                    if (status === kakao.maps.services.Status.OK) {
                        var address = result[0].address.address_name;
                        document.getElementById('keywordInput').value = address;
                        document.getElementById('searchForm').submit();
                    } else {
                        alert("주소를 찾을 수 없습니다.");
                    }
                });
            });
        } else {
            alert("GPS를 지원하지 않는 브라우저입니다.");
        }
    };

    window.showCafesByBookmark = function(bookmarkId) {
        document.getElementById('filterFormBookmarkId').value = bookmarkId;
        document.getElementById('filterForm').submit();
    };

    function updateMapWithCafes(cafes) {
        markers.forEach(marker => marker.setMap(null));
        markers = [];
        var bounds = new kakao.maps.LatLngBounds();

        cafes.forEach((cafe, index) => {
            var latlng = new kakao.maps.LatLng(cafe.y, cafe.x);
            var marker = new kakao.maps.Marker({
                map: map,
                position: latlng,
                title: cafe.placeName
            });
            
            // 마커 클릭 이벤트 추가 - 클로저 사용
            (function(marker, cafe) {
                kakao.maps.event.addListener(marker, 'click', function() {
                    console.log("마커 클릭됨: ", marker); // 마커 디버그 정보 출력
                    console.log("카페 정보: ", cafe); // 카페 정보 디버그 출력

                    map.panTo(marker.getPosition());
                    showCafeInfoModal(cafe);
                });
            })(marker, cafe);

            markers.push(marker);
            bounds.extend(latlng);
        });

        map.setBounds(bounds);

        var listForm = document.getElementById('listForm');
        var listHtml = cafes.map(function(cafe, index) {
            return `
                <li class="cafe-item" onclick="moveToMarker(${index}); toggleCafeSelection(${index});">
                    <strong>${cafe.placeName}</strong><br>
                    주소: ${cafe.addressName}<br>
                    전화번호: ${cafe.phone}<br>
                    <a href="${cafe.placeUrl}" target="_blank">자세히 보기</a>
                </li>
            `;
        }).join('');
        listForm.innerHTML = `<ul style="list-style: none; padding: 0;">${listHtml}</ul>`;
    }
}
</script>


<form id="filterForm" action="/map/filterByBookmark" method="post" style="display:none;">
    <input type="hidden" id="filterFormBookmarkId" name="bookmarkId"/>
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
</form>

</body>
</html>
