package com.lothrazar.autorun.setup;

import org.lwjgl.glfw.GLFW;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientProxy implements IProxy {

  public static KeyBinding key;

  @Override
  public void init() {
    registerKeys();
  }

  private void registerKeys() {
    key = new KeyBinding("key.run", GLFW.GLFW_KEY_P, "key.categories.movement");
    ClientRegistry.registerKeyBinding(key);
  }

  @Override
  public World getClientWorld() {
    return Minecraft.getInstance().world;
  }

  @Override
  public PlayerEntity getClientPlayer() {
    return Minecraft.getInstance().player;
  }
}
