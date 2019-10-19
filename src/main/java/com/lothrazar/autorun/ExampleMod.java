package com.lothrazar.autorun;

import com.lothrazar.autorun.setup.ClientProxy;
import com.lothrazar.autorun.setup.IProxy;
import com.lothrazar.autorun.setup.ServerProxy;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TranslationTextComponent;
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
public class ExampleMod {

  private static final String NBT = "isautorunning";
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
      p.moveForward = 0.7F;
      p.travel(new Vec3d(p.moveStrafing, p.moveVertical, p.moveForward));
    }
  }

  @OnlyIn(Dist.CLIENT)
  @SubscribeEvent
  public void onKeyInput(InputEvent.KeyInputEvent event) {
    if (ClientProxy.key != null && ClientProxy.key.isPressed()) {
      PlayerEntity player = proxy.getClientPlayer();
      boolean value = !player.getPersistentData().getBoolean(NBT);
      player.getPersistentData().putBoolean(NBT, value);
      //      String thechar = ClientProxy.key.getKey().getTranslationKey().replace("key.keyboard.", "");
      player.sendStatusMessage(new TranslationTextComponent("autorun." + value), true);
    }
  }
}
