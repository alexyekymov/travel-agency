(() => {
    const form = document.getElementById('loginForm');
    const error = document.getElementById('error');
    const loginUrl = form.dataset.loginUrl;
    const homeUrl = form.dataset.homeUrl;

    form.addEventListener('submit', async (event) => {
        event.preventDefault();
        error.classList.add('d-none');

        const response = await fetch(loginUrl, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({
                username: form.username.value,
                password: form.password.value
            })
        });

        if (response.ok) {
            window.location.assign(homeUrl);
        } else {
            error.classList.remove('d-none');
        }
    });
})();
