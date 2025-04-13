<!-- 카페에 추가된 리뷰, 홍보 보이기와 이미지 순서대로 보이기 가능. 리뷰 삭제 및 좋아요 기능 아직 추가 안함 -->
<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

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

    #bookmarkCreateModal {
        display: none;
        position: fixed;
        top: 60px;
        right: 10px;
        z-index: 2;
        background-color: white;
        padding: 20px;
        border-radius: 5px;
        box-shadow: 0px 0px 15px rgba(0, 0, 0, 0.5);
    }

    #bookmarkListModal, #cafeInfoModal, #reviewModal {
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
    
    #bookmarkListModal {
    	max-height: 200px;
    	overflow-y: auto;
    }

    #closeModalButton, #closeBookmarkListButton, #closeCafeInfoButton, #closeReviewButton {
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

    /* 리뷰와 홍보 스타일 조정 */
    .content-list {
        margin-top: 20px;
        max-height: 200px;
        overflow-x: auto;  /* 가로 스크롤 사용 */
        overflow-y: hidden; /* 세로 스크롤 숨기기 */
        white-space: nowrap; /* 가로 스크롤이 필요할 경우 줄바꿈 방지 */
        border: 1px solid #ccc;
        padding: 10px;
        box-sizing: border-box;
    }

    .content-item {
        margin-bottom: 10px;
        border: 1px solid #ccc;
        padding: 10px;
        overflow-x: auto; /* 가로 스크롤 사용 */
        white-space: nowrap; /* 가로 스크롤이 필요할 경우 줄바꿈 방지 */
    }

    .review-image, .advertising-image {
        height: 100px;
        margin-right: 5px;
        cursor: pointer;
        display: inline-block; /* 이미지를 인라인 블록으로 표시하여 가로로 정렬 */
    }

    .imageGallery {
        overflow-x: auto; 
        overflow-y: hidden; /* 세로 스크롤 숨기기 */
        white-space: nowrap; 
        margin-bottom: 20px; 
        border: 1px solid #ccc; 
        padding: 10px;
    }
    /* 광고(홍보) 및 리뷰 목록의 스타일 */
	.content-list {
	    margin-top: 20px;
	    max-height: 200px;
	    overflow-y: auto; /* 세로 스크롤 사용 */
	    overflow-x: hidden; /* 가로 스크롤 숨기기 */
	    border: 1px solid #ccc;
	    padding: 10px;
	    box-sizing: border-box;
	}
	
	/* 광고(홍보) 및 리뷰 항목 내부 이미지 컨테이너 스타일 */
	.image-container {
	    overflow-x: auto; /* 가로 스크롤 사용 */
	    overflow-y: hidden; /* 세로 스크롤 숨기기 */
	    white-space: nowrap; /* 이미지를 한 줄로 배치 */
	    border: 1px solid #eee; /* 이미지 컨테이너 구분을 위한 보더 */
	    padding: 5px; /* 이미지와 컨테이너 경계 간의 여백 */
	    margin-top: 5px; /* 상단 여백 추가 */
	}
	
	/* 리뷰 및 광고 이미지를 가로로 정렬하고 클릭 가능하도록 설정 */
	.review-image, .advertising-image {
	    height: 100px;
	    margin-right: 5px;
	    cursor: pointer;
	    display: inline-block; /* 이미지를 인라인 블록으로 표시하여 가로로 정렬 */
	}
	    
</style>
<script src="https://dapi.kakao.com/v2/maps/sdk.js?appkey=67caf0129419f4cf4108be62dbe104d3&libraries=services"></script>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
<div id="map"></div>

<!-- Buttons -->
<div id="bookmarkButton" onclick="showBookmarkCreateModal()">★</div>
<div id="bookmarkListButton" onclick="showBookmarkListModal()">📋 북마크 리스트</div>
<div id="currentLocationButton" onclick="moveToCurrentLocation()">📍</div>

