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
 * Enumerates the possible order states.
 * 
 * @author Miquel Sas
 */
public enum OrderState {
	/** After order was cancelled. */
	CANCELLED,
	/** Set after the order was closed. */
	CLOSED,
	/** Set right after order submission and before order acceptance by the server. */
	CREATED,
	/** Set after order was fully or partially filled. */
	FILLED,
	/** Set after order submission for conditional orders. */
	OPENED;
}
