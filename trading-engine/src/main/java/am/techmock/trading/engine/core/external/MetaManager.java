package am.techmock.trading.engine.core.external;


import am.techmock.trading.engine.core.internal.TradeManager;
import cloud.metaapi.sdk.clients.meta_api.SynchronizationListener;
import cloud.metaapi.sdk.clients.meta_api.models.MetatraderTradeResponse;
import cloud.metaapi.sdk.meta_api.MetaApi;
import cloud.metaapi.sdk.meta_api.MetaApiConnection;
import cloud.metaapi.sdk.meta_api.MetatraderAccount;

import java.util.concurrent.CompletableFuture;

public class MetaManager implements TradeManager {

	private MetaApi meta;

	private boolean closed;
	private String current;
	private MetatraderAccount account;
	private MetaApiConnection connection;
	private SynchronizationListener listener;

	public MetaManager(MetaApi meta, SynchronizationListener listener) {
		this.listener = listener;
		this.meta = meta;
	}

	public CompletableFuture<Void> selectAccount(String accountId) {
		if(this.connection != null) this.closeConnection();
		return this.meta.getMetatraderAccountApi().getAccount(accountId).thenAccept(account -> this.account = account);
	}

	public CompletableFuture<Void> openConnection() {
		return this.account.connect().thenAccept((connection) -> {
			this.connection = connection;
			this.connection.addSynchronizationListener(listener);
			this.connection.waitSynchronized();
			this.closed = false;
		});
	}

	public CompletableFuture<Void> subscribe(String symbol) {
		symbol = symbol.toUpperCase();

		if(current != null && !current.equals(symbol)) {
			this.unsubscribe(current).join();
		}

		current = symbol;
		return this.connection.subscribeToMarketData(symbol);
	}

	public CompletableFuture<Void> unsubscribe(String symbol) {
		return this.connection.unsubscribeFromMarketData(symbol);
	}

	public CompletableFuture<Void> closeConnection() {
		 return CompletableFuture.runAsync(() -> {
			if (!this.closed) {
				this.closed = true;
				this.connection.close().join();
			}
		});
	}

	public CompletableFuture<MetatraderTradeResponse> buy() {
		return this.connection.createMarketBuyOrder(current, 1, null, (Double) null, null);
	}

	public CompletableFuture<MetatraderTradeResponse> sell() {
		return this.connection.createMarketSellOrder(current, 1, null, (Double) null, null);
	}
}
