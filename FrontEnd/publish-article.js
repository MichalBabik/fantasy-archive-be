const publishArticle = () => {
    const articleHeader = $('#article-header').val();
    const description = $('#article-description').val();

    const imageName = $('#article-image').val().split('\\').pop();
    const imagePath = `Images/articles/${imageName}`;

    const text = $('#article-text').val();

    const articleData = {
        title: articleHeader,
        description: description,
        text: text,
        image: imagePath,
        date_added: new Date().toISOString(),
        articleTag: [],
        commentList: [],
        fantasyUser: null
    };

    console.log('Article Data:', JSON.stringify(articleData));

    $.ajax({
        type: 'POST',
        url: 'http://localhost:8080/api/article/publish/' + localStorage.getItem('userId'),
        contentType: 'application/json',
        data: JSON.stringify(articleData),
        success: function (response) {
            console.log('Article published successfully:', response);
            window.location.href = "page-home.html";
        },
        error: function (error) {
            console.error('Error publishing article:', error);
        }
    });
};


$(document).ready(function() {
    $('#article-publish').on('click', function (e) {
        e.preventDefault();
        console.log('Button clicked!');
        publishArticle();
    });
});



