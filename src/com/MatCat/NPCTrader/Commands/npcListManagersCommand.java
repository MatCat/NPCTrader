package com.MatCat.NPCTrader.Commands;

import java.sql.ResultSet;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.MatCat.NPCTrader.NPCTrader;

public class npcListManagersCommand extends npcManagerCommand {

	public npcListManagersCommand(NPCTrader plugin, CommandSender sender,
			Command c, String commandLabel, String[] args,
			String PermissionNode, Boolean bNPCInteract) {
		super(plugin, sender, c, commandLabel, args, PermissionNode,
				bNPCInteract);
	}

	public Boolean Run() {
		if (!super.Run("")) {
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
		TotalPages = (int) Math.ceil((float) NPCDB.GetTotalManagers(NPCID,
				player.getName()) / 8.0);
		if (TotalPages == 0)
			TotalPages = 1;
		String Header = "Manager Page %p of " + TotalPages;
		String SlotList = ChatColor.GREEN + "[" + ChatColor.YELLOW + "%n"
				+ ChatColor.GREEN + "][" + ChatColor.WHITE + "%p"
				+ ChatColor.GREEN + "]";
		try {
			ResultSet RS = NPCDB.GetManagerList(NPCID, Page);
			String SendMSG = Header.replaceAll("%p", String.valueOf(Page));
			sender.sendMessage(SendMSG);
			Boolean IsOwner = NPCDB.CheckOwner(NPCID, player.getName());
			while (RS.next()) {
				String sTemp = SlotList;
				sTemp = sTemp.replaceAll("%n", RS.getString("ManagerName"));
				String sT = "";
				if (RS.getBoolean("CanStock"))
					sT = sT + "Stocking, ";
				if (RS.getBoolean("CanUnStock"))
					sT = sT + "Unstocking, ";
				if (RS.getBoolean("CanMove"))
					sT = sT + "Moving, ";
				if (RS.getBoolean("CanPrice"))
					sT = sT + "Pricing, ";
				if (RS.getBoolean("CanStock"))
					sT = sT + "Messages, ";
				sTemp = sTemp
						.replaceAll("%p", sT.substring(0, sT.length() - 2));

				if (IsOwner
						|| RS.getString("ManagerName").equalsIgnoreCase(
								player.getName()))
					sender.sendMessage(sTemp);
			}
			return true;

		} catch (Exception e) {
			sender.sendMessage("Database error trying to retrieve manager list.");
			return true;
		}

	}
}
