package de.oliver.fancyholograms.commands.hologram;

import de.oliver.fancyholograms.FancyHolograms;
import de.oliver.fancyholograms.api.Hologram;
import de.oliver.fancyholograms.api.HologramData;
import de.oliver.fancyholograms.api.events.HologramCreateEvent;
import de.oliver.fancyholograms.commands.Subcommand;
import de.oliver.fancylib.MessageHelper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CreateCMD implements Subcommand {

    @Override
    public List<String> tabcompletion(@NotNull Player player, @Nullable Hologram hologram, @NotNull String[] args) {
        return null;
    }

    @Override
    public boolean run(@NotNull Player player, @Nullable Hologram hologram, @NotNull String[] args) {
        String name = args[1];

        if (FancyHolograms.get().getHologramsManager().getHologram(name).isPresent()) {
            MessageHelper.error(player, "There already exists a hologram with this name");
            return false;
        }

        final var data = new HologramData(name);
        data.setText(List.of("Edit this line with /hologram edit " + name));
        data.setLocation(player.getLocation().clone());

        final var holo = FancyHolograms.get().getHologramsManager().create(data);

        if (!new HologramCreateEvent(holo, player).callEvent()) {
            MessageHelper.error(player, "Creating the hologram was cancelled");
            return false;
        }

        holo.createHologram();
        holo.showHologram(Bukkit.getOnlinePlayers());

        FancyHolograms.get().getHologramsManager().addHologram(holo);

        MessageHelper.success(player, "Created the hologram");
        return true;
    }
}
