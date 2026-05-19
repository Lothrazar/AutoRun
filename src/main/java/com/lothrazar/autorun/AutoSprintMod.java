package com.lothrazar.autorun;

import com.lothrazar.autorun.setup.ConfigAutoRun;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(AutoSprintMod.MODID)
public class AutoSprintMod {

  public static final String MODID = "autorun";

  public AutoSprintMod(IEventBus bus, ModContainer modContainer) {
//    new ConfigAutoRun();
    modContainer.registerConfig(ModConfig.Type.CLIENT, ConfigAutoRun.CONFIG);
    // client-side only — do not load event handlers on dedicated server
    if (FMLEnvironment.dist == Dist.CLIENT) {
      NeoForge.EVENT_BUS.register(new AutoSprintEvents());
    }
    // IExtensionPoint  removed aww
    // client-side display test is declared via displayTest="IGNORE_ALL_VERSION" in neoforge.mods.toml
  }
}
