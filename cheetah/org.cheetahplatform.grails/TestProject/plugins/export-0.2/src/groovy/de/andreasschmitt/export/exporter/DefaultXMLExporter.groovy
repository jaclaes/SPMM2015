package de.andreasschmitt.export.exporter

import groovy.xml.MarkupBuilder

class DefaultXMLExporter extends AbstractExporter {
	
	protected void exportData(OutputStream outputStream, List data, List fields) throws ExportingException{
		try {
			Writer writer = new OutputStreamWriter(outputStream)
			def builder = new MarkupBuilder(writer)
			
			if(data.size() > 0){
				build("${properCase(data[0]?.class?.simpleName)}s", builder, data, fields, 1)
			}
			
			writer.flush()
		}
		catch(Exception e){
			throw new ExportingException("Error during export", e)
		}
	}
	
	private String properCase(String value){
		if(value?.length() >= 2){
			return "${value[0].toLowerCase()}${value.substring(1)}"	
		}
		
		return value?.toLowerCase()
	}
	
	private void build(String node, builder, Collection data, List fields, int depth){
		if(depth >= 0 && data.size() > 0){
			//Root element
			builder."${properCase(node)}"{
				//Iterate through data
				data.each { object ->
					//Object element
					"${properCase(object?.class?.simpleName)}"(id: object?.id){
						//Object attributes
						fields.each { field ->
							String elementName = getLabel(field)
							
							Object value = getValue(object, field)
							
							if(value instanceof Set){
								if(value.size() > 0){
									this.build(field, builder, value, ExporterUtil.getFields(value.toArray()[0]), depth - 1)	
								}
								else {
									"${elementName}"()	
								}
							}
							else {
								"${elementName}"(value?.toString())	
							}
						}	
					}
					
				}
				
			}
		}
	}
}