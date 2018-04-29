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
package com.qtfx.lib.mkt.data;

import com.qtfx.lib.app.Session;

/**
 * Units used to define periods of aggregate incoming quotes.
 * 
 * @author Miquel Sas
 */
public enum Unit {
	MILLISECOND("MS"),
	SECOND("SC"),
	MINUTE("MN"),
	HOUR("HR"),
	DAY("DY"),
	WEEK("WK"),
	MONTH("MT"),
	YEAR("YR");

	/**
	 * Returns the unit of the given id.
	 * 
	 * @param id The unit id.
	 * @return The unit.
	 */
	public static Unit parseId(String id) {
		Unit[] units = values();
		for (Unit unit : units) {
			if (unit.getId().equals(id.toUpperCase())) {
				return unit;
			}
		}
		throw new IllegalArgumentException("Invalid unit id: " + id);
	}

	/** 2 char id. */
	private String id;

	/**
	 * Constructor.
	 * 
	 * @param id Two char id.
	 */
	private Unit(String id) {
		this.id = id;
	}

	/**
	 * Returns the two char id.
	 * 
	 * @return The id.
	 */
	public String getId() {
		return id;
	}

	/**
	 * Returns the short description.
	 * 
	 * @return The short description.
	 */
	public String getShortName() {
		switch (this) {
		case MILLISECOND:
			return Session.getSession().getString("unitMillisecond");
		case SECOND:
			return Session.getSession().getString("unitSecond");
		case MINUTE:
			return Session.getSession().getString("unitMinute");
		case HOUR:
			return Session.getSession().getString("unitHour");
		case DAY:
			return Session.getSession().getString("unitDay");
		case WEEK:
			return Session.getSession().getString("unitWeek");
		case MONTH:
			return Session.getSession().getString("unitMonth");
		case YEAR:
			return Session.getSession().getString("unitYear");
		default:
			throw new IllegalArgumentException();
		}
	}
}
