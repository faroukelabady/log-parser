package com.ef.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class DbConnectionProperties {
	
	public static Properties readProperties() {
		Properties configProperties = new Properties();
		Path internalPath = Paths.get("/config.properties");
		Path externalPath = Paths.get("./config.properties");

		try(InputStream configuration = getConfiguration(externalPath, internalPath)) {
			configProperties.load(configuration);
		} catch (Exception e) {
			createDefaultProperties(configProperties);
		} 
		
		return configProperties;
	}

	private static InputStream getConfiguration(Path externalPath, Path internalPath) {
		InputStream configuration = getExternalConfiguration(externalPath);
		if (configuration == null) {
			configuration = Utilities.class.getResourceAsStream(internalPath.toString());
		}
		return configuration;
	}

	private static InputStream getExternalConfiguration(Path externalPath) {
		try {
			if (Files.exists(externalPath)) {
				return Files.newInputStream(externalPath);
			}
		} catch (IOException e) {
			return null;
		}
		return null;
	}
	
	private static void createDefaultProperties(Properties mainProperties) {
		mainProperties.put("jdbc.url", "jdbc:mysql://localhost:3306/logs"
				+ "?useUnicode=true&characterEncoding=UTF-8&rewriteBatchedStatements=true");
		mainProperties.put("jdbc.username", "root");
		mainProperties.put("jdbc.password", "root");
	}

}
