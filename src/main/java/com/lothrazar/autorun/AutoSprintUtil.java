package com.lothrazar.autorun;

import com.lothrazar.autorun.setup.ConfigRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
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
      LivingEntity ridin = (LivingEntity) p.getRidingEntity();
      p.moveForward = ConfigRegistry.SPD_MOUNTED.get().floatValue();
      Vector3d vec = new Vector3d(p.moveStrafing, p.moveVertical, p.moveForward);
      actuallyMove(ridin, vec);
    }
    else if (p.getRidingEntity() instanceof BoatEntity) {
      // 
      BoatEntity ridin = (BoatEntity) p.getRidingEntity();
      p.moveForward = ConfigRegistry.SPD_BOATING.get().floatValue();
      Vector3d vec = new Vector3d(p.moveStrafing, p.moveVertical, p.moveForward);
      actuallyMove(ridin, vec);
    }
    else if (p.isOnGround() == false && p.isCreative()) {
      p.moveForward = ConfigRegistry.SPD_CREATIVE.get().floatValue();
      Vector3d vec = new Vector3d(p.moveStrafing, p.moveVertical, p.moveForward);
      actuallyMove(p, vec);
    }
    else {
      p.moveForward = ConfigRegistry.SPD_WALKING.get().floatValue();
      Vector3d vec = new Vector3d(p.moveStrafing, p.moveVertical, p.moveForward);
      actuallyMove(p, vec);
    }
  }

  private static void actuallyMove(BoatEntity p, Vector3d vec) {
    World world = p.world;
    BlockPos blockpos = new BlockPos(p.getPosX(), p.getBoundingBox().minY - 1.0D, p.getPosZ());
    float f5 = p.world.getBlockState(blockpos).getSlipperiness(world, blockpos, p);
    p.moveRelative(AutoSprintUtil.getRelevantMoveFactorBoat(p, f5), vec);
  }

  private static void actuallyMove(LivingEntity p, Vector3d vec) {
    World world = p.world;
    BlockPos blockpos = new BlockPos(p.getPosX(), p.getBoundingBox().minY - 1.0D, p.getPosZ());
    float f5 = p.world.getBlockState(blockpos).getSlipperiness(world, blockpos, p);
    p.moveRelative(AutoSprintUtil.getRelevantMoveFactor(p, f5), vec);
    if (p.getRidingEntity() == null && p instanceof PlayerEntity) {
      //HUNGER
    }
  }

  /**
   * LivingEntity.class::2091
   * 
   * @param p
   * @param flt
   * @return
   */
  public static float getRelevantMoveFactor(LivingEntity p, float flt) {
    if (p instanceof PlayerEntity) {
      PlayerEntity pl = (PlayerEntity) p;
      if (pl.isCreative()) {
        return p.getAIMoveSpeed() * (0.21600002F / (flt * flt * flt));
      }
    }
    return p.isOnGround()// onGround
        ? p.getAIMoveSpeed() * (0.21600002F / (flt * flt * flt))
        : p.jumpMovementFactor;
  }

  public static float getRelevantMoveFactorBoat(BoatEntity p, float flt) {
    // boat has no getAIMoveSpeed(), so we hardcode it
    float aiMoveSpeedMock = 0.0383F;
    return aiMoveSpeedMock * (0.21600002F / (flt * flt * flt));
  }

  public static void setAutorunState(PlayerEntity player, boolean value) {
    player.getPersistentData().putBoolean(NBT, value);
    player.sendStatusMessage(new TranslationTextComponent("autorun." + value), true);
    player.setSprinting(value);
  }

  public static boolean getAutorunState(PlayerEntity player) {
    if (player == null || player.getPersistentData() == null) {
      return false;
    }
    return player.getPersistentData().getBoolean(NBT);
  }
}
