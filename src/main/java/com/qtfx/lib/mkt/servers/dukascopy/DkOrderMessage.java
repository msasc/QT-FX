/*
 * Copyright (C) 2017 Miquel Sas
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

package com.qtfx.lib.mkt.servers.dukascopy;

import java.util.Set;

import com.qtfx.lib.mkt.server.Order;
import com.qtfx.lib.mkt.server.OrderMessage;

/**
 * Dukascopy implementation of the order message.
 *
 * @author Miquel Sas
 */
public class DkOrderMessage implements OrderMessage {
	
	/** Time. */
	private long time;
	/** Text message. */
	private String message;
	/** The order. */
	private Order order;
	/** List of reasons. */
	private Set<Reason> reasons;
	/** Type of message. */
	private Type type;

	/**
	 * Constructor.
	 * 
	 * @param time Time.
	 * @param message Text message.
	 * @param order The order.
	 * @param reasons List of reasons.
	 * @param type Type of message.
	 */
	public DkOrderMessage(long time, String message, Order order, Set<Reason> reasons, Type type) {
		super();
		this.time = time;
		this.message = message;
		this.order = order;
		this.reasons = reasons;
		this.type = type;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getTime() {
		return time;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getMessage() {
		return message;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Order getOrder() {
		return order;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Reason> getReasons() {
		return reasons;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Type getType() {
		return type;
	}

}
