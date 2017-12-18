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

package com.qtfx.lib.mkt.servers.dukascopy;

import java.util.Currency;

import com.dukascopy.api.IAccount;
import com.qtfx.lib.mkt.server.Account;

/**
 * Dukascopy account implementation.
 *
 * @author Miquel Sas
 */
public class DkAccount implements Account {
	
	/** Dukascopy account. */
	private IAccount account;

	/**
	 * Constructor.
	 * 
	 * @param account Dukascopy account.
	 */
	public DkAccount(IAccount account) {
		super();
		this.account = account;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getBalance() {
		return account.getBalance();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Currency getCurrency() {
		return account.getAccountCurrency().getJavaCurrency();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getEquity() {
		return account.getEquity();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getLeverage() {
		return account.getLeverage();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getMarginCutLevel() {
		return account.getMarginCutLevel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getOverWeekEndLeverage() {
		return account.getOverWeekEndLeverage();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getUsedMargin() {
		return account.getUsedMargin();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getUsedLeverage() {
		return account.getUseOfLeverage();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getUserName() {
		return account.getUserName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isConnected() {
		return account.isConnected();
	}
}
