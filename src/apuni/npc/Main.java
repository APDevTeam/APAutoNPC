package apuni.npc;

import java.util.LinkedList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.CitizensPlugin;
import net.citizensnpcs.api.npc.NPC;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin {

    private static Economy economy;

    public void onEnable() {

        economy = getServer().getServicesManager().getRegistration(Economy.class).getProvider();
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("AutoNPC")) {
            sender.sendMessage(ChatColor.AQUA + "Purchase NPC's of your choice!");
            sender.sendMessage(ChatColor.AQUA + "For NPC Rules go here:");
            sender.sendMessage(ChatColor.AQUA + "https://goo.gl/WOYxmT");
			sender.sendMessage(ChatColor.AQUA + "Want to buy a NPC? Run the command /help APAutoNpc");
			return true;
        }

        if (economy.getBalance(player.getName()) < 1000000) {
            sender.sendMessage("You do not have enough money. NPC's cost atleast 1mil apeice!.");
            return true;
        }
        // get the list of regions that contain the given location
        Location location = player.getLocation();
        RegionManager regionManager = ((WorldGuardPlugin) getServer().getPluginManager().getPlugin("WorldGuard")).getRegionManager( location.getWorld());
        ApplicableRegionSet set = regionManager.getApplicableRegions( location );
        LinkedList< String > parentNames = new LinkedList< String >();
        LinkedList< String > regions = new LinkedList< String >();
        for ( ProtectedRegion region : set ) {
            String id = region.getId();
            regions.add( id );
            ProtectedRegion parent = region.getParent();
            while ( parent != null ) {
                parentNames.add( parent.getId());
                parent = parent.getParent();
            }
        }
        // before we return, we remove any area's that are 'parent' to an existing area
        for ( String name : parentNames )
            regions.remove( name );
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
        
        if (cmd.getName().equalsIgnoreCase("test")) {
            NPC temp = ( (CitizensPlugin) CitizensAPI.getPlugin() ).getNPCRegistry().getById(14).clone();
            temp.teleport(player.getLocation(),null); 
            temp.teleport(player.getLocation(),null); 
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "eco take " + player.getName() + " 1000000");
            sender.sendMessage(ChatColor.DARK_AQUA + "You have bought a Building Block Merchant");
            return true;
        }
        
        if (cmd.getName().equalsIgnoreCase("BBMerch")) {
			((CitizensPlugin)CitizensAPI.getPlugin()).getNPCRegistry().getById(2056).clone().teleport(player.getLocation(), null);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "eco take " + player.getName() + " 1000000");
            sender.sendMessage(ChatColor.DARK_AQUA + "You have bought a Building Block Merchant");
            return true;
        }

        if (cmd.getName().equalsIgnoreCase("MineralMerch")) {
            ( (CitizensPlugin) CitizensAPI.getPlugin() ).getNPCRegistry().getById(1297).clone().teleport(player.getLocation(),null);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "eco take " + player.getName() + " 1000000");
            sender.sendMessage(ChatColor.DARK_AQUA + "You have bought a Mineral Merchant Merchant");
            return true;
        }

        if (cmd.getName().equalsIgnoreCase("ShipMerch")) {
            ( (CitizensPlugin) CitizensAPI.getPlugin() ).getNPCRegistry().getById(1759).clone().teleport(player.getLocation(),null);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "eco take " + player.getName() + " 1000000");
            sender.sendMessage(ChatColor.DARK_AQUA + "You have bought a Ship Supply Merchant Merchant");
            return true;
        }

        if (cmd.getName().equalsIgnoreCase("SmithMerch")) {
            ( (CitizensPlugin) CitizensAPI.getPlugin() ).getNPCRegistry().getById(2057).clone().teleport(player.getLocation(),null);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "eco take " + player.getName() + " 1000000");
            sender.sendMessage(ChatColor.DARK_AQUA + "You have bought a Blacksmith Merchant");
            return true;
        }

        if (cmd.getName().equalsIgnoreCase("FoodMerch")) {
            ( (CitizensPlugin) CitizensAPI.getPlugin() ).getNPCRegistry().getById(2058).clone().teleport(player.getLocation(),null);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "eco take " + player.getName() + " 1000000");
            sender.sendMessage(ChatColor.DARK_AQUA + "You have bought a Foodstuffs Merchant Merchant");
            return true;
        }

        if (cmd.getName().equalsIgnoreCase("BTMerchDisabled")) {
            ( (CitizensPlugin) CitizensAPI.getPlugin() ).getNPCRegistry().getById(1435).clone().teleport(player.getLocation(),null);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "eco take " + player.getName() + " 1000000");
            sender.sendMessage(ChatColor.DARK_AQUA + "You have bought a BarTender Merchant");
            return true;
        }

        if (cmd.getName().equalsIgnoreCase("BasicBrawler")) {
            NPC npc = ( (CitizensPlugin) CitizensAPI.getPlugin() ).getNPCRegistry().getById(1651).clone();
            npc.teleport(player.getLocation(),null);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "npc select " + npc.getId());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "eco take " + player.getName() + " 500000");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sentry spawn");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sentry healrate 2.5");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sentry respawn 300");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "npc owner " + player.getName() + " ");
            sender.sendMessage(ChatColor.DARK_AQUA + "You have bought a Basic Brawler");
            return true;
        }

        if (cmd.getName().equalsIgnoreCase("AdvBrawler")) {
            NPC npc = ( (CitizensPlugin) CitizensAPI.getPlugin() ).getNPCRegistry().getById(1653).clone();
            npc.teleport(player.getLocation(),null);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "npc select " + npc.getId());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "eco take " + player.getName() + " 1000000");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sentry spawn");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sentry healrate 2.5");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sentry respawn 300");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "npc owner " + player.getName() + " ");
            sender.sendMessage(ChatColor.DARK_AQUA + "You have bought a Adv Brawler");
            return true;
        }

        if (cmd.getName().equalsIgnoreCase("BasicArcher")) {
            NPC npc = ( (CitizensPlugin) CitizensAPI.getPlugin() ).getNPCRegistry().getById(1747).clone();
            npc.teleport(player.getLocation(),null);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "npc select " + npc.getId());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "eco take " + player.getName() + " 500000");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sentry spawn");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sentry healrate 2.5");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sentry respawn 300");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "npc owner " + player.getName() + " ");
            sender.sendMessage(ChatColor.DARK_AQUA + "You have bought a Basic Archer");
            return true;
        }

        if (cmd.getName().equalsIgnoreCase("AdvArcher")) {
            NPC npc = ( (CitizensPlugin) CitizensAPI.getPlugin() ).getNPCRegistry().getById(1654).clone();
            npc.teleport(player.getLocation(),null);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "npc select " + npc.getId());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "eco take " + player.getName() + " 1000000");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sentry spawn");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sentry healrate 2.5");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sentry respawn 300");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "npc owner " + player.getName() + " ");
			sender.sendMessage(ChatColor.DARK_AQUA + "You have bought a Adv Archer");
            return true;
        }

        if (cmd.getName().equalsIgnoreCase("BasicBlade")){
            if (economy.getBalance(player.getName()) > 1250000) {
                NPC npc = ( (CitizensPlugin) CitizensAPI.getPlugin() ).getNPCRegistry().getById(1835).clone();
                npc.teleport(player.getLocation(),null);
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "npc select " + npc.getId());
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "eco take " + player.getName() + " 1250000");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sentry spawn");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sentry healrate 2.5");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sentry respawn 300");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "npc owner " + player.getName() + " ");
                sender.sendMessage(ChatColor.DARK_AQUA + "You have bought a Basic Bladesmen");
            }
            else {sender.sendMessage("You do not have enough money.");
            }
            return true;
        }

        if (cmd.getName().equalsIgnoreCase("BasicShort")) {
            if (economy.getBalance(player.getName()) > 1250000) {
                NPC npc = ( (CitizensPlugin) CitizensAPI.getPlugin() ).getNPCRegistry().getById(1660).clone();
                npc.teleport(player.getLocation(),null);
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "npc select " + npc.getId());
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "eco take " + player.getName() + " 1250000");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sentry spawn");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sentry healrate 2.5");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sentry respawn 300");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "npc owner " + player.getName() + " ");
                sender.sendMessage(ChatColor.DARK_AQUA + "You have bought a Basic Shortbow");
            }
            else {sender.sendMessage("You do not have enough money.");
            }
            return true;
        }

        if (cmd.getName().equalsIgnoreCase("BasicArm")) {
            if (economy.getBalance(player.getName()) > 1500000) {
				NPC npc = ((CitizensPlugin)CitizensAPI.getPlugin()).getNPCRegistry().getById(1936).clone();
                npc.teleport(player.getLocation(),null);
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "npc select " + npc.getId());
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "eco take " + player.getName() + " 1500000");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sentry spawn");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sentry healrate 2.5");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sentry respawn 300");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "npc owner " + player.getName() + " ");
                sender.sendMessage(ChatColor.DARK_AQUA + "You have bought a Basic Armorer");
            }
            else {sender.sendMessage("You do not have enough money.");
            }
            return true;
        }

        if (cmd.getName().equalsIgnoreCase("BasicLongBow")) {
            if (economy.getBalance(player.getName()) > 1500000) {
                NPC npc = ( (CitizensPlugin) CitizensAPI.getPlugin() ).getNPCRegistry().getById(1659).clone();
                npc.teleport(player.getLocation(),null);
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "npc select " + npc.getId());
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "eco take " + player.getName() + " 1500000");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sentry spawn");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sentry healrate 2.5");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sentry respawn 300");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "npc owner " + player.getName() + " ");
				sender.sendMessage(ChatColor.DARK_AQUA + "You have bought a Basic Longbow");
            }
            else {sender.sendMessage("You do not have enough money.");
            }
            return true;
        }

        if (cmd.getName().equalsIgnoreCase("AdvBlade")) {
            if (economy.getBalance(player.getName()) > 1750000) {
                NPC npc = ( (CitizensPlugin) CitizensAPI.getPlugin() ).getNPCRegistry().getById(1657).clone();
                npc.teleport(player.getLocation(),null);
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "npc select " + npc.getId());
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "eco take " + player.getName() + " 1750000");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sentry spawn");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sentry healrate 2.5");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sentry respawn 300");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "npc owner " + player.getName() + " ");
                sender.sendMessage(ChatColor.DARK_AQUA + "You have bought a Advanced Bladesman");
            }
            else {sender.sendMessage("You do not have enough money.");
            }
            return true;
        }

        if (cmd.getName().equalsIgnoreCase("AdvLongBow")) {
            if (economy.getBalance(player.getName()) > 2500000) {
				NPC npc = ((CitizensPlugin)CitizensAPI.getPlugin()).getNPCRegistry().getById(2086).clone();
                npc.teleport(player.getLocation(),null);
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "npc select " + npc.getId());
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "eco take " + player.getName() + " 2500000");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sentry spawn");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sentry healrate 2.5");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sentry respawn 300");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "npc owner " + player.getName() + " ");
                sender.sendMessage(ChatColor.DARK_AQUA + "You have bought a Advanced Longbow");
            }
            else {sender.sendMessage("You do not have enough money.");
            }
            return true;
        }
        if (cmd.getName().equalsIgnoreCase("AdvShortbow")) {
            if (economy.getBalance(player.getName()) > 1750000) {
                NPC npc = ( (CitizensPlugin) CitizensAPI.getPlugin() ).getNPCRegistry().getById(1662).clone();
                npc.teleport(player.getLocation(),null);
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "npc select " + npc.getId());
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "eco take " + player.getName() + " 1750000");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sentry spawn");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sentry healrate 2.5");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sentry respawn 300");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "npc owner " + player.getName() + " ");
                sender.sendMessage(ChatColor.DARK_AQUA + "You have bought a Advanced Shortbow");
            }
            else {sender.sendMessage("You do not have enough money.");
            }
            return true;
        }
        if (cmd.getName().equalsIgnoreCase("AdvArm")) {
            if (economy.getBalance(player.getName()) > 2500000) {
				NPC npc = ((CitizensPlugin)CitizensAPI.getPlugin()).getNPCRegistry().getById(2087).clone();
                npc.teleport(player.getLocation(),null);
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "npc select " + npc.getId());
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "eco take " + player.getName() + " 2500000");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sentry spawn");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sentry healrate 2.5");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sentry respawn 300");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "npc owner " + player.getName() + " ");
                sender.sendMessage(ChatColor.DARK_AQUA + "You have bought a Advanced Armorer");
            }
            else {sender.sendMessage("You do not have enough money.");
            }
            return true;
        }
        return false;
    }
}