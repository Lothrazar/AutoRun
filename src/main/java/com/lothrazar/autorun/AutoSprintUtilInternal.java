package com.lothrazar.autorun;


import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.boat.Boat;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

// since FLIB is not client only
@OnlyIn(Dist.CLIENT)
public class AutoSprintUtilInternal {
  private static final String NBT = "isautorunning";


  public static Vec3 vector(Player p, float speed) {
    p.zza = speed;
    Vec3 vec = new Vec3((double)p.xxa, (double)p.yya, (double)p.zza);
    return vec;
  }

  public static void moveAlongVector(Boat p, Vec3 vec) {
    Level world = p.level();
    BlockPos blockpos = BlockPos.containing(p.getX(), p.getBoundingBox().minY - (double)1.0F, p.getZ());
    BlockState blockState = world.getBlockState(blockpos);
    float f5 = blockState.getFriction(world, blockpos, p);
    p.moveRelative(getRelevantMoveFactorBoat(p, f5), vec);
  }

  public static void moveAlongVector(LivingEntity p, Vec3 vec) {
    Level world = p.level();
    BlockPos blockpos = BlockPos.containing(p.getX(), p.getBoundingBox().minY - (double)1.0F, p.getZ());
    BlockState bs = world.getBlockState(blockpos);
    float f5 = bs.getFriction(world, blockpos, p);
    p.moveRelative(getRelevantMoveFactor(p, f5), vec);
  }

  public static float getRelevantMoveFactor(LivingEntity p, float flt) {
    if (p instanceof Player pl) {
      if (pl.isCreative()) {
        return p.getSpeed() * (0.21600002F / (flt * flt * flt));
      }
    }

    return p.onGround() ? p.getSpeed() * (0.21600002F / (flt * flt * flt)) : p.getSpeed() * 0.1F;
  }

  public static float getRelevantMoveFactorBoat(Boat p, float flt) {
    float aiMoveSpeedMock = 0.0383F;
    return 0.0383F * (0.21600002F / (flt * flt * flt));
  }

  public static void setAutorunState(Player player, boolean value) {
    player.getPersistentData().putBoolean("isautorunning", value);
    player.sendSystemMessage(Component.translatable("autorun." + value));
    player.setSprinting(value);
  }

  public static boolean getAutorunState(Player player) {
    return player != null && player.getPersistentData() != null ? player.getPersistentData().getBooleanOr("isautorunning", false) : false;
  }

  public static boolean doesKeypressHaltSprint(Player p) {
    if (p.getVehicle() instanceof Boat) {
      return Minecraft.getInstance().options.keyUp.isDown() || Minecraft.getInstance().options.keyDown.isDown();
    } else {
      return Minecraft.getInstance().options.keyUp.isDown() || Minecraft.getInstance().options.keyDown.isDown() || Minecraft.getInstance().options.keyLeft.isDown() || Minecraft.getInstance().options.keyRight.isDown();
    }
  }
}