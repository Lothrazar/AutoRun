package com.lothrazar.autorun;

import com.lothrazar.autorun.setup.ClientProxy;
import com.lothrazar.autorun.setup.ConfigRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.client.event.ScreenEvent.KeyReleased;
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
  public void onKeyInputEventGeneric(InputEvent.Key event) {
    Player player = Minecraft.getInstance().player;
    boolean isCurrentlyAutorun = AutoSprintUtil.getAutorunState(player);
    if (ClientProxy.key != null && ClientProxy.key.isDown()) {
      //wait, are we allowed to?
      if (!isCurrentlyAutorun && player.isFallFlying()) {
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

  @OnlyIn(Dist.CLIENT)
  @SubscribeEvent
  public void onKeyReleased(KeyReleased.Pre event) {}

  @OnlyIn(Dist.CLIENT)
  @SubscribeEvent
  public void onKeyInput(ScreenEvent.KeyPressed.Pre event) {}
}
