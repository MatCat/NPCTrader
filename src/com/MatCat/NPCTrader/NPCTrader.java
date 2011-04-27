package com.MatCat.NPCTrader;

import java.io.File;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;
import java.math.*;

import com.MatCat.NPCTrader.ItemDB;
import com.MatCat.NPCTrader.Commands.npcAddManagerCommand;
import com.MatCat.NPCTrader.Commands.npcAddOwnerCommand;
import com.MatCat.NPCTrader.Commands.npcAdjustCommand;
import com.MatCat.NPCTrader.Commands.npcAdminListCommand;
import com.MatCat.NPCTrader.Commands.npcBankerCommand;
import com.MatCat.NPCTrader.Commands.npcBuyCommand;
//import com.MatCat.NPCTrader.Commands.npcCRCommand;
import com.MatCat.NPCTrader.Commands.npcCreateCommand;
import com.MatCat.NPCTrader.Commands.npcListCommand;
import com.MatCat.NPCTrader.Commands.npcListManagersCommand;
import com.MatCat.NPCTrader.Commands.npcListOwnersCommand;
import com.MatCat.NPCTrader.Commands.npcMessageCommand;
import com.MatCat.NPCTrader.Commands.npcMoveCommand;
import com.MatCat.NPCTrader.Commands.npcRemoveCommand;
import com.MatCat.NPCTrader.Commands.npcRemoveManagerCommand;
import com.MatCat.NPCTrader.Commands.npcRemoveOwnerCommand;
import com.MatCat.NPCTrader.Commands.npcRenameCommand;
import com.MatCat.NPCTrader.Commands.npcSellCommand;
import com.MatCat.NPCTrader.Commands.npcSetManagerCommand;
import com.MatCat.NPCTrader.Commands.npcSetupCommand;
import com.MatCat.NPCTrader.Commands.npcStockCommand;
import com.MatCat.NPCTrader.Commands.npcUnStockCommand;
import com.MatCat.NPCTrader.Commands.npcUpgradeCommand;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.martin.bukkit.npclib.NPCEntity;
import org.martin.bukkit.npclib.NPCManager;
import org.martin.bukkit.npclib.NpcEntityTargetEvent;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;

/*import redecouverte.npcspawner.BasicHumanNpc;
import redecouverte.npcspawner.BasicHumanNpcList;
import redecouverte.npcspawner.NpcSpawner;
*/
import com.nijiko.coelho.iConomy.iConomy;
import com.nijiko.coelho.iConomy.system.Account;
import com.nijiko.permissions.PermissionHandler;
import com.nijiko.permissions.*;

public class NPCTrader extends JavaPlugin {
	private static PluginListener PluginListener = null;
	private static iConomy iConomy = null;
	private static Server Server = null;
	public static PermissionHandler Permissions = null;

	private final NPCTraderPlayerListener playerListener = new NPCTraderPlayerListener(
			this);
	private NPCChunkListener chunkListener = new NPCChunkListener(this);
	public NPCTraderEntityListener NEL;
	public NpcEntityTargetEvent NEL2;
	public NPCManager npcm;
	public static iConomy iC = null;
	public HashMap NPCInteract = new HashMap();
	public int VisibleNPC;
	private static final Yaml yaml = new Yaml(new SafeConstructor());
	private static Settings settings;
	private static File folder;
	public NPCTraderMySQL NPCDB = new NPCTraderMySQL();
	public Boolean UsePermissions = false;

	public NPCTrader() throws Exception {
		/*
		 * PluginLoader pluginLoader, Server instance, PluginDescriptionFile
		 * desc, File folder, File plugin, ClassLoader cLoader
		 * initialize(pluginLoader, instance, desc, folder, plugin, cLoader);
		 */
	}

	public static void ensureEnabled(Server server) {
		PluginManager pm = server.getPluginManager();
		NPCTrader ess = (NPCTrader) pm.getPlugin("NPCTrader");
		if (!ess.isEnabled())
			pm.enablePlugin(ess);
	}

