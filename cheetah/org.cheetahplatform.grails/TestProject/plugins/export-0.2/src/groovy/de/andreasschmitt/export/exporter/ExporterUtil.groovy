package de.andreasschmitt.export.exporter

class ExporterUtil {
	
	private static Map excludes = [hasMany: true, belongsTo: true, searchable: true, __timeStamp: true, 
	                constraints: true, version: true, metaClass: true]
	
	private ExporterUtil(){
		throw new AssertionError()
	}
	
	public static Object getNestedValue(Object domain, String field){
		def subProps = field.split("\\.")
		
		int i = 0
		def lastProp
		for(prop in subProps){
			if(i == 0){
				lastProp = domain."$prop"
			}
			else {
				lastProp = lastProp."$prop"
			}
			i += 1
		}
		
		return lastProp
	}
	
	public static List getFields(Object domain){
		List props = []
		                
		domain?.class?.properties?.declaredFields.each { field ->
			if(!excludes.containsKey(field.name) && !field.name.contains("class\$") && !field.name.startsWith("__timeStamp")){
				props.add(field.name)
			}
		}
		
		props.sort()
		
		return props
	}
	
}