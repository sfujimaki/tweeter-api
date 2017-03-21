package com.example;

import java.util.List;

public interface TweeterMapper {
	List<Tweet> findAll();

	List<Tweet> findByUsername(String username);

	Tweet insert(Tweet tweet);
}
