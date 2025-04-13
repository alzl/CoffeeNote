
 	// CSRF 토큰 설정
    var csrfToken = $('meta[name="_csrf"]').attr('content');
    var csrfHeader = $('meta[name="_csrf_header"]').attr('content');
        function resetPassword() {
            var userId = $('#userId').val();
            var email = $('#email').val();
            $.ajax({
                url: '/login/findPasswordProcess',
                type: 'POST',
                data: {
                    userId: userId,
                    email: email
                },
                beforeSend: function(xhr) {
                    xhr.setRequestHeader(csrfHeader, csrfToken); // CSRF 토큰을 헤더에 추가
                },
                success: function(response) {
                    if (response.success) {
                        $('#result').html('임시 비밀번호: <span style="color: green;">' + response.tempPassword + '</span>');
                    } else {
                        $('#result').html('<span style="color: red;">잘못된 정보입니다.</span>');
                    }
                }
            });
        }