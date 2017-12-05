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
package com.qtfx.lib.db;

import java.util.ArrayList;
import java.util.List;

/**
 * A relation between two tables.
 *
 * @author Miquel Sas
 */
public class Relation extends TableLink {

	/**
	 * A relation type describes the type of a relation. There are only four possible types of relations, listed below.
	 * <ol>
	 * <li><code><b>LookUp</b></code>: in a lookup relation, the list of local fields from the relation segments does
	 * not match the local entity key fields, and the foreign fields from the segments exactly match the foreign entity
	 * key fields. It is a many-to-one relation. A lookup relation can not be reversed because a relation must have
	 * convenient keys for the foreign entity.</li>
	 * <li><code><b>Unique</b></code>: in an unique relation, the list of fields from the segments exactly match both
	 * the local and foreign key fields. It is a one-to-one relation.</li>
	 * <li><code><b>LocalDetail</b></code>: it is a header-detail relation where the local entity is the detail entity.
	 * The fields from the segments match partly the local key fields and exactly the foreign key fields. It is a
	 * many-to-one relation.</li>
	 * <li><code><b>ForeignDetail</b></code>: it is a header-detail relation where the foreign entity is the detail
	 * entity. The fields from the segments match partly the foreign key fields and exactly the local key fields. It is
	 * a one-to-many relation.</li>
	 * </ol>
	 *
	 * A <code>LocalDetail</code> relation if reversed becomes a <code>ForeignDetail</code> relation and viceversa.
	 */
	public enum Type {

		INVALID, LOOKUP, UNIQUE, LOCAL_DETAIL, FOREIGN_DETAIL;

		/**
		 * Returns he category of the relation type.
		 *
		 * @return The relation category.
		 */
		public Category getCategory() {
			switch (this) {
			case LOOKUP:
				return Category.ONE_TO_ONE;
			case UNIQUE:
				return Category.ONE_TO_ONE;
			case LOCAL_DETAIL:
				return Category.MANY_TO_ONE;
			case FOREIGN_DETAIL:
				return Category.ONE_TO_MANY;
			case INVALID:
				return Category.UNKNOWN;
			default:
				return Category.UNKNOWN;
			}
		}
	}

	/**
	 * Relation categories are ONE_TO_ONE, ONE_TO_MANY, MANY_TO_ONE
	 */
	public enum Category {
		UNKNOWN, ONE_TO_ONE, ONE_TO_MANY, MANY_TO_ONE;
	}

	/**
	 * The outer property.
	 */
	private boolean outer = false;

	/**
	 * Default constructor.
	 */
	public Relation() {
		super();
	}

	/**
	 * Check if this relation is outer.
	 *
	 * @return A boolean.
	 */
	public boolean isOuter() {
		return outer;
	}

	/**
	 * Sets the outer property.
	 *
	 * @param outer A boolean.
	 */
	public void setOuter(boolean outer) {
		this.outer = outer;
	}

	/**
	 * Returns this relation type.
	 * 
	 * @return The type of the relation.
	 */
	public Relation.Type getType() {
		Type type = null;
		boolean check = true;
		if (check && getLocalTable() == null) {
			check = false;
		}
		if (check && getForeignTable() == null) {
			check = false;
		}
		if (check && getForeignTable().getPrimaryKeyFields() == null) {
			check = false;
		}
		if (check && getForeignTable().getPrimaryKeyFields().isEmpty()) {
			check = false;
		}
		if (check && isEmpty()) {
			check = false;
		}
		if (check && getLocalTable().getPrimaryKeyFields() == null) {
			check = false;
		}
		if (check && getLocalTable().getPrimaryKeyFields().isEmpty()) {
			check = false;
		}
		if (check) {

			// Get local-foreign key-segment fields
			List<Field> localPrimaryKeyFields = getLocalTable().getPrimaryKeyFields();
			List<Field> foreignPrimaryKeyFields = getForeignTable().getPrimaryKeyFields();
			List<Field> localSegmentFields = new ArrayList<>();
			List<Field> foreignSegmentFields = new ArrayList<>();
			for (Segment segment : getSegments()) {
				localSegmentFields.add(segment.getLocalField());
				foreignSegmentFields.add(segment.getForeignField());
			}

			// Define values for matches
			int NO_MATCH = 1; // Key and segment fields do not match
			int PARTIAL_MATCH_KEY = 2; // Match, key shorter
			int PARTIAL_MATCH_SEG = 3; // Match, segments shorter
			int TOTAL_MATCH = 4; // Exact match
			int count = 0;
			int localMatch = TOTAL_MATCH;
			int foreignMatch = TOTAL_MATCH;

			// Check matches on local fields
			count = Math.max(localPrimaryKeyFields.size(), localSegmentFields.size());
			for (int i = 0; i < count; i++) {
				if (i >= localPrimaryKeyFields.size() && i < localSegmentFields.size()) {
					localMatch = PARTIAL_MATCH_KEY;
					break;
				}
				if (i < localPrimaryKeyFields.size() && i >= localSegmentFields.size()) {
					localMatch = PARTIAL_MATCH_SEG;
					break;
				}
				if (!localPrimaryKeyFields.get(i).equals(localSegmentFields.get(i))) {
					localMatch = NO_MATCH;
					break;
				}
			}

			// Check matches on foreign fields
			count = Math.max(foreignPrimaryKeyFields.size(), foreignSegmentFields.size());
			for (int i = 0; i < count; i++) {
				if (i >= foreignPrimaryKeyFields.size() && i < foreignSegmentFields.size()) {
					foreignMatch = PARTIAL_MATCH_KEY;
					break;
				}
				if (i < foreignPrimaryKeyFields.size() && i >= foreignSegmentFields.size()) {
					foreignMatch = PARTIAL_MATCH_SEG;
					break;
				}
				if (!foreignPrimaryKeyFields.get(i).equals(foreignSegmentFields.get(i))) {
					foreignMatch = NO_MATCH;
					break;
				}
			}

			// Check type
			if (localMatch == TOTAL_MATCH && foreignMatch == TOTAL_MATCH) {
				type = Type.UNIQUE;
			} else if (localMatch == TOTAL_MATCH && foreignMatch == PARTIAL_MATCH_SEG) {
				type = Type.FOREIGN_DETAIL;
			} else if (localMatch == PARTIAL_MATCH_KEY && foreignMatch == TOTAL_MATCH) {
				type = Type.LOCAL_DETAIL;
			} else if (localMatch == PARTIAL_MATCH_SEG && foreignMatch == TOTAL_MATCH) {
				type = Type.LOCAL_DETAIL;
			} else if (localMatch == NO_MATCH && foreignMatch == TOTAL_MATCH) {
				type = Type.LOOKUP;
			}

			// If the type has not been set it may be the one-to-one / unique
			if (type == null && size() == localPrimaryKeyFields.size() && size() == foreignPrimaryKeyFields.size()) {
				type = Type.UNIQUE;
			}
		}
		if (type == null) {
			type = Type.INVALID;
		}
		return type;
	}

	/**
	 * Gets a string representation of this relation.
	 *
	 * @return A string representation of this column spec.
	 */
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder(64);
		b.append(getLocalTable().getName());
		b.append(" -> ");
		b.append(getForeignTable().getName());
		if (isOuter()) {
			b.append(" (+)");
		}
		b.append(" (");
		for (int i = 0; i < size(); i++) {
			if (i > 0) {
				b.append(", ");
			}
			b.append(get(i));
		}
		b.append(")");
		return b.toString();
	}

}
