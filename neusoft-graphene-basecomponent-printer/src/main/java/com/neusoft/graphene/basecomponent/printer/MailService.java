package com.neusoft.graphene.basecomponent.printer;

import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
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
import javax.mail.internet.MimeUtility;

import org.springframework.stereotype.Service;


import sun.misc.BASE64Encoder;

@Service("mailService")
public class MailService {
	@Resource
	private MailPropertis mailPropertis;

	Session mailSession;

	@PostConstruct
	private void init() {
		Properties p = new Properties();
		p.put("mail.smtp.host", mailPropertis.getSmtpHost());
		p.put("mail.smtp.port", mailPropertis.getSmtpPort());
		p.put("mail.smtp.auth", mailPropertis.getSmtpAuth());

		// 根据邮件会话属性和密码验证器构造一个发送邮件的session
		mailSession = Session.getDefaultInstance(p, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(mailPropertis.getAccount(), mailPropertis.getPassword());
			}
		});
	}


	private void attachMail(Multipart multipart, String filePath, String fileName) throws Exception {
		// 附加文件到邮件
		MimeBodyPart mimeBodyPart = new MimeBodyPart();
		FileDataSource fds = new FileDataSource(filePath);
		mimeBodyPart.setDataHandler(new DataHandler(fds));
		// mimeBodyPart.setFileName(fds.getName());
		BASE64Encoder enc = new BASE64Encoder();
		// 处理邮件附件名称乱码的问题
		// mimeBodyPart.setFileName("=?GBK?B?"+enc.encode(fileName.getBytes())+"?=");
		mimeBodyPart.setFileName("=?UTF-8?B?"+enc.encode(fileName.getBytes())+"?=");
		mimeBodyPart.setFileName(MimeUtility.encodeText(fileName));
		mimeBodyPart.setHeader("Content-Transfer-Encoding", "base64");
		multipart.addBodyPart(mimeBodyPart);
	}


//	List list = new ArrayList();
//	list.add(new String[] { "D:\\HadoopBook\\TortoiseSVN-1.7.11-zh_CN.pdf", "your policy.pdf" });
//	mailServer.send("songhui001@neusoft.io", "这是一个邮件Policy", "1111", list);
	public void send(String address, String subject, String content, List<String[]> attachments) {
		try {
			// 根据session创建一个邮件消息
			Message mailMessage = new MimeMessage(mailSession);
			// 设置邮件消息的发送者
			mailMessage.setFrom(new InternetAddress(mailPropertis.getAccount()));
			// 创建邮件的接收者地址，并设置到邮件消息中
			InternetAddress[] to = InternetAddress.parse(address);

			//TO avoid 
			mailMessage.addRecipients(Message.RecipientType.CC, InternetAddress.parse(mailPropertis.getAccount()));
			
			// Message.RecipientType.TO属性表示接收者的类型为TO
			mailMessage.setRecipients(Message.RecipientType.TO, to);
			// 设置邮件消息的主题
			mailMessage.setSubject(subject);
			// 设置邮件消息发送的时间
			mailMessage.setSentDate(new Date());
			// MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
			Multipart mainPart = new MimeMultipart();
			// 创建一个包含HTML内容的MimeBodyPart
			BodyPart html = new MimeBodyPart();
			// 设置HTML内容
			html.setContent(content, "text/html; charset=utf-8");
			mainPart.addBodyPart(html);
			
			for (int i = 0; i < attachments.size(); i++) {
				try {
					String[] s =  attachments.get(i);
					attachMail(mainPart, s[0], s[1]);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			// 将MiniMultipart对象设置为邮件内容
			mailMessage.setContent(mainPart);
			
			Transport transport = mailSession.getTransport("smtp");
			transport.connect(mailPropertis.getSmtpHost(), mailPropertis.getAccount(),
					mailPropertis.getPassword());
			transport.sendMessage(mailMessage, mailMessage.getAllRecipients());
			transport.close();
			
		} catch (MessagingException ex) {
			throw new RuntimeException("邮件发送失败", ex);
			
		}
	}
}
