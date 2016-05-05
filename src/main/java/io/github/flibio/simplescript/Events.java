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
package io.github.flibio.simplescript;

import io.github.flibio.simplescript.parsing.MainParser;
import io.github.flibio.simplescript.parsing.block.Event.EventType;
import io.github.flibio.simplescript.parsing.variable.Variable;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;

public class Events {

    private MainParser parser;

    public Events(MainParser parser) {
        this.parser = parser;
    }

    @Listener
    public void onJoin(ClientConnectionEvent.Join event) {
        parser.getEvents().get(EventType.JOIN).forEach(e -> {
            e.addVariable(new Variable<Player>("player", event.getTargetEntity()));
            e.run();
        });
    }

    @Listener
    public void onQuit(ClientConnectionEvent.Disconnect event) {
        parser.getEvents().get(EventType.QUIT).forEach(e -> {
            e.addVariable(new Variable<Player>("player", event.getTargetEntity()));
            e.run();
        });
    }

}
