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

package com.qtfx.plaf.db;

/**
 * Field names.
 *
 * @author Miquel Sas
 */
public class Fields {
	
	////////////////
	// Data filters.
	
	public static final String DATA_FILTER = "data_filter";

	//////////////////////////
	// Instrument descriptors.
	
	public static final String INSTRUMENT_ID = "instr_id";
	public static final String INSTRUMENT_DESC = "instr_desc";
	public static final String INSTRUMENT_PIP_VALUE = "instr_pipv";
	public static final String INSTRUMENT_PIP_SCALE = "instr_pips";
	public static final String INSTRUMENT_PRIMARY_CURRENCY = "instr_currp";
	public static final String INSTRUMENT_SECONDARY_CURRENCY = "instr_currs";
	public static final String INSTRUMENT_TICK_VALUE = "instr_tickv";
	public static final String INSTRUMENT_TICK_SCALE = "instr_ticks";
	public static final String INSTRUMENT_VOLUME_SCALE = "instr_vols";
	
	///////////////
	// Offer sides.
	
	public static final String OFFER_SIDE = "offer_side";
	
	//////////////////////
	// Period descriptors.
	
	public static final String PERIOD = "period";
	public static final String PERIOD_ID = "period_id";
	public static final String PERIOD_NAME = "period_name";
	public static final String PERIOD_SIZE = "period_size";
	public static final String PERIOD_UNIT_INDEX = "period_unit_index";
	
	//////////////////////
	// Server descriptors.
	
	public static final String SERVER_ID = "server_id";
	public static final String SERVER_NAME = "server_name";
	public static final String SERVER_TITLE = "server_title";
	
	/////////////
	// Data (bar)
	
	public static final String INDEX = "index";
	public static final String TIME = "time";
	public static final String TIME_FMT = "time_fmt";
	public static final String OPEN = "open";
	public static final String HIGH = "high";
	public static final String LOW = "low";
	public static final String CLOSE = "close";
	public static final String VOLUME = "volume";
	
	/////////////////////
	// Ticker table name.
	
	public static final String TABLE_NAME = "table_name";
}
