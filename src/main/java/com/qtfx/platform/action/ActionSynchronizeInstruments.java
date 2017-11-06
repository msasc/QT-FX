/*
 * Copyright (C) 2015 Miquel Sas
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 */

package com.qtfx.platform.action;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qtfx.library.db.Criteria;
import com.qtfx.library.db.Persistor;
import com.qtfx.library.db.Record;
import com.qtfx.library.mkt.data.Instrument;
import com.qtfx.library.mkt.server.Server;
import com.qtfx.platform.QTPlatform;
import com.qtfx.platform.ServerConnector;
import com.qtfx.platform.db.Fields;
import com.qtfx.platform.util.PersistorUtils;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * Synchronize available instruments for the server.
 *
 * @author Miquel Sas
 */
public class ActionSynchronizeInstruments implements EventHandler<ActionEvent> {

	/** Logger instance. */
	private static final Logger LOGGER = LogManager.getLogger();
	/** Status id. */
	private static final String statusId = "sync-inst";

	/**
	 * Runnable to perform synchronizing.
	 */
	class Synchronizer implements Runnable {
		@Override
		public void run() {
			try {
				QTPlatform.getStatusBar().setStatus(statusId, "Connecting to server " + server.getName(), 1d, 5d);
				ServerConnector.connect(server);
				
				QTPlatform.getStatusBar().setStatus(statusId, "Retrieving available instruments", 2, 5);
				List<Instrument> instruments = server.getAvailableInstruments();
				
				QTPlatform.getStatusBar().setStatus(statusId, "Deleting registered instruments", 3, 5);
				Persistor persistor = PersistorUtils.getPersistorInstruments();
				persistor.delete((Criteria) null);

				QTPlatform.getStatusBar().setStatus(statusId, "Inserting available instruments", 4, 5);
				for (Instrument instrument : instruments) {
					Record record = persistor.getDefaultRecord();
					record.setValue(Fields.SERVER_ID, server.getId());
					record.setValue(Fields.INSTRUMENT_ID, instrument.getId());
					record.setValue(Fields.INSTRUMENT_DESC, instrument.getDescription());
					record.setValue(Fields.INSTRUMENT_PIP_VALUE, instrument.getPipValue());
					record.setValue(Fields.INSTRUMENT_PIP_SCALE, instrument.getPipScale());
					record.setValue(Fields.INSTRUMENT_TICK_VALUE, instrument.getTickValue());
					record.setValue(Fields.INSTRUMENT_TICK_SCALE, instrument.getTickScale());
					record.setValue(Fields.INSTRUMENT_VOLUME_SCALE, instrument.getVolumeScale());
					record.setValue(
						Fields.INSTRUMENT_PRIMARY_CURRENCY,
						instrument.getPrimaryCurrency().toString());
					record.setValue(
						Fields.INSTRUMENT_SECONDARY_CURRENCY,
						instrument.getSecondaryCurrency().toString());
					persistor.insert(record);
				}

				QTPlatform.getStatusBar().setStatus(statusId, "Disconnecting from server " + server.getName(), 5, 5);
				ServerConnector.disconnect(server);

			} catch (Exception exc) {
				LOGGER.catching(exc);
			} finally {
				QTPlatform.getStatusBar().removeStatus(statusId);
			}
		}
	}

	/** Server. */
	private Server server;

	/**
	 * Constructor.
	 * 
	 * @param server The server.
	 */
	public ActionSynchronizeInstruments(Server server) {
		this.server = server;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handle(ActionEvent event) {
		new Thread(new Synchronizer()).start();
	}
}
