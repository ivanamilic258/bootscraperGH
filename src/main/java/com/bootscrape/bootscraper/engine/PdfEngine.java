package com.bootscrape.bootscraper.engine;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.List;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;

@Component
public class PdfEngine {


public byte[] generatePdfFromList(List<String> rows){
	ByteArrayOutputStream outputStream = null;
	try {
		outputStream = new ByteArrayOutputStream();
		writePdf( outputStream, rows );
	} catch (DocumentException e) {
		e.printStackTrace();
	}

	return outputStream.toByteArray();
}

	private void writePdf(OutputStream outputStream, List<String> rows) throws DocumentException {
	Document document = new Document();
	PdfWriter.getInstance( document, outputStream);
	document.open();
	for (String row : rows) {
		Paragraph paragraph = new Paragraph();
		paragraph.add(new Chunk(row));
		document.add(paragraph);
	}
	document.close();
}


}