<!-- Bookmark Create Modal -->
<div id="bookmarkCreateModal">
    <div id="closeModalButton" onclick="hideBookmarkCreateModal()">X</div>
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

<!-- Bookmark List Modal -->
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

<!-- Cafe Info Modal (moved below Bookmark List Modal) -->
<div id="cafeInfoModal" style="width: 800px; height: 600px;">
    <div id="closeCafeInfoButton" onclick="hideCafeInfoModal()">X</div>
    <h3>카페 정보</h3>

    <!-- 이미지 갤러리 추가 -->
    <div id="imageGallery" class="imageGallery">
        <!-- 여기에 이미지가 동적으로 추가됩니다 -->
    </div>

    <p id="cafeInfoContent"></p>

    <div style="display: flex; justify-content: space-between;">
        <!-- 광고(홍보) 목록 영역 -->
        <div id="advertisingList" class="content-list" style="width: 48%;">
            <h4>홍보 목록</h4>
            <!-- JavaScript로 동적으로 홍보 목록이 추가될 것입니다 -->
        </div>

        <!-- 리뷰 목록 영역 -->
        <div id="reviewList" class="content-list" style="width: 48%;">
            <h4>리뷰 목록</h4>
            <!-- JavaScript로 동적으로 리뷰 목록이 추가될 것입니다 -->
        </div>
    </div>

    <!-- 홍보 추가 버튼 -->
    <button onclick="redirectToAdvertisingPage()">홍보 추가</button>
    
    <!-- 리뷰 추가 버튼 -->
    <button onclick="showReviewModal()">리뷰 추가</button>
</div>

