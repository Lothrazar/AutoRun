package com.lothrazar.autorun;

import com.lothrazar.autorun.setup.ClientProxy;
import com.lothrazar.autorun.setup.IProxy;
import com.lothrazar.autorun.setup.ServerProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("autorun")
public class AutoSprintMod {

  public static final IProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new ServerProxy());

  public AutoSprintMod() {
    // Register the setup method for modloading
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    MinecraftForge.EVENT_BUS.register(this);
  }

  private void setup(final FMLCommonSetupEvent event) {
    proxy.init();
  }

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
    PlayerEntity player = proxy.getClientPlayer();
    boolean isCurrentlyAutorun = AutoSprintUtil.getAutorunState(player);
    if (ClientProxy.key != null && ClientProxy.key.isPressed()) {
      //toggle it to inverse
      AutoSprintUtil.setAutorunState(player, !isCurrentlyAutorun);
    }
    else if (isCurrentlyAutorun) {
      if (doesKeypressHaltSprint(player)) {
        //auto off
        AutoSprintUtil.setAutorunState(player, false);
      }
    }
  }

  private boolean doesKeypressHaltSprint(PlayerEntity p) {
    if (p.getRidingEntity() instanceof BoatEntity) {
      //boats can still turn left/right 
      return Minecraft.getInstance().gameSettings.keyBindForward.isKeyDown() ||
          Minecraft.getInstance().gameSettings.keyBindBack.isKeyDown();
    }
    return Minecraft.getInstance().gameSettings.keyBindForward.isKeyDown() ||
        Minecraft.getInstance().gameSettings.keyBindBack.isKeyDown() ||
        Minecraft.getInstance().gameSettings.keyBindLeft.isKeyDown() ||
        Minecraft.getInstance().gameSettings.keyBindRight.isKeyDown();
  }
}
