package at.floxx.scrumify.coreObjectsProvider.persistency;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Helper class to do basic file persistency, where Objects are getting binary
 * serialized.
 * 
 * @author mathias
 * 
 */
public class FilePersistency {
	/**
	 * Save an Object to a file located at path.
	 * 
	 * @param obj
	 * @param path
	 * @throws IOException
	 */
	public static void save(Object obj, String path) throws IOException {
		File file = new File(path);
		file.createNewFile();
		FileOutputStream out = new FileOutputStream(file);
		ObjectOutputStream s = new ObjectOutputStream(out);
		s.writeObject(obj);
		s.flush();

	}

	/**
	 * Load an Object from a file located at path.
	 * 
	 * @param path
	 * @return Object.
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Object read(String path) throws IOException,
			ClassNotFoundException {
		FileInputStream in = new FileInputStream(path);
		ObjectInputStream sin = new ObjectInputStream(in);
		return sin.readObject();
	}

}
