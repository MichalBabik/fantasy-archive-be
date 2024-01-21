document.addEventListener("DOMContentLoaded", function () {
    const userId = localStorage.getItem("userId");

    if (userId) {
        fetchUserData(userId);
    } else {
        window.location.href = "page-sign-in.html";
    }
});

function fetchUserData(userId) {
    const apiUrl = `http://localhost:8080/api/user/${userId}`;

    fetch(apiUrl)
        .then(response => response.json())
        .then(userData => {
            const profileHtml = generateProfileHtml(userData);

            document.getElementById("profile-content").innerHTML = profileHtml;


            document.getElementById("save-profile-changes").addEventListener("click", function () {
                updateProfile(userId);
            });
        })
        .catch(error => {
            console.error("Error fetching user data:", error);
        });
}

function updateProfile(userId) {
    const bio = document.getElementById("user-bio").value;
    const imageInput = document.getElementById("user-image");
    const imagePath = imageInput.files.length > 0 ? `Images/users/${imageInput.files[0].name}` : null;


    const requestData = {
        bio: bio,
        avatar: imagePath,
    };

    $.ajax({
        type: "PUT",
        url: `http://localhost:8080/api/user/update/${userId}`,
        contentType: "application/json",
        data: JSON.stringify(requestData),
        success: function (updatedUserData) {

            updateProfileUI(updatedUserData);
            console.log("Profile updated successfully:", updatedUserData);
        },
        error: function (xhr, status, error) {
            console.error("Error updating profile:", xhr.responseText);
        }
    });
}

function updateProfileUI(userData) {

    const profilePictureElement = document.querySelector(".profile-picture");
    if (profilePictureElement) {
        profilePictureElement.src = userData.avatar;
    }


    const bioElement = document.getElementById("user-bio");
    if (bioElement) {
        bioElement.value = userData.bio || "";
    }
}

function generateProfileHtml(userData) {
    const profilePicture = userData.avatar;

    return `
        <h1>${userData.username}'s Profile</h1>

        <div class="spacer"></div>

        <div class="row">
        
            
            <div class="col-6">
                <div class="form-control bg-dark text-light">
                    <div class="col-12">
                        <label>Profile Picture:</label>
                        <img src="${profilePicture}" alt="Profile Picture" class="profile-picture">
                    </div>
                    <div class="col-12">
                        <label>Upload new image:</label>
                        <input type="file" class="form-control" id="user-image" accept="image/*">
                    </div>
                    <div class="col-12">
                        <small>Error</small>
                    </div>
                </div>
            </div>
        
      
            <div class="col-6">
                <div class="form-control bg-dark text-light">
                    <div class="col-12">
                        <label>Bio:</label>
                    </div>
                    <div class="col-12">
                        <textarea class="form-control" rows="4" id="user-bio">${userData.bio || ""}</textarea>
                    </div>
                    <div class="col-12">
                        <small>Error</small>
                    </div>
                </div>
            </div>

        </div>

        <div class="spacer"></div>

        <button class="btn btn-dark" id="save-profile-changes">Save Changes</button>
        
        <div class="spacer"></div>
    `;
}
