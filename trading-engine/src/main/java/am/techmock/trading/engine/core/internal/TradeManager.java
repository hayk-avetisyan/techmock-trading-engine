package am.techmock.trading.engine.core.internal;

import cloud.metaapi.sdk.clients.meta_api.models.MetatraderTradeResponse;

import java.util.concurrent.CompletableFuture;

public interface TradeManager {

	CompletableFuture<Void> selectAccount(String accountId);

	CompletableFuture<Void> openConnection();

	CompletableFuture<Void> subscribe(String symbol);

	CompletableFuture<Void> unsubscribe(String symbol);

	CompletableFuture<Void> closeConnection();

	CompletableFuture<MetatraderTradeResponse> buy();

	CompletableFuture<MetatraderTradeResponse> sell();
}
