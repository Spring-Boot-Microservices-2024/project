// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: payment.proto

package com.example.payment;

public interface PaymentReturnRequestOrBuilder extends
    // @@protoc_insertion_point(interface_extends:PaymentReturnRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>int64 payment_id = 1;</code>
   * @return The paymentId.
   */
  long getPaymentId();

  /**
   * <code>float refund_sum = 2;</code>
   * @return The refundSum.
   */
  float getRefundSum();

  /**
   * <code>string timestamp = 3;</code>
   * @return The timestamp.
   */
  java.lang.String getTimestamp();
  /**
   * <code>string timestamp = 3;</code>
   * @return The bytes for timestamp.
   */
  com.google.protobuf.ByteString
      getTimestampBytes();
}
