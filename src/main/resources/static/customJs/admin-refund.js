document.addEventListener('DOMContentLoaded', function () {
    const modal = document.getElementById('returnDetailModal');
    const modalCloseBtn = document.getElementById('modalCloseBtn');
    let currentId = null;

    const csrfMeta = document.getElementById('csrfMeta');
    const csrfHeaderMeta = document.getElementById('csrfHeaderMeta');
    const csrfToken = csrfMeta ? csrfMeta.getAttribute('content') : null;
    const csrfHeader = csrfHeaderMeta ? csrfHeaderMeta.getAttribute('content') : 'X-CSRF-TOKEN';

    function openModal() {
        if (!modal) return;
        modal.style.display = 'block';
        modal.classList.add('show');
        modal.setAttribute('aria-modal', 'true');
        modal.removeAttribute('aria-hidden');
    }
    function closeModal() {
        if (!modal) return;
        modal.style.display = 'none';
        modal.classList.remove('show');
        modal.setAttribute('aria-hidden', 'true');
        modal.removeAttribute('aria-modal');
    }

    function fillModal(data) {
        currentId = data.returnId;
        const setText = (id, text) => {
            const el = document.getElementById(id);
            if (el) el.textContent = text ?? '';
        };
        setText('m-returnId', data.returnId);
        setText('m-orderId', data.orderId);
        setText('m-requesterEmail', data.requesterEmail || '비회원');

        const itemsList = document.getElementById('m-itemsList');
        if (itemsList) {
            itemsList.innerHTML = '';
            if (!data.items || data.items.length === 0) {
                const li = document.createElement('li');
                li.textContent = '전체반품';
                itemsList.appendChild(li);
            } else {
                data.items.forEach(it => {
                    const li = document.createElement('li');
                    li.textContent = it.title + ' (수량:' + it.quantity + ')';
                    itemsList.appendChild(li);
                });
            }
        }
        setText('m-reason', data.reason || '');
        setText('m-returnType', data.returnType || '');
        setText('m-requestedAt', data.requestedAt || '');
    }

    // 상세보기 버튼 바인딩
    document.querySelectorAll('.btn-view-detail').forEach(btn => {
        btn.addEventListener('click', function () {
            const id = this.getAttribute('data-return-id');
            if (!id) return;
            fetch(`/admin/returns/${id}`, { method: 'GET', headers: { 'Accept': 'application/json' } })
                .then(res => {
                    if (!res.ok) throw new Error('상세 불러오기 실패: ' + res.status);
                    return res.json();
                })
                .then(json => {
                    fillModal(json);
                    openModal();
                })
                .catch(err => {
                    console.error(err);
                    alert('상세 정보를 불러오는 데 실패했습니다.');
                });
        });
    });

    // 빠른 승인/거절 (테이블 버튼)
    function doApprove(id, fromModal) {
        fetch(`/admin/returns/${id}/approve`, {
            method: 'POST',
            headers: Object.assign({'Content-Type': 'application/json'}, (csrfToken ? {[csrfHeader]: csrfToken} : {})),
            body: JSON.stringify({})
        }).then(res => {
            if (!res.ok) return res.text().then(t => Promise.reject({status: res.status, text: t}));
            return res.json().catch(()=>({}));
        }).then(() => {
            const row = document.querySelector(`tr[data-id="${id}"]`);
            if (row) {
                const badge = row.querySelector('td span');
                if (badge) {
                    badge.textContent = 'APPROVED';
                    badge.className = 'status-badge status-APPROVED';
                }
                row.querySelectorAll('.btn-approve, .btn-reject').forEach(el => el.remove());
            }
            if (fromModal) closeModal();
            alert('반품이 승인되었습니다.');
        }).catch(err => {
            console.error(err);
            alert('승인 처리 중 오류가 발생했습니다.');
        });
    }

    function doReject(id, reason, fromModal) {
        fetch(`/admin/returns/${id}/reject`, {
            method: 'POST',
            headers: Object.assign({'Content-Type': 'application/json'}, (csrfToken ? {[csrfHeader]: csrfToken} : {})),
            body: JSON.stringify({ reason: reason || null })
        }).then(res => {
            if (!res.ok) return res.text().then(t => Promise.reject({status: res.status, text: t}));
            return res.json().catch(()=>({}));
        }).then(() => {
            const row = document.querySelector(`tr[data-id="${id}"]`);
            if (row) {
                const badge = row.querySelector('td span');
                if (badge) {
                    badge.textContent = 'REJECTED';
                    badge.className = 'status-badge status-REJECTED';
                }
                row.querySelectorAll('.btn-approve, .btn-reject').forEach(el => el.remove());
            }
            if (fromModal) closeModal();
            alert('반품이 거절되었습니다.');
        }).catch(err => {
            console.error(err);
            alert('거절 처리 중 오류가 발생했습니다.');
        });
    }

    document.querySelectorAll('.btn-approve').forEach(b => {
        b.addEventListener('click', function () {
            const id = this.getAttribute('data-return-id');
            if (!id) return;
            if (!confirm('선택한 반품을 승인하시겠습니까?')) return;
            doApprove(id, false);
        });
    });

    document.querySelectorAll('.btn-reject').forEach(b => {
        b.addEventListener('click', function () {
            const id = this.getAttribute('data-return-id');
            if (!id) return;
            const reason = prompt('거절 사유를 입력하세요 (선택):');
            doReject(id, reason, false);
        });
    });

    // 모달 버튼
    const modalApproveBtn = document.getElementById('modalApproveBtn');
    const modalRejectBtn = document.getElementById('modalRejectBtn');
    modalApproveBtn?.addEventListener('click', function () {
        if (!currentId) return;
        if (!confirm('이 반품을 승인하시겠습니까?')) return;
        doApprove(currentId, true);
    });
    modalRejectBtn?.addEventListener('click', function () {
        if (!currentId) return;
        const reason = prompt('거절 사유를 입력하세요 (선택):');
        doReject(currentId, reason, true);
    });

    modalCloseBtn?.addEventListener('click', closeModal);
    window.addEventListener('click', function (e) { if (e.target === modal) closeModal(); });

});
