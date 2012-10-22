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

		// YAML does not support +++
		serverZone = config.getInt("Server-Zone");

		try {
			File players = new File(plugin.getDataFolder(), "playerTimes.txt");
			Scanner scan = new Scanner(players);

			while (scan.hasNextLine()) {

				String[] line = scan.nextLine().split("=");
				String name = line[0];
				int zone = Integer.parseInt(line[1]);
				int display = 24;
				try {
					display = Integer.parseInt(line[2]);
				} catch (Exception e) {
				}
				plugin.addPlayers(name, zone, display);
			}
		} catch (FileNotFoundException e) {
		}
	}

	public void save() {
		try {
			BufferedWriter b = new BufferedWriter(new FileWriter(players));
			for (Entry<String, Integer> m : plugin.timeZones.entrySet()) {

				b.write(m.getKey() + "=" + m.getValue() + "="
						+ plugin.displayMode.get(m.getKey()));
				b.newLine();
			}
			b.close();
		} catch (IOException e) {
		}
	}

	public int getServerZone() {
		return serverZone;
	}

}
