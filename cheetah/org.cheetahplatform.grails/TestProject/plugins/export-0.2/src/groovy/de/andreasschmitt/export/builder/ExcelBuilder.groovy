package de.andreasschmitt.export.builder

import groovy.util.BuilderSupport
import jxl.write.WritableFont
import jxl.Workbook
import jxl.write.Label
import jxl.write.Number
import jxl.write.WritableCellFormat
import jxl.write.WritableFont
import jxl.write.WritableSheet
import jxl.write.WritableWorkbook
import jxl.write.WriteException
import jxl.write.biff.RowsExceededException
import jxl.write.WritableFont
import jxl.format.UnderlineStyle
import jxl.format.UnderlineStyleimport jxl.write.WritableCellFormat
import jxl.write.biff.CellValueimport jxl.write.WritableFont
import org.apache.commons.logging.*

/*
 * workbook(outputStream: outputStream){
 * 
 *     sheet(name: "Sheet1"){
 *     	   format(name: "format1"){
 *             font(name: "Arial", size: 10, bold: true, underline: "single", italic: true)    
 *     	   }
 *     
 *         cell(row: 0, column: 0, value: "Hello1")
 *         cell(row: 0, column: 1, value: "Hello2")
 *     }
 *     
 *     sheet(name: "Sheet2"){
 *     
 *     }
 * }
 * 
 */

class ExcelBuilder extends BuilderSupport {

	WritableWorkbook workbook
	WritableSheet sheet
	
	String format
	Map formats = [:]

	private static Log log = LogFactory.getLog(ExcelBuilder)

    protected void setParent(Object parent, Object child) {
    }

    protected Object createNode(Object name) {
    	log.debug("createNode(Object name)")
    	log.debug("name: ${name}")
    	
    	if(name == "write"){
    		this.write()
    	}
    	
        return null
    }

    protected Object createNode(Object name, Object value) {
    	log.debug("createNode(Object name, Object value)")
    	log.debug("name: ${name} value: ${value}")
        return null
    }

    protected Object createNode(Object name, Map attributes) {
    	log.debug("createNode(Object name, Map attributes)")
    	log.debug("name: ${name} attributes: ${attributes}")
    	
    	switch(name){
    		case "workbook":
    			if(attributes?.outputStream){
    		    	try {
    		    		log.debug("Creating workbook")
    		        	workbook = Workbook.createWorkbook(attributes?.outputStream)	
    		    	}
    		    	catch(Exception e){
    		    		log.error("Error creating workbook", e)
    		    	}
    			}
    			break
    			
    		case "sheet":
    			try {
        			log.debug("Creating sheet")
        			sheet = workbook.createSheet(attributes?.name, workbook.getNumberOfSheets())	
    			}
    			catch(Exception e){
    				log.error("Error creating sheet", e)
    			}
    			break
    			
    		case "cell":
    			try {
    				CellValue value
    				
        			if(attributes?.value instanceof java.lang.Number){
        				log.debug("Creating number cell")
        				value = new Number(attributes?.column, attributes?.row, attributes?.value)
        			}
        			else {
        				log.debug("Creating label cell")
        				value = new Label(attributes?.column, attributes?.row, attributes?.value?.toString())
        			}

    				if(attributes?.format && formats.containsKey(attributes?.format)){
    					value.setCellFormat(formats[attributes.format])
    				}
    				
    				sheet.addCell(value)
    			}
    			catch(Exception e){
    				log.error("Error adding cell with attributes: ${attributes}", e)
    			}
    			break
    			
    		case "format":
    			if(attributes?.name){
    				format = attributes?.name
    			}
    			break
    		
    		case "font":
    			try {
    				log.debug("attributes: ${attributes}")
    				
        			attributes.name = attributes?.name ? attributes?.name : "arial"
        	    	attributes.italic = attributes?.italic ? attributes?.italic : false
        	    	attributes.bold = attributes?.bold ? attributes?.bold : "false"
        	    	attributes["size"] = attributes["size"] ? attributes["size"] : WritableFont.DEFAULT_POINT_SIZE
        	    	attributes.underline = attributes?.underline ? attributes?.underline : "none"
        	    			
        	    	Map bold = ["true": WritableFont.BOLD, "false": WritableFont.NO_BOLD]		
        	    	if(bold.containsKey(attributes.bold.toString())){
        	    		attributes.bold = bold[attributes?.bold.toString()]	
        	    	}
        	    			
        	    	Map underline = ["none": UnderlineStyle.NO_UNDERLINE, "double accounting": UnderlineStyle.DOUBLE_ACCOUNTING,
        	    			         "single": UnderlineStyle.SINGLE, "single accounting": UnderlineStyle.SINGLE_ACCOUNTING]
        	    	if(underline.containsKey(attributes.underline)){
        	    		attributes.underline = underline[attributes.underline]
        	    	}
        	    	
        	    	Map fontname = ["arial":  WritableFont.ARIAL, "courier": WritableFont.COURIER, 
        	    	            "tahoma":  WritableFont.TAHOMA, "times":  WritableFont.TIMES]
        	    	if(fontname.containsKey(attributes.name)){
        	    		attributes.name = fontname[attributes.name]
        	    	}
        	    	
        	    	log.debug("attributes: ${attributes}")
        	    			
        	    	WritableFont font = new WritableFont(attributes.name, attributes["size"], attributes.bold, attributes.italic, attributes.underline) 
        	    	WritableCellFormat cellFormat = new WritableCellFormat(font)
        	    	formats.put(format, cellFormat)	
    			}
    			catch(Exception e){
    				
    			}
    			break
    	}
    	
        return null
    }

    protected Object createNode(Object name, Map attributes, Object value) {
    	log.debug("createNode(Object name, Map attributes, Object value)")
    	log.debug("name: ${name} attributes: ${attributes}, value: ${value}")
        return null
    }
    
    public void write(){
    	log.debug("Writing document")
    	
    	try {
        	workbook.write()
    		workbook.close()	
    	}
    	catch(Exception e){
    		log.error("Error writing document", e)
    	}
    }
    
}