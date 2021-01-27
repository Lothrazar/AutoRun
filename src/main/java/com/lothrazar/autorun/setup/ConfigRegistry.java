package com.lothrazar.autorun.setup;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.lothrazar.autorun.AutoSprintMod;
import java.nio.file.Path;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;

public class ConfigRegistry {

  private static final ForgeConfigSpec.Builder CFG = new ForgeConfigSpec.Builder();
  private static ForgeConfigSpec COMMON_CONFIG;
  public static DoubleValue SPD_WALKING;
  public static DoubleValue SPD_MOUNTED;
  public static DoubleValue SPD_BOATING;
  public static DoubleValue SPD_CREATIVE;
  static {
    initConfig();
  }

  private static void initConfig() {
    final String WALL = "####################################################################################";
    CFG.comment(WALL,
        "Mod settings such as speed in different situations", WALL)
        .push(AutoSprintMod.MODID);
    SPD_WALKING = CFG.comment("Walking speed ")
        .defineInRange("walkingSpeed",
            0.85F,
            0.0010000000000F, 1F);
    SPD_MOUNTED = CFG.comment("Mounted riding speed, while riding any Living Entity ")
        .defineInRange("ridingSpeed",
            0.45F,
            0.0010000000000F, 1F);
    SPD_BOATING = CFG.comment("Boating speed (any entity that extends BoatEntity) ")
        .defineInRange("boatingSpeed",
            0.89F,
            0.0010000000000F, 1F);
    SPD_CREATIVE = CFG.comment("Creative flight speed")
        .defineInRange("flightSpeed",
            1F,
            0.0010000000000F, 1F);
    //
    //  CFG.pop();//ROOT
    COMMON_CONFIG = CFG.build();
  }

  public static void setup(Path path) {
    final CommentedFileConfig configData = CommentedFileConfig.builder(path)
        .sync()
        .autosave()
        .writingMode(WritingMode.REPLACE)
        .build();
    configData.load();
    COMMON_CONFIG.setConfig(configData);
  }
}
