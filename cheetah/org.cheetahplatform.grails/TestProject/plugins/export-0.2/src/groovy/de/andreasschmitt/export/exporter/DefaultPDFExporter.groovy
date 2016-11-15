package de.andreasschmitt.export.exporter

import com.lowagie.text.Document
import com.lowagie.text.DocumentException
import com.lowagie.text.PageSize
import com.lowagie.text.Paragraph
import com.lowagie.text.pdf.PdfPTable
import com.lowagie.text.pdf.PdfWriter
import com.lowagie.text.Font
import com.lowagie.text.FontFactory
import com.lowagie.text.pdf.BaseFont
import com.lowagie.text.pdf.PdfPCell
import com.lowagie.text.HeaderFooter
import com.lowagie.text.Phrase
import com.lowagie.text.Element
import com.lowagie.text.Rectangle
import java.awt.Color

class DefaultPDFExporter extends AbstractExporter {
	
	private static Map fontStyles = [bold: Font.BOLD, italic: Font.ITALIC, normal: Font.NORMAL, bolditalic: Font.BOLDITALIC]
	
	protected void exportData(OutputStream outputStream, List data, List fields) throws ExportingException{
		try {
			Document document = new Document(PageSize.A4.rotate(), 36, 36, 36, 36)
			
			PdfWriter.getInstance(document, outputStream)
			document.open()
			
			int fontSize = 8
			
			// Default encoding is Latin 1
			String encoding = BaseFont.CP1252 
			// Possible values Cp1250, Cp1252, Cp1257, Identity-H, Identity-V, MacRoman
			if(getParameters().containsKey("encoding")){
				encoding = getParameters().get("encoding")
			}
			
			// Title font
			Font title = createFont("title", FontFactory.HELVETICA, encoding, 10, Font.BOLD)
			
			// Header font
			Font header = createFont("header", FontFactory.HELVETICA, encoding, 8, Font.BOLD)
			
			// Text font
			Font text = createFont("text", FontFactory.HELVETICA, encoding, 8, Font.NORMAL)
			
			Phrase footerPhrase = new Phrase("", text)
			HeaderFooter footer = new HeaderFooter(footerPhrase, true)
			footer.setAlignment(Element.ALIGN_CENTER)
			footer.disableBorderSide(Rectangle.TOP)
			footer.disableBorderSide(Rectangle.BOTTOM)
			document.setFooter(footer)
			
			if(getParameters().containsKey("title")){
				Paragraph paragraph = new Paragraph(getParameters().get("title"), title)
				paragraph.setSpacingAfter(10)
				document.add(paragraph)
			}
			
			PdfPTable table = new PdfPTable(fields?.size())
			table.setWidthPercentage(100)
			table.setHeaderRows(1)
			
			Color borderColor = new Color(163, 163, 163)
			float minimumCellHeight = 17
			
			fields.each { field ->
				String value = getLabel(field)
				PdfPCell cell = new PdfPCell(new Paragraph(value, header))
				cell.setBorderColor(borderColor)
				cell.setMinimumHeight(minimumCellHeight)
				table.addCell(cell)
			}
			
			Color separatorColor = new Color(238, 238, 238)
			
			data.eachWithIndex { object, index ->				
				fields.each { field ->
					String value = getValue(object, field)?.toString()
					PdfPCell cell = new PdfPCell(new Paragraph(value, text))
					if(index % 2 == 0){
						cell.setBackgroundColor(separatorColor)
					}
					cell.setBorderColor(borderColor)
					cell.setMinimumHeight(minimumCellHeight)
					table.addCell(cell)
				}
			}

			document.add(table)
			document.close()
		}
		catch(Exception e){
			throw new ExportingException("Error during export", e)
		}
	}
	
	// Create font from parameters
	private Font createFont(String type, String family, String encoding, int fontSize, int style){
		// Font size
		if(getParameters().containsKey(type + ".font.size")){
			try {
				fontSize = new Integer(getParameters().get(type + ".font.size"))	
			}
			catch(Exception e){
				println e
			}
		}
		
		// Font family
		if(getParameters().containsKey(type + ".font.family")){
			family = getParameters().get(type + ".font.family")
		}
		
		// Font style
		if(getParameters().containsKey(type + ".font.style")){
			try {
				style = fontStyles[getParameters().get(type + ".font.style")]
			}
			catch(Exception e){
				println e
			}
		}
		
		return FontFactory.getFont(family, encoding, fontSize, style) 
	}
}