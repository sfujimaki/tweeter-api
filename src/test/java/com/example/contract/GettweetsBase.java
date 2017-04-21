package com.example.contract;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.Fixtures;
import com.example.TweeterApiApplication;
import com.example.TweeterMapper;

import io.restassured.RestAssured;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TweeterApiApplication.class)
public abstract class GettweetsBase {
	@LocalServerPort
	int port;
	@MockBean
	TweeterMapper tweeterMapper;

	@Before
	public void setup() {
		RestAssured.port = port;
		BDDMockito.given(tweeterMapper.findAll()).willReturn(Fixtures.tweetsAll());
		BDDMockito.given(tweeterMapper.findByUsername("user"))
				.willReturn(Fixtures.tweetsByUsername("user"));
		BDDMockito.given(tweeterMapper.findByUsername("foo"))
				.willReturn(Fixtures.tweetsByUsername("foo"));
	}
}
