package com.lothrazar.autorun.setup;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.ClientRegistry;
import org.lwjgl.glfw.GLFW;

public class ClientProxy {

  public static KeyMapping key;

  public static void registerKeys() {
    key = new KeyMapping("key.run", GLFW.GLFW_KEY_H, "key.categories.movement");
    ClientRegistry.registerKeyBinding(key);
  }
}
