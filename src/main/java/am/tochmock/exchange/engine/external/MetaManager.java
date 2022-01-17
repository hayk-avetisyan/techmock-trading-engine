package am.tochmock.exchange.engine.external;


import cloud.metaapi.sdk.meta_api.MetaApi;
import cloud.metaapi.sdk.meta_api.MetaApiConnection;
import cloud.metaapi.sdk.meta_api.MetatraderAccount;
import org.nd4j.common.base.Preconditions;

public class MetaManager {

	private MetaApi meta;

	private MetatraderAccount account;
	private MetaApiConnection connection;
	private boolean closed;

	public MetaManager(MetaApi meta) {
		this.meta = meta;
	}

	public void selectAccount(String accountId) {
		if(this.connection != null) this.closeConnection();
		this.account = this.meta.getMetatraderAccountApi().getAccount(accountId).join();
	}

	public void openConnection(String symbol) {
		Preconditions.checkNotNull(this.account, "Cannot connect: no account specified");
		this.connection = this.account.connect().join();
		this.connection.waitSynchronized().join();
		this.connection.subscribeToMarketData(symbol).join();
		this.closed = false;
	}

	public void closeConnection() {
		if (!this.closed) {
			this.connection.close();
			this.closed = true;
		}
	}
}
