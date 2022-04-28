package am.techmock.trading.engine.server.controllers;


import am.techmock.trading.engine.core.internal.Engine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class AccountController {

	private Engine engine;

	@Autowired
	public AccountController(Engine engine) {
		this.engine = engine;
	}

	@GetMapping("account/connection/open/{accountId}")
	public void startTrading(@PathVariable("accountId") String accountId) {
		engine.openConnection(accountId);
	}

	@GetMapping("account/connection/close")
	public void stopTrading() {
		engine.closeConnection();
	}
}