	public double distance(double X1, double Y1, double Z1, double X2,
			double Y2, double Z2) {
		double dx = X1 - X2; // horizontal difference
		double dy = Y1 - Y2; // vertical difference
		double dz = Z1 - Z2; // vertical difference
		double dist = Math.sqrt((dx * dx) + (dy * dy) + (dz * dz)); // distance
																	// using
																	// Pythagoras
																	// theorem
		return dist;
	}

	@SuppressWarnings("deprecation")
	public boolean CanCreate(String player) {
		if (this.UsePermissions) {
			int mCount = settings.MaxNPC(Permissions.getGroup(player), player);
			if (mCount == -1) {
				mCount = 99999999;
			}
			if (NPCDB.GetTotalNPC(player) >= mCount) {
				System.out
						.println("Using Permissions, Failed: "
								+ NPCDB.GetTotalNPC(player)
								+ ">="
								+ settings.MaxNPC(Permissions.getGroup(player),
										player));
				return false;
			} else {
				return true;
			}
		} else {
			return true;
		}
	}

	public static Settings getSettings() {
		return settings;
	}

	@Override
	public void onDisable() {
		try {
			getServer().getScheduler().cancelAllTasks();
			/*for (String NPCID : npcm.NPCMap().keySet()) {
				npcm.despawnById(NPCID);
				NPCDB.dbClose();
			}*/
		npcm = null;
		NPCDB.dbClose();
		} catch (Exception e) {

		}
		PluginDescriptionFile pdfFile = this.getDescription();
		System.out.println(pdfFile.getName() + " version "
				+ pdfFile.getVersion() + " is disabled!");

	}

	public boolean ci() {
		this.useiConomy = (iConomy != null);
		return this.useiConomy;
	}

