// /static/js/cart-guest-merge.js

document.addEventListener("DOMContentLoaded", function () {
    if (!LOGGED_IN) {
        // 비로그인 상태 → 모달 로직 건너뜀
        return;
    }
    const csrfToken = getCsrfToken();

    // ✅ 1. 비회원 장바구니 상태 조회 → 모달 표시 여부 결정
    fetch('/cart/guest/status', {
        method: 'GET',
        credentials: 'same-origin',
        headers: {"X-XSRF-TOKEN": csrfToken}
    })
        .then(function (res) {
            if (!res.ok) {
                return null;
            }
            return res.json();
        })
        .then(function (data) {
            if (!data) return;

            if (data.hasGuestCart) {
                $('#guestCartMergeModal').modal('show');
            }
        })
        .catch(function (err) {
            console.error('guest cart status error:', err);
        });

    // ✅ 2. "예" 버튼: 비회원 → 회원 장바구니 병합
    const yesBtn = document.getElementById('guestCartYesBtn');
    if (yesBtn) {
        yesBtn.addEventListener('click', function () {

            // ★ 쿠키 가져오기
            const csrfToken = getCsrfToken();

            fetch('/cart/merge-guest', {
                method: 'POST',
                credentials: 'same-origin',
                headers: {
                    "X-XSRF-TOKEN": csrfToken // [수정] headers 내부로 이동
                }
            })
                .then(function (res) {
                    if (!res.ok) {
                        throw new Error('merge guest cart failed');
                    }
                    location.reload();
                })
                .catch(function (err) {
                    console.error(err);
                    alert('장바구니 병합 중 오류가 발생했습니다.');
                });
        });
    }

    // ✅ 3. "아니오" 버튼: 비회원 장바구니만 삭제
    const noBtn = document.getElementById('guestCartNoBtn');

    if (noBtn) {
        noBtn.addEventListener('click', function () {
            // ★ 쿠키 가져오기
            const csrfToken = getCsrfToken();

            fetch('/cart/guest', {
                method: 'DELETE',
                credentials: 'same-origin',
                headers: {
                    "X-XSRF-TOKEN": csrfToken // ★ 헤더 추가
                }
            })
                .then(function (res) {
                    if (!res.ok) {
                        throw new Error('guest cart clear failed');
                    }
                    $('#guestCartMergeModal').modal('hide');
                    location.reload();
                })
                .catch(function (err) {
                    console.error(err);
                    alert('비회원 장바구니 삭제 중 오류가 발생했습니다.');
                });
        });
    }
});
