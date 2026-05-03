package com.lothrazar.autorun.setup;

import com.lothrazar.autorun.AutoSprintMod;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.*;

public class ConfigAutoRun {

  public static ModConfigSpec CONFIG;
  public static DoubleValue SPD_WALKING;
  public static DoubleValue SPD_MOUNTED;
  public static DoubleValue SPD_BOATING;
  public static DoubleValue SPD_CREATIVE;
  public static BooleanValue ALLOW_ELYTRA;
  static {
    final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    final String WALL = "####################################################################################";
    BUILDER.comment(WALL,
        "Mod settings such as speed in different situations", WALL)
        .push(AutoSprintMod.MODID);
    ALLOW_ELYTRA = BUILDER.comment("\r\n Allowed while using Elytra").define("allowElytra", true);
    SPD_WALKING = BUILDER.comment("\r\n Walking speed ")
        .defineInRange("walkingSpeed",
            0.85F,
            0.0010000000000F, 1F);
    SPD_MOUNTED = BUILDER.comment("\r\n Mounted riding speed, while riding any Living Entity ")
        .defineInRange("ridingSpeed",
            0.45F,
            0.0010000000000F, 1F);
    BUILDER.comment("   ");
    SPD_BOATING = BUILDER.comment("\r\n Boating speed (any entity that extends BoatEntity) ")
        .defineInRange("boatingSpeed",
            0.5F,
            0.0010000000000F, 1F);
    BUILDER.comment("   ");
    SPD_CREATIVE = BUILDER.comment("\r\n Creative flight speed")
        .defineInRange("flightSpeed",
            1F,
            0.0010000000000F, 1F);
    //
    CONFIG = BUILDER.build();
  }

}
