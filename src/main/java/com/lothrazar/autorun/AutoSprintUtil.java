package com.lothrazar.autorun;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class AutoSprintUtil {

  private static final String NBT = "isautorunning";

  /**
   * Do the auto sprint thing
   * 
   * @param p
   */
  public static void move(PlayerEntity p) {
    p.moveForward = 0.85F;
    Vec3d p_213352_1_ = new Vec3d(p.moveStrafing, p.moveVertical, p.moveForward);
    World world = p.world;
    BlockPos blockpos = new BlockPos(p.posX, p.getBoundingBox().minY - 1.0D, p.posZ);
    float f5 = p.world.getBlockState(blockpos).getSlipperiness(world, blockpos, p);
    p.moveRelative(AutoSprintUtil.func_213335_r(p, f5), p_213352_1_);
  }

  /**
   * LivingEntity.class::2091
   * 
   * @param p
   * @param p_213335_1_
   * @return
   */
  public static float func_213335_r(PlayerEntity p, float p_213335_1_) {
    return p.onGround ? p.getAIMoveSpeed() * (0.21600002F / (p_213335_1_ * p_213335_1_ * p_213335_1_)) : p.jumpMovementFactor;
  }

  public static void setAutorunState(PlayerEntity player, boolean value) {
    player.getPersistentData().putBoolean(NBT, value);
    player.sendStatusMessage(new TranslationTextComponent("autorun." + value), true);
  }

  public static boolean getAutorunState(PlayerEntity player) {
    if (player == null || player.getPersistentData() == null) {
      return false;
    }
    return player.getPersistentData().getBoolean(NBT);
  }
}
