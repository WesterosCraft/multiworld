/**
 * Multiworld Mod
 * Copyright (c) 2021-2022 by Isaiah.
 */
package me.isaiah.multiworld;

import com.mojang.brigadier.CommandDispatcher;

import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.StringArgumentType.greedyString;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import me.isaiah.multiworld.command.CreateCommand;
import me.isaiah.multiworld.command.SetspawnCommand;
import me.isaiah.multiworld.command.SpawnCommand;
import me.isaiah.multiworld.command.TpCommand;
import me.isaiah.multiworld.perm.Perm;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.Difficulty;
import net.minecraft.util.registry.RegistryKey;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Multiworld version 1.3
 */
public class MultiworldMod {

    public static final String MOD_ID = "multiworld";
    public static MinecraftServer mc;
    public static String CMD = "mw";
    public static ICreator world_creator;
	public static final Logger log = LogManager.getLogger("multiworld");

    public static void setICreator(ICreator ic) {
        world_creator = ic;
    }

    public static ServerWorld create_world(String id, RegistryKey<DimensionType> dim, ChunkGenerator gen, Difficulty dif) {
        return world_creator.create_world(id, dim,gen,dif);
    }

    // On mod init
    public static void init() {
    	MultiworldMod.log.info(" Multiworld init");
    }

    // On server start
    public static void on_server_started(MinecraftServer mc) {
        MultiworldMod.mc = mc;
    }
    
    // On command register
    public static void register_commands(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal(CMD)
                    .requires(source -> {
                    	if (source.getEntity() instanceof ServerPlayerEntity) {
                    		try {
                    			return Perm.has(source.getPlayer(), "multiworld.cmd") ||
                                    Perm.has(source.getPlayer(), "multiworld.admin");                    		                        	
                    		} catch (CommandSyntaxException e) {
                    		}
                    	}
            			return source.hasPermissionLevel(1);
                    }) 
                        .executes(ctx -> {
                            return broadcast(ctx.getSource(), Formatting.AQUA, null);
                        })
                        .then(argument("message", greedyString()).suggests(new InfoSuggest())
                                .executes(ctx -> {
                                    try {
                                        return broadcast(ctx.getSource(), Formatting.AQUA, getString(ctx, "message") );
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        return 1;
                                    }
                                 }))); 
    }
    
    public static int broadcast(ServerCommandSource source, Formatting formatting, String message) throws CommandSyntaxException {
        if (null == message) {
        	source.sendFeedback(new LiteralText("Usage:").formatted(Formatting.AQUA), false);
            return 1;
        }

        boolean ALL = Perm.has(source, "multiworld.admin");
        String[] args = message.split(" ");

        if (args[0].equalsIgnoreCase("setspawn") && (ALL || Perm.has(source, "multiworld.setspawn") )) {
            return SetspawnCommand.run(mc, source.getPlayer(), args);
        }

        if (args[0].equalsIgnoreCase("spawn") && (ALL || Perm.has(source, "multiworld.spawn")) ) {
            return SpawnCommand.run(mc, source.getPlayer(), args);
        }

        if (args[0].equalsIgnoreCase("tp") ) {
            if (!(ALL || Perm.has(source, "multiworld.tp"))) {
                source.sendFeedback(Text.of("No permission! Missing permission: multiworld.tp"), false);
                return 1;
            }
            if (args.length == 1) {
                source.sendFeedback(new LiteralText("Usage: /" + CMD + " tp <world>"), false);
                return 0;
            }
            return TpCommand.run(mc, source.getPlayer(), args);
        }

        if (args[0].equalsIgnoreCase("list") ) {
            if (!(ALL || Perm.has(source, "multiworld.cmd"))) {
                source.sendFeedback(Text.of("No permission! Missing permission: multiworld.cmd"), false);
                return 1;
            }
            source.sendFeedback(new LiteralText("All Worlds:").formatted(Formatting.AQUA), false);
            mc.getWorlds().forEach(world -> {
                String name = world.getRegistryKey().getValue().toString();
                if (name.startsWith("multiworld:")) name = name.replace("multiworld:", "");

                source.sendFeedback(new LiteralText("- " + name), false);
            });
        }

        if (args[0].equalsIgnoreCase("version") && (ALL || Perm.has(source, "multiworld.cmd")) ) {
        	source.sendFeedback(new LiteralText("Westeroscraft Mutliworld Mod version 1.4.2"), false);
            return 1;
        }

        if (args[0].equalsIgnoreCase("create") ) {
            if (!(ALL || Perm.has(source, "multiworld.create"))) {
                source.sendFeedback(Text.of("No permission! Missing permission: multiworld.create"), false);
                return 1;
            }
            return CreateCommand.run(mc, source, args);
        }

        return Command.SINGLE_SUCCESS; // Success
    }
    
    
}
