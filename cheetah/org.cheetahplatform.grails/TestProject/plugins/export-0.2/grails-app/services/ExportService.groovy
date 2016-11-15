import de.andreasschmitt.export.exporter.Exporter
import de.andreasschmitt.export.exporter.ExportingException

class ExportService {

    boolean transactional = true
    
    def exporterFactory

    public void export(String type, OutputStream outputStream, List objects, Map formatters, Map parameters) throws ExportingException {
    	export(type, outputStream, objects, null, null, formatters, parameters)
    }
    
    public void export(String type, OutputStream outputStream, List objects, List fields, Map labels, Map formatters, Map parameters) throws ExportingException {
    	Exporter exporter = exporterFactory.createExporter(type, fields, labels, formatters, parameters)
    	exporter.export(outputStream, objects)
    }
}
