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

import io.github.flibio.simplescript.parsing.FileResolver;
import io.github.flibio.simplescript.parsing.event.EventTypes;
import io.github.flibio.simplescript.parsing.event.LinkedEvent;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.entity.spawn.BlockSpawnCause;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.item.inventory.DropItemEvent;

public class LinkedEvents {

    private FileResolver resolver;

    public LinkedEvents(FileResolver resolver) {
        this.resolver = resolver;
    }

    @Listener
    public void onDrop(DropItemEvent.Destruct event, @First BlockSpawnCause cause) {
        resolver.getEvents().values().forEach(mainEvent -> {
            mainEvent.getLinkedEvents().forEach(eventUuid -> {
                if (SimpleScript.getLinkedEvents().containsKey(eventUuid)) {
                    LinkedEvent linkedEvent = SimpleScript.getLinkedEvents().get(eventUuid);
                    if (linkedEvent.getType().equals(EventTypes.DROP)) {
                        if (linkedEvent.isCancelled() && mainEvent.getCompareObject().equals(cause.getBlockSnapshot().getLocation().get())) {
                            event.getEntities().clear();
                            linkedEvent.setCancelled(false);
                            SimpleScript.addLinkedEvent(linkedEvent);
                        }
                    }
                }
            });
        });
    }
}
