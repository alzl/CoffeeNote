<!-- 0828-북마크 잘 되고, 카페에 리뷰추가 및 사진 추가도 됨, 사진들을 보여주는 건 아직 안함 -->
<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
        top: 60px; /* Adjusted to appear below buttons */
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
        top: 50%; /* Center vertically */
        left: 50%; /* Center horizontally */
        transform: translate(-50%, -50%);
        z-index: 2;
        background-color: white;
        padding: 20px;
        border-radius: 5px;
        box-shadow: 0px 0px 15px rgba(0, 0, 0, 0.5);
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
    /* cafeInfoModal 스타일 조정 */
	#reviewList {
	    margin-top: 20px;
	    max-height: 200px; /* 원하는 높이 설정 */
	    overflow-y: auto;  /* 내용이 넘칠 경우 스크롤 사용 */
	    white-space: pre-line; /* 여러 줄 텍스트를 유지 */
	}
	
	.review-item {
	    margin-bottom: 10px;
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
<div id="cafeInfoModal">
    <div id="closeCafeInfoButton" onclick="hideCafeInfoModal()">X</div>
    <h3>카페 정보</h3>
    <p id="cafeInfoContent"></p>
        <!-- 리뷰 리스트를 표시할 영역 추가 -->
    <div id="reviewList">
        <!-- 여기에서 JavaScript를 통해 리뷰가 추가될 것입니다 -->
    </div>
    <button onclick="showReviewModal()">리뷰 추가</button> <!-- 리뷰 추가 버튼 -->
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

    window.showCafeInfoModal = function(cafe) {
    	console.log("마커를 선택했습니다")
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

        // 리뷰 가져오기 - 0828 추가
        $.ajax({
            url: '/map/getCafeReviews',
            type: 'GET',
            data: { cafeId: cafe.cafeId },
            success: function(reviews) {
                var reviewListHtml = '';
                if (reviews.length > 0) {
                    reviews.forEach(function(review) {
                    	console.log("리뷰 데이터 확인: ", review); // 디버깅용 로그 추가
                        reviewListHtml += 
                            "<div class='review-item' style='border: 1px solid #ccc; padding: 10px; margin-bottom: 10px;'>" +
                            "<div style='font-weight: bold;'>" + review.userName + " (" + review.userId + ")</div>" +
                            "<div><strong>제목:</strong> " + review.reviewTitle + "</div>" +
                            "<div><strong>내용:</strong> <div style='border: 1px solid #eee; padding: 5px; white-space: pre-line;'>" + review.reviewContent + "</div></div>" +
                            "<div><strong>별점:</strong> " + review.rating + "</div>" +
                            "<div><strong>좋아요:</strong> " + review.likes + "</div>" +
                            "<div><strong>작성일:</strong> " + review.createdDate + "</div>" +
                        "</div>";
                    });
                } else { //리뷰가 하나도 없는 경우
                    reviewListHtml = '<p>리뷰가 없습니다.</p>';
                }
                document.getElementById('reviewList').innerHTML = reviewListHtml;
            },
            error: function(err) {
                console.error("리뷰 로딩 중 오류 발생: ", err);
            }
        });
    };

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
