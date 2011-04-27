package com.MatCat.NPCTrader.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.MatCat.NPCTrader.NPCTrader;

public class npcMessageCommand extends npcManagerCommand {

	public npcMessageCommand(NPCTrader plugin, CommandSender sender, Command c,
			String commandLabel, String[] args, String PermissionNode,
			Boolean bNPCInteract) {
		super(plugin, sender, c, commandLabel, args, PermissionNode,
				bNPCInteract);
	}

	public Boolean Run() {
		if (!super.Run("CanText")) {
			return true;
		}
		if (args.length < 3) {
			sender.sendMessage("Usage: /npc message <Message> <Text>");
			sender.sendMessage("");
			sender.sendMessage("[<Message>] Greeting, Goodbye, Sell, Buy, InsQtyBuy, InsQtySell, InsFundsBuy, InsFundsSell, NotStocking");
			sender.sendMessage("[<Text>] Text of the message");
			sender.sendMessage("");
			sender.sendMessage("You can use the following % codes:");
			sender.sendMessage("%i - item name, %n - Item Number, %q - Qty, %a - Amount, %c Formated Amount");

			return true;
		}
		String FullText = "";
		for (int Count = 2; Count <= (args.length - 1); Count++) {
			FullText = FullText + args[Count] + " ";
		}
		if (args[1].equalsIgnoreCase("greeting")) {
			if (NPCDB.NPCSetText(NPCID, "GreetingMSG", FullText)) {

				sender.sendMessage("Message Set");
				return true;
			} else {

				sender.sendMessage("Database error setting message.");
				return true;
			}
		} else if (args[1].equalsIgnoreCase("goodbye")) {
			if (NPCDB.NPCSetText(NPCID, "GoodbyeMSg", FullText)) {
				sender.sendMessage("Message Set");
				return true;
			} else {
				sender.sendMessage("Database error setting message.");
				return true;
			}

		} else if (args[1].equalsIgnoreCase("sell")) {
			if (NPCDB.NPCSetText(NPCID, "SellMSG", FullText)) {
				sender.sendMessage("Message Set");
				return true;
			} else {
				sender.sendMessage("Database error setting message.");
				return true;
			}

		} else if (args[1].equalsIgnoreCase("buy")) {
			if (NPCDB.NPCSetText(NPCID, "buyMSG", FullText)) {
				sender.sendMessage("Message Set");
				return true;
			} else {
				sender.sendMessage("Database error setting message.");
				return true;
			}
		} else if (args[1].equalsIgnoreCase("insqtybuy")) {
			if (NPCDB.NPCSetText(NPCID, "insqtybuyMSG", FullText)) {
				sender.sendMessage("Message Set");
				return true;
			} else {
				sender.sendMessage("Database error setting message.");
				return true;
			}
		} else if (args[1].equalsIgnoreCase("insqtysell")) {
			if (NPCDB.NPCSetText(NPCID, "insqtysellMSG", FullText)) {
				sender.sendMessage("Message Set");
				return true;
			} else {
				sender.sendMessage("Database error setting message.");
				return true;
			}
		} else if (args[1].equalsIgnoreCase("insfundsbuy")) {
			if (NPCDB.NPCSetText(NPCID, "insfundsbuyMSG", FullText)) {
				sender.sendMessage("Message Set");
				return true;
			} else {
				sender.sendMessage("Database error setting message.");
				return true;
			}
		} else if (args[1].equalsIgnoreCase("insfundssell")) {
			if (NPCDB.NPCSetText(NPCID, "insfundssellMSG", FullText)) {
				sender.sendMessage("Message Set");
				return true;
			} else {
				sender.sendMessage("Database error setting message.");
				return true;
			}
		} else if (args[1].equalsIgnoreCase("notstocking")) {
			if (NPCDB.NPCSetText(NPCID, "notstockingMSG", FullText)) {
				sender.sendMessage("Message Set");
				return true;
			} else {
				sender.sendMessage("Database error setting message.");
				return true;
			}
		} else {

			sender.sendMessage("No such message type: " + args[1]);
			return true;
		}

	}
}
