/* for test */
# DROP TABLE IF EXISTS Tistory_category;
# DROP TABLE IF EXISTS Tistory_post;
# DROP TABLE IF EXISTS Post;
# DROP TABLE IF EXISTS Tistory_info;
# DROP TABLE IF EXISTS Category;

CREATE TABLE IF NOT EXISTS category
(
    cat_id                  BIGINT AUTO_INCREMENT NOT NULL,
    cat_name                VARCHAR(256) NOT NULL,
    cat_parent              BIGINT,
    cat_visible             BIT NOT NULL,

    PRIMARY KEY (cat_id)
);

CREATE TABLE IF NOT EXISTS post
(
    post_id                 BIGINT AUTO_INCREMENT NOT NULL,
    cat_id                  BIGINT NOT NULL,
    post_title              VARCHAR(256) NOT NULL,
    post_content            TEXT,
    post_tags               VARCHAR(256),
    post_visible            BIT NOT NULL,

    PRIMARY KEY (post_id),

    FOREIGN KEY (cat_id)
        REFERENCES category(cat_id)
);

CREATE TABLE IF NOT EXISTS tistory_info
(
    tistory_blog_name       VARCHAR(64) NOT NULL,
    tistory_blog_id         VARCHAR(64) NOT NULL,
    tistory_access_token    VARCHAR(128) NOT NULL,

    PRIMARY KEY (tistory_blog_name)
);

CREATE TABLE IF NOT EXISTS tistory_category
(
    tistory_cat_id          BIGINT NOT NULL,
    tistory_blog_name       VARCHAR(64) NOT NULL,
    cat_id                  BIGINT NOT NULL,

    PRIMARY KEY (tistory_cat_id),

    FOREIGN KEY (tistory_blog_name)
        REFERENCES tistory_info(tistory_blog_name),

    FOREIGN KEY (cat_id)
        REFERENCES category(cat_id)
);

CREATE TABLE IF NOT EXISTS tistory_post
(
    tistory_post_id         BIGINT NOT NULL,
    tistory_blog_name       VARCHAR(64) NOT NULL,
    post_id                 BIGINT NOT NULL,
    tistory_post_date       DATETIME NOT NULL,

    PRIMARY KEY (tistory_post_id),

    FOREIGN KEY (tistory_blog_name)
        REFERENCES tistory_info(tistory_blog_name),

    FOREIGN KEY (post_id)
        REFERENCES post(post_id)
);