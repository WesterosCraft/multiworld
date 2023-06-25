package me.isaiah.multiworld.forge;

import net.minecraft.server.network.ServerPlayerEntity;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;

/**
 */
public class LuckPermsHandler {
    public static boolean hasPermission(ServerPlayerEntity plr, String perm) {
		LuckPerms luckperms = LuckPermsProvider.get();
		User user = luckperms.getUserManager().getUser(plr.getUuid());
		return user.getCachedData().getPermissionData().checkPermission(perm).asBoolean();
    }

}