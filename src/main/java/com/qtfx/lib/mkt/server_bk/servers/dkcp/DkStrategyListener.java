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

import com.dukascopy.api.IAccount;
import com.dukascopy.api.IBar;
import com.dukascopy.api.IContext;
import com.dukascopy.api.IMessage;
import com.dukascopy.api.IStrategy;
import com.dukascopy.api.ITick;
import com.dukascopy.api.Instrument;
import com.dukascopy.api.JFException;
import com.dukascopy.api.OfferSide;
import com.dukascopy.api.Period;

/**
 * Dukascopy strategy implementation to listen to strategy events, to retrieve the context, the history, ticks and all
 * data that requires a running strategy.
 * 
 * @author Miquel Sas
 */
public class DkStrategyListener implements IStrategy {

	/////////////////////
	// Dukascopy objects.

	/** The saved context. */
	private IContext context;

	///////////////
	// MKT objects.

	/** The server. */
	private DkServer server;

	/**
	 * Constructor.
	 * 
	 * @param server The server.
	 */
	public DkStrategyListener(DkServer server) {
		super();
		this.server = server;
	}

	/**
	 * Returns the <em>IContext</em> interface.
	 * 
	 * @return The context.
	 */
	IContext getContext() {
		return context;
	}

	/**
	 * Called on strategy start, register the context.
	 */
	@Override
	public void onStart(IContext context) throws JFException {
		this.context = context;
	}

	/**
	 * Called on every tick of every instrument that application is subscribed on.
	 */
	@Override
	public void onTick(Instrument instrument, ITick tick) throws JFException {
		server.getFeedDispatcher().addTick(DkConverter.fromDkInstrument(instrument), DkConverter.fromDkTick(tick));
	}

	/**
	 * Called on every bar for every basic period and instrument that application is subscribed on.
	 */
	@Override
	public void onBar(Instrument instrument, Period period, IBar askBar, IBar bidBar) throws JFException {
		server.getFeedDispatcher().addData(
			DkConverter.fromDkInstrument(instrument),
			DkConverter.fromDkPeriod(period), 
			DkConverter.fromDkOfferSide(OfferSide.ASK),
			DkConverter.fromDkBar(askBar));
		server.getFeedDispatcher().addData(
			DkConverter.fromDkInstrument(instrument),
			DkConverter.fromDkPeriod(period), 
			DkConverter.fromDkOfferSide(OfferSide.BID),
			DkConverter.fromDkBar(bidBar));
	}

	/**
	 * Called when new message is received.
	 */
	@Override
	public void onMessage(IMessage message) throws JFException {
	}

	/**
	 * Called when account information update is received.
	 */
	@Override
	public void onAccount(IAccount account) throws JFException {
	}

	/**
	 * Called before strategy is stopped.
	 */
	@Override
	public void onStop() throws JFException {
	}

}
