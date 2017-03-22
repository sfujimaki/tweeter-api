package com.example;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.restassured3.operation.preprocess.RestAssuredPreprocessors.modifyUris;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Base64Utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TweeterControllerTest {
	@Rule
	public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation(
			"target/generated-snippets");
	RequestSpecification documentationSpec;
	@Autowired
	ObjectMapper objectMapper;
	@MockBean
	TweeterMapper tweeterMapper;
	@LocalServerPort
	int port;

	@Before
	public void setUp() throws Exception {
		RestAssured.port = port;
		this.documentationSpec = new RequestSpecBuilder()
				.addFilter(documentationConfiguration(this.restDocumentation)).build();
	}

	@Test
	public void getTimelines() throws Exception {
		given(tweeterMapper.findAll()).willReturn(Fixtures.tweetsAll());

		RestAssured.given(this.documentationSpec).header(HttpHeaders.AUTHORIZATION,
				"Basic " + Base64Utils.encodeToString(("user:password".getBytes())))
				.accept("application/json")
				.filter(document("api/get-timelines",
						preprocessRequest(modifyUris().scheme("https")
								.host("api.example.com").removePort()),
						responseFields(
								fieldWithPath("[].tweetId")
										.description("The ID of this tweet"),
								fieldWithPath("[].text")
										.description("The text of this tweet"),
								fieldWithPath("[].username")
										.description("Owner's username of this tweet"),
								fieldWithPath("[].createdAt")
										.description("The date of this tweet"))))
				.log().all().when().get("/v1/timelines").then().log().all().assertThat()
				.statusCode(is(200)).contentType(ContentType.JSON)
				.content("$", hasSize(4))
				.body("[0].tweetId", is("00000000-0000-0000-0000-000000000000"))
				.body("[0].text", is("tweet1")).body("[0].username", is("user"))
				.body("[0].createdAt", notNullValue())
				.body("[1].tweetId", is("00000000-0000-0000-0000-000000000001"))
				.body("[1].text", is("tweet2")).body("[1].username", is("user"))
				.body("[1].createdAt", notNullValue())
				.body("[2].tweetId", is("00000000-0000-0000-0000-000000000002"))
				.body("[2].text", is("tweet3")).body("[2].username", is("foo"))
				.body("[2].createdAt", notNullValue())
				.body("[3].tweetId", is("00000000-0000-0000-0000-000000000003"))
				.body("[3].text", is("tweet4")).body("[3].username", is("user"))
				.body("[3].createdAt", notNullValue());
	}

	@Test
	public void tweets() throws Exception {
		given(tweeterMapper.findByUsername("user"))
				.willReturn(Fixtures.tweetsByUsername("user"));

		RestAssured.given(this.documentationSpec).header(HttpHeaders.AUTHORIZATION,
				"Basic " + Base64Utils.encodeToString(("user:password".getBytes())))
				.accept("application/json")
				.filter(document("api/get-tweets",
						preprocessRequest(modifyUris().scheme("https")
								.host("api.example.com").removePort()),
						responseFields(
								fieldWithPath("[].tweetId")
										.description("The ID of this tweet"),
								fieldWithPath("[].text")
										.description("The text of this tweet"),
								fieldWithPath("[].username")
										.description("Owner's username of this tweet"),
								fieldWithPath("[].createdAt")
										.description("The date of this tweet"))))
				.log().all().when().get("/v1/tweets").then().log().all().assertThat()
				.statusCode(is(200)).contentType(ContentType.JSON).body("$", hasSize(3))
				.body("[0].tweetId", is("00000000-0000-0000-0000-000000000000"))
				.body("[0].text", is("tweet1")).body("[0].username", is("user"))
				.body("[0].createdAt", notNullValue())
				.body("[1].tweetId", is("00000000-0000-0000-0000-000000000001"))
				.body("[1].text", is("tweet2")).body("[1].username", is("user"))
				.body("[1].createdAt", notNullValue())
				.body("[2].tweetId", is("00000000-0000-0000-0000-000000000003"))
				.body("[2].text", is("tweet4")).body("[2].username", is("user"))
				.body("[2].createdAt", notNullValue());
	}

	@Test
	public void postTweets() throws Exception {
		given(tweeterMapper.insert(Matchers.any())).willReturn(Fixtures.tweet());

		Map<String, Object> body = new HashMap<>();
		body.put("text", "tweet1");

		RestAssured.given(this.documentationSpec).header(HttpHeaders.AUTHORIZATION,
				"Basic " + Base64Utils.encodeToString(("user:password".getBytes())))
				.contentType(ContentType.JSON).body(objectMapper.writeValueAsString(body))
				.accept("application/json")
				.filter(document("api/post-tweets",
						preprocessRequest(modifyUris()
								.scheme("https").host("api.example.com").removePort()),
						requestFields(fieldWithPath("text")
								.description("The text of this tweet")),
						responseFields(
								fieldWithPath("tweetId")
										.description("The ID of this tweet"),
								fieldWithPath("text")
										.description("The text of this tweet"),
								fieldWithPath("username")
										.description("Owner's username of this tweet"),
								fieldWithPath("createdAt")
										.description("The date of this tweet"))))
				.log().all().when().post("/v1/tweets").then().log().all().assertThat()
				.statusCode(is(201)).contentType(ContentType.JSON)
				.body("tweetId", is("00000000-0000-0000-0000-000000000000"))
				.body("text", is("tweet1")).body("username", is("user"))
				.body("createdAt", notNullValue());
	}

}