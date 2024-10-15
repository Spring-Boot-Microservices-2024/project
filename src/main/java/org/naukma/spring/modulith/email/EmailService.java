package org.naukma.spring.modulith.email;

import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JmsTemplate jmsTemplate;

    public void reportEmail(String email) {
        jmsTemplate.setPubSubDomain(true);
        jmsTemplate.convertAndSend("email", email);
    }

}
