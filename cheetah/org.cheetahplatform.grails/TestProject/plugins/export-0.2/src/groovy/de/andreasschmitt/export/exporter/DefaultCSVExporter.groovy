package de.andreasschmitt.export.exporter

import au.com.bytecode.opencsv.CSVWriter

class DefaultCSVExporter extends AbstractExporter {
	
	char separator = ';'
	char quoteCharacter = '"'
	
	protected void exportData(OutputStream outputStream, List data, List fields) throws ExportingException{
		try {
			CSVWriter writer = new CSVWriter(new OutputStreamWriter(outputStream), separator, quoteCharacter)
			
			//Create header
			List header = []
			fields.each { field ->
				String value = getLabel(field)
				header.add(value)
			}
			writer.writeNext(header as String[])
			
			//Rows
			data.each { object ->
				List row = []
				
				fields.each { field ->
					String value = getValue(object, field)?.toString()
					row.add(value)
				}
				
				writer.writeNext(row as String[])
			}
			
			writer.flush()
		}
		catch(Exception e){
			throw new ExportingException("Error during export", e)
		}
	}
}