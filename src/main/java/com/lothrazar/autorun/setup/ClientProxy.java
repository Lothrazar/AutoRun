package com.lothrazar.autorun.setup;

import org.lwjgl.input.Keyboard;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy {

  public static KeyBinding key;

  @Override
  public void init() {
    registerKeys();
  }

  private void registerKeys() {
    key = new KeyBinding("key.autorun", Keyboard.KEY_P, "key.categories.movement");
    ClientRegistry.registerKeyBinding(key);
  }

  @Override
  public World getClientWorld() {
    return Minecraft.getMinecraft().world;
  }

  @Override
  public EntityPlayer getClientPlayer() {
    return Minecraft.getMinecraft().player;
  }
}
