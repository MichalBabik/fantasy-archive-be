document.addEventListener('DOMContentLoaded', function () {
    const userListContainer = document.getElementById('userList');

    fetch('http://localhost:8080/api/allUsers')
        .then(response => response.json())
        .then(users => {
            users.forEach(user => {
                const listItem = document.createElement('li');
                listItem.innerHTML = `<strong>${user.username}</strong> - Registered on ${formatDate(user.date)}`;
                userListContainer.appendChild(listItem);
            });
        })
        .catch(error => {
            console.error('Error fetching user data', error);
        });
});

function formatDate(dateString) {
    const options = { year: 'numeric', month: 'long', day: 'numeric', hour: '2-digit', minute: '2-digit', second: '2-digit' };
    return new Date(dateString).toLocaleString('en-US', options);
}