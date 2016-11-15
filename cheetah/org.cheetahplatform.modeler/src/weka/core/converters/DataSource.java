/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package weka.core.converters;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;

import weka.core.Instance;
import weka.core.Instances;
import weka.core.RevisionHandler;
import weka.core.RevisionUtils;

/**
 * Helper class for loading data from files and URLs. Via the ConverterUtils class it determines which converter to use for loading the data
 * into memory. If the chosen converter is an incremental one, then the data will be loaded incrementally, otherwise as batch. In both cases
 * the same interface will be used (<code>hasMoreElements</code>, <code>nextElement</code>). Before the data can be read again, one has to
 * call the <code>reset</code> method. The data source can also be initialized with an Instances object, in order to provide a unified
 * interface to files and already loaded datasets.
 * 
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @version $Revision: 6416 $
 * @see #hasMoreElements(Instances)
 * @see #nextElement(Instances)
 * @see #reset()
 * @see DataSink
 */
public class DataSource implements Serializable, RevisionHandler {

	/** for serialization. */
	private static final long serialVersionUID = -613122395928757332L;

	/**
	 * returns whether the extension of the location is likely to be of ARFF format, i.e., ending in ".arff" or ".arff.gz"
	 * (case-insensitive).
	 * 
	 * @param location
	 *            the file location to check
	 * @return true if the location seems to be of ARFF format
	 */
	public static boolean isArff(String location) {
		if (location.toLowerCase().endsWith(ArffLoader.FILE_EXTENSION.toLowerCase())
				|| location.toLowerCase().endsWith(ArffLoader.FILE_EXTENSION_COMPRESSED.toLowerCase()))
			return true;
		return false;
	}

	/**
	 * convencience method for loading a dataset in batch mode from a stream.
	 * 
	 * @param stream
	 *            the stream to load the dataset from
	 * @return the dataset
	 * @throws Exception
	 *             if loading fails
	 */
	public static Instances read(InputStream stream) throws Exception {
		DataSource source;
		Instances result;

		source = new DataSource(stream);
		result = source.getDataSet();

		return result;
	}

	/**
	 * convencience method for loading a dataset in batch mode.
	 * 
	 * @param loader
	 *            the loader to get the dataset from
	 * @return the dataset
	 * @throws Exception
	 *             if loading fails
	 */
	public static Instances read(Loader loader) throws Exception {
		DataSource source;
		Instances result;

		source = new DataSource(loader);
		result = source.getDataSet();

		return result;
	}

	/** the file to load. */
	protected File m_File;

	/** the URL to load. */
	protected URL m_URL;

	/** the loader. */
	protected Loader m_Loader;

	/** whether the loader is incremental. */
	protected boolean m_Incremental;

	/** the instance counter for the batch case. */
	protected int m_BatchCounter;

	/** the last internally read instance. */
	protected Instance m_IncrementalBuffer;

	/** the batch buffer. */
	protected Instances m_BatchBuffer;

	/**
	 * Initializes the datasource with the given input stream. This stream is always interpreted as ARFF.
	 * 
	 * @param stream
	 *            the stream to use
	 */
	public DataSource(InputStream stream) {
		super();

		m_BatchBuffer = null;
		m_Loader = new ArffLoader();
		try {
			m_Loader.setSource(stream);
		} catch (Exception e) {
			m_Loader = null;
		}
		m_File = null;
		m_URL = null;
		m_Incremental = (m_Loader instanceof IncrementalConverter);

		initBatchBuffer();
	}

	/**
	 * Initializes the datasource with the given dataset.
	 * 
	 * @param inst
	 *            the dataset to use
	 */
	public DataSource(Instances inst) {
		super();

		m_BatchBuffer = inst;
		m_Loader = null;
		m_File = null;
		m_URL = null;
		m_Incremental = false;
	}

	/**
	 * Initializes the datasource with the given Loader.
	 * 
	 * @param loader
	 *            the Loader to use
	 */
	public DataSource(Loader loader) {
		super();

		m_BatchBuffer = null;
		m_Loader = loader;
		m_File = null;
		m_URL = null;
		m_Incremental = (m_Loader instanceof IncrementalConverter);

		initBatchBuffer();
	}

	/**
	 * returns the full dataset, can be null in case of an error.
	 * 
	 * @return the full dataset
	 * @throws Exception
	 *             if resetting of loader fails
	 */
	public Instances getDataSet() throws Exception {
		Instances result;

		result = null;

		// reset the loader
		reset();

		try {
			if (m_BatchBuffer == null)
				result = m_Loader.getDataSet();
			else
				result = m_BatchBuffer;
		} catch (Exception e) {
			e.printStackTrace();
			result = null;
		}

		return result;
	}

