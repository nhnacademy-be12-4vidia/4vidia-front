
// /static/js/cart.js
document.addEventListener("DOMContentLoaded", function () {
    const selectAll = document.getElementById("selectAll");
    const rows = Array.from(document.querySelectorAll(".cart-item-row"));
    const selectedTotalAmount = document.getElementById("selectedTotalAmount");

    // 장바구니가 비었으면 그냥 종료
    if (!selectedTotalAmount || rows.length === 0) {
        return;
    }

    function formatNumber(num) {
        return num
            .toString()
            .replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    }

    // row 단위 lineTotal 계산: 단가 × 수량
    function calcRowLineTotal(row) {
        const unitPrice = parseInt(row.dataset.unitPrice || "0", 10);
        const qtyInput = row.querySelector(".cart-qty-input");
        const qty = parseInt(qtyInput?.value || "0", 10);
        return unitPrice * qty;
    }

    // row의 “상품 금액” 텍스트 업데이트
    function updateRowLineTotalDisplay(row) {
        const lineTotal = calcRowLineTotal(row);
        const span = row.querySelector(".line-total-text");
        if (span) {
            span.textContent = formatNumber(lineTotal) + "원";
        }
    }

    // 체크된 아이템들의 총합 계산
    function recalcSelectedTotal() {
        let total = 0;
        rows.forEach(row => {
            const checkbox = row.querySelector(".cart-item-checkbox");
            if (checkbox && checkbox.checked) {
                total += calcRowLineTotal(row);
            }
        });
        selectedTotalAmount.textContent = formatNumber(total) + "원";
    }

    // ✅ 초기: 모두 체크 + 각 row 금액 표시
    rows.forEach(row => {
        const checkbox = row.querySelector(".cart-item-checkbox");
        if (checkbox) {
            checkbox.checked = true;
        }
        updateRowLineTotalDisplay(row);
    });

    if (selectAll) {
        selectAll.checked = true;
        selectAll.indeterminate = false;
    }
    recalcSelectedTotal();

    // ✅ 개별 체크박스 변경 시: 전체선택 상태/총합 갱신
    rows.forEach(row => {
        const checkbox = row.querySelector(".cart-item-checkbox");
        if (!checkbox) return;

        checkbox.addEventListener("change", function () {
            const allChecked = rows.every(r => {
                const cb = r.querySelector(".cart-item-checkbox");
                return cb && cb.checked;
            });
            const noneChecked = rows.every(r => {
                const cb = r.querySelector(".cart-item-checkbox");
                return !cb || !cb.checked;
            });

            if (selectAll) {
                selectAll.checked = allChecked;
                selectAll.indeterminate = !allChecked && !noneChecked;
            }

            recalcSelectedTotal();
        });
    });

    // ✅ 전체 선택 체크박스
    if (selectAll) {
        selectAll.addEventListener("change", function () {
            rows.forEach(row => {
                const checkbox = row.querySelector(".cart-item-checkbox");
                if (checkbox) {
                    checkbox.checked = selectAll.checked;
                }
            });
            selectAll.indeterminate = false;
            recalcSelectedTotal();
        });
    }
    // ✅ 수량 변경 시: 라인 금액 + 총합 실시간 업데이트 + 백엔드로 PUT 요청
    rows.forEach(row => {
        const qtyInput = row.querySelector(".cart-qty-input");
        const checkbox = row.querySelector(".cart-item-checkbox");
        if (!qtyInput || !checkbox) return;

        const bookId = checkbox.value;  // 체크박스 value = bookId

        function onQtyChangeOnlyView() {
            // 최소 1 이상 유지
            if (!qtyInput.value || parseInt(qtyInput.value, 10) < 1) {
                qtyInput.value = 1;
            }
            updateRowLineTotalDisplay(row);
            recalcSelectedTotal();
        }

        // 타이핑 중 → 금액만 실시간 반영
        qtyInput.addEventListener("input", onQtyChangeOnlyView);

        // 입력 확정(change) → 화면 반영 + 백엔드에 PUT 요청
        qtyInput.addEventListener("change", function () {
            onQtyChangeOnlyView();

            const quantity = parseInt(qtyInput.value, 10);

            // 👉 여기 URL은 실제 백엔드(cart-service) 주소에 맞게 수정!
            // 예시들:
            //   "/api/carts/items/" + bookId
            //   "/cart-service/items/" + bookId
            // 너네 게이트웨이/라우팅 구조에 맞게만 맞춰줘~
            fetch(`/cart/items/${bookId}`, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json"
                    // JWT + 게이트웨이에서 X-User-Id/X-Guest-Id 셋팅해주면
                    // 여기서 굳이 넣어줄 필요 없음
                },
                body: JSON.stringify({
                    quantity: quantity   // UpdateCartItemRequest 의 필드명에 맞춰서!
                })
            }).then(res => {
                if (!res.ok) {
                    console.error("수량 변경 실패", res.status);
                    // 필요하면 alert("수량 변경에 실패했습니다."); 같은 처리
                }
            }).catch(err => {
                console.error("수량 변경 중 에러", err);
            });
        });
    });
    // --- 🛒 선택한 아이템 삭제 로직 시작 ---

    const clearCartForm = document.getElementById('clearCartForm'); // HTML에서 이 ID를 부여했다고 가정
    const selectedItemsContainer = document.getElementById('selectedItemsContainer'); // HTML에서 이 ID를 부여했다고 가정

    if (clearCartForm && selectedItemsContainer) {
        clearCartForm.addEventListener('submit', function(event) {
            // 폼 제출 기본 동작 방지
            event.preventDefault();

            // 1. 선택된 체크박스 모두 수집
            const checkedItems = document.querySelectorAll('.cart-item-checkbox:checked');

            if (checkedItems.length === 0) {
                alert("삭제할 아이템을 하나 이상 선택해주세요.");
                return; // 선택된 아이템이 없으면 제출 중단
            }

            // 2. 이전에 추가된 숨겨진 필드가 있다면 모두 제거 (재실행 방지)
            selectedItemsContainer.innerHTML = '';

            // 3. 체크된 아이템의 ID를 숨겨진 input 필드에 추가
            //    이 필드들은 백엔드의 List<Long> itemIds에 매핑됩니다.
            checkedItems.forEach(checkbox => {
                const input = document.createElement('input');
                input.type = 'hidden';
                input.name = 'itemIds';
                input.value = checkbox.value; // bookId
                selectedItemsContainer.appendChild(input);
            });

            // 4. 경고창 띄우고 확인 시 Form 제출
            if (confirm(`선택한 도서 ${checkedItems.length}개를 장바구니에서 삭제하시겠습니까?`)) {
                // 숨겨진 필드가 모두 추가되었으므로 폼 제출
                clearCartForm.submit();
            }
        });
    }
});


