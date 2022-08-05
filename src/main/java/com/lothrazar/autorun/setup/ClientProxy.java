package com.lothrazar.autorun.setup;

import org.lwjgl.glfw.GLFW;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientProxy {

  public static final KeyMapping key = new KeyMapping("key.run", GLFW.GLFW_KEY_H, "key.categories.movement");

  @SubscribeEvent
  public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
    //    net.minecraftforge.client.ClientRegistry.registerKeyBinding(CAKE); 
    event.register(key);
  }

  public static void registerKeys() {}
}
