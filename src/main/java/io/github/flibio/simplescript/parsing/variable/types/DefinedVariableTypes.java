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

import io.github.flibio.simplescript.parsing.variable.VariableFunction;
import io.github.flibio.simplescript.parsing.variable.VariableFunctions;
import io.github.flibio.simplescript.parsing.variable.VariableProperties;
import io.github.flibio.simplescript.parsing.variable.VariableProperty;

import java.util.Arrays;
import java.util.List;

public class DefinedVariableTypes {

    public static final DefinedVariableType PLAYER = new DefinedVariableType() {

        public String getId() {
            return "player";
        }

        public List<VariableProperty<?>> getProperties() {
            return Arrays.asList(VariableProperties.DISPLAY_NAME, VariableProperties.LOCATION, VariableProperties.PERMISSION);
        }

        public List<VariableFunction> getFunctions() {
            return Arrays.asList(VariableFunctions.SEND_MESSAGE, VariableFunctions.TELEPORT);
        }

    };

    public static final DefinedVariableType BLOCK = new DefinedVariableType() {

        public String getId() {
            return "block";
        }

        public List<VariableProperty<?>> getProperties() {
            return Arrays.asList(VariableProperties.LOCATION, VariableProperties.BLOCK_TYPE);
        }

    };

    public static List<DefinedVariableType> values() {
        return Arrays.asList(PLAYER, BLOCK);
    }

    public static DefinedVariableType valueOf(String valueOf) {
        for (DefinedVariableType type : values()) {
            if (type.getId().equalsIgnoreCase(valueOf.replaceAll("_", " ").trim())) {
                return type;
            }
        }
        return null;
    }

    private DefinedVariableTypes() {
    }

}
