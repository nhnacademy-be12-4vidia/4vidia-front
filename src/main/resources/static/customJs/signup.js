// ===== 상태 =====
let emailValid = false;     // 중복확인 통과
let emailVerified = false;  // 이메일 인증 완료

let timerInterval = null;
let remainingSeconds = 0;

// ===== 유틸: CSRF + AJAX 표시 =====
function csrfHeaders() {
    const token = document.querySelector('meta[name="_csrf"]')?.getAttribute('content');
    const header = document.querySelector('meta[name="_csrf_header"]')?.getAttribute('content');

    const h = {
        "Content-Type": "application/json",
        "Accept": "application/json",
        "X-Requested-With": "XMLHttpRequest"
    };
    if (token && header) h[header] = token;
    return h;
}

function ajaxHeaders() {
    return {
        "Accept": "application/json",
        "X-Requested-With": "XMLHttpRequest"
    };
}

// ===== 공통: redirect/HTML 방어 + ApiResponse 파싱 =====
async function safeReadJsonOrThrow(res, defaultMsg) {
    // ✅ 204는 성공으로 보는 서버도 많음
    if (res.status === 204) {
        return { header: { isSuccessful: true }, data: null };
    }

    if (res.redirected || res.status === 302) {
        throw Object.assign(new Error("로그인이 필요하거나 서버가 리다이렉트했습니다."), {
            errorCode: "REDIRECTED",
            status: res.status,
            redirectedUrl: res.url
        });
    }

    const ct = (res.headers.get("content-type") || "").toLowerCase();

    if (!ct.includes("application/json")) {
        const text = await res.text().catch(() => "");
        throw Object.assign(new Error("서버 응답이 올바르지 않습니다."), {
            errorCode: "NON_JSON",
            status: res.status,
            raw: text
        });
    }

    const data = await res.json().catch(() => null);

    if (!data || !data.header) {
        throw Object.assign(new Error(defaultMsg || "요청에 실패했습니다."), {
            errorCode: "INVALID_RESPONSE",
            status: res.status,
            response: data
        });
    }

    if (data.header.isSuccessful !== true) {
        throw Object.assign(new Error(data.header.resultMessage || defaultMsg), {
            errorCode: data.header.errorCode,
            status: data.header.resultCode ?? res.status,
            response: data
        });
    }

    return data;
}

// ✅ check-email은 ApiResponse(data=true/false) or boolean(true/false) 둘 다 올 수 있음
async function readBooleanOrApiResponse(res, defaultMsg) {
    if (res.redirected || res.status === 302) {
        throw Object.assign(new Error("로그인이 필요하거나 서버가 리다이렉트했습니다."), {
            errorCode: "REDIRECTED",
            status: res.status,
            redirectedUrl: res.url
        });
    }

    const ct = (res.headers.get("content-type") || "").toLowerCase();

    if (!ct.includes("application/json")) {
        const text = await res.text().catch(() => "");
        throw Object.assign(new Error("서버 응답이 올바르지 않습니다."), {
            errorCode: "NON_JSON",
            status: res.status,
            raw: text
        });
    }

    const data = await res.json().catch(() => null);

    if (data && data.header) {
        if (data.header.isSuccessful !== true) {
            throw Object.assign(new Error(data.header.resultMessage || defaultMsg), {
                errorCode: data.header.errorCode,
                status: data.header.resultCode ?? res.status,
                response: data
            });
        }
        return data.data === true;
    }

    if (typeof data === "boolean") return data;

    throw Object.assign(new Error(defaultMsg || "요청에 실패했습니다."), {
        errorCode: "INVALID_RESPONSE",
        status: res.status,
        response: data
    });
}

// ✅ errorCode 기준 메시지 매핑
function mapEmailAuthError(errorCode, fallbackMsg) {
    switch (errorCode) {
        case "U501": return "인증시간이 만료되었습니다. 재발송을 눌러주세요.";
        case "U503": return "인증코드가 일치하지 않습니다. 다시 확인해주세요.";
        case "U601": return "메일 발송에 실패했습니다. 잠시 후 다시 시도해주세요.";
        case "AUTH_REQUIRED": return "로그인이 필요합니다. 새로고침 후 다시 시도해주세요.";
        case "REDIRECTED": return "로그인이 필요하거나 서버가 리다이렉트했습니다. 새로고침 후 다시 시도해주세요.";
        case "NON_JSON": return "서버 응답이 올바르지 않습니다. 새로고침 후 다시 시도해주세요.";
        default: return fallbackMsg || "요청에 실패했습니다.";
    }
}

