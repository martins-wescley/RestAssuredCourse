package br.rj.wmartins.rest.tests.refact;

import static io.restassured.RestAssured.*;

import org.junit.Test;

import br.rj.wmartins.rest.core.BaseTest;
import io.restassured.RestAssured;
import io.restassured.specification.FilterableRequestSpecification;

public class AuthTest extends BaseTest{

	@Test
	public void nãoDeveAcessarAPISemToken() {
		FilterableRequestSpecification req = (FilterableRequestSpecification) RestAssured.requestSpecification;
		req.removeHeader("Authorization");
		
		given()
		.when()
			.get("/contas")
		.then()
			.statusCode(401);
	}
}
