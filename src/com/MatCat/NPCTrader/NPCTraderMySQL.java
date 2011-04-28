package com.MatCat.NPCTrader;

import java.sql.*;
import java.util.Date;
import java.util.List;

import com.MatCat.NPCTrader.ItemDB;
import com.nijiko.coelho.iConomy.iConomy;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.plugin.PluginManager;
import org.martin.bukkit.npclib.NPCEntity;
import org.martin.bukkit.npclib.NPCManager;

/*import redecouverte.npcspawner.BasicHumanNpc;
 import redecouverte.npcspawner.BasicHumanNpcList;
 import redecouverte.npcspawner.NpcSpawner;
 */
public class NPCTraderMySQL {

	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	private ResultSet generatedKeys = null;
	private int NPCID = 0;
	private int OwnerID = 0;

	public Boolean CheckDB() {
		String[] TableArrSqlite;
		String[] TableArrMySQL;
		TableArrSqlite = new String[7];
		TableArrMySQL = new String[7];

		String TTable = "CREATE TABLE IF NOT EXISTS npc(";
		TTable = TTable + "  ID INTEGER PRIMARY KEY AUTOINCREMENT, ";
		TTable = TTable + "  NPCName Text,";
		TTable = TTable + "  Banker text,";
		TTable = TTable + "  ItemSlots INT,";
		TTable = TTable + "  Units INT,";
		TTable = TTable + "  PosX FLOAT DEFAULT NULL,";
		TTable = TTable + "  PosY FLOAT DEFAULT NULL,";
		TTable = TTable + "  PosZ FLOAT DEFAULT NULL,";
		TTable = TTable + "  PosYaw FLOAT DEFAULT NULL,";
		TTable = TTable + "  PosPitch FLOAT DEFAULT NULL,";
		TTable = TTable + "  WorldName text";
		TTable = TTable + ")";

		TableArrSqlite[0] = TTable;
		TTable = "";
		TTable = TTable + "CREATE TABLE IF NOT EXISTS  npcinventory(";
		TTable = TTable + "  ID INTEGER PRIMARY KEY AUTOINCREMENT,  ";
		TTable = TTable + "  NPCID INT,";
		TTable = TTable + "  ItemID INT,";
		TTable = TTable + "  UnitQty INT DEFAULT 0,";
		TTable = TTable + "  ItemBuyPrice INT,";
		TTable = TTable + "  ItemSellPrice INT,";
		TTable = TTable + "  IsBuy TINYINT,";
		TTable = TTable + "  IsSell TINYINT,";
		TTable = TTable + "  LotQty INT DEFAULT 0,";
		TTable = TTable + "  SlotID INT,";
		TTable = TTable + "  ItemData INT, slottype ";
		TTable = TTable + ")";

		TableArrSqlite[1] = TTable;
		TTable = "";
		TTable = TTable + "CREATE TABLE IF NOT EXISTS  npcmanagerpermissions(";
		TTable = TTable + "  ID INTEGER PRIMARY KEY AUTOINCREMENT, ";
		TTable = TTable + "  MID INT,";
		TTable = TTable + "  CanStock TINYINT,";
		TTable = TTable + "  CanUnstock TINYINT,";
		TTable = TTable + "  CanMove TINYINT,";
		TTable = TTable + "  CanPrice TINYINT,";
		TTable = TTable + "  CanText TINYINT";
		TTable = TTable + ")";
		TableArrSqlite[2] = TTable;
		TTable = "";

		TTable = TTable + "CREATE TABLE IF NOT EXISTS  npcmanagers(";
		TTable = TTable + "  ID INTEGER PRIMARY KEY AUTOINCREMENT, ";
		TTable = TTable + "  NPCID INT,";
		TTable = TTable + "  ManagerName text";
		TTable = TTable + ")";
		TableArrSqlite[3] = TTable;
		TTable = "";

		TTable = TTable + "CREATE TABLE  IF NOT EXISTS npcowner(";
		TTable = TTable + "  ID INTEGER PRIMARY KEY AUTOINCREMENT, ";
		TTable = TTable + "  NPCID INT,";
		TTable = TTable + "  OwnerName Text";
		TTable = TTable + ")";
		TableArrSqlite[4] = TTable;
		TTable = "";

		TTable = TTable + "CREATE TABLE IF NOT EXISTS  npctext(";
		TTable = TTable + "  ID INTEGER PRIMARY KEY AUTOINCREMENT, ";
		TTable = TTable + "  NPCID INT,";
		TTable = TTable + "  GreetingMSG Text, ";
		TTable = TTable + "  GoodbyeMSG text, ";
		TTable = TTable + "  SellMSG text, ";
		TTable = TTable + "  BuyMSG text,  ";
		TTable = TTable + "  InsQtyBuyMsg text, ";
		TTable = TTable + "  InsQtySellMsg text, ";
		TTable = TTable + "  InsFundsBuyMsg text, ";
		TTable = TTable + "  InsFundsSellMsg text, ";
		TTable = TTable + "  NotStockingMsg text";
		TTable = TTable + ")";

		TableArrSqlite[5] = TTable;
		TTable = "";
		TableArrSqlite[6] = "CREATE TABLE IF NOT EXISTS npctransactions(ID INTEGER PRIMARY KEY AUTOINCREMENT,  TTime DATETIME,  NPCID INTEGER(11),  Player VARCHAR(255),  Banker VARCHAR(255),  ItemID INTEGER(11),  ItemDataID TINYINT(1),  Quantity INTEGER(11),  ItemCost INTEGER(11),  Total INTEGER(11),  bPurchased BIT(1))";

		String NPCTable = "CREATE TABLE IF NOT EXISTS npc(";
		NPCTable = NPCTable + "  ID INT(11) NOT NULL ";
		NPCTable = NPCTable + "AUTO_INCREMENT,";
		NPCTable = NPCTable + "  NPCName VARCHAR(25) NOT NULL,";
		NPCTable = NPCTable + "  Banker VARCHAR(255) NOT NULL,";
		NPCTable = NPCTable + "  ItemSlots INT(11) DEFAULT NULL,";
		NPCTable = NPCTable + "  Units INT(11) DEFAULT NULL,";
		NPCTable = NPCTable + "  PosX FLOAT DEFAULT NULL,";
		NPCTable = NPCTable + "  PosY FLOAT DEFAULT NULL,";
		NPCTable = NPCTable + "  PosZ FLOAT DEFAULT NULL,";
		NPCTable = NPCTable + "  PosYaw FLOAT DEFAULT NULL,";
		NPCTable = NPCTable + "  PosPitch FLOAT DEFAULT NULL,";
		NPCTable = NPCTable + "  WorldName VARCHAR(255) DEFAULT ";
		NPCTable = NPCTable + " NULL,";
		NPCTable = NPCTable + "  Balance INT(11) DEFAULT 0,";
		NPCTable = NPCTable + "  PRIMARY KEY (ID)";
		NPCTable = NPCTable + ")";

		String NPCInvTable = "CREATE TABLE IF NOT EXISTS  npcinventory(";
		NPCInvTable = NPCInvTable + "  ID INT(11) NOT NULL ";
		NPCInvTable = NPCInvTable + "AUTO_INCREMENT,";
		NPCInvTable = NPCInvTable + "  NPCID INT(11) DEFAULT NULL,";
		NPCInvTable = NPCInvTable + "  ItemID INT(11) DEFAULT NULL,";
		NPCInvTable = NPCInvTable + "  UnitQty INT(11) DEFAULT 0,";
		NPCInvTable = NPCInvTable + "  ItemBuyPrice INT(11) DEFAULT NULL,";
		NPCInvTable = NPCInvTable + "  ItemSellPrice INT(11) DEFAULT NULL,";
		NPCInvTable = NPCInvTable + "  IsBuy TINYINT(1) DEFAULT NULL,";
		NPCInvTable = NPCInvTable + "  IsSell TINYINT(1) DEFAULT NULL,";
		NPCInvTable = NPCInvTable + "  LotQty INT(11) DEFAULT 0,";
		NPCInvTable = NPCInvTable + "  SlotID INT(11) DEFAULT NULL,";
		NPCInvTable = NPCInvTable + "  ItemData TINYINT(4) DEFAULT NULL,";
		NPCInvTable = NPCInvTable + "  SlotType VARCHAR(20) DEFAULT NULL, ";
		NPCInvTable = NPCInvTable + "  SlotValue VARCHAR(255) DEFAULT NULL, ";
		NPCInvTable = NPCInvTable + "  SlotFlag TINYINT(1) DEFAULT NULL, ";
		NPCInvTable = NPCInvTable + "  PRIMARY KEY (ID)";
		NPCInvTable = NPCInvTable + ")";

		String NPCMPTable = "CREATE TABLE IF NOT EXISTS  npcmanagerpermissions(";
		NPCMPTable = NPCMPTable + "  ID INT(11) NOT NULL ";
		NPCMPTable = NPCMPTable + "AUTO_INCREMENT,";
		NPCMPTable = NPCMPTable + "  MID INT(11) NOT NULL,";
		NPCMPTable = NPCMPTable + "  CanStock TINYINT(1) DEFAULT NULL,";
		NPCMPTable = NPCMPTable + "  CanUnstock TINYINT(1) DEFAULT NULL,";
		NPCMPTable = NPCMPTable + "  CanMove TINYINT(1) DEFAULT NULL,";
		NPCMPTable = NPCMPTable + "  CanPrice TINYINT(1) DEFAULT NULL,";
		NPCMPTable = NPCMPTable + "  CanText TINYINT(1) DEFAULT NULL,";
		NPCMPTable = NPCMPTable + "  PRIMARY KEY (ID, MID)";
		NPCMPTable = NPCMPTable + ")";

		String NPCMTable = "CREATE TABLE IF NOT EXISTS  npcmanagers(";
		NPCMTable = NPCMTable + "  ID INT(11) NOT NULL ";
		NPCMTable = NPCMTable + "AUTO_INCREMENT,";
		NPCMTable = NPCMTable + "  NPCID INT(11) DEFAULT NULL,";
		NPCMTable = NPCMTable + "  ManagerName VARCHAR(255) DEFAULT ";
		NPCMTable = NPCMTable + "NULL,";
		NPCMTable = NPCMTable + "  PRIMARY KEY (ID)";
		NPCMTable = NPCMTable + ")";

		String NPCOTable = "CREATE TABLE  IF NOT EXISTS npcowner(";
		NPCOTable = NPCOTable + "  ID INT(11) NOT NULL ";
		NPCOTable = NPCOTable + "AUTO_INCREMENT,";
		NPCOTable = NPCOTable + "  NPCID INT(11) DEFAULT NULL,";
		NPCOTable = NPCOTable + "  OwnerName VARCHAR(255) DEFAULT ";
		NPCOTable = NPCOTable + "NULL,";
		NPCOTable = NPCOTable + "  PRIMARY KEY (ID)";
		NPCOTable = NPCOTable + ")";

		String NPCTrTable = "CREATE TABLE IF NOT EXISTS npctransactions(ID INTEGER(11) NOT NULL AUTO_INCREMENT,  TTime DATETIME DEFAULT NULL,  NPCID INTEGER(11) DEFAULT NULL,  Player VARCHAR(255) DEFAULT NULL,  Banker VARCHAR(255) DEFAULT NULL,  ItemID INTEGER(11) DEFAULT NULL,  ItemDataID TINYINT(1) DEFAULT NULL,  Quantity INTEGER(11) DEFAULT NULL,  ItemCost INTEGER(11) DEFAULT NULL,  Total INTEGER(11) DEFAULT NULL,  bPurchased BIT(1) DEFAULT NULL,  PRIMARY KEY (ID))";
		String NPCTTable = "CREATE TABLE IF NOT EXISTS  npctext(";
		NPCTTable = NPCTTable + "  ID INT(11) NOT NULL 	";
		NPCTTable = NPCTTable + "AUTO_INCREMENT,";
		NPCTTable = NPCTTable + "  NPCID INT(11) NOT NULL,";
		NPCTTable = NPCTTable + "  GreetingMSG VARCHAR(255) DEFAULT ";
		NPCTTable = NPCTTable + "NULL,";
		NPCTTable = NPCTTable + "  GoodbyeMSG VARCHAR(255) DEFAULT ";
		NPCTTable = NPCTTable + "NULL,";
		NPCTTable = NPCTTable + "  SellMSG VARCHAR(255) DEFAULT ";
		NPCTTable = NPCTTable + "NULL,";
		NPCTTable = NPCTTable + "  BuyMSG VARCHAR(255) DEFAULT ";
		NPCTTable = NPCTTable + "NULL,";
		NPCTTable = NPCTTable + "  InsQtyBuyMsg VARCHAR(255) DEFAULT ";
		NPCTTable = NPCTTable + "NULL,";
		NPCTTable = NPCTTable + "  InsQtySellMsg VARCHAR(255) DEFAULT ";
		NPCTTable = NPCTTable + "NULL,";
		NPCTTable = NPCTTable + "  InsFundsBuyMsg VARCHAR(255) ";
		NPCTTable = NPCTTable + "DEFAULT NULL,";
		NPCTTable = NPCTTable + "  InsFundsSellMsg VARCHAR(255) ";
		NPCTTable = NPCTTable + "DEFAULT NULL,";
		NPCTTable = NPCTTable + "  NotStockingMsg VARCHAR(255) ";
		NPCTTable = NPCTTable + "DEFAULT NULL,";
		NPCTTable = NPCTTable + "  PRIMARY KEY (ID, NPCID)";
		NPCTTable = NPCTTable + ")";
		String AT1 = "ALTER TABLE npctransactions MODIFY Total decimal(10,2);";
		String AT2 = "ALTER TABLE npctransactions MODIFY ItemCost decimal(10,2);";
		String AT3 = "ALTER TABLE npcinventory MODIFY ItemBuyPrice decimal(10,2);";
		String AT4 = "ALTER TABLE npcinventory MODIFY ItemSellPrice decimal(10,2);";
		if (true) {
			/*
			 * NPCTable = NPCTable.replaceAll("AUTO_INCREMENT",
			 * "AUTOINCREMENT"); NPCInvTable =
			 * NPCInvTable.replaceAll("AUTO_INCREMENT", "AUTOINCREMENT");
			 * NPCMPTable = NPCMPTable.replaceAll("AUTO_INCREMENT",
			 * "AUTOINCREMENT"); NPCMTable =
			 * NPCMTable.replaceAll("AUTO_INCREMENT", "AUTOINCREMENT");
			 * NPCOTable = NPCOTable.replaceAll("AUTO_INCREMENT",
			 * "AUTOINCREMENT"); NPCTTable =
			 * NPCTTable.replaceAll("AUTO_INCREMENT", "AUTOINCREMENT");
			 */
		}

		try {
			if (NPCTrader.getSettings().DB("Driver").equalsIgnoreCase("sqlite")) {

				/* dbConnect(); */

				for (int Count = 0; Count <= 6; ++Count) {
					preparedStatement = connect
							.prepareStatement(TableArrSqlite[Count]);
					preparedStatement.execute();

				}
			} else {

				preparedStatement = connect.prepareStatement(NPCTable);
				preparedStatement.execute();
				preparedStatement = connect.prepareStatement(NPCInvTable);
				preparedStatement.execute();
				preparedStatement = connect.prepareStatement(NPCMPTable);
				preparedStatement.execute();
				preparedStatement = connect.prepareStatement(NPCMTable);
				preparedStatement.execute();
				preparedStatement = connect.prepareStatement(NPCOTable);
				preparedStatement.execute();
				preparedStatement = connect.prepareStatement(NPCTTable);
				preparedStatement.execute();
				preparedStatement = connect.prepareStatement(NPCTrTable);
				preparedStatement.execute();

				try {
					preparedStatement = connect
							.prepareStatement("alter table npcinventory add SlotType VARCHAR(20), add SlotValue VARCHAR(255), add SlotFlag TINYINT(1)");
					preparedStatement.execute();
				} catch (Exception e) {
					// e.printStackTrace();
				}
				preparedStatement = connect.prepareStatement(AT1);
				preparedStatement.execute();
				preparedStatement = connect.prepareStatement(AT2);
				preparedStatement.execute();
				preparedStatement = connect.prepareStatement(AT3);
				preparedStatement.execute();
				preparedStatement = connect.prepareStatement(AT4);
				preparedStatement.execute();

			}
			return true;
		} catch (Exception ex) {
			return false;
		}

	}

