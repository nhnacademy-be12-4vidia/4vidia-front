// 좋아요/찜하기 토글 (AJAX/Fetch API 사용)
function toggleLike(event, button) {
    // 이벤트 버블링을 막아 상위 링크(<a>)가 실행되는 것을 방지합니다.
    event.preventDefault();
    event.stopPropagation();

    const bookId = button.getAttribute('data-book-id');
    const icon = button.querySelector('i'); // 아이콘 요소 가져오기
    const isLiked = icon.classList.contains('fas');

    let method = isLiked ? 'DELETE' : 'POST';
    let url = `/mypage/like/test?bookId=${bookId}`;

    const csrfToken = getCsrfToken();

    fetch(url, {
        method: method,
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            "X-XSRF-TOKEN": csrfToken // ★ 헤더 추가
        }
    })
        .then(response => {
            if (response.ok) {
                icon.classList.toggle('far');
                icon.classList.toggle('fas');

                alert(isLiked ? `[${bookId}] 찜 취소되었습니다.` : `[${bookId}] 찜 등록되었습니다!`);

            } else if (response.status === 401) {
                alert('처리 실패: 로그인이 필요합니다.');
            } else {
                alert('처리 실패: 서버 오류이거나 잘못된 요청입니다. (상태: ' + response.status + ')');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('요청 중 네트워크 오류가 발생했습니다.');
        });
}

// 페이지네이션 링크 클릭 시 이 함수를 호출하여 페이지 이동을 강제합니다.
function handleDelayedRefresh(event) {
    // 기본 링크 이동을 막습니다.
    event.preventDefault();

    // 클릭된 <a> 태그의 href 속성에서 이동할 최종 URL을 가져옵니다.
    const targetUrl = event.currentTarget.href;

    if (targetUrl) {
        // 새로고침을 통해 서버에서 최신 좋아요 상태를 반영한 페이지를 로드합니다.
        window.location.href = targetUrl;
    }
}