package com.example;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
public class TweeterController {
	private final TweeterMapper tweeterMapper;

	public TweeterController(TweeterMapper tweeterMapper) {
		this.tweeterMapper = tweeterMapper;
	}

	@GetMapping("v1/timelines")
	List<Tweet> getTimelines() {
		return tweeterMapper.findAll();
	}

	@GetMapping("v1/tweets")
	List<Tweet> tweets(Authentication authentication) {
		String username = authentication.getName();
		return tweeterMapper.findByUsername(username);
	}

	@PostMapping("v1/tweets")
	@ResponseStatus(HttpStatus.CREATED)
	Tweet postTweets(@RequestBody Tweet tweet, Authentication authentication) {
		tweet.tweetId = UUID.randomUUID();
		tweet.createdAt = new Date();
		tweet.username = authentication.getName();
		return tweeterMapper.insert(tweet);
	}
}
