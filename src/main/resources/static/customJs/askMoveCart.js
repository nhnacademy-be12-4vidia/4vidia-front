/**
 * @param {string} bookId - 장바구니에 담을 상품 ID
 * @param {string} quantity - 수량 (일반적으로 1)
 */
function addToCartAndConfirmMove(bookId, quantity) {
    // Thymeleaf 폼의 action URL을 가져옵니다. (여기서는 /cart/add)
    const formActionUrl = '/cart/add';

    // 서버에 상품 추가를 요청하는 AJAX 통신 시작
    $.ajax({
        url: formActionUrl, // 폼의 제출 URL
        type: 'POST',
        data: {
            bookId: bookId,
            quantity: quantity
        },
        success: function(response) {
            // 1. 장바구니 추가 성공 후 팝업 띄우기
            const shouldMoveToCart = confirm("장바구니에 상품이 담겼습니다. 지금 장바구니 페이지로 이동하시겠습니까?");

            if (shouldMoveToCart) {
                // 2. 사용자가 "확인"을 클릭한 경우: 장바구니 페이지로 이동
                window.location.href = "/cart";
            } else {
                // 3. 사용자가 "취소"를 클릭한 경우: 현재 페이지에 머무름
                console.log("계속 쇼핑합니다.");
            }
        },
        error: function(xhr, status, error) {
            // 장바구니 추가 실패 시 처리
            alert("상품을 장바구니에 담는 데 실패했습니다. 오류: " + xhr.responseText);
        }
    });
}