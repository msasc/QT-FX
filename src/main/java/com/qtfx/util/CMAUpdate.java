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

package com.qtfx.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qtfx.lib.app.Session;
import com.qtfx.lib.gui.Alert;
import com.qtfx.lib.gui.TaskPane;
import com.qtfx.lib.gui.launch.Argument;
import com.qtfx.lib.gui.launch.ArgumentManager;
import com.qtfx.lib.task.Task;
import com.qtfx.lib.task.file.FileCopy;
import com.qtfx.lib.util.Lists;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 *
 * @author Miquel Sas
 *
 */
public class CMAUpdate extends Application {

	/** Logger configuration and text server initialization. */
	static {
		System.setProperty("log4j.configurationFile", "resources/LoggerQTPlatform.xml");
		Session.addBaseResource("resources/StringsLibrary.xml");
		Session.addBaseResource("resources/StringsQTPlatform.xml");
	}
	/** Logger instance. */
	private static final Logger LOGGER = LogManager.getLogger();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Locale.setDefault(Locale.UK);
		launch(args);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {

		// Argument manager.
		ArgumentManager argMngr = new ArgumentManager();
		Argument argEnvironment = new Argument("environment", "Environment: quality/production/incubator", true, false,
			"quality", "production", "incubator");
		Argument argTarget = new Argument("target", "Target: local/remote", true, false, "local", "remote");
		Argument argModules = new Argument("modules", "Modules: central/dictionary/local", true, true, "central",
			"dictionary", "local");
		Argument argPurge = new Argument("purge", "Purge destination directories", false, false, false);
		argMngr.add(argEnvironment);
		argMngr.add(argTarget);
		argMngr.add(argModules);
		argMngr.add(argPurge);

		// Validate arguments.
		if (!argMngr.parse(getParameters())) {
			Alert alert = new Alert(primaryStage);
			alert.setTitle("Argument errors");
			alert.setType(Alert.Type.ERROR);
			for (String error : argMngr.getErrors()) {
				alert.addText(error + "\n");
			}
			alert.show();
			System.exit(0);
			return;
		}
		
		// List of tasks and task pane.
		List<Task> taskTests = getTaskList(argMngr);
		TaskPane taskPane = new TaskPane(20);
		for (Task taskTest : taskTests) {
			taskPane.addTask(taskTest);
		}
		
		// Border pane as root.
		BorderPane root = new BorderPane();
		root.setCenter(taskPane.getPane());
		
		// Scene.
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("CMA Updater");

		// Factor dimensions
		primaryStage.setWidth(Screen.getPrimary().getBounds().getWidth() * 0.8);
		primaryStage.setHeight(Screen.getPrimary().getBounds().getHeight() * 0.8);
		primaryStage.centerOnScreen();

		// On request to close.
		primaryStage.setOnCloseRequest(e -> {
			for (Task taskTest : taskTests) {
				if (taskTest.isRunning()) {
					Alert.warning("Task check", "There are tasks still running");
					e.consume();
					return;
				}
			}
		});
		
		primaryStage.show();
	}

	/**
	 * Returns the destination root for a local target task.
	 * 
	 * @param argMngr The argument manager
	 * @return The source root.
	 */
	private static String getDstRootLocal(ArgumentManager argMngr) {
		String dstRoot = null;
		if (argMngr.getValue("environment").equals("production")) {
			dstRoot = "c:\\Development\\Eclipse-Workspaces\\Roca\\cma-head";
		}
		if (argMngr.getValue("environment").equals("quality")) {
			dstRoot = "c:\\Development\\Eclipse-Workspaces\\Roca\\cma-development";
		}
		if (argMngr.getValue("environment").equals("incubator")) {
			dstRoot = "c:\\Development\\Eclipse-Workspaces\\Roca\\cma-incubator";
		}
		return dstRoot;
	}

