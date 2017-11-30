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

package com.qtfx.util;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Iterator;
import java.util.stream.LongStream;

import com.qtfx.lib.db.Field;
import com.qtfx.lib.db.FieldGroup;
import com.qtfx.lib.db.FieldList;
import com.qtfx.lib.db.Record;
import com.qtfx.lib.db.RecordSet;
import com.qtfx.lib.db.Types;
import com.qtfx.lib.db.Value;
import com.qtfx.lib.util.Calendar;
import com.qtfx.lib.util.Random;
import com.qtfx.lib.util.Strings;

import javafx.util.StringConverter;

/**
 * Test utilities, field list, random values...
 * 
 * @author Miquel Sas
 */
public class Util {

	public static Calendar calendarStart = Calendar.getGTMCalendar(2010, 1, 1);
	public static Calendar calendarEnd = Calendar.getGTMCalendar(2015, 12, 31);

	private static LongStream longStream;
	private static Iterator<Long> longIterator;

	/**
	 * Returns a random value for a possible values field.
	 */
	public static Value getRandomPossibleValues(Field field) {
		int count = field.getPossibleValues().size();
		int index = Random.nextInt(count);
		return field.getPossibleValues().get(index);
	}

	/**
	 * Returns a random date between calendars start and end.
	 */
	public static Date getRandomDate() {
		if (longStream == null) {
			long start = calendarStart.getTimeInMillis();
			long end = calendarEnd.getTimeInMillis();
			longStream = Random.longs(start, end + 1);
			longIterator = longStream.iterator();
		}
		long randomTime = longIterator.next();
		return new Date(randomTime);
	}

	/**
	 * Returns a random text.
	 */
	public static String getRandomText(Field field) {
		int length = field.getLength();
		if (!field.isFixedWidth()) {
			length = Random.nextInt(length) + 1;
		}
		return getRandomString(length, Strings.LETTERS + Strings.DIGITS);
	}

	/**
	 * Returns a random text.
	 */
	public static String getRandomCode(Field field, int prefixLength) {
		int suffixLength = field.getLength() - prefixLength;
		return getRandomString(prefixLength, Strings.LETTERS) + getRandomString(suffixLength, Strings.DIGITS);
	}