<!-- Review Modal -->
<div id="reviewModal">
    <div id="closeReviewButton" onclick="hideReviewModal()">X</div>
    <h3>리뷰 작성</h3>
    <form id="reviewForm" action="<c:url value='/map/addreview?${_csrf.parameterName}=${_csrf.token}'/>" method="post" enctype="multipart/form-data">
        <input type="text" name="title" placeholder="리뷰 제목" required><br>
        <textarea name="content" placeholder="리뷰 내용" required></textarea><br>
        <label>별점:
            <select name="rating" required>
                <option value="1">1점</option>
                <option value="2">2점</option>
                <option value="3">3점</option>
                <option value="4">4점</option>
                <option value="5">5점</option>
            </select>
        </label><br>
        <!-- 리뷰작성 시 전송될 CN_Cafe에 필요한 정보와 사진들... -->
        <input type="file" name="images" multiple><br>
        <input type="hidden" name="cafeId" id="reviewCafeId"><br>
        <input type="hidden" name="cafeX" id="reviewCafeX"><br>
        <input type="hidden" name="cafeY" id="reviewCafeY"><br>
        <input type="hidden" name="cafeName" id="reviewCafeName"><br>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        <button type="submit">리뷰 등록</button>
    </form>
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
                        지번주소: ${cafe.addressName}<br>
                        도로명주소: ${cafe.roadAddressName}<br>
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
                    roadAddressName: '${cafe.roadAddressName}',
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

    window.showBookmarkCreateModal = function() {
        selectedCafes.clear(); // 북마크 모달 열 때 selectedCafes 초기화
        document.getElementById('bookmarkCreateModal').style.display = 'block';
        isBookmarkModalOpen = true;
    };

    window.hideBookmarkCreateModal = function() {
        document.getElementById('bookmarkCreateModal').style.display = 'none';
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
	//0902추가 - 카페에 등록된 리뷰 홍보들과 이미지 보이기
	window.showCafeInfoModal = function(cafe) {
	    console.log("마커를 선택했습니다");
	    var cafeModal = document.getElementById('cafeInfoModal');
	    cafeModal.style.display = 'block';
	    console.log("카페 데이터 확인: ", cafe); // 디버깅용 로그 추가
	
	    // 기존 카페 정보 설정
	    var cafeInfoContent = 
	        "<strong>이름:</strong> " + cafe.placeName + "<br>" +
	        "<strong>지번주소:</strong> " + cafe.addressName + "<br>" +
	        "<strong>도로명주소:</strong> " + cafe.roadAddressName + "<br>" +
	        "<strong>전화번호:</strong> " + cafe.phone + "<br>" +
	        "<strong>자세히 보기:</strong> <a href='" + cafe.placeUrl + "' target='_blank'>링크</a>";
	
	    document.getElementById('cafeInfoContent').innerHTML = cafeInfoContent;
	    
	    // 리뷰 작성 시 필요한 정보를 히든 필드에 설정
	    document.getElementById('reviewCafeId').value = cafe.cafeId;
	    document.getElementById('reviewCafeX').value = cafe.x;
	    document.getElementById('reviewCafeY').value = cafe.y;
	    document.getElementById('reviewCafeName').value = cafe.placeName;
	
	    // 리뷰와 홍보 이미지 가져오기
	    $.ajax({
	        url: '/map/getCafeMedia',
	        type: 'GET',
	        data: { cafeId: cafe.cafeId },
	        success: function(mediaItems) {
	            console.log("Received media items:", mediaItems);  // 서버 응답 데이터 확인을 위한 로그 추가
	
	            var reviewListHtml = '';
	            var advertisingListHtml = '';
	            var imageGalleryHtml = ''; // 이미지 갤러리 HTML
	
	            if (mediaItems.length > 0) {
	                mediaItems.forEach(function(item) {
	                    if (item.type === 'review') {
	                        // 리뷰 데이터 HTML 생성
	                        reviewListHtml += generateReviewHtml(item);
	
	                    } else if (item.type === 'advertising') {
	                        // 홍보 데이터 HTML 생성
	                        advertisingListHtml += generateAdvertisingHtml(item);
	                    }
	
	                    // 이미지 갤러리에 이미지 추가
	                    item.images.forEach(function(imagePath) {
	                        imageGalleryHtml += "<img src='" + imagePath + "' class='review-image' onclick='showImageFullScreen(\"" + imagePath + "\")' title='" + (item.type === 'review' ? 'Review Image' : 'Advertising Image') + "'>";
	                    });
	                });
	            } else { 
	                reviewListHtml = '<p>리뷰가 없습니다.</p>';
	                advertisingListHtml = '<p>홍보가 없습니다.</p>';
	            }
	
	            document.getElementById('reviewList').innerHTML = reviewListHtml;
	            document.getElementById('advertisingList').innerHTML = advertisingListHtml;
	            document.getElementById('imageGallery').innerHTML = imageGalleryHtml; // 이미지 갤러리에 이미지 추가
	        },
	        error: function(err) {
	            console.error("미디어 로딩 중 오류 발생: ", err);
	        }
	    });
	};

	// 홍보 추가 페이지로 리다이렉트
	window.redirectToAdvertisingPage = function() {
	    alert("마이페이지에서 카페를 등록하여 홍보를 작성해주세요");
	    window.location.href = '/user/home';
	};
	//리뷰랑 홍보 item 작성하는 메서드
	function generateReviewHtml(review) {
	    return "<div class='content-item'>" +
	        "<div style='font-weight: bold;'>" + review.userName + " (" + review.userId + ")</div>" +
	        "<div><strong>제목:</strong> " + review.title + "</div>" +
	        "<div><strong>내용:</strong> <div style='border: 1px solid #eee; padding: 5px; white-space: pre-line;'>" + review.content + "</div></div>" +
	        "<div><strong>별점:</strong> " + review.rating + "</div>" +
	        "<div><strong>좋아요:</strong> " + review.likes + "</div>" +
	        "<div><strong>작성일:</strong> " + review.createdDate + "</div>" +
	        "<strong>이미지:</strong><br>" + 
	        "<div class='image-container'>" + reviewImagesHtml(review.images) + "</div>" +  // 이미지 컨테이너로 감싸기
	        "</div>";
	}
	
	function generateAdvertisingHtml(advertising) {
	    var html = "<div class='content-item'>" +
	        "<div style='font-weight: bold;'>" + advertising.userName + " (" + advertising.userId + ")</div>" +
	        "<div><strong>제목:</strong> " + advertising.title + "</div>" +
	        "<div><strong>내용:</strong> <div class='advertising-content' style='border: 1px solid #eee; padding: 5px; white-space: pre-line;'>" + advertising.content + "</div></div>" +
	        "<div><strong>작성일:</strong> " + advertising.createdDate + "</div>" +
	        "<strong>이미지:</strong><br>" +
	        "<div class='image-container'>" + advertisingImagesHtml(advertising.images) + "</div>" +  // 이미지 컨테이너로 감싸기
	        "</div>";
	    return html;
	}
	
	function reviewImagesHtml(images) {
	    var reviewImagesHtml = '';
	    if (images && images.length > 0) {  // undefined 체크 추가
	        images.forEach(function(imagePath) {
	            reviewImagesHtml += "<img src='" + imagePath + "' class='review-image' onclick='showImageFullScreen(\"" + imagePath + "\")' title='Review Image'>";
	        });
	    } else {
	        reviewImagesHtml = '<p>이미지가 없습니다.</p>';
	    }
	    return reviewImagesHtml;
	}
	
	function advertisingImagesHtml(images) {
	    var advertisingImagesHtml = '';
	    if (images && images.length > 0) {  // undefined 체크 추가
	        images.forEach(function(imagePath) {
	            advertisingImagesHtml += "<img src='" + imagePath + "' class='advertising-image' onclick='showImageFullScreen(\"" + imagePath + "\")' title='Advertising Image' style='max-width: 200px; max-height: 150px;'>";
	        });
	    } else {
	        advertisingImagesHtml = '<p>이미지가 없습니다.</p>';
	    }
	    return advertisingImagesHtml;
	}
	
    //0828추가 리뷰의 이미지 크게 보이기
    // 이미지를 전체 화면으로 표시하는 함수 추가
	function showImageFullScreen(imagePath) {
	    // 전체 화면 표시 로직을 여기에 작성합니다.
	    // 예를 들어, 새 창에서 이미지를 표시하거나 모달 창을 사용할 수 있습니다.
	    var modal = document.createElement('div');
	    modal.style.position = 'fixed';
	    modal.style.top = 0;
	    modal.style.left = 0;
	    modal.style.width = '100%';
	    modal.style.height = '100%';
	    modal.style.backgroundColor = 'rgba(0,0,0,0.8)';
	    modal.style.display = 'flex';
	    modal.style.alignItems = 'center';
	    modal.style.justifyContent = 'center';
	    modal.style.zIndex = 9999;
	
	    var img = document.createElement('img');
	    img.src = imagePath;
	    img.style.maxWidth = '90%';
	    img.style.maxHeight = '90%';
	    img.style.cursor = 'pointer';
	
	    // 모달 클릭 시 닫기
	    modal.onclick = function () {
	        document.body.removeChild(modal);
	    };
	
	    modal.appendChild(img);
	    document.body.appendChild(modal);
	}


    window.hideCafeInfoModal = function() {
        document.getElementById('cafeInfoModal').style.display = 'none';
    };

    window.showReviewModal = function() {
        document.getElementById('reviewModal').style.display = 'block';
    };

    window.hideReviewModal = function() {
        document.getElementById('reviewModal').style.display = 'none';
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
};
</script>

<form id="filterForm" action="/map/filterByBookmark" method="post" style="display:none;">
    <input type="hidden" id="filterFormBookmarkId" name="bookmarkId"/>
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
</form>

</body>
</html>
