package com.otto.gestao_vagas.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;

public class TestUtils {

	public static String objectToJson(Object object) {
		try {
			final ObjectMapper objectMapper = new ObjectMapper();
			return objectMapper.writeValueAsString(object);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String generateToken(UUID idCompany) {
		Algorithm algorithm = Algorithm.HMAC256("JAVAGAS_@123#");

		var experesIn = Instant.now().plus(Duration.ofHours(2));

		var token = JWT.create().withIssuer("javagas")
				.withExpiresAt(experesIn)
				.withClaim("roles", Arrays.asList("COMPANY"))
				.withSubject(idCompany.toString()).sign(algorithm);

		return token;
	}
}
