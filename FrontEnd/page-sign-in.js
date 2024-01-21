document.addEventListener('DOMContentLoaded', function () {
    const signInForm = document.getElementById('form-sign-in');
    const signInEmail = document.getElementById('sign-email');
    const signInPassword = document.getElementById('sign-password');

    signInForm.addEventListener('submit', function (event) {
        event.preventDefault();

        const inputsAreValid = checkInputs(signInEmail, signInPassword);

        if (inputsAreValid) {
            var email = signInEmail.value.trim();
            var password = signInPassword.value.trim();

            var data = {
                email: email,
                password: password
            };

            fetch('http://localhost:8080/api/user/signin', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(data)
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Authentication failed');
                    }
                    return response.json();
                })
                .then(data => {
                    console.log('User authenticated with ID:', data);

                    localStorage.setItem('userId', data);

                    signInLinkContainer.style.display = 'none';
                    signOutLinkContainer.style.display = 'inline';
                    window.location.href = "page-home.html";
                })
                .catch(error => {

                    setErrorFor(signInPassword, 'Wrong password!');
                    console.error('Authentication failed:', error.message);

                });
        }
    });
});

function checkInputs(signInEmail, signInPassword) {
    const emailValue = signInEmail.value.trim();
    const passwordValue = signInPassword.value.trim();

    if (emailValue === '') {
        setErrorFor(signInEmail, 'Email cannot be empty!');
        return false;
    } else if (!isValidEmail(emailValue)) {
        setErrorFor(signInEmail, 'Email is not valid!');
        return false;
    } else {
        setSuccess(signInEmail);
    }

    if (passwordValue === '') {
        setErrorFor(signInPassword, 'Password cannot be empty!');
        return false;
    } else {
        setSuccess(signInPassword);
    }
    return true;
}

function setErrorFor(input, message) {
    const formControl = input.parentElement.parentElement; // .form-control bg-dark text-light
    const small = formControl.querySelector('small');
    small.innerText = message;
    formControl.className = 'form-control bg-dark text-light error';
}

function setSuccess(input) {
    const formControl = input.parentElement.parentElement; // .form-control bg-dark text-light
    formControl.className = 'form-control bg-dark text-light';
}

function isValidEmail(email) {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
}