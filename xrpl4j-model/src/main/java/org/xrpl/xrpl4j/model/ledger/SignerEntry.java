package org.xrpl.xrpl4j.model.ledger;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.primitives.UnsignedInteger;
import org.immutables.value.Value;
import org.xrpl.xrpl4j.model.transactions.Address;

/**
 * Represents an individual signer in a {@link SignerList}.
 */
@Value.Immutable
@JsonSerialize(as = ImmutableSignerEntry.class)
@JsonDeserialize(as = ImmutableSignerEntry.class)
public interface SignerEntry {

  static ImmutableSignerEntry.Builder builder() {
    return ImmutableSignerEntry.builder();
  }

  /**
   * An XRP Ledger classic {@link Address} whose signature contributes to the multi-signature. It does not need to be a
   * funded address in the ledger.
   */
  @JsonProperty("Account")
  Address account();

  /**
   * The weight of a signature from this signer. A multi-signature is only valid if the sum weight of the
   * signatures provided meets or exceeds the {@link SignerList#signerQuorum()} value.
   */
  @JsonProperty("SignerWeight")
  UnsignedInteger signerWeight();

}