// ===== 타이머 =====
function startTimer(seconds) {
    stopTimer();
    remainingSeconds = seconds;

    const badge = document.getElementById('timerBadge');
    if (badge) badge.style.display = "inline-block";

    updateTimerUI();

    timerInterval = setInterval(() => {
        remainingSeconds--;
        updateTimerUI();

        if (remainingSeconds <= 0) {
            stopTimer();

            emailVerified = false;
            const codeFeedback = document.getElementById('codeFeedback');

            codeFeedback.textContent = "인증시간이 만료되었습니다. 재발송을 눌러주세요.";
            codeFeedback.style.color = "red";

            document.getElementById('sendOrResendBtn').disabled = false;
            document.getElementById('verifyCodeBtn').disabled = true;

            updateSignupButtonState();
        }
    }, 1000);
}

function updateTimerUI() {
    const mm = String(Math.floor(remainingSeconds / 60)).padStart(2, "0");
    const ss = String(remainingSeconds % 60).padStart(2, "0");

    const badge = document.getElementById('timerBadge');
    if (!badge) return;

    badge.textContent = `${mm}:${ss}`;
    if (remainingSeconds <= 30) badge.classList.add('danger');
    else badge.classList.remove('danger');
}

function stopTimer() {
    if (timerInterval) clearInterval(timerInterval);
    timerInterval = null;
    remainingSeconds = 0;

    const badge = document.getElementById('timerBadge');
    if (badge) badge.style.display = "none";
}

// ===== 전화번호 =====
function formatPhoneNumber(value) {
    if (!value) return "";
    value = value.replace(/[^0-9]/g, '');

    if (value.length > 3 && value.length <= 7) {
        return value.substring(0, 3) + '-' + value.substring(3);
    } else if (value.length > 7) {
        return value.substring(0, 3) + '-' + value.substring(3, 7) + '-' + value.substring(7, 11);
    }
    return value;
}

function handlePhoneInput(input) {
    input.value = formatPhoneNumber(input.value);
    updateSignupButtonState();
}

// ===== 버튼 상태 업데이트 =====
function updateSignupButtonState() {
    const name = document.getElementById('name').value.trim();
    const phoneValue = document.getElementById('phone').value.trim();
    const email = document.getElementById('email').value.trim();
    const password = document.getElementById('password');

    const rawPhone = phoneValue.replace(/[^0-9]/g, '');
    const isPhoneValid = rawPhone.length >= 10 && rawPhone.length <= 11;
    const isPasswordValid = password.checkValidity();

    const signupButton = document.getElementById('signupButton');
    const canSubmit = name && isPhoneValid && email && isPasswordValid && emailValid && emailVerified;

    signupButton.disabled = !canSubmit;
}

// ===== 이메일 중복확인 =====
async function checkEmail() {
    const email = document.getElementById('email').value.trim();
    const feedback = document.getElementById('emailFeedback');
    const sendBtn = document.getElementById('sendOrResendBtn');

    const emailPattern = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]*[A-Za-z][A-Za-z0-9.-]*\.[A-Za-z]{2,}$/;
    if (!emailPattern.test(email)) {
        feedback.textContent = "올바른 이메일 형식이 아닙니다.";
        feedback.style.color = "red";
        emailValid = false;
        emailVerified = false;
        sendBtn.disabled = true;
        document.getElementById('codeBlock').style.display = "none";
        updateSignupButtonState();
        return;
    }

    feedback.textContent = "중복확인 중...";
    feedback.style.color = "#495057";

    try {
        const res = await fetch(`/auth/check-email?email=${encodeURIComponent(email)}`, {
            method: "GET",
            credentials: "same-origin",
            headers: ajaxHeaders()
        });

        const exists = await readBooleanOrApiResponse(res, "이메일 중복확인에 실패했습니다.");

        if (exists) {
            feedback.textContent = "이미 사용 중인 이메일입니다.";
            feedback.style.color = "red";
            emailValid = false;
            emailVerified = false;
            sendBtn.disabled = true;
            document.getElementById('codeBlock').style.display = "none";
        } else {
            feedback.textContent = "사용 가능한 이메일입니다! 인증을 진행해주세요.";
            feedback.style.color = "green";
            emailValid = true;
            emailVerified = false;

            sendBtn.disabled = false;
            sendBtn.textContent = "인증코드 발송";
            document.getElementById('codeBlock').style.display = "none";

            // 인증 관련 메시지 초기화
            document.getElementById('codeFeedback').textContent = "";
        }

        updateSignupButtonState();

    } catch (e) {
        const msg = mapEmailAuthError(e.errorCode, e.message);
        feedback.textContent = msg;
        feedback.style.color = "red";

        emailValid = false;
        emailVerified = false;
        sendBtn.disabled = true;

        updateSignupButtonState();
    }
}

