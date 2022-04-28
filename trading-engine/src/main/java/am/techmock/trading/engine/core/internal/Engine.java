package am.techmock.trading.engine.core.internal;

public class Engine {

	private TradeManager tradeManager;

	public Engine(TradeManager tradeManager) {
		this.tradeManager = tradeManager;
	}

	public void openConnection(String accountId) {
		tradeManager.selectAccount(accountId).thenApply((account) -> tradeManager.openConnection()).join().join();
	}

	public void closeConnection() {
		this.tradeManager.closeConnection().join();
	}

	public void switchAccount(String accountId) {
		this.tradeManager.closeConnection().thenApply((completed) -> this.tradeManager.selectAccount(accountId)).join().join();
	}

	public void subscribe(String symbol) {
		this.tradeManager.subscribe(symbol).join();
	}

	public void buy() {
		this.tradeManager.buy().join();
	}

	public void sell() {
		this.tradeManager.sell().join();
	}
}
