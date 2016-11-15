package org.cheetahplatform.recommendation;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Vector;

import pmsedit.logmodel.Process;
import pmsedit.logmodel.input.LogmodelParser;



/**
 * The representation of the log for the recommendation engine.
 * 
 * @author Christian Haisjackl
 */
public class Log {
	private List<Process> log;



	/**
	 * Creating an empty log
	 */
	public Log() {
		log = new Vector<Process>();
	}



	/**
	 * This reads a log
	 * 
	 * @param input The log file
	 * @throws FileNotFoundException If the specified file does not exist
	 */
	public Log( File input ) throws FileNotFoundException {
		this( new FileInputStream( input ) );
	}



	/**
	 * Reads the log
	 * 
	 * @param input The log file as input stream
	 */
	public Log( InputStream input ) {
		try {
			log = new LogmodelParser( input ).parse();
		} catch( Exception e ) {
			throw new Error( e );
		}
	}



	/**
	 * This method returns the read log.
	 * 
	 * @return The log
	 */
	public List<Process> getLog() {
		return log;
	}



	/**
	 * This method adds a single process to the log
	 * 
	 * @param process The process to add
	 */
	public void addProcess( Process process ) {
		log.add( process );
	}



	/**
	 * This method adds an additional log to the current log.
	 * 
	 * @param l
	 */
	public void addLog( Log l ) {
		log.addAll( l.getLog() );
	}
}
