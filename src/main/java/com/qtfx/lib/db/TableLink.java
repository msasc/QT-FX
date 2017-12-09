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

package com.qtfx.lib.db;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * A link between a local and a foreign table. Its the root of foreign keys and relations.
 * 
 * @author Miquel Sas
 */
public class TableLink implements Iterable<TableLink.Segment> {

	/**
	 * Segment.
	 */
	public static class Segment {

		/** The local field. */
		private Field localField;
		/** Local field alias. */
		private String localFieldAlias;
		/** The foreign field. */
		private Field foreignField;
		/** Foreign field alias. */
		private String foreignFieldAlias;

		/**
		 * Default constructor.
		 */
		public Segment() {
			super();
		}

		/**
		 * Constructor assigning the fields.
		 *
		 * @param localField The local field.
		 * @param foreignField The foreign field.
		 */
		public Segment(Field localField, Field foreignField) {
			super();
			this.localField = localField;
			this.foreignField = foreignField;
		}

		/**
		 * Get the local field.
		 *
		 * @return The local field.
		 */
		public Field getLocalField() {
			return localField;
		}

		/**
		 * Get the foreign field.
		 *
		 * @return The foreign field.
		 */
		public Field getForeignField() {
			return foreignField;
		}

		/**
		 * Set the local field.
		 *
		 * @param localField The local field.
		 */
		public void setLocalField(Field localField) {
			this.localField = localField;
		}

		/**
		 * Set the foreign field.
		 *
		 * @param foreignField The foreign field.
		 */
		public void setForeignField(Field foreignField) {
			this.foreignField = foreignField;
		}

		/**
		 * Returns the local field alias.
		 * 
		 * @return The local field alias.
		 */
		public String getLocalFieldAlias() {
			if (localFieldAlias == null && localField != null) {
				return localField.getAlias();
			}
			return localFieldAlias;
		}

		/**
		 * Sets the local field alias.
		 * 
		 * @param localFieldAlias The local field alias.
		 */
		public void setLocalFieldAlias(String localFieldAlias) {
			this.localFieldAlias = localFieldAlias;
		}

		/**
		 * Returns the foreign field alias.
		 * 
		 * @return The foreign field alias.
		 */
		public String getForeignFieldAlias() {
			if (foreignFieldAlias == null && foreignField != null) {
				return foreignField.getAlias();
			}
			return foreignFieldAlias;
		}

		/**
		 * Sets the foreign field alias.
		 * 
		 * @param foreignFieldAlias The foreign field alias.
		 */
		public void setForeignFieldAlias(String foreignFieldAlias) {
			this.foreignFieldAlias = foreignFieldAlias;
		}

		/**
		 * Appends this segment to an SQL select construction.
		 *
		 * @param b The string builder where the SQL query is being appended.
		 */
		public void appendToSQL(StringBuilder b) {
			b.append(localField.getNameRelate());
			b.append(" = ");
			b.append(foreignField.getNameRelate());
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int hashCode() {
			int hash = 0;
			hash ^= localField.hashCode();
			hash ^= foreignField.hashCode();
			return hash;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			final Segment other = (Segment) obj;
			if (!Objects.equals(this.localField, other.localField)) {
				return false;
			}
			return Objects.equals(this.foreignField, other.foreignField);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			StringBuilder b = new StringBuilder(64);
			b.append(getLocalField().getName());
			b.append(" -> ");
			b.append(getForeignField().getName());
			return b.toString();
		}
	}

	/** The local table. */
	private Table localTable = null;
	/** The foreign table. */
	private Table foreignTable = null;
	/** Local table alias. */
	private String localTableAlias;
	/** Foreign table alias. */
	private String foreignTableAlias;
	/** List of segments. */
	private List<Segment> segments = new ArrayList<>();

	/**
	 * Constructor.
	 */
	public TableLink() {
		super();
	}

	/**
	 * Add a segment to this table link.
	 *
	 * @param localField The local field.
	 * @param foreignField The foreign field.
	 */
	public void add(Field localField, Field foreignField) {
		segments.add(new Segment(localField, foreignField));
	}

	/**
	 * Returns the segment at the index.
	 * 
	 * @param index The index.
	 * @return The segment.
	 */
	public Segment get(int index) {
		return segments.get(index);
	}

	/**
	 * Return the list of segments.
	 * 
	 * @return The list of segments.
	 */
	public List<Segment> getSegments() {
		return segments;
	}

	/**
	 * Return the number of segments.
	 * 
	 * @return The number of segments.
	 */
	public int size() {
		return segments.size();
	}

	/**
	 * Check empty (no segments).
	 * 
	 * @return A boolean.
	 */
	public boolean isEmpty() {
		return segments.isEmpty();
	}

	/**
	 * Get the local table.
	 *
	 * @return The local table
	 */
	public Table getLocalTable() {
		return localTable;
	}

	/**
	 * Returns the local table alias.
	 * 
	 * @return The local table alias.
	 */
	public String getLocalTableAlias() {
		if (localTableAlias == null && localTable != null) {
			return localTable.getAlias();
		}
		return localTableAlias;
	}

	/**
	 * Set the local table.
	 *
	 * @param localTable The local table
	 */
	public void setLocalTable(Table localTable) {
		this.localTable = localTable;
	}

	/**
	 * Sets the local table alias.
	 * 
	 * @param localTableAlias The local table alias.
	 */
	public void setLocalTableAlias(String localTableAlias) {
		this.localTableAlias = localTableAlias;
	}

	/**
	 * Get the foreign table.
	 *
	 * @return The foreign table.
	 */
	public Table getForeignTable() {
		return foreignTable;
	}

	/**
	 * Returns the foreign table alias.
	 * 
	 * @return The foreign table alias.
	 */
	public String getForeignTableAlias() {
		if (foreignTableAlias == null && foreignTable != null) {
			return foreignTable.getAlias();
		}
		return foreignTableAlias;
	}

	/**
	 * Set the foreign table.
	 *
	 * @param foreignTable The foreign table.
	 */
	public void setForeignTable(Table foreignTable) {
		this.foreignTable = foreignTable;
	}

	/**
	 * Sets the foreign table alias.
	 * 
	 * @param foreignTableAlias The foreign table alias.
	 */
	public void setForeignTableAlias(String foreignTableAlias) {
		this.foreignTableAlias = foreignTableAlias;
	}

	/**
	 * Returns the local field index or -1.
	 * 
	 * @param field The field.
	 * @return The index.
	 */
	public int getLocalFieldIndex(Field field) {
		for (int i = 0; i < segments.size(); i++) {
			if (segments.get(i).getLocalField().equals(field)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Returns the foreign field index or -1.
	 * 
	 * @param field The field.
	 * @return The index.
	 */
	public int getForeignFieldIndex(Field field) {
		for (int i = 0; i < segments.size(); i++) {
			if (segments.get(i).getForeignField().equals(field)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Returns a boolean indicating if the field is contained as a local field.
	 * 
	 * @param field The field.
	 * @return A boolean.
	 */
	public boolean containsLocalField(Field field) {
		return getLocalFieldIndex(field) >= 0;
	}

	/**
	 * Returns a boolean indicating if the field is contained as a foreign field.
	 * 
	 * @param field The field.
	 * @return A boolean.
	 */
	public boolean containsForeignField(Field field) {
		return getForeignFieldIndex(field) >= 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<Segment> iterator() {
		return segments.iterator();
	}

}
