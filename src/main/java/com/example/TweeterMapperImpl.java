package com.example;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class TweeterMapperImpl implements TweeterMapper {
	private final JdbcTemplate jdbcTemplate;

	public TweeterMapperImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<Tweet> findAll() {
		return jdbcTemplate.query(
				"SELECT tweet_id, text, username, created_at FROM tweets ORDER BY created_at DESC",
				tweetRowMapper);
	}

	@Override
	public List<Tweet> findByUsername(String username) {
		return jdbcTemplate.query(
				"SELECT tweet_id, text, username, created_at FROM tweets WHERE username=? ORDER BY created_at DESC",
				tweetRowMapper, username);
	}

	@Override
	public Tweet insert(Tweet tweet) {
		jdbcTemplate.update(
				"INSERT INTO tweets(tweet_id, text, username, created_at) VALUES (?, ?, ?, ?)",
				tweet.tweetId.toString(), tweet.text, tweet.username, tweet.createdAt);
		return tweet;
	}

	RowMapper<Tweet> tweetRowMapper = new RowMapper<Tweet>() {
		@Override
		public Tweet mapRow(ResultSet rs, int i) throws SQLException {
			Tweet tweet = new Tweet();
			tweet.tweetId = UUID.fromString(rs.getString("tweet_id"));
			tweet.text = rs.getString("text");
			tweet.username = rs.getString("username");
			tweet.createdAt = new Date(rs.getTimestamp("created_at").getTime());
			return tweet;
		}
	};
}
