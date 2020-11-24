package com.ripple.xrpl4j.tests;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.BaseEncoding;
import com.google.common.primitives.UnsignedInteger;
import com.google.common.primitives.UnsignedLong;
import com.ripple.cryptoconditions.CryptoConditionReader;
import com.ripple.cryptoconditions.der.DerEncodingException;
import com.ripple.xrpl4j.codec.binary.XrplBinaryCodec;
import com.ripple.xrpl4j.model.jackson.ObjectMapperFactory;
import com.ripple.xrpl4j.model.transactions.AccountDelete;
import com.ripple.xrpl4j.model.transactions.AccountSet;
import com.ripple.xrpl4j.model.transactions.Address;
import com.ripple.xrpl4j.model.transactions.CheckCancel;
import com.ripple.xrpl4j.model.transactions.CheckCash;
import com.ripple.xrpl4j.model.transactions.CheckCreate;
import com.ripple.xrpl4j.model.transactions.CurrencyAmount;
import com.ripple.xrpl4j.model.transactions.DepositPreAuth;
import com.ripple.xrpl4j.model.transactions.EscrowCancel;
import com.ripple.xrpl4j.model.transactions.EscrowCreate;
import com.ripple.xrpl4j.model.transactions.EscrowFinish;
import com.ripple.xrpl4j.model.transactions.Flags;
import com.ripple.xrpl4j.model.transactions.Flags.PaymentFlags;
import com.ripple.xrpl4j.model.transactions.Hash256;
import com.ripple.xrpl4j.model.transactions.IssuedCurrencyAmount;
import com.ripple.xrpl4j.model.transactions.OfferCancel;
import com.ripple.xrpl4j.model.transactions.OfferCreate;
import com.ripple.xrpl4j.model.transactions.Payment;
import com.ripple.xrpl4j.model.transactions.PaymentChannelClaim;
import com.ripple.xrpl4j.model.transactions.PaymentChannelCreate;
import com.ripple.xrpl4j.model.transactions.PaymentChannelFund;
import com.ripple.xrpl4j.model.transactions.Transaction;
import com.ripple.xrpl4j.model.transactions.TrustSet;
import com.ripple.xrpl4j.model.transactions.XrpCurrencyAmount;
import org.junit.jupiter.api.Test;

public class BinarySerializationTests {

  ObjectMapper objectMapper = ObjectMapperFactory.create();
  XrplBinaryCodec binaryCodec = new XrplBinaryCodec();

  @Test
  public void serializeAccountSetTransaction() throws JsonProcessingException {
    AccountSet accountSet = AccountSet.builder()
      .account(Address.of("rpP2GdsQwenNnFPefbXFgiTvEgJWQpq8Rw"))
      .fee(XrpCurrencyAmount.of("10"))
      .sequence(UnsignedInteger.valueOf(10598))
      .build();

    String expectedBinary = "1200032280000000240000296668400000000000000A81140F3D0C7D2CFAB2EC8295451F0B3CA038E8E9CDCD";
    assertSerializesAndDeserializes(accountSet, expectedBinary);
  }

  @Test
  public void serializeAccountDelete() throws JsonProcessingException {
    AccountDelete accountDelete = AccountDelete.builder()
      .account(Address.of("rWYkbWkCeg8dP6rXALnjgZSjjLyih5NXm"))
      .fee(XrpCurrencyAmount.of("5000000"))
      .sequence(UnsignedInteger.valueOf(2470665))
      .destination(Address.of("rPT1Sjq2YGrBMTttX4GZHjKu9dyfzbpAYe"))
      .destinationTag(UnsignedInteger.valueOf(13))
      .build();

    String expectedBinary = "1200152280000000240025B3092E0000000D6840000000004C4B4081140596915CFDEEE3A695B3EFD6BDA9AC78"
      + "8A368B7B8314F667B0CA50CC7709A220B0561B85E53A48461FA8";
    assertSerializesAndDeserializes(accountDelete, expectedBinary);
  }

