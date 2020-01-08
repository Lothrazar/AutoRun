package com.lothrazar.autorun;

import com.lothrazar.autorun.setup.ClientProxy;
import com.lothrazar.autorun.setup.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(modid = "autorun")
public class AutoSprintMod {

  @SidedProxy(clientSide = "com.lothrazar.autorun.setup.ClientProxy", serverSide = "com.lothrazar.autorun.setup.CommonProxy")
  public static CommonProxy proxy;

  public AutoSprintMod() {
    // Register the setup method for modloading
    //    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    MinecraftForge.EVENT_BUS.register(this);
  }

  @EventHandler
  public void onInit(FMLInitializationEvent event) {
    proxy.init();
  }

  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public void onTick(PlayerTickEvent event) {
    if (AutoSprintUtil.getAutorunState(event.player)) {
      AutoSprintUtil.move(event.player);
    }
  }

  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public void onKeyInput(InputEvent.KeyInputEvent event) {
    EntityPlayer player = proxy.getClientPlayer();
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

  private boolean doesKeypressHaltSprint(EntityPlayer p) {
    if (p.getRidingEntity() instanceof EntityBoat) {
      //boats can still turn left/right 
      return Minecraft.getMinecraft().gameSettings.keyBindForward.isKeyDown() ||
          Minecraft.getMinecraft().gameSettings.keyBindBack.isKeyDown();
    }
    return Minecraft.getMinecraft().gameSettings.keyBindForward.isKeyDown() ||
        Minecraft.getMinecraft().gameSettings.keyBindBack.isKeyDown() ||
        Minecraft.getMinecraft().gameSettings.keyBindLeft.isKeyDown() ||
        Minecraft.getMinecraft().gameSettings.keyBindRight.isKeyDown();
  }
}
