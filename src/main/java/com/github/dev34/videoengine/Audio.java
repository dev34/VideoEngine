package com.github.dev34.videoengine;

public class Audio {

    private final String sound;
    private final long durationTicks;

    public Audio(String sound, long durationTicks) {
        this.sound = sound;
        this.durationTicks = durationTicks;
    }

    public String getSound() {
        return sound;
    }

    public long getDurationTicks() {
        return durationTicks;
    }
}
