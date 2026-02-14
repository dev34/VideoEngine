package com.github.dev34.videoengine;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.SoundCategory;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public final class VideoPlayer {

    private static HashMap<Player, VideoSession> viewers = new HashMap<>();

    public static HashMap<Player, VideoSession> getViewers() {
        return viewers;
    }

    public static void displayFrame(Player player, String font, String character){
        if (character == null || character.isEmpty()) return;
        try {
            TextComponent component = Component.text(character).font(Key.key(font));
            BukkitAudiences audiences = BukkitAudiences.create(VideoEnginePlugin.plugin);
            audiences.player(player).sendActionBar(component);
        } catch (Exception e) {
            Bukkit.getLogger().warning("Couldn't display frame! " + e.getMessage());
        }
    }

    public static void displayFrames(Player player, String font, int frames) {

        int[][] ranges = {
                {0xE000, 0xF8FF},
                {0xF0000, 0xFFFFD},
                {0x100000, 0x10FFFD}
        };

        int totalAvailable = 0;
        for (int[] range : ranges) {
            totalAvailable += range[1] - range[0] + 1;
        }

        if (frames > totalAvailable) return;

        VideoSession session = new VideoSession(font, frames, ranges);
        viewers.put(player, session);

        int secondsTotal = frames / 20;
        int remainingSeconds = secondsTotal;
        int soundCount = (int) Math.ceil(secondsTotal / 10.0);

        String[] sounds = new String[soundCount];
        int[] durations = new int[soundCount];

        for (int i = 0; i < soundCount; i++) {
            int sec = Math.min(10, remainingSeconds);
            remainingSeconds -= sec;
            sounds[i] = "minecraft:" + font + "_" + i;
            durations[i] = sec * 20;
        }

        session.setSounds(sounds, durations);

        BukkitRunnable runnable = new BukkitRunnable() {

            int soundTickCounter = 0;

            @Override
            public void run() {

                if (session.getCurrentFrame() >= session.getFrames()) {
                    cancel();
                    viewers.remove(player);
                    return;
                }

                if (session.getSounds() != null && session.getCurrentSound() < session.getSounds().length) {
                    if (soundTickCounter == 0) {
                        player.playSound(player, session.getSounds()[session.getCurrentSound()], SoundCategory.MASTER, 1.0f, 1.0f);
                    }

                    soundTickCounter++;

                    if (soundTickCounter >= session.getSoundDurations()[session.getCurrentSound()]) {
                        soundTickCounter = 0;
                        session.setCurrentSound(session.getCurrentSound() + 1);
                    }
                }

                String character = new String(Character.toChars(session.getCodePoint()));
                displayFrame(player, session.getFont(), character);

                session.setCurrentFrame(session.getCurrentFrame() + 1);
                session.setCodePoint(session.getCodePoint() + 1);

                if (session.getCodePoint() > session.getRanges()[session.getRangeIndex()][1]) {
                    session.setRangeIndex(session.getRangeIndex() + 1);
                    if (session.getRangeIndex() < session.getRanges().length) {
                        session.setCodePoint(session.getRanges()[session.getRangeIndex()][0]);
                    }
                }
            }
        };

        session.setTask(runnable);
        runnable.runTaskTimer(VideoEnginePlugin.plugin, 0L, 1L);
    }

    public static void stopVideo(Player player) {
        VideoSession session = viewers.remove(player);

        if (session != null) {
            session.pause();
            BukkitAudiences audiences = BukkitAudiences.create(VideoEnginePlugin.plugin);
            audiences.player(player).sendActionBar(Component.text(""));
            player.stopAllSounds();
        }
    }

    // Pause & Stop causes video to stop being synced with audio

    public static void pauseVideo(Player player) {
        VideoSession session = viewers.get(player);
        if (session != null) {
            session.pause();
        }
    }

    public static void resumeVideo(Player player) {
        VideoSession session = viewers.get(player);
        if (session == null) return;

        BukkitRunnable runnable = new BukkitRunnable() {

            int soundTickCounter = 0;

            @Override
            public void run() {

                if (session.getCurrentFrame() >= session.getFrames()) {
                    cancel();
                    stopVideo(player);
                    return;
                }

                if (session.getSounds() != null && session.getCurrentSound() < session.getSounds().length) {
                    if (soundTickCounter == 0) {
                        player.playSound(player, session.getSounds()[session.getCurrentSound()], SoundCategory.MASTER, 1.0f, 1.0f);
                    }

                    soundTickCounter++;

                    if (soundTickCounter >= session.getSoundDurations()[session.getCurrentSound()]) {
                        soundTickCounter = 0;
                        session.setCurrentSound(session.getCurrentSound() + 1);
                    }
                }

                String character = new String(Character.toChars(session.getCodePoint()));
                displayFrame(player, session.getFont(), character);

                session.setCurrentFrame(session.getCurrentFrame() + 1);
                session.setCodePoint(session.getCodePoint() + 1);

                if (session.getCodePoint() > session.getRanges()[session.getRangeIndex()][1]) {
                    session.setRangeIndex(session.getRangeIndex() + 1);
                    if (session.getRangeIndex() < session.getRanges().length) {
                        session.setCodePoint(session.getRanges()[session.getRangeIndex()][0]);
                    }
                }
            }
        };

        session.setTask(runnable);
        runnable.runTaskTimer(VideoEnginePlugin.plugin, 0L, 1L);

    }

}
