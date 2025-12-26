function toggleLike(event, button) {
    event.preventDefault();
    event.stopPropagation();

    // 1. CSRF 토큰 확인 (아예 토큰도 없는 상태면 바로 로그인 유도)
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
            // 2. 리다이렉트 감지 (스프링 시큐리티가 302로 보냈을 경우)
            if (response.redirected) {
                if(confirm('로그인이 필요한 서비스입니다.\n로그인 페이지로 이동하시겠습니까?')) {
                    window.location.href = '/auth/login';
                }
                return;
            }

            if (response.ok) {
                // 성공 시 하트 아이콘 변경
                icon.classList.toggle('far');
                icon.classList.toggle('fas');
            }
            // [여기가 핵심 수정!] 401 (Unauthorized) 응답이 왔을 때
            else if (response.status === 401) {
                // 기존: alert('처리 실패: 로그인이 필요합니다.');
                // 수정: 확인 창 띄우고 로그인 페이지로 이동
                if(confirm('로그인이 필요한 서비스입니다.\n로그인 페이지로 이동하시겠습니까?')) {
                    window.location.href = '/auth/login'; // 로그인 페이지 URL 확인 필요
                }
            }
            // 403 (Forbidden) - CSRF 토큰 만료 등
            else if (response.status === 403) {
                alert('보안 토큰이 만료되었습니다. 새로고침 후 다시 시도해주세요.');
                location.reload();
            }
            else {
                alert('처리 실패: 서버 오류 (상태: ' + response.status + ')');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            // 네트워크 에러 시에도 로그인 문제일 가능성이 높음
            if(confirm('요청을 처리할 수 없습니다.\n로그인 상태를 확인하시겠습니까?')) {
                window.location.href = '/auth/login';
            }
        });
}