	/**
	 * Returns the source root for a local target task.
	 * 
	 * @param argMngr The argument manager
	 * @return The source root.
	 */
	private static String getSrcRootLocal(ArgumentManager argMngr) {
		String srcRoot = null;
		if (argMngr.getValue("environment").equals("production")) {
			srcRoot = "c:\\Development\\Eclipse-Workspaces\\Roca\\workspace-head";
		}
		if (argMngr.getValue("environment").equals("quality")) {
			srcRoot = "c:\\Development\\Eclipse-Workspaces\\Roca\\workspace-development";
		}
		if (argMngr.getValue("environment").equals("incubator")) {
			srcRoot = "c:\\Development\\Eclipse-Workspaces\\Roca\\workspace-incubator";
		}
		return srcRoot;
	}

	/**
	 * Returns the source root for a remote target task.
	 * 
	 * @param argMngr The argument manager
	 * @return The source root.
	 */
	private static String getSrcRootRemote(ArgumentManager argMngr) {
		return getDstRootLocal(argMngr);
	}

	/**
	 * Returns a suitable description.
	 * 
	 * @param argMngr The argument manager
	 * @param module Module (central/dictionary/local)
	 * @return The name.
	 */
	private static String getDescription(ArgumentManager argMngr, String module) {
		StringBuilder b = new StringBuilder();
		b.append("Copy task");
		b.append(" [environment: ");
		b.append(argMngr.getValue("environment"));
		b.append("] [target: ");
		b.append(argMngr.getValue("target"));
		b.append("] [module: " + module);
		b.append("]");
		return b.toString();
	}

	/**
	 * Add local directories/files to the copy task.
	 * 
	 * @param fc The file copy.
	 * @param argMngr The argument manager
	 * @param dirs Directories/Files.
	 * @param src Base source.
	 * @param dst Base destination.
	 * @param name Name.
	 */
	private static void addLocal(
		FileCopy fc,
		ArgumentManager argMngr,
		boolean dirs,
		String src,
		String dst,
		String name) {
		File fileSrcRoot = new File(getSrcRootLocal(argMngr));
		File fileSrcParent = new File(fileSrcRoot, src);
		File fileSrc = new File(fileSrcParent, name);
		File fileDstRoot = new File(getDstRootLocal(argMngr));
		File fileDstParent = new File(fileDstRoot, dst);
		File fileDst = new File(fileDstParent, name);
		if (dirs) {
			fc.addDirectories(fileSrc, fileDst);
		} else {
			fc.addFiles(fileSrc, fileDst);
		}
	}

	/**
	 * Add local directories to the copy task.
	 * 
	 * @param fc The file copy.
	 * @param argMngr The argument manager
	 * @param src Base source.
	 * @param dst Base destination.
	 * @param name Name.
	 */
	private static void addLocalDirs(FileCopy fc, ArgumentManager argMngr, String src, String dst, String name) {
		addLocal(fc, argMngr, true, src, dst, name);
	}

	/**
	 * Add local files to the copy task.
	 * 
	 * @param fc The file copy.
	 * @param argMngr The argument manager
	 * @param src Base source.
	 * @param dst Base destination.
	 * @param name Name.
	 */
	private static void addLocalFiles(FileCopy fc, ArgumentManager argMngr, String src, String dst, String name) {
		addLocal(fc, argMngr, false, src, dst, name);
	}

	/**
	 * Add the local library copy task
	 * 
	 * @param fc File copy.
	 * @param argMngr The argument manager
	 * @param module Module (CMA_Central/CMA_Dictionary/CMA_Local)
	 */
	private static void addLibrary(FileCopy fc, ArgumentManager argMngr, String module) {
		String srcParent = "XVR COM Lib";
		String dstParent = module + "\\mads\\library";
		addLocalDirs(fc, argMngr, srcParent, dstParent, "bin");
		addLocalDirs(fc, argMngr, srcParent, dstParent, "res");
		addLocalDirs(fc, argMngr, srcParent, dstParent, "xsd");
		addLocalDirs(fc, argMngr, srcParent, dstParent, "xml");
	}

