
//hides parts of gui in case of signing in or out
window.onload = function() {
    if (localStorage.getItem('userId') !== null) {
        showElement('signOutLinkContainer');
        showElement('writeArticleLinkContainer');
        hideElement('signInLinkContainer');
        hideElement('registrationLinkContainer')

    } else {
        showElement('signInLinkContainer');
        showElement('registrationLinkContainer');
        hideElement('signOutLinkContainer');
        hideElement('profileLinkContainer');
        hideElement('dividerLinkContainer');

    }
};

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


