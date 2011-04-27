package com.MatCat.NPCTrader.Commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.martin.bukkit.npclib.NPCEntity;


import com.MatCat.NPCTrader.NPCTrader;
import com.MatCat.NPCTrader.NPCTraderMySQL;

class npcCommand {
	CommandSender sender;
	Command c;
	String[] args;
	String commandLabel;
	NPCTraderMySQL NPCDB;
	Boolean isRun = false;
	String subCommand;
	Player player;
	Location l;
	String PermissionNode;
	NPCTrader plugin;
	Boolean isPermissions = false;
	Boolean bNPCInteract = false;
	NPCEntity npc;
	int NPCID = 0;

	public npcCommand(NPCTrader p, CommandSender s, String cl, String[] a,
			String pn, Boolean b) {
		plugin = p;
		NPCDB = plugin.NPCDB;
		sender = s;
		// c = co;
		commandLabel = cl;
		args = a;
		PermissionNode = pn;
		bNPCInteract = b;
	}

	Boolean Run() {
		// NPCDB = new NPCTraderMySQL();
		player = (Player) sender;
		l = player.getLocation();
		try {
			subCommand = args[0].toLowerCase();
		} catch (Exception e1) {
		}
		try {
			boolean dbc = plugin.NPCDB.dbConnect();
			isRun = true;
		} catch (Exception e) {
			System.out.println("NPC Trader Database Error: " + e.getMessage());
			return false;
		}

		if (args.length < 1) {
			sender.sendMessage("To interact with an NPC Right click on it");
			sender.sendMessage("Use /npc <command> as listed below");
			sender.sendMessage("for more specific help");
			sender.sendMessage("");
			sender.sendMessage("list, buy, sell, create, ");
			sender.sendMessage("remove, stock, unstock,");
			sender.sendMessage("addmanager, addowner,");
			sender.sendMessage("banker, setup, adjust, rename, ");
			sender.sendMessage("message, move, setmanager, upgrade");
			return true;
		}
		if (plugin.UsePermissions) {
			// System.out.println("Checking Permissions");
			if (!plugin.Permissions.has(player, "npc.admin")) {
				if (!plugin.Permissions.has(player, PermissionNode)) {
					sender.sendMessage("You do not have permission to use that command!");
					// System.out.println("Failed user permission check for "
					// + "permissions." + PermissionNode);
					return false;
				}
			}
		}
		try {
			NPCID = Integer.parseInt((String) plugin.NPCInteract.get(player
					.getName()));
		} catch (Exception e) {
			NPCID = 0;
		}
		if (NPCID != 0) {
			if (bNPCInteract) {
				try {
					npc = plugin.npcm.getNPC((Integer.toString(NPCID)));
				} catch (Exception e) {
					sender.sendMessage("You must first right click on an NPC.");
					return false;
				}
			}
		}

		return true;
	}

	protected void finalize() throws Throwable {
		try {
			NPCDB.dbClose(); // close open files
		} finally {
			super.finalize();
		}
	}

}
