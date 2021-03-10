package me.shetj.emailplugin;


import com.sun.mail.util.MailSSLSocketFactory;

import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.Project;
import org.gradle.api.tasks.Optional;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.security.GeneralSecurityException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * 发送邮件task
 * @author
 */
public class SendEmailTask extends DefaultTask {

    private Project mProject;


    public void init(Project project) {
        this.mProject = project;
        this.setGroup("android");
    }

    @TaskAction
    public void startSendEmail() {
        EmailExtension emailInfo = getAndCheckEmailInfo();
        try {
            // 收件人电子邮箱
            String to = emailInfo.emailTo;

            // 发件人电子邮箱
            String from = emailInfo.emailFrom;

            // 指定发送邮件的主机为 smtp.qq.com
            String host = emailInfo.emailHost;

            // 获取系统属性
            Properties properties = System.getProperties();
            // 设置邮件服务器
            properties.setProperty("mail.smtp.host", host);

            // 开启debug调试properties.setProperty("mail.debug", "true")
            properties.put("mail.smtp.port", 465);

            properties.put("mail.smtp.auth", "true");
            properties.setProperty("mail.transport.protocol", "smtp");

            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.ssl.socketFactory", sf);
            // 获取默认session对象
            Session session = Session.getDefaultInstance(properties, new Authenticator() {
                @Override
                public PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(emailInfo.emailUser, emailInfo.emailPassword); //发件人邮件用户名、密码
                }
            });

            try {
                // 创建默认的 MimeMessage 对象
                MimeMessage message = new MimeMessage(session);
                // Set From: 头部头字段
                message.setFrom(new InternetAddress(from));
                // Set To: 头部头字段
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
                // Set Subject: 头部头字段
                message.setSubject(emailInfo.emailTitle);

                // 创建消息部分
                BodyPart messageBodyPart = new MimeBodyPart();
                // 消息
                messageBodyPart.setText(emailInfo.emailMessage);
                // 创建多重消息
                Multipart multipart = new MimeMultipart();
                // 设置文本消息部分
                multipart.addBodyPart(messageBodyPart);

                if (emailInfo.emailFile != null && new File(emailInfo.emailFile).exists()) {
                    // 附件部分
                    File file = new File(emailInfo.emailFile);
                    messageBodyPart = new MimeBodyPart();
                    String filename = emailInfo.emailFile;
                    DataSource source = new FileDataSource(filename);
                    messageBodyPart.setDataHandler(new DataHandler(source));
                    messageBodyPart.setFileName(file.getName());
                    messageBodyPart.setDescription("这是一个文件");
                    multipart.addBodyPart(messageBodyPart);
                }
                // 发送完整消息
                message.setContent(multipart);
                // 发送消息

                Transport transport = session.getTransport();
                System.out.println("start sendEmail");
                transport.send(message);
                System.out.println("Sent message successfully....\n from = " + emailInfo.emailFrom +" ; to = "+ emailInfo.emailTo);
            } catch (MessagingException mex) {
                mex.printStackTrace();
                System.out.println("mex: " + mex.getMessage());
            }
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
            System.out.println("mex: " + e.getMessage());
        }
    }

    private EmailExtension getAndCheckEmailInfo() {
        EmailExtension emailInfo = EmailExtension.getConfig(mProject);
        if (emailInfo == null) {
            throw new GradleException("EmailHelper is must not be null!");
        }
        if (emailInfo.emailFrom == null) {
            throw new GradleException("emailFrom is must not be null!");
        }
        if (emailInfo.emailHost == null) {
            throw new GradleException("emailHost is must not be null!");
        }
        if (emailInfo.emailTo == null) {
            throw new GradleException("emailTo is must not be null!");
        }
        if (emailInfo.emailUser == null) {
            throw new GradleException("emailUser is must not be null!");
        }
        if (emailInfo.emailPassword == null) {
            throw new GradleException("emailPassword is must not be null!");
        }
        if (emailInfo.emailMessage == null) {
            throw new GradleException("emailMessage is must not be null!");
        }

        return emailInfo;
    }



}
