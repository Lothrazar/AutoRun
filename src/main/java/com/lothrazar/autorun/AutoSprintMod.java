package com.lothrazar.autorun;

import com.lothrazar.autorun.setup.ConfigAutoRun;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.IExtensionPoint;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(AutoSprintMod.MODID)
public class AutoSprintMod {

  public static final String MODID = "autorun";

  public AutoSprintMod(IEventBus bus, ModContainer modContainer) {
//    new ConfigAutoRun();
    modContainer.registerConfig(ModConfig.Type.COMMON, ConfigAutoRun.CONFIG);
    // 'net.minecraftforge.fml.DistExecutor' is deprecated and marked for removal
    // do not register client proxy
    NeoForge.EVENT_BUS.register(new AutoSprintEvents());
    //
    //the mod is client only, not required server side
//    ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class,
//        () -> new IExtensionPoint.DisplayTest(() -> "ANY", (remote, isServer) -> true));
  }
}
