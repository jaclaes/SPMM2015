package models;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.cheetahplatform.common.AssertionFailedException;
import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.common.logging.db.DatabaseUtil;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.ModelerConstants;
import org.cheetahplatform.modeler.engine.AbstractModelingActivity;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;

import com.mysql.jdbc.Driver;


public class DBInput {
	private Connection connection;
	private HashMap<String, Graph> graphs;
		
	
	public DBInput() throws SQLException
	{
		new Driver();
		connection = DriverManager.getConnection("jdbc:mysql://localhost/novices_versus_experts", "graph", "graph");
		graphs = new HashMap<String, Graph>();
	}
	
	
	
	public void selectGraphs() throws Exception
	{
		ResultSet resultSet = connection.createStatement().executeQuery("select database_id from process_instance");
		while (resultSet.next()) {
			try	{
				long databaseId = resultSet.getLong("database_id");
				ResultSet resultSet3 = connection.createStatement().executeQuery("select timestamp, type, workflow_element, originator, data from audittrail_entry where process_instance = " +	databaseId + " order by database_id");
				ProcessInstance instance = new ProcessInstance();
				while (resultSet3.next()) {
					long timestamp = resultSet3.getLong(1);
					String type = resultSet3.getString(2);
					String workflowElement = resultSet3.getString(3);
					String originator = resultSet3.getString(4);
					String rawData = resultSet3.getString(5);
					List<Attribute> data = DatabaseUtil.fromDataBaseRepresentation(rawData);
		
					AuditTrailEntry entry = new AuditTrailEntry(new	Date(timestamp), type, workflowElement, originator, data);
					instance.addEntry(entry);
				}
				ResultSet resultSet2 = connection.createStatement().executeQuery(
									"select data, id from process_instance where database_id="+databaseId);
				
				resultSet2.next();
				String data = resultSet2.getString(1);
				//if(data.contains("task1_1"))	{
					List<Attribute> attributes = DatabaseUtil.fromDataBaseRepresentation(data);
					instance.addAttributes(attributes);
					instance.setId(resultSet2.getString(2));
			
					if (!instance.isAttributeDefined(ModelerConstants.ATTRIBUTE_TYPE))
						continue;
					
					String type = instance.getAttribute(ModelerConstants.ATTRIBUTE_TYPE);
					if(!type.equals("BPMN"))	
						continue;
									
					Graph graph = new Graph(EditorRegistry.getDescriptors(type));
					try	{
						AbstractModelingActivity.restoreGraph(graph, instance);
					}catch(NullPointerException e)	{
						continue;
					}
						
					//skip graph which contains no nodes
					if(graph.getNodes().size() == 0)	
						continue;
					
					graphs.put("Graph ID " + databaseId, graph);
				//}
			}catch(AssertionFailedException ex)	{
				System.out.println(ex);
			}
		}
	}
	
	public HashMap<String, Graph> getGraphs()
	{
		try {
			selectGraphs();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		//remove graphs from list where source or target of edges is null
		ArrayList<String> removeIds = new ArrayList<String>();
		Set<String> keys = graphs.keySet();
		Iterator<String> it = keys.iterator();
		while(it.hasNext())     {
            String id = it.next();
            Graph g = graphs.get(id);

            for(Edge e : g.getEdges())      {
            	if((e.getSource() == null) || (e.getTarget() == null) || (e.getSource().getDescriptor() == null) || (e.getTarget().getDescriptor() == null))    {
            		removeIds.add(id);
                }
            }
		}
		
		for(String id : removeIds)
			graphs.remove(id);

		return graphs;
	}
}
