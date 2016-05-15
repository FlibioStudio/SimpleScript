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
package io.github.flibio.simplescript.parsing.parser;

import io.github.flibio.simplescript.parsing.block.Block;
import io.github.flibio.simplescript.parsing.block.Teleport;
import io.github.flibio.simplescript.parsing.line.Line;
import io.github.flibio.simplescript.parsing.parser.variable.InlineVariableParser;
import io.github.flibio.simplescript.parsing.parser.variable.InlineVariableParser.ParsedType;
import io.github.flibio.simplescript.parsing.tokenizer.Tokenizer;
import io.github.flibio.simplescript.parsing.variable.VariableFunctions;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TeleportParser implements Parser<Teleport> {

    @Override
    public boolean canParse(Line line) {
        return line.getData().trim().matches("^teleport " + InlineVariableParser.getRegex() + " to " + InlineVariableParser.getRegex() + "$");
    }

    @Override
    public Teleport parse(Block superBlock, Line line) {
        if (canParse(line)) {
            Tokenizer tokenizer = new Tokenizer(line.getData());
            tokenizer.nextToken();

            ParsedType wType = InlineVariableParser.parse(tokenizer, Arrays.asList("to"), VariableFunctions.TELEPORT);
            tokenizer = wType.getTokenizer();

            ParsedType lType = InlineVariableParser.parse(tokenizer, Arrays.asList(""));
            Teleport teleport = new Teleport(superBlock, line.getIndentLevel(), lType.getResult(), wType.getResult());
            teleport.addVariables(Stream.concat(wType.getVariables().stream(), lType.getVariables().stream()).collect(Collectors.toList()));
            return teleport;
        }
        throw new InvalidParseStringException(line.getData() + " could not be parsed as a teleport!");
    }

}
