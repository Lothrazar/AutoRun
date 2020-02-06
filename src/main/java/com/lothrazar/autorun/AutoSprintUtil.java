package com.lothrazar.autorun;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class AutoSprintUtil {

  /**
   * Mappings incomplete:
   * 
   * func_226277_ct_ == getPosX() func_226281_cx_ == getPosZ()
   * 
   * 
   */
  private static final String NBT = "isautorunning";

  /**
   * Do the auto sprint thing
   * 
   * @param p
   */
  public static void move(PlayerEntity p) {
    if (p.isPassenger() && p.getRidingEntity() instanceof LivingEntity) {
      //
      LivingEntity ridin = (LivingEntity) p.getRidingEntity();
      p.moveForward = 0.45F;
      Vec3d vec = new Vec3d(p.moveStrafing, p.moveVertical, p.moveForward);
      actuallyMove(ridin, vec);
    }
    else if (p.getRidingEntity() instanceof BoatEntity) {
      //
      BoatEntity ridin = (BoatEntity) p.getRidingEntity();
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

  private static void actuallyMove(BoatEntity p, Vec3d vec) {
    World world = p.world;
    BlockPos blockpos = new BlockPos(p.getPosX(), p.getBoundingBox().minY - 1.0D, p.getPosZ());
    float f5 = p.world.getBlockState(blockpos).getSlipperiness(world, blockpos, p);
    p.moveRelative(AutoSprintUtil.func_213335_r(p, f5), vec);
  }

  public static float func_213335_r(BoatEntity p, float flt) {
    return (0.0113F) * (0.21600002F / (flt * flt * flt));
  }

  private static void actuallyMove(LivingEntity p, Vec3d vec) {
    World world = p.world;
    BlockPos blockpos = new BlockPos(p.getPosX(), p.getBoundingBox().minY - 1.0D, p.getPosZ());
    float f5 = p.world.getBlockState(blockpos).getSlipperiness(world, blockpos, p);
    p.moveRelative(AutoSprintUtil.func_213335_r(p, f5), vec);
  }

  /**
   * LivingEntity.class::2091
   * 
   * @param p
   * @param flt
   * @return
   */
  public static float func_213335_r(LivingEntity p, float flt) {
    if (p instanceof PlayerEntity) {
      PlayerEntity pl = (PlayerEntity) p;
      if (pl.isCreative()) {
        return p.getAIMoveSpeed() * (0.21600002F / (flt * flt * flt));
      }
    }
    return p.onGround ? p.getAIMoveSpeed() * (0.21600002F / (flt * flt * flt)) : p.jumpMovementFactor;
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
