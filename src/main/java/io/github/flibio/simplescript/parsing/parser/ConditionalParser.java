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
import io.github.flibio.simplescript.parsing.block.Conditional;
import io.github.flibio.simplescript.parsing.line.Line;
import io.github.flibio.simplescript.parsing.parser.variable.InlineVariableParser;
import io.github.flibio.simplescript.parsing.parser.variable.InlineVariableParser.ParsedVariable;
import io.github.flibio.simplescript.parsing.parser.variable.InlineVariablePropertyParser;
import io.github.flibio.simplescript.parsing.parser.variable.InlineVariablePropertyParser.ParsedProperty;
import io.github.flibio.simplescript.parsing.tokenizer.Tokenizer;

import java.util.Arrays;

public class ConditionalParser implements Parser<Conditional> {

    @Override
    public boolean canParse(Line line) {
        return line.getData().trim()
                .matches("^" + InlineVariableParser.getRegex() + " (has|have) ([a-zA-Z ]+)( not)? of ((\".*\")|([-]?[0-9]+(.[0-9]+)?))$");
    }

    @Override
    public Conditional parse(Block superBlock, Line line) {
        if (canParse(line)) {
            Tokenizer tokenizer = new Tokenizer(line.getData());
            ParsedVariable var = InlineVariableParser.parse(tokenizer);

            tokenizer = var.getTokenizer();
            tokenizer.nextToken();
            if (InlineVariablePropertyParser.isValid(tokenizer.getData(), Arrays.asList("of", "not"))) {
                ParsedProperty parsed = InlineVariablePropertyParser.parse(tokenizer, Arrays.asList("of", "not"));
                tokenizer = parsed.getTokenizer();
                String nextValue = tokenizer.nextToken().getValue();
                if (nextValue.equalsIgnoreCase("of")) {
                    String value = tokenizer.nextToken().getValue();
                    return new Conditional(superBlock, line.getIndentLevel(), var.getResult(), parsed.getProperty(), value, true);
                } else {
                    return new Conditional(superBlock, line.getIndentLevel(), var.getResult(), parsed.getProperty(), nextValue, false);
                }
            }
        }
        throw new InvalidParseStringException(line.getData() + " could not be parsed as a conditional!");
    }
}