  @Test
  public void serializeCheckCancel() throws JsonProcessingException {
    CheckCancel checkCancel = CheckCancel.builder()
      .account(Address.of("rUn84CUYbNjRoTQ6mSW7BVJPSVJNLb1QLo"))
      .checkId(Hash256.of("49647F0D748DC3FE26BDACBC57F251AADEFFF391403EC9BF87C97F67E9977FB0"))
      .sequence(UnsignedInteger.valueOf(12))
      .fee(XrpCurrencyAmount.of("12"))
      .build();

    String expectedBinary = "1200122280000000240000000C501849647F0D748DC3FE26BDACBC57F251AADEFFF391403EC9BF87C97F67E997"
      + "7FB068400000000000000C81147990EC5D1D8DF69E070A968D4B186986FDF06ED0";
    assertSerializesAndDeserializes(checkCancel, expectedBinary);
  }

  @Test
  public void serializeCheckCashWithXrpAmount() throws JsonProcessingException {
    CheckCash checkCash = CheckCash.builder()
      .account(Address.of("rfkE1aSy9G8Upk4JssnwBxhEv5p4mn2KTy"))
      .checkId(Hash256.of("838766BA2B995C00744175F69A1B11E32C3DBC40E64801A4056FCBD657F57334"))
      .sequence(UnsignedInteger.ONE)
      .fee(XrpCurrencyAmount.of("12"))
      .amount(XrpCurrencyAmount.of("100"))
      .build();

    String expectedBinary = "120011228000000024000000015018838766BA2B995C00744175F69A1B11E32C3DBC40E64801A4056FCBD657F5"
      + "733461400000000000006468400000000000000C811449FF0C73CA6AF9733DA805F76CA2C37776B7C46B";
    assertSerializesAndDeserializes(checkCash, expectedBinary);
  }

  @Test
  public void serializeCheckCashWithXrpDeliverMin() throws JsonProcessingException {
    CheckCash checkCash = CheckCash.builder()
      .account(Address.of("rfkE1aSy9G8Upk4JssnwBxhEv5p4mn2KTy"))
      .checkId(Hash256.of("838766BA2B995C00744175F69A1B11E32C3DBC40E64801A4056FCBD657F57334"))
      .sequence(UnsignedInteger.ONE)
      .fee(XrpCurrencyAmount.of("12"))
      .deliverMin(XrpCurrencyAmount.of("100"))
      .build();

    String expectedBinary = "120011228000000024000000015018838766BA2B995C00744175F69A1B11E32C3DBC40E64801A4056FCBD657F5"
      + "733468400000000000000C6A4000000000000064811449FF0C73CA6AF9733DA805F76CA2C37776B7C46B";
    assertSerializesAndDeserializes(checkCash, expectedBinary);
  }

  @Test
  public void serializeCheckCashWithIssuedCurrencyDeliverMin() throws JsonProcessingException {
    CheckCash checkCash = CheckCash.builder()
      .account(Address.of("rfkE1aSy9G8Upk4JssnwBxhEv5p4mn2KTy"))
      .checkId(Hash256.of("838766BA2B995C00744175F69A1B11E32C3DBC40E64801A4056FCBD657F57334"))
      .sequence(UnsignedInteger.ONE)
      .fee(XrpCurrencyAmount.of("12"))
      .deliverMin(IssuedCurrencyAmount.builder()
        .currency("USD")
        .issuer(Address.of("rfkE1aSy9G8Upk4JssnwBxhEv5p4mn2KTy"))
        .value("100")
        .build())
      .build();

    String expectedBinary = "120011228000000024000000015018838766BA2B995C00744175F69A1B11E32C3DBC40E64801A4056FCBD657F5"
      + "733468400000000000000C6AD5038D7EA4C68000000000000000000000000000555344000000000049FF0C73CA6AF9733DA805F76CA2C37776B7C46B811449FF0C73CA6AF9733DA805F76CA2C37776B7C46B";
    assertSerializesAndDeserializes(checkCash, expectedBinary);
  }

