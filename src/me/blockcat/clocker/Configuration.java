package me.blockcat.clocker;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.Scanner;

import org.bukkit.configuration.file.FileConfiguration;

public class Configuration {

	private Clocker plugin;

	private int serverZone = 0;
	private FileConfiguration config;
	private File players;

	public Configuration(Clocker clocker) {
		plugin = clocker;
		config = plugin.getConfig();
		players = new File(plugin.getDataFolder(), "playerTimes.txt");
	}

	public void load() {
		if (!config.contains("Server-Zone")) {
			config.set("Server-Zone", 0);
			plugin.saveConfig();
		}
		serverZone = config.getInt("Server-Zone");
	}


	public int getServerZone() {
		return serverZone;
	}

}
