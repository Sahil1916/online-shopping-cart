package service;

import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import util.EmailConfig;

public class EmailService {

    private final Session session;

    public EmailService() {

        Properties props = new Properties();

        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        session = Session.getInstance(props,
                new Authenticator() {

                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {

                        return new PasswordAuthentication(
                                EmailConfig.EMAIL,
                                EmailConfig.APP_PASSWORD);
                    }
                });
    }

    private void sendHtmlMail(String to,
                              String subject,
                              String html) {

        try {

            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(EmailConfig.EMAIL));

            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(to));

            message.setSubject(subject);

            message.setContent(html, "text/html; charset=UTF-8");

            Transport.send(message);

            System.out.println("=================================");
            System.out.println("Email Sent Successfully");
            System.out.println("To : " + to);
            System.out.println("=================================");

        } catch (MessagingException e) {

            System.out.println("=================================");
            System.out.println("Email Sending Failed");
            System.out.println("=================================");

            e.printStackTrace();
        }
    }

    // ==========================================================
    // ORDER PLACED EMAIL
    // ==========================================================

    public void sendOrderPlacedMail(
            String customerName,
            String customerEmail,
            Long orderId,
            double total) {

        String subject = "Order Placed Successfully - ShopWith_Sahil";

        String html =
        		"<html>" +
        		"<body style='margin:0;padding:0;background:#f4f6f9;font-family:Arial,sans-serif;'>" +

        		"<table width='100%' cellpadding='0' cellspacing='0'>" +
        		"<tr>" +
        		"<td align='center'>" +

        		"<table width='650' cellpadding='0' cellspacing='0' style='background:#ffffff;margin:30px;border-radius:12px;overflow:hidden;'>"

        		+

        		"<tr>" +
        		"<td style='background:#4F46E5;padding:30px;text-align:center;color:white;'>" +
        		"<h1 style='margin:0;'>🛍 ShopVerse</h1>" +
        		"<p style='margin-top:10px;font-size:16px;'>Premium Shopping Experience</p>" +
        		"</td>" +
        		"</tr>"

        		+

        		"<tr>" +
        		"<td style='padding:35px;'>"

        		+

        		"<h2 style='color:#222;'>Hello " + customerName + " 👋</h2>"

        		+

        		"<p style='font-size:16px;color:#555;'>"
        		+
        		"Thank you for shopping with <b>ShopVerse</b>."
        		+
        		"</p>"

        		+

        		"<div style='background:#E8F5E9;border-left:6px solid #4CAF50;padding:18px;margin-top:20px;'>"

        		+
        		"<h2 style='margin:0;color:#2E7D32;'>✅ Order Placed Successfully</h2>"

        		+
        		"</div>"

        		+

        		"<table width='100%' cellpadding='10' cellspacing='0' style='margin-top:30px;border-collapse:collapse;'>"

        		+

        		"<tr style='background:#F5F5F5;'>"

        		+
        		"<th align='left'>Order ID</th>"

        		+
        		"<th align='left'>Total Amount</th>"

        		+
        		"</tr>"

        		+

        		"<tr>"

        		+
        		"<td>#" + orderId + "</td>"

        		+
        		"<td><b>₹" + total + "</b></td>"

        		+
        		"</tr>"

        		+

        		"</table>"

        		+

        		"<br>"

        		+

        		"<p style='font-size:15px;color:#555;'>"

        		+
        		"Your order is currently in "

        		+
        		"<b style='color:#FF9800;'>Pending</b> status."

        		+
        		"</p>"

        		+

        		"<p style='font-size:15px;color:#555;'>"

        		+
        		"We'll notify you when your order is"

        		+
        		"<b> Confirmed</b>,"

        		+
        		"<b> Shipped</b> and"

        		+
        		"<b> Delivered.</b>"

        		+
        		"</p>"

        		+

        		"<div style='text-align:center;margin-top:35px;'>"

        		+
        		"<a href='http://127.0.0.1:5500/Orders.html' "

        		+
        		"style='background:#4F46E5;color:white;text-decoration:none;padding:15px 30px;border-radius:8px;font-size:16px;'>"

        		+
        		"View My Orders"

        		+
        		"</a>"

        		+
        		"</div>"

        		+

        		"<hr style='margin-top:40px;'>"

        		+

        		"<h3>Need Help?</h3>"

        		+

        		"<p>Email : support@shopverse.com</p>"

        		+

        		"<p>Phone : +91 9876543210</p>"

        		+

        		"<p style='margin-top:40px;color:#888;'>"

        		+
        		"© 2026 ShopVerse. All Rights Reserved."

        		+
        		"</p>"

        		+

        		"</td>"

        		+
        		"</tr>"

        		+

        		"</table>"

        		+

        		"</td>"

        		+
        		"</tr>"

        		+
        		"</table>"

        		+

        		"</body>"

        		+
        		"</html>";
    
        sendHtmlMail(customerEmail, subject, html);
    }
}