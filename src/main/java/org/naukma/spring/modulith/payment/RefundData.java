package org.naukma.spring.modulith.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefundData {
    private long paymentId;
    private float refundSum;
    private String timestamp;
}
