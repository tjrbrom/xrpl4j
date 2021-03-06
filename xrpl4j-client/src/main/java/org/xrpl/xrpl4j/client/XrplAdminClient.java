package org.xrpl.xrpl4j.client;

import okhttp3.HttpUrl;
import org.xrpl.xrpl4j.model.client.admin.AcceptLedgerResult;

/**
 * A client that can call Rippled Admin API methods. @see "https://xrpl.org/admin-rippled-methods.html".
 */
public class XrplAdminClient {

  private final JsonRpcClient jsonRpcClient;

  /**
   * Public constructor.
   *
   * @param rippledUrl The {@link HttpUrl} of the rippled node to connect to.
   */
  public XrplAdminClient(HttpUrl rippledUrl) {
    this.jsonRpcClient = JsonRpcClient.construct(rippledUrl);
  }

  /**
   * Advances the ledger. When running rippled in standalone mode, this method is useful to manually trigger
   * the ledger to close.
   *
   * @see "https://xrpl.org/ledger_accept.html"
   * @return A {@link AcceptLedgerResult} containing information about the accepted ledger.
   * @throws JsonRpcClientErrorException If {@code jsonRpcClient} throws an error.
   */
  public AcceptLedgerResult acceptLedger() throws JsonRpcClientErrorException {
    JsonRpcRequest request = JsonRpcRequest.builder()
      .method("ledger_accept")
      .build();

    return jsonRpcClient.send(request, AcceptLedgerResult.class);
  }

}
