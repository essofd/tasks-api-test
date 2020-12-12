package apitest;

import static io.restassured.RestAssured.given;

import org.hamcrest.Matchers;
import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class ApiTest {
	
	@BeforeClass
	public static void setUp() {
		RestAssured.baseURI="http://localhost:8001/tasks-backend";
	}
	
	@Test
	public void deveRetornarTarefas() {
		given()
		.when()
			.get("/todo")
		.then()
			.statusCode(200)
		;
	}

	@Test
	public void deveAdicionarTarefaComSucesso() {
		given()
			.body("{\"task\": \"teste via API\",\"dueDate\": \"2020-12-12\"}")
			.contentType(ContentType.JSON)
		.when()
			.post("/todo")
		.then()
			.statusCode(201)
		;
	}
	
	@Test
	public void deveRemoverTarefaComSucesso() {
		//inserir
		Integer id=given()
			.body("{\"task\": \"teste para remo��o\",\"dueDate\": \"2020-12-20\"}")
			.contentType(ContentType.JSON)
		.when()
			.post("/todo")
		.then()
			.statusCode(201)
			.extract().path("id")
		;
		
		//remover
		given()
		.when()
			.delete("/todo/"+id)
		.then()
			.statusCode(204)
		;
	}
	
	@Test
	public void naoDeveAdicionarTarefaInvalida() {
		given()
			.body("{\"task\": \"teste via API\",\"dueDate\": \"2010-12-12\"}")
			.contentType(ContentType.JSON)
		.when()
			.post("/todo")
		.then()
			.statusCode(400)
			.body("message", Matchers.is("Due date must not be in past"))
		;
	}
	
}
