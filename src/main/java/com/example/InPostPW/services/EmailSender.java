package com.example.InPostPW.services;

public interface EmailSender {
    void sendEmail(String toEmail, String subject, String body);
}
