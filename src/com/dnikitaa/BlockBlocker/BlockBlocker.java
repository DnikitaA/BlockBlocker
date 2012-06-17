package com.dnikitaa.BlockBlocker;

import java.io.File;
import java.util.*;
import java.lang.Integer;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class BlockBlocker extends JavaPlugin
{
	class PlayerPlaceListener implements Listener
	{
	    final BlockBlocker blocker;
	    
	    public PlayerPlaceListener()
	    {
	        blocker = BlockBlocker.this;
	    }
	    
	    @EventHandler(priority=org.bukkit.event.EventPriority.NORMAL)
		
	    public void onPlayerInteract(PlayerInteractEvent event)
	    {
	        Action action = event.getAction();
	        if(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)
	        {
	            Player player = event.getPlayer();
	            ItemStack item = player.getItemInHand();
	            if(!isAllowed(item))
	            {
	                event.setCancelled(true);
	                if(action == Action.RIGHT_CLICK_BLOCK)
	                    player.sendMessage((new StringBuilder()).append(ChatColor.RED).append("You can't place that block type").toString());
	            }
	        }
	    }

	}
	
	private final PlayerPlaceListener PlayerPlaceReg = new PlayerPlaceListener();
	private static String DIR = "plugins/BlockBlocker/";
	private static List<String> BlockList = new ArrayList<String>();
	
	public BlockBlocker(){}
	
	public void onDisable()
	{
		BlockList.clear();
		System.out.println((new StringBuilder()).append(this).append(" disable").toString());
	}

    public void onEnable()
    {
    	File data = getDataFolder();
    	DIR = (new StringBuilder(String.valueOf(data.getPath()))).append(File.separator).toString();
    	System.out.println((new StringBuilder()).append(this).append(" enable").toString());
    	getServer().getPluginManager().registerEvents(PlayerPlaceReg, this);
    	readConfigFiles();
    }
    
    public void readConfigFiles()
    {
    	File normal = new File((new StringBuilder(String.valueOf(DIR))).append("blockblocked.txt").toString());
    	if(!normal.exists())
    	{
    		try
           	{
    			normal.createNewFile();
           	}
           	catch(Exception e)
           	{
           		e.printStackTrace();
           	}
    	}
    	BlockList.clear();
    	try
    	{
    		for(Scanner scan = new Scanner(normal); scan.hasNextLine();)
    			BlockList.add(scan.next());    		
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }
    
    public boolean isAllowed(ItemStack item)
    {
    	int Id,SubId;
    	int sep;
    	for(int i=0;i<BlockList.size();i++)
    	{
    		if((sep=BlockList.get(i).indexOf(':'))!=-1)
    		{
    			Id=Integer.parseInt(BlockList.get(i).substring(0, sep));
    			SubId=Integer.parseInt(BlockList.get(i).substring(sep+1,BlockList.get(i).length()));
    			if(item.getTypeId()==Id&&item.getDurability()==SubId)
    				return false;
    		}
    		else
    		{
    			Id=Integer.parseInt(BlockList.get(i));
    			if(item.getTypeId()==Id)
    				return false;
    		}
    	}
    	return true;
    }
}
