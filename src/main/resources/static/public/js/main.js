document.addEventListener("DOMContentLoaded", () => {
    
    // 1. HERO TEXT ANIMATION (Chữ chạy từng ký tự)
    // Tách các chữ cái trong h1 để animate
    const heroTitle = document.querySelector('.hero-title');
    if (heroTitle) {
        heroTitle.innerHTML = heroTitle.textContent.replace(/\S/g, "<span class='letter'>$&</span>");
        
        anime.timeline({loop: false})
            .add({
                targets: '.hero-title .letter',
                translateY: [40, 0],
                translateZ: 0,
                opacity: [0, 1],
                easing: "easeOutExpo",
                duration: 1200,
                delay: (el, i) => 30 * i
            }).add({
                targets: '.hero-desc',
                translateY: [20, 0],
                opacity: [0, 1],
                easing: "easeOutExpo",
                duration: 800,
                offset: '-=800'
            }).add({
                targets: '.hero-btns .btn',
                translateY: [20, 0],
                opacity: [0, 1],
                easing: "easeOutExpo",
                duration: 800,
                delay: (el, i) => 100 * i,
                offset: '-=800'
            });
    }

    // 2. SCROLL REVEAL (Hiện dần khi cuộn chuột)
    const observerOptions = {
        root: null,
        rootMargin: '0px',
        threshold: 0.1
    };

    const observer = new IntersectionObserver((entries, observer) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                // Animation cho các thẻ Card
                anime({
                    targets: entry.target,
                    translateY: [50, 0],
                    opacity: [0, 1],
                    duration: 800,
                    easing: 'easeOutCubic',
                    delay: entry.target.dataset.delay || 0 // Lấy delay từ html
                });
                observer.unobserve(entry.target); // Chỉ chạy 1 lần
            }
        });
    }, observerOptions);

    // Gắn observer vào các phần tử cần animate
    document.querySelectorAll('.animate-on-scroll').forEach((el) => {
        el.style.opacity = 0; // Ẩn trước
        observer.observe(el);
    });

    // 3. BACKGROUND PARTICLES (Hạt bay bay ở nền Hero)
    const heroSection = document.querySelector('.hero-section');
    if(heroSection) {
        for (let i = 0; i < 15; i++) {
            let dot = document.createElement('div');
            dot.classList.add('particle');
            heroSection.appendChild(dot);
            
            // Random vị trí và kích thước
            let size = Math.random() * 20 + 5;
            dot.style.width = size + 'px';
            dot.style.height = size + 'px';
            dot.style.left = Math.random() * 100 + '%';
            dot.style.top = Math.random() * 100 + '%';
            
            // Animation bay ngẫu nhiên
            anime({
                targets: dot,
                translateX: () => anime.random(-100, 100),
                translateY: () => anime.random(-100, 100),
                scale: [0, 1],
                opacity: [0, 0.3, 0],
                duration: () => anime.random(3000, 8000),
                delay: () => anime.random(0, 5000),
                loop: true,
                easing: 'easeInOutSine'
            });
        }
    }
});