function toggleLike(event, button) {
    event.preventDefault();
    event.stopPropagation();

    // ✅ 로그인 체크
    if (!window.IS_LOGGED_IN) {
        if (confirm('로그인이 필요한 서비스입니다.\n로그인 페이지로 이동하시겠습니까?')) {
            window.location.href = '/auth/login';
        }
        return;
    }

    const bookId = button.dataset.bookId;
    const icon = button.querySelector('i');
    const isLiked = icon.classList.contains('fas');

    const method = isLiked ? 'DELETE' : 'POST';
    const csrfToken = getCsrfToken();

    fetch(`/books/like?bookId=${bookId}`, {
        method,
        headers: {
            'X-XSRF-TOKEN': csrfToken
        }
    })
        .then(response => {
            if (!response.ok) {
                alert('요청 처리 중 문제가 발생했습니다.');
                return;
            }

            icon.classList.toggle('far');
            icon.classList.toggle('fas');
        })
        .catch(() => {
            alert('서버와 통신할 수 없습니다.');
        });
}