	/**
	 * Returns a random big decimal.
	 */
	public static BigDecimal getRandomDecimal(Field field) {
		int length = field.getLength();
		int decimals = field.getDecimals();
		int integer = length - (decimals != 0 ? decimals + 1 : 0);
		integer = Random.nextInt(integer) + 1;
		String integerPart = getRandomString(integer, Strings.DIGITS);
		String decimalPart = getRandomString(decimals, Strings.DIGITS);
		String number = integerPart + "." + decimalPart;
		return new BigDecimal(number).setScale(decimals, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * Returns a random boolean.
	 */
	public static boolean getRandomBoolean() {
		return (Random.nextInt(2) == 1 ? true : false);
	}

	/**
	 * Returns a random string.
	 */
	public static String getRandomString(int length, String source) {
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < length; i++) {
			b.append(Strings.getRandomChar(source));
		}
		return b.toString();
	}

	/**
	 * Returns a random string.
	 */
	public static String getRandomString(int minLength, int maxLength, String source) {
		int length = minLength + Random.nextInt(maxLength - minLength);
		return getRandomString(length, source);
	}

	static class ArticleStringConverter extends StringConverter<Value> {
		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString(Value value) {
			String sv = value.toString();
			return sv.substring(0, 2) + "-" + sv.substring(2);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Value fromString(String string) {
			String prefix = "";
			String suffix = "";
			if (string.length() >= 2) {
				prefix = string.substring(0, 2);
			}
			if (string.length() > 2) {
				suffix = string.substring(2);
			}
			return new Value(prefix + suffix);
		}
	}

	/**
	 * Returns a field list suitable for many tests.
	 */
	public static FieldList getFieldList() {

		FieldGroup fgKEY = new FieldGroup();
		fgKEY.setIndex(0);
		fgKEY.setName("KEY");
		fgKEY.setTitle("Key");

		FieldGroup fgDESC = new FieldGroup();
		fgDESC.setIndex(1);
		fgDESC.setName("DESC");
		fgDESC.setTitle("Description and attributes");

		FieldGroup fgOTHER = new FieldGroup();
		fgOTHER.setIndex(2);
		fgOTHER.setName("NAME");
		fgOTHER.setTitle("Other characteristics");

		FieldList fieldList = new FieldList();

		Field fCARTICLE = new Field();
		fCARTICLE.setName("CARTICLE");
		fCARTICLE.setAlias("CARTICLE");
		fCARTICLE.setTitle("Artice code");
		fCARTICLE.setLabel("Artice code");
		fCARTICLE.setHeader("Artice");
		fCARTICLE.setType(Types.STRING);
		fCARTICLE.setLength(10);
		fCARTICLE.setFixedWidth(true);
		fCARTICLE.setPrimaryKey(true);
		fCARTICLE.setFieldGroup(fgKEY);
		fCARTICLE.setStringConverter(new ArticleStringConverter());
		fCARTICLE.setStyle("-fx-font-family: monospace; -fx-font-weight: bold; -fx-font-size: 16");
		fieldList.addField(fCARTICLE);

		Field fDARTICLE = new Field();
		fDARTICLE.setName("DARTICLE");
		fDARTICLE.setAlias("DARTICLE");
		fDARTICLE.setTitle("Artice description");
		fDARTICLE.setLabel("Artice description");
		fDARTICLE.setHeader("Artice description");
		fDARTICLE.setType(Types.STRING);
		fDARTICLE.setLength(100);
		fDARTICLE.setFixedWidth(false);
		fDARTICLE.setFieldGroup(fgDESC);
		fieldList.addField(fDARTICLE);

		Field fCBUSINESS = new Field();
		fCBUSINESS.setName("CBUSINESS");
		fCBUSINESS.setAlias("CBUSINESS");
		fCBUSINESS.setTitle("Business code");
		fCBUSINESS.setLabel("Business code");
		fCBUSINESS.setHeader("Business");
		fCBUSINESS.setType(Types.STRING);
		fCBUSINESS.setLength(4);
		fCBUSINESS.setFieldGroup(fgDESC);
		fieldList.addField(fCBUSINESS);

		Field fTCREATED = new Field();
		fTCREATED.setName("TCREATED");
		fTCREATED.setAlias("TCREATED");
		fTCREATED.setTitle("Date created");
		fTCREATED.setLabel("Date created");
		fTCREATED.setHeader("Date created");
		fTCREATED.setType(Types.DATE);
		fTCREATED.setFieldGroup(fgOTHER);
		fieldList.addField(fTCREATED);

		Field fQSALES = new Field();
		fQSALES.setName("QSALES");
		fQSALES.setAlias("QSALES");
		fQSALES.setTitle("Sales");
		fQSALES.setLabel("Sales");
		fQSALES.setHeader("Sales");
		fQSALES.setType(Types.DECIMAL);
		fQSALES.setLength(14);
		fQSALES.setDecimals(4);
		fQSALES.setFieldGroup(fgOTHER);
		fieldList.addField(fQSALES);

		Field fICHECKED = new Field();
		fICHECKED.setName("ICHECKED");
		fICHECKED.setAlias("ICHECKED");
		fICHECKED.setTitle("Checked");
		fICHECKED.setLabel("Checked");
		fICHECKED.setHeader("Checked");
		fICHECKED.setType(Types.BOOLEAN);
		fICHECKED.setEditBooleanInCheckBox(true);
		fICHECKED.setFieldGroup(fgOTHER);
		fieldList.addField(fICHECKED);

		Field fIREQUIRED = new Field();
		fIREQUIRED.setName("IREQUIRED");
		fIREQUIRED.setAlias("IREQUIRED");
		fIREQUIRED.setTitle("Required");
		fIREQUIRED.setLabel("Required");
		fIREQUIRED.setHeader("Required");
		fIREQUIRED.setType(Types.BOOLEAN);
		fIREQUIRED.setEditBooleanInCheckBox(false);
		fIREQUIRED.setFieldGroup(fgOTHER);
		fieldList.addField(fIREQUIRED);

		Field fISTATUS = new Field();
		fISTATUS.setName("ISTATUS");
		fISTATUS.setAlias("ISTATUS");
		fISTATUS.setTitle("Status");
		fISTATUS.setLabel("Status");
		fISTATUS.setHeader("Status");
		fISTATUS.setType(Types.STRING);
		fISTATUS.setLength(2);
		fISTATUS.addPossibleValue(new Value("01"), "Created");
		fISTATUS.addPossibleValue(new Value("02"), "Acceptance");
		fISTATUS.addPossibleValue(new Value("03"), "Accepted");
		fISTATUS.addPossibleValue(new Value("04"), "Engineered");
		fISTATUS.addPossibleValue(new Value("05"), "Produced");
		fISTATUS.addPossibleValue(new Value("06"), "Sales");
		fISTATUS.addPossibleValue(new Value("07"), "Obsolete");
		fieldList.addField(fISTATUS);

		return fieldList;
	}

	/**
	 * Return a record (random)
	 */
	public static Record getRandomRecord(FieldList fields) {
		Record record = new Record(fields);
		record.setValue("CARTICLE", new Value(getRandomCode(record.getField("CARTICLE"), 2)));
		record.setValue("DARTICLE", new Value(getRandomString(50, 100, Strings.LETTERS)));
		record.setValue("CBUSINESS", new Value(getRandomCode(record.getField("CBUSINESS"), 4)));
		record.setValue("TCREATED", new Value(getRandomDate()));
		record.setValue("QSALES", new Value(getRandomDecimal(record.getField("QSALES"))));
		record.setValue("TCREATED", new Value(getRandomDate()));
		record.setValue("ICHECKED", new Value(getRandomBoolean()));
		record.setValue("IREQUIRED", new Value(getRandomBoolean()));
		record.setValue("ISTATUS", getRandomPossibleValues(record.getField("ISTATUS")));
		return record;
	}

	/**
	 * Return a record set (random)
	 */
	public static RecordSet getRandomRecordSet(int length, FieldList fields) {
		RecordSet rs = new RecordSet(fields);
		for (int i = 0; i < length; i++) {
			rs.add(getRandomRecord(fields));
		}
		return rs;
	}
}
