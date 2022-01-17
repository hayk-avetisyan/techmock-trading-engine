package am.tochmock.exchange.engine.config;


import am.tochmock.exchange.engine.external.MetaManager;
import cloud.metaapi.sdk.clients.error_handler.ValidationException;
import cloud.metaapi.sdk.meta_api.MetaApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class MetaTraderConfig {

	@Value("${meta.api.token}")
	private String token;

	@Bean
	MetaApi.Options metaApiOptions() {
		return new MetaApi.Options();
	}

	@Bean
	@Autowired
	MetaManager metaManager(MetaApi.Options options) throws IOException, ValidationException {
		MetaApi meta = new MetaApi(token, options);
		return new MetaManager(meta);
	}

}
