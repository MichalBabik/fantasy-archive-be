document.addEventListener('DOMContentLoaded', function () {
    const signInForm = document.getElementById('form-sign-in');
    const signInEmail = document.getElementById('sign-email');
    const signInPassword = document.getElementById('sign-password');

    signInForm.addEventListener('submit', async function (event) {
        event.preventDefault();

        checkInputs();

        if (inputsAreValid) {
            const email = signInEmail.value.trim();
            const password = signInPassword.value.trim();

            const data = {
                email: email,
                password: password
            };

            try {
                const response = await fetch('http://localhost:8080/api/user/signin', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(data)
                });

                if (!response.ok) {
                    throw new Error('Authentication failed');
                }

                const userData = await response.json();
                console.log('User authenticated with ID:', userData);

                localStorage.setItem('userId', userData);

                signInLinkContainer.style.display = 'none';
                signOutLinkContainer.style.display = 'inline';
                window.location.href = "page-home.html";
            } catch (error) {
                // Handle authentication failure
                console.error('Authentication failed:', error.message);
                setErrorFor(signInEmail, 'Invalid email or password');
                setErrorFor(signInPassword, 'Invalid email or password');
            }
        }
    });

    function checkInputs() {
        const emailValue = signInEmail.value.trim();
        const passwordValue = signInPassword.value.trim();

        if (emailValue === '') {
            setErrorFor(signInEmail, 'Email cannot be empty!');
        } else if (!isValidEmail(emailValue)) {
            setErrorFor(signInEmail, 'Email is not valid!');
        } else {
            setSuccess(signInEmail);
        }

        if (passwordValue === '') {
            setErrorFor(signInPassword, 'Password cannot be empty!');
        } else {
            setSuccess(signInPassword);
        }
    }

    function setErrorFor(input, message) {
        const formControl = input.parentElement.parentElement; // .form-control bg-dark text-light
        const small = formControl.querySelector('small');
        small.innerText = message;
        formControl.className = 'form-control bg-dark text-light error';
        inputsAreValid = false;
    }

    function setSuccess(input) {
        const formControl = input.parentElement.parentElement; // .form-control bg-dark text-light
        formControl.className = 'form-control bg-dark text-light';
    }

    function isValidEmail(email) {
        return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
    }
});