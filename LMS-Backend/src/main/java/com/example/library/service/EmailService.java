package com.example.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // Send simple text email
    public void sendSimpleEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        message.setFrom("akashsasanka480@gmail.com");
        
        mailSender.send(message);
    }

    // Send HTML email 
    public void sendHtmlEmail(String to, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true); // true = HTML
        helper.setFrom("akashsasanka480@gmail.com");
        
        mailSender.send(message);
    }

    // Send welcome email after signup
    public void sendWelcomeEmail(String to, String firstName) throws MessagingException {
        String subject = "Welcome to Our Library!";
        String htmlContent = """
            <html>
            <body style="font-family: Arial, sans-serif;">
                <div style="max-width: 600px; margin: 0 auto; padding: 20px;">
                    <h2 style="color: #4CAF50;">Welcome to Our Library, %s!</h2>
                    <p>Thank you for signing up. Your account has been successfully created.</p>
                    <p>You can now:</p>
                    <ul>
                        <li>Browse our extensive collection of books</li>
                        <li>Borrow books online</li>
                        <li>Track your reading history</li>
                    </ul>
                    <div style="margin-top: 30px; padding: 15px; background-color: #f5f5f5; border-radius: 5px;">
                        <p style="margin: 0;">Happy Reading!</p>
                        <p style="margin: 5px 0 0 0; color: #666;">The Library Team</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(firstName);
        
        sendHtmlEmail(to, subject, htmlContent);
    }

    // Send book borrowed confirmation
    public void sendBookBorrowedEmail(String to, String bookTitle, String dueDate) throws MessagingException {
        String subject = "Book Borrowed Successfully";
        String htmlContent = """
            <html>
            <body style="font-family: Arial, sans-serif;">
                <div style="max-width: 600px; margin: 0 auto; padding: 20px;">
                    <h2 style="color: #2196F3;">Book Borrowed Successfully</h2>
                    <p>You have successfully borrowed: <strong>%s</strong></p>
                    <p>Due Date: <strong style="color: #f44336;">%s</strong></p>
                    <p>Please return the book on or before the due date to avoid late fees.</p>
                </div>
            </body>
            </html>
            """.formatted(bookTitle, dueDate);
        
        sendHtmlEmail(to, subject, htmlContent);
    }
}