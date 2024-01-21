$(document).ready(function () {
    const createCommentUrl = "http://localhost:8080/api/comment/create/";

    const userId = localStorage.getItem('userId');
    if (userId === null) {
        window.location.replace("page-sign-in.html");
    }

    function addComment(comment, username) {
        if (comment && comment.id && comment.text && comment.date_added) {
            const formattedDate = new Date(comment.date_added).toLocaleString();

            const commentBlock = `
            <div>
                <strong>${username}:</strong> ${comment.text} 
                <br>
                <small style="color: grey;">(Added on ${formattedDate})</small>
            </div>
            <br>
        `;

            $("#comment-section").append(commentBlock);
        }
    }

    function getAllCommentsAndDisplay(commentUsernames) {

        $.ajax({
            type: "GET",
            url: "http://localhost:8080/api/comment/getAll",
            success: function (comments) {
                $("#comment-section").empty();
                comments.forEach(function (comment) {
                    const userId = commentUsernames[comment.id];
                    if (userId) {
                        addComment(comment, userId);
                    }
                });
            },
            error: function (xhr, status, error) {
                console.error("Error getting comments:", xhr.status, xhr.statusText, xhr.responseText);
            }
        });
    }

    function getCommentUsernames(callback) {
        $.ajax({
            type: "GET",
            url: "http://localhost:8080/api/comment/getListOfUsernames",
            success: function (commentUsernames) {
                callback(commentUsernames);
            },
            error: function (xhr, status, error) {
                console.error("Error getting comment usernames:", xhr.status, xhr.statusText, xhr.responseText);
            }
        });
    }


    getCommentUsernames(function (commentUsernames) {
        getAllCommentsAndDisplay(commentUsernames);
    });


    function getUserRole(userId) {
        return new Promise((resolve, reject) => {
            $.ajax({
                type: "GET",
                url: `http://localhost:8080/api/user/getRole/${userId}`,
                success: function (role) {
                    resolve(role);
                },
                error: function (xhr, status, error) {
                    reject(error);
                }
            });
        });
    }

    $("#clear-discussion").click(async function () {
        const userId = localStorage.getItem('userId');
        try {
            const userRole = await getUserRole(userId);
            console.log(userRole);
            if (userRole !== 'MODERATOR' && userRole !== 'ADMIN') {
                alert("You do not have the appropriate role to delete comments!");
                return;
            }

            if (confirm("Are you sure you want to delete all comments?")) {
                $.ajax({
                    type: "DELETE",
                    url: `http://localhost:8080/api/comment/deleteAll/?userId=${userId}`,
                    success: function (response) {
                        console.log(response);
                        getAllCommentsAndDisplay({});
                    },
                    error: function (xhr, status, error) {
                        console.error("Error deleting comments:", xhr.status, xhr.statusText, xhr.responseText);
                    }
                });
            }
        } catch (error) {
            console.error("Error fetching user role:", error);
        }
    });

    $("#form-discussion").submit(function (event) {
        event.preventDefault();

        const commentText = $("#comment").val();
        const userId = localStorage.getItem('userId');

        if (!commentText.trim()) {
            alert("Comment cannot be blank!");
            return;
        }

        const commentData = {
            text: commentText
        };

        $.ajax({
            type: "POST",
            url: createCommentUrl + userId,
            contentType: "application/json",
            data: JSON.stringify(commentData),
            success: function (response) {
                console.log(response);
                getCommentUsernames(function (commentUsernames) {
                    getAllCommentsAndDisplay(commentUsernames);
                });

                $("#comment").val("");
            },
            error: function (xhr, status, error) {
                console.error("Error creating comment:", xhr.responseText);
            }
        });
    });
});