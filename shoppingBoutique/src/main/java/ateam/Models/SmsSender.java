/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ateam.Models;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class SmsSender {

    public static final String ACCOUNT_SID = "ACe8fdada7646497ac53040670783f2f5d";
    public static final String AUTH_TOKEN = "4eb5d4746fd78e8d5dc54f6fb8187f49";
    public static final String TWILIO_PHONE_NUMBER = "+12295446517";

    static {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    public static void sendSms(String toPhoneNumber, String messageBody) {
        Message message = Message.creator(
                new PhoneNumber(toPhoneNumber),
                new PhoneNumber(TWILIO_PHONE_NUMBER),
                messageBody)
                .create();

        System.out.println("SMS sent successfully. SID: " + message.getSid());
    }
    public static void main(String[] args) {
        sendSms("+27631821265", "the A team , collect your order");
    }
}
