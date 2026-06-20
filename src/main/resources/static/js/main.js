// ============================================
// Lait's Go - JavaScript principal
// ============================================

document.addEventListener('DOMContentLoaded', function() {

    // --- Sidebar Toggle (mobile) ---
    const sidebarToggle = document.getElementById('sidebarToggle');
    const sidebar = document.querySelector('.sidebar');

    if (sidebarToggle && sidebar) {
        sidebarToggle.addEventListener('click', function() {
            sidebar.classList.toggle('open');
        });

        // Fermer la sidebar quand on clique en dehors (mobile)
        document.addEventListener('click', function(event) {
            const isClickInside = sidebar.contains(event.target) || sidebarToggle.contains(event.target);
            if (!isClickInside && window.innerWidth <= 768) {
                sidebar.classList.remove('open');
            }
        });
    }

    // --- Gestion du resize (fermer la sidebar sur desktop) ---
    window.addEventListener('resize', function() {
        if (window.innerWidth > 768) {
            sidebar.classList.remove('open');
        }
    });

});