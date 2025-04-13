// CSRF 토큰 설정
    var csrfToken = $('meta[name="_csrf"]').attr('content');
    var csrfHeader = $('meta[name="_csrf_header"]').attr('content');
        function findUserId() {
            var email = $('#email').val();
            $.ajax({
                url: '/login/findIdProcess',
                type: 'POST',
                data: { email: email },
                beforeSend: function(xhr) {
                    xhr.setRequestHeader(csrfHeader, csrfToken); // CSRF 토큰을 헤더에 추가
                },
                success: function(response) {
                    if (response.exists) {
                        $('#result').html('당신의 아이디는: <span style="color: green;">' + response.userId + '</span>');
                    } else {
                        $('#result').html('<span style="color: red;">해당 이메일을 사용하는 유저가 없습니다.</span>');
                    }
                }
            });
        }