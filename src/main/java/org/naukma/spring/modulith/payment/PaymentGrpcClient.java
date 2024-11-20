package org.naukma.spring.modulith.payment;

import com.example.payment.*;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentGrpcClient {
    private static final Logger logger = LoggerFactory.getLogger(PaymentGrpcClient.class);

    @GrpcClient("payment-service") // Reference to the gRPC server
    private PaymentServiceGrpc.PaymentServiceBlockingStub paymentServiceBlockingStub;

    @GrpcClient("payment-service") // Reference to the gRPC server
    private PaymentServiceGrpc.PaymentServiceStub paymentServiceStub;

    // Method to call the ProcessPayment (unary) gRPC method
    public String processPayment(long userId, long eventId, float price, String paymentMethod, String timestamp) {
        // Build the payment request
        PaymentRequest request = PaymentRequest.newBuilder()
                .setUserId(userId)
                .setEventId(eventId)
                .setPrice(price)
                .setPaymentMethod(paymentMethod)
                .setTimestamp(timestamp)
                .build();

        // Call the gRPC service and get a response
        PaymentResponse response = paymentServiceBlockingStub.processPayment(request);

        logger.info("Received payment response: {}", response.getMessage());
        return response.getMessage();
    }

    // Method for calling the Bidirectional Streaming RPC StreamPaymentReturn
    public void streamPaymentReturn(List<RefundData> refunds) {
        logger.info("Initiating bidirectional stream for payment return for payments");

        // Create a StreamObserver for the responses
        StreamObserver<PaymentReturnResponse> responseObserver = new StreamObserver<>() {

            @Override
            public void onNext(PaymentReturnResponse response) {
                // Process each response from the server
                logger.info("Received refund update: paymentId = {}, success = {}, message = {}",
                        response.getPaymentId(), response.getSuccess(), response.getMessage());
            }

            @Override
            public void onError(Throwable t) {
                // Handle any error that occurs during the stream
                logger.error("Stream encountered an error: ", t);
            }

            @Override
            public void onCompleted() {
                // Complete the stream when the server finishes
                logger.info("Payment return stream completed.");
            }
        };

        // Create a StreamObserver for the request
        StreamObserver<PaymentReturnRequest> requestObserver = paymentServiceStub.streamPaymentReturn(responseObserver);

        // Send multiple requests to the server as part of the stream
        for (RefundData refundData: refunds) { // Example loop to send multiple refund requests
            PaymentReturnRequest returnRequest = PaymentReturnRequest.newBuilder()
                    .setPaymentId(refundData.getPaymentId())
                    .setRefundSum(refundData.getRefundSum())
                    .setTimestamp(refundData.getTimestamp())
                    .build();

            logger.info("Sending payment return request for paymentId: {}, refundSum: {}", refundData.getPaymentId(), refundData.getRefundSum());
            requestObserver.onNext(returnRequest);
        }

        // Complete the stream (i.e., tell the server that we're done sending requests)
        requestObserver.onCompleted();
    }
}
