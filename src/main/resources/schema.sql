/* for test */
DROP TABLE IF EXISTS Tistory_category;
DROP TABLE IF EXISTS Tistory_post;
DROP TABLE IF EXISTS Post;
DROP TABLE IF EXISTS Tistory_info;
DROP TABLE IF EXISTS Category;

CREATE TABLE IF NOT EXISTS Category
(
    cat_id                  BIGINT NOT NULL,
    cat_name                VARCHAR(256) NOT NULL,
    cat_parent              BIGINT,
    cat_visible             BIT NOT NULL,

    PRIMARY KEY (cat_id)
);

CREATE TABLE IF NOT EXISTS Post
(
    post_id                 BIGINT NOT NULL,
    cat_id                  BIGINT NOT NULL,
    post_title              VARCHAR(256) NOT NULL,
    post_content            TEXT,
    post_tags               VARCHAR(256),
    post_visible            BIT NOT NULL,
    post_deleted            BIT NOT NULL,

    PRIMARY KEY (post_id),

    FOREIGN KEY (cat_id)
        REFERENCES Category(cat_id)
);

CREATE TABLE IF NOT EXISTS Tistory_info
(
    tistory_blog_name       VARCHAR(64) NOT NULL,
    tistory_blog_id         VARCHAR(64) NOT NULL,
    tistory_access_token    VARCHAR(128) NOT NULL,

    PRIMARY KEY (tistory_blog_name)
);

CREATE TABLE IF NOT EXISTS Tistory_category
(
    tistory_cat_id          BIGINT NOT NULL,
    tistory_blog_name       VARCHAR(64) NOT NULL,
    cat_id                  BIGINT NOT NULL,

    PRIMARY KEY (tistory_cat_id),

    FOREIGN KEY (tistory_blog_name)
        REFERENCES Tistory_info(tistory_blog_name),

    FOREIGN KEY (cat_id)
        REFERENCES Category(cat_id)
);

CREATE TABLE IF NOT EXISTS Tistory_post
(
    tistory_post_id         BIGINT NOT NULL,
    tistory_blog_name       VARCHAR(64) NOT NULL,
    post_id                 BIGINT NOT NULL,
    tistory_post_date       DATE NOT NULL,

    PRIMARY KEY (tistory_post_id),

    FOREIGN KEY (tistory_blog_name)
        REFERENCES Tistory_info(tistory_blog_name),

    FOREIGN KEY (post_id)
        REFERENCES Post(post_id)
);