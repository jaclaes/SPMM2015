/**********************************************************************************************
 * Copyright 2009 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file 
 * except in compliance with the License. A copy of the License is located at
 *
 *       http://aws.amazon.com/apache2.0/
 *
 * or in the "LICENSE.txt" file accompanying this file. This file is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under the License. 
 *
 * ********************************************************************************************
 *
 *  Amazon Product Advertising API
 *  Signed Requests Sample Code
 *
 *  API Version: 2009-03-31
 *
 */

package at.floxx.scrumify.amazonWebService.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import at.floxx.scrumify.amazonWebService.helper.SignedRequestsHelper;

/*
 * This class shows how to make a simple authenticated ItemLookup call to the
 * Amazon Product Advertising API.
 * 
 * See the README.html that came with this sample for instructions on
 * configuring and running the sample.
 */
public class DataModel {
	
	public static void main(String[] args) {
		getScrumBooks();
	}
    /*
     * Your AWS Access Key ID, as taken from the AWS Your Account page.
     */
    private static final String AWS_ACCESS_KEY_ID = "AKIAIQ7DIDPED3ZYO65A";

    /*
     * Your AWS Secret Key corresponding to the above ID, as taken from the AWS
     * Your Account page.
     */
    private static final String AWS_SECRET_KEY = "o8651N+YaK2DpQFgA01N9/8qN8v079ZlK3c7nxwC";

    /*
     * Use one of the following end-points, according to the region you are
     * interested in:
     * 
     *      US: ecs.amazonaws.com 
     *      CA: ecs.amazonaws.ca 
     *      UK: ecs.amazonaws.co.uk 
     *      DE: ecs.amazonaws.de 
     *      FR: ecs.amazonaws.fr 
     *      JP: ecs.amazonaws.jp
     * 
     */
    private static final String ENDPOINT = "ecs.amazonaws.com";

    /*
     * The Item ID to lookup. The value below was selected for the US locale.
     * You can choose a different value if this value does not work in the
     * locale of your choice.
     */
//    private static final String ITEM_ID = "0545010225";
    public static List<List<String>> getScrumBooks() {
    	return getScrumBooks("Scrum");
    }
    
    public static List<List<String>> getScrumBooks(String searchKeyWord) {
        /*
         * Set up the signed requests helper 
         */
        SignedRequestsHelper helper;
        try {
            helper = SignedRequestsHelper.getInstance(ENDPOINT, AWS_ACCESS_KEY_ID, AWS_SECRET_KEY);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
        String requestUrl = null;
        String title = null;

        /* The helper can sign requests in two forms - map form and string form */
        
        /*
         * Here is an example in map form, where the request parameters are stored in a map.
         */
//        System.out.println("Map form example:");
        Map<String, String> params = new HashMap<String, String>();
        params.put("Service", "AWSECommerceService");
        params.put("Version", "2009-03-31");
//        params.put("Operation", "ItemLookup");
//        params.put("ItemId", ITEM_ID);
//        params.put("ResponseGroup", "Small");
        params.put("Operation", "ItemSearch");
        params.put("SearchIndex", "Books");
        params.put("Title", searchKeyWord);

        requestUrl = helper.sign(params);
//        System.out.println("Signed Request is \"" + requestUrl + "\"");

//        title = fetchTitle(requestUrl);
//        System.out.println("Signed Title is \"" + title + "\"");
//        System.out.println();

        return fetchBooks(requestUrl);



    }

    /*
     * Utility function to fetch the response from the service and extract the
     * title from the XML.
     */
    @SuppressWarnings("unused")
	private static String fetchTitle(String requestUrl) {
        String title = null;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(requestUrl);
            Node titleNode = doc.getElementsByTagName("Title").item(0);
            title = titleNode.getTextContent();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return title;
    }
    
    /*
     * Utility function to fetch the response from the service and extract the
     * title from the XML.
     */
    private static List<List<String>> fetchBooks(String requestUrl) {
        String title = null;
        List<List<String>> result = new ArrayList<List<String>>();
        
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(requestUrl);
            NodeList foundItems = doc.getElementsByTagName("Item");
            for(int i = 0; i < foundItems.getLength(); i++) {
            	Node item = foundItems.item(i);
            	NodeList attributes = item.getChildNodes();
            	List<String> bookAttributes = new ArrayList<String>();
//            	bookAttributes.add(attributes.item(3).getTextContent());
//            	bookAttributes.add(attributes.item(1).getTextContent());


//            	System.out.println("Item: " + i);
//            	for(int j = 0; j < attributes.getLength(); j++) {
//            		System.out.println("  Attribute: " + j + ", Context: " + attributes.item(j).getTextContent());
//            		System.out.println("  Nr of Child Nodes: " + attributes.item(j).getChildNodes().getLength());
////            		
            		NodeList childNodes = attributes.item(3).getChildNodes();
            		String previousNode = null;
            		for(int k = 0; k < childNodes.getLength(); k++) {
//            			System.out.println("	->ChildNode: " + k + ", Context of childnodes: " + childNodes.item(k).getTextContent());
//            			System.out.println("	->ChildNode: " + k + ", Name: " + childNodes.item(k).getNodeName());
            			if(childNodes.item(k).getNodeName().equals(previousNode)) {
            				bookAttributes.get(bookAttributes.size()-1).concat(", " + childNodes.item(k).getTextContent());
            			}
            			else {
            				previousNode = childNodes.item(k).getNodeName();
            				bookAttributes.add(childNodes.item(k).getTextContent());
            			}
//            		}
            	}

            	bookAttributes.add(attributes.item(1).getTextContent());
            	result.add(bookAttributes);
//            	System.out.println(item.getChildNodes().getLength());
            }
            
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

}