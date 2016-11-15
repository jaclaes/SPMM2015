package org.cheetahplatform.tdm;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.TransferData;

/**
 * This class can be used to transfer objects of Type T via Drag and Drop. <br>
 * <br>
 * 
 * The transferred object is converted to a byte Array and back again.<br>
 * There is always a unique Transfer type for each Class. So the <code>CustomTransfer.getInstance(Class clazz)</code> method needs a class
 * to create the correct CustomTransfer.
 * 
 * @author Stefan Zugal (csae7511@uibk.ac.at)<br>
 * <br>
 *         Date: 09.11.2005
 */
public class CustomTransfer extends ByteArrayTransfer {
	// The only instance of CustomTransfer
	private static Map<Class<?>, CustomTransfer> INSTANCES = new HashMap<Class<?>, CustomTransfer>();

	/**
	 * Returns the only CustomTransfer for given class.
	 * 
	 * @param The
	 *            class for which the Transfer object should be returned.
	 * @return The only instance for given class.
	 */
	public static CustomTransfer getInstance(Class<?> clazz) {
		CustomTransfer transfer = INSTANCES.get(clazz);

		// if there is no transfer type yet, create one
		if (transfer == null) {
			transfer = new CustomTransfer(clazz);
			INSTANCES.put(clazz, transfer);
		}

		return transfer;
	}

	private int id;

	private String type;

	private CustomTransfer(Class<?> clazz) {
		type = clazz.toString() + System.currentTimeMillis();
		id = registerType(type);
	}

	
	@Override
	protected int[] getTypeIds() {
		return new int[] { id };
	}

	
	@Override
	protected String[] getTypeNames() {
		return new String[] { type };
	}

	
	@Override
	protected void javaToNative(Object object, TransferData transferData) {
		try {
			// convert object to byte array
			ByteArrayOutputStream bytes = new ByteArrayOutputStream(1000);
			ObjectOutputStream out = new ObjectOutputStream(bytes);
			out.writeObject(object);

			super.javaToNative(bytes.toByteArray(), transferData);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	@Override
	protected Object nativeToJava(TransferData transferData) {
		byte[] bytes = (byte[]) super.nativeToJava(transferData);

		if (bytes == null)
			return null;

		try {
			// convert byte array back to object
			ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes));

			return in.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
