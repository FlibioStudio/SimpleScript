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
package io.github.flibio.simplescript.parsing;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.io.Files;
import io.github.flibio.simplescript.SimpleScript;
import io.github.flibio.simplescript.parsing.block.Block;
import io.github.flibio.simplescript.parsing.block.Event;
import io.github.flibio.simplescript.parsing.event.EventType;
import io.github.flibio.simplescript.parsing.event.LinkedEvent;
import io.github.flibio.simplescript.parsing.line.Line;
import io.github.flibio.simplescript.parsing.parser.BroadcastParser;
import io.github.flibio.simplescript.parsing.parser.CancelParser;
import io.github.flibio.simplescript.parsing.parser.ConditionalParser;
import io.github.flibio.simplescript.parsing.parser.DropParser;
import io.github.flibio.simplescript.parsing.parser.EventParser;
import io.github.flibio.simplescript.parsing.parser.Parser;
import io.github.flibio.simplescript.parsing.parser.SendParser;
import io.github.flibio.simplescript.parsing.parser.SetBlockParser;
import io.github.flibio.simplescript.parsing.parser.TeleportParser;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class FileResolver {

    private Logger logger = SimpleScript.getInstance().getLogger();

    private List<Parser<?>> parsers = Arrays.asList(new EventParser(), new BroadcastParser(), new SendParser(), new ConditionalParser(),
            new CancelParser(), new TeleportParser(), new DropParser(), new SetBlockParser());
    private Map<Integer, Block> superBlocks = new HashMap<>();
    private Multimap<EventType, Event> events = HashMultimap.create();

    protected FileResolver(List<Line> lines) {
        int lineNumber = 1;

        for (Line line : lines) {
            // Skip lines that are commented
            if (line.getData().trim().length() >= 1 && line.getData().trim().substring(0, 1).equals("#"))
                continue;
            boolean parsed = false;
            for (Parser<?> parser : parsers) {
                try {
                    if (parser.canParse(line)) {
                        int indent = line.getIndentLevel();
                        // Parse the line and set the superblock
                        Block superBlock = superBlocks.get(indent - 1);
                        Block block = parser.parse(superBlock, line);
                        if (superBlock != null) {
                            superBlock.addSubBlock(block);
                        }
                        // Set the current superblock for this line indent
                        superBlocks.put(indent, block);
                        if (block instanceof Event) {
                            // Add the event to the event list
                            Event event = (Event) block;
                            // Add any linked events
                            for (EventType type : event.getType().getLinkedEventTypes()) {
                                UUID uuid = UUID.randomUUID();
                                while (SimpleScript.getLinkedEvents().containsKey(uuid)) {
                                    uuid = UUID.randomUUID();
                                }
                                LinkedEvent linkedEvent = new LinkedEvent(event, type, uuid);
                                event.addLinkedEvent(uuid);
                                SimpleScript.addLinkedEvent(linkedEvent);
                            }
                            // Add the event to the event list
                            events.put(event.getType(), event);
                        }
                        parsed = true;
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage());
                    parsed = false;
                }
            }
            if (!parsed) {
                logger.error("Line '" + line.getData().trim() + "'[" + lineNumber + "] could not be parsed!");
            }
            lineNumber++;
        }
    }

    public Multimap<EventType, Event> getEvents() {
        return events;
    }

    public static Optional<FileResolver> of(File folder) {
        try {
            List<Line> lines = new ArrayList<>();
            folder.mkdirs();
            for (File file : folder.listFiles()) {
                if (file.isFile()) {
                    String ext = Files.getFileExtension(file.getName());
                    if (ext.equalsIgnoreCase("ss")) {
                        lines.addAll(Line.toLines(java.nio.file.Files.readAllLines(Paths.get(file.getPath()))));
                    }
                }
            }
            return Optional.of(new FileResolver(lines));
        } catch (IOException e) {
            return Optional.empty();
        }
    }
}
