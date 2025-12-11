// static/customJs/admin-refund.js
// jQuery + Bootstrap4 환경에서 동작하도록 수정한 버전입니다.

document.addEventListener('click', function(e) {
    const viewBtn = e.target.closest('.btn-view-detail');
    if (viewBtn) {
        const id = viewBtn.getAttribute('data-refund-id') || viewBtn.getAttribute('data-return-id');
        if (!id) {
            console.warn('no id for view button', viewBtn);
            return;
        }
        console.debug('fetching refund detail for id=', id);
        fetchDetailAndShowModal(id);
    }
});

async function fetchDetailAndShowModal(id) {
    try {
        const res = await fetch(`/admin/refunds/${id}`, { headers: { 'Accept': 'application/json' }});
        if (!res.ok) {
            console.error('detail fetch failed', res.status, await res.text());
            throw new Error('detail fetch failed: ' + res.status);
        }
        const data = await res.json();
        console.debug('detail data', data);
        fillModal(data);

        // Bootstrap4 (jQuery) 방식으로 모달 띄우기
        if (typeof jQuery === 'undefined') {
            console.error('jQuery is not loaded. admin-refund.js requires jQuery when using Bootstrap 4.');
            alert('페이지 스크립트 로딩 오류: jQuery가 없습니다.');
            return;
        }

        // jQuery modal 플러그인이 존재하는지 확인
        if (typeof jQuery.fn.modal !== 'function') {
            console.error('Bootstrap modal plugin not available (jQuery.fn.modal is not a function).');
            alert('페이지 스크립트 로딩 오류: Bootstrap JS가 제대로 로드되지 않았습니다.');
            return;
        }

        // show modal via jQuery
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

    if (acceptBtn && rejectBtn) {
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
    } else {
        console.warn('modal action buttons not found', { acceptBtn, rejectBtn });
    }
}

async function postAction(id, action) {
    if (!confirm(action === 'accept' ? '승인하시겠습니까?' : '거절하시겠습니까?')) return;
    try {
        const csrf = getCsrfToken();
        const headers = { 'Content-Type': 'application/json' };
        if (csrf) headers['X-CSRF-TOKEN'] = csrf;

        const res = await fetch(`/admin/refunds/${id}/${action}`, { method: 'POST', headers });
        if (!res.ok) {
            console.error('action failed', res.status, await res.text());
            throw new Error('action failed');
        }

        // Bootstrap4 방식으로 모달 닫기 (jQuery)
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

function getCsrfToken() {
    const meta = document.querySelector('meta[name="_csrf"]');
    return meta ? meta.getAttribute('content') : '';
}

// simple escaping
function escapeHtml(s) {
    if (s === null || s === undefined) return '';
    return String(s)
        .replaceAll('&', '&amp;')
        .replaceAll('<', '&lt;')
        .replaceAll('>', '&gt;')
        .replaceAll('"', '&quot;')
        .replaceAll("'", '&#39;');
}
