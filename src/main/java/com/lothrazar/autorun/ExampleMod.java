package com.lothrazar.autorun;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.lothrazar.autorun.setup.ClientProxy;
import com.lothrazar.autorun.setup.IProxy;
import com.lothrazar.autorun.setup.ServerProxy;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
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
public class ExampleMod {

  private static final String NBT = "isautorunning";
  // Directly reference a log4j logger.
  private static final Logger LOGGER = LogManager.getLogger();
  public static final IProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new ServerProxy());

  public ExampleMod() {
    // Register the setup method for modloading
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    MinecraftForge.EVENT_BUS.register(this);
  }

  private void setup(final FMLCommonSetupEvent event) {
    proxy.init();
  }

  @SubscribeEvent
  public void onTick(PlayerTickEvent event) {
    PlayerEntity p = event.player;
    if (p.getPersistentData().getBoolean(NBT)) {
      System.out.println("!autoru  n");
      p.moveForward = 0.1F;
      p.travel(new Vec3d(p.moveStrafing, p.moveVertical, p.moveForward));
    }
  }

  @SubscribeEvent
  public void onKeyInput(InputEvent.KeyInputEvent event) {
    if (ClientProxy.key != null && ClientProxy.key.isPressed()) {
      boolean old = proxy.getClientPlayer().getPersistentData().getBoolean(NBT);
      System.out.println("old" + old);
      proxy.getClientPlayer().getPersistentData().putBoolean(NBT, !old);
    }
  }
}
