package com.example;

import java.util.*;
import java.util.stream.Collectors;

public class Fixtures {

	public static Tweet tweet() {
		return new Tweet(UUID.fromString("00000000-0000-0000-0000-000000000000"),
				"tweet1", "user", new Date());
	}

	public static List<Tweet> tweetsAll() {
		return Arrays.asList(
				new Tweet(UUID.fromString("00000000-0000-0000-0000-000000000000"),
						"tweet1", "user", new Date()),
				new Tweet(UUID.fromString("00000000-0000-0000-0000-000000000001"),
						"tweet2", "user", new Date()),
				new Tweet(UUID.fromString("00000000-0000-0000-0000-000000000002"),
						"tweet3", "foo", new Date()),
				new Tweet(UUID.fromString("00000000-0000-0000-0000-000000000003"),
						"tweet4", "user", new Date()));
	}

	public static List<Tweet> tweetsByUsername(String username) {
		return tweetsAll().stream().filter(t -> Objects.equals(username, t.username))
				.collect(Collectors.toList());
	}
}
