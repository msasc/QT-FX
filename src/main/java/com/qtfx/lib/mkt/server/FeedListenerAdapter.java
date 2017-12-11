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
package com.qtfx.lib.mkt.server.feed;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Miquel Sas
 *
 */
public class FeedListenerAdapter implements FeedListener {

	/**
	 * List of current subscriptions.
	 */
	List<DataSubscription> currentDataSubscriptions = new ArrayList<>();
	/**
	 * List of completed subscriptions.
	 */
	List<DataSubscription> dataSubscriptions = new ArrayList<>();
	/**
	 * List of tick subscriptions.
	 */
	List<TickSubscription> tickSubscriptions = new ArrayList<>();

	/**
	 * Constructor.
	 */
	public FeedListenerAdapter() {
		super();
	}

	/**
	 * Adds a subscription to current data.
	 * 
	 * @param subscription The subscription.
	 */
	public void addCurrentDataSubscription(DataSubscription subscription) {
		currentDataSubscriptions.add(subscription);
	}

	/**
	 * Adds a subscription to current data.
	 * 
	 * @param subscription The subscription.
	 */
	public void addDataSubscription(DataSubscription subscription) {
		dataSubscriptions.add(subscription);
	}

	/**
	 * Add a tick subscription.
	 * 
	 * @param subscription The subscription.
	 */
	public void addTickSubscription(TickSubscription subscription) {
		tickSubscriptions.add(subscription);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DataSubscription> getCurrentDataSubscriptions() {
		return currentDataSubscriptions;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DataSubscription> getDataSubscriptions() {
		return dataSubscriptions;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<TickSubscription> getTickSubscriptions() {
		return tickSubscriptions;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCurrentData(DataEvent dataEvent) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onData(DataEvent dataEvent) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onTick(TickEvent tickEvent) {
	}

}