  @Test
  void serializeCheckCreate() throws JsonProcessingException {
    CheckCreate checkCreate = CheckCreate.builder()
      .account(Address.of("rUn84CUYbNjRoTQ6mSW7BVJPSVJNLb1QLo"))
      .sequence(UnsignedInteger.ONE)
      .fee(XrpCurrencyAmount.of("12"))
      .destination(Address.of("rfkE1aSy9G8Upk4JssnwBxhEv5p4mn2KTy"))
      .destinationTag(UnsignedInteger.ONE)
      .sendMax(XrpCurrencyAmount.of("100000000"))
      .expiration(UnsignedInteger.valueOf(570113521))
      .invoiceId(Hash256.of("6F1DFD1D0FE8A32E40E1F2C05CF1C15545BAB56B617F9C6C2D63A6B704BEF59B"))
      .build();

    String expectedBinary = "120010228000000024000000012A21FB3DF12E0000000150116F1DFD1D0FE8A32E40E1F2C05CF1C15545BAB56B"
      + "617F9C6C2D63A6B704BEF59B68400000000000000C694000000005F5E10081147990EC5D1D8DF69E070A968D4B186986FDF06ED0831449"
      + "FF0C73CA6AF9733DA805F76CA2C37776B7C46B";
    assertSerializesAndDeserializes(checkCreate, expectedBinary);
  }

  @Test
  void serializeDepositPreAuth() throws JsonProcessingException {
    DepositPreAuth preAuth = DepositPreAuth.builder()
      .account(Address.of("rDd6FpNbeY2CrQajSmP178BmNGusmQiYMM"))
      .authorize(Address.of("rDJFnv5sEfp42LMFiX3mVQKczpFTdxYDzM"))
      .fee(XrpCurrencyAmount.of("10"))
      .sequence(UnsignedInteger.valueOf(65))
      .build();

    String expectedBinary = "1200132280000000240000004168400000000000000A81148A928D14A643F388AC0D26BAF9755B07EB0A2B4485"
      + "1486FFE2A17E861BA0FE9A3ED8352F895D80E789E0";
    assertSerializesAndDeserializes(preAuth, expectedBinary);
  }

  @Test
  void serializeEscrowCreate() throws JsonProcessingException, DerEncodingException {
    EscrowCreate checkCreate = EscrowCreate.builder()
      .account(Address.of("r4jQDHCUvgcBAa5EzcB1D8BHGcjYP9eBC2"))
      .amount(XrpCurrencyAmount.of("100"))
      .fee(XrpCurrencyAmount.of("12"))
      .sequence(UnsignedInteger.ONE)
      .cancelAfter(UnsignedLong.valueOf(630000001))
      .finishAfter(UnsignedLong.valueOf(630000000))
      .destination(Address.of("rHb9CJAWyB4rj91VRWn96DkukG4bwdtyTh"))
      .destinationTag(UnsignedInteger.valueOf(23480))
      .condition(CryptoConditionReader.readCondition(BaseEncoding.base16().decode(
        "A0258020E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855810100"))
      )
      .build();

    String expectedBinary = "120001228000000024000000012E00005BB82024258D09812025258D0980614000000000000064684000000000"
      + "00000C701127A0258020E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B8558101008114EE5F7CF61504C7CF"
      + "7E0C22562EB19CC7ACB0FCBA8314B5F762798A53D543A014CAF8B297CFF8F2F937E8";
    assertSerializesAndDeserializes(checkCreate, expectedBinary);
  }

  @Test
  void serializeEscrowCancel() throws JsonProcessingException {
    EscrowCancel escrowCancel = EscrowCancel.builder()
      .account(Address.of("r4jQDHCUvgcBAa5EzcB1D8BHGcjYP9eBC2"))
      .fee(XrpCurrencyAmount.of("12"))
      .sequence(UnsignedInteger.ONE)
      .owner(Address.of("r4jQDHCUvgcBAa5EzcB1D8BHGcjYP9eBC2"))
      .offerSequence(UnsignedInteger.valueOf(25))
      .build();

    String expectedBinary = "1200042280000000240000000120190000001968400000000000000C8114EE5F7CF61504C7CF7E0C22562EB19C"
      + "C7ACB0FCBA8214EE5F7CF61504C7CF7E0C22562EB19CC7ACB0FCBA";
    assertSerializesAndDeserializes(escrowCancel, expectedBinary);
  }

