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
package com.qtfx.lib.mkt.server_bk.servers.dkcp;

import com.dukascopy.api.IBar;
import com.dukascopy.api.Instrument;
import com.dukascopy.api.OfferSide;
import com.dukascopy.api.Period;
import com.dukascopy.api.feed.IBarFeedListener;

/**
 * Implementation of the <i>IBarFeedListener</i> interface.
 * 
 * @author Miquel Sas
 */
public class DkBarFeedListener implements IBarFeedListener {

	///////////////
	// MKT objects.
	
	/** The Dukascopy server. */
	private DkServer server ;

	/**
	 * Constructor.
	 * 
	 * @param server The Dukascopy server.
	 */
	public DkBarFeedListener(DkServer server) {
		super();
		this.server = server;
	}

	/**
	 * The method is being called when next bar arrives and posts the bar to the dispatcher.
	 * 
	 * @param dkInstrument Instrument.
	 * @param dkPeriod Period.
	 * @param dkOfferSide Offer side.
	 * @param dkBar Bar.
	 */
	@Override
	public void onBar(Instrument dkInstrument, Period dkPeriod, OfferSide dkOfferSide, IBar dkBar) {
		server.getFeedDispatcher().addData(
			DkConverter.fromDkInstrument(dkInstrument),
			DkConverter.fromDkPeriod(dkPeriod),
			DkConverter.fromDkOfferSide(dkOfferSide),
			DkConverter.fromDkBar(dkBar));
	}

}
