package com.lothrazar.autorun;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

@OnlyIn(Dist.CLIENT)
public class AutoSprintUtil {

  private static final String NBT = "isautorunning";

  /**
   * Do the auto sprint thing
   * 
   * @param p
   */
  public static void move(EntityPlayer p) {
    if (p.isPassenger() && p.getRidingEntity() instanceof EntityLivingBase) {
      //
      EntityLivingBase ridin = (EntityLivingBase) p.getRidingEntity();
      p.moveForward = 0.45F;
      Vec3d vec = new Vec3d(p.moveStrafing, p.moveVertical, p.moveForward);
      actuallyMove(ridin, vec);
    }
    else if (p.getRidingEntity() instanceof EntityBoat) {
      //
      EntityBoat ridin = (EntityBoat) p.getRidingEntity();
      p.moveForward = 0.915F;
      Vec3d vec = new Vec3d(p.moveStrafing, p.moveVertical, p.moveForward);
      actuallyMove(ridin, vec);
    }
    else if (p.onGround == false && p.isCreative()) {
      p.moveForward = 0.995F;
      Vec3d vec = new Vec3d(p.moveStrafing, p.moveVertical, p.moveForward);
      actuallyMove(p, vec);
    }
    else {
      p.moveForward = 0.85F;
      Vec3d vec = new Vec3d(p.moveStrafing, p.moveVertical, p.moveForward);
      actuallyMove(p, vec);
    }
  }

  private static void actuallyMove(EntityBoat p, Vec3d vec) {
    World world = p.world;
    BlockPos blockpos = new BlockPos(p.posX, p.getBoundingBox().minY - 1.0D, p.posZ);
    float f5 = p.world.getBlockState(blockpos).getSlipperiness(world, blockpos, p);
    p.moveRelative(AutoSprintUtil.func_213335_r(p, f5), vec);
  }

  public static float func_213335_r(EntityBoat p, float flt) {
    return (0.0113F) * (0.21600002F / (flt * flt * flt));
  }

  private static void actuallyMove(EntityLivingBase p, Vec3d vec) {
    World world = p.world;
    BlockPos blockpos = new BlockPos(p.posX, p.getBoundingBox().minY - 1.0D, p.posZ);
    float f5 = p.world.getBlockState(blockpos).getSlipperiness(world, blockpos, p);
    p.moveRelative(AutoSprintUtil.func_213335_r(p, f5), vec);
  }

  /**
   * EntityLivingBase.class::2091
   * 
   * @param p
   * @param flt
   * @return
   */
  public static float func_213335_r(EntityLivingBase p, float flt) {
    if (p instanceof EntityPlayer) {
      EntityPlayer pl = (EntityPlayer) p;
      if (pl.isCreative()) {
        return p.getAIMoveSpeed() * (0.21600002F / (flt * flt * flt));
      }
    }
    return p.onGround ? p.getAIMoveSpeed() * (0.21600002F / (flt * flt * flt)) : p.jumpMovementFactor;
  }

  public static void setAutorunState(EntityPlayer player, boolean value) {
    player.getPersistentData().putBoolean(NBT, value);
    player.sendStatusMessage(new TranslationTextComponent("autorun." + value), true);
  }

  public static boolean getAutorunState(EntityPlayer player) {
    if (player == null || player.getPersistentData() == null) {
      return false;
    }
    return player.getPersistentData().getBoolean(NBT);
  }
}
