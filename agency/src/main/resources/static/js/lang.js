(() => {
    const select = document.getElementById('langSelect');
    if (!select) return;

    select.addEventListener('change', () => {
        const lang = select.value;

        const invalid = document.querySelector('.is-invalid');
        const form = invalid && invalid.closest('form');

        if (form) {
            let input = form.querySelector('input[name="lang"]');
            if (!input) {
                input = document.createElement('input');
                input.type = 'hidden';
                input.name = 'lang';
                form.appendChild(input);
            }
            input.value = lang;
            form.submit();
            return;
        }

        const params = new URLSearchParams(location.search);
        params.set('lang', lang);
        location.search = params.toString();
    });
})();
