CREATE TABLE IF NOT EXISTS likes (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     createDateTime TIMESTAMP,
                                     modifiedDateTime TIMESTAMP,
                                     post_post_id BIGINT,
                                     user_user_id BIGINT,
                                     FOREIGN KEY (post_post_id) REFERENCES posts(post_id),
    FOREIGN KEY (user_user_id) REFERENCES users(user_id)
    );