	@Override
	public void onEnable() {

		PluginManager pm = getServer().getPluginManager();

		PluginDescriptionFile pdfFile = this.getDescription();
		Server = getServer();

		PluginListener = new PluginListener();

		try {
			getConfiguration().load();
			File file = new File(this.getDataFolder(), "config.yml");
			boolean create = (!file.exists() || getConfiguration() == null);

			if (create) {
				Resources.writeResource(file, "/config.yml");
				getConfiguration().load();
				System.out
						.println(pdfFile.getName()
								+ " version "
								+ pdfFile.getVersion()
								+ ": Writing config.yml...\nYou must now edit it for your MySQL settings.");
				pm.disablePlugin(this);
				return;
			}

			settings = new Settings(getConfiguration());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		try {
			if (!NPCDB.dbConnect()) {
				pm.disablePlugin(this);
				NPCDB.dbClose();
				return;
			}

			if (!NPCDB.CheckDB()) {
				System.out.println("Database error trying to check database.");
				pm.disablePlugin(this);
				NPCDB.dbClose();
				return;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}

		// Event Registration
		getServer().getPluginManager().registerEvent(Type.PLUGIN_ENABLE,
				PluginListener, Priority.Monitor, this);
		Plugin iConomy = NPCTrader.getBukkitServer().getPluginManager()
				.getPlugin("iConomy");

		if (iConomy != null) {
			if (iConomy.isEnabled()) {
				System.out
						.println("NPCTrader Successfully linked with iConomy.");
			}
		}
		boolean ui = false;
		ui = ci();

		if (!ui) {
			System.out.println(pdfFile.getName() + " version "
					+ pdfFile.getVersion() + ": No iConomy, disabling plugin");
			pm.disablePlugin(this);
			return;
		} else {
			this.npcm = new NPCManager(this);
			folder = getDataFolder();
			try {
				ItemDB.load(getDataFolder(), "items.db");
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			com.nijikokun.bukkit.Permissions.Permissions test = (com.nijikokun.bukkit.Permissions.Permissions) this.getServer().getPluginManager()
					.getPlugin("Permissions");

			if (NPCTrader.Permissions == null) {
				if (test != null) {
					NPCTrader.Permissions = test.getHandler();
					UsePermissions = true;
					System.out.println("NPCTrader is Using Permissions");
				} else {
					UsePermissions = false;
					System.out.println("NPCTrader is not using Permissions");
				}
			}

			try {
				NPCDB.SetupNPC(this, getServer().getWorlds());
				NPCDB.dbClose();
			} catch (Exception e) {
				System.out.println("NPCTrader error setting up NPCs: "
						+ e.toString());
				pm.disablePlugin(this);
				return;
			} finally {

			}
			// pm.registerEvent(Event.Type.CHUNK_LOADED, this.chunkListener,
			// Event.Priority.Normal, this);
			NPCTrader plugin;
			plugin = this;
/*
			VisibleNPC = getServer().getScheduler().scheduleSyncRepeatingTask(
					this, new Runnable() {
						public void run() {
							double X = 0;
							double Y = 0;
							double Z = 0;
							try {
								for (String NPCID : npcm.NPCMap().keySet()) {
									NPCEntity npc = npcm.getNPC(NPCID);
									double X2 = npc.getBukkitEntity()
											.getLocation().getX();
									double Y2 = npc.getBukkitEntity()
											.getLocation().getY();
									double Z2 = npc.getBukkitEntity()
											.getLocation().getZ();
									// We need to go through every player on and
									// see if ANYONE is inside 100 block radious

									Player[] pl = getServer()
											.getOnlinePlayers();
									int Count = 0;
									for (Player player : pl) {
										X = player.getLocation().getX();
										Y = player.getLocation().getY();
										Z = player.getLocation().getZ();

										if (distance(X, Y, Z, X2, Y2, Z2) < 25) {
											++Count;
											if (npc.ReDraw) {
												// System.out.println("NPC "
												// + npc.getName()
												// +
												// " within 100 blocks... revisualizing");
												npcm.despawnById(npc.ID);
												// NPCTraderMySQL NPCDB = new
												// NPCTraderMySQL();
												NPCDB.dbConnect();
												NPCDB.SetupNPC(
														Integer.parseInt(NPCID),
														getSettings().NPC(
																"Prefix"),
														getSettings().NPC(
																"Suffix"),
														npcm,
														getServer().getWorlds());
												NPCDB.dbClose();
												npc.ReDraw = false;
											}
										}
									}
									if (Count == 0) {
										npc.ReDraw=true;
									}
								}
							} catch (Exception e) {
								//e.printStackTrace();
							}

						}
					}, 20L, 20L);
			// pm.registerEvent(Event.Type.PLAYER_MOVE, this.playerListener,
			// Event.Priority.Monitor, this);
			 * 
			 */
			pm.registerEvent(Type.CHUNK_LOAD, this.chunkListener, Event.Priority.Monitor, this);
			//pm.registerEvent(Type.CHUNK_UNLOAD, this.playerListener, Event.Priority.Monitor, this);

			NEL = new NPCTraderEntityListener(this);
			
			pm.registerEvent(Type.ENTITY_TARGET, NEL, Priority.Normal, this);
			pm.registerEvent(Type.ENTITY_DAMAGE, NEL, Priority.Normal, this);

			System.out.println(pdfFile.getName() + " version "
					+ pdfFile.getVersion() + " is enabled!");
		}
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String commandLabel, String[] args) {
		String subCommand = "";
		try {
			subCommand = args[0].toLowerCase();
		} catch (Exception e1) {
		}
		if (subCommand.equals("create")) {
			npcCreateCommand ncCreate = new npcCreateCommand(this, sender,
					command, commandLabel, args, "npc.create", false);
			return ncCreate.Run();
			/*
			 * } else if (subCommand.equals("cr")) { npcCRCommand ncRM = new
			 * npcCRCommand(this, sender, command, commandLabel, args,
			 * "npc.testing", false); return ncRM.Run();
			 */} else if (subCommand.equals("removemanager")) {
			npcRemoveManagerCommand ncRM = new npcRemoveManagerCommand(this,
					sender, command, commandLabel, args,
					"npc.manager.removemanager", true);
			return ncRM.Run();
		} else if (subCommand.equals("removeowner")) {
			npcRemoveOwnerCommand ncRO = new npcRemoveOwnerCommand(this,
					sender, command, commandLabel, args,
					"npc.owner.removeowner", true);
			return ncRO.Run();
		} else if (subCommand.equals("upgrade")) {
			npcUpgradeCommand nC = new npcUpgradeCommand(this, sender, command,
					commandLabel, args, "npc.owner.upgrade", true);
			return nC.Run();
		} else if (subCommand.equals("addowner")) {
			npcAddOwnerCommand nC = new npcAddOwnerCommand(this, sender,
					command, commandLabel, args, "npc.owner.addowner", true);
			return nC.Run();
		} else if (subCommand.equals("remove")) {
			npcRemoveCommand nC = new npcRemoveCommand(this, sender, command,
					commandLabel, args, "npc.owner.remove", true);
			return nC.Run();

		} else if (subCommand.equals("banker")) {
			npcBankerCommand nC = new npcBankerCommand(this, sender, command,
					commandLabel, args, "npc.owner.banker", true);
			return nC.Run();

		} else if (subCommand.equals("setup")) {
			npcSetupCommand nC = new npcSetupCommand(this, sender, command,
					commandLabel, args, "npc.manager.setup", true);
			return nC.Run();

		} else if (subCommand.equals("addmanager")) {
			npcAddManagerCommand nC = new npcAddManagerCommand(this, sender,
					command, commandLabel, args, "npc.owner.addmanager", true);
			return nC.Run();
		} else if (subCommand.equals("move")) {
			npcMoveCommand nC = new npcMoveCommand(this, sender, command,
					commandLabel, args, "npc.manager.move", true);
			return nC.Run();
		} else if (subCommand.equals("rename")) {
			npcRenameCommand nC = new npcRenameCommand(this, sender, command,
					commandLabel, args, "npc.owner.rename", true);
			return nC.Run();
		} else if (subCommand.equals("stock")) {
			npcStockCommand nC = new npcStockCommand(this, sender, command,
					commandLabel, args, "npc.manager.stock", true);
			return nC.Run();
		} else if (subCommand.equals("buy")) {
			npcBuyCommand nC = new npcBuyCommand(this, sender, command,
					commandLabel, args, "npc.user.buy", true);
			return nC.Run();

		} else if (subCommand.equals("sell")) {
			npcSellCommand nC = new npcSellCommand(this, sender, command,
					commandLabel, args, "npc.user.sell", true);
			return nC.Run();

		} else if (subCommand.equals("unstock")) {
			npcUnStockCommand nC = new npcUnStockCommand(this, sender, command,
					commandLabel, args, "npc.manager.unstock", true);
			return nC.Run();

		} else if (subCommand.equals("setmanager")) {
			npcSetManagerCommand nC = new npcSetManagerCommand(this, sender,
					command, commandLabel, args, "npc.owner.setmanager", true);
			return nC.Run();
		} else if (subCommand.equals("adjust")) {
			npcAdjustCommand nC = new npcAdjustCommand(this, sender, command,
					commandLabel, args, "npc.manager.adjust", true);
			return nC.Run();
		} else if (subCommand.equals("list")) {
			String SubSubCommand;

			try {
				try {
					SubSubCommand = args[1].toLowerCase();
				} catch (Exception e) {
					SubSubCommand = "";
				}
				if (SubSubCommand.equalsIgnoreCase("owners")) {
					npcListOwnersCommand nC = new npcListOwnersCommand(this,
							sender, command, commandLabel, args,
							"npc.owner.listowners", true);
					return nC.Run();
				}

			} catch (Exception e) {
				return true;
			}
			try {
				if (SubSubCommand.equalsIgnoreCase("managers")) {
					npcListManagersCommand nC = new npcListManagersCommand(
							this, sender, command, commandLabel, args,
							"npc.manager.listmanagers", true);
					return nC.Run();
				}
			} catch (Exception e) {
				return true;
			}
			try {
				if (SubSubCommand.equalsIgnoreCase("npcs")) {
					npcAdminListCommand nC = new npcAdminListCommand(this,
							sender, command, commandLabel, args,
							"npc.admin.list", false);
					return nC.Run();
				}
			} catch (Exception e) {
				return true;
			}

			npcListCommand nC = new npcListCommand(this, sender, commandLabel,
					args, "npc.user.list", true);
			return nC.Run();

		} else if (subCommand.equals("message")) {
			npcMessageCommand nC = new npcMessageCommand(this, sender, command,
					commandLabel, args, "npc.manager.message", true);
			return nC.Run();

		} else {
			sender.sendMessage("To interact with an NPC Right click on it");
			sender.sendMessage("Use /npc <command> as listed below");
			sender.sendMessage("for more specific help");
			sender.sendMessage("");
			sender.sendMessage("list, buy, sell, create, ");
			sender.sendMessage("remove, stock, unstock,");
			sender.sendMessage("addmanager, addowner,");
			sender.sendMessage("banker, setup, adjust, ");
			sender.sendMessage("message, move, setmanager, upgrade");
			return true;
		}

	}

	public void removeItems(Player player, int ItemID, int ItemData, int amount) {
		PlayerInventory inventory = player.getInventory();
		ItemStack[] items = inventory.getContents();
		ItemStack thisItem;
		int toRemove = amount;
		Byte iD2 = 0;
		try {
			iD2 = (byte) ItemData;
		} catch (Exception e) {
			iD2 = 0;
		}

		for (int invSlot = 35; (invSlot >= 0) && (toRemove > 0); --invSlot) {
			thisItem = items[invSlot];
			byte iD = 0;
			try {

				try {
					iD = (byte) thisItem.getDurability();
				} catch (Exception e) {
					iD = 0;
				}

			} catch (Exception e) {
				System.out.println("Offending Itemdata is: "
						+ thisItem.getData().getData());
			}
			if ((items[invSlot] != null) && (thisItem.getTypeId() == ItemID)
					&& (iD == iD2)) {
				toRemove -= thisItem.getAmount();
				inventory.clear(invSlot);
			}
		}

		if (toRemove < 0) // removed too many! put some back!
			inventory.addItem(new ItemStack[] { new ItemStack(ItemID,
					-toRemove, (byte) ItemData) });
	}

	public int PlayerItemCount(Player player, int ItemID, int ItemData) {
		PlayerInventory inventory = player.getInventory();
		ItemStack[] items = inventory.getContents();
		int amount = 0;
		Byte iD2 = 0;
		try {
			iD2 = (byte) ItemData;
			// System.out.println("STOCK says iD2 of " + iD2);
		} catch (Exception e) {
			iD2 = 0;
			// System.out.println("STOCK says iD2 " + e.toString() +
			// ", setting "
			// + iD2);
		}
		for (ItemStack item : items) {
			byte iD = 0;
			try {

				try {
					iD = (byte) item.getDurability();
					// System.out.println("INV says iD of " + iD);
				} catch (Exception e) {
					iD = 0;
					// System.out.println("INV says iD of " + e.toString() +
					// " setting to " + iD);
				}
				if ((item != null) && (item.getTypeId() == ItemID)
						&& (iD == iD2)) {
					amount += item.getAmount();
				}
			} catch (Exception e) {
				System.out.println("Offending Itemdata is: "
						+ item.getData().getData());
			}

		}
		return amount;
	}

	public String expandName(String Name) {
		int m = 0;
		String Result = "";
		for (int n = 0; n < getServer().getOnlinePlayers().length; n++) {
			String str = getServer().getOnlinePlayers()[n].getName();
			if (str.matches("(?i).*" + Name + ".*")) {
				m++;
				Result = str;
			}
			if (str.equalsIgnoreCase(Name))
				return str;
		}
		if (m == 1)
			return Result;
		if (m > 1) {
			return null;
		}
		if (m < 1) {
			return Name;
		}
		return Name;
	}

	private boolean useiConomy = false;

	public String GetMaterialName(int ItemID, byte ItemData) {
		for (Material m : Material.values()) {
			if (m.getId() == ItemID) {
				// found your material
				if (ItemID == 351) {
					switch (Math.abs(ItemData - 15)) {
					case 15:
						return "INK SAC";
					case 1:
						return "ORANGE DYE";
					case 2:
						return "MAGENTA DYE";
					case 3:
						return "LIGHT BLUE DYE";
					case 4:
						return "DANDELION YELLOW";
					case 5:
						return "LIME DYE";
					case 6:
						return "PINK DYE";
					case 7:
						return "GRAY DYE";
					case 8:
						return "LIGHT GRAY DYE";
					case 9:
						return "CYAN DYE";
					case 10:
						return "PURPLE DYE";
					case 11:
						return "LAPIS LUZULI";
					case 12:
						return "COCO BEANS";
					case 13:
						return "CACTUS GREEN";
					case 14:
						return "ROSE RED";
					case 0:
						return "BONE MEAL";
					}
				}
				if (ItemID == 263) {
					switch (ItemData) {
					case 0:
						return "COAL";
					case 1:
						return "CHARCOAL";
					}
				}
				if (ItemID == 15) {
					switch (ItemData) {
					case 0:
						return "LOG";
					case 1:
						return "PINE LOG";
					case 2:
						return "BIRCH LOG";
					}
				}
				if (ItemID == 43) {
					switch (ItemData) {
					case 0:
						return "DOUBLE STONE SLAB";
					case 1:
						return "DOUBLE SANDSTONE SLAB";
					case 2:
						return "DOUBLE WOODEN SLAB";
					case 3:
						return "DOUBLE COBBLESTONE SLAB";
					}
				}
				if (ItemID == 44) {
					switch (ItemData) {
					case 0:
						return "STONE SLAB";
					case 1:
						return "SANDSTONE SLAB";
					case 2:
						return "WOODEN SLAB";
					case 3:
						return "COBBLESTONE SLAB";
					}
				}

				if (ItemID == 35) {
					switch (ItemData) {
					case 0:
						return "WHITE WOOL";
					case 1:
						return "ORANGE WOOL";
					case 2:
						return "MAGENTA WOOL";
					case 3:
						return "LIGHT BLUE WOOL";
					case 4:
						return "YELLOW WOOL";
					case 5:
						return "LIME WOOL";
					case 6:
						return "PINK WOOL";
					case 7:
						return "GRAY WOOL";
					case 8:
						return "LIGHT GRAY WOOL";
					case 9:
						return "CYAN WOOL";
					case 10:
						return "PURPLE WOOL";
					case 11:
						return "BLUE WOOL";
					case 12:
						return "BROWN WOOL";
					case 13:
						return "GREEN WOOL";
					case 14:
						return "RED WOOL";
					case 15:
						return "BLACK WOOL";
					}

				}
				return m.name();
			}
		}
		return "No Item";
	}

	public void AddBalance(String Player, Double Amt) {
		Account account = iConomy.getBank().getAccount(Player);
		if (account == null) {
			iConomy.getBank().addAccount(Player);
			account = iConomy.getBank().getAccount(Player);

		}
		account.add(Amt);
	}

	public void SubtractBalance(String Player, Double Amt) {
		Account account = iConomy.getBank().getAccount(Player);
		if (account == null) {
			iConomy.getBank().addAccount(Player);
			account = iConomy.getBank().getAccount(Player);

		}

		account.subtract(Amt);
	}

	public double GetPlayerBalance(String Player) {
		Account account = iConomy.getBank().getAccount(Player);
		if (account == null) {
			iConomy.getBank().addAccount(Player);
			account = iConomy.getBank().getAccount(Player);

		}

		return account.getBalance();
	}

	public static Server getBukkitServer() {
		return Server;
	}

	public static iConomy getiConomy() {
		return iConomy;
	}

	public static boolean setiConomy(iConomy plugin) {
		if (iConomy == null) {
			iConomy = plugin;
		} else {
			return false;
		}
		return true;
	}

	public boolean checkiConomy() {
		Plugin test = this.getServer().getPluginManager().getPlugin("iConomy");

		if (test != null) {
			iC = (iConomy) test;
			this.useiConomy = true;
		} else {
			this.useiConomy = false;
		}

		return useiConomy;
	}

}
