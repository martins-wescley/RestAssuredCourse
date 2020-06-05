package br.rj.wmartins.rest;

import static io.restassured.RestAssured.*;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public class OlaMundoTest {
	
	@Test
	public void testOlaMundo() {
		Response response = RestAssured.request(Method.GET, "http://restapi.wcaquino.me/ola");
		//System.out.println(response.getBody().asString().equals("Ola Mundo!"));
		Assert.assertTrue(response.getBody().asString().equals("Ola Mundo!"));
		Assert.assertTrue(response.statusCode() == 200);
		Assert.assertEquals(response.statusCode(), 200);
		
		
		ValidatableResponse validacao = response.then();
		validacao.statusCode(response.statusCode());
	}
	
	@Test
	public void devoConhecerOutrasFormarRestAssured() {
		Response response = request(Method.GET, "http://restapi.wcaquino.me/ola");
		ValidatableResponse validacao = response.then();
		validacao.statusCode(response.statusCode());
		
		get("http://restapi.wcaquino.me/ola").then().statusCode(200);
		
		//Fluent Mode
		given()
			//Pré Condições - Token de Validação
		.when()
			//Metodos HTTP
			.get("http://restapi.wcaquino.me/ola")
		.then()
			//Resultado, Assertivas
			.statusCode(200);
	}
	
	@Test
	public void devoConhecerMatcherHamcrest() {
		Assert.assertThat("Maria", Matchers.is("Maria"));
	}
	
	@Test
	public void devoValidarBody() {
		given()
		//Pré Condições - Token de Validação
	.when()
		//Metodos HTTP
		.get("http://restapi.wcaquino.me/ola")
	.then()
		//Resultado, Assertivas
		.statusCode(200)
		.body(Matchers.is("Ola Mundo!"))
		.body(Matchers.containsString("Mundo"));
	}
}
