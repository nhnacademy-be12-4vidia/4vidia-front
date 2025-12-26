function toggleLike(event, button) {
    event.preventDefault();
    event.stopPropagation();

    // [수정 1] 요청 전 로그인 상태(CSRF 토큰 유무)를 먼저 확인합니다.
    // getCsrfToken()이 null이나 빈 값을 반환한다고 가정합니다.
    const csrfToken = getCsrfToken();

    if (!csrfToken) {
        if(confirm('로그인이 필요한 서비스입니다.\n로그인 페이지로 이동하시겠습니까?')) {
            window.location.href = '/login'; // 실제 로그인 URL로 변경해주세요
        }
        return; // 서버 요청을 보내지 않고 종료
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
            // [수정 2] 리다이렉트(302)가 발생하여 로그인 페이지(HTML)가 응답으로 온 경우 체크
            if (response.redirected) {
                alert('로그인이 필요합니다.');
                return; // 더 이상 처리하지 않음
            }

            if (response.ok) {
                icon.classList.toggle('far');
                icon.classList.toggle('fas');
                alert(isLiked ? `[${bookId}] 찜 취소되었습니다.` : `[${bookId}] 찜 등록되었습니다!`);
            } else if (response.status === 401 || response.status === 403) {
                // 403은 CSRF 토큰 오류일 수도 있지만, 비로그인 상태일 때도 자주 발생
                alert('처리 실패: 로그인이 필요합니다.');
            } else {
                alert('처리 실패: 서버 오류 (상태: ' + response.status + ')');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            // 네트워크 오류가 떴다는 것은 보통 CORS나 리다이렉트 문제이므로
            // 사용자에게는 '로그인이 필요하거나 오류가 발생했다'고 안내하는 것이 안전합니다.
            alert('요청을 처리할 수 없습니다. 로그인 상태를 확인해주세요.');
        });
}