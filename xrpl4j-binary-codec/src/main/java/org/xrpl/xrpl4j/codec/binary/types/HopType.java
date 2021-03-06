package org.xrpl.xrpl4j.codec.binary.types;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.xrpl.xrpl4j.codec.addresses.UnsignedByte;
import org.xrpl.xrpl4j.codec.addresses.UnsignedByteArray;
import org.xrpl.xrpl4j.codec.binary.BinaryCodecObjectMapperFactory;
import org.xrpl.xrpl4j.codec.binary.serdes.BinaryParser;

import java.util.OptionalInt;

/**
 * Codec for XRPL Hop object inside a Path object.
 */
public class HopType extends SerializedType<HopType> {

  /**
   * Constant for masking types of a Hop
   */
  public static final byte TYPE_ACCOUNT = 0x01;
  public static final byte TYPE_CURRENCY = 0x10;
  public static final byte TYPE_ISSUER = 0x20;

  private ObjectMapper objectMapper = BinaryCodecObjectMapperFactory.getObjectMapper();

  public HopType() {
    this(UnsignedByteArray.empty());
  }

  public HopType(UnsignedByteArray list) {
    super(list);
  }

  @Override
  public HopType fromParser(BinaryParser parser, OptionalInt lengthHint) {
    int type = parser.readUInt8().intValue();
    UnsignedByteArray byteArray = UnsignedByteArray.of(UnsignedByte.of(type));

    if ((type & TYPE_ACCOUNT) > 0) {
      byteArray.append(parser.read(AccountIdType.WIDTH));
    }

    if ((type & TYPE_CURRENCY) > 0) {
      byteArray.append(parser.read(CurrencyType.WIDTH));
    }

    if ((type & TYPE_ISSUER) > 0) {
      byteArray.append(parser.read(AccountIdType.WIDTH));
    }

    return new HopType(byteArray);
  }

  @Override
  public HopType fromJSON(JsonNode node) throws JsonProcessingException {
    if (!node.isObject()) {
      throw new IllegalArgumentException("node is not an object");
    }

    UnsignedByteArray byteArray = UnsignedByteArray.ofSize(1);

    Hop hop = objectMapper.treeToValue(node, Hop.class);

    hop.account().ifPresent(account -> {
      byteArray.append(new AccountIdType().fromJSON(account).value());
      byteArray.set(0, byteArray.get(0).or(UnsignedByte.of(TYPE_ACCOUNT)));
    });

    hop.currency().ifPresent(currency -> {
      byteArray.append(new CurrencyType().fromJSON(currency).value());
      byteArray.set(0, byteArray.get(0).or(UnsignedByte.of(TYPE_CURRENCY)));
    });

    hop.issuer().ifPresent(issuer -> {
      byteArray.append(new AccountIdType().fromJSON(issuer).value());
      byteArray.set(0, byteArray.get(0).or(UnsignedByte.of(TYPE_ISSUER)));
    });

    return new HopType(byteArray);
  }

  @Override
  public JsonNode toJSON() {
    BinaryParser parser = new BinaryParser(this.toHex());
    int type = parser.readUInt8().intValue();

    ImmutableHop.Builder builder = Hop.builder();

    if ((type & TYPE_ACCOUNT) > 0) {
      builder.account(new AccountIdType().fromParser(parser).toJSON());
    }

    if ((type & TYPE_CURRENCY) > 0) {
      builder.currency(new CurrencyType().fromParser(parser).toJSON());
    }

    if ((type & TYPE_ISSUER) > 0) {
      builder.account(new AccountIdType().fromParser(parser).toJSON());
    }

    return objectMapper.valueToTree(builder.build());
  }
}
