package am.techmock.trading.engine.core.external.listeners;

import cloud.metaapi.sdk.clients.meta_api.models.MetatraderSymbolPrice;

import java.util.EventListener;
import java.util.List;

public class PriceListener implements EventListener {

	public void priceUpdated(String instanceIndex, List<MetatraderSymbolPrice> prices, Double equity, Double margin, Double freeMargin, Double marginLevel, Double accountCurrencyExchangeRate) {
		System.out.println();
		System.out.println("instanceIndex " + instanceIndex);
		System.out.println("equity " + equity);
		System.out.println("margin " + margin);
		System.out.println("freeMargin " + freeMargin);
		System.out.println("marginLevel " + marginLevel);
		System.out.println("accountCurrencyExchangeRate " + accountCurrencyExchangeRate);

		System.out.println("prices");
		for (MetatraderSymbolPrice price: prices) {
			System.out.println(price.symbol + " " + price.bid + "/" + price.ask);
		}
		System.out.println();
	}
}
