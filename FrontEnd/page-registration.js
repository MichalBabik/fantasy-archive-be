const form = document.getElementById('form-registration')
const username = document.getElementById('username')
const email = document.getElementById('email')
const password = document.getElementById('password')
const passwordCheck = document.getElementById('password-check')
let inputsAreValid = false;

form.addEventListener('submit', async (e) => {
    inputsAreValid = true;
    e.preventDefault();
    checkInputs();

    if (inputsAreValid) {
        const emailValue = email.value.trim();
        const isEmailAvailable = await checkEmailAvailability(emailValue);

        if (!isEmailAvailable) {
            setErrorFor(email, 'Email is already registered!');
        } else {
            try {
                const response = await fetch('http://localhost:8080/api/user/create', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({
                        username: username.value,
                        email: email.value,
                        password: password.value,
                        avatar: "Images/users/basic-profile.png",
                        bio: ""
                    })
                });

                if (response.ok) {

                    const user = await response.json();
                    localStorage.setItem('userId', user.id);
                    console.log('New FantasyUser added');

                    window.location.href = "page-home.html";

                    const signOutLink = document.getElementById('signOutLink');
                    if (signOutLink) {
                        signOutLink.style.display = 'inline';
                    }
                } else {
                    console.error('Failed to create user. Status:', response.status);
                }
            } catch (error) {
                console.error('Error during user creation:', error);
            }
        }
    }
})

function checkInputs() {
    const usernameValue = username.value.trim();
    const emailValue = email.value.trim();
    const passwordValue = password.value.trim();
    const passwordCheckValue = passwordCheck.value.trim();

    if (usernameValue === '') {
        setErrorFor(username, 'Username cannot be empty!');
    } else {
        setSuccess(username);
    }

    if (emailValue === '') {
        setErrorFor(email, 'Email cannot be empty!');
    } else if (!isValidEmail(emailValue)) {
        setErrorFor(email, 'Email is not valid!');
    } else {
        setSuccess(email);
    }

    if (passwordValue === '') {
        setErrorFor(password, 'Password cannot be empty!');
    } else {
        setSuccess(password);
    }

    if (passwordCheckValue === '') {
        setErrorFor(passwordCheck, 'Repeat password cannot be empty!');
    } else if (passwordValue !== passwordCheckValue) {
        setErrorFor(passwordCheck, 'Passwords do not match!');
    } else {
        setSuccess(passwordCheck);
    }
}

function setErrorFor(input, message) {
    const formControl = input.parentElement.parentElement; // .form-control bg-dark text-light
    const small = formControl.querySelector('small');
    small.innerText = message;
    formControl.className = 'form-control bg-dark text-light error'
    inputsAreValid = false;
}

function setSuccess(input) {
    const formControl = input.parentElement.parentElement; // .form-control bg-dark text-light
    formControl.className = 'form-control bg-dark text-light';
}

function isValidEmail(email) {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
}

async function checkEmailAvailability(email) {
    try {
        const response = await fetch(`http://localhost:8080/api/user/check/${encodeURIComponent(email)}`);
        return response.status === 200;
    } catch (error) {
        console.error('Error checking email availability', error);
        return false;
    }
}

