package ateam.ServiceImpl;

import ateam.Models.Email;
import ateam.Models.Product;
import ateam.Service.EmailService;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.util.ByteArrayDataSource;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;
import java.io.ByteArrayOutputStream;

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
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            message.setSubject("Password Reset OTP");
            message.setText("Your OTP for password reset is: " + otp);

            Transport.send(message);
            System.out.println("OTP sent successfully to " + email);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void VoucherEmail(String to,BigDecimal change,int voucher){
        // Recipient's email ID needs to be mentioned
       

        // Sender's email ID needs to be mentioned
        String from = "Carols_Boutique@outlook.com";
        final String username = "Carols_Boutique@outlook.com"; // change accordingly
        final String password = "Password@4"; // change accordingly

        // Assuming you are sending email through relay.jangosmtp.net
        String host = "smtp.@outlook.com"; // replace with your SMTP server

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");

        // Get the Session object
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            // Create a default MimeMessage object
            Message message = new MimeMessage(session);

            // Set From: header field of the header
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));

            // Set Subject: header field
            message.setSubject("Your Gift Voucher from Carol's Boutique!");

            // Retrieve session attributes
           

            // Create the message part
            BodyPart messageBodyPart = new MimeBodyPart();

            // Now set the actual message
            String htmlContent = "<html><body>"
                    + "<div class='voucher-container' style='max-width: 600px; margin: 50px auto; background-color: #ffffff; border: 1px solid #dedede; border-radius: 10px; overflow: hidden; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);'>"
                    + "<div class='voucher-header' style='background-color: #343a40; color: #ffffff; padding: 20px; text-align: center;'>"
                    + "<h1 style='margin: 0; font-size: 24px;'>Carol's Boutique</h1>"
                    + "</div>"
                    + "<div class='voucher-body' style='padding: 20px; text-align: center;'>"
                    + "<h2 style='margin-top: 0; font-size: 22px; color: #333333;'>Gift Voucher</h2>"
                    + "<p>Congratulations! You have received a gift voucher worth</p>"
                    + "<div class='voucher-amount' style='font-size: 20px; color: #28a745;'>" + change.toString() + "</div>"
                    + "<p>Your voucher code is:</p>"
                    + "<div class='voucher-code' style='margin: 20px 0; font-size: 30px; font-weight: bold; color: #e83e8c; letter-spacing: 2px;'>" + voucher + "</div>"
                    + "</div>"
                    + "<div class='voucher-footer' style='background-color: #f8f9fa; padding: 10px; text-align: center; font-size: 12px; color: #6c757d;'>"
                    + "<p>Thank you for shopping at Carol's Boutique!</p>"
                    + "<p>Please present this voucher at the time of purchase.</p>"
                    + "</div>"
                    + "</div>"
                    + "</body></html>";

            messageBodyPart.setContent(htmlContent, "text/html");

            // Create a multipart message
            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);

            // Send the complete message parts
            message.setContent(multipart);

            // Send message
            Transport.send(message);

            System.out.println("Sent message successfully....");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    
    @Override
    public void sendSaleReceipt(String toEmail, String salespersonName, String saleTime, List<Product> items, BigDecimal totalAmountWithVAT, BigDecimal vatAmount, BigDecimal change, String paymentMethod, BigDecimal cashPaid, BigDecimal cardPaid) {
        final String from = "ramovhatp@gmail.com";
        final String password = "xaed clmt qpis ctvf";
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {
            // Create the PDF content
            ByteArrayOutputStream pdfStream = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, pdfStream);
            document.open();

            // Add stylish content to PDF
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 24, Font.BOLD, BaseColor.RED);
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.DARK_GRAY);
            Font normalFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);

            // Add Title
            Paragraph title = new Paragraph("Sale Receipt", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));

            // Add a separator line
            LineSeparator separator = new LineSeparator();
            separator.setLineWidth(2f);
            separator.setLineColor(BaseColor.RED);
            document.add(separator);
            document.add(new Paragraph(" "));

            // Add sale details
            document.add(new Paragraph("Dear Customer,", normalFont));
            document.add(new Paragraph("Thank you for your purchase!", normalFont));
            document.add(new Paragraph(" "));

            Paragraph saleDetails = new Paragraph("Sale Details:", headerFont);
            saleDetails.setSpacingBefore(10);
            document.add(saleDetails);
            document.add(new Paragraph("Salesperson: " + salespersonName, normalFont));
            document.add(new Paragraph("Sale Time: " + saleTime, normalFont));
            document.add(new Paragraph(" "));

            // Create and style table
            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            // Set column widths (adjust as needed for SKU column width)
            float[] columnWidths = {2f, 2.5f, 1f, 1f, 1f, 2f}; // Increased width for SKU column
            table.setWidths(columnWidths);

            // Table headers
            PdfPCell cell = new PdfPCell(new Phrase("Product Name", headerFont));
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            table.addCell(new PdfPCell(new Phrase("SKU", headerFont)));
            table.addCell(new PdfPCell(new Phrase("Size", headerFont)));
            table.addCell(new PdfPCell(new Phrase("Color", headerFont)));
            table.addCell(new PdfPCell(new Phrase("Quantity", headerFont)));
            table.addCell(new PdfPCell(new Phrase("Price", headerFont)));

            // Table data
            for (Product item : items) {
                table.addCell(new PdfPCell(new Phrase(item.getProduct_name(), normalFont)));
                table.addCell(new PdfPCell(new Phrase(item.getProduct_SKU(), normalFont)));
                table.addCell(new PdfPCell(new Phrase(item.getSize(), normalFont)));
                table.addCell(new PdfPCell(new Phrase(item.getColor(), normalFont)));
                table.addCell(new PdfPCell(new Phrase(String.valueOf(item.getScanCount()), normalFont)));
                table.addCell(new PdfPCell(new Phrase(String.format("R%.2f", item.getProduct_price()), normalFont)));
            }

            document.add(table);
            document.add(new Paragraph(" "));
            Paragraph totalAmountParagraph = new Paragraph("Total Amount: " + String.format("R%.2f", totalAmountWithVAT), headerFont);
            totalAmountParagraph.setSpacingBefore(10);
            document.add(totalAmountParagraph);
            document.add(new Paragraph("VAT Amount: " + String.format("R%.2f", vatAmount), normalFont));
            document.add(new Paragraph("Cash Paid: " + String.format("R%.2f", cashPaid), normalFont));
            document.add(new Paragraph("Change: " + String.format("R%.2f", change), normalFont));
            document.add(new Paragraph("Payment Method: " + paymentMethod, normalFont));
            document.add(new Paragraph(" "));

            // Footer
            Paragraph footer = new Paragraph("Thank you for shopping with us!", normalFont);
            footer.setSpacingBefore(20);
            document.add(footer);
            document.add(new Paragraph("Best regards,", normalFont));
            document.add(new Paragraph("Your Company Name", normalFont));

            document.close();
            pdfStream.flush();
            byte[] pdfBytes = pdfStream.toByteArray();
            pdfStream.close();

            // Send the email with PDF attachment
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setSubject("Sale Receipt");

            Multipart multipart = new MimeMultipart();

            // Text part of the email
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText("Please find your sale receipt attached.");
            multipart.addBodyPart(messageBodyPart);

            // PDF attachment
            messageBodyPart = new MimeBodyPart();
            DataSource source = new ByteArrayDataSource(pdfBytes, "application/pdf");
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName("SaleReceipt.pdf");
            multipart.addBodyPart(messageBodyPart);

            message.setContent(multipart);

            Transport.send(message);

            System.out.println("Sale receipt email sent successfully to " + toEmail);

        } catch (MessagingException | IOException | DocumentException e) {
            e.printStackTrace();
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        return email.matches(emailRegex);
    }
}
