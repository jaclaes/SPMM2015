package org.cheetahplatform.recommendation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.cheetahplatform.recommendation.message.MsgAddProcessLog;
import org.cheetahplatform.recommendation.message.MsgBoolean;
import org.cheetahplatform.recommendation.message.MsgGetRecommendation;
import org.cheetahplatform.recommendation.message.MsgRecommendationResult;
import org.cheetahplatform.recommendation.message.MsgSetMapper;
import org.cheetahplatform.recommendation.message.MsgSetStrategy;

import pmsedit.logmodel.LogEntry;
import pmsedit.logmodel.Process;
import pmsedit.logmodel.ProcessInstance;

/**
 * This class is the server for communicating with a PAIS.
 * 
 * @author Christian Haisjackl
 */
public class RecoServer {
	// /**
	// * @author Christian Haisjackl
	// */
	// private class RequestHandler extends Thread {
	// private Socket client;
	//
	// private RequestHandler(Socket client) {
	// this.client = client;
	// }
	//
	// /**
	// * @see java.lang.Thread#run()
	// */
	// @Override
	// public void run() {
	// try {
	// ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(client.getInputStream()));
	// ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(client.getOutputStream()));
	// out.flush();
	//
	// while (active) {
	// try {
	// Object msg = in.readObject();
	// if (msg instanceof MsgGetRecommendation)
	// msg = getRecommendation((MsgGetRecommendation) msg);
	// else if (msg instanceof MsgAddProcessLog)
	// msg = addProcessLog((MsgAddProcessLog) msg);
	// else if (msg instanceof MsgSetStrategy)
	// msg = setStrategy((MsgSetStrategy) msg);
	// else if (msg instanceof MsgSetMapper)
	// msg = setActNamesAndIDs((MsgSetMapper) msg);
	// else if (msg instanceof MsgShutDown)
	// shutDown();
	// out.writeObject(msg);
	// out.flush();
	// } catch (EOFException eofe) {
	// System.out.println("Client Disconnected - " + client.getInetAddress() + ":" + client.getPort());
	// break;
	// }
	// }
	// in.close();
	// out.close();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// try {
	// client.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// }

	private Strategy strategy;
	private String eventState;
	private TargetFunction targetFunction;
	private HashMap<Long, LogEntry> activities;
	private File logFile;

	private Log log;

	private Mapper mapper;

	/**
	 * This constructor starts the server.
	 * 
	 * @param logFilePath
	 *            The path to the log file
	 */
	public RecoServer(String logFilePath) {
		System.out.println("Startup Server at port ");
		this.logFile = new File(logFilePath);
		System.out.println("Server started up using " + logFilePath);
	}

