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
import io.github.flibio.simplescript.parsing.block.Block;
import io.github.flibio.simplescript.parsing.block.Event;
import io.github.flibio.simplescript.parsing.block.Event.EventType;
import io.github.flibio.simplescript.parsing.line.Line;
import io.github.flibio.simplescript.parsing.parser.BroadcastParser;
import io.github.flibio.simplescript.parsing.parser.ConditionalParser;
import io.github.flibio.simplescript.parsing.parser.EventParser;
import io.github.flibio.simplescript.parsing.parser.Parser;
import io.github.flibio.simplescript.parsing.parser.SendParser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FileResolver {

    private List<Parser<?>> parsers = Arrays.asList(new EventParser(), new BroadcastParser(), new SendParser(), new ConditionalParser());
    private Map<Integer, Block> superBlocks = new HashMap<>();
    private Multimap<EventType, Event> events = HashMultimap.create();

    protected FileResolver(List<Line> lines) {

        lines.forEach(line -> {
            for (Parser<?> parser : parsers) {
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
                        events.put(event.getType(), event);
                    }
                }
            }
        });
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
