package com.example;

import java.util.Date;
import java.util.UUID;

class Tweet {
	public UUID tweetId;
	public String text;
	public String username;
	public Date createdAt;

	public Tweet() {
	}

	public Tweet(UUID tweetId, String text, String username, Date createdAt) {
		this.tweetId = tweetId;
		this.text = text;
		this.username = username;
		this.createdAt = createdAt;
	}
}
