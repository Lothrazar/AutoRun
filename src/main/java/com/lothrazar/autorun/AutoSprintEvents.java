package com.lothrazar.autorun;

import com.lothrazar.autorun.setup.ClientProxy;
import com.lothrazar.autorun.setup.ConfigAutoRun;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.boat.Boat;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public class AutoSprintEvents {

  @OnlyIn(Dist.CLIENT)
  @SubscribeEvent
  public void onKeyInputEventGeneric(InputEvent.Key event) {
    Player player = Minecraft.getInstance().player;
    boolean isCurrentlyAutorun = AutoSprintUtilInternal.getAutorunState(player);
    if (ClientProxy.KEY != null && ClientProxy.KEY.isDown()) {
      //wait, are we allowed to?
      if (!isCurrentlyAutorun && player.isFallFlying()) {
        if (ConfigAutoRun.ALLOW_ELYTRA.get() == false) {
          return;
        }
      }
      // toggle it to inverse
      AutoSprintUtilInternal.setAutorunState(player, !isCurrentlyAutorun);
    }
    else if (isCurrentlyAutorun) {
      if (AutoSprintUtilInternal.doesKeypressHaltSprint(player)) {
        // auto off
        AutoSprintUtilInternal.setAutorunState(player, false);
      }
    }
  }

  @OnlyIn(Dist.CLIENT)
  @SubscribeEvent
  public void onTick(PlayerTickEvent.Post event) {
    if (AutoSprintUtilInternal.getAutorunState(event.getEntity())) {
      move(event.getEntity());
    }
  }

  private void move(Player player) {
    float speed; // = ConfigAutoRun.SPD_WALKING.get().floatValue();
    if (player.isPassenger() && player.getVehicle() instanceof LivingEntity livin) {
      speed = ConfigAutoRun.SPD_MOUNTED.get().floatValue();
      AutoSprintUtilInternal.moveAlongVector(livin, AutoSprintUtilInternal.vector(player, speed));
    }
    else if (player.getVehicle() instanceof Boat ridin) {
      speed = ConfigAutoRun.SPD_BOATING.get().floatValue();
      AutoSprintUtilInternal.moveAlongVector(ridin, AutoSprintUtilInternal.vector(player, speed));
    }
    else if (player.onGround() == false && player.isCreative()) {
      speed = ConfigAutoRun.SPD_CREATIVE.get().floatValue();
      AutoSprintUtilInternal.moveAlongVector(player, AutoSprintUtilInternal.vector(player, speed));
    }
    else {
      speed = ConfigAutoRun.SPD_WALKING.get().floatValue();
      AutoSprintUtilInternal.moveAlongVector(player, AutoSprintUtilInternal.vector(player, speed));
    }
  }
  //
  //  @OnlyIn(Dist.CLIENT)
  //  @SubscribeEvent
  //  public void onKeyReleased(KeyReleased.Pre event) {}
  //
  //  @OnlyIn(Dist.CLIENT)
  //  @SubscribeEvent
  //  public void onKeyInput(ScreenEvent.KeyPressed.Pre event) {}
}
