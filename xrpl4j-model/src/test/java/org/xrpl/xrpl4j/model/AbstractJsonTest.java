package org.xrpl.xrpl4j.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.junit.Before;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.xrpl.xrpl4j.model.client.rippled.XrplResult;
import org.xrpl.xrpl4j.model.jackson.ObjectMapperFactory;
import org.xrpl.xrpl4j.model.ledger.LedgerObject;
import org.xrpl.xrpl4j.model.transactions.Transaction;

public class AbstractJsonTest {

  protected ObjectMapper objectMapper;

  @Before
  public void setUp() {
    objectMapper = ObjectMapperFactory.create();
  }

  protected void assertCanSerializeAndDeserialize(
      Transaction transaction,
      String json
  ) throws JsonProcessingException, JSONException {
    String serialized = objectMapper.writeValueAsString(transaction);
    JSONAssert.assertEquals(json, serialized, JSONCompareMode.STRICT);

    Transaction deserialized = objectMapper.readValue(serialized, Transaction.class);
    assertThat(deserialized).isEqualTo(transaction);
  }

  protected void assertCanSerializeAndDeserialize(
      XrplResult result,
      String json
  ) throws JsonProcessingException, JSONException {
    String serialized = objectMapper.writeValueAsString(result);
    JSONAssert.assertEquals(json, serialized, JSONCompareMode.STRICT);

    XrplResult deserialized = objectMapper.readValue(serialized, result.getClass());
    assertThat(deserialized).isEqualTo(result);
  }
}
