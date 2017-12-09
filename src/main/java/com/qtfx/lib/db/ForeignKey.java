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

/**
 * A table foreign key.
 *
 * @author Miquel Sas
 */
public class ForeignKey extends TableLink {

	/**
	 * Enumerate the ON DELETE actions
	 */
	public enum OnDelete {
		/** Constant for ON DELETE [NO ACTION|RESTRICT] */
		RESTRICT,
		/** Constant for ON DELETE [CASCADE] */
		CASCADE,
		/** Constant for ON DELETE [SET NULL] */
		SET_NULL;
	}

	/** The name of this constraint. */
	private String name = null;
	/** The type of deletion restriction: default RESTRICT. */
	private OnDelete deleteRestriction = OnDelete.RESTRICT;
	/** A boolean that indicates if the foreing key is persistent. */
	private boolean persistent = false;

	/**
	 * Default constructor.
	 */
	public ForeignKey() {
		super();
	}

	/**
	 * Constructor indicating if this foreign key is persisten.
	 * 
	 * @param persistent A boolean that indicates if the foreing key is persistent.
	 */
	public ForeignKey(boolean persistent) {
		super();
		this.persistent = persistent;
	}

	/**
	 * Get the name of this foreign key.
	 *
	 * @return The foreign key name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name of this foreign key.
	 *
	 * @param name The name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns a boolean that indicates if the foreing key is persistent.
	 * 
	 * @return A boolean that indicates if the foreing key is persistent.
	 */
	public boolean isPersistent() {
		return persistent;
	}

	/**
	 * Sets a boolean that indicates if the foreing key is persistent.
	 * 
	 * @param persistent A boolean that indicates if the foreing key is persistent.
	 */
	public void setPersistent(boolean persistent) {
		this.persistent = persistent;
	}

	/**
	 * Get the delete restriction.
	 *
	 * @return The delete restriction.
	 */
	public OnDelete getDeleteRestriction() {
		return deleteRestriction;
	}

	/**
	 * Set the delete restriction.
	 *
	 * @param deleteRestriction The delete restriction.
	 */
	public void setDeleteRestriction(OnDelete deleteRestriction) {
		this.deleteRestriction = deleteRestriction;
	}

	/**
	 * Returns the relation that could be build based on this foreign key.
	 * 
	 * @return The relation.
	 */
	public Relation getRelation() {
		Relation relation = new Relation();
		relation.setLocalTable(getLocalTable());
		relation.setForeignTable(getForeignTable());
		relation.setOuter(true);
		relation.setLocalTableAlias(getLocalTableAlias());
		relation.setForeignTableAlias(getForeignTableAlias());
		getSegments().forEach(segment -> relation.getSegments().add(segment));
		return relation;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder(64);
		b.append(getLocalTable().getName());
		b.append(" -> ");
		b.append(getForeignTable().getName());
		return b.toString();
	}
}
