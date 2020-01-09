package com.lothrazar.autorun;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class AutoSprintUtil {

  private static final String NBT = "isautorunning";

  /**
   * Do the auto sprint thing
   * 
   * @param p
   */
  public static void move(EntityPlayer p) {
    if (p.isRiding() && p.getRidingEntity() instanceof EntityLivingBase) {
      //so ride mount
      EntityLivingBase ridin = (EntityLivingBase) p.getRidingEntity();
      p.moveForward = 0.45F;
      Vec3d vec = new Vec3d(p.moveStrafing, p.moveVertical, p.moveForward);
      actuallyMove(ridin, vec);
    }
    else if (p.getRidingEntity() instanceof EntityBoat) {
      //sailing
      EntityBoat ridin = (EntityBoat) p.getRidingEntity();
      p.moveForward = 0.915F;
      Vec3d vec = new Vec3d(p.moveStrafing, p.moveVertical, p.moveForward);
      actuallyMove(ridin, vec);
    }
    else if (p.onGround == false && p.isCreative()) {
      //creative flight
      p.moveForward = 0.995F;
      Vec3d vec = new Vec3d(p.moveStrafing, p.moveVertical, p.moveForward);
      actuallyMove(p, vec);
    }
    else {
      //default speed walking
      p.moveForward = 0.85F;
      Vec3d vec = new Vec3d(p.moveStrafing, p.moveVertical, p.moveForward);
      actuallyMove(p, vec);
    }
  }

  private static void actuallyMove(EntityBoat p, Vec3d vec) {
    World world = p.world;
    BlockPos pos = new BlockPos(p.posX, p.getEntityBoundingBox().minY - 1.0D, p.posZ);
    IBlockState state = p.world.getBlockState(pos);
    float f5 = state.getBlock().getSlipperiness(state, world, pos, p);
    //    p.move(type, x, y, z);
    //    p.moveRelative(strafe, up, forward, friction);
    //    p.moveRelative(AutoSprintUtil.func_213335_r(p, f5), vec);
    p.motionX *= f5;
    p.motionZ *= f5;
  }

  public static float func_213335_r(EntityBoat p, float flt) {
    return (0.0113F) * (0.21600002F / (flt * flt * flt));
  }

  private static void actuallyMove(EntityLivingBase p, Vec3d vec) {
    World world = p.world;
    BlockPos pos = new BlockPos(p.posX, p.getRenderBoundingBox().minY - 1.0D, p.posZ);
    IBlockState state = p.world.getBlockState(pos);
    float f5 = state.getBlock().getSlipperiness(state, world, pos, p);
    p.moveStrafing = 0;//*= 0.98F;
    p.moveForward *= AutoSprintUtil.func_213335_r(p, f5 / 2);//0.98F;
    p.randomYawVelocity *= 0.9F;
    p.setSprinting(true);
    
 
    p.travel(p.moveStrafing, p.moveVertical, p.moveForward);
 
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
    player.getEntityData().setBoolean(NBT, value);
    String s = value ? "Auto-run enabled via keybinding" : "Auto-run disabled";//I18n.translateToLocal("autorun." + value);
    player.sendStatusMessage(new TextComponentTranslation(s), true);
  }

  public static boolean getAutorunState(EntityPlayer player) {
    if (player == null || player.getEntityData() == null) {
      return false;
    }
    return player.getEntityData().getBoolean(NBT);
  }
}