// ===== 이메일 코드 발송 =====
async function sendEmailCode() {
    const email = document.getElementById('email').value.trim();
    const codeFeedback = document.getElementById('codeFeedback');
    const btn = document.getElementById('sendOrResendBtn');

    if (!emailValid) {
        codeFeedback.textContent = "먼저 이메일 중복확인을 완료해주세요.";
        codeFeedback.style.color = "red";
        return;
    }

    btn.disabled = true;
    codeFeedback.textContent = "인증코드를 발송 중입니다...";
    codeFeedback.style.color = "#495057";

    try {
        const res = await fetch(`/auth/email/send-code`, {
            method: "POST",
            credentials: "same-origin",
            headers: csrfHeaders(),
            body: JSON.stringify({ email })
        });

        await safeReadJsonOrThrow(res, "인증코드 발송에 실패했습니다.");

        document.getElementById('codeBlock').style.display = "flex";
        codeFeedback.textContent = "인증코드를 발송했어요. 메일함을 확인해주세요.";
        codeFeedback.style.color = "green";

        btn.textContent = "재발송";
        btn.disabled = false;

        document.getElementById('verifyCodeBtn').disabled = false;

        emailVerified = false;
        startTimer(180);
        updateSignupButtonState();

    } catch (e) {
        const msg = mapEmailAuthError(e.errorCode, e.message);
        codeFeedback.textContent = msg;
        codeFeedback.style.color = "red";

        btn.textContent = "재발송";
        btn.disabled = false;

        updateSignupButtonState();
    }
}

// ===== 인증코드 확인 =====
async function verifyEmailCode() {
    const email = document.getElementById('email').value.trim();
    const code = document.getElementById('emailCode').value.trim();
    const codeFeedback = document.getElementById('codeFeedback');

    if (code.length !== 6) {
        codeFeedback.textContent = "인증코드 6자리를 입력해주세요.";
        codeFeedback.style.color = "red";
        return;
    }

    codeFeedback.textContent = "인증코드를 확인 중입니다...";
    codeFeedback.style.color = "#495057";

    try {
        const res = await fetch(`/auth/email/verify-code`, {
            method: "POST",
            credentials: "same-origin",
            headers: csrfHeaders(),
            body: JSON.stringify({ email, code })
        });

        await safeReadJsonOrThrow(res, "인증코드가 올바르지 않거나 만료되었습니다.");

        emailVerified = true;
        stopTimer();

        codeFeedback.textContent = "이메일 인증이 완료되었습니다!";
        codeFeedback.style.color = "green";

        const sendBtn = document.getElementById('sendOrResendBtn');
        sendBtn.textContent = "인증 완료";
        sendBtn.disabled = true;

        document.getElementById('verifyCodeBtn').disabled = true;
        document.getElementById('emailDupBtn').disabled = true;
        document.getElementById('email').readOnly = true;

        updateSignupButtonState();

    } catch (e) {
        emailVerified = false;

        const msg = mapEmailAuthError(e.errorCode, e.message);
        codeFeedback.textContent = msg;
        codeFeedback.style.color = "red";

        if (e.errorCode === "U501") {
            stopTimer();
            document.getElementById('verifyCodeBtn').disabled = true;
        } else {
            document.getElementById('verifyCodeBtn').disabled = false;
        }

        const sendBtn = document.getElementById('sendOrResendBtn');
        sendBtn.textContent = "재발송";
        sendBtn.disabled = false;

        updateSignupButtonState();
    }
}

// ===== 비밀번호 체크 =====
function validatePassword() {
    const passwordInput = document.getElementById("password");
    const checkIcon = document.getElementById("passwordCheckIcon");

    if (passwordInput.value && passwordInput.checkValidity()) checkIcon.style.display = "block";
    else checkIcon.style.display = "none";
}

// ===== 이메일 입력 변경 시 상태 초기화 =====
function resetEmailValidation() {
    emailValid = false;
    emailVerified = false;

    stopTimer();

    const emailFeedback = document.getElementById('emailFeedback');
    emailFeedback.textContent = "이메일 중복확인을 해주세요.";
    emailFeedback.style.color = "orange";

    document.getElementById('codeFeedback').textContent = "";
    document.getElementById('codeBlock').style.display = "none";
    document.getElementById('sendOrResendBtn').disabled = true;

    document.getElementById('emailDupBtn').disabled = false;
    document.getElementById('verifyCodeBtn').disabled = false;
    document.getElementById('email').readOnly = false;

    updateSignupButtonState();
}

// ===== 최종 폼 제출 방어 =====
function validateForm(event) {
    if (!emailValid) { alert("이메일 중복 확인을 완료해주세요."); return false; }
    if (!emailVerified) { alert("이메일 인증을 완료해주세요."); return false; }

    const phoneInput = document.getElementById('phone');
    phoneInput.value = phoneInput.value.replace(/-/g, '');
    return true;
}

document.addEventListener('DOMContentLoaded', () => {
    // ✅ 생년월일: 미래 날짜 선택 방지 (로컬 시간 기준)
    const d = new Date();
    const today = `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`;
    document.getElementById("birthDate")?.setAttribute("max", today);

    // 초기 문구 (원하면 제거 가능)
    const emailFeedback = document.getElementById('emailFeedback');
    if (emailFeedback && !emailFeedback.textContent.trim()) {
        emailFeedback.textContent = "이메일 중복확인을 해주세요.";
        emailFeedback.style.color = "orange";
    }

    updateSignupButtonState();
});
