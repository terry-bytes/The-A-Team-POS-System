/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ateam.ServiceImpl;

/**
 *
 * @author Train 09
 */
import ateam.Models.Email;
import ateam.Models.Product;
import ateam.Service.EmailService;
import java.math.BigDecimal;
import java.util.List;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailServiceImpl implements EmailService {

    private Email email;

    public EmailServiceImpl(Email email) {
        this.email = email;
    }

    public EmailServiceImpl() {
        this.email = new Email("ramovhatp@gmail.com", "xaed clmt qpis ctvf");
    }

    @Override
    public void sendMail(Email email) {
        final String USERNAME = email.getSender();
        final String PASSWORD = email.getPassword();
        String receiver = email.getReceiver().trim();
        String subject = email.getSubject();
        String content = email.getMessage();

        if (!isValidEmail(receiver)) {
            System.err.println("Invalid email address: " + receiver);
            return;
        }

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiver));
            message.setSubject(subject);
            message.setText(content);

            Transport.send(message);

            System.out.println("Email sent successfully to " + receiver);

        } catch (AddressException e) {
            System.err.println("Invalid email address: " + e.getMessage());
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendPasswordResetMail(String email, String otp) {
        final String from = "ramovhatp@gmail.com";
        final String password = "xaed clmt qpis ctvf";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            message.setSubject("Password Reset OTP");
            message.setText("Your OTP for password reset is: " + otp);

            Transport.send(message);
            System.out.println("OTP sent successfully to " + email);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendSaleReceipt(String toEmail, String salespersonName, String saleTime, List<Product> items, BigDecimal totalAmount, String paymentMethod) {
        final String from = "ramovhatp@gmail.com";
        final String password = "xaed clmt qpis ctvf";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setSubject("Sale Receipt");

            // Use HTML content for the email with inline CSS
            StringBuilder content = new StringBuilder();
            content.append("<html><body style='font-family: Arial, sans-serif; color: #333;'>")
                    .append("<div style='text-align: center; padding: 20px;'>")
                    .append("<img src='https://th.bing.com/th/id/OIP.QhSR_Wwm-xeoz9Fh3w0orAAAAA?rs=1&pid=ImgDetMain' alt='Company Logo' style='max-width: 150px;'>")
                    .append("</div>")
                    .append("<h2 style='color: #0056b3;'>Dear Customer,</h2>")
                    .append("<p>Thank you for your purchase!</p>")
                    .append("<h3 style='color: #0056b3;'>Sale Details:</h3>")
                    .append("<p><strong>Salesperson:</strong> ").append(salespersonName).append("</p>")
                    .append("<p><strong>Sale Time:</strong> ").append(saleTime).append("</p>")
                    .append("<h3 style='color: #0056b3;'>Items Bought:</h3>")
                    .append("<table border='1' cellpadding='10' cellspacing='0' style='width: 100%; border-collapse: collapse;'>")
                    .append("<tr style='background-color: #f4f4f4; color: #333;'>")
                    .append("<th style='border: 1px solid #ddd;'>Product Name</th>")
                    .append("<th style='border: 1px solid #ddd;'>SKU</th>")
                    .append("<th style='border: 1px solid #ddd;'>Size</th>")
                    .append("<th style='border: 1px solid #ddd;'>Color</th>")
                    .append("<th style='border: 1px solid #ddd;'>Quantity</th>")
                    .append("<th style='border: 1px solid #ddd;'>Price</th>")
                    .append("</tr>");

            for (Product item : items) {
                content.append("<tr>")
                        .append("<td style='border: 1px solid #ddd;'>").append(item.getProduct_name()).append("</td>")
                        .append("<td style='border: 1px solid #ddd;'>").append(item.getProduct_SKU()).append("</td>")
                        .append("<td style='border: 1px solid #ddd;'>").append(item.getSize()).append("</td>")
                        .append("<td style='border: 1px solid #ddd;'>").append(item.getColor()).append("</td>")
                        .append("<td style='border: 1px solid #ddd;'>").append(item.getScanCount()).append("</td>")
                        .append("<td style='border: 1px solid #ddd;'>").append(item.getProduct_price()).append("</td>")
                        .append("</tr>");
            }

            content.append("</table>")
                    .append("<p style='font-weight: bold; color: #0056b3;'>Total Amount: ").append(totalAmount).append("</p>")
                    .append("<p><strong>Payment Method:</strong> ").append(paymentMethod).append("</p>")
                    .append("<p>Thank you for shopping with us!</p>")
                    .append("<p>Best regards,<br>Your Company Name</p>")
                    .append("</body></html>");

            message.setContent(content.toString(), "text/html");

            Transport.send(message);
            System.out.println("Sale receipt sent successfully to " + toEmail);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }

}