function submitOrder() {
    const checked = document.querySelectorAll('.cart-item-checkbox:checked');

    if (checked.length === 0) {
        alert('주문할 상품을 하나 이상 선택해주세요.');
        return;
    }

    const form = document.getElementById('orderForm');

    // 1) 기존에 붙어 있던 orderCheckoutRequests 관련 hidden input 제거
    Array.from(form.querySelectorAll('input[name^="orderCheckoutRequests["]'))
        .forEach(e => e.remove());

    let index = 0;

    checked.forEach(cb => {
        const row = cb.closest('.cart-item-row');
        const qtyInput = row.querySelector('.cart-qty-input');

        const bookId = cb.value;             // 체크박스 value = bookId (지금 구조)
        const quantity = qtyInput.value;     // 그 줄의 수량 input 값

        // 2) orderCheckoutRequests[index].bookId
        const bookIdHidden = document.createElement('input');
        bookIdHidden.type = 'hidden';
        bookIdHidden.name = `orderCheckoutRequests[${index}].bookId`;
        bookIdHidden.value = bookId;
        form.appendChild(bookIdHidden);

        // 3) orderCheckoutRequests[index].quantity
        const quantityHidden = document.createElement('input');
        quantityHidden.type = 'hidden';
        quantityHidden.name = `orderCheckoutRequests[${index}].quantity`;
        quantityHidden.value = quantity;
        form.appendChild(quantityHidden);

        index++;
    });

    // 👉 이 경우 bookId, quantity(단일 필드)는 전송하지 않으므로
    //    OrderPageRequest.bookId / quantity 는 null,
    //    orderCheckoutRequests 에만 값이 들어가게 됨.

    form.submit();
}