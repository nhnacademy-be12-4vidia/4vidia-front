function toggleLike(event, button) {
    event.preventDefault();
    event.stopPropagation();

    // 1. CSRF 토큰 확인
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
            if (response.status === 401) {
                if(confirm('로그인이 필요한 서비스입니다.\n로그인 페이지로 이동하시겠습니까?')) {
                    window.location.href = '/auth/login';
                }
                return; // 이후 로직 실행 안 함
            }

            // 성공 시 (200 OK)
            if (response.ok) {
                icon.classList.toggle('far');
                icon.classList.toggle('fas');
            } else {
                // 401도 아니고 성공도 아닌 경우 (500 에러 등)
                alert('요청 처리 중 문제가 발생했습니다. (상태: ' + response.status + ')');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('서버와 통신할 수 없습니다.');
        });
}