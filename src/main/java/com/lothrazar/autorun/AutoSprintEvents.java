package com.lothrazar.autorun;

import com.lothrazar.autorun.setup.ClientProxy;
import com.lothrazar.autorun.setup.ConfigAutoRun;
import com.lothrazar.library.util.AutoSprintUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class AutoSprintEvents {

  @OnlyIn(Dist.CLIENT)
  @SubscribeEvent
  public void onKeyInputEventGeneric(InputEvent.Key event) {
    Player player = Minecraft.getInstance().player;
    boolean isCurrentlyAutorun = AutoSprintUtil.getAutorunState(player);
    if (ClientProxy.KEY != null && ClientProxy.KEY.isDown()) {
      //wait, are we allowed to?
      if (!isCurrentlyAutorun && player.isFallFlying()) {
        if (ConfigAutoRun.ALLOW_ELYTRA.get() == false) {
          return;
        }
      }
      // toggle it to inverse
      AutoSprintUtil.setAutorunState(player, !isCurrentlyAutorun);
    }
    else if (isCurrentlyAutorun) {
      if (AutoSprintUtil.doesKeypressHaltSprint(player)) {
        // auto off
        AutoSprintUtil.setAutorunState(player, false);
      }
    }
  }

  @OnlyIn(Dist.CLIENT)
  @SubscribeEvent
  public void onTick(PlayerTickEvent event) {
    if (AutoSprintUtil.getAutorunState(event.player)) {
      move(event.player);
    }
  }

  private void move(Player player) {
    float speed; // = ConfigAutoRun.SPD_WALKING.get().floatValue();
    if (player.isPassenger() && player.getVehicle() instanceof LivingEntity livin) {
      speed = ConfigAutoRun.SPD_MOUNTED.get().floatValue();
      AutoSprintUtil.moveAlongVector(livin, AutoSprintUtil.vector(player, speed));
    }
    else if (player.getVehicle() instanceof Boat ridin) {
      speed = ConfigAutoRun.SPD_BOATING.get().floatValue();
      AutoSprintUtil.moveAlongVector(ridin, AutoSprintUtil.vector(player, speed));
    }
    else if (player.isOnGround() == false && player.isCreative()) {
      speed = ConfigAutoRun.SPD_CREATIVE.get().floatValue();
      AutoSprintUtil.moveAlongVector(player, AutoSprintUtil.vector(player, speed));
    }
    else {
      speed = ConfigAutoRun.SPD_WALKING.get().floatValue();
      AutoSprintUtil.moveAlongVector(player, AutoSprintUtil.vector(player, speed));
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
