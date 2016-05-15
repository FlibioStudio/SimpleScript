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
package io.github.flibio.simplescript.parsing.variable.types;

import io.github.flibio.simplescript.parsing.tokenizer.Tokenizer;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RuntimeVariableTypes {

    public static final RuntimeVariableType<String> STRING = new RuntimeVariableType<String>() {

        public boolean isValid(String var) {
            return var.matches("^(\".*\")$");
        }

        public Optional<ParsedVarType<String>> parse(String var) {
            if (isValid(var))
                return Optional.of(new ParsedVarType<String>(UUID.randomUUID().toString(), new Tokenizer(var).nextToken().getValue()));
            return Optional.empty();
        }

    };

    public static final RuntimeVariableType<Double> DOUBLE = new RuntimeVariableType<Double>() {

        public boolean isValid(String var) {
            return var.matches("^([-]?[0-9]+(.[0-9]+)?)$");
        }

        public Optional<ParsedVarType<Double>> parse(String var) {
            if (isValid(var))
                return Optional.of(new ParsedVarType<Double>(UUID.randomUUID().toString(), Double.valueOf(var)));
            return Optional.empty();
        }

    };

    public static final RuntimeVariableType<Boolean> BOOLEAN = new RuntimeVariableType<Boolean>() {

        public boolean isValid(String var) {
            return var.matches("^(true|false)$");
        }

        public Optional<ParsedVarType<Boolean>> parse(String var) {
            if (isValid(var))
                return Optional.of(new ParsedVarType<Boolean>(UUID.randomUUID().toString(), Boolean.valueOf(var)));
            return Optional.empty();
        }

    };

    public static final RuntimeVariableType<BlockType> BLOCK_TYPE = new RuntimeVariableType<BlockType>() {

        public boolean isValid(String var) {
            return Sponge.getRegistry().getType(BlockType.class, var.trim()).isPresent();
        }

        public Optional<ParsedVarType<BlockType>> parse(String var) {
            if (isValid(var))
                return Optional.of(new ParsedVarType<BlockType>(UUID.randomUUID().toString(), Sponge.getRegistry()
                        .getType(BlockType.class, var.trim()).get()));
            return Optional.empty();
        }
    };

    public static final RuntimeVariableType<ItemType> ITEM_TYPE = new RuntimeVariableType<ItemType>() {

        public boolean isValid(String var) {
            return Sponge.getRegistry().getType(ItemType.class, var.trim()).isPresent();
        }

        public Optional<ParsedVarType<ItemType>> parse(String var) {
            if (isValid(var))
                return Optional.of(new ParsedVarType<ItemType>(UUID.randomUUID().toString(), Sponge.getRegistry()
                        .getType(ItemType.class, var.trim()).get()));
            return Optional.empty();
        }
    };

    public static final RuntimeVariableType<Location<World>> LOCATION = new RuntimeVariableType<Location<World>>() {

        public boolean isValid(String var) {
            return var.matches("^[-]?[0-9]+ [-]?[0-9]+ [-]?[0-9]+$");
        }

        public Optional<ParsedVarType<Location<World>>> parse(String var) {
            if (isValid(var)) {
                String[] coords = var.split(" ", 3);
                try {
                    int x = Integer.parseInt(coords[0]);
                    int y = Integer.parseInt(coords[1]);
                    int z = Integer.parseInt(coords[2]);
                    Optional<World> wOpt = Sponge.getServer().getWorld(Sponge.getServer().getDefaultWorldName());
                    if (wOpt.isPresent())
                        return Optional
                                .of(new ParsedVarType<Location<World>>(UUID.randomUUID().toString(), new Location<World>(wOpt.get(), x, y, z)));
                    return Optional.empty();
                } catch (Exception e) {
                    return Optional.empty();
                }
            } else {
                return Optional.empty();
            }
        }
    };

    public static List<RuntimeVariableType<?>> values() {
        return Arrays.asList(STRING, BOOLEAN, DOUBLE, BLOCK_TYPE, ITEM_TYPE, LOCATION);
    }

    private RuntimeVariableTypes() {
    }

    public static class ParsedVarType<T> {

        private String key;
        private T result;

        public ParsedVarType(String key, T result) {
            this.result = result;
            this.key = key;
        }

        public String getKey() {
            return key;
        }

        public T getResult() {
            return result;
        }
    }

}
