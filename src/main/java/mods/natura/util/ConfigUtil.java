package mods.natura.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.world.biome.BiomeGenBase;

import mods.natura.Natura;

public class ConfigUtil {

    private static final Map<String[], Set<Integer>> BIOME_CACHE = new IdentityHashMap<>();

    public static boolean isBiomeAllowed(BiomeGenBase biome, String[] whitelist, String[] blacklist) {
        if (biome == null) {
            return true;
        }
        if (whitelist != null && whitelist.length > 0 && !resolveBiomeIds(whitelist).contains(biome.biomeID)) {
            return false;
        }
        return blacklist == null || blacklist.length == 0 || !resolveBiomeIds(blacklist).contains(biome.biomeID);
    }

    public static boolean isDimensionAllowed(int dimension, int[] whitelist, int[] blacklist) {
        if (whitelist != null && whitelist.length > 0 && !contains(whitelist, dimension)) {
            return false;
        }
        return blacklist == null || blacklist.length == 0 || !contains(blacklist, dimension);
    }

    public static BiomeGenBase[] resolveBiomes(String[] entries) {
        List<BiomeGenBase> biomes = new ArrayList<>();
        if (entries == null || entries.length == 0) {
            return biomes.toArray(new BiomeGenBase[biomes.size()]);
        }
        Set<Integer> ids = resolveBiomeIds(entries);
        for (BiomeGenBase biome : BiomeGenBase.getBiomeGenArray()) {
            if (biome != null && ids.contains(biome.biomeID)) {
                biomes.add(biome);
            }
        }
        return biomes.toArray(new BiomeGenBase[biomes.size()]);
    }

    private static Set<Integer> resolveBiomeIds(String[] entries) {
        Set<Integer> cached = BIOME_CACHE.get(entries);
        if (cached != null) {
            return cached;
        }
        Set<Integer> ids = new HashSet<>();
        for (String entry : entries) {
            if (entry == null) {
                continue;
            }
            String value = entry.trim();
            if (value.isEmpty()) {
                continue;
            }
            BiomeGenBase biome = findBiome(value);
            if (biome == null) {
                Natura.logger.warn("Unknown biome '{}' in Natura config", value);
            } else {
                ids.add(biome.biomeID);
            }
        }
        BIOME_CACHE.put(entries, ids);
        return ids;
    }

    private static BiomeGenBase findBiome(String entry) {
        try {
            int id = Integer.parseInt(entry);
            if (id >= 0 && id < BiomeGenBase.getBiomeGenArray().length) {
                return BiomeGenBase.getBiome(id);
            }
            return null;
        } catch (NumberFormatException ignored) {
            for (BiomeGenBase biome : BiomeGenBase.getBiomeGenArray()) {
                if (biome != null && biome.biomeName != null && entry.equalsIgnoreCase(biome.biomeName)) {
                    return biome;
                }
            }
            return null;
        }
    }

    private static boolean contains(int[] values, int value) {
        for (int i : values) {
            if (i == value) {
                return true;
            }
        }
        return false;
    }
}
