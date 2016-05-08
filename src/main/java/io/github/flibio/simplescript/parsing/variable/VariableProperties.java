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

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.Optional;
import java.util.UUID;

/**
 * Represents a property that is stored on a variable.
 */
public enum VariableProperties implements VariableProperty {

    DISPLAY_NAME {

        public String getValue(Object rObj) {
            Object obj = parseUUID(rObj);
            if (obj instanceof DataHolder) {
                Optional<Text> tOpt = ((DataHolder) obj).get(Keys.DISPLAY_NAME);
                return tOpt.isPresent() ? tOpt.get().toPlain() : "error";
            }
            return "error";
        }

    };

    public static VariableProperty getEnum(String input) {
        for (VariableProperty value : values()) {
            if (value.toString().equalsIgnoreCase(input.replaceAll(" ", "_")))
                return value;
        }
        return null;
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
