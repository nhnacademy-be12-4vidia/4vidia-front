/* resources/static/customJs/loading.js */

let loadingTimer = null;
let startTime = 0;

/**
 * 로딩 스피너와 타이머를 시작합니다.
 */
function startLoading() {
    const loader = document.getElementById('global-loader');
    if (!loader) return;

    // 초기 시간 설정
    startTime = Date.now();

    // HTML 구조 생성 (이미지 디자인 반영)
    loader.innerHTML = `
        <div class="loader-content">
            <div class="spinner-wrapper">
                <svg class="spinner-circle" viewBox="0 0 50 50">
                    <circle class="path" cx="25" cy="25" r="20" fill="none" stroke-width="5"></circle>
                </svg>
            </div>
            <div class="loader-text-main">
                <i class="fas fa-search"></i> 도서를 검색하고 있습니다... 
                (<span id="loading-seconds">0.0</span>초)
            </div>
            <div class="loader-text-sub">잠시만 기다려주세요</div>
        </div>
    `;

    loader.classList.add('active');

    // 0.1초마다 타이머 업데이트
    loadingTimer = setInterval(() => {
        const elapsedTime = (Date.now() - startTime) / 1000;
        const secondsSpan = document.getElementById('loading-seconds');
        if (secondsSpan) {
            secondsSpan.innerText = elapsedTime.toFixed(1);
        }
    }, 100);
}

/**
 * 로딩 스피너를 숨기고 타이머를 종료합니다.
 */
function stopLoading() {
    const loader = document.getElementById('global-loader');
    if (loader) {
        loader.classList.remove('active');
    }
    // 타이머 초기화
    if (loadingTimer) {
        clearInterval(loadingTimer);
        loadingTimer = null;
    }
}

// 뒤로가기 시 방지 로직
window.addEventListener('pageshow', function(event) {
    if (event.persisted || (window.performance && window.performance.navigation.type === 2)) {
        stopLoading();
    }
});