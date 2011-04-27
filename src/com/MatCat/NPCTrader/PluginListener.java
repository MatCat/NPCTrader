package com.MatCat.NPCTrader;

import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.PluginEvent;
import org.bukkit.event.server.ServerListener;

import com.nijiko.coelho.iConomy.iConomy;
import org.bukkit.plugin.Plugin;

/**
 * Checks for plugins whenever one is enabled
 */
public class PluginListener extends ServerListener {
	public PluginListener() {
	}

	public void onPluginEnable(PluginEnableEvent event) {
		if (NPCTrader.getiConomy() == null) {
			Plugin iConomy = NPCTrader.getBukkitServer().getPluginManager()
					.getPlugin("iConomy");

			if (iConomy != null) {
				if (iConomy.isEnabled()) {
					NPCTrader.setiConomy((iConomy) iConomy);
					System.out
							.println("NPCTrader Successfully linked with iConomy.");
					NPCTrader.ensureEnabled(NPCTrader.getBukkitServer());
				}
			}
		}
	}
}