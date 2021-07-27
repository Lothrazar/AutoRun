package com.lothrazar.autorun;

import com.lothrazar.autorun.setup.ConfigRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class AutoSprintUtil {

  private static final String NBT = "isautorunning";

  public static void move(Player p) {
    if (p.isPassenger() && p.getVehicle() instanceof LivingEntity) {
      LivingEntity ridin = (LivingEntity) p.getVehicle();
      p.zza = ConfigRegistry.SPD_MOUNTED.get().floatValue();
      Vec3 vec = new Vec3(p.xxa, p.yya, p.zza);
      actuallyMove(ridin, vec);
    }
    else if (p.getVehicle() instanceof Boat) {
      // 
      Boat ridin = (Boat) p.getVehicle();
      p.zza = ConfigRegistry.SPD_BOATING.get().floatValue();
      Vec3 vec = new Vec3(p.xxa, p.yya, p.zza);
      actuallyMove(ridin, vec);
    }
    else if (p.isOnGround() == false && p.isCreative()) {
      p.zza = ConfigRegistry.SPD_CREATIVE.get().floatValue();
      Vec3 vec = new Vec3(p.xxa, p.yya, p.zza);
      actuallyMove(p, vec);
    }
    else {
      p.zza = ConfigRegistry.SPD_WALKING.get().floatValue();
      Vec3 vec = new Vec3(p.xxa, p.yya, p.zza);
      actuallyMove(p, vec);
    }
  }

  private static void actuallyMove(Boat p, Vec3 vec) {
    Level world = p.level;
    BlockPos blockpos = new BlockPos(p.getX(), p.getBoundingBox().minY - 1.0D, p.getZ());
    BlockState blockState = p.level.getBlockState(blockpos);
    float f5 = blockState.getFriction(world, blockpos, p);
    p.moveRelative(AutoSprintUtil.getRelevantMoveFactorBoat(p, f5), vec);
  }

  private static void actuallyMove(LivingEntity p, Vec3 vec) {
    Level world = p.level;
    BlockPos blockpos = new BlockPos(p.getX(), p.getBoundingBox().minY - 1.0D, p.getZ());
    BlockState bs = p.level.getBlockState(blockpos);
    float f5 = bs.getFriction(world, blockpos, p);
    p.moveRelative(AutoSprintUtil.getRelevantMoveFactor(p, f5), vec);
  }

  public static float getRelevantMoveFactor(LivingEntity p, float flt) {
    if (p instanceof Player) {
      Player pl = (Player) p;
      if (pl.isCreative()) {
        return p.getSpeed() * (0.21600002F / (flt * flt * flt));
      }
    }
    return p.isOnGround()// onGround
        ? p.getSpeed() * (0.21600002F / (flt * flt * flt))
        : p.flyingSpeed;
  }

  public static float getRelevantMoveFactorBoat(Boat p, float flt) {
    // boat has no getAIMoveSpeed(), so we hardcode it
    final float aiMoveSpeedMock = 0.0383F;
    return aiMoveSpeedMock * (0.21600002F / (flt * flt * flt));
  }

  public static void setAutorunState(Player player, boolean value) {
    player.getPersistentData().putBoolean(NBT, value);
    player.displayClientMessage(new TranslatableComponent("autorun." + value), true);
    player.setSprinting(value);
  }

  public static boolean getAutorunState(Player player) {
    if (player == null || player.getPersistentData() == null) {
      return false;
    }
    return player.getPersistentData().getBoolean(NBT);
  }

  public static boolean doesKeypressHaltSprint(Player p) {
    if (p.getVehicle() instanceof Boat) {
      // boats can still turn left/right 
      return Minecraft.getInstance().options.keyUp.isDown() ||
          Minecraft.getInstance().options.keyDown.isDown();
    }
    return Minecraft.getInstance().options.keyUp.isDown() ||
        Minecraft.getInstance().options.keyDown.isDown() ||
        Minecraft.getInstance().options.keyLeft.isDown() ||
        Minecraft.getInstance().options.keyRight.isDown();
  }
}
