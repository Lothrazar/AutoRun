package com.lothrazar.autorun;

import com.lothrazar.autorun.setup.ClientProxy;
import com.lothrazar.autorun.setup.ConfigRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(AutoSprintMod.MODID)
public class AutoSprintMod {

  public static final String MODID = "autorun";

  public AutoSprintMod() {
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupClient);
    ConfigRegistry.setup(FMLPaths.CONFIGDIR.get().resolve(MODID + ".toml"));
    MinecraftForge.EVENT_BUS.register(new AutoSprintEvents());
    // allow client only
    ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class,
            () -> new IExtensionPoint.DisplayTest(() -> "ANY", (remote, isServer) -> true));
//    ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));
  }

  private void setupClient(final FMLClientSetupEvent event) {
    ClientProxy.registerKeys();
  }
}
