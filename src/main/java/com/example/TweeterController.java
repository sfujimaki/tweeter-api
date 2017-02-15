package com.example;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TweeterController {
	private final JdbcTemplate jdbcTemplate;

	public TweeterController(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@GetMapping("timelines")
	List<Tweet> getTimelines() {
		return jdbcTemplate.query(
				"SELECT tweet_id, text, username, created_at FROM tweets ORDER BY created_at DESC",
				tweetRowMapper);
	}

	@GetMapping("tweets")
	List<Tweet> tweets(Authentication authentication) {
		String username = authentication.getName();
		return jdbcTemplate.query(
				"SELECT tweet_id, text, username, created_at FROM tweets WHERE username=? ORDER BY created_at DESC",
				tweetRowMapper, username);
	}

	@PostMapping("tweets")
	Tweet postTweets(@RequestBody Tweet tweet, Authentication authentication) {
		tweet.tweetId = UUID.randomUUID();
		tweet.createdAt = new Date();
		tweet.username = authentication.getName();
		jdbcTemplate.update(
				"INSERT INTO tweets(tweet_id, text, username, created_at) VALUES (?, ?, ?, ?)",
				tweet.tweetId.toString(), tweet.text, tweet.username, tweet.createdAt);
		return tweet;
	}

	static class Tweet {
		public UUID tweetId;
		public String text;
		public String username;
		public Date createdAt;
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
