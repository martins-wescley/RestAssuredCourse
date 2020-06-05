package br.rj.wmartins.rest.core.refact.suite;

import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import br.rj.wmartins.rest.core.BaseTest;
import br.rj.wmartins.rest.tests.refact.AuthTest;
import br.rj.wmartins.rest.tests.refact.ContasTest;
import br.rj.wmartins.rest.tests.refact.MovimentacaoTest;
import br.rj.wmartins.rest.tests.refact.SaldoTest;
import io.restassured.RestAssured;

@RunWith(org.junit.runners.Suite.class)
@SuiteClasses({
	ContasTest.class,
	MovimentacaoTest.class,
	SaldoTest.class,
	AuthTest.class
})
public class Suite extends BaseTest {

	@BeforeClass
	public static void login() {
		Map<String, String> login = new HashMap<String, String>();
		login.put("email", "wescleymartins02@gmail.com");
		login.put("senha", "digimon02@");
		
		String TOKEN = given()
			.body(login)
		.when()
			.post("/signin")
		.then()
			.statusCode(200)
			.extract().path("token");
		
		RestAssured.requestSpecification.header("Authorization", "JWT " + TOKEN);
		
		RestAssured.get("/reset").then().statusCode(200);
	}
}
