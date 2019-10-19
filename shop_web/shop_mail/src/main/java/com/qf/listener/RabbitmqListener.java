package com.qf.listener;

import com.qf.entity.Email;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;

@Component
public class RabbitmqListener {

    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    private JavaMailSender javaMailSender;


    @RabbitListener(queues = "mail_queue")
    public void sendEmail(Email email) {

        System.out.println("收到邮件："+email);

        System.out.println("发送人为："+from);

        //创建一封邮件
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        //创建一个邮件的包装工具
        MimeMessageHelper mimeMessageHelper = null;

        try {
            mimeMessageHelper = new MimeMessageHelper(mimeMessage);

            mimeMessageHelper.setSubject(email.getSubject());

            mimeMessageHelper.setFrom(from);

            mimeMessageHelper.setTo(email.getTo());

            mimeMessageHelper.setText(email.getContent(),true);

            mimeMessageHelper.setSentDate(new Date());

            javaMailSender.send(mimeMessage);
            System.out.println("邮件发送成功");
        } catch (MessagingException e) {
            e.printStackTrace();
        }




    }
}
