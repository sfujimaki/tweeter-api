package com.example;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.Base64Utils;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@WebMvcTest(TweeterController.class)
public class TweeterControllerTest {
	@Autowired
	MockMvc mvc;
	@Autowired
	ObjectMapper objectMapper;
	@MockBean
	TweeterMapper tweeterMapper;

	@Before
	public void setUp() throws Exception {

	}

	@Test
	public void getTimelines() throws Exception {
		given(tweeterMapper.findAll()).willReturn(Fixtures.tweetsAll());

		mvc.perform(get("/v1/timelines")
				.header(HttpHeaders.AUTHORIZATION,
						"Basic " + Base64Utils
								.encodeToString(("user:password".getBytes())))
				.accept(MediaType.APPLICATION_JSON_UTF8)).andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$", hasSize(4)))
				.andExpect(jsonPath("$[0].tweetId",
						is("00000000-0000-0000-0000-000000000000")))
				.andExpect(jsonPath("$[0].text", is("tweet1")))
				.andExpect(jsonPath("$[0].username", is("user")))
				.andExpect(jsonPath("$[0].createdAt", notNullValue()))
				.andExpect(jsonPath("$[1].tweetId",
						is("00000000-0000-0000-0000-000000000001")))
				.andExpect(jsonPath("$[1].text", is("tweet2")))
				.andExpect(jsonPath("$[1].username", is("user")))
				.andExpect(jsonPath("$[1].createdAt", notNullValue()))
				.andExpect(jsonPath("$[2].tweetId",
						is("00000000-0000-0000-0000-000000000002")))
				.andExpect(jsonPath("$[2].text", is("tweet3")))
				.andExpect(jsonPath("$[2].username", is("foo")))
				.andExpect(jsonPath("$[2].createdAt", notNullValue()))
				.andExpect(jsonPath("$[3].tweetId",
						is("00000000-0000-0000-0000-000000000003")))
				.andExpect(jsonPath("$[3].text", is("tweet4")))
				.andExpect(jsonPath("$[3].username", is("user")))
				.andExpect(jsonPath("$[3].createdAt", notNullValue()));
	}

	@Test
	public void tweets() throws Exception {
		given(tweeterMapper.findByUsername("user"))
				.willReturn(Fixtures.tweetsByUsername("user"));

		mvc.perform(get("/v1/tweets")
				.header(HttpHeaders.AUTHORIZATION,
						"Basic " + Base64Utils
								.encodeToString(("user:password".getBytes())))
				.accept(MediaType.APPLICATION_JSON_UTF8)).andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$", hasSize(3)))
				.andExpect(jsonPath("$[0].tweetId",
						is("00000000-0000-0000-0000-000000000000")))
				.andExpect(jsonPath("$[0].text", is("tweet1")))
				.andExpect(jsonPath("$[0].username", is("user")))
				.andExpect(jsonPath("$[0].createdAt", notNullValue()))
				.andExpect(jsonPath("$[1].tweetId",
						is("00000000-0000-0000-0000-000000000001")))
				.andExpect(jsonPath("$[1].text", is("tweet2")))
				.andExpect(jsonPath("$[1].username", is("user")))
				.andExpect(jsonPath("$[1].createdAt", notNullValue()))
				.andExpect(jsonPath("$[2].tweetId",
						is("00000000-0000-0000-0000-000000000003")))
				.andExpect(jsonPath("$[2].text", is("tweet4")))
				.andExpect(jsonPath("$[2].username", is("user")))
				.andExpect(jsonPath("$[2].createdAt", notNullValue()));
	}

	@Test
	public void postTweets() throws Exception {
		given(tweeterMapper.insert(Matchers.any())).willReturn(Fixtures.tweet());

		Map<String, Object> body = new HashMap<>();
		body.put("text", "tweet1");

		mvc.perform(post("/v1/tweets")
				.header(HttpHeaders.AUTHORIZATION,
						"Basic " + Base64Utils
								.encodeToString(("user:password".getBytes())))
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(objectMapper.writeValueAsString(body))).andDo(print())
				.andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(
						jsonPath("$.tweetId", is("00000000-0000-0000-0000-000000000000")))
				.andExpect(jsonPath("$.text", is("tweet1")))
				.andExpect(jsonPath("$.username", is("user")))
				.andExpect(jsonPath("$.createdAt", notNullValue()));
	}

}