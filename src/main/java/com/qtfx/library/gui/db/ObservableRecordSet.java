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

package com.qtfx.library.gui.db;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.qtfx.library.db.Record;

import javafx.beans.InvalidationListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

/**
 *
 * @author Miquel Sas
 *
 */
public class ObservableRecordSet implements ObservableList<Record> {

	/**
	 * 
	 */
	public ObservableRecordSet() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public boolean contains(Object o) {
		return false;
	}

	@Override
	public Iterator<Record> iterator() {
		return null;
	}

	@Override
	public Object[] toArray() {
		return null;
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return null;
	}

	@Override
	public boolean add(Record e) {
		return false;
	}

	@Override
	public boolean remove(Object o) {
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addAll(Collection<? extends Record> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addAll(int index, Collection<? extends Record> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Record get(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Record set(int index, Record element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void add(int index, Record element) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Record remove(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int indexOf(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int lastIndexOf(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ListIterator<Record> listIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListIterator<Record> listIterator(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Record> subList(int fromIndex, int toIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addListener(InvalidationListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeListener(InvalidationListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addListener(ListChangeListener<? super Record> listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeListener(ListChangeListener<? super Record> listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean addAll(Record... elements) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean setAll(Record... elements) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean setAll(Collection<? extends Record> col) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeAll(Record... elements) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean retainAll(Record... elements) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void remove(int from, int to) {
		// TODO Auto-generated method stub
		
	}

}
