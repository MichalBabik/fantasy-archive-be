document.addEventListener('DOMContentLoaded', function() {
    // header component
    const headerContainer = document.getElementById('header-container');
    const headerRequest = new XMLHttpRequest();
    headerRequest.open('GET', 'header.html', true);
    headerRequest.onreadystatechange = function() {
        if (headerRequest.readyState === XMLHttpRequest.DONE && headerRequest.status === 200) {
            headerContainer.innerHTML = headerRequest.responseText;
        }
    };
    headerRequest.send();

    // footer component
    const footerContainer = document.getElementById('footer-container');
    const footerRequest = new XMLHttpRequest();
    footerRequest.open('GET', 'footer.html', true);
    footerRequest.onreadystatechange = function() {
        if (footerRequest.readyState === XMLHttpRequest.DONE && footerRequest.status === 200) {
            footerContainer.innerHTML = footerRequest.responseText;
        }
    };
    footerRequest.send();

    // home component
    const homeContainer = document.getElementById('home-container');
    const homeRequest = new XMLHttpRequest();
    homeRequest.open('GET', 'home.html', true);
    homeRequest.onreadystatechange = function() {
        if (homeRequest.readyState === XMLHttpRequest.DONE && homeRequest.status === 200) {
            homeContainer.innerHTML = homeRequest.responseText;
        }
    };
    homeRequest.send();

    // articles component
    const articlesContainer = document.getElementById('articles-container');
    const articlesRequest = new XMLHttpRequest();
    articlesRequest.open('GET', 'articles.html', true);
    articlesRequest.onreadystatechange = function() {
        if (articlesRequest.readyState === XMLHttpRequest.DONE && articlesRequest.status === 200) {
            articlesContainer.innerHTML = articlesRequest.responseText;
        }
    };
    articlesRequest.send();


    // article component
    const articleContainer = document.getElementById('article-container');
    const articleRequest = new XMLHttpRequest();
    articleRequest.open('GET', 'article.html', true);
    articleRequest.onreadystatechange = function() {
        if (articleRequest.readyState === XMLHttpRequest.DONE && articleRequest.status === 200) {
            articleContainer.innerHTML = articleRequest.responseText;
        }
    };
    articleRequest.send();

});





