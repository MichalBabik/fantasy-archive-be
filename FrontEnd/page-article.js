const getParameterByName = (name) => {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get(name);
};

const getArticleAuthorDetails = (articleId) => {
    return new Promise((resolve, reject) => {
        // Fetch the author details using the getArticleCreatorUsername endpoint
        $.ajax({
            type: 'GET',
            url: `http://localhost:8080/api/article/getArticleCreatorDetails/${articleId}`,
            success: function (authorDetails) {
                //console.log('Author details received:', authorDetails);
                resolve(authorDetails);
            },
            error: function (error) {
                console.error('Error fetching author details:', error);
                reject(error);
            }
        });
    });
};

const generateArticlePageHTML = (article, authorDetails) => {
    const dateAdded = new Date(article.date_added);
    const formattedDate = `${dateAdded.getDate()}.${dateAdded.getMonth() + 1}.${dateAdded.getFullYear()} ${dateAdded.getHours()}:${(dateAdded.getMinutes() < 10 ? '0' : '') + dateAdded.getMinutes()}`;

    const author = authorDetails && authorDetails.username ? authorDetails.username : 'Unknown Author';
    const authorPhoto = authorDetails && authorDetails.avatar ? authorDetails.avatar : 'Images/users/basic-profile.png';
    const authorBio = authorDetails && authorDetails.bio ? authorDetails.bio : 'No bio available.';

    const articlePageHTML = `
        <div class="main-header" id="header-container"></div>
        <div class="my-page-body">
            <div class="container">
                <div class="row">
                    <div class="col-xs-12 col-lg-8">
                        <div class="col-12 fs-5 p-3 ">
                            <h2>${article.title}</h2>
                        </div>

                        <div class="text-article">
                            <div class="row">
                                <div class="col-12 text-center d-flex justify-content-center">
                                    <div class="article-img">
                                        <img class="article-img" src="${article.image}" alt="${article.title}">
                                    </div>
                                </div>
                            </div>

                            <p>${article.text}</p>

                            <div class="spacer"></div>
                            <div class="col-12">
                                <h6 class="article-annotation">by ${author}, ${formattedDate}</h6>
                            </div>
                            <div class="spacer"></div>
                        </div>
                    </div>

                    <div class="col-xs-12 col-lg-4">
                        <div class="row">
                            <div class="col-12 text-center d-flex justify-content-center">
                                <div class="writer-img">
                                     <img src="${authorPhoto}" alt="Photo of article writer ${author}">
                                </div>
                            </div>
                            <div class="col-12 text-center">
                                <h2>${author}</h2>
                            </div>
                            <div class="col-xs-12 col-lg-2 "></div>
                            <div class="col-xs-12 col-lg-8 text-center">
                                <h6 class="article-annotation">${authorBio}</h6>
                            </div>
                            
                            <div class="spacer"></div>
                            <div class="spacer"></div>
                            <div class="spacer"></div>
                            <div class="spacer"></div>
                            
                            <div class="col-12 text-center">
                                <h3>Add tags to article</h3>
                                <select id="tagsDropdown"> </select>
                                <button class="btn btn-dark" type="button" id="addTagBtn">Add Tag</button>
                                
                            </div>
                            
                            <div class="col-12 text-center">
                                <div id="articleTagsContainer"></div>
                            </div>
                            
                        </div>
                    </div>
                </div>

                <div class="col-12">
                    <div class="interactive-link">
                        <a class="sign-in-img" onclick="history.back()"><img src="Images/icons/arrow-left-circle-fill.svg"
                                                                            alt="Left arrow"></a>
                    </div>
                </div>
                <div class="spacer"></div>
            </div>
        </div>
    </html>`;

    return articlePageHTML;
};

const updateArticlePage = () => {
    const articleId = getParameterByName('articleId');

    if (!articleId) {
        console.warn('No articleId found in the URL. This might be the homepage.');
        return;
    }

    getArticleAuthorDetails(articleId)
        .then(authorDetails => {
            $.ajax({
                type: 'GET',
                url: `http://localhost:8080/api/article/getArticle/${articleId}`,
                success: function (article) {
                    // Pass the entire authorDetails object to generateArticlePageHTML
                    const articleContentHTML = generateArticlePageHTML(article, authorDetails);

                    $('.my-page-body').html(articleContentHTML);

                    // Fetch and display tags associated with the article
                    fetchArticleTags(articleId);

                    $.ajax({
                        type: 'GET',
                        url: 'http://localhost:8080/api/tag/allTags',
                        success: function (tags) {
                            var dropdown = $('#tagsDropdown');

                            dropdown.empty();

                            tags.forEach(function (tag) {
                                dropdown.append($('<option>', {
                                    value: tag.id,
                                    text: tag.name
                                }));
                            });
                        },
                        error: function () {
                            console.error('Error fetching tags.');
                        }
                    });

                },
                error: function (error) {
                    console.error('Error fetching article:', error);
                }
            });
        })
        .catch(error => {
            console.error('Error fetching author details:', error);
        });
};


const fetchArticleTags = (articleId) => {
    console.log('Fetching tags for articleId:', articleId);

    // Fetch article tags using the provided endpoint
    $.ajax({
        type: 'GET',
        url: `http://localhost:8080/api/article/getArticleTags/${articleId}`,
        success: function (tags) {
            console.log('Tags received:', tags);
            // Display the tags below the "Add Tag" button
            displayArticleTags(tags);
        },
        error: function (error) {
            console.error('Error fetching article tags:', error);
        }
    });
};

const displayArticleTags = (tags) => {
    var tagsContainer = $('#articleTagsContainer');
    tagsContainer.empty();

    tags.forEach(function (tag) {
        tagsContainer.append($('<div>', {
            text: '#' + tag.name,
            class: 'tag'
        }));
        tagsContainer.append('<br>');
    });
};

const handleAddTagClick = () => {
    const articleId = getParameterByName('articleId');
    const selectedTagId = $('#tagsDropdown').val();

    // Check if a tag is selected
    if (!selectedTagId) {
        console.error('No tag selected!');
        return;
    }

    // Log information for debugging
    console.log('articleId: ' + articleId + ', selectedTagId: ' + selectedTagId);

    // Send the tagId directly in the URL
    $.ajax({
        type: 'PUT',
        url: `http://localhost:8080/api/article/updateTags/${articleId}/${selectedTagId}`,
        contentType: 'application/json',
        success: function (response) {
            console.log('Tag added successfully:', response);
            // Optionally, you can update the UI or perform other actions here
            alert('Tag added successfully!');
            // Fetch and display updated tags associated with the article
            fetchArticleTags(articleId);
        },
        error: function (error) {
            console.error('Error adding tag:', error);
            // Optionally, handle the error or display an error message
            alert('Error adding tag: ' + error.responseJSON.message);
        }
    });
};

$(document).ready(function () {
    updateArticlePage();

    $(document).on('click', '#addTagBtn', handleAddTagClick);

});