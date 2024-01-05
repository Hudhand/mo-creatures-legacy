package drzhark.mocreatures.entity;

import java.util.List;

import drzhark.mocreatures.MoCreatures;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class MoCEntityItemPlaceable extends EntityLiving {
	
	public long punchCooldown;
	
	public MoCEntityItemPlaceable(World world) 
	{
		super(world);
	}
	
	 @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (MoCreatures.isServer()) {
           if (DamageSource.outOfWorld.equals(source))
           {
              this.setDead();
              return false;
           }
           
           else if (this.isEntityInvulnerable()) {return false;}
           
           else if (source.isExplosion())
           {
              this.dropItemEntity();
              this.setDead();
              return false;
           }
           else if (DamageSource.inFire.equals(source))
           {
        	   
              if (!this.isBurning()) {this.setFire(5);} 
              else {this.damageItemEntity(0.15F);}

              return false;
           }
           else if (DamageSource.onFire.equals(source) && this.getHealth() > 0.5F)
           {
              this.damageItemEntity(4.0F);
              return false;
           }
           else
           {
              boolean flag = "arrow".equals(source.getDamageType());
              boolean flag1 = "player".equals(source.getDamageType());
              if (!flag1 && !flag) {return false;}
              else
              {
            	  
                 if (source.getSourceOfDamage() instanceof EntityArrow)
                 {
                    source.getSourceOfDamage().setDead();
                 }

                 if (source.getEntity() instanceof EntityPlayer
                		 && !((EntityPlayer)source.getEntity()).capabilities.allowEdit)
                 {
                    return false;
                 }
                 else if (source.getEntity() instanceof EntityPlayer
                		 && ((EntityPlayer)source.getEntity()).capabilities.isCreativeMode)
                 {
                    this.setDead();
                    return false;
                 }
                 else
                 {
                    long i = this.worldObj.getTotalWorldTime();
                    if (i - punchCooldown > 5L && !flag)
                    {
                       punchCooldown = i;
                    }
                    else
                    {
                       this.dropItemEntity();
                       this.setDead();
                    }
                    return false;
                 }
              }
           }
        }
        else {return false;}
     }
	
	 
	 private void damageItemEntity(float decrease_health_amount) {
	        float item_entity_health = this.getHealth();
	        item_entity_health -= decrease_health_amount;
	        if (item_entity_health <= 0.5F) {
	           this.setDead();
	        } else {
	           this.setHealth(item_entity_health);
	        }

	     }
	     
	 
	public void dropItemEntity() {
		if (MoCreatures.isServer())
	    {
			this.entityDropItem(new ItemStack(Items.stick), 0F);   // <--- Default drop as placeholder, this is ment to be overridden by the child classes
	    }
	}

	public EntityItem getClosestEntityItem(Entity entity, double d)
    {
        double d1 = -1D;
        EntityItem entityitem = null;
        List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.expand(d, d, d));
        for (int k = 0; k < list.size(); k++)
        {
            Entity entity1 = (Entity) list.get(k);
            if (!(entity1 instanceof EntityItem))
            {
                continue;
            }
            EntityItem entityitem1 = (EntityItem) entity1;
            double d2 = entityitem1.getDistanceSq(entity.posX, entity.posY, entity.posZ);
            if (((d < 0.0D) || (d2 < (d * d))) && ((d1 == -1D) || (d2 < d1)))
            {
                d1 = d2;
                entityitem = entityitem1;
            }
        }

        return entityitem;
    }
}
