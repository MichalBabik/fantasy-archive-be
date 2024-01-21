$(document).ready(function () {
    $('#add-tag-btn').click(function () {
        const tagName = $('#tag-name').val().trim();

        if (tagName === '') {
            alert('Tag name cannot be empty!');
            return;
        }

        $.ajax({
            type: 'GET',
            url: 'http://localhost:8080/api/tag/exist/' + tagName,
            success: function (response) {
                console.log(response);

                if (response.exists) {

                    alert('Tag already exists!');
                } else {
                    $.ajax({
                        type: 'POST',
                        url: 'http://localhost:8080/api/tag/create/' + tagName,
                        success: function (response) {
                            if (response && response.message === 'Tag created') {
                                alert('Tag added successfully!');
                            } else {
                                alert(response.message || 'Error creating tag.');
                            }
                        },
                        error: function (xhr) {
                            if (xhr.status === 409) {
                                alert('Tag with that name already exists!');
                            } else {
                                alert('Error creating tag.');
                            }
                        }
                    });
                }
            },
            error: function (checkError) {
                alert('Error checking tag existence.');
            }
        });
    });


    $.ajax({
        type: 'GET',
        url: 'http://localhost:8080/api/tag/allTags',
        success: function (tags) {
            var deleteDropdown = $('#delete-tag-dropdown');
            deleteDropdown.empty();

            tags.forEach(function (tag) {
                deleteDropdown.append($('<option>', {
                    value: tag.id,
                    text: tag.name
                }));
            });
        },
        error: function () {
            console.error('Error fetching tags for delete dropdown.');
        }
    });

    $('#delete-tag-btn').click(function () {
        const selectedTagId = $('#delete-tag-dropdown').val();

        if (!selectedTagId) {
            alert('Please select a tag to delete!');
            return;
        }

        $.ajax({
            type: 'DELETE',
            url: 'http://localhost:8080/api/tag/' + selectedTagId,
            success: function (response) {
                if (response === 'Tag deleted') {
                    alert('Tag deleted successfully!');
                } else {
                    alert('Error deleting tag: ' + response);
                }
            },
            error: function (xhr) {
                if (xhr.status === 404) {
                    alert('Tag not found!');
                } else {
                    alert('Error deleting tag.');
                }
            }
        });
    });

});