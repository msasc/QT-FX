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
package com.qtfx.lib.mkt.server;

import java.util.Set;

/**
 * Enumerates the possible messages received upon order changes.
 * 
 * @author Miquel Sas
 */
public interface OrderMessage {
	/**
	 * Reasons of an order message.
	 */
	enum Reason {
		/** Amount changed. */
		AMOUNT_CHANGED,
		/** Expiration time changed. */
		EXPIRATION_TIME_CHANGED,
		/** Label changed. */
		LABEL_CHANGED,
		/** Price changed. */
		PRICE_CHANGED,
		/** Stop loss price changed. */
		STOP_LOSS_CHANGED,
		/** Take profit price changed. */
		TAKE_PROFIT_CHANGED,
		/** Order command changed. */
		ORDER_COMMAND_CHANGED,
		/** Order closed by merge. */
		CLOSED_BY_MERGE,
		/** Order closed by stop loss. */
		CLOSED_BY_STOP_LOSS,
		/** Order closed by take profit. */
		CLOSED_BY_TAKE_PROFIT,
		/** Order closed at market. */
		CLOSED_AT_MARKET,
		/** Order fully filled. */
		FILLED;
	}

	/**
	 * Types of an order message.
	 */
	enum Type {
		/** Sent after a successful order change. */
		CHANGE_OK,
		/** Sent if the order change was rejected by the server. */
		CHANGE_REJECTED,
		/** Sent after successful order closing. */
		CLOSE_OK,
		/** Sent if order close request was rejected. */
		CLOSE_REJECTED,
		/** Sent after successful order filling. */
		FILL_OK,
		/** Sent if server rejected order fill execution. */
		FILL_REJECTED,
		/** Sent after order submission was accepted by the server. */
		SUBMIT_OK,
		/** Sent when order submission was rejected. */
		SUBMIT_REJECTED,
		/** Sent after successful orders merge. */
		MERGE_OK,
		/** Sent if orders merge was rejected by the server. */
		MERGE_REJECTED;
	}

	/**
	 * Return the message time.
	 * 
	 * @return The time.
	 */
	long getTime();

	/**
	 * Return a text message.
	 * 
	 * @return The message.
	 */
	String getMessage();

	/**
	 * Return the order.
	 * 
	 * @return The order.
	 */
	Order getOrder();

	/**
	 * Return the list of reasons.
	 * 
	 * @return The list of reasons.
	 */
	Set<Reason> getReasons();

	/**
	 * Return the type.
	 * 
	 * @return The type.
	 */
	Type getType();
}
