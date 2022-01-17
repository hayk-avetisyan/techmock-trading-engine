package am.tochmock.exchange.engine.controllers;


import am.tochmock.exchange.engine.internal.Engine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class IndexController {

	private Engine engine;

	@Autowired
	public IndexController(Engine engine) {
		this.engine = engine;
	}

	@GetMapping("/")
	public void index() throws Exception {

		engine.start();
	}

	@GetMapping("/status")
	public void status() throws Exception {
		engine.getGateway().requestAccounts();
	}
}
