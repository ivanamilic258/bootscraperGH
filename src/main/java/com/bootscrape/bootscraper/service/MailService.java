package com.bootscrape.bootscraper.service;

import com.bootscrape.bootscraper.engine.PdfEngine;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class MailService {

	@Autowired
	JavaMailSender mailSender;

	Logger logger = Logger.getLogger( MailService.class );

	private static String NO_REPLY = "noreply@wizzscraper.com";

	public void sendEmail(List<String> sendTo, String sendFrom, String subject, String htmlContent)
			throws MessagingException {
		createAndSendMimeMessage( sendTo, sendFrom, subject, htmlContent, Arrays.asList( "ivanamilic258@gmail.com" ), null,null, null , null);
	}

	public void sendEmailWithAttachment(List<String> sendTo, String sendFrom, String subject, String htmlContent, InputStream inputStream, String filename)
			throws MessagingException {
		createAndSendMimeMessage( sendTo, sendFrom, subject, htmlContent, Arrays.asList( "ivanamilic258@gmail.com" ), null,null, filename , inputStream);
	}


	private void createAndSendMimeMessage(List<String> sendTo, String sendFrom, String subject, String htmlContent, List<String> cc,
										  List<String> bcc, File file, String filename, InputStream stream) throws MessagingException {

		MimeMessage mimeMsg = mailSender.createMimeMessage();

		MimeMessageHelper helper = new MimeMessageHelper( mimeMsg, true );
		helper.setText( htmlContent, true );
		helper.setTo( sendTo.toArray(new String[0]) );
		helper.setSubject( subject );

		// if for any reason the sender is omitted we set it to noreply@hyperoptic.com
		if (Objects.equals( sendFrom, null )) {
			sendFrom = NO_REPLY;
			logger.warn( "No sender provided. Using no-reply." );
		}
		helper.setFrom( sendFrom );
		if (cc != null && !cc.isEmpty()) {
			helper.setCc( cc.toArray( new String[cc.size()] ) );
		}
		if (bcc != null && !bcc.isEmpty()) {
			helper.setBcc( bcc.toArray( new String[bcc.size()] ) );
		}
		if (!Objects.equals( file, null )) {
			try {
				filename = file.getName();
				stream = new FileInputStream( file );
			} catch (FileNotFoundException e) {
				logger.warn( "File not found: " + file.getName() + " while generating e-mail template. Sending an e-mail without it..." );
			}
		}
		if (!Objects.equals( filename, null ) && !Objects.equals( stream, null )) {
			try {
				helper.addAttachment( filename, new ByteArrayResource( IOUtils.toByteArray( stream ) ) );
			} catch (IOException e) {
				logger.warn( "Could not attach file from stream: " + filename + " to e-mail template. Sending an e-mail without it..." );
			} finally {
				try {
					stream.close();
				} catch (IOException e) {
					logger.error( "Could not close file stream " + filename + " while generating e-mail template." );
				}
			}
		}
		mailSender.send( mimeMsg );

	}

}
