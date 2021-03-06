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
import io.github.flibio.simplescript.parsing.block.Send;
import io.github.flibio.simplescript.parsing.line.Line;
import io.github.flibio.simplescript.parsing.parser.variable.InlineVariableParser;
import io.github.flibio.simplescript.parsing.parser.variable.InlineVariableParser.ParsedVariable;
import io.github.flibio.simplescript.parsing.tokenizer.Tokenizer;
import io.github.flibio.simplescript.parsing.variable.VariableFunctions;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SendParser implements Parser<Send> {

    @Override
    public boolean canParse(Line line) {
        return line.getData().trim().matches("^send " + InlineVariableParser.getRegex() + " to " + InlineVariableParser.getRegex() + "$");
    }

    @Override
    public Send parse(Block superBlock, Line line) {
        if (canParse(line)) {
            Tokenizer tokenizer = new Tokenizer(line.getData());
            tokenizer.nextToken();

            ParsedVariable mType = InlineVariableParser.parse(tokenizer);
            tokenizer = mType.getTokenizer();
            tokenizer.nextToken();
            ParsedVariable tType = InlineVariableParser.parse(tokenizer, Arrays.asList(VariableFunctions.SEND_MESSAGE));
            Send send = new Send(superBlock, line.getIndentLevel(), mType.getResult(), tType.getResult());
            send.addVariables(Stream.concat(mType.getVariables().stream(), tType.getVariables().stream()).collect(Collectors.toList()));
            return send;
        }
        throw new InvalidParseStringException(line.getData() + " could not be parsed as a send!");
    }

}
