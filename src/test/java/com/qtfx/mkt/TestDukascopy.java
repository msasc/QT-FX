package com.qtfx.mkt;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qtfx.lib.mkt.data.Data;
import com.qtfx.lib.mkt.data.Filter;
import com.qtfx.lib.mkt.data.Instrument;
import com.qtfx.lib.mkt.data.OfferSide;
import com.qtfx.lib.mkt.data.Period;
import com.qtfx.lib.mkt.server.AccountType;
import com.qtfx.lib.mkt.server.DataIterator;
import com.qtfx.lib.mkt.server.HistoryManager;
import com.qtfx.lib.mkt.server.ServerException;
import com.qtfx.lib.mkt.servers.dukascopy.DkCore;
import com.qtfx.lib.mkt.servers.dukascopy.DkServer;

public class TestDukascopy {
	
	/** Logger configuration. */
	static {
		System.setProperty("log4j.configurationFile", "resources/LoggerQTPlatform.xml");
	}
	/** Logger instance. */
	private static final Logger LOGGER = LogManager.getLogger();

	private static final String url = "https://www.dukascopy.com/client/demo/jclient/jforex.jnlp";
	private static String userName = "msasc";
	private static String password = "C1a2r3l4a5";

	
	class Console implements Runnable {

		@Override
		public void run() {
        	Scanner s = new Scanner(System.in);            	
        	while(true){
        		while(s.hasNext()){
            		String str = s.next();
	            	if(str.equalsIgnoreCase("stop")){
	            		try {
							server.getConnectionManager().disconnect();
						} catch (ServerException e) {
							LOGGER.catching(e);
						}
	            		System.out.println("Bye bye.");
	            		System.exit(0);
	            	}
        		}
            	try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
        	}
		}
	}

	public static void main(String[] args) {
		TestDukascopy dk = new TestDukascopy();
		try {
			dk.Test();
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}

	DkServer server;
	private void Test() throws Exception {
		server = new DkServer();
		server.getConnectionManager().connect(userName, password, AccountType.DEMO);
        
		HistoryManager history = server.getHistoryManager();
		Instrument instrument = DkCore.fromDkInstrument(com.dukascopy.api.Instrument.EURUSD);
		Period period = Period.ONE_MIN;
		long time = history.getTimeOfFirstData(instrument, period);
		System.out.println(new Timestamp(time));
		System.out.println("--------------");

		OfferSide offerSide = OfferSide.ASK;
		Filter filter = Filter.ALL_FLATS;
		
		int before = 0;
		int after = 100;
		int retrieved = 0;
//		while (retrieved < 500) {
//			List<Data> list = history.getDataList(instrument, period, offerSide, filter, time, before, after);
//			for (Data data : list) {
//				retrieved++;
//				time = data.getTime();
//				System.out.println(data + " - Retrieved: " + retrieved + " " + (Data.isFlat(data) ? "FLAT" : ""));
//			}
//		}
		
		retrieved = 0;
		DataIterator iter = history.getDataIterator(instrument, period, offerSide, filter, time);
		while (iter.hasNext()) {
			Data data = iter.next();
			retrieved++;
			System.out.println(data + " - Retrieved: " + retrieved + " " + (Data.isFlat(data) ? "FLAT" : ""));
		}
		
		
		new Thread(new Console()).start();
	}

}
