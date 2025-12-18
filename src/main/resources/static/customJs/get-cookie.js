// function getCookie(name) {
//     return document.cookie
//         .split('; ')
//         .find(row => row.startsWith(name + '='))
//         ?.split('=')[1];
// }



// get-cookie.js

/**
 * CSRF 토큰 값을 가져오는 통합 함수
 * 1순위: HTML 헤더의 Meta 태그 (가장 정확함)
 * 2순위: 쿠키 (백업)
 */
function getCsrfToken() {
    // 1. Meta 태그에서 찾기
    const metaToken = document.querySelector('meta[name="_csrf"]');
    if (metaToken) {
        return metaToken.getAttribute('content');
    }

    // 2. 쿠키에서 찾기 (Meta 태그가 없을 경우 대비)
    return document.cookie
        .split('; ')
        .find(row => row.startsWith('XSRF-TOKEN='))
        ?.split('=')[1];
}

/**
 * CSRF 헤더 이름을 가져오는 함수 (기본값: X-XSRF-TOKEN)
 */
function getCsrfHeaderName() {
    const metaHeader = document.querySelector('meta[name="_csrf_header"]');
    return metaHeader ? metaHeader.getAttribute('content') : 'X-XSRF-TOKEN';
}