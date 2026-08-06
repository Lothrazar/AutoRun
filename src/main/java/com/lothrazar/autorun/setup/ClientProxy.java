package com.lothrazar.autorun.setup;

import com.lothrazar.autorun.AutoSprintMod;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;
import net.minecraft.client.KeyMapping;

@EventBusSubscriber(modid = AutoSprintMod.MODID, value = Dist.CLIENT)
public class ClientProxy {

  public static KeyMapping KEY;

  @SubscribeEvent
  public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
    //    net.minecraftforge.client.ClientRegistry.registerKeyBinding(CAKE);
    event.register(KEY = new KeyMapping("key.run", GLFW.GLFW_KEY_H, KeyMapping.Category.MOVEMENT));
  }
}
