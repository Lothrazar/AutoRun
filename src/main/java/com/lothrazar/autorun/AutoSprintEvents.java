package com.lothrazar.autorun;

import com.lothrazar.autorun.setup.ClientProxy;
import com.lothrazar.autorun.setup.ConfigRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class AutoSprintEvents {

  @OnlyIn(Dist.CLIENT)
  @SubscribeEvent
  public void onTick(PlayerTickEvent event) {
    if (AutoSprintUtil.getAutorunState(event.player)) {
      AutoSprintUtil.move(event.player);
    }
  }

  @OnlyIn(Dist.CLIENT)
  @SubscribeEvent
  public void onKeyInput(InputEvent.KeyInputEvent event) {
    PlayerEntity player = Minecraft.getInstance().player;
    boolean isCurrentlyAutorun = AutoSprintUtil.getAutorunState(player);
    if (ClientProxy.key != null && ClientProxy.key.isPressed()) {
      //wait, are we allowed to?
      if (!isCurrentlyAutorun && player.isElytraFlying()) {
        if (ConfigRegistry.ALLOW_ELYTRA.get() == false) {
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
}
