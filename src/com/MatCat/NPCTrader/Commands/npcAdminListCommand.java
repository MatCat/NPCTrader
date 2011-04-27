package com.MatCat.NPCTrader.Commands;

import java.sql.ResultSet;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.MatCat.NPCTrader.NPCTrader;

public class npcAdminListCommand extends npcAdminCommand {

	public npcAdminListCommand(NPCTrader plugin, CommandSender sender,
			Command c, String commandLabel, String[] args,
			String PermissionNode, Boolean bNPCInteract) {
		super(plugin, sender, c, commandLabel, args, PermissionNode,
				bNPCInteract);
		// TODO Auto-generated constructor stub
	}

	public Boolean Run() {
		if (!super.Run()) {
			return true;
		}
		int Page = 1;
		int TotalPages = 1;
		try {
			if (Integer.parseInt(args[1]) > 1) {
				Page = Integer.parseInt(args[1]);
				// System.out.println("Paginating to "
				// + Page);
			}
		} catch (Exception e) {
			// e.printStackTrace();
		}
		TotalPages = (int) Math.ceil((float) NPCDB.GetTotalNPCs() / 8.0);
		if (TotalPages == 0)
			TotalPages = 1;
		String Header = "NPC List - Page %p of " + TotalPages;
		String NPCList = ChatColor.GREEN + "[" + ChatColor.YELLOW
				+ plugin.getSettings().NPC("Prefix") + "%n"
				+ plugin.getSettings().NPC("Suffix") + ChatColor.GREEN + "]["
				+ ChatColor.WHITE + "B" + ChatColor.AQUA + ":"
				+ ChatColor.WHITE + "%b" + ChatColor.GREEN + "]["
				+ ChatColor.WHITE + "X" + ChatColor.AQUA + ":"
				+ ChatColor.WHITE + "%x" + ChatColor.GREEN + "]["
				+ ChatColor.WHITE + "Y" + ChatColor.AQUA + ":"
				+ ChatColor.WHITE + "%y" + ChatColor.GREEN + "]["
				+ ChatColor.WHITE + "Z" + ChatColor.AQUA + ":"
				+ ChatColor.WHITE + "%z" + ChatColor.GREEN + "]["
				+ ChatColor.WHITE + "S" + ChatColor.AQUA + ":"
				+ ChatColor.WHITE + "%s" + ChatColor.GREEN + "]["
				+ ChatColor.WHITE + "U" + ChatColor.AQUA + ":"
				+ ChatColor.WHITE + "%u" + ChatColor.GREEN + "]["
				+ ChatColor.WHITE + "W" + ChatColor.AQUA + ":"
				+ ChatColor.WHITE + "%w" + ChatColor.GREEN + "]";
		try {
			ResultSet RS = NPCDB.GetNPCList(Page);
			String SendMSG = Header.replaceAll("%p", String.valueOf(Page));
			sender.sendMessage(SendMSG);
			while (RS.next()) {
				String sTemp = NPCList;
				sTemp = sTemp.replaceAll("%n", RS.getString("NPCName"));
				sTemp = sTemp.replaceAll("%b", RS.getString("Banker"));
				sTemp = sTemp.replaceAll("%x",
						Integer.toString(RS.getInt("PosX")));
				sTemp = sTemp.replaceAll("%y",
						Integer.toString(RS.getInt("PosY")));
				sTemp = sTemp.replaceAll("%z",
						Integer.toString(RS.getInt("PosZ")));
				sTemp = sTemp.replaceAll("%s",
						Integer.toString(RS.getInt("ItemSlots")));
				sTemp = sTemp.replaceAll("%u",
						Integer.toString(RS.getInt("Units")));
				sTemp = sTemp.replaceAll("%w", RS.getString("WorldName"));

				sender.sendMessage(sTemp);
			}
			return true;
		} catch (Exception e) {
			sender.sendMessage("Database error trying to read NPC List.");
			return true;
		}

	}
}
