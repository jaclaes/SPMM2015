package org.cheetahplatform.j2ee.beans;

import java.net.MalformedURLException;
import java.net.URL;

public class Test {

	public static void main(String[] args) {

		try {
			URL url = new URL(
					"file:/C:/Program Files/Java/Glassfish/domains/cheetahplatform/applications/j2ee-apps/org.cheetahplatform.j2ee.applicaton/org.cheetahplatform.j2ee_jar/org/cheetahplatform/j2ee/beans/../../recommendation/training/");

			System.out.println(url.toExternalForm());
			System.out.println(url);
			System.out.println(url.getPath());
			System.out.println(url.getFile());

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
