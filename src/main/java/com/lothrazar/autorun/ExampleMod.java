package com.lothrazar.autorun;

import com.lothrazar.autorun.setup.ClientProxy;
import com.lothrazar.autorun.setup.IProxy;
import com.lothrazar.autorun.setup.ServerProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("autorun")
public class ExampleMod {

  private static final String NBT = "isautorunning";
  public static final IProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new ServerProxy());

  public ExampleMod() {
    // Register the setup method for modloading
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    MinecraftForge.EVENT_BUS.register(this);
  }

  private void setup(final FMLCommonSetupEvent event) {
    proxy.init();
  }

  @SubscribeEvent
  public void onTick(PlayerTickEvent event) {
    PlayerEntity p = event.player;
    if (p.getPersistentData().getBoolean(NBT)) {
      this.move(p);
    }
  }

  private void move(PlayerEntity p) {
    p.moveForward = 0.85F;
    Vec3d p_213352_1_ = new Vec3d(p.moveStrafing, p.moveVertical, p.moveForward);
    World world = p.world;
    BlockPos blockpos = new BlockPos(p.posX, p.getBoundingBox().minY - 1.0D, p.posZ);
    float f5 = p.world.getBlockState(blockpos).getSlipperiness(world, blockpos, p);
    p.moveRelative(func_213335_r(p, f5), p_213352_1_);
  }

  /**
   * LivingEntity.class::2091
   * 
   * @param p
   * @param p_213335_1_
   * @return
   */
  private float func_213335_r(PlayerEntity p, float p_213335_1_) {
    return p.onGround ? p.getAIMoveSpeed() * (0.21600002F / (p_213335_1_ * p_213335_1_ * p_213335_1_)) : p.jumpMovementFactor;
  }

  @OnlyIn(Dist.CLIENT)
  @SubscribeEvent
  public void onKeyInput(InputEvent.KeyInputEvent event) {
    PlayerEntity player = proxy.getClientPlayer();
    boolean isCurrentlyAutorun = player.getPersistentData().getBoolean(NBT);
    if (ClientProxy.key != null && ClientProxy.key.isPressed()) {
      //toggle it off
      setAutorunState(player, !isCurrentlyAutorun);
    }
    else if (isCurrentlyAutorun) {
      if (Minecraft.getInstance().gameSettings.keyBindForward.isKeyDown() ||
          Minecraft.getInstance().gameSettings.keyBindBack.isKeyDown() ||
          Minecraft.getInstance().gameSettings.keyBindLeft.isKeyDown() ||
          Minecraft.getInstance().gameSettings.keyBindRight.isKeyDown()) {
        setAutorunState(proxy.getClientPlayer(), false);
      }
    }
  }

  @OnlyIn(Dist.CLIENT)
  public static void setAutorunState(PlayerEntity player, boolean value) {
    player.getPersistentData().putBoolean(NBT, value);
    player.sendStatusMessage(new TranslationTextComponent("autorun." + value), true);
  }
}
