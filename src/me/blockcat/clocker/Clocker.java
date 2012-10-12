package me.blockcat.clocker;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

public class Clocker extends JavaPlugin {

	//public HashMap<String, Integer> timeZones = new HashMap<String, Integer>();
	private DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	private DateFormat daysFormat = new SimpleDateFormat("MM/dd");
	private Configuration config;

	@Override
	public void onEnable() {
		config = new Configuration(this);
		config.load();
	}
	
	@Override
	public void onDisable() {
		//config.save();
	}

	public String getTime() {
		String time = formatter.format(new Date());
		return time;
	}

	public String getPlayerTime (Player p, String h) {
		return getPlayerTime(p,h,"00");
	}

	public String getPlayerTime (Player p, String h, String m) {
		return getPlayerTime(p, h, m, "00");
	}

	public String getPlayerTime(Player p, String h, String m, String s) {
		int hour = Integer.parseInt(h);
		int minute = Integer.parseInt(m);
		int second = Integer.parseInt(s);
		return getPlayerTime(p, hour, minute, second);
	}

	public String getPlayerTime(Player p, int h, int m, int s) {
		String time = "";
		String[] days = daysFormat.format(new Date()).split("/");
		h = h - config.getServerZone() + p.getMetadata("timezone").get(0).asInt();
		int day = Integer.parseInt(days[0]);
		int month = Integer.parseInt(days[1]);
		System.out.println(h/24);
		if (h / 24 >= 1) {
			day += h/24;
			h -= (h / 24) * 24;
		}
		if (day >= Datam.getDaysbyId(month)) {
			day -= Datam.getDaysbyId(month);
			month ++;
		}
		if (month >= 13) {
			month -= 12;
		}
		NumberFormat f = new DecimalFormat("00");
		String h2 = f.format(h);
		String m2 = f.format(m);
		String s2 = f.format(s);

		time = h2+":"+m2+":"+s2 + " - " + day + "/" + month;

		return time;
	}
	
	public String getServerTime (Player p, String h) {
		return getServerTime(p,h,"00");
	}

	public String getServerTime (Player p, String h, String m) {
		return getServerTime(p, h, m, "00");
	}

	public String getServerTime(Player p, String h, String m, String s) {
		int hour = Integer.parseInt(h);
		int minute = Integer.parseInt(m);
		int second = Integer.parseInt(s);
		return getPlayerTime(p, hour, minute, second);
	}

	public String getServerTime(Player p, int h, int m, int s) {
		String time = "";
		String[] days = daysFormat.format(new Date()).split("/");
		h = h + config.getServerZone() - p.getMetadata("timezone").get(0).asInt();
		int day = Integer.parseInt(days[0]);
		int month = Integer.parseInt(days[1]);
		System.out.println(h/24);
		if (h / 24 >= 1) {
			day += h/24;
			h -= (h / 24) * 24;
		}
		if (day >= Datam.getDaysbyId(month)) {
			day -= Datam.getDaysbyId(month);
			month ++;
		}
		if (month >= 13) {
			month -= 12;
		}
		NumberFormat f = new DecimalFormat("00");
		String h2 = f.format(h);
		String m2 = f.format(m);
		String s2 = f.format(s);

		time = h2+":"+m2+":"+s2 + " - " + day + "/" + month;

		return time;
	}

	/*public void addPlayers (String player, int zone) {
		timeZones.put(player, zone);
	}*/

