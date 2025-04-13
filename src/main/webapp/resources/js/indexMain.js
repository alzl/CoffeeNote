let slideIndex = 0; // 현재 슬라이드의 인덱스 초기화
let slides = []; // 슬라이드 요소를 담을 배열
let indicators = []; // 인디케이터 요소를 담을 배열
let slideInterval; // 슬라이드 자동 전환을 위한 변수

// 슬라이드 쇼를 시작하는 함수
function startSlideShow() {
    initializeSlides(); // 슬라이드와 인디케이터 요소를 초기화
    if (slides.length > 0) {
        showSlides(); // 슬라이드를 보이게 함
        resetSlideInterval(); // 슬라이드 자동 전환 시작
    }
}

// 슬라이드를 이동시키는 함수
function moveSlide(n) {
    if (slides.length === 0) return; // 슬라이드가 없으면 함수 종료
    slideIndex = (slideIndex + n + slides.length) % slides.length; // 슬라이드 인덱스를 계산
    showSlides(); // 슬라이드 업데이트
    resetSlideInterval(); // 슬라이드 자동 전환 리셋
}

// 현재 슬라이드로 이동시키는 함수
function currentSlide(n) {
    if (slides.length === 0) return; // 슬라이드가 없으면 함수 종료
    slideIndex = n - 1; // 인덱스를 0부터 시작하도록 조정
    showSlides(); // 슬라이드 업데이트
    resetSlideInterval(); // 슬라이드 자동 전환 리셋
}

// 슬라이드를 표시하는 함수
function showSlides() {
    if (slides.length === 0) return; // 슬라이드가 없으면 함수 종료

    // 모든 슬라이드를 비활성화
    for (let i = 0; i < slides.length; i++) {
        slides[i].classList.remove('main_active');
    }

    // 현재 슬라이드만 활성화
    slides[slideIndex].classList.add('main_active');

    // 모든 인디케이터의 active 클래스를 제거
    for (let i = 0; i < indicators.length; i++) {
        indicators[i].classList.remove('main_active');
    }

    // 현재 슬라이드에 해당하는 인디케이터에 active 클래스 추가
    if (indicators[slideIndex]) {
        indicators[slideIndex].classList.add('main_active');
    }
}

// 슬라이드와 인디케이터 요소를 초기화하는 함수
function initializeSlides() {
    slides = document.getElementsByClassName("main_slide"); // 슬라이드 요소를 가져옴
    indicators = document.getElementsByClassName("main_indicator"); // 인디케이터 요소를 가져옴
}

// 슬라이드 자동 전환을 시작하는 함수
function resetSlideInterval() {
    clearInterval(slideInterval); // 기존의 슬라이드 전환 인터벌 클리어
    if (slides.length > 1) { // 슬라이드가 2개 이상인 경우에만 자동 전환
        slideInterval = setInterval(function() {
            moveSlide(1); // 7초마다 슬라이드를 다음으로 이동
        }, 7000); // 7000ms (7초)
    }
}

// 페이지가 완전히 로드된 후 슬라이드 쇼를 시작
window.addEventListener('load', function() {
    startSlideShow(); // 슬라이드 쇼 시작
});

// 인디케이터 클릭 시 슬라이드 이동
document.addEventListener('click', function(event) {
    if (event.target.classList.contains('main_indicator')) {
        const index = Array.from(indicators).indexOf(event.target);
        currentSlide(index + 1); // 클릭한 인디케이터의 인덱스로 슬라이드 이동
    }
});