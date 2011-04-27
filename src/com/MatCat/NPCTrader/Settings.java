package com.MatCat.NPCTrader;

import java.util.*;
import org.bukkit.ChatColor;
import org.bukkit.util.config.Configuration;

public class Settings {
	private Configuration config;

	public Settings(Configuration config) {
		this.config = config;
	}

	public String DB(String DBSetting) {
		return config.getString("DB." + DBSetting, "");
	}

	public Double Prices(String PriceSetting) {
		return config.getDouble("Prices." + PriceSetting, 0);
	}

	public String NPC(String NPCSetting) {
		return config.getString("NPC." + NPCSetting, "");
	}

	public int MaxNPC(String Group, String Playername) {
		int PlayerCount = config.getInt("MaxNPCPlayersCount." + Playername, -2);
		if (PlayerCount == -2) {
			int GroupCount = config.getInt("MaxNPCGroupsCount." + Group, -1);
			System.out.println("Checking " + Playername + " for " + Group
					+ " returns " + GroupCount);
			return GroupCount;
		} else {
			return PlayerCount;
		}
	}

	public int UnitStack() {
		return config.getInt("UnitStack", 64);
	}
	// config.get<type>("settingname","default");

}
