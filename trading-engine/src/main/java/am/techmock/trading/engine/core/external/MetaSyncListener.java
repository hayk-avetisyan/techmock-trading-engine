package am.techmock.trading.engine.core.external;

import am.techmock.trading.engine.core.external.listeners.PriceListener;
import cloud.metaapi.sdk.clients.meta_api.SynchronizationListener;
import cloud.metaapi.sdk.clients.meta_api.models.MetatraderSymbolPrice;
import io.reactivex.rxjava3.subjects.AsyncSubject;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MetaSyncListener extends SynchronizationListener {

	private PriceListener priceListener;
	private AsyncSubject<MetatraderSymbolPrice> asyncSubject;



	public MetaSyncListener(PriceListener priceListener) {
		this.priceListener = priceListener;
	}

	@Override
	public CompletableFuture<Void> onSymbolPricesUpdated(String instanceIndex, List<MetatraderSymbolPrice> prices, Double equity, Double margin, Double freeMargin, Double marginLevel, Double accountCurrencyExchangeRate) {
		return CompletableFuture.runAsync(() -> priceListener.priceUpdated(
				instanceIndex,
				prices,
				equity,
				margin,
				freeMargin,
				marginLevel,
				accountCurrencyExchangeRate
		));
	}


}
