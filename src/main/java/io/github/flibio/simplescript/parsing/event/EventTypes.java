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
package io.github.flibio.simplescript.parsing.event;

import io.github.flibio.simplescript.parsing.variable.types.DefinedVariableType;
import io.github.flibio.simplescript.parsing.variable.types.DefinedVariableTypes;

import java.util.Arrays;
import java.util.List;

public class EventTypes {

    public static final EventType JOIN = new EventType() {

        public String getId() {
            return "join";
        }

        public List<DefinedVariableType> getDefinedVariables() {
            return Arrays.asList(DefinedVariableTypes.PLAYER);
        }

    };

    public static final EventType QUIT = new EventType() {

        public String getId() {
            return "quit";
        }

        public List<DefinedVariableType> getDefinedVariables() {
            return Arrays.asList(DefinedVariableTypes.PLAYER);
        }

    };

    public static final EventType BREAK = new EventType() {

        public String getId() {
            return "break";
        }

        public List<DefinedVariableType> getDefinedVariables() {
            return Arrays.asList(DefinedVariableTypes.PLAYER, DefinedVariableTypes.BLOCK);
        }

        public List<EventType> getLinkedEventTypes() {
            return Arrays.asList(DROP);
        }

    };

    public static final EventType DROP = new EventType() {

        public String getId() {
            return "drop";
        }

        public List<DefinedVariableType> getDefinedVariables() {
            return Arrays.asList();
        }
    };

    public static List<EventType> values() {
        return Arrays.asList(JOIN, QUIT, BREAK, DROP);
    }

    public static EventType valueOf(String valueOf) {
        for (EventType type : values()) {
            if (type.getId().equalsIgnoreCase(valueOf.replaceAll("_", " ").trim())) {
                return type;
            }
        }
        return null;
    }

    private EventTypes() {
    }
}
