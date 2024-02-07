package drzhark.mocreatures.entity.animal;

import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import drzhark.mocreatures.MoCTools;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.entity.MoCEntityTameableAnimal;
import drzhark.mocreatures.entity.item.MoCEntityEgg;
import drzhark.mocreatures.inventory.MoCAnimalChest;
import drzhark.mocreatures.network.MoCMessageHandler;
import drzhark.mocreatures.network.message.MoCMessageAnimation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class MoCEntityWyvern extends MoCEntityTameableAnimal {

    public MoCAnimalChest localchest;
    public ItemStack localItemstack;
    public int mouthCounter;
    public int wingFlapCounter;
    public int diveCounter;
    public static final String wyvernNames[] = { "Jungle", "Swamp", "Savanna", "Sand", "Mother", "Undead", "Light", "Dark", "Arctic", "Cave", "Mountain", "Sea" };

    public MoCEntityWyvern(World world)
    {
        super(world);
        setSize(1.9F, 1.7F);
        setAdult(false);
        this.stepHeight = 1.0F;

        if(rand.nextInt(6) == 0)
        {
            setMoCAge(50 + rand.nextInt(50));
        }
        else
        {
            setMoCAge(80 + rand.nextInt(20));
        }
    }
    
    @Override
    public boolean isPredator()
    {
    	return true;
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
        dataWatcher.addObject(22, Byte.valueOf((byte) 0)); // isRideable - 0 false 1 true
        dataWatcher.addObject(23, Byte.valueOf((byte) 0)); // isChested - 0 false 1 true
        dataWatcher.addObject(24, Byte.valueOf((byte) 0)); // armor 0 by default, 1 metal, 2 gold, 3 diamond, 4 crystaline
        dataWatcher.addObject(25, Byte.valueOf((byte) 0)); // isFlying 0 false 1 true
        dataWatcher.addObject(26, Byte.valueOf((byte) 0)); // isSitting - 0 false 1 true
    }
    
    public boolean getIsFlying()
    {
        return (dataWatcher.getWatchableObjectByte(25) == 1);
    }

    
    public void setIsFlying(boolean flag)
    {
        byte input = (byte) (flag ? 1 : 0);
        dataWatcher.updateObject(25, Byte.valueOf(input));
    }

    public byte getArmorType()
    {
        return dataWatcher.getWatchableObjectByte(24);
    }

    @Override
    public void setArmorType(byte i)
    {
        dataWatcher.updateObject(24, Byte.valueOf(i));
    }

    public boolean getIsRideable()
    {
        return (dataWatcher.getWatchableObjectByte(22) == 1);
    }

    public void setRideable(boolean flag)
    {
        byte input = (byte) (flag ? 1 : 0);
        dataWatcher.updateObject(22, Byte.valueOf(input));
    }

    public boolean getIsChested()
    {
        return (dataWatcher.getWatchableObjectByte(23) == 1);
    }

    public void setIsChested(boolean flag)
    {
        byte input = (byte) (flag ? 1 : 0);
        dataWatcher.updateObject(23, Byte.valueOf(input));
    }

    public boolean getIsSitting()
    {
        return (dataWatcher.getWatchableObjectByte(26) == 1);
    }

    public void setSitting(boolean flag)
    {
        byte input = (byte) (flag ? 1 : 0);
        dataWatcher.updateObject(26, Byte.valueOf(input));
    }

    @Override
    public void selectType()
    {
        if (getType() == 0)
        {
            int i = rand.nextInt(100);
            if (i <= 12)
            {
                setType(1);
            }
            else if (i <= 24)
            {
                setType(2);
            }
            else if (i <= 36)
            {
                setType(3);
            }
            else if (i <= 48)
            {
                setType(4);
            }
            else if (i <= 60)
            {
                setType(9);
            }
            else if (i <= 72)
            {
                setType(10);
            }
            else if (i <= 84)
            {
                setType(11);
            }
            else if (i <= 95)
            {
                setType(12);
            }
            else
            {
                setType(5);
            }
            
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(getType() >= 5 ? 80.0D : 40.0D);
            this.setHealth(getMaxHealth());
        }
    }

    @Override
    public boolean isNotScared()
    {
        return true;
    }

    @Override
    public ResourceLocation getTexture()
    {
        switch (getType())
        {
        case 1:
            return MoCreatures.proxy.getTexture("wyvernjungle.png");
        case 2:
            return MoCreatures.proxy.getTexture("wyvernmix.png");
        case 3:
            return MoCreatures.proxy.getTexture("wyvernsand.png");
        case 4:
            return MoCreatures.proxy.getTexture("wyvernsun.png");
        case 5:
            return MoCreatures.proxy.getTexture("wyvernmother.png");
        case 6:
            return MoCreatures.proxy.getTexture("wyvernundead.png");
        case 7:
            return MoCreatures.proxy.getTexture("wyvernlight.png");
        case 8:
            return MoCreatures.proxy.getTexture("wyverndark.png");
        case 9:
            return MoCreatures.proxy.getTexture("wyvernarctic.png");
        case 10:
            return MoCreatures.proxy.getTexture("wyverncave.png");
        case 11:
            return MoCreatures.proxy.getTexture("wyvernmountain.png");
        case 12:
            return MoCreatures.proxy.getTexture("wyvernsea.png");
        default:
            return MoCreatures.proxy.getTexture("wyvernsun.png");
        }
    }

    @Override
    public void onLivingUpdate()
    {
        /**
         * slow falling
         */
        if (!getIsFlying() && isOnAir() && (motionY < 0.0D))
        {
            motionY *= 0.6D;
        }

        if (mouthCounter > 0 && ++mouthCounter > 30)
        {
            mouthCounter = 0;
        }

        if (diveCounter > 0 && ++diveCounter > 5)
        {
            diveCounter = 0;
        }

        if ((this.jumpPending))
        {
            if (wingFlapCounter == 0)
            {
                MoCTools.playCustomSound(this, "wingflap", worldObj);
            }
            wingFlapCounter = 1;
        }

        if (isOnAir() && rand.nextInt(30) == 0)
        {
            wingFlapCounter = 1;
        }

        if (wingFlapCounter > 0 && ++wingFlapCounter > 20)
        {
            wingFlapCounter = 0;
        }

        if (isFlyingAlone())
        {
            wingFlapCounter = 1;
        }

        if (MoCreatures.isServer())
        {
            if (!getIsAdult() && (rand.nextInt(500) == 0))
            {
                setMoCAge(getMoCAge() + 1);
                if (getMoCAge() >= getMaxAge())
                {
                    setAdult(true);
                }
            }

            if (isFlyingAlone() &&  rand.nextInt(60) == 0 && !isMovementCeased())
            {
                wingFlap();
            }

            if (isFlyingAlone() && !hasPath() && !isMovementCeased() && entityToAttack == null && rand.nextInt(20)==0)
            {
                updateWanderPath();
            }

            if (riddenByEntity != null)
            {
                setIsFlying(false);
            }
            else if (entityToAttack != null && rand.nextInt(20)==0)
            {
                setIsFlying(true);
            }
            else if (!getIsTamed() && rand.nextInt(300)==0)
            {
                setIsFlying(!getIsFlying());
            }

            if (!getIsTamed() && this.dimension == MoCreatures.wyvernLairDimensionID && (rand.nextInt(50) == 0) && this.posY < 10D)
            {
                this.setDead();
            }
        }

        if (motionY > 0.5) // prevent large boundingbox checks
        {
            motionY = 0.5;
        }
        super.onLivingUpdate();
    }

    //using it?
    public void wingFlap()
    {
        if (wingFlapCounter == 0)
        {
            MoCTools.playCustomSound(this, "wyvernwingflap", worldObj);
        }
        wingFlapCounter = 1;

        motionY = 0.5D;
    }

    @Override
    public float getSizeFactor() 
    {   
        return (float)getMoCAge() * 0.01F;
    }

    @Override
    public boolean isFlyingAlone()
    {
        return getIsFlying() && riddenByEntity == null;
    }

    @Override
    public int flyingHeight()
    {
        return 18;
    }

    @Override
    public boolean interact(EntityPlayer entityPlayer)
    {
        if (super.interact(entityPlayer)) {return false;}
        
        ItemStack itemstack = entityPlayer.inventory.getCurrentItem();
        
        if (getIsTamed() && itemstack != null)
        {
        	Item item = itemstack.getItem();
        	
        	if (!getIsRideable() && getMoCAge() > 90 && (item == Items.saddle || item == MoCreatures.craftedSaddle) )
            {
                if (--itemstack.stackSize == 0)
                {
                    entityPlayer.inventory.setInventorySlotContents(entityPlayer.inventory.currentItem, null);
                }
                setRideable(true);
                return true;
            }
        	
        	if (getMoCAge() > 90 && (
        			item == Items.iron_horse_armor
        			|| item == Items.golden_horse_armor
        			|| item == Items.diamond_horse_armor
        			))
            {
                if (getArmorType() == 0) {MoCTools.playCustomSound(this, "armorput", worldObj);}
                
                byte armorType = 0;
                
                if (item == Items.iron_horse_armor) {armorType = 1;}
                if (item == Items.golden_horse_armor) {armorType = 2;}
                if (item == Items.diamond_horse_armor) {armorType = 3;}
                
                dropArmor();
                

                setArmorType(armorType);
                if (--itemstack.stackSize == 0)
                {
                    entityPlayer.inventory.setInventorySlotContents(entityPlayer.inventory.currentItem, null);
                }

                return true;
            }

            if (getMoCAge() > 90 && !getIsChested() && (item == Item.getItemFromBlock(Blocks.chest)))
            {
                if (--itemstack.stackSize == 0)
                {
                    entityPlayer.inventory.setInventorySlotContents(entityPlayer.inventory.currentItem, null);
                }

                entityPlayer.inventory.addItemStackToInventory(new ItemStack(MoCreatures.key));
                setIsChested(true);
                worldObj.playSoundAtEntity(this, "mob.chicken.plop", 1.0F, ((rand.nextFloat() - rand.nextFloat()) * 0.2F) + 1.0F);
                return true;
            }

            if ((item == MoCreatures.key) && getIsChested())
            {
                // if first time opening horse chest, we must initialize it
                if (localchest == null)
                {
                    localchest = new MoCAnimalChest(StatCollector.translateToLocal("container.MoCreatures.WyvernChest"), 14);// 
                }
                // only open this chest on server side
                if (MoCreatures.isServer())
                {
                    entityPlayer.displayGUIChest(localchest);
                }
                return true;
            }

            if ((item == MoCreatures.essenceLight) && getMoCAge() > 90 && getType() < 5)
            {
                if (--itemstack.stackSize == 0)
                {
                    entityPlayer.inventory.setInventorySlotContents(entityPlayer.inventory.currentItem, new ItemStack(Items.glass_bottle));
                }
                else
                {
                    entityPlayer.inventory.addItemStackToInventory(new ItemStack(Items.glass_bottle));
                }

                if (MoCreatures.isServer())
                {
                    int i = getType() + 49;
                    MoCEntityEgg entityegg = new MoCEntityEgg(worldObj, i);
                    entityegg.setPosition(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ);
                    entityPlayer.worldObj.spawnEntityInWorld(entityegg);
                    entityegg.motionY += worldObj.rand.nextFloat() * 0.05F;
                    entityegg.motionX += (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.3F;
                    entityegg.motionZ += (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.3F;
                }
                return true;
            }

            if (getType() == 5 && (item == MoCreatures.essenceUndead))
            {
                if (--itemstack.stackSize == 0)
                {
                    entityPlayer.inventory.setInventorySlotContents(entityPlayer.inventory.currentItem, new ItemStack(Items.glass_bottle));
                }
                else
                {
                    entityPlayer.inventory.addItemStackToInventory(new ItemStack(Items.glass_bottle));
                }
                
                if (MoCreatures.isServer())
                {
                    setType(6);
                }
                return true;
            }

            if (getType() == 5 && (item == MoCreatures.essenceLight))
            {
                if (--itemstack.stackSize == 0)
                {
                    entityPlayer.inventory.setInventorySlotContents(entityPlayer.inventory.currentItem, new ItemStack(Items.glass_bottle));
                }
                else
                {
                    entityPlayer.inventory.addItemStackToInventory(new ItemStack(Items.glass_bottle));
                }
                
                if (MoCreatures.isServer())
                {
                    setType(7);
                }
                return true;
            }

            if (getType() == 5 && (item == MoCreatures.essenceDarkness))
            {
                if (--itemstack.stackSize == 0)
                {
                    entityPlayer.inventory.setInventorySlotContents(entityPlayer.inventory.currentItem, new ItemStack(Items.glass_bottle));
                }
                else
                {
                    entityPlayer.inventory.addItemStackToInventory(new ItemStack(Items.glass_bottle));
                }
                
                if (MoCreatures.isServer())
                {
                    setType(8);
                }
                return true;
            }
        }

        if ((itemstack == null) && getIsRideable() && getMoCAge() > 90 && (riddenByEntity == null))
        {
            if (MoCreatures.isServer())
            {
            	entityPlayer.rotationYaw = rotationYaw;
                entityPlayer.rotationPitch = rotationPitch;
            	
                entityPlayer.mountEntity(this);
                setSitting(false);
            }
            
            return true;
        }
        
        else
        {
            return false;
        }
    }

    /**
     * Drops the current armor
     */
    public void dropArmor()
    {
        if (MoCreatures.isServer())
        {
            int i = getArmorType();
            if (i != 0)
            {
                MoCTools.playCustomSound(this, "armoroff", worldObj);
            }

            if (i == 1)
            {
                EntityItem entityItem = new EntityItem(worldObj, this.posX, this.posY, this.posZ, new ItemStack(Items.iron_horse_armor, 1));
                entityItem.delayBeforeCanPickup = 10;
                worldObj.spawnEntityInWorld(entityItem);
            }
            if (i == 2)
            {
                EntityItem entityItem = new EntityItem(worldObj, this.posX, this.posY, this.posZ, new ItemStack(Items.golden_horse_armor, 1));
                entityItem.delayBeforeCanPickup = 10;
                worldObj.spawnEntityInWorld(entityItem);
            }
            if (i == 3)
            {
                EntityItem entityItem = new EntityItem(worldObj, this.posX, this.posY, this.posZ, new ItemStack(Items.diamond_horse_armor, 1));
                entityItem.delayBeforeCanPickup = 10;
                worldObj.spawnEntityInWorld(entityItem);
            }
            setArmorType((byte) 0);
        }
    }

    @Override
    public boolean rideableEntity()
    {
        return true;
    }

    @Override
    protected String getDeathSound()
    {
        return "mocreatures:wyverndying";
    }

    @Override
    protected String getHurtSound()
    {
        openMouth();
        return "mocreatures:wyvernhurt";
    }

    @Override
    protected String getLivingSound()
    {
        openMouth();
        return "mocreatures:wyverngrunt";
    }

    @Override
    public int getTalkInterval()
    {
        return 400;
    }

    @Override
    protected boolean isMovementCeased()
    {
        return (riddenByEntity != null) || getIsSitting();
    }

    @Override
    public boolean isFlyer()
    {
        return true;
    }

    @Override
    protected void fall(float f)
    {
    }

    @Override
    public double getMountedYOffset()
    {
        return (double)this.height * 0.90D * getSizeFactor();
    }

    @Override
    public void updateRiderPosition()
    {
        double dist = getSizeFactor() * (0.3D);
        double newPosX = posX - (dist * Math.cos((MoCTools.realAngle(renderYawOffset - 90F)) / 57.29578F));
        double newPosZ = posZ - (dist * Math.sin((MoCTools.realAngle(renderYawOffset - 90F)) / 57.29578F));
        riddenByEntity.setPosition(newPosX, posY + getMountedYOffset() + riddenByEntity.getYOffset(), newPosZ);
    }

    @Override
    protected void attackEntity(Entity entity, float distanceToEntity)
    {
        if (attackTime <= 0 && (distanceToEntity < 3.0D) && (entity.boundingBox.maxY > boundingBox.minY) && (entity.boundingBox.minY < boundingBox.maxY))
        {
            attackTime = 20;
            boolean poisonChance = (rand.nextInt(3) == 0);
            if (poisonChance)
            {
                if (entity instanceof EntityPlayer) 
                {
                    MoCreatures.poisonPlayer((EntityPlayer) entity);
                }
                ((EntityLivingBase) entity).addPotionEffect(new PotionEffect(Potion.poison.id, 200, 0));
                MoCTools.playCustomSound(this, "wyvernpoisoning", worldObj);
            }
            
            int wyvernAttackDamage = 5;
            
            if (getType() >= 5) {wyvernAttackDamage = 10;}
            
            entity.attackEntityFrom(DamageSource.causeMobDamage(this), wyvernAttackDamage);
            
            openMouth();
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float damageTaken)
    {
    	float vanillaHorseArmorProtection = 3.7F * getArmorType(); 
        
        damageTaken *= (1-(vanillaHorseArmorProtection * 0.04F)); //final damage taken after applying armor values. The function uses same damage reduction value as vanilla minecraft.
        
        if (damageTaken < 0F) {damageTaken = 0F;}
    	
        if (super.attackEntityFrom(damageSource, damageTaken))
        {
            Entity entityThatAttackedThisCreature = damageSource.getEntity();
         
            if (entityThatAttackedThisCreature != null && getIsTamed() && (entityThatAttackedThisCreature instanceof EntityPlayer && (entityThatAttackedThisCreature.getCommandSenderName().equals(getOwnerName()))))
            { 
            	return false; 
            }
            
            if ((riddenByEntity != null) && (entityThatAttackedThisCreature == riddenByEntity)) {return false;}
            
            if ((entityThatAttackedThisCreature != this) && (worldObj.difficultySetting.getDifficultyId() > 0))
            {
                entityToAttack = entityThatAttackedThisCreature;
            }
            
            return true;
        }
        return super.attackEntityFrom(damageSource, damageTaken);
    }

    @Override
    protected Entity findPlayerToAttack()
    {
        if (worldObj.difficultySetting.getDifficultyId() > 0 && !getIsTamed())
        {
            EntityPlayer entityPlayer = worldObj.getClosestVulnerablePlayerToEntity(this, 10D);
            if ((entityPlayer != null))
            {
                    return entityPlayer;
            }
            if ((rand.nextInt(500) == 0))
            {
                EntityLivingBase entityLiving = getClosestEntityLiving(this, 8D);
                return entityLiving;
            }
        }
        
        if (MoCreatures.proxy.specialPetsDefendOwner)
        {
	        if (this.getIsTamed() && this.riddenByEntity == null && !getIsSitting()) //defend owner if they are attacked by an entity
	    	{
	    		EntityPlayer ownerOfEntityThatIsOnline = MinecraftServer.getServer().getConfigurationManager().func_152612_a(this.getOwnerName());
	    		
	    		if (ownerOfEntityThatIsOnline != null)
	    		{
	    			EntityLivingBase entityThatAttackedOwner = ownerOfEntityThatIsOnline.getAITarget();
	    			
	    			if (entityThatAttackedOwner != null)
	    			{
	    				return entityThatAttackedOwner;
	    			}
	    		}
	    	}
        }
        return null;
    }

    @Override
    public boolean shouldEntityBeIgnored(Entity entity)
    {
        return (super.shouldEntityBeIgnored(entity) || (entity instanceof MoCEntityWyvern) || (entity instanceof EntityPlayer));
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbtTagCompound)
    {
        super.writeEntityToNBT(nbtTagCompound);
        nbtTagCompound.setBoolean("Saddle", getIsRideable());
        nbtTagCompound.setBoolean("Chested", getIsChested());
        nbtTagCompound.setByte("ArmorType", getArmorType());
        nbtTagCompound.setBoolean("isSitting", getIsSitting());
        if (getIsChested() && localchest != null)
        {
            NBTTagList nbttaglist = new NBTTagList();
            for (int i = 0; i < localchest.getSizeInventory(); i++)
            {
                localItemstack = localchest.getStackInSlot(i);
                if (localItemstack != null)
                {
                    NBTTagCompound nbtTagCompound1 = new NBTTagCompound();
                    nbtTagCompound1.setByte("Slot", (byte) i);
                    localItemstack.writeToNBT(nbtTagCompound1);
                    nbttaglist.appendTag(nbtTagCompound1);
                }
            }
            nbtTagCompound.setTag("Items", nbttaglist);
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbtTagCompound)
    {
        super.readEntityFromNBT(nbtTagCompound);
        setRideable(nbtTagCompound.getBoolean("Saddle"));
        setIsChested(nbtTagCompound.getBoolean("Chested"));
        setArmorType(nbtTagCompound.getByte("ArmorType"));
        setSitting(nbtTagCompound.getBoolean("isSitting"));
        if (getIsChested())
        {
            NBTTagList nbttaglist = nbtTagCompound.getTagList("Items", 10);
            localchest = new MoCAnimalChest(StatCollector.translateToLocal("container.MoCreatures.WyvernChest"), 14);
            for (int i = 0; i < nbttaglist.tagCount(); i++)
            {
                NBTTagCompound nbtTagCompound1 = (NBTTagCompound) nbttaglist.getCompoundTagAt(i);
                int j = nbtTagCompound1.getByte("Slot") & 0xff;
                if ((j >= 0) && j < localchest.getSizeInventory())
                {
                    localchest.setInventorySlotContents(j, ItemStack.loadItemStackFromNBT(nbtTagCompound1));
                }
            }
        }
    }

    @Override
    public double roperYOffset()
    {
        if (this.getIsAdult())
        {
            return 0D;
        }
        else
        {
            return (130 - getMoCAge()) * 0.01D;
        }
    }

    @Override
    public int nameYOffset()
    {
        if (getIsAdult())
        {
            return -120;
        }
        return -80;
    }

    @Override
    public boolean isMyHealFood(ItemStack itemstack)
    {
        return itemstack != null && 
        	(
        		itemstack.getItem() == MoCreatures.ratRaw
        		|| itemstack.getItem() == MoCreatures.turkeyRaw
        		|| (itemstack.getItem().itemRegistry).getNameForObject(itemstack.getItem()).equals("etfuturum:rabbit_raw")
        		|| (itemstack.getItem().itemRegistry).getNameForObject(itemstack.getItem()).equals("harvestcraft:rabbitrawItem")
        		|| MoCreatures.isGregTech6Loaded &&
        			(	
        				OreDictionary.getOreName(OreDictionary.getOreID(itemstack)) == "foodScrapmeat"
        			)
        	);
    }

    private void openMouth()
        {
             if (MoCreatures.isServer())
             {
                 mouthCounter = 1;
                 MoCMessageHandler.INSTANCE.sendToAllAround(new MoCMessageAnimation(this.getEntityId(), 1), new TargetPoint(this.worldObj.provider.dimensionId, this.posX, this.posY, this.posZ, 64));
             }
            
        }
     
    @Override
    public void performAnimation(int animationType)
    {
        if (animationType == 1) //opening mouth
        {
            mouthCounter = 1;
        }
        if (animationType == 2) //diving mount
        {
            diveCounter = 1;
        }
    }

    @Override
    public void makeEntityDive()
    {
        if (MoCreatures.isServer())
            MoCMessageHandler.INSTANCE.sendToAllAround(new MoCMessageAnimation(this.getEntityId(), 2), new TargetPoint(this.worldObj.provider.dimensionId, this.posX, this.posY, this.posZ, 64));
        super.makeEntityDive();
    }

    @Override
    protected void dropFewItems(boolean flag, int x)
    {
        int xCoordinate = MathHelper.floor_double(posX);
        int yCoordinate = MathHelper.floor_double(boundingBox.minY);
        int zCoordinate = MathHelper.floor_double(posZ);
        int chance = MoCreatures.proxy.wyvernEggDropChance;
        if (getType() == 5) //mother wyverns drop eggs more frequently
        {
            chance = MoCreatures.proxy.motherWyvernEggDropChance;
        }
        String biomeName = MoCTools.BiomeName(worldObj, xCoordinate, yCoordinate, zCoordinate);
        if (rand.nextInt(100) < chance)
        {
            entityDropItem(new ItemStack(MoCreatures.mocegg, 1, getType() + 49), 0.0F);
        }
    }

    @Override
    public boolean canBeCollidedWith()
    {
        return riddenByEntity == null;
    }

    @Override
    public void dropMyStuff() 
    {
        if (MoCreatures.isServer())
        {
            dropArmor();
            MoCTools.dropSaddle(this, worldObj);
            
            if (getIsChested())
            {
               MoCTools.dropInventory(this, localchest);
               MoCTools.dropCustomItem(this, this.worldObj, new ItemStack(Blocks.chest, 1));
               setIsChested(false);
            }
        }
    }

    @Override
    public float getAdjustedYOffset()
    {
         if (getIsSitting())
         {
             return 0.4F;
         }
        return 0F;
    }

    @Override
    public double getCustomSpeed() //controls flying speed only
    {
         if (riddenByEntity != null)
         {
             if (getType() < 5)
             {
                 return 1.4D;
             }
             return 1.44D;
         }
        return 0.8D;
    }

     private int getMaxAge()
     {
         if (getType() >= 5)
         {
             return 180;
         }
         return 100;
     }

    @Override
    public EnumCreatureAttribute getCreatureAttribute()
    {
        if (getType() == 6) 
        {
            return EnumCreatureAttribute.UNDEAD;
        }
        return super.getCreatureAttribute();
    }

    @Override
    public boolean isSwimmerEntity()
    {
        return true;
    }
}