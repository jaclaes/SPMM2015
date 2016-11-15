package org.cheetahplatform.modeler.graph.mapping;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.List;

import org.cheetahplatform.common.Activator;
import org.cheetahplatform.common.logging.db.IDatabaseConnector;

import com.thoughtworks.xstream.XStream;

public class MappingConverter {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws FileNotFoundException, SQLException {
		IDatabaseConnector connector = Activator.getDatabaseConnector();
		connector.setAdminCredentials("root", "mysql");
		connector.setDatabaseURL("jdbc:mysql://localhost/ibm_layout");

		XStream xStream = new XStream();
		List<Paragraph> fromXML = (List<Paragraph>) xStream.fromXML(new FileInputStream("C:\\input.xml"));

		for (Paragraph paragraph : fromXML) {
			ParagraphProvider.addParagraph(paragraph);
		}

		ParagraphProvider.save();
	}

}
