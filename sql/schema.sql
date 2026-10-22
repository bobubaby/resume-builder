CREATE DATABASE IF NOT EXISTS resume_builder DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE resume_builder;

CREATE TABLE IF NOT EXISTS user (
    id          BIGINT       AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(50)  NOT NULL,
    password    VARCHAR(100) NOT NULL,
    nickname    VARCHAR(50)  DEFAULT NULL,
    token       VARCHAR(64)  DEFAULT NULL,
    create_time DATETIME     DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_username (username),
    KEY idx_token (token)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS resume (
    id          BIGINT      AUTO_INCREMENT PRIMARY KEY,
    user_id     BIGINT      NOT NULL,
    title       VARCHAR(100) NOT NULL COMMENT '标题由后端写入（默认“未命名简历”）',
    theme       VARCHAR(20) NOT NULL DEFAULT 'default',
    create_time DATETIME    DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    KEY idx_user (user_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS resume_section (
    id         BIGINT       AUTO_INCREMENT PRIMARY KEY,
    resume_id  BIGINT       NOT NULL,
    type       VARCHAR(30)  NOT NULL COMMENT 'basic/education/experience/project/skill/markdown',
    title      VARCHAR(100) NOT NULL DEFAULT '',
    content    TEXT         COMMENT 'JSON 字符串，按 type 结构不同',
    sort_order INT          NOT NULL DEFAULT 0,
    visible    TINYINT      NOT NULL DEFAULT 1,
    KEY idx_resume (resume_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;