	public MsgBoolean addProcessLog(MsgAddProcessLog msg) {
		System.out.println("RecoServer.addProcessLog");
		String logFilePath;
		if (logFile.isDirectory())
			logFilePath = logFile.getAbsolutePath();
		else
			logFilePath = logFile.getParentFile().getAbsolutePath();

		File f = new File(logFilePath + File.separator + msg.getLogname());
		// creating a file that does not exist
		int i = 0;
		while (f.exists()) {
			f = new File(logFilePath + File.separator + i + msg.getLogname());
			i++;
		}

		try {
			FileWriter fos = new FileWriter(f);
			BufferedWriter bw = new BufferedWriter(fos);
			bw.write(msg.getLog());
			bw.flush();
			bw.close();
			fos.close();

			Log tmp;
			if (mapper != null)
				tmp = new Log(mapper.map(f));
			else
				tmp = new Log(f);

			for (Process p : tmp.getLog()) {
				for (ProcessInstance pi : p.getProcessInstances()) {
					for (LogEntry le : pi.getLogEntries()) {
						if (le.getEventType().getName().equals(eventState)) {
							if (!activities.containsKey(new Long(le.getTask())))
								activities.put(new Long(le.getTask()), le);
						}
					}
				}
			}

			log.addLog(tmp);
			strategy.addTraces(tmp, targetFunction, eventState);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new MsgBoolean(true);
	}

	/**
	 * @return The event state of the tasks for recommendations
	 */
	public String getEventState() {
		return eventState;
	}

	/**
	 * @return The log
	 */
	public Log getLog() {
		return log;
	}

	public MsgRecommendationResult getRecommendation(MsgGetRecommendation msg) {
		List<LogEntry> l = new Vector<LogEntry>();
		List<LogEntry> nt = new Vector<LogEntry>();
		System.out.println("RecoServer.getRecommendation() - " + msg.getActivties());
		for (Long actID : msg.getActivties())
			l.add(activities.get(actID));
		for (Long nextTask : msg.getPossibleTasks())
			nt.add(activities.get(nextTask));

		Trace partialTrace = new Trace(l, targetFunction);
		RecommendationResultList rrl = strategy.getRecommendations(partialTrace, nt);
		List<List<Long>> traces = new Vector<List<Long>>();
		List<List<Double>> doDonts = new Vector<List<Double>>();

		for (RecommendationResult rr : rrl.getResults()) {
			List<Long> tasks = new Vector<Long>();
			for (LogEntry le : rr.getTrace().getEntries())
				tasks.add(new Long(le.getTask()));
			List<Double> dd = new Vector<Double>();
			dd.add(rr.getDoValue());
			dd.add(rr.getDontValue());
			traces.add(tasks);
			doDonts.add(dd);
		}

		return new MsgRecommendationResult(traces, doDonts);
	}

	public MsgBoolean setActNamesAndIDs(MsgSetMapper msg) {
		System.out.println("RecoServer.setActNamesAndIDs");
		try {
			mapper = Mapper.getInstance(msg.getMapper(), msg.getActor(), msg.getNames(), msg.getIds(), msg.getAdditionalAttributes());
			return new MsgBoolean(true);
		} catch (Exception e) {
			e.printStackTrace();
			return new MsgBoolean(false);
		}
	}

	public MsgBoolean setStrategy(MsgSetStrategy msg) {
		System.out.println("RecoServer.setStrategy");
		try {
			targetFunction = new TargetFunction(msg.getTarget(), msg.getCalcMaxValue());
			String thisStrategy = msg.getStrategy();
			eventState = msg.getEventState();
			logFile = (msg.getLogFile().equals("")) ? logFile : new File(msg.getLogFile());

			System.out.println("Setting Logfile/-path: " + logFile);

			// if logfile is directory, read every log in this directory
			if (logFile.isDirectory()) {
				log = new Log();
				for (String lfp : logFile.list()) {
					File lf = new File(logFile.getAbsolutePath() + File.separator + lfp);
					if (lfp.equals(".") || lfp.equals("..") || lf.isDirectory())
						continue;

					if (mapper != null)
						log.addLog(new Log(mapper.map(lf)));
					else
						log.addLog(new Log(lf));
				}
			} else {
				if (mapper != null)
					log = new Log(mapper.map(logFile));
				else
					log = new Log(logFile);
			}

			List<Process> processes = log.getLog();
			System.out.println("Number of Processes in Database: " + processes.size());
			activities = new HashMap<Long, LogEntry>();
			for (Process p : log.getLog()) {
				System.out.println("Process: " + p.getId() + " contains " + p.getProcessInstances().size() + " process instances");
				for (ProcessInstance pi : p.getProcessInstances()) {
					for (LogEntry le : pi.getLogEntries()) {
						if (le.getEventType().getName().equals(eventState)) {
							if (!activities.containsKey(new Long(le.getTask())))
								activities.put(new Long(le.getTask()), le);
						}
					}
				}
			}

			List<Object> additionalParams = msg.getAdditionalParams();
			this.strategy = Strategy.getInstance(thisStrategy, log, targetFunction, eventState, additionalParams);

		} catch (Exception e) {
			e.printStackTrace();
			return new MsgBoolean(false);
		}
		return new MsgBoolean(true);
	}
}
