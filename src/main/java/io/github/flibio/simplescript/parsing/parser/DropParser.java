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
import io.github.flibio.simplescript.parsing.block.Drop;
import io.github.flibio.simplescript.parsing.line.Line;
import io.github.flibio.simplescript.parsing.parser.variable.InlineVariableParser;
import io.github.flibio.simplescript.parsing.parser.variable.InlineVariableParser.ParsedVariable;
import io.github.flibio.simplescript.parsing.tokenizer.Tokenizer;
import io.github.flibio.simplescript.parsing.variable.types.RuntimeVariableTypes;

public class DropParser implements Parser<Drop> {

    @Override
    public boolean canParse(Line line) {
        return line.getData().trim().matches(
                "^drop " + InlineVariableParser.getRegex() + InlineVariableParser.getRegex() + " at "
                        + InlineVariableParser.getRegex() + "$");
    }

    @Override
    public Drop parse(Block superBlock, Line line) {
        if (canParse(line)) {
            Tokenizer tokenizer = new Tokenizer(line.getData());
            tokenizer.nextToken();

            ParsedVariable pAmount = InlineVariableParser.parse(tokenizer, RuntimeVariableTypes.DOUBLE);
            tokenizer = pAmount.getTokenizer();

            ParsedVariable pType = InlineVariableParser.parse(tokenizer, RuntimeVariableTypes.ITEM_TYPE);
            tokenizer = pType.getTokenizer();
            tokenizer.nextToken();
            ParsedVariable pLocation = InlineVariableParser.parse(tokenizer, RuntimeVariableTypes.LOCATION);
            Drop drop = new Drop(superBlock, line.getIndentLevel(), pLocation.getResult(), pType.getResult(), pAmount.getResult());

            drop.addVariables(pAmount.getVariables());
            drop.addVariables(pType.getVariables());
            drop.addVariables(pLocation.getVariables());
            return drop;
        }
        throw new InvalidParseStringException(line.getData() + " could not be parsed as a drop!");
    }
}
