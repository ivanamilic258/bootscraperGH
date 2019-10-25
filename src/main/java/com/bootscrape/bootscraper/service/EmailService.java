package com.bootscrape.bootscraper.service;

import com.bootscrape.bootscraper.engine.PdfEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class EmailService {

	@Autowired
	PdfEngine  pdfEngine;

	public void sendEmail(){
		byte[] bytes =pdfEngine.generatePdfFromList( Arrays.asList( "dsh vdfsgkjsdfhgdfkb gk dfabgfsb vd","fdasdf xsz","dfsfdsf dfcs" ) );
	}

}
