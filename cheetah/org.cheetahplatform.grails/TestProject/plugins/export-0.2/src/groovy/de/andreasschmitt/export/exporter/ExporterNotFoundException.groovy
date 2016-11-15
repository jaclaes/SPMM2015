package de.andreasschmitt.export.exporter

class ExporterNotFoundException extends Exception {

	public ExporterNotFoundException(){
		super()
	}
	
	public ExporterNotFoundException(String message){
		super(message)
	}
	
	public ExporterNotFoundException(Throwable throwable){
		super(throwable)
	}
	
	public ExporterNotFoundException(String message, Throwable throwable){
		super(message, throwable)
	}

}