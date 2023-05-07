package me.isaiah.multiworld.command;

import java.io.File;
import java.io.IOException;

import me.isaiah.multiworld.config.FileConfiguration;
import dimapi.FabricDimensionInternals;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;

@SuppressWarnings("deprecation") // Fabric dimension API
public class SpawnCommand {

    public static int run(MinecraftServer mc, ServerPlayerEntity plr, String[] args) {
        ServerWorld w = plr.getWorld();
        TeleportTarget target = getSpawn(w);
        ServerPlayerEntity teleported = FabricDimensionInternals.changeDimension(plr, w, target);
        return 1;
    }

    public static TeleportTarget getSpawn(ServerWorld w) {
        File config_dir = new File("config");
        config_dir.mkdirs();
        
        File cf = new File(config_dir, "multiworld"); 
        cf.mkdirs();

        File worlds = new File(cf, "worlds");
        worlds.mkdirs();

        Identifier id = w.getRegistryKey().getValue();
        File namespace = new File(worlds, id.getNamespace());
        namespace.mkdirs();

        BlockPos sp = w.getSpawnPos();
        double x = sp.getX();
        double y = sp.getY();
        double z = sp.getZ();
        double spy = w.getSpawnAngle();
        double spp = 0.0;
        File wc = new File(namespace, id.getPath() + ".yml");
        if (wc.exists()) {
        	FileConfiguration config;
	        try {
	            config = new FileConfiguration(wc);
	            x = config.getDouble("spawnX");
	            y = config.getDouble("spawnY");
	            z = config.getDouble("spawnZ");
	            spy = config.getDouble("spawnYaw");
	            spp = config.getDouble("spawnPitch");
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
        }
        return new TeleportTarget(new Vec3d(x, y, z), new Vec3d(0, 0, 0), (float) spy, (float) spp);
    }

}
