CREATE TABLE tweets (
  tweet_id   VARCHAR(36) PRIMARY KEY,
  text       VARCHAR(255),
  username   VARCHAR(128),
  created_at TIMESTAMP
);