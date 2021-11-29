/* for test */
DROP TABLE IF EXISTS TistoryCategory;
DROP TABLE IF EXISTS TistoryPost;
DROP TABLE IF EXISTS Post;
DROP TABLE IF EXISTS TistoryInfo;
DROP TABLE IF EXISTS Category;


CREATE TABLE IF NOT EXISTS Category
(
    cat_id                  BIGINT NOT NULL,
    cat_name                VARCHAR(256) NOT NULL,
    cat_paraent             BIGINT,
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

CREATE TABLE IF NOT EXISTS TistoryInfo
(
    tistory_blog_id         VARCHAR(64) NOT NULL,
    tistory_blog_name       VARCHAR(256) NOT NULL,
    tistory_access_token    TEXT NOT NULL,

    PRIMARY KEY (tistory_blog_id)
);

CREATE TABLE IF NOT EXISTS TistoryCategory
(
    tistory_cat_id          BIGINT NOT NULL,
    tistory_blog_id         VARCHAR(64) NOT NULL,
    cat_id                  BIGINT NOT NULL,
    tistory_entries         BIGINT NOT NULL,

    PRIMARY KEY (tistory_cat_id),

    FOREIGN KEY (tistory_blog_id)
        REFERENCES TistoryInfo(tistory_blog_id),

    FOREIGN KEY (cat_id)
        REFERENCES Category(cat_id)
);

CREATE TABLE IF NOT EXISTS TistoryPost
(
    tistory_post_id         BIGINT NOT NULL,
    tistory_blog_id         VARCHAR(256) NOT NULL,
    post_id                 BIGINT NOT NULL,
    tistory_post_date       DATE NOT NULL,
    tistory_visibility      TINYINT NOT NULL,

    PRIMARY KEY (tistory_post_id),

    FOREIGN KEY (tistory_blog_id)
        REFERENCES TistoryInfo(tistory_blog_id),

    FOREIGN KEY (post_id)
        REFERENCES Post(post_id)
);