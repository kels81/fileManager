/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vaadin.demo.dashboard.utils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

/**
 *
 * @author Edrd
 */
public class Mail {

    public Mail() {
    }

    //public boolean enviar(String asunto, List<String> receptores, String mensaje, List<String> adjuntos) {
    public boolean enviar(String asunto, String receptores, String mensaje, List<String> adjuntos) {
        try {
            String emisor = "mamaardilla@hotmail.com";
            Properties props = new Properties();
            // [HOTMAIL]
            props.setProperty("mail.smtp.host", "smtp.live.com");
            props.setProperty("mail.smtp.starttls.enable", "true");
            props.setProperty("mail.smtp.port", "25");
            props.setProperty("mail.smtp.user", "mamaardilla@hotmail.com");
            props.setProperty("mail.smtp.auth", "true");

            //Se obtiene sesión desde el servidor de correos
            Session session = Session.getInstance(props, null);
            session.setDebug(true);

            //Se obtienen todos los receptores para enviar el e-mail
            MimeMessage message = new MimeMessage(session);

            //List<InternetAddress> emailsDestino = new ArrayList<InternetAddress>();
            int i = 0;
            //receptores = receptores.replaceAll(".*[]", "");
            StringTokenizer emailsSt = new StringTokenizer(receptores, "[,]");
            while (emailsSt.hasMoreTokens()) {
                String email = emailsSt.nextToken();
                try {
                    //agregamos las direcciones de email que reciben el email, en el primer parametro envíamos el tipo de receptor
                    message.addRecipients(Message.RecipientType.TO, email);
                    //Message.RecipientType.TO;  para
                    //Message.RecipientType.CC;  con copia
                    //Message.RecipientType.BCC; con copia oculta
                } catch (Exception ex) {
                    //en caso que el email esté mal formado lanzará una exception y la ignoramos
                }
            }

//            InternetAddress[] dest = new InternetAddress[receptores.size()];
//            for (int i = 0; i < dest.length; i++) {
//                dest[i] = new InternetAddress(receptores.get(i));
//            }
            //Se define quién es el emisor del e-mail
            message.setFrom(new InternetAddress(emisor));

            //Se definen el o los destinatarios
//            message.addRecipients(Message.RecipientType.TO, dest);
            //message.addRecipients(Message.RecipientType.CC, dest);
            //message.addRecipients(Message.RecipientType.BCC, dest);
            //Se define el asunto del e-mail y la fecha de envio
            message.setSubject(asunto);
            message.setSentDate(new Date());

            //Se seteo el mensaje del e-mail
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(mensaje);
            messageBodyPart.setContent(mensaje, "text/html; charset=ISO-8859-1");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            //Se adjuntan los archivos al correo
            if (adjuntos != null && adjuntos.size() > 0) {
                for (String rutaAdjunto : adjuntos) {
                    messageBodyPart = new MimeBodyPart();
                    File f = new File(rutaAdjunto);
                    if (f.exists()) {
                        DataSource source = new FileDataSource(rutaAdjunto);
                        messageBodyPart.setDataHandler(new DataHandler(source));
                        messageBodyPart.setFileName(f.getName());
                        multipart.addBodyPart(messageBodyPart);
                    }
                }
            }

            //Se junta el mensaje y los archivos adjuntos
            message.setContent(multipart);

            Transport t = null;
            t = session.getTransport("smtp");
            t.connect("smtp.live.com", "mamaardilla@hotmail.com", "Cyb3rs1x");    //hotmail
            t.sendMessage(message, message.getAllRecipients());
            t.close();

            return true;
        } catch (MessagingException ex) {
            return false;
        }
    }

    public boolean enviarSpring(String asunto, String receptores, String mensaje, List<String> adjuntos) {
        try {

            JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

            String emisor = "mamaardilla@hotmail.com";
            Properties properties = new Properties();
            // [HOTMAIL]
            properties.setProperty("mail.smtp.host", "smtp.live.com");
            properties.setProperty("mail.smtp.port", "25");
            properties.setProperty("mail.smtp.starttls.enable", "true");
            properties.setProperty("mail.smtp.user", "mamaardilla@hotmail.com");
            properties.setProperty("mail.smtp.password", "Cyb3rs1x");
            properties.put("mail.smtp.auth", "true");

            mailSender.setJavaMailProperties(properties);
            mailSender.setUsername("mamaardilla@hotmail.com");
            mailSender.setPassword("Cyb3rs1x");

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.toString());

            //Se obtienen todos los receptores para enviar el e-mail
            StringTokenizer emailsSt = new StringTokenizer(receptores, "[,]");
            while (emailsSt.hasMoreTokens()) {
                String email = emailsSt.nextToken();
                try {
                    //Se definen el o los destinatarios que reciben el mail
                    helper.addTo(email);
                } catch (Exception ex) {
                    //en caso que el email esté mal formado lanzará una exception y la ignoramos
                }
            }

            //Se define quién es el emisor del e-mail
            helper.setFrom(emisor);

            //Se define el asunto del e-mail y la fecha de envio
            helper.setSubject(asunto);
            helper.setSentDate(new Date());

            //Se seteo el mensaje del e-mail
            helper.setText(mensaje, true); // true to activate multipart


            //Se adjuntan los archivos al correo
            if (adjuntos != null && adjuntos.size() > 0) {
                for (String rutaAdjunto : adjuntos) {
                    File f = new File(rutaAdjunto);
                    if (f.exists()) {
                        DataSource source = new FileDataSource(rutaAdjunto);
                        helper.addAttachment(f.getName(), source);
                    }
                }
            }
            
            //Se manda el email
            mailSender.send(message);

            return true;
        } catch (MessagingException e) {
            System.out.println("e: "+e.getMessage());
            return false;
        }

    }

}
