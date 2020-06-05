package br.rj.wmartins.rest;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import io.restassured.http.ContentType;

public class VerbosTest {
	
	@Test
	public void deveSalvarUsuario() {
		given()
			.log().all()
			//Informar de que tipo � o conte�do que estou enviando
			.contentType("application/json")
			//Esses objetos ser�o enviados no Body
			.body("{\"name\": \"Jose\",\"age\": 50}")
		.when()
			//Fazendo uma requisi��o do tipo post para esse recurso
			.post("http://restapi.wcaquino.me/users")
		.then()
		.log().all()
		.statusCode(201)
		.body("id", is(notNullValue()))
		.body("name", is("Jose"))
		.body("age", is(50));
	}
	
	@Test
	public void naoDeveSalvarSemNome() {
		given()
		.log().all()
		//Informar de que tipo � o conte�do que estou enviando
		.contentType("application/json")
		//Esses objetos ser�o enviados no Body
		.body("{\"age\": 50}")
	.when()
		//Fazendo uma requisi��o do tipo post para esse recurso
		.post("http://restapi.wcaquino.me/users")
	.then()
	.log().all()
	.statusCode(400)
	.body("id", is(nullValue()))
	.body("error", is("Name � um atributo obrigat�rio"));
	}
	
	@Test
	public void deveSalvarUsuarioViaXML() {
		given()
			.log().all()
			//Informar de que tipo � o conte�do que estou enviando
			.contentType(ContentType.XML)
			//Esses objetos ser�o enviados no Body
			.body("<user> <name>Jose</name> <age>50</age> </user>")
		.when()
			//Fazendo uma requisi��o do tipo post para esse recurso
			.post("http://restapi.wcaquino.me/usersXML")
		.then()
		.log().all()
		.statusCode(201)
		.rootPath("user")
		.body("id", is(notNullValue()))
		.body("name", is("Jose"))
		.body("age", is("50"));
	}
	
	@Test
	public void deveSalvarUsuarioViaXMLUsandoObjeto() {
		User user = new User("Usuario XML", 40);
		given()
			.log().all()
			//Informar de que tipo � o conte�do que estou enviando
			.contentType(ContentType.XML)
			//Esses objetos ser�o enviados no Body
			.body(user)
		.when()
			//Fazendo uma requisi��o do tipo post para esse recurso
			.post("http://restapi.wcaquino.me/usersXML")
		.then()
		.log().all()
		.statusCode(201)
		.rootPath("user")
		.body("id", is(notNullValue()))
		.body("name", is("Usuario XML"))
		.body("age", is("40"));
	}
	
	@Test
	public void deveDesserializarXMLA0SalvarUsuario() {
		User user = new User("Usuario XML", 40);
		
		User usuarioInserido = given()
			.log().all()
			//Informar de que tipo � o conte�do que estou enviando
			.contentType(ContentType.XML)
			//Esses objetos ser�o enviados no Body
			.body(user)
		.when()
			//Fazendo uma requisi��o do tipo post para esse recurso
			.post("http://restapi.wcaquino.me/usersXML")
		.then()
		.log().all()
		.statusCode(201)
		.extract().body().as(User.class);
		
		Assert.assertThat(usuarioInserido.getId(), notNullValue());
		Assert.assertThat(usuarioInserido.getName(), is("Usuario XML"));
		Assert.assertThat(usuarioInserido.getAge(), is(40));
		Assert.assertThat(usuarioInserido.getSalary(), nullValue());
	}
	@Test
	public void deveAlterarUsuario() {
		given()
			.log().all()
			//Informar de que tipo � o conte�do que estou enviando
			.contentType("application/json")
			//Esses objetos ser�o enviados no Body
			.body("{\"name\": \"Usuario Alterado\",\"age\": 70}")
		.when()
			//Fazendo uma requisi��o do tipo post para esse recurso
			.put("http://restapi.wcaquino.me/users/1")
		.then()
		.log().all()
		.statusCode(200)
		.body("id", is(1))
		.body("name", is("Usuario Alterado"))
		.body("age", is(70))
		.body("salary", is(1234.5678f));
	}
	
