let isIdChecked = false;
let isEmailChecked = false;

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
		$('#profileModal').hide();
	    const selectedImg = $(this).attr('src');
	    $('#selectedProfileImg').attr('src', selectedImg);  // 메인 화면에 선택된 이미지 표시
	    // hidden input의 value를 업데이트
    	$('#profileImg').val(selectedImg);
	
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

	$('#changeProfile').click(function(){
		$('#profileModal').show();
	});
	
// 아이디 중복 체크 버튼 클릭
$('#checkDuplicateId').click(function() {
    const userId = $('#userId').val();
    $('#idError').text('');  // 초기화
    $('#idSuccess').text('');
    
    if (userId === '') {
        $('#idError').text('아이디를 입력하세요.');
        return;
    }
    // Ajax 요청으로 아이디 중복 체크 수행
    $.ajax({
        url: '/login/check-id-duplicate',
        method: 'GET',
        data: { id: userId },
        success: function(response) {
            if (response.exists) {
                $('#idError').text('이미 사용 중인 아이디입니다.');
                isIdChecked = false;
            } else {
                $('#idSuccess').text('사용 가능한 아이디입니다.');
                isIdChecked = true;
            }
        },
        error: function(xhr, status, error) {
        	console.log("Error response: ", xhr.responseText);
            $('#idError').text('서버 오류가 발생했습니다. 다시 시도해주세요.');
        }
    });
});

// 이메일 중복 체크 버튼 클릭
$('#checkDuplicateEmail').click(function() {
    const email = $('#email').val();
    $('#emailError').text('');  // 초기화
    $('#emailSuccess').text('');
    
    if (email === '') {
        $('#emailError').text('이메일을 입력하세요.');
        return;
    }
    
    if (!validateEmail(email)) {
        $('#emailError').text('올바른 이메일을 입력하세요.');
        return;
    }

    // 이메일 유효성 검사가 통과된 경우에만 중복 체크
    $.ajax({
        url: '/login/check-email-duplicate',
        method: 'GET',
        data: { email: email },
        success: function(response) {
            if (response.exists) {
                $('#emailError').text('이미 사용 중인 이메일입니다.');
                isEmailChecked = false;
            } else {
                $('#emailSuccess').text('사용 가능한 이메일입니다.');
                isEmailChecked = true;
            }
        },
        error: function(xhr, status, error) {
        	console.log("Error response: ", xhr.responseText);
            $('#emailError').text('서버 오류가 발생했습니다. 다시 시도해주세요.');
        }
    });
});

// 비밀번호와 비밀번호 확인 일치 여부 확인
$('#password, #confirmPassword').on('input', function() {
    const password = $('#password').val();
    const confirmPassword = $('#confirmPassword').val();
    
    if (password === '') {
        $('#passwordError').text('비밀번호를 입력하세요.');
    } else {
        $('#passwordError').text('');
    }
    
    if (password !== confirmPassword) {
        $('#confirmPasswordError').text('비밀번호가 일치하지 않습니다.');
    } else {
        $('#confirmPasswordError').text('');
    }
});

// 이메일 유효성 검사 함수
function validateEmail(email) {
    const re = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    return re.test(email);
}

// 회원가입 폼 제출 시 유효성 검사
$('#registerForm').submit(function(event) {
    // 아이디 중복 체크 확인
    if (!isIdChecked) {
        $('#idError').text('아이디 중복 체크를 해주세요.');
        event.preventDefault();
    }

    // 이메일 중복 체크 확인
    if (!isEmailChecked) {
        $('#emailError').text('이메일 중복 체크를 해주세요.');
        event.preventDefault();
    }

    // 비밀번호 확인
    const password = $('#password').val();
    const confirmPassword = $('#confirmPassword').val();
    if (password !== confirmPassword) {
        $('#confirmPasswordError').text('비밀번호가 일치하지 않습니다.');
        event.preventDefault();
    }

    // 이메일 유효성 검사
    const email = $('#email').val();
    if (email === '' || !validateEmail(email)) {
        $('#emailError').text('올바른 이메일을 입력하세요.');
        event.preventDefault();
    }

    // userName이 비어있는 경우 기본값 '커피러버' 설정
    const userName = $('#userName').val();
    if (userName.trim() === '') {
        $('#userName').val('커피러버');
    }
});