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

package com.qtfx.lib.db.rdbms;

import java.sql.SQLException;

import com.qtfx.lib.db.ForeignKey;
import com.qtfx.lib.db.Index;
import com.qtfx.lib.db.PersistorDDL;
import com.qtfx.lib.db.PersistorException;
import com.qtfx.lib.db.Table;

/**
 * Database persistor data definition.
 * 
 * @author Miquel Sas
 */
public class DBPersistorDDL implements PersistorDDL {

	/**
	 * The underlying <code>DBEngine</code>.
	 */
	private DBEngine dbEngine;

	/**
	 * Constructor.
	 * 
	 * @param dbEngine The underlying database engine.
	 */
	public DBPersistorDDL(DBEngine dbEngine) {
		super();
		this.dbEngine = dbEngine;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int addForeignKey(Table table, ForeignKey foreignKey) throws PersistorException {
		try {
			return dbEngine.executeAddForeignKey(table, foreignKey);
		} catch (SQLException exc) {
			throw new PersistorException(exc);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int addPrimaryKey(Table table) throws PersistorException {
		try {
			return dbEngine.executeAddPrimaryKey(table);
		} catch (SQLException exc) {
			throw new PersistorException(exc);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int buildTable(Table table) throws PersistorException {
		try {
			return dbEngine.executeBuildTable(table);
		} catch (SQLException exc) {
			throw new PersistorException(exc);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int createSchema(String schema) throws PersistorException{
		try {
			return dbEngine.executeCreateSchema(schema);
		} catch (SQLException exc) {
			throw new PersistorException(exc);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int createTable(Table table) throws PersistorException {
		try {
			return dbEngine.executeCreateTable(table);
		} catch (SQLException exc) {
			throw new PersistorException(exc);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int createIndex(Index index) throws PersistorException {
		try {
			return dbEngine.executeCreateIndex(index);
		} catch (SQLException exc) {
			throw new PersistorException(exc);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int dropForeignKey(Table table, ForeignKey foreignKey) throws PersistorException {
		try {
			return dbEngine.executeDropForeignKey(table, foreignKey);
		} catch (SQLException exc) {
			throw new PersistorException(exc);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int dropIndex(Index index) throws PersistorException {
		try {
			return dbEngine.executeDropIndex(index);
		} catch (SQLException exc) {
			throw new PersistorException(exc);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int dropTable(Table table) throws PersistorException {
		try {
			return dbEngine.executeDropTable(table);
		} catch (SQLException exc) {
			throw new PersistorException(exc);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean existsSchema(String schema) throws PersistorException {
		try {
			DBMetaData metaData = new DBMetaData(dbEngine);
			return metaData.existsSchema(schema);
		} catch (SQLException exc) {
			throw new PersistorException(exc);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean existsTable(Table table) throws PersistorException {
		return existsTable(table.getSchema(), table.getName());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean existsTable(String schema, String table) throws PersistorException {
		try {
			DBMetaData metaData = new DBMetaData(dbEngine);
			return metaData.existsTable(schema, table);
		} catch (SQLException exc) {
			throw new PersistorException(exc);
		}
	}
}
