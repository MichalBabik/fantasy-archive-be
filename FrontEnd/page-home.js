const updateHomepage = () => {
    $.ajax({
        type: 'GET',
        url: 'http://localhost:8080/api/article/getHomepageArticles',
        success: function (articles) {
            const fetchAuthorPromises = articles.map(article => {
                return new Promise((resolve, reject) => {
                    $.ajax({
                        type: 'GET',
                        url: `http://localhost:8080/api/article/getArticleCreatorUsername/${article.id}`,
                        success: function (username) {
                            const author = username || 'Unknown Author';
                            resolve({
                                article,
                                author
                            });
                        },
                        error: function (error) {
                            console.error('Error fetching author username:', error);
                            reject(error);
                        }
                    });
                });
            });

            // Wait for all Promises to resolve
            Promise.all(fetchAuthorPromises)
                .then(articleDataArray => {
                    let homepageHTML = '';

                    articleDataArray.forEach(({ article, author }) => {
                        const dateAdded = new Date(article.date_added);
                        const formattedDate = `${dateAdded.getDate()}.${dateAdded.getMonth() + 1}.${dateAdded.getFullYear()} ${dateAdded.getHours()}:${(dateAdded.getMinutes() < 10 ? '0' : '') + dateAdded.getMinutes()}`;

                        const newArticleHTML = `
                            <h5 class="home-article-header">
                                <a class="hyper-link" href="#" onclick="showArticlePage(${article.id})">${article.title}</a>
                            </h5>
                            <div class="col-xs-12 col-lg-5">
                                <a class="img-link" href="#" onclick="showArticlePage(${article.id})">
                                    <img class="outlook-article-img" src="${article.image}" alt="${article.title}">
                                </a>
                            </div>
                            <div class="col-xs-12 col-lg-7">
                                <p class="home-text-article">${article.description}</p>
                                <h6 class="article-annotation">by ${author}, ${formattedDate}</h6>
                            </div>
                            <div class="spacer"></div>
                        `;

                        homepageHTML += newArticleHTML; // Accumulate each new article
                    });

                    homepageHTML += `
                        <div class="col-12 text-center">
                            <a class="icon-link icon-link-hover" href="page-articles.html">
                                <img src="Images/icons/arrow-right-circle-fill.svg" alt="Bootstrap icon right arrow" width="32" height="32">
                            </a>
                        </div>
                    `;

                    $('.fs-5 .row').html(homepageHTML);
                })
                .catch(error => {
                    console.error('Error fetching author usernames:', error);
                });
        },
        error: function (error) {
            console.error('Error fetching articles:', error);
        }
    });
};

const showArticlePage = (articleId) => {
    window.location.href = `page-article.html?articleId=${articleId}`;
};

$(document).ready(function () {
    updateHomepage();
});