	@Override
	public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] args) {
		if (cmnd.getName().equalsIgnoreCase("clock")) {
			if ( args.length == 0) {
				cs.sendMessage(ChatColor.AQUA + "The time is:");
				cs.sendMessage(ChatColor.GREEN + getTime());
			}


			if (args.length >= 1) {
				if (! (cs instanceof Player)) {
					return true;
				}
				Player p = (Player)cs;
				if (args[0].equalsIgnoreCase("zone")) {
					if (args.length == 1) {
						p.sendMessage(ChatColor.RED + "Please fill in your timezone. (GMT)");
					} else {
						try {
						int z = Integer.parseInt(args[1]);
						//timeZones.put(p.getName(), z);
						p.setMetadata("timezone", new FixedMetadataValue(this, z));
						p.sendMessage((String) (ChatColor.YELLOW + "Your timezone has been set to: " + (z >=  0 ? ("+" + z) : z)));
						} catch(Exception e) {
							p.sendMessage("Not a vaild number.");
						}
					}
				} else if (args[0].equalsIgnoreCase("help")) {
					p.sendMessage(ChatColor.AQUA + "~~~~"+ getTime() + "~~~~" );
					p.sendMessage(ChatColor.GOLD + "/clock zone <#> " + ChatColor.DARK_GREEN + ": Sets your timezone, GMT based.");					
					p.sendMessage(ChatColor.GOLD + "/clock tmt <hh:mm:ss> " + ChatColor.DARK_GREEN + ": Converts server time to your time.");
					p.sendMessage(ChatColor.GOLD + "/clock tst <hh:mm:ss> " + ChatColor.DARK_GREEN + ": Converts your time to server time.");

				} else if (args[0].equalsIgnoreCase("tmt")) {
					NumberFormat f = new DecimalFormat("00");
					if (args.length >= 2) {
						if (p.hasMetadata("timezone")) {
							// Send his time.  !! own timezone + server timezone
							String wonderTime = "";
							String[] times = args[1].split(":");
							String playerTime = "";
							if (times.length == 1) {
								try {
								playerTime = getPlayerTime(p, times[0]);
								String h2 = f.format(Integer.parseInt(times[0]));
								wonderTime = h2 +":00:00";
								} catch(Exception e) {
									p.sendMessage("Not a vaild number.");
								}
							} else if (times.length == 2) {
								try {
								playerTime = getPlayerTime(p, times[0], times[1]);
								String h2 = f.format(Integer.parseInt(times[0]));
								String m2 = f.format(Integer.parseInt(times[1]));
								wonderTime = h2 +":" + m2 + ":00";
								} catch(Exception e) {
									p.sendMessage("Not a vaild number.");
								}
							} else if (times.length == 3) {
								try {
								playerTime = getPlayerTime(p, times[0], times[1], times[2]);
								String h2 = f.format(Integer.parseInt(times[0]));
								String m2 = f.format(Integer.parseInt(times[1]));
								String s2 = f.format(Integer.parseInt(times[2]));

								wonderTime = h2 +":" + m2 + ":" + s2;
								} catch(Exception e) {
									p.sendMessage("Not a vaild number.");
								}
							}
							wonderTime += " - " + daysFormat.format(new Date());
							p.sendMessage(ChatColor.AQUA + "Server time : " + wonderTime);
							p.sendMessage(ChatColor.GOLD + "Your time: " + playerTime);



						} else {
							p.sendMessage(ChatColor.RED + "You haven't configured your timezone yet.");
						}
					} else {
						p.sendMessage(ChatColor.RED + "Please fill in a time.");
					}
				}
				else if (args[0].equalsIgnoreCase("tst")) {
					NumberFormat f = new DecimalFormat("00");
					if (args.length >= 2) {
						if (p.hasMetadata("timezone")) {
							// Send his time.  !! own timezone + server timezone
							String wonderTime = "";
							String[] times = args[1].split(":");
							String serverTime = "";
							if (times.length == 1) {
								serverTime = getServerTime(p, times[0]);
								String h2 = f.format(Integer.parseInt(times[0]));
								wonderTime = h2 +":00:00";
							} else if (times.length == 2) {
								serverTime = getServerTime(p, times[0], times[1]);
								String h2 = f.format(Integer.parseInt(times[0]));
								String m2 = f.format(Integer.parseInt(times[1]));
								wonderTime = h2 +":" + m2 + ":00";
							} else if (times.length == 3) {
								serverTime = getServerTime(p, times[0], times[1], times[2]);
								String h2 = f.format(Integer.parseInt(times[0]));
								String m2 = f.format(Integer.parseInt(times[1]));
								String s2 = f.format(Integer.parseInt(times[2]));

								wonderTime = h2 +":" + m2 + ":" + s2;
							}
							wonderTime += " - " + daysFormat.format(new Date());
							p.sendMessage(ChatColor.AQUA + "Your time : " + wonderTime);
							p.sendMessage(ChatColor.GOLD + "Server time: " + serverTime);



						} else {
							p.sendMessage(ChatColor.RED + "You haven't configured your timezone yet.");
						}
					} else {
						p.sendMessage(ChatColor.RED + "Please fill in a time.");
					}

				}
			}

		}

		return true;
	}
}