	public boolean AddTransaction(int NPCID, String p, String b, int i,
			byte id, int q, double ic, double total, Boolean bp) {
		String sSQL = "Insert into npctransactions (ttime, NPCID, Player, Banker, ItemID, ItemDataID, Quantity, ItemCost, Total, bPurchased) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try {
			preparedStatement = connect.prepareStatement(sSQL);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			return false;
		}
		try {
			java.util.Date utilDate = new java.util.Date();
			java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
			preparedStatement.setDate(1, sqlDate);
			preparedStatement.setInt(2, NPCID);
			preparedStatement.setString(3, p);
			preparedStatement.setString(4, b);
			preparedStatement.setInt(5, i);
			preparedStatement.setByte(6, id);
			preparedStatement.setInt(7, q);
			preparedStatement.setDouble(8, ic);
			preparedStatement.setDouble(9, total);
			preparedStatement.setBoolean(10, bp);
			int ar = preparedStatement.executeUpdate();
			if (ar == 0) {
				return false;
			}

		} catch (Exception e) {

			return false;
		}

		return true;
	}

	public Boolean CheckOwner(int NPCID, String Owner) {
		try {
			/* dbConnect(); */
			preparedStatement = connect
					.prepareStatement("Select * from npcowner where NPCID = ? and OwnerName = ?");
			preparedStatement.setInt(1, NPCID);
			preparedStatement.setString(2, Owner);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				// System.out.println(Owner + " passes for " + NPCID);
				return true;
			}
		} catch (Exception e) {
			// System.out.println(Owner + " errors for " + NPCID);
			e.printStackTrace();
		} finally {
			close();
		}
		// System.out.println(Owner + " fails for " + NPCID);
		return false;
	}

	public Boolean CheckManager(int NPCID, String Manager) {
		try {
			/* dbConnect(); */
			if (this.CheckOwner(NPCID, Manager))
				return true;

			preparedStatement = connect
					.prepareStatement("Select * from npcmanagers where NPCID = ? and ManagerName = ?");
			preparedStatement.setInt(1, NPCID);
			preparedStatement.setString(2, Manager);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return false;
	}

	public int CheckUnitCount(int NPCID, int SlotID) {
		try {
			/* dbConnect(); */
			preparedStatement = connect
					.prepareStatement("Select * from npcinventory where NPCID = ? and SlotID = ?");
			preparedStatement.setInt(1, NPCID);
			preparedStatement.setInt(2, SlotID);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				return resultSet.getInt("UnitQty");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return -1;
	}

	public Boolean AddSlot(int NPCID, int SlotID) {
		try {
			/* dbConnect(); */
			preparedStatement = connect
					.prepareStatement("insert into npcinventory (NPCID, SlotID) VALUES (?, ?)");
			preparedStatement.setInt(1, NPCID);
			preparedStatement.setInt(2, SlotID);

			int affectedRows = preparedStatement.executeUpdate();

			if (affectedRows == 0) {

				return false;
			} else {
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public String GetSlotItem(int NPCID, int SlotID) {
		try {
			/* dbConnect(); */
			preparedStatement = connect
					.prepareStatement("Select * from npcinventory WHERE NPCID = ? AND SlotID = ?");
			preparedStatement.setInt(1, NPCID);
			preparedStatement.setInt(2, SlotID);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				int ItemData = 0;
				ItemData = resultSet.getInt("ItemData");
				return resultSet.getString("ItemID") + ":" + ItemData;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public Boolean SlotAdjust(int NPCID, int SlotID, String Field, String Value) {
		if (Field.equalsIgnoreCase("item")) {
			Field = "ItemID";
		}
		if (Field.equalsIgnoreCase("buyprice")) {
			Field = "ItemBuyPrice";
		}
		if (Field.equalsIgnoreCase("sellprice")) {
			Field = "ItemSellPrice";
		}
		if (Field.equalsIgnoreCase("buy")) {
			Field = "isbuy";
		}
		if (Field.equalsIgnoreCase("sell")) {
			Field = "issell";
		}
		String OSQL = "Update npcinventory SET " + Field
				+ " = ? WHERE NPCID = ? and SlotID = ?";
		if (Field.equalsIgnoreCase("itemid")) {
			// Handle Item + Item Data
			String[] itemArgs = Value.split("[^a-zA-Z0-9]");
			try {
				int itemId = ItemDB.get(itemArgs[0]);
				Value = String.valueOf(itemId);
			} catch (Exception e) {
				return false;

			}
			byte itemData = itemArgs.length > 1 ? Byte.parseByte(itemArgs[1])
					: (byte) 0;

			OSQL = "Update npcinventory SET ItemID = ?, ItemData = " + itemData
					+ " WHERE NPCID = ? AND SlotID = ?";
		}

		try {
			// Handle Other Parem
			/* dbConnect(); */
			preparedStatement = connect.prepareStatement(OSQL);

			if (Field.equalsIgnoreCase("itemid")) {
				preparedStatement.setInt(1, Integer.parseInt(Value));
			} else if (Field.equalsIgnoreCase("itembuyprice")) {
				preparedStatement.setInt(1, Integer.parseInt(Value));
			} else if (Field.equalsIgnoreCase("itemsellprice")) {
				preparedStatement.setInt(1, Integer.parseInt(Value));
			} else if (Field.equalsIgnoreCase("isbuy")) {
				preparedStatement.setBoolean(1, Boolean.parseBoolean(Value));
			} else if (Field.equalsIgnoreCase("issell")) {
				preparedStatement.setBoolean(1, Boolean.parseBoolean(Value));
			} else if (Field.equalsIgnoreCase("lotqty")) {
				preparedStatement.setInt(1, Integer.parseInt(Value));
			}

			preparedStatement.setInt(2, NPCID);
			preparedStatement.setInt(3, SlotID);
			int affectedRows = preparedStatement.executeUpdate();

			if (affectedRows == 0) {
				// TODO: Problem Handling
				// System.out.println(OSQL);
				return false;
			} else {
				return true;
			}
		} catch (Exception e) {
			return false;
		}

	}

	public String ReturnScalar(String sql) {
		try {
			preparedStatement = connect.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				return resultSet.getString(1);
			}
		} catch (Exception e) {
			return "";
		}
		return "";
	}

	public String NPCMessage(int NPCID, String s, int Qty, int Item,
			byte ItemData, double total, NPCTrader parent) {
		/* dbConnect(); */
		String sSQL = "";
		try {
			sSQL = "Select " + s + " from npctext WHERE NPCID = " + NPCID;
		} catch (Exception e) {
			sSQL = "Select GreetingMSG from npctext where NPCID = " + NPCID;
		}
		try {
			preparedStatement = connect.prepareStatement(sSQL);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			resultSet = preparedStatement.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			while (resultSet.next()) {
				String sTemp = "";
				try {
					sTemp = resultSet.getString(s);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					sTemp = sTemp.replaceAll("%i",
							parent.GetMaterialName(Item, ItemData));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					sTemp = sTemp.replaceAll("%n", Integer.toString(Item));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					sTemp = sTemp.replaceAll("%q", Integer.toString(Qty));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					sTemp = sTemp.replaceAll("%a", Double.toString(total));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					sTemp = sTemp.replaceAll("%c",
							iConomy.getBank().format(total));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return sTemp;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";

	}

	public Boolean SlotSetup(int NPCID, int SlotID, int ItemID, byte itemData,
			int LotQty, int BuyPrice, int SellPrice, Boolean Buy, Boolean Sell) {
		try {
			/* dbConnect(); */
			preparedStatement = connect
					.prepareStatement("update npcinventory set "
							+ " ItemID = ?" + ",ItemData = ?" + ",LotQty = ?"
							+ ",ItemBuyPrice = ?" + ",ItemSellPrice = ?"
							+ ",IsBuy = ?" + ",IsSell = ?"
							+ " where NPCID = ? and slotid = ?");
			preparedStatement.setInt(1, ItemID);
			preparedStatement.setByte(2, itemData);
			preparedStatement.setInt(3, LotQty);
			preparedStatement.setInt(4, BuyPrice);
			preparedStatement.setInt(5, SellPrice);
			preparedStatement.setBoolean(6, Buy);
			preparedStatement.setBoolean(7, Sell);
			preparedStatement.setInt(8, NPCID);
			preparedStatement.setInt(9, SlotID);

			int affectedRows = preparedStatement.executeUpdate();

			if (affectedRows == 0) {
				// TODO: Problem Handling
				return false;
			} else {
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	public Boolean CheckManagerPermission(int NPCID, String Manager,
			String Permission) {
		try {
			if (this.CheckOwner(NPCID, Manager)) {
				return true;
			}

			/* dbConnect(); */

			preparedStatement = connect
					.prepareStatement("Select * from npcmanagers where NPCID = ? and ManagerName = ?");
			preparedStatement.setInt(1, NPCID);
			preparedStatement.setString(2, Manager);
			resultSet = preparedStatement.executeQuery();
			int MID = 0;
			while (resultSet.next()) {
				MID = resultSet.getInt("ID");
			}
			if (MID != 0) {
				preparedStatement = connect
						.prepareStatement("Select * from npcmanagerpermissions where MID = ? and ? = ?");
				preparedStatement.setInt(1, MID);
				preparedStatement.setString(2, Permission);
				preparedStatement.setBoolean(3, true);
				resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					// If this even executes it came back true
					return true;
				}
				return false;
			} else {
				// TODO: Problem Handling
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return false;
	}

	public int NPCAddManager(int NPCID, String Manager) {
		try {
			/* dbConnect(); */
			preparedStatement = connect
					.prepareStatement(
							"insert into npcmanagers (NPCID, ManagerName) VALUES (?, ?)",
							Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setInt(1, NPCID);
			preparedStatement.setString(2, Manager);

			int affectedRows = preparedStatement.executeUpdate();

			if (affectedRows == 0) {
				// TODO: Problem Handling
				return 0;
			}

			generatedKeys = preparedStatement.getGeneratedKeys();
			if (generatedKeys.next()) {
				OwnerID = generatedKeys.getInt(1);
			} else {
				// TODO: Problem Handling
				return 0;
			}

			preparedStatement = connect
					.prepareStatement(
							"insert into npcmanagerpermissions (MID, CanStock, "
									+ "CanUnstock, CanMove, CanPrice, CanText) VALUES (?, ?, ?, ?, ?, ?)",
							Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setInt(1, OwnerID);
			preparedStatement.setBoolean(2, true);
			preparedStatement.setBoolean(3, true);
			preparedStatement.setBoolean(4, false);
			preparedStatement.setBoolean(5, true);
			preparedStatement.setBoolean(6, false);
			affectedRows = preparedStatement.executeUpdate();

			if (affectedRows == 0) {
				// TODO: Problem Handling
				return 0;
			}

			generatedKeys = preparedStatement.getGeneratedKeys();
			if (generatedKeys.next()) {
				// BLAH
			} else {
				// TODO: Problem Handling
				return 0;
			}

			return OwnerID;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return 0;
	}

	public Boolean NPCClearNPC(int NPCID) {
		try {
			/* dbConnect(); */
			preparedStatement = connect
					.prepareStatement("delete from npcowner Where NPCID = ?");
			preparedStatement.setInt(1, NPCID);
			int affectedRows = preparedStatement.executeUpdate();
			preparedStatement = connect
					.prepareStatement("delete from npctext Where NPCID = ?");
			preparedStatement.setInt(1, NPCID);
			affectedRows = preparedStatement.executeUpdate();
			preparedStatement = connect
					.prepareStatement("delete from npcinventory Where NPCID = ?");
			preparedStatement.setInt(1, NPCID);
			affectedRows = preparedStatement.executeUpdate();
			preparedStatement = connect
					.prepareStatement("delete from npc Where ID = ?");
			preparedStatement.setInt(1, NPCID);
			affectedRows = preparedStatement.executeUpdate();
			preparedStatement = connect
					.prepareStatement("delete from npcmanagerpermissions where mid IN (select id from npcmanagers where npcid = ?)");
			preparedStatement.setInt(1, NPCID);
			affectedRows = preparedStatement.executeUpdate();
			preparedStatement = connect
					.prepareStatement("delete from npcmanagers Where NPCID = ?");
			preparedStatement.setInt(1, NPCID);
			affectedRows = preparedStatement.executeUpdate();
			return true;
		} catch (Exception e) {
			// TODO: Error Handling
			e.printStackTrace();
		} finally {
			close();
		}
		return false;
	}

	public Boolean NPCRemoveOwner(int NPCID, String Owner) {
		try {
			/* dbConnect(); */
			preparedStatement = connect
					.prepareStatement("delete from npcowner Where NPCID = ? and OwnerName = ?");
			preparedStatement.setInt(1, NPCID);
			preparedStatement.setString(2, Owner);

			int affectedRows = preparedStatement.executeUpdate();

			if (affectedRows == 0) {
				// TODO: Problem Handling
				return false;
			} else {
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return false;
	}

	public Boolean NPCRemoveManager(int NPCID, String Manager) {
		try {
			/* dbConnect(); */
			preparedStatement = connect
					.prepareStatement("delete from npcmanagers Where NPCID = ? and ManagerName = ?");
			preparedStatement.setInt(1, NPCID);
			preparedStatement.setString(2, Manager);

			int affectedRows = preparedStatement.executeUpdate();

			if (affectedRows == 0) {
				// TODO: Problem Handling
				return false;
			} else {
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return false;
	}

	public Boolean NPCSetManagerPermission(int NPCID, String Manager,
			String Permission, Boolean Value) {
		try {
			/* dbConnect(); */
			preparedStatement = connect
					.prepareStatement("Select * from npcmanagers where NPCID = ? and ManagerName = ?");
			preparedStatement.setInt(1, NPCID);
			preparedStatement.setString(2, Manager);
			resultSet = preparedStatement.executeQuery();
			int MID = 0;
			while (resultSet.next()) {
				MID = resultSet.getInt("ID");
			}

			if (MID != 0) {

				String UpdateSQL = "UPDATE npcmanagerpermissions ";
				UpdateSQL = UpdateSQL + "SET " + Permission + " = ?";
				UpdateSQL = UpdateSQL + " WHERE MID = ?";
				preparedStatement = connect.prepareStatement(UpdateSQL);
				preparedStatement.setBoolean(1, Value);
				preparedStatement.setInt(2, MID);

				int affectedRows = preparedStatement.executeUpdate();

				if (affectedRows == 0) {
					// TODO: Problem Handling
					return false;
				}

			} else {
				// TODO: Problem Handling
				return false;
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return false;

	}

	public Boolean NPCSetBanker(int NPCID, String Banker) {
		try {
			/* dbConnect(); */
			preparedStatement = connect
					.prepareStatement("UPDATE npc SET Banker = ? WHERE ID = ?");
			preparedStatement.setString(1, Banker);
			preparedStatement.setInt(2, NPCID);

			int affectedRows = preparedStatement.executeUpdate();

			if (affectedRows == 0) {
				// TODO: Problem Handling
				return false;
			}

			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return false;
	}

	public Boolean Upgrade(int NPCID, int Slots, int Units) {
		try {
			/* dbConnect(); */
			preparedStatement = connect
					.prepareStatement("UPDATE npc SET ItemSlots = ItemSlots + ?, Units = Units + ? WHERE ID = ?");
			preparedStatement.setInt(1, Math.abs(Slots));
			preparedStatement.setInt(2, Math.abs(Units));
			preparedStatement.setInt(3, NPCID);
			int affectedRows = preparedStatement.executeUpdate();

			if (affectedRows == 0) {
				// TODO: Problem Handling
				return false;
			}

			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return false;
	}

	public int NPCAddOwner(int NPCID, String Owner) {
		try {
			/* dbConnect(); */
			preparedStatement = connect
					.prepareStatement("insert into npcowner (NPCID, OwnerName) VALUES (?, ?)");
			preparedStatement.setInt(1, NPCID);
			preparedStatement.setString(2, Owner);

			int affectedRows = preparedStatement.executeUpdate();

			if (affectedRows == 0) {
				// TODO: Problem Handling
				return 0;
			}

			preparedStatement = connect
					.prepareStatement("select ID from npcowner where npcid = ? AND OwnerName = ? Order by ID desc limit 1");
			preparedStatement.setInt(1, NPCID);
			preparedStatement.setString(2, Owner);

			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				OwnerID = resultSet.getInt("id");
			} else {
				// TODO: Problem Handling
				return 0;
			}

			return OwnerID;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return 0;
	}

	public Boolean NPCMove(int NPCID, float X, float Y, float Z, float Pitch,
			float yaw) {
		try {
			/* dbConnect(); */
			String UpdateSQL = "UPDATE npc ";
			UpdateSQL = UpdateSQL + "SET PosX = ?";
			UpdateSQL = UpdateSQL + ", PosY = ?";
			UpdateSQL = UpdateSQL + ", PosZ = ?";
			UpdateSQL = UpdateSQL + ", PosPitch = ?";
			UpdateSQL = UpdateSQL + ", PosYaw = ?";
			UpdateSQL = UpdateSQL + " WHERE ID = ?";
			preparedStatement = connect.prepareStatement(UpdateSQL);
			preparedStatement.setFloat(1, X);
			preparedStatement.setFloat(2, Y);
			preparedStatement.setFloat(3, Z);
			preparedStatement.setFloat(4, Pitch);
			preparedStatement.setFloat(5, yaw);
			preparedStatement.setInt(6, NPCID);

			int affectedRows = preparedStatement.executeUpdate();

			if (affectedRows == 0) {
				// TODO: Problem Handling
				return false;
			}

			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return false;

	}

	public Boolean NPCMove(int NPCID, float Pitch, float yaw) {
		try {
			/* dbConnect(); */
			String UpdateSQL = "UPDATE npc ";
			UpdateSQL = UpdateSQL + "set PosPitch = " + Pitch;
			UpdateSQL = UpdateSQL + ", PosYaw = " + yaw;
			UpdateSQL = UpdateSQL + " WHERE ID = " + NPCID;
			preparedStatement = connect.prepareStatement(UpdateSQL);
			int affectedRows = preparedStatement.executeUpdate();

			if (affectedRows == 0) {
				// TODO: Problem Handling
				return false;
			}

			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return false;

	}

	public void SetupNPC(NPCTrader parent, List<World> worlds) {
		try {
			/* dbConnect(); */

			String UpdateSQL = "Select * from npc";
			preparedStatement = connect.prepareStatement(UpdateSQL);
			resultSet = preparedStatement.executeQuery();
			int NPCCount = 0;
			while (resultSet.next()) {
				for (World world : worlds) {
					// System.out.println("NPC " + resultSet.getString("ID")
					// + " - Checking for World: " + world.getName());
					if (world.getName().equalsIgnoreCase(
							resultSet.getString("WorldName").trim())) {
						// We need to see if we are in a chunk that is loaded...
						Location l = new Location(world,
								resultSet.getDouble("PosX"),
								resultSet.getDouble("PosY"),
								resultSet.getDouble("PosZ"),
								resultSet.getFloat("PosPitch"),
								resultSet.getFloat("PosYaw"));
							//We need to stick it in the global NPCObj
						NPCObj no = new NPCObj(resultSet.getString("ID"),l.getX(),l.getY(),l.getZ(),world.getName());
						parent.NPCL.put(resultSet.getString("ID"), no);
						if (world.isChunkLoaded(world.getChunkAt(l))) {
							//This npc is good to be loaded!
							NPCEntity hnpc = parent.npcm.spawnNPC(
									parent.getSettings().NPC("Prefix")
											+ resultSet.getString("NPCName")
											+ parent.getSettings().NPC("Suffix"),
									l, resultSet.getString("ID"));
						}
						++NPCCount;
					}
				}
			}
			System.out.println("Loaded " + NPCCount + " NPCs.");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}

	public void SetupNPC(Integer NPCID, NPCTrader parent, List<World> worlds) {
		try {
			/* dbConnect(); */

			String UpdateSQL = "Select * from npc WHERE ID = " + NPCID;
			// System.out.println(NPCID);
			preparedStatement = connect.prepareStatement(UpdateSQL);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				for (World world : worlds) {
					if (world.getName().equalsIgnoreCase(
							resultSet.getString("WorldName").trim())) {
						Location l = new Location(world,
								resultSet.getDouble("PosX"),
								resultSet.getDouble("PosY"),
								resultSet.getDouble("PosZ"),
								resultSet.getFloat("PosYaw"),
								resultSet.getFloat("PosPitch"));

						NPCEntity hnpc = parent.npcm.spawnNPC(
								parent.getSettings().NPC("Prefix")
										+ resultSet.getString("NPCName")
										+ parent.getSettings().NPC("Suffix"),
								l, resultSet.getString("ID"));
						// System.out.println("World Match... Added");
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}

	public void SetupNPC(Integer NPCID, String npcPrefix, String npcSuffix,
			NPCManager npcm, List<World> worlds) {
		try {
			/* dbConnect(); */

			String UpdateSQL = "Select * from npc WHERE ID = " + NPCID;
			// System.out.println(NPCID);
			preparedStatement = connect.prepareStatement(UpdateSQL);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				for (World world : worlds) {
					if (world.getName().equalsIgnoreCase(
							resultSet.getString("WorldName").trim())) {
						Location l = new Location(world,
								resultSet.getDouble("PosX"),
								resultSet.getDouble("PosY"),
								resultSet.getDouble("PosZ"),
								resultSet.getFloat("PosYaw"),
								resultSet.getFloat("PosPitch"));
						NPCEntity hnpc = npcm.spawnNPC(
								npcPrefix + resultSet.getString("NPCName")
										+ npcSuffix, l,
								resultSet.getString("ID"));
						// System.out.println("World Match... Added");
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}

	public Boolean NPCSetText(int NPCID, String Message, String Text) {
		try {
			/* dbConnect(); */

			String UpdateSQL = "UPDATE npctext ";
			UpdateSQL = UpdateSQL + "SET " + Message + " = ?";
			UpdateSQL = UpdateSQL + " WHERE NPCID = ?";
			preparedStatement = connect.prepareStatement(UpdateSQL);
			preparedStatement.setString(1, Text);
			preparedStatement.setInt(2, NPCID);

			// System.out.println("SQL Statement: " + UpdateSQL);

			int affectedRows = preparedStatement.executeUpdate();
			if (affectedRows == 0) {
				// TODO: Problem Handling
				System.out.println("SQL Statement: " + UpdateSQL);
				return false;
			}

			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return false;

	}

	public int CreateNPC(String NPCName, int ItemSlots, int Units,
			String Owner, String worldName) {

		try {
			/* dbConnect(); */
			preparedStatement = connect
					.prepareStatement("Insert Into npc (NPCName, ItemSlots, Units, banker, WorldName) values (?, ?, ?, ?, ?)");
			preparedStatement.setString(1, NPCName);
			preparedStatement.setInt(2, ItemSlots);
			preparedStatement.setInt(3, Units);
			preparedStatement.setString(4, Owner);
			preparedStatement.setString(5, worldName);

			int affectedRows = preparedStatement.executeUpdate();
			System.out.println("NPCT: " + affectedRows);
			if (affectedRows == 0) {
				// TODO: Problem Handling
				return 0;
			}

			preparedStatement = connect
					.prepareStatement("SELECT ID from npc where NPCName = ? AND banker = ? and WorldName = ? Order by ID DESC limit 1");
			preparedStatement.setString(1, NPCName);
			preparedStatement.setString(2, Owner);
			preparedStatement.setString(3, worldName);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				NPCID = resultSet.getInt("id");
				System.out.println("NPCID: " + NPCID);

			} else {
				// TODO: Problem Handling
				return 0;
			}

			for (int Count = 1; Count <= ItemSlots; Count++) {
				if (!this.AddSlot(NPCID, Count)) {
					// TODO: PRoblem with adding slots
					return 0;
				}
			}

			preparedStatement = connect
					.prepareStatement("insert into npctext (NPCID, GreetingMSg, "
							+ "Goodbyemsg, Sellmsg, buymsg, insqtybuymsg, insqtysellmsg, "
							+ "insfundsbuymsg, insfundssellmsg, notstockingmsg) VALUES ("
							+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			preparedStatement.setInt(1, NPCID);
			// TODO: Read msg's from config for defauls
			preparedStatement.setString(2, "Hello, how can I help you today?");
			preparedStatement.setString(3, "Thank you for shopping with us!");
			preparedStatement.setString(4, "You sold %q of %i for %a Credits.");
			preparedStatement.setString(5,
					"You bought %q of %i for %a Credits.");
			preparedStatement.setString(6,
					"Sorry but we do not have %q %i in stock.");
			preparedStatement.setString(7, "You do not have %q %i.");
			preparedStatement.setString(8,
					"Sorry but you do not have %a Credits.");
			preparedStatement
					.setString(9,
							"Sorry but I cannot afford to pay you %a Credits for %q %i.");
			preparedStatement
					.setString(10, "We are currently not stocking %i.");

			affectedRows = preparedStatement.executeUpdate();

			if (affectedRows == 0) {
				// TODO: Problem Handling
				return 0;
			}

			/*
			 * generatedKeys = preparedStatement.getGeneratedKeys(); if
			 * (generatedKeys.next()) { // } else { // TODO: Problem Handling
			 * return 0; }
			 */
			return NPCID;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return 0;
	}

	public void dbClose() {
		try {
			if (connect != null) {
				connect.close();
			}
		} catch (Exception e) {

		}
	}

	public boolean dbConnect() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			// System.out.println(NPCTrader.getSettings().DB("Database"));

			connect = DriverManager.getConnection(
					NPCTrader.getSettings().DB("Database"), NPCTrader
							.getSettings().DB("User"), NPCTrader.getSettings()
							.DB("Pass"));
			// System.setProperty("sqlite.purejava", "false");
			// Class.forName("org.sqlite.JDBC");
			// System.out.println(NPCTrader.getSettings().DB("Database"));

			// connect = DriverManager.getConnection(NPCTrader.getSettings().DB(
			// "Database"));

		} catch (Exception e) {
			// TODO: Problem Handling
			// e.printStackTrace();
			System.out
					.println("Database configuration Error!\nVerify your config.yml settings are correct and valid for your MySQL setup.\nReturned Error: "
							+ e.toString());
			return false;
		} finally {

		}
		return true;
	}

	private void close() {
		try {
			if (resultSet != null) {
				resultSet.close();
			}

			if (generatedKeys != null) {
				generatedKeys.close();
			}

			if (statement != null) {
				statement.close();
			}

			if (preparedStatement != null) {
				preparedStatement.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String GetBanker(int NPCID) {
		try {
			/* dbConnect(); */
			preparedStatement = connect
					.prepareStatement("Select Banker from npc WHERE ID = ?");
			preparedStatement.setInt(1, NPCID);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				return resultSet.getString("Banker");
			}

		} catch (Exception e) {
			e.printStackTrace();

		}
		return "";
	}

	/*
	 * public void AddBalance(int NPCID, int Amount) { // Returns new balance
	 * try { /*dbConnect(); preparedStatement =
	 * connect.prepareStatement("Update NPC set balance = balance + ? WHERE ID = ?"
	 * ); preparedStatement.setInt(1, Qty); preparedStatement.setInt(2, NPCID);
	 * preparedStatement.setInt(3, SlotID); resultSet =
	 * preparedStatement.executeQuery();
	 * 
	 * } catch (Exception e) { e.printStackTrace();
	 * 
	 * } }
	 */
	public Double GetTotalCost(int NPCID, int SlotID, int Qty, Boolean IsBuy) {
		try {
			/* dbConnect(); */
			String BSFlag;
			if (IsBuy) {
				BSFlag = "buy";
			} else {
				BSFlag = "sell";
			}
			preparedStatement = connect
					.prepareStatement("select (Item"
							+ BSFlag
							+ "Price * ?) TotalCost from npcinventory  where NPCID = ? and SlotID = ?");
			preparedStatement.setInt(1, Qty);
			preparedStatement.setInt(2, NPCID);
			preparedStatement.setInt(3, SlotID);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				return resultSet.getDouble("TotalCost");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return (double) 0;
	}

	public int GetMaxStock(int NPCID, NPCTrader parent) {
		try {
			/* dbConnect(); */
			preparedStatement = connect.prepareStatement("select (Units * "
					+ parent.getSettings().UnitStack()
					+ ") MaxStock from npc where ID = ?");
			preparedStatement.setInt(1, NPCID);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				return resultSet.getInt("MaxStock");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public String GetOwnerList(int NPCID) {
		try {
			/* dbConnect(); */
			preparedStatement = connect
					.prepareStatement("select (o.OwnerName) OwnerName, (n.Banker) Banker from npcowner o join npc n on o.NPCID = n.ID where o.NPCID = ?");
			preparedStatement.setInt(1, NPCID);
			resultSet = preparedStatement.executeQuery();
			String Return = "";
			String Banker = "";
			while (resultSet.next()) {
				Banker = resultSet.getString("Banker");
				String Inject = "";
				if (Banker.equalsIgnoreCase(resultSet.getString("OwnerName")))
					Inject = " (BANKER)";
				Return = Return + resultSet.getString("OwnerName") + Inject
						+ ", ";
			}
			return Return.substring(0, Return.length() - 2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";

	}

	public int GetTotalNPC(String Player) {
		String sSQL = "select (count(id)) Count from npcowner where ownername like ? group by ownername";
		try {
			preparedStatement = connect.prepareStatement(sSQL);
			preparedStatement.setString(1, Player);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				return resultSet.getInt("Count");
			}
		} catch (Exception e) {
			return 0;
		}
		return 0;

	}

	public void SetName(String NPCName, int NPCID) {
		try {
			preparedStatement = connect
					.prepareStatement("UPDATE npc SET NPCName = ? WHERE ID = ?");
			preparedStatement.setString(1, NPCName);
			preparedStatement.setInt(2, NPCID);
			int affectedRows = preparedStatement.executeUpdate();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public ResultSet GetSlotList(int NPCID, int Page) {
		try {
			/* dbConnect(); */
			preparedStatement = connect
					.prepareStatement("select * from npcinventory where NPCID = ? order by id asc Limit 8 offset ?");
			preparedStatement.setInt(1, NPCID);
			preparedStatement.setInt(2, (Page * 8) - 8);
			resultSet = preparedStatement.executeQuery();
			return resultSet;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public ResultSet GetManagerList(int NPCID, int Page) {
		try {
			/* dbConnect(); */
			preparedStatement = connect
					.prepareStatement("select * from npcmanagers m join npcmanagerpermissions p on m.id = p.mid WHERE NPCID = ? limit 8 offset ?");
			preparedStatement.setInt(1, NPCID);
			preparedStatement.setInt(2, (Page * 8) - 8);
			resultSet = preparedStatement.executeQuery();
			return resultSet;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public ResultSet GetNPCList(int Page) {
		try {
			/* dbConnect(); */
			preparedStatement = connect
					.prepareStatement("select * from npc order by Banker limit 8 offset ?");
			preparedStatement.setInt(1, (Page * 8) - 8);
			resultSet = preparedStatement.executeQuery();
			return resultSet;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public int GetTotalSlots(int NPCID) {
		try {
			/* dbConnect(); */
			preparedStatement = connect
					.prepareStatement("select ItemSlots from npc where ID = ?");
			preparedStatement.setInt(1, NPCID);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				return resultSet.getInt("ItemSlots");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public int GetTotalUnits(int NPCID) {
		try {
			/* dbConnect(); */
			preparedStatement = connect
					.prepareStatement("select Units from npc where ID = ?");
			preparedStatement.setInt(1, NPCID);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				return resultSet.getInt("Units");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public int GetTotalManagers(int NPCID, String Manager) {
		try {
			/* dbConnect(); */
			String Inject = "";
			if (!this.CheckOwner(NPCID, Manager))
				Inject = " AND ManagerName='" + Manager + "'";
			preparedStatement = connect
					.prepareStatement("select count(id) TM from npcmanagers where NPCID = ?"
							+ Inject);
			preparedStatement.setInt(1, NPCID);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				return resultSet.getInt("TM");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public int GetStock(int NPCID, int SlotID) {
		try {
			/* dbConnect(); */
			preparedStatement = connect
					.prepareStatement("Select UnitQty from npcinventory where NPCID = ? and SlotID = ?");
			preparedStatement.setInt(1, NPCID);
			preparedStatement.setInt(2, SlotID);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				return resultSet.getInt("UnitQty");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public Boolean IsBS(int NPCID, int SlotID, Boolean IsBuy) {
		try {
			String BS = "";
			if (IsBuy)
				BS = "buy";
			if (!IsBuy)
				BS = "sell";
			/* dbConnect(); */
			preparedStatement = connect.prepareStatement("Select is" + BS
					+ " from npcinventory where NPCID = ? and SlotID = ?");
			preparedStatement.setInt(1, NPCID);
			preparedStatement.setInt(2, SlotID);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				return resultSet.getBoolean("is" + BS);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public void RemoveStock(Integer NPCID, int SlotID, int Qty) {
		try {
			/* dbConnect(); */
			preparedStatement = connect
					.prepareStatement("Update npcinventory SET UnitQty = IFNULL(unitqty,0) - ? WHERE NPCID = ? AND SlotID = ?");
			preparedStatement.setInt(1, Qty);
			preparedStatement.setInt(2, NPCID);
			preparedStatement.setInt(3, SlotID);
			preparedStatement.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void AddStock(Integer NPCID, int SlotID, int Qty) {
		try {
			/* dbConnect(); */
			preparedStatement = connect
					.prepareStatement("Update npcinventory SET UnitQty = IFNULL(unitqty,0) + ? WHERE NPCID = ? AND SlotID = ?");
			preparedStatement.setInt(1, Qty);
			preparedStatement.setInt(2, NPCID);
			preparedStatement.setInt(3, SlotID);
			preparedStatement.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int GetSlotLot(Integer NPCID, int SlotID) {
		try {
			/* dbConnect(); */
			preparedStatement = connect
					.prepareStatement("Select LotQty from npcinventory where NPCID = ? and SlotID = ?");
			preparedStatement.setInt(1, NPCID);
			preparedStatement.setInt(2, SlotID);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				return resultSet.getInt("LotQty");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public int GetTotalNPCs() {
		try {
			/* dbConnect(); */
			preparedStatement = connect
					.prepareStatement("select count(id) TN from npc");
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				return resultSet.getInt("TN");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
}
