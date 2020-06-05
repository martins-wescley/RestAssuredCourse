package br.rj.wmartins.rest.tests;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import br.rj.wmartins.rest.core.BaseTest;
import br.rj.wmartins.rest.utils.DateUtils;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BarrigaTest extends BaseTest{

	private String TOKEN;
	
	@Before
	public void login() {
		Map<String, String> login = new HashMap<String, String>();
		login.put("email", "wescleymartins02@gmail.com");
		login.put("senha", "digimon02@");
		
		TOKEN = given()
			.body(login)
		.when()
			.post("/signin")
		.then()
		.statusCode(200)
		.extract().path("token");
	}
	
	@Test
	public void n�oDeveAcessarAPISemToken() {
		given()
		.when()
			.get("/contas")
		.then()
		.log().all()
		.statusCode(401);
	}
	
	@Test
	public void T01_deveIncluirContaComSucesso() {
		given()
		.header("Authorization", "JWT " + TOKEN)
			.body("{ \"nome\": \"Conta Teste Token\" }")
		.when()
			.post("/contas")
		.then()
			.statusCode(201);
	}
	
	@Test
	public void deveAlterarContaComSucesso() {
		given()
		.header("Authorization", "JWT " + TOKEN)
			.body("{ \"nome\": \"Conta Alterada\" }")
		.when()
			.put("/contas/173060")
		.then()
			.statusCode(200)
			.body("nome", is("Conta Alterada"));
	}
	
	@Test
	public void naoDeveInserirContaComNomeDuplicado() {
		given()
		.header("Authorization", "JWT " + TOKEN)
			.body("{ \"nome\": \"Conta Alterada\" }")
		.when()
			.post("/contas")
		.then()
			.statusCode(400)
			.body("error", is("J� existe uma conta com esse nome!"));
	}
	
	@Test
	public void deveInserirMovimentacaoSucesso() {
		Movimentacao mov = getMovimentacaoValida();
		given()
		.header("Authorization", "JWT " + TOKEN)
			.body(mov)
		.when()
			.post("/transacoes")
		.then()
			.statusCode(201);
	}
	
	@Test
	public void deveValidarCamposObrigatoriosMovimentacao() {
		given()
		.header("Authorization", "JWT " + TOKEN)
			.body("{}")
		.when()
			.post("/transacoes")
		.then()
			.statusCode(400)
			.body("$", hasSize(8))
			.body("msg", hasItems(
					"Data da Movimenta��o � obrigat�rio", 
					"Data do pagamento � obrigat�rio",
					"Descri��o � obrigat�rio", 
					"Interessado � obrigat�rio", 
					"Valor � obrigat�rio",
					"Valor deve ser um n�mero", 
					"Conta � obrigat�rio", 
					"Situa��o � obrigat�rio"
					));
	}
	
	@Test
	public void naoDeveInserirMovimentacaoDataFutura() {
		/*LocalDate today = LocalDate.now();
		LocalDate amanha = today.plusDays(1);
		
		String formattedDate = amanha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		*/
		Movimentacao mov = getMovimentacaoValida();
		mov.setData_transacao(DateUtils.getDataDiferencaDias(2));
		given()
		.header("Authorization", "JWT " + TOKEN)
			.body(mov)
		.when()
			.post("/transacoes")
		.then()
			.statusCode(400)
			.body("$", hasSize(1))
			.body("msg", hasItem("Data da Movimenta��o deve ser menor ou igual � data atual"));
	}
	
	@Test
	public void naoDeveRemoverContaComMovimentacao() {
		given()
			.header("Authorization", "JWT " + TOKEN)
		.when()
			.delete("/contas/173060")
		.then()
			.statusCode(500)
			.body("constraint", is("transacoes_conta_id_foreign"));
	}
	
	@Test
	public void deveCalcularSaldoContas() {
		given()
			.header("Authorization", "JWT " + TOKEN)
		.when()
			.get("/saldo")
		.then()
			.statusCode(200)
			.body("find{it.conta_id == 173060}.saldo", is("100.00"));
	}
	
	@Test
	public void deveRemoverMovimentacao() {
		given()
			.header("Authorization", "JWT " + TOKEN)
		.when()
			.delete("/transacoes/152319")
		.then()
			.statusCode(204);
	}
	
	private Movimentacao getMovimentacaoValida() {
		Movimentacao mov = new Movimentacao();
		mov.setConta_id(173060);
		//mov.setUsuario_id(usuario_id);
		mov.setDescricao("Descri��o da Movimenta��o");
		mov.setEnvolvido("Envolvido na mov");
		mov.setTipo("REC");
		mov.setData_transacao(DateUtils.getDataDiferencaDias(-1));
		mov.setData_pagamento(DateUtils.getDataDiferencaDias(5));
		mov.setValor(100f);
		mov.setStatus(true);
		
		return mov;
	}
}
