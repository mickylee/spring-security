/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.security.oauth2.core.endpoint;

import org.junit.Test;
import org.springframework.security.oauth2.core.OAuth2AccessToken;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link OAuth2AccessTokenResponse}.
 *
 * @author Luander Ribeiro
 * @author Joe Grandja
 */
public class OAuth2AccessTokenResponseTests {
	private static final String TOKEN_VALUE = "access-token";
	private static final long EXPIRES_IN = Instant.now().plusSeconds(5).toEpochMilli();

	@Test(expected = IllegalArgumentException.class)
	public void buildWhenTokenValueIsNullThenThrowIllegalArgumentException() {
		OAuth2AccessTokenResponse.withToken(null)
			.tokenType(OAuth2AccessToken.TokenType.BEARER)
			.expiresIn(EXPIRES_IN)
			.build();
	}

	@Test(expected = IllegalArgumentException.class)
	public void buildWhenTokenTypeIsNullThenThrowIllegalArgumentException() {
		OAuth2AccessTokenResponse.withToken(TOKEN_VALUE)
			.tokenType(null)
			.expiresIn(EXPIRES_IN)
			.build();
	}

	@Test(expected = IllegalArgumentException.class)
	public void buildWhenExpiresInIsNegativeThenThrowIllegalArgumentException() {
		OAuth2AccessTokenResponse.withToken(TOKEN_VALUE)
			.tokenType(OAuth2AccessToken.TokenType.BEARER)
			.expiresIn(-1L)
			.build();
	}

	@Test
	public void buildWhenAllAttributesProvidedThenAllAttributesAreSet() {
		Instant expiresAt = Instant.now().plusSeconds(5);
		Set<String> scopes = new LinkedHashSet<>(Arrays.asList("scope1", "scope2"));
		Map<String, Object> additionalParameters = new HashMap<>();
		additionalParameters.put("param1", "value1");
		additionalParameters.put("param2", "value2");

		OAuth2AccessTokenResponse tokenResponse = OAuth2AccessTokenResponse
			.withToken(TOKEN_VALUE)
			.tokenType(OAuth2AccessToken.TokenType.BEARER)
			.expiresIn(expiresAt.toEpochMilli())
			.scopes(scopes)
			.additionalParameters(additionalParameters)
			.build();

		assertThat(tokenResponse.getAccessToken()).isNotNull();
		assertThat(tokenResponse.getAccessToken().getTokenValue()).isEqualTo(TOKEN_VALUE);
		assertThat(tokenResponse.getAccessToken().getTokenType()).isEqualTo(OAuth2AccessToken.TokenType.BEARER);
		assertThat(tokenResponse.getAccessToken().getIssuedAt()).isNotNull();
		assertThat(tokenResponse.getAccessToken().getExpiresAt()).isAfterOrEqualTo(expiresAt);
		assertThat(tokenResponse.getAccessToken().getScopes()).isEqualTo(scopes);
		assertThat(tokenResponse.getAdditionalParameters()).isEqualTo(additionalParameters);
	}
}
