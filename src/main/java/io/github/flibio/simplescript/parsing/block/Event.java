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
package io.github.flibio.simplescript.parsing.block;

import io.github.flibio.simplescript.parsing.event.EventType;
import org.spongepowered.api.event.Cancellable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class Event extends Block {

    private EventType type;
    private Cancellable event;

    private Event parentEvent;
    private List<UUID> linkedEvents = new ArrayList<>();

    private Object compareObj;

    public Event(Block superBlock, int indentLevel, EventType eventType) {
        super(superBlock, indentLevel);
        this.type = eventType;
    }

    public EventType getType() {
        return type;
    }

    public void setEventCancelled(boolean eventCancelled) {
        if (event != null)
            event.setCancelled(eventCancelled);
    }

    public Optional<Event> getParentEvent() {
        return parentEvent != null ? Optional.of(parentEvent) : Optional.empty();
    }

    public void setParentEvent(Event event) {
        parentEvent = event;
    }

    public void addLinkedEvent(UUID event) {
        linkedEvents.add(event);
    }

    public List<UUID> getLinkedEvents() {
        return linkedEvents;
    }

    public void setCompareObject(Object obj) {
        compareObj = obj;
    }

    public Object getCompareObject() {
        return compareObj;
    }

    public void runEvent(Cancellable event) {
        this.event = event;
        for (Block subBlock : getSubBlocks()) {
            if (isCancelled()) {
                break;
            }
            subBlock.run();
        }
        // Reset the cancellation
        setCancelled(false);
        getSubBlocks().forEach(sb -> {
            sb.setCancelled(false);
        });
        event = null;
        clearVariables();
    }

    @Override
    public void run() {
        runEvent(null);
    }
}