  @Test
  void serializeEscrowFinish() throws JsonProcessingException, DerEncodingException {
    EscrowFinish escrowFinish = EscrowFinish.builder()
      .account(Address.of("rMYPppnVNQ7crMizv8D6wF45kYuSupygyr"))
      .fee(XrpCurrencyAmount.of("330"))
      .sequence(UnsignedInteger.valueOf(3))
      .owner(Address.of("rMYPppnVNQ7crMizv8D6wF45kYuSupygyr"))
      .offerSequence(UnsignedInteger.valueOf(25))
      .condition(CryptoConditionReader.readCondition(BaseEncoding.base16().decode(
        "A0258020E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855810100")
      ))
      .fulfillment(CryptoConditionReader.readFulfillment(BaseEncoding.base16().decode("A0028000")))
      .build();

    String expectedBinary = "1200022280000000240000000320190000001968400000000000014A701004A0028000701127A0258020E3B0C4"
      + "4298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B8558101008114E151CA3207BAB5B91D2F0E4D35ECDFD4551C69A182"
      + "14E151CA3207BAB5B91D2F0E4D35ECDFD4551C69A1";
    assertSerializesAndDeserializes(escrowFinish, expectedBinary);
  }

  @Test
  void serializeXrpPayment() throws JsonProcessingException {
    Address source = Address.builder()
      .value("r45dBj4S3VvMMYXxr9vHX4Z4Ma6ifPMCkK")
      .build();

    Address destination = Address.builder()
      .value("rrrrrrrrrrrrrrrrrrrrBZbvji")
      .build();

    Payment payment = Payment.builder()
      .account(source)
      .destination(destination)
      .sourceTag(UnsignedInteger.valueOf(1))
      .destinationTag(UnsignedInteger.valueOf(2))
      .amount(XrpCurrencyAmount.of("12345"))
      .fee(XrpCurrencyAmount.of("789"))
      .sequence(UnsignedInteger.valueOf(56565656))
      .build();

    String expectedBinary = "1200002280000000230000000124035F1F982E00000002614000000000003039684000000000000" +
      "3158114EE39E6D05CFD6A90DAB700A1D70149ECEE29DFEC83140000000000000000000000000000000000000001";
    assertSerializesAndDeserializes(payment, expectedBinary);
  }

  @Test
  void serializeIssuedCurrencyPayment() throws JsonProcessingException {
    Address source = Address.builder()
      .value("r45dBj4S3VvMMYXxr9vHX4Z4Ma6ifPMCkK")
      .build();

    Address destination = Address.builder()
      .value("rrrrrrrrrrrrrrrrrrrrBZbvji")
      .build();

    Address issuer = Address.builder()
      .value("rDgZZ3wyprx4ZqrGQUkquE9Fs2Xs8XBcdw")
      .build();

    CurrencyAmount amount = IssuedCurrencyAmount.builder()
      .currency("USD")
      .issuer(issuer)
      .value("1234567890123456")
      .build();

    Payment payment = Payment.builder()
      .flags(PaymentFlags.builder()
        .partialPayment(true)
        .build())
      .account(source)
      .destination(destination)
      .sourceTag(UnsignedInteger.valueOf(1))
      .destinationTag(UnsignedInteger.valueOf(2))
      .amount(amount)
      .fee(XrpCurrencyAmount.of("789"))
      .sequence(UnsignedInteger.valueOf(1))
      .build();

    String expectedBinary = "1200002280020000230000000124000000012E0000000261D84462D53C8ABAC0000000000000000000000000" +
      "55534400000000008B1CE810C13D6F337DAC85863B3D70265A24DF446840000000000003158114EE39E6D05CFD6A90DAB700A1D7" +
      "0149ECEE29DFEC83140000000000000000000000000000000000000001";
    assertSerializesAndDeserializes(payment, expectedBinary);
  }

