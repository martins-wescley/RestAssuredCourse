package br.rj.wmartins.rest;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public class OlaMundo {
	
	public static void main(String[] args) {
		//Fazendo uma requisição na URL http://restapi.wcaquino.me/ola
		//Response = resposta da requisição
		Response response = RestAssured.request(Method.GET, "http://restapi.wcaquino.me/ola");
		System.out.println(response.getBody().asString().equals("Ola Mundo!"));
		System.out.println(response.statusCode() == 200);
		
		
		ValidatableResponse validacao = response.then();
		validacao.statusCode(response.statusCode());
	}

}
