package com.MatCat.NPCTrader.Commands;

import java.sql.ResultSet;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.MatCat.NPCTrader.NPCTrader;

public class npcListCommand extends npcCommand {

	public npcListCommand(NPCTrader plugin, CommandSender sender,
			String commandLabel, String[] args, String PermissionNode,
			Boolean bNPCInteract) {
		super(plugin, sender, commandLabel, args, PermissionNode, bNPCInteract);
	}

	public Boolean Run() {
		if (!super.Run()) {
			return true;
		}
		return ShowList();
	}

	public Boolean ShowList() {
		// Master sale list
		int Page = 1;
		int TotalPages = 1;
		try {
			if (Integer.parseInt(args[1]) > 1) {
				Page = Integer.parseInt(args[1]);
				// System.out.println("Paginating to " + Page);
			}
		} catch (Exception e) {
			// e.printStackTrace();
			Page = 1;
		}

		TotalPages = (int) Math.ceil((float) NPCDB.GetTotalSlots(NPCID) / 8.0);
		String Header = "Inventory Page %p of " + TotalPages;
		String SlotList = ChatColor.GREEN + "[" + ChatColor.WHITE + "%s"
				+ ChatColor.GREEN + "][" + ChatColor.WHITE + "%i"
				+ ChatColor.AQUA + "*" + ChatColor.WHITE + "%l"
				+ ChatColor.GREEN + "][" + ChatColor.WHITE + "B"
				+ ChatColor.AQUA + ":" + ChatColor.WHITE + "%b"
				+ ChatColor.GREEN + "][" + ChatColor.WHITE + "S"
				+ ChatColor.AQUA + ":" + ChatColor.WHITE + "%a"
				+ ChatColor.GREEN + "][" + ChatColor.WHITE + "Q"
				+ ChatColor.AQUA + ":" + ChatColor.WHITE + "%c"
				+ ChatColor.AQUA + "/" + ChatColor.WHITE + "%q"
				+ ChatColor.GREEN + "]";
		try {
			ResultSet RS = NPCDB.GetSlotList(NPCID, Page);
			String SendMSG = Header.replaceAll("%p", String.valueOf(Page));
			sender.sendMessage(SendMSG);
			String WoolColor = "";
			while (RS.next()) {
				int iDT = RS.getInt("ItemData");
				if (RS.getInt("ItemID") == 351) {
					iDT = Math.abs(iDT - 15);
				}
				if (RS.getInt("ItemID") == 35 || RS.getInt("ItemID") == 351) {
					if (iDT == 0)
						if (RS.getInt("ItemID") == 351) {
							WoolColor = ChatColor.getByCode(15).toString();
						} else {
							WoolColor = ChatColor.getByCode(15).toString();
						}
					if (iDT == 1)
						WoolColor = ChatColor.getByCode(11).toString();
					if (iDT == 2)
						WoolColor = ChatColor.getByCode(13).toString();
					if (iDT == 3)
						WoolColor = ChatColor.getByCode(9).toString();
					if (iDT == 4)
						WoolColor = ChatColor.getByCode(14).toString();
					if (iDT == 5)
						WoolColor = ChatColor.getByCode(10).toString();
					if (iDT == 6)
						WoolColor = ChatColor.getByCode(12).toString();
					if (iDT == 7)
						WoolColor = ChatColor.getByCode(8).toString();
					if (iDT == 8)
						WoolColor = ChatColor.getByCode(7).toString();
					if (iDT == 9)
						WoolColor = ChatColor.getByCode(3).toString();
					if (iDT == 10)
						WoolColor = ChatColor.getByCode(5).toString();
					if (iDT == 11)
						WoolColor = ChatColor.getByCode(1).toString();
					if (iDT == 12)
						WoolColor = ChatColor.getByCode(6).toString();
					if (iDT == 13)
						WoolColor = ChatColor.getByCode(2).toString();
					if (iDT == 14)
						WoolColor = ChatColor.getByCode(4).toString();
					if (iDT == 15)
						if (RS.getInt("ItemID") == 351) {
							WoolColor = ChatColor.getByCode(0).toString();
						} else {
							WoolColor = ChatColor.getByCode(0).toString();
						}
				}
				String sTemp = SlotList;
				sTemp = sTemp.replaceAll("%s", RS.getString("SlotID"));
				sTemp = sTemp.replaceAll(
						"%i",
						WoolColor
								+ plugin.GetMaterialName(RS.getInt("ItemID"),
										RS.getByte("ItemData")).replaceAll(
										"AIR", "-----"));
				WoolColor = "";
				sTemp = sTemp.replaceAll("%l", RS.getString("LotQty"));
				sTemp = sTemp.replaceAll("%q", RS.getString("UnitQty"));
				sTemp = sTemp.replaceAll("%c",
						String.valueOf(NPCDB.GetMaxStock(NPCID, plugin)));
				if (RS.getBoolean("IsBuy")) {
					sTemp = sTemp
							.replaceAll("%b", RS.getString("ItemBuyPrice"));
				} else {
					sTemp = sTemp.replaceAll("%b", "--");
				}
				if (RS.getBoolean("IsSell")) {
					sTemp = sTemp.replaceAll("%a",
							RS.getString("ItemSellPrice"));
				} else {
					sTemp = sTemp.replaceAll("%a", "--");
				}
				sender.sendMessage(sTemp);
			}
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}
}
