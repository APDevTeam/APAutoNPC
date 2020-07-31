package net.apdevteam.apautonpc;

import java.util.HashMap;
import java.util.LinkedList;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
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




public class APAutoNPC extends JavaPlugin {
    private Economy economy;
    private NPCRegistry registry;
    public static final String Not_Enough_Money = ChatColor.DARK_AQUA + "You cannot afford this!";
    public static final String Successfull_Purchase = ChatColor.DARK_GRAY + "Congratulations on your new NPC!";
    public static final int Merchant_Cost = 1000000;
    public static final int Basic_Brawl_Arch_Cost = 500000;
    public static final int Adv_Brawl_Arch_Cost = 1000000;
    public static final int Basic_Blade_Shortbow_Cost = 1250000;
    public static final int Basic_Arm_Longbow_Cost = 1500000;
    public static final int Adv_Blade_Shortbow_Cost = 1750000;
    public static final int Adv_Longbow_Arm_Cost = 2500000;
    public static final String Not_A_NPC = ChatColor.DARK_GRAY + "That is not a valid NPC type, please type /AutoNPC help";
    HashMap<String, Integer> merchIDs = new HashMap<String, Integer>();
    HashMap<String, Integer> npcIDs = new HashMap<String, Integer>();


    public void onEnable() {
        economy = getServer().getServicesManager().getRegistration(Economy.class).getProvider();
        registry = ((CitizensPlugin)CitizensAPI.getPlugin()).getNPCRegistry();
        merchIDs.put("BBMerch", 1471);
        merchIDs.put("MineralMerch", 1297);
        merchIDs.put("FoodMerch", 2058);
        merchIDs.put("BSMerch", 2057);
        merchIDs.put("ShipMerch", 1759);
        merchIDs.put("BTMerch", 1435);
    }

    public void onDisable() {
        Plugin plugin = null;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(!(sender instanceof Player)){
            sender.sendMessage("You must be a player to use this command");
            return false;
        }

        Player player = (Player) sender;
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
        for(String name : parentNames)
            regions.remove(name);
        boolean inAirspace = false;
        for(String name : regions){
            if(name.toLowerCase().contains("airspace")){
                inAirspace = true;
                break;
            }
        }

        if(cmd.getName().equalsIgnoreCase("AutoNPC")) {
            Boolean yes = true;

            if(!inAirspace){
                sender.sendMessage("you need to be in airspace");
                return true;
            }else {

                if(yes = merchIDs.containsKey(args[0])) {
                    if(economy.getBalance(player) > Merchant_Cost) {
                        int MerchID = merchIDs.get(args[0]);
                        NPC npc = ((CitizensPlugin)CitizensAPI.getPlugin()).getNPCRegistry().getById(MerchID).clone();
                        npc.teleport(player.getLocation(), null);
                        economy.withdrawPlayer(player, 1000000);


                    }else {
                        sender.sendMessage(Not_Enough_Money);
                    }





                }else {
                    if(yes = npcIDs.containsKey(args[0])) {


                        sender.sendMessage("Sentinel's are disabled right now!! Check back later!");



                    }else {
                        if(args[0] == "help") {
                            //placeholder
                        }else {
                            sender.sendMessage(Not_A_NPC);
                        }
                    }
                }




            }
        }


        return false;}
}