package com.login.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@Slf4j
public class SMSService {
    @Value("${twilio.account.sid}")
    private String TWILIO_ACCOUNT_SID;
    @Value("${twilio.account.auth_token}")
    private String TWILIO_ACCOUNT_AUTH_TOKEN;
    @Value("${twilio.account.mobile}")
    private String TWILIO_OUTGOING_PHONE;
    @PostConstruct
    private void setUp(){
        log.info("sid->"+TWILIO_ACCOUNT_SID+"token->"+TWILIO_ACCOUNT_AUTH_TOKEN);
        Twilio.init(TWILIO_ACCOUNT_SID,TWILIO_ACCOUNT_AUTH_TOKEN);
    }

    public String sendSMS(String number,String message) {
        log.info("Sending SMS to: "+number);
        Message msg=Message.creator(new PhoneNumber("+"+number),
                new PhoneNumber(TWILIO_OUTGOING_PHONE),
                message).create();
        return msg.getStatus().toString();
    }
}