	@Test
	public void deveCustomizarURL() {
		given()
			.log().all()
			//Informar de que tipo � o conte�do que estou enviando
			.contentType("application/json")
			//Esses objetos ser�o enviados no Body
			.body("{\"name\": \"Usuario Alterado\",\"age\": 70}")
		.when()
			//Fazendo uma requisi��o do tipo post para esse recurso
			.put("http://restapi.wcaquino.me/{entidade}/{userId}", "users", "1")
		.then()
		.log().all()
		.statusCode(200)
		.body("id", is(1))
		.body("name", is("Usuario Alterado"))
		.body("age", is(70))
		.body("salary", is(1234.5678f));
	}
	
	@Test
	public void deveCustomizarURLParte2() {
		given()
			.log().all()
			//Informar de que tipo � o conte�do que estou enviando
			.contentType("application/json")
			//Esses objetos ser�o enviados no Body
			.body("{\"name\": \"Usuario Alterado\",\"age\": 70}")
			.pathParam("entidade", "users")
			.pathParam("userId", "1")
		.when()
			//Fazendo uma requisi��o do tipo post para esse recurso
			.put("http://restapi.wcaquino.me/{entidade}/{userId}")
		.then()
		.log().all()
		.statusCode(200)
		.body("id", is(1))
		.body("name", is("Usuario Alterado"))
		.body("age", is(70))
		.body("salary", is(1234.5678f));
	}
	
	@Test
	public void devoRemoverUsuario() {
		given()
			.log().all()
		.when()
			.delete("http://restapi.wcaquino.me/users/1")
		.then()
			.statusCode(204);
	}
	
	@Test
	public void naoDevoRemoverUsuarioInexistente() {
		given()
			.log().all()
		.when()
			.delete("http://restapi.wcaquino.me/users/1000")
		.then()
			.log().all()
			.statusCode(400)
			.body("error", is("Registro inexistente"));
	}
	
	@Test
	public void deveSalvarUsuarioUsandoMap() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", "Usuario via map");
		params.put("age", 20);
		given()
			.log().all()
			//Informar de que tipo � o conte�do que estou enviando
			.contentType("application/json")
			//Esses objetos ser�o enviados no Body
			.body(params)
		.when()
			//Fazendo uma requisi��o do tipo post para esse recurso
			.post("http://restapi.wcaquino.me/users")
		.then()
		.log().all()
		.statusCode(201)
		.body("id", is(notNullValue()))
		.body("name", is("Usuario via map"))
		.body("age", is(20));
	}
	
	@Test
	public void deveSalvarUsuarioUsandoObjeto() {
		User user = new User("Usuario via objeto", 35);
		
		given()
			.log().all()
			//Informar de que tipo � o conte�do que estou enviando
			.contentType("application/json")
			//Esses objetos ser�o enviados no Body
			.body(user)
		.when()
			//Fazendo uma requisi��o do tipo post para esse recurso
			.post("http://restapi.wcaquino.me/users")
		.then()
		.log().all()
		.statusCode(201)
		.body("id", is(notNullValue()))
		.body("name", is("Usuario via objeto"))
		.body("age", is(35));
	}
	
	@Test
	public void deveDeserializarObjetoAoSalvarUsuario() {
		User user = new User("Usuario deserializado", 35);
		
		User usuarioInserido = given()
			.log().all()
			//Informar de que tipo � o conte�do que estou enviando
			.contentType("application/json")
			//Esses objetos ser�o enviados no Body
			.body(user)
		.when()
			//Fazendo uma requisi��o do tipo post para esse recurso
			.post("http://restapi.wcaquino.me/users")
		.then()
		.log().all()
		.statusCode(201)
		.extract().body().as(User.class);
		
		System.out.println(usuarioInserido);
		Assert.assertThat(usuarioInserido.getId(), notNullValue());
		Assert.assertEquals("Usuario deserializado", usuarioInserido.getName());
		Assert.assertThat(usuarioInserido.getAge(), is(35));
	}
}