  @Test
  void serializePaymentChannelCreate() throws JsonProcessingException {
    PaymentChannelCreate create = PaymentChannelCreate.builder()
      .account(Address.of("rf1BiGeXwwQoi8Z2ueFYTEXSwuJYfV2Jpn"))
      .sourceTag(UnsignedInteger.ONE)
      .fee(XrpCurrencyAmount.of("100"))
      .sequence(UnsignedInteger.ONE)
      .amount(XrpCurrencyAmount.of("10000"))
      .destination(Address.of("rf1BiGeXwwQoi8Z2ueFYTEXSwuJYfV2Jpn"))
      .destinationTag(UnsignedInteger.valueOf(2))
      .settleDelay(UnsignedInteger.ONE)
      .publicKey("32D2471DB72B27E3310F355BB33E339BF26F8392D5A93D3BC0FC3B566612DA0F0A")
      .cancelAfter(UnsignedLong.valueOf(533171558))
      .build();

    String expectedBinary = "12000D2280000000230000000124000000012E0000000220241FC78D6620270000000161400000000000" +
      "2710684000000000000064712132D2471DB72B27E3310F355BB33E339BF26F8392D5A93D3BC0FC3B566612DA0F0A81144B4E9C06F" +
      "24296074F7BC48F92A97916C6DC5EA983144B4E9C06F24296074F7BC48F92A97916C6DC5EA9";
    assertSerializesAndDeserializes(create, expectedBinary);
  }

  @Test
  void serializePaymentChannelClaim() throws JsonProcessingException {
    PaymentChannelClaim claim = PaymentChannelClaim.builder()
      .account(Address.of("rsA2LpzuawewSBQXkiju3YQTMzW13pAAdW"))
      .fee(XrpCurrencyAmount.of("10"))
      .sequence(UnsignedInteger.ONE)
      .flags(Flags.PaymentChannelClaimFlags.builder().tfClose(true).build())
      .channel(Hash256.of("C1AE6DDDEEC05CF2978C0BAD6FE302948E9533691DC749DCDD3B9E5992CA6198"))
      .balance(XrpCurrencyAmount.of("1000000"))
      .amount(XrpCurrencyAmount.of("1000000"))
      .signature("30440220718D264EF05CAED7C781FF6DE298DCAC68D002562C9BF3A07C1E721B420C0DAB02203A5A4779E" +
        "F4D2CCC7BC3EF886676D803A9981B928D3B8ACA483B80ECA3CD7B9B")
      .publicKey("32D2471DB72B27E3310F355BB33E339BF26F8392D5A93D3BC0FC3B566612DA0F0A")
      .build();

    String expectedBinary = "12000F228002000024000000015016C1AE6DDDEEC05CF2978C0BAD6FE302948E9533691DC74" +
      "9DCDD3B9E5992CA61986140000000000F42406240000000000F424068400000000000000A712132D2471DB72B27E3310F" +
      "355BB33E339BF26F8392D5A93D3BC0FC3B566612DA0F0A764630440220718D264EF05CAED7C781FF6DE298DCAC68D0025" +
      "62C9BF3A07C1E721B420C0DAB02203A5A4779EF4D2CCC7BC3EF886676D803A9981B928D3B8ACA483B80ECA3CD7B9B8114" +
      "204288D2E47F8EF6C99BCC457966320D12409711";

    assertSerializesAndDeserializes(claim, expectedBinary);
  }

  @Test
  void serializePaymentChannelFund() throws JsonProcessingException {
    PaymentChannelFund fund = PaymentChannelFund.builder()
      .account(Address.of("rf1BiGeXwwQoi8Z2ueFYTEXSwuJYfV2Jpn"))
      .fee(XrpCurrencyAmount.of("10"))
      .sequence(UnsignedInteger.ONE)
      .channel(Hash256.of("C1AE6DDDEEC05CF2978C0BAD6FE302948E9533691DC749DCDD3B9E5992CA6198"))
      .amount(XrpCurrencyAmount.of("200000"))
      .expiration(UnsignedLong.valueOf(543171558))
      .build();

    String expectedJson = "12000E228000000024000000012A206023E65016C1AE6DDDEEC05CF2978C0BAD6FE302948E9533" +
      "691DC749DCDD3B9E5992CA6198614000000000030D4068400000000000000A81144B4E9C06F24296074F7BC48F92A97916C6DC5EA9";

    assertSerializesAndDeserializes(fund, expectedJson);
  }

