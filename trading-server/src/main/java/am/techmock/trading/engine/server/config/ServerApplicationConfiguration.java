package am.techmock.trading.engine.server.config;


import cloud.metaapi.sdk.meta_api.MetaApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import java.io.IOException;

@Configuration
@ImportResource("classpath:configuration.xml")
public class ServerApplicationConfiguration {

	@Bean
	public MetaApi metaApi(@Value("${meta.api.token}") String token) throws IOException {
		return new MetaApi(token);
	}


}
