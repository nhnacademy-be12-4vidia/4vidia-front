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

    // ✅ 수량 변경 시: 라인 금액 + 총합 실시간 업데이트
    rows.forEach(row => {
        const qtyInput = row.querySelector(".cart-qty-input");
        if (!qtyInput) return;

        function onQtyChange() {
            updateRowLineTotalDisplay(row);
            recalcSelectedTotal();
        }

        qtyInput.addEventListener("input", onQtyChange);
        qtyInput.addEventListener("change", onQtyChange);
    });
});
