document.addEventListener('click', function(e) {
    const viewBtn = e.target.closest('.btn-view-detail');
    if (viewBtn) {
        const id = viewBtn.getAttribute('data-refund-id')
            || viewBtn.getAttribute('data-return-id');

        if (!id) {
            console.warn('no id for view button', viewBtn);
            return;
        }

        console.debug('fetching refund detail for id=', id);
        fetchDetailAndShowModal(id);
    }
});

async function fetchDetailAndShowModal(refundId) {
    try {
        const res = await fetch(`/admin/refunds/${refundId}`, {
            headers: { 'Accept': 'application/json' }
        });

        if (!res.ok) {
            console.error('detail fetch failed', res.status, await res.text());
            throw new Error('detail fetch failed: ' + res.status);
        }

        const data = await res.json();
        console.debug('detail data', data);
        fillModal(data);

        // Bootstrap 4 + jQuery 모달
        if (typeof jQuery === 'undefined' || typeof jQuery.fn.modal !== 'function') {
            alert('모달 스크립트 로딩 오류');
            return;
        }

        $('#refundDetailModal').modal('show');
    } catch (err) {
        console.error(err);
        alert('상세 정보를 불러오지 못했습니다.');
    }
}

function fillModal(data) {
    const body = document.getElementById('refund-detail-body');
    if (!body) {
        console.error('refund-detail-body not found');
        return;
    }

    body.innerHTML = `
        <dl class="row">
          <dt class="col-sm-3">반품번호</dt><dd class="col-sm-9">${escapeHtml(data.refundId)}</dd>
          <dt class="col-sm-3">주문번호</dt><dd class="col-sm-9">${escapeHtml(data.orderId)}</dd>
          <dt class="col-sm-3">회원</dt><dd class="col-sm-9">${escapeHtml(data.email)} / ${escapeHtml(data.name)}</dd>
          <dt class="col-sm-3">사유</dt><dd class="col-sm-9">${escapeHtml(data.description || '-')}</dd>
          <dt class="col-sm-3">신청일</dt><dd class="col-sm-9">${escapeHtml(data.createdAt)}</dd>
          <dt class="col-sm-3">상태</dt><dd class="col-sm-9">${escapeHtml(data.refundStatus)}</dd>
        </dl>
    `;

    const acceptBtn = document.getElementById('modal-accept-btn');
    const rejectBtn = document.getElementById('modal-reject-btn');

    if (!acceptBtn || !rejectBtn) return;

    if (data.refundStatus === 'PROCESS') {
        acceptBtn.classList.remove('d-none');
        rejectBtn.classList.remove('d-none');

        acceptBtn.onclick = () => postAction(data.refundId, 'accept');
        rejectBtn.onclick = () => postAction(data.refundId, 'reject');
    } else {
        acceptBtn.classList.add('d-none');
        rejectBtn.classList.add('d-none');
        acceptBtn.onclick = null;
        rejectBtn.onclick = null;
    }
}

async function postAction(id, action) {
    if (!confirm(action === 'accept' ? '승인하시겠습니까?' : '거절하시겠습니까?')) return;

    try {
        const res = await fetch(`/admin/refunds/${id}/${action}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' }
        });

        if (!res.ok) {
            console.error('action failed', res.status, await res.text());
            throw new Error('action failed');
        }

        if (typeof jQuery !== 'undefined' && typeof jQuery.fn.modal === 'function') {
            $('#refundDetailModal').modal('hide');
        }

        alert('처리되었습니다.');
        location.reload();
    } catch (err) {
        console.error(err);
        alert('처리 실패');
    }
}

// XSS 방지
function escapeHtml(s) {
    if (s === null || s === undefined) return '';
    return String(s)
        .replaceAll('&', '&amp;')
        .replaceAll('<', '&lt;')
        .replaceAll('>', '&gt;')
        .replaceAll('"', '&quot;')
        .replaceAll("'", '&#39;');
}
