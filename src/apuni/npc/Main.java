package apuni.npc;

import java.util.LinkedList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.CitizensPlugin;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin{
	private static Economy economy;
	private static NPCRegistry registry;
	// @formatter:off
    public static final String	TOO_POOR_MSG = 						"You can't afford $0! It cost $1.",
    							PURCHASE_SUCCESS =					"You have purchased $0 for $1.",
    							MERCHANT_BUILDING_BLOCKS_ALIAS =	"BBMerch",
    							MERCHANT_BUILDING_BLOCKS_NAME = 	"Building Blocks Merchant";
    public static final double	MERCHANT_BUILDING_BLOCKS_PRICE =	1000000;
    public static final int		MERCHANT_BUILDING_BLOCKS_NPCID =	2056;
    // @formatter:on
	
	public void onEnable(){
		economy = getServer().getServicesManager().getRegistration(Economy.class).getProvider();
		registry = ((CitizensPlugin)CitizensAPI.getPlugin()).getNPCRegistry();
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(!(sender instanceof Player)){
			sender.sendMessage("You can only use this command from in the game!");
			return true;
		}
		
		Player player = (Player)sender;
		
		if(cmd.getName().equalsIgnoreCase("AutoNPC")){
			sender.sendMessage(ChatColor.AQUA + "Purchase NPC's of your choice!");
			sender.sendMessage(ChatColor.AQUA + "For NPC Rules go here:");
			sender.sendMessage(ChatColor.AQUA + "https://goo.gl/WOYxmT");
			sender.sendMessage(ChatColor.AQUA + "Want to buy a NPC? Run the command /help APAutoNpc");
			return true;
		}
		
		if(economy.getBalance(player) < 1000000){
			sender.sendMessage("You do not have enough money. NPC's cost atleast 1mil apeice!.");
			return true;
		}
		// get the list of regions that contain the given location
		Location location = player.getLocation();
		RegionManager regionManager = ((WorldGuardPlugin)getServer().getPluginManager().getPlugin("WorldGuard")).getRegionManager(location.getWorld());
		ApplicableRegionSet set = regionManager.getApplicableRegions(location);
		LinkedList<String> parentNames = new LinkedList<String>();
		LinkedList<String> regions = new LinkedList<String>();
		for(ProtectedRegion region : set){
			String id = region.getId();
			regions.add(id);
			ProtectedRegion parent = region.getParent();
			while(parent != null){
				parentNames.add(parent.getId());
				parent = parent.getParent();
			}
		}
		// before we return, we remove any area's that are 'parent' to an existing area
		for(String name : parentNames)
			regions.remove(name);
		boolean inAirspace = false;
		for(String name : regions){
			if(name.toLowerCase().contains("airspace")){
				inAirspace = true;
				break;
			}
		}
		
		if(!inAirspace){
			sender.sendMessage("you need to be in airspace");
			return true;
		}
		
		if(cmd.getName().equalsIgnoreCase("test")){
			NPC temp = ((CitizensPlugin)CitizensAPI.getPlugin()).getNPCRegistry().getById(14).clone();
			temp.teleport(player.getLocation(), null);
			temp.teleport(player.getLocation(), null);
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "eco take " + player.getName() + " 1000000");
			sender.sendMessage(ChatColor.DARK_AQUA + "You have bought a Building Block Merchant");
			return true;
		}
		
		// START MERCHANTS
		
		for(APNPC n : APNPC.values()){
			if(cmd.getName().equalsIgnoreCase(n.alias)){
				if(economy.withdrawPlayer(player, n.price).transactionSuccess()){ // If transaction sucessful
					NPC newNPC = registry.getById(n.npcID).clone();
					newNPC.getStoredLocation().getChunk().load(); // Load chunk
					Location dest = player.getLocation();
					newNPC.teleport(dest, TeleportCause.PLUGIN);
					sender.sendMessage(PURCHASE_SUCCESS.replace("$0", n.name).replace("$1", formatBalance(n.price)));
					return true;
				}
				else{ // If you didn't have enough money
					sender.sendMessage(TOO_POOR_MSG.replace("$0", n.name).replace("$1", formatBalance(n.price)));
					return true;
				}
			}
			// else do nothing
		}
		
		
		// END MERCHANTs
		
		if(cmd.getName().equalsIgnoreCase("BasicBrawler")){
			NPC npc = ((CitizensPlugin)CitizensAPI.getPlugin()).getNPCRegistry().getById(1651).clone();
			npc.teleport(player.getLocation(), null);
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "npc select " + npc.getId());
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "eco take " + player.getName() + " 500000");
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sentry spawn");
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sentry healrate 2.5");
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sentry respawn 300");
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "npc owner " + player.getName() + " ");
			sender.sendMessage(ChatColor.DARK_AQUA + "You have bought a Basic Brawler");
			return true;
		}
		
		if(cmd.getName().equalsIgnoreCase("AdvBrawler")){
			NPC npc = ((CitizensPlugin)CitizensAPI.getPlugin()).getNPCRegistry().getById(1653).clone();
			npc.teleport(player.getLocation(), null);
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "npc select " + npc.getId());
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "eco take " + player.getName() + " 1000000");
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sentry spawn");
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sentry healrate 2.5");
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sentry respawn 300");
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "npc owner " + player.getName() + " ");
			sender.sendMessage(ChatColor.DARK_AQUA + "You have bought a Adv Brawler");
			return true;
		}
		
		if(cmd.getName().equalsIgnoreCase("BasicArcher")){
			NPC npc = ((CitizensPlugin)CitizensAPI.getPlugin()).getNPCRegistry().getById(1747).clone();
			npc.teleport(player.getLocation(), null);
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "npc select " + npc.getId());
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "eco take " + player.getName() + " 500000");
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sentry spawn");
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sentry healrate 2.5");
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sentry respawn 300");
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "npc owner " + player.getName() + " ");
			sender.sendMessage(ChatColor.DARK_AQUA + "You have bought a Basic Archer");
			return true;
		}
		
		if(cmd.getName().equalsIgnoreCase("AdvArcher")){
			NPC npc = ((CitizensPlugin)CitizensAPI.getPlugin()).getNPCRegistry().getById(1654).clone();
			npc.teleport(player.getLocation(), null);
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "npc select " + npc.getId());
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "eco take " + player.getName() + " 1000000");
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sentry spawn");
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sentry healrate 2.5");
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sentry respawn 300");
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "npc owner " + player.getName() + " ");
			sender.sendMessage(ChatColor.DARK_AQUA + "You have bought a Adv Archer");
			return true;
		}
		
		if(cmd.getName().equalsIgnoreCase("BasicBlade")){
			if(economy.getBalance(player) > 1250000){
				NPC npc = ((CitizensPlugin)CitizensAPI.getPlugin()).getNPCRegistry().getById(1835).clone();
				npc.teleport(player.getLocation(), null);
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "npc select " + npc.getId());
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "eco take " + player.getName() + " 1250000");
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sentry spawn");
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sentry healrate 2.5");
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sentry respawn 300");
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "npc owner " + player.getName() + " ");
				sender.sendMessage(ChatColor.DARK_AQUA + "You have bought a Basic Bladesmen");
			}
			else{
				sender.sendMessage("You do not have enough money.");
			}
			return true;
		}
		
		if(cmd.getName().equalsIgnoreCase("BasicShort")){
			if(economy.getBalance(player) > 1250000){
				NPC npc = ((CitizensPlugin)CitizensAPI.getPlugin()).getNPCRegistry().getById(1660).clone();
				npc.teleport(player.getLocation(), null);
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "npc select " + npc.getId());
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "eco take " + player.getName() + " 1250000");
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sentry spawn");
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sentry healrate 2.5");
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sentry respawn 300");
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "npc owner " + player.getName() + " ");
				sender.sendMessage(ChatColor.DARK_AQUA + "You have bought a Basic Shortbow");
			}
			else{
				sender.sendMessage("You do not have enough money.");
			}
			return true;
		}
		
		if(cmd.getName().equalsIgnoreCase("BasicArm")){
			if(economy.getBalance(player) > 1500000){
				NPC npc = ((CitizensPlugin)CitizensAPI.getPlugin()).getNPCRegistry().getById(1936).clone();
				npc.teleport(player.getLocation(), null);
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "npc select " + npc.getId());
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "eco take " + player.getName() + " 1500000");
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sentry spawn");
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sentry healrate 2.5");
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sentry respawn 300");
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "npc owner " + player.getName() + " ");
				sender.sendMessage(ChatColor.DARK_AQUA + "You have bought a Basic Armorer");
			}
			else{
				sender.sendMessage("You do not have enough money.");
			}
			return true;
		}
		
		if(cmd.getName().equalsIgnoreCase("BasicLongBow")){
			if(economy.getBalance(player) > 1500000){
				NPC npc = ((CitizensPlugin)CitizensAPI.getPlugin()).getNPCRegistry().getById(1659).clone();
				npc.teleport(player.getLocation(), null);
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "npc select " + npc.getId());
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "eco take " + player.getName() + " 1500000");
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sentry spawn");
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sentry healrate 2.5");
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sentry respawn 300");
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "npc owner " + player.getName() + " ");
				sender.sendMessage(ChatColor.DARK_AQUA + "You have bought a Basic Longbow");
			}
			else{
				sender.sendMessage("You do not have enough money.");
			}
			return true;
		}
		
		if(cmd.getName().equalsIgnoreCase("AdvBlade")){
			if(economy.getBalance(player) > 1750000){
				NPC npc = ((CitizensPlugin)CitizensAPI.getPlugin()).getNPCRegistry().getById(1657).clone();
				npc.teleport(player.getLocation(), null);
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "npc select " + npc.getId());
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "eco take " + player.getName() + " 1750000");
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sentry spawn");
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sentry healrate 2.5");
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sentry respawn 300");
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "npc owner " + player.getName() + " ");
				sender.sendMessage(ChatColor.DARK_AQUA + "You have bought a Advanced Bladesman");
			}
			else{
				sender.sendMessage("You do not have enough money.");
			}
			return true;
		}
		
		if(cmd.getName().equalsIgnoreCase("AdvLongBow")){
			if(economy.getBalance(player) > 2500000){
				NPC npc = ((CitizensPlugin)CitizensAPI.getPlugin()).getNPCRegistry().getById(2086).clone();
				npc.teleport(player.getLocation(), null);
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "npc select " + npc.getId());
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "eco take " + player.getName() + " 2500000");
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sentry spawn");
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sentry healrate 2.5");
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sentry respawn 300");
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "npc owner " + player.getName() + " ");
				sender.sendMessage(ChatColor.DARK_AQUA + "You have bought a Advanced Longbow");
			}
			else{
				sender.sendMessage("You do not have enough money.");
			}
			return true;
		}
		if(cmd.getName().equalsIgnoreCase("AdvShortbow")){
			if(economy.getBalance(player) > 1750000){
				NPC npc = ((CitizensPlugin)CitizensAPI.getPlugin()).getNPCRegistry().getById(1662).clone();
				npc.teleport(player.getLocation(), null);
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "npc select " + npc.getId());
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "eco take " + player.getName() + " 1750000");
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sentry spawn");
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sentry healrate 2.5");
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sentry respawn 300");
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "npc owner " + player.getName() + " ");
				sender.sendMessage(ChatColor.DARK_AQUA + "You have bought a Advanced Shortbow");
			}
			else{
				sender.sendMessage("You do not have enough money.");
			}
			return true;
		}
		if(cmd.getName().equalsIgnoreCase("AdvArm")){
			if(economy.getBalance(player) > 2500000){
				NPC npc = ((CitizensPlugin)CitizensAPI.getPlugin()).getNPCRegistry().getById(2087).clone();
				npc.teleport(player.getLocation(), null);
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "npc select " + npc.getId());
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "eco take " + player.getName() + " 2500000");
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sentry spawn");
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sentry healrate 2.5");
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sentry respawn 300");
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "npc owner " + player.getName() + " ");
				sender.sendMessage(ChatColor.DARK_AQUA + "You have bought a Advanced Armorer");
			}
			else{
				sender.sendMessage("You do not have enough money.");
			}
			return true;
		}
		return false;
	}
	
	/**
	 * The following method reformats a double for balances
	 * @param bal The balance to be reformatted
	 * @return A formatted representation of the balance
	 */
	protected static String formatBalance(double bal){
		String rep = String.format("%1$.2f", bal);
		if(rep.length() >= 7) {
			String a = rep.substring(0, rep.indexOf('.') - 3);
			String b = rep.substring(rep.indexOf('.') - 3, rep.length());
			rep = a + ',' + b;
		}
		if(rep.length() >= 11){
			String a = rep.substring(0, rep.indexOf(',') - 3);
			String b = rep.substring(rep.indexOf(',') - 3, rep.length());
			rep = a + ',' + b;
		}
		return rep + " soft";
	}
}
