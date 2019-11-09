package com.lothrazar.autorun.setup;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class ServerProxy extends CommonProxy {

  @Override
  public void init() {}

  @Override
  public World getClientWorld() {
    throw new IllegalStateException("Client side only code on the server");
  }

  @Override
  public EntityPlayer getClientPlayer() {
    throw new IllegalStateException("Only run this on the client!");
  }
}