	/**
	 * returns the full dataset with the specified class index set, can be null in case of an error.
	 * 
	 * @param classIndex
	 *            the class index for the dataset
	 * @return the full dataset
	 * @throws Exception
	 *             if resetting of loader fails
	 */
	public Instances getDataSet(int classIndex) throws Exception {
		Instances result;

		result = getDataSet();
		if (result != null)
			result.setClassIndex(classIndex);

		return result;
	}

	/**
	 * returns the determined loader, null if the DataSource was initialized with data alone and not a file/URL.
	 * 
	 * @return the loader used for retrieving the data
	 */
	public Loader getLoader() {
		return m_Loader;
	}

	/**
	 * Returns the revision string.
	 * 
	 * @return the revision
	 */
	@Override
	public String getRevision() {
		return RevisionUtils.extract("$Revision: 6416 $");
	}

	/**
	 * returns the structure of the data.
	 * 
	 * @return the structure of the data
	 * @throws Exception
	 *             if something goes wrong
	 */
	public Instances getStructure() throws Exception {
		if (m_BatchBuffer == null)
			return m_Loader.getStructure();
		return new Instances(m_BatchBuffer, 0);
	}

	/**
	 * returns the structure of the data, with the defined class index.
	 * 
	 * @param classIndex
	 *            the class index for the dataset
	 * @return the structure of the data
	 * @throws Exception
	 *             if something goes wrong
	 */
	public Instances getStructure(int classIndex) throws Exception {
		Instances result;

		result = getStructure();
		if (result != null)
			result.setClassIndex(classIndex);

		return result;
	}

	/**
	 * returns whether there are more Instance objects in the data.
	 * 
	 * @param structure
	 *            the structure of the dataset
	 * @return true if there are more Instance objects available
	 * @see #nextElement(Instances)
	 */
	public boolean hasMoreElements(Instances structure) {
		boolean result;

		result = false;

		if (isIncremental()) {
			// user still hasn't collected the last one?
			if (m_IncrementalBuffer != null) {
				result = true;
			} else {
				try {
					m_IncrementalBuffer = m_Loader.getNextInstance(structure);
					result = (m_IncrementalBuffer != null);
				} catch (Exception e) {
					e.printStackTrace();
					result = false;
				}
			}
		} else {
			result = (m_BatchCounter < m_BatchBuffer.numInstances());
		}

		return result;
	}

	/**
	 * initializes the batch buffer if necessary, i.e., for non-incremental loaders.
	 */
	protected void initBatchBuffer() {
		try {
			if (!isIncremental())
				m_BatchBuffer = m_Loader.getDataSet();
			else
				m_BatchBuffer = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * returns whether the loader is an incremental one.
	 * 
	 * @return true if the loader is a true incremental one
	 */
	public boolean isIncremental() {
		return m_Incremental;
	}

	/**
	 * returns the next element and sets the specified dataset, null if none available.
	 * 
	 * @param dataset
	 *            the dataset to set for the instance
	 * @return the next Instance
	 */
	public Instance nextElement(Instances dataset) {
		Instance result;

		result = null;

		if (isIncremental()) {
			// is there still an instance in the buffer?
			if (m_IncrementalBuffer != null) {
				result = m_IncrementalBuffer;
				m_IncrementalBuffer = null;
			} else {
				try {
					result = m_Loader.getNextInstance(dataset);
				} catch (Exception e) {
					e.printStackTrace();
					result = null;
				}
			}
		} else {
			if (m_BatchCounter < m_BatchBuffer.numInstances()) {
				result = m_BatchBuffer.instance(m_BatchCounter);
				m_BatchCounter++;
			}
		}

		if (result != null) {
			result.setDataset(dataset);
		}

		return result;
	}

	/**
	 * resets the loader.
	 * 
	 * @throws Exception
	 *             if resetting fails
	 */
	public void reset() throws Exception {
		if (m_File != null)
			((AbstractFileLoader) m_Loader).setFile(m_File);
		else if (m_URL != null)
			((URLSourcedLoader) m_Loader).setURL(m_URL.toString());
		else if (m_Loader != null)
			m_Loader.reset();

		m_BatchCounter = 0;
		m_IncrementalBuffer = null;

		if (m_Loader != null) {
			if (!isIncremental())
				m_BatchBuffer = m_Loader.getDataSet();
			else
				m_BatchBuffer = null;
		}
	}
}