  @Test
  void serializeTrustLine() throws JsonProcessingException {
    TrustSet trustSet = TrustSet.builder()
      .account(Address.of("rJMiz2rCMjZzEMijXNH1exNBryTQEjFd9S"))
      .fee(XrpCurrencyAmount.of("12"))
      .flags(Flags.TrustSetFlags.builder()
        .tfSetNoRipple()
        .tfFullyCanonicalSig(false)
        .build())
      .sequence(UnsignedInteger.valueOf(44))
      .limitAmount(IssuedCurrencyAmount.builder()
        .currency("WCG")
        .issuer(Address.of("rUx4xgE7bNWCCgGcXv1CCoQyTcCeZ275YG"))
        .value("10000000")
        .build())
      .build();


    String expectedBinary = "1200142200020000240000002C63D6438D7EA4C680000000000000000000000000005743470000000000832297BEF589D59F9C03A84F920F8D9128CC1CE468400000000000000C8114BE6C30732AE33CF2AF3344CE8172A6B9300183E3";
    assertSerializesAndDeserializes(trustSet, expectedBinary);
  }

  @Test
  public void serializeOfferCreate() throws JsonProcessingException {
    OfferCreate offerCreate = OfferCreate.builder()
      .takerGets(currencyAmount(100))
      .takerPays(currencyAmount(200))
      .fee(XrpCurrencyAmount.of(300))
      .account(Address.of("rUx4xgE7bNWCCgGcXv1CCoQyTcCeZ275YG"))
      .sequence(UnsignedInteger.valueOf(11223344))
      .offerSequence(UnsignedInteger.valueOf(123))
      .flags(Flags.OfferFlags.builder().fullyCanonicalSig(true).sell(true).build())
      .expiration(UnsignedInteger.valueOf(456))
      .build();

    assertSerializesAndDeserializes(offerCreate, "12000722800800002400AB41302A000001C820190000007B64D5071AFD498D00000000000000000000000000005743470000000000832297BEF589D59F9C03A84F920F8D9128CC1CE465D5038D7EA4C680000000000000000000000000005743470000000000832297BEF589D59F9C03A84F920F8D9128CC1CE468400000000000012C8114832297BEF589D59F9C03A84F920F8D9128CC1CE4");
  }

  @Test
  public void serializeOfferCancel() throws JsonProcessingException {
    OfferCancel offerCreate = OfferCancel.builder()
      .fee(XrpCurrencyAmount.of(300))
      .account(Address.of("rUx4xgE7bNWCCgGcXv1CCoQyTcCeZ275YG"))
      .sequence(UnsignedInteger.valueOf(11223344))
      .offerSequence(UnsignedInteger.valueOf(123))
      .build();

    assertSerializesAndDeserializes(offerCreate, "12000822800000002400AB413020190000007B68400000000000012C8114832297BEF589D59F9C03A84F920F8D9128CC1CE4");
  }

  private <TxnType extends Transaction> void assertSerializesAndDeserializes(
    TxnType transaction,
    String expectedBinary
  ) throws JsonProcessingException {
    String transactionJson = objectMapper.writeValueAsString(transaction);
    String transactionBinary = binaryCodec.encode(transactionJson);
    assertThat(transactionBinary).isEqualTo(expectedBinary);

    String decodedBinary = binaryCodec.decode(transactionBinary);
    TxnType deserialized = objectMapper.readValue(decodedBinary, objectMapper.getTypeFactory().constructType(transaction.getClass()));
    assertThat(deserialized).isEqualTo(transaction);
  }

  private static IssuedCurrencyAmount currencyAmount(int amount) {
    return IssuedCurrencyAmount.builder()
      .currency("WCG")
      .issuer(Address.of("rUx4xgE7bNWCCgGcXv1CCoQyTcCeZ275YG"))
      .value(amount + "")
      .build();
  }

}
