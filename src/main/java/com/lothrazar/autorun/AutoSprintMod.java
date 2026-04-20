package com.lothrazar.autorun;

import com.lothrazar.autorun.setup.ConfigAutoRun;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(AutoSprintMod.MODID)
public class AutoSprintMod {

  public static final String MODID = "autorun";

  public AutoSprintMod() {
    new ConfigAutoRun();
    // 'net.minecraftforge.fml.DistExecutor' is deprecated and marked for removal
    // do not register client proxy
    MinecraftForge.EVENT_BUS.register(new AutoSprintEvents());
    //
     //
    //the mod is client only, not required server side
    ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class,
        () -> new IExtensionPoint.DisplayTest(() -> "ANY", (remote, isServer) -> true));
  }
}
