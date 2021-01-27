package com.lothrazar.autorun.setup;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.glfw.GLFW;

public class ClientProxy {

  public static KeyBinding key;

  public static void registerKeys() {
    key = new KeyBinding("key.run", GLFW.GLFW_KEY_H, "key.categories.movement");
    ClientRegistry.registerKeyBinding(key);
  }
}
