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

package com.qtfx.gui;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * Table view sample to understand how it works.
 *
 * @author Miquel Sas
 */
public class TableViewSample extends Application {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Person class.
	 */
	static class Person {
		private SimpleStringProperty firstName;
		private SimpleStringProperty lastName;
		private SimpleStringProperty email;

		private Person(String firstName, String lastName, String email) {
			this.firstName = new SimpleStringProperty(firstName);
			this.lastName = new SimpleStringProperty(lastName);
			this.email = new SimpleStringProperty(email);
		}

		public StringProperty firstNameProperty() {
			return firstName;
		}

		public StringProperty lastNameProperty() {
			return lastName;
		}

		public StringProperty emailProperty() {
			return email;
		}
	}

	/**
	 * Callback: first name.
	 */
	static class FirstName implements Callback<CellDataFeatures<Person, String>, ObservableValue<String>> {
		@Override
		public ObservableValue<String> call(CellDataFeatures<Person, String> person) {
			return person.getValue().firstNameProperty();
		}
	}

	/**
	 * Callback: last name.
	 */
	static class LastName implements Callback<CellDataFeatures<Person, String>, ObservableValue<String>> {
		@Override
		public ObservableValue<String> call(CellDataFeatures<Person, String> person) {
			return person.getValue().lastNameProperty();
		}
	}

	/**
	 * Callback: email.
	 */
	static class Email implements Callback<CellDataFeatures<Person, String>, ObservableValue<String>> {
		@Override
		public ObservableValue<String> call(CellDataFeatures<Person, String> person) {
			return person.getValue().emailProperty();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void start(Stage stage) throws Exception {

		// Columns.
		TableColumn<Person, String> firstName = new TableColumn<>("First name");
		firstName.setMinWidth(100);
		firstName.setCellValueFactory(new FirstName());

		TableColumn<Person, String> lastName = new TableColumn<>("Last name");
		lastName.setMinWidth(100);
		lastName.setCellValueFactory(new LastName());

		TableColumn<Person, String> email = new TableColumn<>("Email");
		email.setMinWidth(200);
		email.setCellValueFactory(new Email());

		// The table.
		TableView<Person> table = new TableView<>();

		// The list of persons.
		ObservableList<Person> items = FXCollections.observableArrayList();
		items.add(new Person("Jacob", "Smith", "jacob.smith@example.com"));
		items.add(new Person("Isabella", "Johnson", "isabella.johnson@example.com"));
		items.add(new Person("Ethan", "Williams", "ethan.williams@example.com"));
		items.add(new Person("Emma", "Jones", "emma.jones@example.com"));
		items.add(new Person("Michael", "Brown", "michael.brown@example.com"));

		// Set items and columns.
		table.setItems(items);
		table.getColumns().add(firstName);
		table.getColumns().add(lastName);
		table.getColumns().add(email);

		Label label = new Label("Address Book");
		label.setFont(new Font("Arial", 20));

		VBox vbox = new VBox();
		vbox.setSpacing(5);
		vbox.setPadding(new Insets(10, 0, 0, 10));
		vbox.getChildren().addAll(label, table);

		Group group = new Group(vbox);

		Scene scene = new Scene(group);
		stage.setTitle("Table View Sample");
		stage.setWidth(450);
		stage.setHeight(500);

		stage.setScene(scene);
		stage.show();
	}
}
