package am.tochmock.exchange.engine.internal;

import am.tochmock.exchange.engine.external.MetaManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Engine {

	private MetaManager metaManager;

	@Autowired
	public Engine(MetaManager metaManager) {
		this.metaManager = metaManager;
	}

	public void start(String symbol) throws Exception {
		metaManager.openConnection(symbol);
	}

}
