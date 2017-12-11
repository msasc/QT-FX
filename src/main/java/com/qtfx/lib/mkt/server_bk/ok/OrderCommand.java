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
package com.qtfx.lib.mkt.server_bk.ok;

/**
 * Enumerates the different types of orders. Note that not all brokers may support all enumerated order commands. In
 * such cases the system will approximate the required order command to the available one.
 * 
 * @author Miquel Sas
 */
public enum OrderCommand {
	/** Buy, the command for a long order already filled. */
	BUY,
	/** Buy at market price. */
	BUY_MARKET,
	/** Buy when ask price &lt;= specified price. */
	BUY_LIMIT_ASK,
	/** Buy when bid price &lt;= specified price. */
	BUY_LIMIT_BID,
	/** Buy when ask price &gt;= specified price. */
	BUY_STOP_ASK,
	/** Buy when bid price &gt;= specified price. */
	BUY_STOP_BID,
	/** Sell, the command for a short order already filled. */
	SELL,
	/** Sell at market price. */
	SELL_MARKET,
	/** Sell when ask price &gt;= specified price. */
	SELL_LIMIT_ASK,
	/** Sell when bid price &gt;= specified price. */
	SELL_LIMIT_BID,
	/** Sell when ask price &lt;= specified price. */
	SELL_STOP_ASK,
	/*** Sell when bid price &lt;= specified price. */
	SELL_STOP_BID,
	/** Place a ask at the specified price. */
	PLACE_ASK,
	/** Place bid at specified price. */
	PLACE_BID;
}
