package drzhark.mocreatures.item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import drzhark.mocreatures.MoCTools;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.achievements.MoCAchievements;
import drzhark.mocreatures.entity.MoCEntityAnimal;
import drzhark.mocreatures.entity.animal.MoCEntityBigCat;
import drzhark.mocreatures.entity.animal.MoCEntityElephant;
import drzhark.mocreatures.entity.animal.MoCEntityHorse;
import drzhark.mocreatures.entity.animal.MoCEntityKitty;
import drzhark.mocreatures.entity.animal.MoCEntityOstrich;
import drzhark.mocreatures.entity.animal.MoCEntityWyvern;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class MoCItemWhip extends MoCItem {
	
	int bigCatWhipCounter;
	
    public MoCItemWhip(String name)
    {
        super(name);
        maxStackSize = 1;
        setMaxDamage(24);
    }

    @Override
    public boolean isFull3D()
    {
        return true;
    }

    public ItemStack onItemRightClick2(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        return itemstack;
    }

    @Override
    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l, float f1, float f2, float f3)
    {
        Block block = world.getBlock(i, j, k);
        Block block1 = world.getBlock(i, j + 1, k);
        if ((l != 0) && (block != null) && (block1 == Blocks.air) && (block != Blocks.air)&& (block != Blocks.standing_sign))
        {
            this.whipFX(world, i, j, k);
            world.playSoundAtEntity(entityplayer, "mocreatures:whip", 0.5F, 0.4F / ((itemRand.nextFloat() * 0.4F) + 0.8F));
            itemstack.damageItem(1, entityplayer);
            List list_of_entities_near_player = world.getEntitiesWithinAABBExcludingEntity(entityplayer, entityplayer.boundingBox.expand(5D, 5D, 5D));
            
            
            if (!(list_of_entities_near_player.isEmpty()))
            {
	            List<Entity> whippable_entity_list = new ArrayList<>();
	            
	            List<Double> whippable_entity_distance_to_player_list = new ArrayList<>();
	            
	            int iteration_length = list_of_entities_near_player.size();
	            
	            for (int index = 0; index < iteration_length; index++)
	            {
	                Entity entity_near_player = (Entity) list_of_entities_near_player.get(index);
	                
	                if (entity_near_player instanceof MoCEntityAnimal)
	                {
	                    MoCEntityAnimal animal = (MoCEntityAnimal) entity_near_player;
	                    if (MoCreatures.proxy.enableStrictOwnership && animal.getOwnerName() != null && !animal.getOwnerName().equals("") && !entityplayer.getCommandSenderName().equals(animal.getOwnerName()) && !MoCTools.isThisPlayerAnOP(entityplayer)) 
	                    { 
	                       continue;
	                    }
	                    
	                    else if ((entity_near_player instanceof MoCEntityBigCat)
	                    		|| (entity_near_player instanceof MoCEntityHorse)
	                    		|| (entity_near_player instanceof MoCEntityKitty)
	                    		|| (entity_near_player instanceof MoCEntityWyvern)
	                    		|| (entity_near_player instanceof MoCEntityOstrich)
	                    		|| (entity_near_player instanceof MoCEntityElephant))
	                    {
	                    	whippable_entity_list.add(entity_near_player);
	                    	whippable_entity_distance_to_player_list.add(entityplayer.getDistanceSq(entity_near_player.posX, entity_near_player.posY, entity_near_player.posZ));
	                    }
	                }
	                else {continue;}
	            }
	            if (!(whippable_entity_distance_to_player_list.isEmpty()) && !(whippable_entity_list.isEmpty())) 
	            {
	            	int entity_index_in_list = whippable_entity_distance_to_player_list.indexOf(Collections.min(whippable_entity_distance_to_player_list));
	            	
	            	
		            if ((entity_index_in_list >= 0))
		            {
		            
			            Entity closest_whippable_entity_to_player = whippable_entity_list.get(entity_index_in_list);    
			                
			            if (closest_whippable_entity_to_player != null)
			            {
			                
			                if (closest_whippable_entity_to_player instanceof MoCEntityBigCat)
			                {
			                    MoCEntityBigCat entitybigcat = (MoCEntityBigCat) closest_whippable_entity_to_player;
			                    if (entitybigcat.getIsTamed())
			                    {
			                        entitybigcat.setSitting(!entitybigcat.getIsSitting());
			                        bigCatWhipCounter++;
			                    }
			                    else if ((world.difficultySetting.getDifficultyId() > 0) && entitybigcat.getIsAdult())
			                    {
			                        entitybigcat.setTarget(entityplayer);
			                    }
			                }
			                if (bigCatWhipCounter > 6)
			                {
			                	entityplayer.addStat(MoCAchievements.indiana, 1);
			                	bigCatWhipCounter = 0;
			                }
			                if (closest_whippable_entity_to_player instanceof MoCEntityHorse)
			                {
			                    MoCEntityHorse entityhorse = (MoCEntityHorse) closest_whippable_entity_to_player;
			                    if (entityhorse.getIsTamed())
			                    {
			                        if (entityhorse.riddenByEntity == null)
			                        {
			                            entityhorse.setEating(!entityhorse.getEating());
			                        }
			                        else if (entityhorse.isNightmare())
			                        {
			                            entityhorse.setNightmareInt(250);
			                        }
			                        else if (entityhorse.sprintCounter == 0)
			                        {
			                            entityhorse.sprintCounter = 1;
			                            
			                            if (entityhorse.isUndead()) {world.playSoundAtEntity(closest_whippable_entity_to_player, "mocreatures:horsemadundead", 1.0F, 1.0F + 0.2F);}
			                            
			                            else if (entityhorse.isGhost()) {world.playSoundAtEntity(closest_whippable_entity_to_player, "mocreatures:horsemadghost", 1.0F, 1.0F + 0.2F);}
			                            
			                            else {world.playSoundAtEntity(closest_whippable_entity_to_player, "mocreatures:horsemad", 1.0F, 1.0F + 0.2F);}
			                            
			                        }
			                        //TODO reactivate the right one prior to release
			                    }
			                }
			                
			                if ((closest_whippable_entity_to_player instanceof MoCEntityKitty))
			                {
			                    MoCEntityKitty entitykitty = (MoCEntityKitty) closest_whippable_entity_to_player;
			                    if ((entitykitty.getKittyState() > 2) && entitykitty.whipeable())
			                    {
			                        entitykitty.setSitting(!entitykitty.getIsSitting());
			                    }
			                }
			                
			                if ((closest_whippable_entity_to_player instanceof MoCEntityWyvern))
			                {
			                    MoCEntityWyvern entitywyvern = (MoCEntityWyvern) closest_whippable_entity_to_player;
			                    if (entitywyvern.getIsTamed() && !entitywyvern.isOnAir())
			                    {
			                        entitywyvern.setSitting(!entitywyvern.getIsSitting());
			                    }
			                }
			                
			                if (closest_whippable_entity_to_player instanceof MoCEntityOstrich)
			                {
			                    MoCEntityOstrich entityostrich = (MoCEntityOstrich) closest_whippable_entity_to_player;
			
			                    //makes ridden ostrich sprint
			                    if (entityostrich.riddenByEntity != null && entityostrich.sprintCounter == 0)
			                    {
			                        entityostrich.sprintCounter = 1;
			                        world.playSoundAtEntity(closest_whippable_entity_to_player, "mocreatures:ostrichhurt", 1.0F, 1.0F + 0.2F);
			                    }
			
			                    //toggles hiding of tamed ostriches
			                    if (entityostrich.getIsTamed() && entityostrich.riddenByEntity == null)
			                    {
			                        entityostrich.setHiding(!entityostrich.getHiding());
			                    }
			                }
			                if (closest_whippable_entity_to_player instanceof MoCEntityElephant)
			                {
			                    MoCEntityElephant entityelephant = (MoCEntityElephant) closest_whippable_entity_to_player;
			
			                    //makes ridden elephants charge
			                    if (entityelephant.riddenByEntity != null && entityelephant.sprintCounter == 0)
			                    {
			                        entityelephant.sprintCounter = 1;
			                        world.playSoundAtEntity(closest_whippable_entity_to_player, "mocreatures:elephantgrunt", 1.0F, 1.0F + 0.2F);
			                    }
			                }
			            }  
			        }
	            }
            }
        }
        return true; //always true to play the item swing on use animation
    }

    public void whipFX(World world, int i, int j, int k)
    {
        double d = i + 0.5F;
        double d1 = j + 1.0F;
        double d2 = k + 0.5F;
        double d3 = 0.2199999988079071D;
        double d4 = 0.27000001072883606D;
        world.spawnParticle("smoke", d - d4, d1 + d3, d2, 0.0D, 0.0D, 0.0D);
        world.spawnParticle("flame", d - d4, d1 + d3, d2, 0.0D, 0.0D, 0.0D);
        world.spawnParticle("smoke", d + d4, d1 + d3, d2, 0.0D, 0.0D, 0.0D);
        world.spawnParticle("flame", d + d4, d1 + d3, d2, 0.0D, 0.0D, 0.0D);
        world.spawnParticle("smoke", d, d1 + d3, d2 - d4, 0.0D, 0.0D, 0.0D);
        world.spawnParticle("flame", d, d1 + d3, d2 - d4, 0.0D, 0.0D, 0.0D);
        world.spawnParticle("smoke", d, d1 + d3, d2 + d4, 0.0D, 0.0D, 0.0D);
        world.spawnParticle("flame", d, d1 + d3, d2 + d4, 0.0D, 0.0D, 0.0D);
        world.spawnParticle("smoke", d, d1, d2, 0.0D, 0.0D, 0.0D);
        world.spawnParticle("flame", d, d1, d2, 0.0D, 0.0D, 0.0D);
    }
}