package com.github.dev34.videoengine;

import org.bukkit.scheduler.BukkitRunnable;

public final class VideoSession {

    private BukkitRunnable task;
    private int currentFrame = 0;
    private int rangeIndex = 0;
    private int codePoint;
    private String font;
    private int frames;
    private int[][] ranges;
    private String[] sounds;
    private int[] soundDurations;
    private int currentSound = 0;

    public VideoSession(String font, int frames, int[][] ranges) {
        this.font = font;
        this.frames = frames;
        this.ranges = ranges;
        this.codePoint = ranges[0][0];
    }

    public void setTask(BukkitRunnable task) {
        this.task = task;
    }

    public int getCurrentFrame() {
        return currentFrame;
    }

    public void setCurrentFrame(int frame) {
        this.currentFrame = frame;
    }

    public int getRangeIndex() {
        return rangeIndex;
    }

    public void setRangeIndex(int rangeIndex) {
        this.rangeIndex = rangeIndex;
    }

    public int getCodePoint() {
        return codePoint;
    }

    public void setCodePoint(int codePoint) {
        this.codePoint = codePoint;
    }

    public String getFont() {
        return font;
    }

    public int getFrames() {
        return frames;
    }

    public int[][] getRanges() {
        return ranges;
    }

    public BukkitRunnable getTask() {
        return task;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public void setFrames(int frames) {
        this.frames = frames;
    }

    public void setRanges(int[][] ranges) {
        this.ranges = ranges;
    }

    public void setSounds(String[] sounds) {
        this.sounds = sounds;
    }

    public void setSoundDurations(int[] soundDurations) {
        this.soundDurations = soundDurations;
    }

    public void pause() {
        if (task != null) task.cancel();
    }

    public void setSounds(String[] sounds, int[] durations) {
        this.sounds = sounds;
        this.soundDurations = durations;
    }

    public String[] getSounds() {
        return sounds;
    }

    public int[] getSoundDurations() {
        return soundDurations;
    }

    public int getCurrentSound() {
        return currentSound;
    }

    public void setCurrentSound(int currentSound) {
        this.currentSound = currentSound;
    }
}
