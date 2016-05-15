/*
 * This file is part of SimpleScript, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2016 - 2016 Flibio
 * Copyright (c) Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package io.github.flibio.simplescript.parsing.variable;

import io.github.flibio.simplescript.parsing.variable.types.RuntimeVariableType;
import io.github.flibio.simplescript.parsing.variable.types.RuntimeVariableTypes;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.command.source.LocatedSource;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.LocateableSnapshot;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Represents a property that is stored on a variable.
 */
public class VariableProperties {

    public static final VariableProperty<String> DISPLAY_NAME = new VariableProperty<String>() {

        public Optional<String> getValue(Object rObj) {
            Object obj = parseUUID(rObj);
            if (obj instanceof DataHolder) {
                Optional<Text> tOpt = ((DataHolder) obj).get(Keys.DISPLAY_NAME);
                return tOpt.isPresent() ? Optional.of(tOpt.get().toPlain()) : Optional.empty();
            }
            return Optional.empty();
        }

        public String getId() {
            return "display name";
        }

        public RuntimeVariableType<?> getType() {
            return RuntimeVariableTypes.STRING;
        }

    };

    public static final VariableProperty<Location<World>> LOCATION = new VariableProperty<Location<World>>() {

        public Optional<Location<World>> getValue(Object rObj) {
            Object obj = parseUUID(rObj);
            if (obj instanceof LocateableSnapshot) {
                return ((LocateableSnapshot<?>) obj).getLocation();
            }
            if (obj instanceof LocatedSource) {
                return Optional.of(((LocatedSource) obj).getLocation());
            }
            return Optional.empty();
        }

        public String getId() {
            return "location";
        }

        public RuntimeVariableType<?> getType() {
            return RuntimeVariableTypes.LOCATION;
        }

    };

    public static final VariableProperty<Boolean> PERMISSION = new VariableProperty<Boolean>() {

        public Optional<Boolean> getValue(Object rObj) {
            return Optional.of(false);
        }

        public boolean test(Object rObj, Object expected) {
            Object obj = parseUUID(rObj);
            if (obj instanceof Subject) {
                return ((Subject) obj).hasPermission(expected.toString());
            }
            return false;
        }

        public String getId() {
            return "permission";
        }

        public RuntimeVariableType<?> getType() {
            return RuntimeVariableTypes.BOOLEAN;
        }

    };

    public static final VariableProperty<String> BLOCK_TYPE = new VariableProperty<String>() {

        public Optional<String> getValue(Object obj) {
            if (obj instanceof BlockSnapshot) {
                return Optional.of(((BlockSnapshot) obj).getState().getType().getName());
            }
            return Optional.empty();
        }

        public String getId() {
            return "block type";
        }

        public RuntimeVariableType<?> getType() {
            return RuntimeVariableTypes.BLOCK_TYPE;
        }

    };

    public static List<VariableProperty<?>> values() {
        return Arrays.asList(DISPLAY_NAME, LOCATION, PERMISSION, BLOCK_TYPE);
    }

    public static VariableProperty<?> valueOf(String valueOf) {
        for (VariableProperty<?> prop : values()) {
            if (prop.getId().equalsIgnoreCase(valueOf.replaceAll("_", " ").trim())) {
                return prop;
            }
        }
        return null;
    }

    private VariableProperties() {
    }

    private static Object parseUUID(Object obj) {
        if (obj instanceof UUID) {
            for (Player p : Sponge.getServer().getOnlinePlayers()) {
                if (p.getUniqueId().equals((UUID) obj)) {
                    return p;
                }
            }
        }
        return obj;
    }
}
