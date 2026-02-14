package com.github.dev34.videoengine.commands;

import com.github.dev34.videoengine.VideoPlayer;
import com.github.dev34.videoengine.VideoEnginePlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VideoEngineCommand implements CommandExecutor {

    private static final String helpMessage = VideoEnginePlugin.colorize(
            "&7---------------&8[&a VideoEngine &8]&7---------------\n" +
                    "&a/videoengine play <video> <duration> [player] &8-&f Start playing a video (duration in seconds)\n" +
                    "&a/videoengine stop [player] &8- &fStop playing a video\n" +
                    "&a/videoengine help &8-&f See this list of commands\n");

    private static final String playPerm = "videoengine.play";
    private static final String stopPerm = "videoengine.stop";
    private static final String playOthersPerm = "videoengine.play.others";
    private static final String stopOthersPerm = "videoengine.stop.others";

    private boolean checkPermission(Player player, String permission) {
        if (!player.hasPermission(permission)) {
            player.sendMessage(VideoEnginePlugin.colorize("&cYou do not have permission to use this command! (" + permission + ")"));
            return false;
        }
        return true;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be run by a player.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            player.sendMessage(helpMessage);
            return true;
        }

        String subcommand = args[0].toLowerCase();

        switch (subcommand) {
            case "play": {
                if (args.length < 3) {
                    player.sendMessage(VideoEnginePlugin.colorize("&cUsage: /videoengine play <video> <duration in seconds> [player]"));
                    return true;
                }

                String video = args[1];
                int durationSeconds;

                try {
                    durationSeconds = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    player.sendMessage(VideoEnginePlugin.colorize("&cDuration must be a number (seconds)!"));
                    return true;
                }

                Player target = (args.length >= 4) ? Bukkit.getPlayerExact(args[3]) : player;

                if (target == null) {
                    player.sendMessage(VideoEnginePlugin.colorize("&cPlayer not found."));
                    return true;
                }

                if (!player.equals(target)) {
                    if (!checkPermission(player, playOthersPerm)) return true;
                } else {
                    if (!checkPermission(player, playPerm) && !checkPermission(player, playOthersPerm)) return true;
                }

                if (VideoPlayer.getViewers().containsKey(target)) {
                    player.sendMessage(VideoEnginePlugin.colorize("&c" + target.getName() + " is already watching a video."));
                    return true;
                }

                player.sendMessage(VideoEnginePlugin.colorize("&aPlaying video &f'" + video + "'&a for &f" + target.getName() + "&a for &f" + durationSeconds + " seconds"));

                VideoPlayer.displayFrames(target, video, durationSeconds * 20);
                break;
            }
            case "stop": {
                Player target = (args.length >= 2) ? Bukkit.getPlayerExact(args[1]) : player;

                if (target == null) {
                    player.sendMessage(VideoEnginePlugin.colorize("&cPlayer not found."));
                    return true;
                }

                if (!player.equals(target)) {
                    if (!checkPermission(player, stopOthersPerm)) return true;
                } else {
                    if (!checkPermission(player, stopPerm) && !checkPermission(player, stopOthersPerm)) return true;
                }

                if (!VideoPlayer.getViewers().containsKey(target)) {
                    player.sendMessage(VideoEnginePlugin.colorize("&c" + target.getName() + " is not watching a video."));
                    return true;
                }

                player.sendMessage(VideoEnginePlugin.colorize("&aVideo has been stopped for " + target.getName()));
                VideoPlayer.stopVideo(target);
                break;
            }
            default:
                player.sendMessage(helpMessage);
        }

        return true;
    }
}
