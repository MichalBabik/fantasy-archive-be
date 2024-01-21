//hides parts of gui in case of signing in or out
window.onload = function() {
    if (localStorage.getItem('userId') !== null) {
        fetchUserPrivileges();
    } else {
        showElement('signInLinkContainer');
        showElement('registrationLinkContainer');
        hideElement('signOutLinkContainer');
        hideElement('profileLinkContainer');
        hideElement('dividerLinkContainer');
    }
};

function fetchUserPrivileges() {
    var userId = localStorage.getItem('userId');
    var lock = true;

    fetch(`http://localhost:8080/api/user/isPrivileged/${userId}`)
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                if (response.status === 401) {
                    showElement('signOutLinkContainer');
                    hideElement('signInLinkContainer');
                    hideElement('registrationLinkContainer');
                    console.log("som tu alebo kde 401")
                    lock = false;
                }
            }
        })
        .then(data => {
            if (data) {
                showElement('signOutLinkContainer');
                hideElement('signInLinkContainer');
                hideElement('registrationLinkContainer');
                showElement('writeArticleLinkContainer');
                showElement('tagManagementLinkContainer');
            } else {
                if (lock) {
                    showElement('signInLinkContainer');
                    showElement('registrationLinkContainer');
                    hideElement('signOutLinkContainer');
                    hideElement('profileLinkContainer');
                    hideElement('dividerLinkContainer');
                    console.log("asi aj tu 401")
                }
            }
        })
        .catch(error => {
            console.error(`Error fetching user privileges: ${error.message}`);
        });
}

function signOut() {
    if (localStorage.getItem('userId') !== null) {
        localStorage.removeItem('userId');
        console.log('User signed out');
        window.location.href = "page-home.html";
    } else {
        console.log('User already signed out');
    }
}

function showElement(elementId) {
    var element = document.getElementById(elementId);
    if (element) {
        element.style.display = 'inline';
    }
}

function hideElement(elementId) {
    var element = document.getElementById(elementId);
    if (element) {
        element.style.display = 'none';
    }
}