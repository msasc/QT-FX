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

import java.util.ArrayList;
import java.util.List;

import com.qtfx.lib.util.Strings;

/**
 * Periods of trading.
 * 
 * @author Miquel Sas
 */
public class Period implements Comparable<Period> {

	/** One minute period. */
	public static final Period ONE_MIN = new Period(Unit.MINUTE, 1);
	/** Three minutes period. */
	public static final Period THREE_MINS = new Period(Unit.MINUTE, 3);
	/** Five minutes period. */
	public static final Period FiveMins = new Period(Unit.MINUTE, 5);
	/** Fifteen minutes period. */
	public static final Period FIFTEEN_MINS = new Period(Unit.MINUTE, 15);
	/** Thirty minutes period. */
	public static final Period THIRTY_MINS = new Period(Unit.MINUTE, 30);
	/** One hour period. */
	public static final Period ONE_HOUR = new Period(Unit.HOUR, 1);
	/** Four hours period. */
	public static final Period FOUR_HOURS = new Period(Unit.HOUR, 4);
	/** Daily period. */
	public static final Period DAILY = new Period(Unit.DAY, 1);
	/** Weekly period. */
	public static final Period WEEKLY = new Period(Unit.WEEK, 1);
	/** Monthly period. */
	public static final Period MONTHLY = new Period(Unit.MONTH, 1);

	/**
	 * Returns the list of standard pre-defined periods.
	 * 
	 * @return The list of standard pre-defined periods.
	 */
	public static List<Period> getStandardPeriods() {
		List<Period> periods = new ArrayList<>();
		periods.add(ONE_MIN);
		periods.add(THREE_MINS);
		periods.add(FiveMins);
		periods.add(FIFTEEN_MINS);
		periods.add(THIRTY_MINS);
		periods.add(ONE_HOUR);
		periods.add(FOUR_HOURS);
		periods.add(DAILY);
		periods.add(WEEKLY);
		periods.add(MONTHLY);
		return periods;
	}

	/**
	 * Parse a period id.
	 * 
	 * @param id The period id.
	 * @return The period.
	 */
	public static Period parseId(String id) {
		// Id length must be 5.
		if (id.length() != 5) {
			throw new IllegalArgumentException("Invalid period id");
		}
		// Strings unit and size.
		String sunit = id.substring(0, 2);
		String ssize = id.substring(2);
		try {
			Unit unit = Unit.parseId(sunit);
			int size = Integer.parseInt(ssize);
			return new Period(unit, size);
		} catch (Exception exc) {
			throw new IllegalArgumentException("Invalid period id");
		}
	}

	/**
	 * Unit.
	 */
	private Unit unit;
	/**
	 * The number of units or size.
	 */
	private int size = -1;

	/**
	 * Constructor assigning unit and size.
	 * 
	 * @param unit The unit.
	 * @param size The size or number of units.
	 */
	public Period(Unit unit, int size) {
		super();
		this.unit = unit;
		this.size = size;
	}

	/**
	 * Returns a string id that uniquely identifies this period, by concatenating the unit id and the length padded to 3
	 * chars.
	 * 
	 * @return The period id.
	 */
	public String getId() {
		StringBuilder b = new StringBuilder();
		b.append(getUnit().getId());
		b.append(Strings.leftPad(Integer.toString(getSize()), 3, '0'));
		return b.toString();
	}

	/**
	 * Returns the unit.
	 * 
	 * @return The unit.
	 */
	public Unit getUnit() {
		return unit;
	}

	/**
	 * Returns the size or number of units.
	 * 
	 * @return The size or number of units.
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Returns the time this period elapses in millisecond. The time returned for months and years is the maximum (31 or
	 * 366 days).
	 * 
	 * @return The time of the priod in milliseconds.
	 */
	public long getTime() {
		long time = 0;
		switch (unit) {
		case MILLISECOND:
			time = 1;
			break;
		case SECOND:
			time = 1000;
			break;
		case MINUTE:
			time = 1000 * 60;
			break;
		case HOUR:
			time = 1000 * 60 * 60;
			break;
		case DAY:
			time = 1000 * 60 * 60 * 24;
			break;
		case WEEK:
			time = 1000 * 60 * 60 * 24 * 7;
			break;
		case MONTH:
			time = 1000 * 60 * 60 * 24 * 31;
			break;
		case YEAR:
			time = 1000 * 60 * 60 * 24 * 366;
			break;
		default:
			throw new IllegalArgumentException();
		}
		time *= size;
		return time;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compareTo(Period p) {

		// Unit equals, the size decides.
		if (getUnit().equals(p.getUnit())) {
			if (getSize() < p.getSize()) {
				return -1;
			} else if (getSize() > p.getSize()) {
				return 1;
			} else {
				return 0;
			}
		}

		// The ordinal of the unit decides.
		if (getUnit().ordinal() < p.getUnit().ordinal()) {
			return -1;
		} else if (getUnit().ordinal() > p.getUnit().ordinal()) {
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (o instanceof Period) {
			Period p = (Period) o;
			return compareTo(p) == 0;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append(getSize());
		b.append(" ");
		b.append(getUnit().name());
		if (getSize() > 1) {
			b.append("s");
		}
		return b.toString();
	}

	/**
	 * Returns an XML element representation of this period.
	 * 
	 * @return An XML element representation of this period.
	 */
	public String toXML() {
		StringBuilder b = new StringBuilder();
		b.append("<period");
		b.append(" unit=\"" + getUnit().name() + "\"");
		b.append(" size=\"" + getSize() + "\"");
		b.append("/>");
		return b.toString();
	}

}