	/**
	 * Add the local budget dictionary module copy task.
	 * 
	 * @param fc File copy.
	 * @param argMngr The argument manager
	 * @param module Module (CMA_Central/CMA_Dictionary/CMA_Local)
	 */
	private static void addModuleBudgetDictionary(FileCopy fc, ArgumentManager argMngr, String module) {
		String srcParent = "XVR COM Module Budget Dictionary";
		String dstParent = module + "\\mads\\module_budget_dictionary";
		// bin
		addLocalDirs(fc, argMngr, srcParent, dstParent, "bin");
		// res
		addLocalFiles(fc, argMngr, srcParent, dstParent, "res\\Budget_Dictionary_DBSchema.txt");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "res\\Budget_Dictionary_Descriptor.txt");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "res\\Budget_Dictionary_Domains.txt");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "res\\Budget_Dictionary_Strings.txt");
		// xml
		addLocalFiles(fc, argMngr, srcParent, dstParent, "xml\\Budget_Dictionary_DBSchema.xml");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "xml\\Budget_Dictionary_Descriptor.xml");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "xml\\Budget_Dictionary_Domains.xml");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "xml\\Budget_Dictionary_Strings.xml");
	}

	/**
	 * Add the local budget local module copy task.
	 * 
	 * @param fc File copy.
	 * @param argMngr The argument manager
	 * @param module Module (CMA_Central/CMA_Dictionary/CMA_Local)
	 * @param menu A boolean to indicate if the menu file should be copied.
	 */
	private static void addModuleBudgetLocal(FileCopy fc, ArgumentManager argMngr, String module, boolean menu) {
		String srcParent = "XVR COM Module Budget Local";
		String dstParent = module + "\\mads\\module_budget_local";
		// bin
		addLocalDirs(fc, argMngr, srcParent, dstParent, "bin");
		// res
		addLocalFiles(fc, argMngr, srcParent, dstParent, "res\\Budget_Local_DBSchema.txt");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "res\\Budget_Local_Descriptor.txt");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "res\\Budget_Local_Domains.txt");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "res\\Budget_Local_Strings.txt");
		// xml
		addLocalFiles(fc, argMngr, srcParent, dstParent, "xml\\Budget_Local_DBSchema.xml");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "xml\\Budget_Local_Descriptor.xml");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "xml\\Budget_Local_Domains.xml");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "xml\\Budget_Local_Strings.xml");
		if (menu) {
			addLocalFiles(fc, argMngr, srcParent, dstParent, "xml\\CMA_Local_Menu.xml");
		}
	}

	/**
	 * Add the local margins central module copy task.
	 * 
	 * @param fc File copy.
	 * @param argMngr The argument manager
	 * @param module Module (CMA_Central/CMA_Dictionary/CMA_Local)
	 * @param menu A boolean to indicate if the menu file should be copied.
	 */
	private static void addModuleMarginsCentral(FileCopy fc, ArgumentManager argMngr, String module, boolean menu) {
		String srcParent = "XVR COM Module Margins Central";
		String dstParent = module + "\\mads\\module_margins_central";
		// bin
		addLocalDirs(fc, argMngr, srcParent, dstParent, "bin");
		// res
		addLocalFiles(fc, argMngr, srcParent, dstParent, "res\\Margins_Central_DBSchema.txt");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "res\\Margins_Central_Descriptor.txt");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "res\\Margins_Central_Domains.txt");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "res\\Margins_Central_Strings.txt");
		// xml
		addLocalFiles(fc, argMngr, srcParent, dstParent, "xml\\Margins_Central_DBSchema.xml");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "xml\\Margins_Central_Descriptor.xml");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "xml\\Margins_Central_Domains.xml");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "xml\\Margins_Central_Strings.xml");
		if (menu) {
			addLocalFiles(fc, argMngr, srcParent, dstParent, "xml\\CMA_Central_Menu.xml");
		}
	}

	/**
	 * Add the local margins dictionary module copy task.
	 * 
	 * @param fc File copy.
	 * @param argMngr The argument manager
	 * @param module Module (CMA_Central/CMA_Dictionary/CMA_Local)
	 * @param menu A boolean to indicate if the menu file should be copied.
	 */
	private static void addModuleMarginsDictionary(FileCopy fc, ArgumentManager argMngr, String module, boolean menu) {
		String srcParent = "XVR COM Module Margins Dictionary";
		String dstParent = module + "\\mads\\module_margins_dictionary";
		// bin
		addLocalDirs(fc, argMngr, srcParent, dstParent, "bin");
		// res
		addLocalFiles(fc, argMngr, srcParent, dstParent, "res\\Margins_Dictionary_DBSchema.txt");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "res\\Margins_Dictionary_Descriptor.txt");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "res\\Margins_Dictionary_Domains.txt");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "res\\Margins_Dictionary_Strings.txt");
		// xml
		addLocalFiles(fc, argMngr, srcParent, dstParent, "xml\\Margins_Dictionary_DBSchema.xml");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "xml\\Margins_Dictionary_Descriptor.xml");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "xml\\Margins_Dictionary_Domains.xml");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "xml\\Margins_Dictionary_Strings.xml");
		if (menu) {
			addLocalFiles(fc, argMngr, srcParent, dstParent, "xml\\Margins_Dictionary_Menu.xml");
		}
	}

	/**
	 * Add the local margins library module copy task.
	 * 
	 * @param fc File copy.
	 * @param argMngr The argument manager
	 * @param module Module (CMA_Central/CMA_Dictionary/CMA_Local)
	 */
	private static void addModuleMarginsLibrary(FileCopy fc, ArgumentManager argMngr, String module) {
		String srcParent = "XVR COM Module Margins Library";
		String dstParent = module + "\\mads\\module_margins_library";
		// bin
		addLocalDirs(fc, argMngr, srcParent, dstParent, "bin");
		// res
		addLocalFiles(fc, argMngr, srcParent, dstParent, "res\\Margins_Library_DBSchema.txt");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "res\\Margins_Library_Descriptor.txt");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "res\\Margins_Library_Domains.txt");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "res\\Margins_Library_Strings.txt");
		// xml
		addLocalFiles(fc, argMngr, srcParent, dstParent, "xml\\Margins_Library_DBSchema.xml");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "xml\\Margins_Library_Descriptor.xml");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "xml\\Margins_Library_Domains.xml");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "xml\\Margins_Library_Strings.xml");
	}

	/**
	 * Add the local margins local module copy task.
	 * 
	 * @param fc File copy.
	 * @param argMngr The argument manager
	 * @param module Module (CMA_Central/CMA_Dictionary/CMA_Local)
	 */
	private static void addModuleMarginsLocal(FileCopy fc, ArgumentManager argMngr, String module) {
		String srcParent = "XVR COM Module Margins Local";
		String dstParent = module + "\\mads\\module_margins_local";
		// bin
		addLocalDirs(fc, argMngr, srcParent, dstParent, "bin");
		// res
		addLocalFiles(fc, argMngr, srcParent, dstParent, "res\\Margins_Local_DBSchema.txt");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "res\\Margins_Local_Descriptor.txt");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "res\\Margins_Local_Domains.txt");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "res\\Margins_Local_Strings.txt");
		// xml
		addLocalFiles(fc, argMngr, srcParent, dstParent, "xml\\Margins_Local_DBSchema.xml");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "xml\\Margins_Local_Descriptor.xml");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "xml\\Margins_Local_Domains.xml");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "xml\\Margins_Local_Strings.xml");
	}

	/**
	 * Add the local security module copy task.
	 * 
	 * @param fc File copy.
	 * @param argMngr The argument manager
	 * @param module Module (CMA_Central/CMA_Dictionary/CMA_Local)
	 */
	private static void addModuleSecurity(FileCopy fc, ArgumentManager argMngr, String module) {
		String srcParent = "XVR COM Module Seguridad";
		String dstParent = module + "\\mads\\module_security";
		// bin
		addLocalDirs(fc, argMngr, srcParent, dstParent, "bin");
		// res
		addLocalFiles(fc, argMngr, srcParent, dstParent, "res\\Module_Seguridad_DBSchema.txt");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "res\\Module_Seguridad_DBSchema_en.txt");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "res\\Module_Seguridad_Descriptor.txt");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "res\\Module_Seguridad_Descriptor_en.txt");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "res\\Module_Seguridad_Strings.txt");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "res\\Module_Seguridad_Strings_en.txt");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "res\\Seguridad_DBSchema.txt");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "res\\Seguridad_DBSchema_en.txt");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "res\\Seguridad_DBSchema_es.txt");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "res\\Seguridad_Descriptor.txt");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "res\\Seguridad_Descriptor_en.txt");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "res\\Seguridad_Descriptor_es.txt");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "res\\Seguridad_Domains.txt");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "res\\Seguridad_Strings.txt");
		// xml
		addLocalFiles(fc, argMngr, srcParent, dstParent, "xml\\Module_Seguridad_Descriptor.xml");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "xml\\Seguridad_DBSchema.xml");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "xml\\Seguridad_Descriptor.xml");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "xml\\Seguridad_Domains.xml");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "xml\\Seguridad_Strings.xml");
	}

	/**
	 * Add the local strategic plan central module copy task.
	 * 
	 * @param fc File copy.
	 * @param argMngr The argument manager
	 * @param module Module (CMA_Central/CMA_Dictionary/CMA_Local)
	 */
	private static void addModuleStrategicPlanCentral(FileCopy fc, ArgumentManager argMngr, String module) {
		String srcParent = "XVR COM Module StrategicPlan Central";
		String dstParent = module + "\\mads\\module_stplan_central";
		// bin
		addLocalDirs(fc, argMngr, srcParent, dstParent, "bin");
		// res
		addLocalFiles(fc, argMngr, srcParent, dstParent, "res\\StrategicPlan_Central_DBSchema.txt");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "res\\StrategicPlan_Central_Descriptor.txt");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "res\\StrategicPlan_Central_Domains.txt");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "res\\StrategicPlan_Central_Strings.txt");
		// xml
		addLocalFiles(fc, argMngr, srcParent, dstParent, "xml\\StrategicPlan_Central_DBSchema.xml");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "xml\\StrategicPlan_Central_Descriptor.xml");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "xml\\StrategicPlan_Central_Domains.xml");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "xml\\StrategicPlan_Central_Strings.xml");
	}

	/**
	 * Add the local strategic plan local module copy task.
	 * 
	 * @param fc File copy.
	 * @param argMngr The argument manager
	 * @param module Module (CMA_Central/CMA_Dictionary/CMA_Local)
	 */
	private static void addModuleStrategicPlanLocal(FileCopy fc, ArgumentManager argMngr, String module) {
		String srcParent = "XVR COM Module StrategicPlan Local";
		String dstParent = module + "\\mads\\module_stplan_local";
		// bin
		addLocalDirs(fc, argMngr, srcParent, dstParent, "bin");
		// res
		addLocalFiles(fc, argMngr, srcParent, dstParent, "res\\StrategicPlan_Local_DBSchema.txt");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "res\\StrategicPlan_Local_Descriptor.txt");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "res\\StrategicPlan_Local_Domains.txt");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "res\\StrategicPlan_Local_Strings.txt");
		// xml
		addLocalFiles(fc, argMngr, srcParent, dstParent, "xml\\StrategicPlan_Local_DBSchema.xml");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "xml\\StrategicPlan_Local_Descriptor.xml");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "xml\\StrategicPlan_Local_Domains.xml");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "xml\\StrategicPlan_Local_Strings.xml");
	}

	/**
	 * Add the local working capital central module copy task.
	 * 
	 * @param fc File copy.
	 * @param argMngr The argument manager
	 * @param module Module (CMA_Central/CMA_Dictionary/CMA_Local)
	 */
	private static void addModuleWorkingCapitalCentral(FileCopy fc, ArgumentManager argMngr, String module) {
		String srcParent = "XVR COM Module WorkingCapital Central";
		String dstParent = module + "\\mads\\module_wcapital_central";
		// bin
		addLocalDirs(fc, argMngr, srcParent, dstParent, "bin");
		// res
		addLocalFiles(fc, argMngr, srcParent, dstParent, "res\\WorkingCapital_Central_DBSchema.txt");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "res\\WorkingCapital_Central_Descriptor.txt");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "res\\WorkingCapital_Central_Domains.txt");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "res\\WorkingCapital_Central_Strings.txt");
		// xml
		addLocalFiles(fc, argMngr, srcParent, dstParent, "xml\\WorkingCapital_Central_DBSchema.xml");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "xml\\WorkingCapital_Central_Descriptor.xml");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "xml\\WorkingCapital_Central_Domains.xml");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "xml\\WorkingCapital_Central_Strings.xml");
	}

	/**
	 * Add the local working capital local module copy task.
	 * 
	 * @param fc File copy.
	 * @param argMngr The argument manager
	 * @param module Module (CMA_Central/CMA_Dictionary/CMA_Local)
	 */
	private static void addModuleWorkingCapitalLocal(FileCopy fc, ArgumentManager argMngr, String module) {
		String srcParent = "XVR COM Module WorkingCapital Local";
		String dstParent = module + "\\mads\\module_wcapital_local";
		// bin
		addLocalDirs(fc, argMngr, srcParent, dstParent, "bin");
		// res
		addLocalFiles(fc, argMngr, srcParent, dstParent, "res\\WorkingCapital_Local_DBSchema.txt");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "res\\WorkingCapital_Local_Descriptor.txt");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "res\\WorkingCapital_Local_Domains.txt");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "res\\WorkingCapital_Local_Strings.txt");
		// xml
		addLocalFiles(fc, argMngr, srcParent, dstParent, "xml\\WorkingCapital_Local_DBSchema.xml");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "xml\\WorkingCapital_Local_Descriptor.xml");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "xml\\WorkingCapital_Local_Domains.xml");
		addLocalFiles(fc, argMngr, srcParent, dstParent, "xml\\WorkingCapital_Local_Strings.xml");
	}

	/**
	 * Add directories for a remote copy task.
	 * 
	 * @param fc The file copy.
	 * @param argMngr The argument manager
	 * @param parent Source (for instance CMA_Central\\mads)
	 * @param name Last directory name.
	 * @param drive Destination drive.
	 */
	private static void addRemoteDirs(FileCopy fc, ArgumentManager argMngr, String parent, String name, String drive) {
		File fileSrcRoot = new File(getSrcRootRemote(argMngr));
		File fileSrcParent = new File(fileSrcRoot, parent);
		File fileSrc = new File(fileSrcParent, name);
		File fileDstParent = new File(
			drive + ":" + (argMngr.getValue("target").equals("production") ? "\\CMA\\" : "\\") + parent);
		File fileDst = new File(fileDstParent, name);
		fc.addDirectories(fileSrc, fileDst);
	}

	/**
	 * Add files for a remote copy task.
	 * 
	 * @param fc The file copy.
	 * @param argMngr The argument manager
	 * @param parent Source (for instance CMA_Central\\mads)
	 * @param name Last directory name.
	 * @param drive Destination drive.
	 */
	private static void addRemoteFiles(FileCopy fc, ArgumentManager argMngr, String parent, String name, String drive) {
		File fileSrcRoot = new File(getSrcRootRemote(argMngr));
		File fileSrcParent = new File(fileSrcRoot, parent);
		File fileSrc = new File(fileSrcParent, name);
		File fileDstParent = new File(
			drive + ":" + (argMngr.getValue("target").equals("production") ? "\\CMA\\" : "\\") + parent);
		File fileDst = new File(fileDstParent, name);
		fc.addFiles(fileSrc, fileDst);
	}

	/**
	 * Returns the list of tasks to update.
	 * 
	 * @param argMngr The argument manager.
	 * @return The list of tasks.
	 */
	private static List<Task> getTaskList(ArgumentManager argMngr) {
		// A task per central/dictionary/local.
		List<Task> taskTests = new ArrayList<>();

		// Environment and target.
		boolean purge = argMngr.isPassed("purge");
		boolean local = argMngr.getValue("target").equals("local");
		boolean remote = !local;

		// Local: copy from workspace to execution image.
		if (local) {

			// Central.
			if (argMngr.getValues("modules").contains("central")) {
				FileCopy fc = new FileCopy(Session.getSession());
				fc.setTitle(getDescription(argMngr, "central"));
				fc.setPurgeDestination(purge);
				addLibrary(fc, argMngr, "CMA_Central");
				addModuleBudgetDictionary(fc, argMngr, "CMA_Central");
				addModuleBudgetLocal(fc, argMngr, "CMA_Central", false);
				addModuleMarginsCentral(fc, argMngr, "CMA_Central", true);
				addModuleMarginsDictionary(fc, argMngr, "CMA_Central", false);
				addModuleMarginsLibrary(fc, argMngr, "CMA_Central");
				addModuleMarginsLocal(fc, argMngr, "CMA_Central");
				addModuleStrategicPlanCentral(fc, argMngr, "CMA_Central");
				addModuleStrategicPlanLocal(fc, argMngr, "CMA_Central");
				addModuleWorkingCapitalCentral(fc, argMngr, "CMA_Central");
				addModuleWorkingCapitalLocal(fc, argMngr, "CMA_Central");
				addModuleSecurity(fc, argMngr, "CMA_Central");
				taskTests.add(fc);
			}

			// Dictionary
			if (argMngr.getValues("modules").contains("dictionary")) {
				FileCopy fc = new FileCopy(Session.getSession());
				fc.setTitle(getDescription(argMngr, "dictionary"));
				fc.setPurgeDestination(purge);
				addLibrary(fc, argMngr, "CMA_Dictionary");
				addModuleBudgetDictionary(fc, argMngr, "CMA_Dictionary");
				addModuleMarginsCentral(fc, argMngr, "CMA_Dictionary", false);
				addModuleMarginsDictionary(fc, argMngr, "CMA_Dictionary", true);
				addModuleMarginsLibrary(fc, argMngr, "CMA_Dictionary");
				addModuleMarginsLocal(fc, argMngr, "CMA_Dictionary");
				addModuleSecurity(fc, argMngr, "CMA_Dictionary");
				taskTests.add(fc);
			}

			// Local.
			if (argMngr.getValues("modules").contains("local")) {
				FileCopy fc = new FileCopy(Session.getSession());
				fc.setTitle(getDescription(argMngr, "local"));
				fc.setPurgeDestination(purge);
				addLibrary(fc, argMngr, "CMA_Local");
				addModuleBudgetDictionary(fc, argMngr, "CMA_Local");
				addModuleBudgetLocal(fc, argMngr, "CMA_Local", true);
				addModuleMarginsCentral(fc, argMngr, "CMA_Local", false);
				addModuleMarginsDictionary(fc, argMngr, "CMA_Local", false);
				addModuleMarginsLibrary(fc, argMngr, "CMA_Local");
				addModuleMarginsLocal(fc, argMngr, "CMA_Local");
				addModuleStrategicPlanLocal(fc, argMngr, "CMA_Local");
				addModuleWorkingCapitalCentral(fc, argMngr, "CMA_Local");
				addModuleWorkingCapitalLocal(fc, argMngr, "CMA_Local");
				addModuleSecurity(fc, argMngr, "CMA_Local");
				taskTests.add(fc);
			}
		}

		// Remote: copy from execution image to destination drives.
		if (remote) {

			// The list of destination drives.
			List<String> drives = new ArrayList<>();
			if (argMngr.getValue("environment").equals("production")) {
				drives.addAll(Lists.asList("L", "M", "N", "O", "P", "Q", "R", "S", "T"));
			} else if (argMngr.getValue("environment").equals("quality")) {
				drives.addAll(Lists.asList("T"));
			}

			// Central.
			if (argMngr.getValues("modules").contains("central")) {
				for (String drive : drives) {
					FileCopy fc = new FileCopy(Session.getSession());
					fc.setTitle(getDescription(argMngr, "central") + " (" + drive + ")");
					fc.setPurgeDestination(purge);
					addRemoteDirs(fc, argMngr, "CMA_Central\\mads", "library", drive);
					addRemoteDirs(fc, argMngr, "CMA_Central\\mads", "module_budget_dictionary", drive);
					addRemoteDirs(fc, argMngr, "CMA_Central\\mads", "module_budget_local", drive);
					addRemoteDirs(fc, argMngr, "CMA_Central\\mads", "module_margins_central", drive);
					addRemoteDirs(fc, argMngr, "CMA_Central\\mads", "module_margins_dictionary", drive);
					addRemoteDirs(fc, argMngr, "CMA_Central\\mads", "module_margins_library", drive);
					addRemoteDirs(fc, argMngr, "CMA_Central\\mads", "module_margins_local", drive);
					addRemoteDirs(fc, argMngr, "CMA_Central\\mads", "module_security", drive);
					addRemoteDirs(fc, argMngr, "CMA_Central\\mads", "module_stplan_central", drive);
					addRemoteDirs(fc, argMngr, "CMA_Central\\mads", "module_stplan_local", drive);
					addRemoteDirs(fc, argMngr, "CMA_Central\\mads", "module_wcapital_central", drive);
					addRemoteDirs(fc, argMngr, "CMA_Central\\mads", "module_wcapital_local", drive);
					addRemoteFiles(fc, argMngr, "CMA_Central", "CMA_Central.cmd", drive);
					addRemoteFiles(fc, argMngr, "CMA_Central", "JLoad.cmd", drive);
					addRemoteFiles(fc, argMngr, "CMA_Central", "JLoad_res.cmd", drive);
					taskTests.add(fc);
				}
			}

			// Dictionary.
			if (argMngr.getValues("modules").contains("dictionary")) {
				for (String drive : drives) {
					FileCopy fc = new FileCopy(Session.getSession());
					fc.setTitle(getDescription(argMngr, "dictionary") + " (" + drive + ")");
					fc.setPurgeDestination(purge);
					addRemoteDirs(fc, argMngr, "CMA_Dictionary\\mads", "library", drive);
					addRemoteDirs(fc, argMngr, "CMA_Dictionary\\mads", "module_budget_dictionary", drive);
					addRemoteDirs(fc, argMngr, "CMA_Dictionary\\mads", "module_margins_central", drive);
					addRemoteDirs(fc, argMngr, "CMA_Dictionary\\mads", "module_margins_dictionary", drive);
					addRemoteDirs(fc, argMngr, "CMA_Dictionary\\mads", "module_margins_library", drive);
					addRemoteDirs(fc, argMngr, "CMA_Dictionary\\mads", "module_margins_local", drive);
					addRemoteDirs(fc, argMngr, "CMA_Dictionary\\mads", "module_security", drive);
					addRemoteFiles(fc, argMngr, "CMA_Dictionary", "CMA_Dictionary.cmd", drive);
					addRemoteFiles(fc, argMngr, "CMA_Dictionary", "JLoad.cmd", drive);
					addRemoteFiles(fc, argMngr, "CMA_Central", "JLoad_res.cmd", drive);
					taskTests.add(fc);
				}
			}

			// Local.
			if (argMngr.getValues("modules").contains("local")) {
				for (String drive : drives) {
					FileCopy fc = new FileCopy(Session.getSession());
					fc.setTitle(getDescription(argMngr, "local") + " (" + drive + ")");
					fc.setPurgeDestination(purge);
					addRemoteDirs(fc, argMngr, "CMA_Local\\mads", "library", drive);
					addRemoteDirs(fc, argMngr, "CMA_Local\\mads", "module_budget_dictionary", drive);
					addRemoteDirs(fc, argMngr, "CMA_Local\\mads", "module_budget_local", drive);
					addRemoteDirs(fc, argMngr, "CMA_Local\\mads", "module_margins_central", drive);
					addRemoteDirs(fc, argMngr, "CMA_Local\\mads", "module_margins_dictionary", drive);
					addRemoteDirs(fc, argMngr, "CMA_Local\\mads", "module_margins_library", drive);
					addRemoteDirs(fc, argMngr, "CMA_Local\\mads", "module_margins_local", drive);
					addRemoteDirs(fc, argMngr, "CMA_Local\\mads", "module_security", drive);
					addRemoteDirs(fc, argMngr, "CMA_Local\\mads", "module_stplan_local", drive);
					addRemoteDirs(fc, argMngr, "CMA_Local\\mads", "module_wcapital_central", drive);
					addRemoteDirs(fc, argMngr, "CMA_Local\\mads", "module_wcapital_local", drive);
					addRemoteFiles(fc, argMngr, "CMA_Local", "CMA_Local.cmd", drive);
					addRemoteFiles(fc, argMngr, "CMA_Local", "JLoad.cmd", drive);
					addRemoteFiles(fc, argMngr, "CMA_Central", "JLoad_res.cmd", drive);
					taskTests.add(fc);
				}
			}
		}

		return taskTests;
	}

}
