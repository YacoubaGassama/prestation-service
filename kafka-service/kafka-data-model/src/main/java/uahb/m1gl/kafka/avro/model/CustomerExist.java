/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package uahb.m1gl.kafka.avro.model;
@org.apache.avro.specific.AvroGenerated
public enum CustomerExist implements org.apache.avro.generic.GenericEnumSymbol<CustomerExist> {
  EXIST, NOTEXIST  ;
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"enum\",\"name\":\"CustomerExist\",\"namespace\":\"uahb.m1gl.kafka.avro.model\",\"symbols\":[\"EXIST\",\"NOTEXIST\"]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  @Override
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
}
