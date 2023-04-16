package com.lothrazar.autorun;

import com.lothrazar.autorun.setup.ClientProxy;
import com.lothrazar.autorun.setup.ConfigAutoRun;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(AutoSprintMod.MODID)
public class AutoSprintMod {

  public static final String MODID = "autorun";

  public AutoSprintMod() {
    new ConfigAutoRun();
    DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
      FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientProxy::onRegisterKeyMappings);
    });
    MinecraftForge.EVENT_BUS.register(new AutoSprintEvents());
    // allow client only
    ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class,
        () -> new IExtensionPoint.DisplayTest(() -> "ANY", (remote, isServer) -> true));
  }
}
