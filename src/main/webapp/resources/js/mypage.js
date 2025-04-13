// 기존코드
//CSRF 토큰 설정
var csrfToken = $('meta[name="_csrf"]').attr('content');
var csrfHeader = $('meta[name="_csrf_header"]').attr('content');
	//0909추가 - 프로필 변경
	//프로필 변경 모달 닫기 기능 추가
	$('.close').click(function() {
	    $('#profileModal').hide();
	});
	
	// 모달 바깥 클릭 시 닫기
	$(window).click(function(event) {
	    if (event.target.id === 'profileModal') {
	        $('#profileModal').hide();
	    }
	});
	// 프로필 이미지 클릭 시 모달 표시
	$('#selectedProfileImg').click(function() {
	    $('#profileModal').show();
	});
	
	// 모달에서 이미지 클릭 시 선택한 이미지를 반영하고 서버에 업데이트 요청
	$('.profile-options img').click(function() {
	    const selectedImg = $(this).attr('src');
	    $('#selectedProfileImg').attr('src', selectedImg);  // 메인 화면에 선택된 이미지 표시
		showLoadingSpinner(); //로딩
	    // AJAX 요청으로 프로필 이미지 업데이트
	    $.ajax({
	        url: '/user/updateProfileImg',
	        method: 'POST',
	        data: { profileImg: selectedImg },
            beforeSend: function(xhr) {
                xhr.setRequestHeader(csrfHeader, csrfToken); // CSRF 토큰을 헤더에 추가
            },
	        success: function(response) {
	            alert(response.message);
	            $('#profileModal').hide();  // 모달 닫기
	        },
	        error: function(xhr, status, error) {
	            alert('프로필 이미지 변경 중 오류가 발생했습니다.');
	        }
	    });
	});

	// 로딩 버퍼 표시/숨김 함수
	function showLoadingSpinner() {
	    document.getElementById('loadingSpinner').style.display = 'block';
	}
	function hideLoadingSpinner() {
	    document.getElementById('loadingSpinner').style.display = 'none';
	}

	//0905추가 - 북마크 상세보기
	function viewBookmarkDetails(bookmarkId) {
        // 상세보기 버튼 클릭 시 mapview.jsp로 이동
        window.location.href = '/map/mapview/' + bookmarkId;
    }
	//0903 추가 - 북마크 삭제 확인 함수
	function confirmDeleteBookmark(bookmarkId) {
	    if (confirm("정말 이 북마크를 삭제하시겠습니까?")) {
			showLoadingSpinner(); //로딩버퍼 시작
	        $.ajax({
	            url: '/user/deleteBookmark',
	            method: 'POST',
	            data: { bookmarkId: bookmarkId },
	            success: function(response) {
	                alert(response.message);
	                location.reload();
	            },
	            error: function(xhr, status, error) {
	                alert("삭제 중 오류가 발생했습니다.");
	            },
	            complete: function() {
	                hideLoadingSpinner(); //로딩 버퍼 종료
	            }
	        });
	    }
	}
	//0902 추가 - 등록카페, 홍보글 삭제 버튼
	// 홍보글 삭제 확인 함수
    function confirmDeleteAdvertising(advertisingId) {
        if (confirm("정말 이 홍보글을 삭제하시겠습니까?")) {
        	showLoadingSpinner(); //로딩버퍼 시작
            $.ajax({
                url: '/user/deleteAdvertising',
                method: 'POST',
                data: { advertisingId: advertisingId },
                success: function(response) {
                    alert(response.message);
                    location.reload();
                },
                error: function(xhr, status, error) {
                    alert("삭제 중 오류가 발생했습니다.");
                },
	            complete: function() {
	                hideLoadingSpinner(); //로딩 버퍼 종료
	            }
            });
        }
    }
 	// 등록된 카페 삭제 확인 함수
    function confirmDeleteRegisteredCafe(cafeId) { // cafeId를 받아서 삭제 요청 수행
        if (confirm("정말 이 등록된 카페를 삭제하시겠습니까?")) {
        	showLoadingSpinner(); //로딩버퍼 시작
            $.ajax({
                url: '/user/deleteRegisteredCafe',
                method: 'POST',
                data: { cafeId: cafeId }, // cafeId를 전달
                success: function(response) {
                    alert(response.message);
                    location.reload();
                },
                error: function(xhr, status, error) {
                    alert("삭제 중 오류가 발생했습니다.");
                },
	            complete: function() {
	                hideLoadingSpinner(); //로딩 버퍼 종료
	            }
            });
        }
    }

	// 드롭다운 버튼 클릭 시 폼 열기/닫기
	$('#dropdown-btn').click(function() {
	    $('#dropdown-content').toggle();
	});
	
	// X 버튼 클릭 시 홍보 작성 폼 숨기기
	$(document).on('click', '#cancelButton', function() {
	    $('#advertisingForm').hide();  // 홍보 작성 폼 숨기기
	});
	
	// 검색된 카페 항목 클릭 시 선택 이벤트 추가
	$(document).on('click', '.cafe-item', function() {
	    // 모든 카페 항목에서 'selected' 클래스 제거
	    $('.cafe-item').removeClass('selected');
	    
	    // 현재 클릭된 항목에만 'selected' 클래스 추가
	    $(this).addClass('selected');
	    
	    const selectedCafeName = $(this).data('place-name');
	    const selectedCafeId = $(this).data('id');
	    const selectedCafeX = $(this).data('x');
	    const selectedCafeY = $(this).data('y');
	    
	    $('#selectedCafeName').text(selectedCafeName);
	    $('#selectedCafeId').val(selectedCafeId);
	    $('#selectedCafeX').val(selectedCafeX);
	    $('#selectedCafeY').val(selectedCafeY);
	    $('#selectedCafePlaceName').val(selectedCafeName);
	    $('#businessRegForm').show();
	});
	
    //0902 추가 등록된 카페 항목 클릭 시 홍보 작성 폼 표시
	$('.registered-cafe-item').click(function() {
	    // 기존 선택된 항목을 초기화하고 선택한 항목 스타일 변경
	    $('.registered-cafe-item').removeClass('selected');
	    $(this).addClass('selected');
	    
	    //0927추가 - 내가 선택한 등록카페의 이름 가져와서 홍보 작성 폼에 띄우기
	    var placeName = $(this).find('#mypage-strong').text();
		$('#currentCafe').html('\"' + placeName + '\"' + ' 카페의' + '&nbsp;');
		
	    // 선택된 카페의 ID를 홍보 작성 폼에 설정
	    const selectedCafeId = $(this).data('cafe-id');
	    $('#advertisingCafeId').val(selectedCafeId);
	
	    // 디버그 로그 추가 - 선택한 카페 ID가 제대로 설정되는지 확인 (필요없어서 뺌)
	    //console.log("홍보 작성 폼 열기: 선택된 카페 ID:", selectedCafeId);
	
	    // 홍보 작성 폼 초기화 및 표시
	    $('#createAdvertisingForm')[0].reset();  // 폼 초기화
	    $('#advertisingForm').show();  // 홍보 폼 표시
	});
	
	//0902 - 홍보 폼 제출 전에 데이터를 확인하는 함수 추가
    function validateAdvertisingForm() {
        const cafeId = $('#advertisingCafeId').val();
        const title = $('input[name="title"]').val();
        //const content = $('input[name="content"]').val(); //input태그로 하면 여러줄 안됨
        const content = $('textarea[name="content"]').val();  // 수정된 부분
        const images = $('input[name="images"]')[0].files;

        // 디버그 로그 - 폼 제출 시 데이터 확인
        console.log("홍보 글 제출");
        console.log("Cafe ID:", cafeId);
        console.log("Title:", title);
        console.log("Content:", content);
        console.log("Images count:", images.length);
        for (let i = 0; i < images.length; i++) {
            console.log("Image " + (i + 1) + ": " + images[i].name);
        }

        if (!cafeId) {
            alert("카페를 선택해주세요.");
            return false;
        }
        if (!title || !content) {
            alert("제목과 설명을 모두 입력해주세요.");
            return false;
        }

        return true;
    }
    
	//0902 홍보 폼 제출 시 validateAdvertisingForm 함수를 호출하도록 설정
	$('#createAdvertisingForm').on('submit', function(e) {
	    if (!validateAdvertisingForm()) {
	        e.preventDefault();  // 폼 제출 중지
	    }
	});

    // 이름칸의 '수정' 버튼 클릭 시 비밀번호 입력 창 표시
    $('#updateNameBtn').click(function() {
        $('#namePasswordModal').show();
    });

    // 비밀번호 확인 버튼 클릭 시
    $('#nameConfirmPasswordBtn').click(function() {
        const password = $('#namePassword').val();

        if (password === '') {
            $('#namePasswordError').text('비밀번호를 입력하세요.');
            return;
        }
        showLoadingSpinner(); //로딩버퍼 시작

        // 비밀번호 확인 Ajax 요청
        $.ajax({
            url: '/user/verifyPassword',  // 비밀번호 검증을 위한 URL
            method: 'GET',
            data: { password: password },
            success: function(response) {
                if (response.match) {
                    // 비밀번호가 일치하면 폼 제출
                    document.getElementById('nameUpdateForm').submit();
                } else {
                    $('#namePasswordError').text('비밀번호가 일치하지 않습니다.');
                }
            },
            error: function(xhr, status, error) {
                console.log("Error response: ", xhr.responseText);
                $('#namePasswordError').text('서버 오류가 발생했습니다. 다시 시도해주세요.');
            },
            complete: function() {
	            hideLoadingSpinner(); //로딩버퍼종료
	        }
        });
    });
    
	 // 이메일칸의 '수정' 버튼 클릭 시 비밀번호 입력 창 표시
    $('#updateEmailBtn').click(function() {
        $('#emailPasswordModal').show();
    });
	
    // 비밀번호 확인 버튼 클릭 시
    $('#emailConfirmPasswordBtn').click(function() {
        const password = $('#emailPassword').val();
        const email = $('#userEmail').val(); // 이메일 입력값 가져오기
        if (password === '') {
            $('#emailPasswordError').text('비밀번호를 입력하세요.');
            return;
        }
        //이메일 유효성 검사
        if (!validateEmail(email)) {
	        $('#emailPasswordError').text('유효하지 않은 이메일 형식입니다.');
	        return;
	    }
        showLoadingSpinner(); //로딩버퍼 시작
		
        // 비밀번호 확인 Ajax 요청
        $.ajax({
            url: '/user/verifyPassword',  // 비밀번호 검증을 위한 URL
            method: 'GET',
            data: { password: password },
            success: function(response) {
                if (response.match) {
                    // 비밀번호가 일치하면 폼 제출
                    document.getElementById('emailUpdateForm').submit();
                } else {
                    $('#emailPasswordError').text('비밀번호가 일치하지 않습니다.');
                }
            },
            error: function(xhr, status, error) {
                console.log("Error response: ", xhr.responseText);
                $('#emailPasswordError').text('서버 오류가 발생했습니다. 다시 시도해주세요.');
            },
            complete: function() {
	            hideLoadingSpinner(); //로딩버퍼 종료
	        }
        });
    });
    
 	// 0828 사업자 등록번호 수정 클릭 시
	// 사업장 추가 버튼 클릭 시 비밀번호 확인 모달 창 표시
    $('#registerBusinessBtn').click(function() {
        $('#registerBusinessPasswordModal').show();
    });

    // 사업장 추가 비밀번호 확인 버튼 클릭 시
    $('#registerBusinessConfirmPasswordBtn').click(function() {
        const password = $('#registerBusinessPassword').val();
        const businessRegNum = $('#businessRegNum').val();

        if (password === '') {
            $('#registerBusinessPasswordError').text('비밀번호를 입력하세요.');
            return;
        }

        if (!validateBusinessReg(businessRegNum)) {
            $('#registerBusinessPasswordError').text('유효한 사업자 등록번호를 입력하세요.');
            return;
        }
        showLoadingSpinner(); //로딩버퍼 시작

        // 비밀번호 확인 Ajax 요청
        $.ajax({
            url: '/user/verifyPassword',
            method: 'GET',
            data: { password: password },
            //0920 얘만 post 보안오류 나서 추가함
            beforeSend: function(xhr) {
            	xhr.setRequestHeader(csrfHeader, csrfToken); // CSRF 토큰을 헤더에 추가
        	},
            success: function(response) {
                if (response.match) {
                    document.getElementById('registerBusinessForm').submit();
                } else {
                    $('#registerBusinessPasswordError').text('비밀번호가 일치하지 않습니다.');
                }
            },
            error: function(xhr, status, error) {
                $('#registerBusinessPasswordError').text('서버 오류가 발생했습니다. 다시 시도해주세요.');
            },
            complete: function() {
	            hideLoadingSpinner();
	        }
        });
    });
    
    //0829 카페 검색  추가
	$(document).ready(function() {
	    // CSRF 토큰을 전역 Ajax 설정에 추가
	    var token = $("meta[name='_csrf']").attr("content");
	    var header = $("meta[name='_csrf_header']").attr("content");

	    $.ajaxSetup({
	        beforeSend: function(xhr) {
	            xhr.setRequestHeader(header, token);
	        }
	    });
	
	    // 검색 버튼 클릭 시 카페 검색
	    $('#searchCafeBtn').click(function() {
	        const keyword = $('#searchKeyword').val();
	
	        if (keyword.trim() === '') {
	            alert('검색어를 입력하세요.');
	            return;
	        }
	        showLoadingSpinner(); //로딩버퍼 시작
	
	        // Ajax 요청으로 카페 검색 수행
	        $.ajax({
	            url: '/user/searchmap',  // 검색 처리할 URL (CN_UserController에서 처리)
	            method: 'POST',
	            data: { keyword: keyword },
	            beforeSend: function(xhr) {
	                xhr.setRequestHeader(header, token); // CSRF 토큰 추가
	            },
	            success: function(response) {
	                console.log("Response from server:", response);  // response 출력하여 디버깅
	                if (Array.isArray(response)) {
	                    $('#cafeList').empty();  // 기존 검색 결과 초기화
	                    if (response.length > 0) {
	                        $('#cafeListForm').show();  // 검색 결과 섹션 표시
	                        response.forEach(function(cafe) {
	                        	// 상세 정보를 포함한 카페 리스트 항목 생성
	                        	$('#cafeList').append(
	                        	    '<li class="cafe-item" data-id="' + cafe.id + '" data-x="' + cafe.x + '" data-y="' + cafe.y + '" data-place-name="' + cafe.place_name + '">' +
	                        	    '<strong>' + cafe.place_name + '</strong><br>' +
	                        	    '주소: ' + cafe.address_name + '<br>' +
	                        	    '도로명 주소: ' + cafe.road_address_name + '<br>' +
	                        	    '전화번호: ' + (cafe.phone ? cafe.phone : '전화번호 정보 없음') + '<br>' +
	                        	    '<a href="' + cafe.place_url + '" target="_blank">상세 정보 보기</a>' +
	                        	    '</li>'
	                        	);
	                        });
	
	                        // 검색된 카페 항목 클릭 시 선택 이벤트 추가
	                        $('.cafe-item').click(function() {
	                            $(this).toggleClass('selected');
	                            const selectedCafeName = $(this).data('place-name');
	                            const selectedCafeId = $(this).data('id');
	                            const selectedCafeX = $(this).data('x');
	                            const selectedCafeY = $(this).data('y');
	                            
	                            $('#selectedCafeName').text(selectedCafeName);
	                            $('#selectedCafeId').val(selectedCafeId);
	                            $('#selectedCafeX').val(selectedCafeX);
	                            $('#selectedCafeY').val(selectedCafeY);
	                            $('#selectedCafePlaceName').val(selectedCafeName);
	                            $('#businessRegForm').show();
	                        });
	                    } else {
	                        $('#cafeListForm').hide();
	                        alert('검색된 카페가 없습니다.');
	                    }
	                } else {
	                    console.error("Expected an array but got:", response);
	                    alert('서버 응답이 올바르지 않습니다.');
	                }
	            },
	            error: function(xhr, status, error) {
	                console.error("Error occurred while searching cafes: ", xhr.responseText);
	                alert('카페 검색 중 오류가 발생했습니다.');
	            },
	            complete: function() {
		            hideLoadingSpinner(); //로딩버퍼 종료
		        }
	        });
	    });
	});

    // 0828 사업자 등록번호 유효성 검사
    function validateBusinessReg(businessRegNum) {
        // 사업자 등록번호는 10자리 숫자로 되어있다고 가정
        const regEx = /^[0-9]{10}$/;
        return regEx.test(businessRegNum);
    }
    
    // 이메일 유효성 검사 함수
    function validateEmail(email) {
        const re = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
        return re.test(email);
    }
    
    //북마크 리스트 토글 기능 함수
      function toggleBookmarkList() {
        var bookmarkUl = document.getElementById("userBookmarkList");
        var toggleIcon = document.getElementById("mypage-toggleIcon");

        if (bookmarkUl.style.display === "none") {
            bookmarkUl.style.display = "block";
            toggleIcon.textContent = "▲"; // 아이콘 변경 (열림)
        } else {
            bookmarkUl.style.display = "none";
            toggleIcon.textContent = "▼"; // 아이콘 변경 (닫힘)
        }
    }
    
    //내가 좋아요한 북마크 리스트 토글 기능 함수
      function toggleLikedBookmarkList() {
        var likedBookmarkUl = document.getElementById("likedBookmarkList");
        var likedToggleIcon = document.getElementById("mypage-likedToggleIcon");

        if (likedBookmarkUl.style.display === "none") {
            likedBookmarkUl.style.display = "block";
            likedToggleIcon.textContent = "▲"; // 아이콘 변경 (열림)
        } else {
            likedBookmarkUl.style.display = "none";
            likedToggleIcon.textContent = "▼"; // 아이콘 변경 (닫힘)
        }
    }
    
    // 등록된 카페 리스트 토글 기능 함수
    function toggleRegisteredCafeList() {
        var registeredCafeList = document.getElementById("registeredCafeList");
        var cafeToggleIcon = document.getElementById("cafeToggleIcon");

        if (registeredCafeList.style.display === "none") {
            registeredCafeList.style.display = "block";
            cafeToggleIcon.textContent = "▲"; // 아이콘 변경 (열림)
        } else {
            registeredCafeList.style.display = "none";
            cafeToggleIcon.textContent = "▼"; // 아이콘 변경 (닫힘)
        }
    }
   
   	// 사업장 등록 비밀번호 확인 모달 함수
	function openPasswordPopup() {
	    document.getElementById("registerBusinessPasswordModal").style.display = "block";
	}
	
	function closePasswordPopup() {
	    document.getElementById("emailPasswordModal").style.display = "none";
	    document.getElementById("namePasswordModal").style.display = "none";
	    document.getElementById("registerBusinessPasswordModal").style.display = "none";
	    
	}
	
	document.getElementById('cafeToggleIcon').addEventListener('click', function() {
    document.getElementById('cafeToggleIcon').scrollIntoView({ behavior: 'smooth' });
});