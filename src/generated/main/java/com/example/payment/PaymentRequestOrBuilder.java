// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: payment.proto

package com.example.payment;

public interface PaymentRequestOrBuilder extends
    // @@protoc_insertion_point(interface_extends:PaymentRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>int64 user_id = 1;</code>
   * @return The userId.
   */
  long getUserId();

  /**
   * <code>int64 event_id = 2;</code>
   * @return The eventId.
   */
  long getEventId();

  /**
   * <code>float price = 3;</code>
   * @return The price.
   */
  float getPrice();

  /**
   * <code>string payment_method = 4;</code>
   * @return The paymentMethod.
   */
  java.lang.String getPaymentMethod();
  /**
   * <code>string payment_method = 4;</code>
   * @return The bytes for paymentMethod.
   */
  com.google.protobuf.ByteString
      getPaymentMethodBytes();

  /**
   * <code>string timestamp = 5;</code>
   * @return The timestamp.
   */
  java.lang.String getTimestamp();
  /**
   * <code>string timestamp = 5;</code>
   * @return The bytes for timestamp.
   */
  com.google.protobuf.ByteString
      getTimestampBytes();
}
