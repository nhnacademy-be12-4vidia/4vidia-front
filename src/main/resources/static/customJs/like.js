function toggleLike(event, button) {
    event.preventDefault();
    event.stopPropagation();

    // 1. CSRF 토큰 확인 (비로그인 시 토큰이 없을 수 있음)
    const csrfToken = getCsrfToken();
    if (!csrfToken) {
        if(confirm('로그인이 필요한 서비스입니다.\n로그인 페이지로 이동하시겠습니까?')) {
            window.location.href = '/auth/login';
        }
        return;
    }

    const bookId = button.getAttribute('data-book-id');
    const icon = button.querySelector('i');
    const isLiked = icon.classList.contains('fas');

    let method = isLiked ? 'DELETE' : 'POST';
    let url = `/books/like?bookId=${bookId}`;

    fetch(url, {
        method: method,
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            "X-XSRF-TOKEN": csrfToken
        }
    })
        .then(response => {
            // 2. 리다이렉트 감지 (스프링 시큐리티가 로그인페이지로 보낼 경우)
            if (response.redirected) {
                if(confirm('로그인이 필요한 서비스입니다.\n이동하시겠습니까?')) {
                    window.location.href = '/auth/login';
                }
                return;
            }

            if (response.ok) {
                icon.classList.toggle('far');
                icon.classList.toggle('fas');
                // alert(isLiked ? `찜 취소` : `찜 등록`); // UX상 찜은 알림 없이 즉시 바뀌는게 좋습니다.
            }
            // [중요 수정] 401(인증 안됨) 응답 시 로그인 페이지로 이동
            else if (response.status === 401) {
                if(confirm('로그인이 필요한 서비스입니다.\n로그인 페이지로 이동하시겠습니까?')) {
                    window.location.href = '/auth/login';
                }
            }
            else if (response.status === 403) {
                alert('잘못된 접근입니다 (CSRF 토큰 만료 등). 새로고침 후 다시 시도해주세요.');
            }
            else {
                alert('처리 실패: 서버 오류 (상태: ' + response.status + ')');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('요청을 처리할 수 없습니다.');
        });
}