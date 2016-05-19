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
import io.github.flibio.simplescript.parsing.block.Cancel;
import io.github.flibio.simplescript.parsing.event.EventType;
import io.github.flibio.simplescript.parsing.event.EventTypes;
import io.github.flibio.simplescript.parsing.line.Line;
import io.github.flibio.simplescript.parsing.tokenizer.Token;
import io.github.flibio.simplescript.parsing.tokenizer.TokenTypes;
import io.github.flibio.simplescript.parsing.tokenizer.Tokenizer;

public class CancelParser implements Parser<Cancel> {

    @Override
    public boolean canParse(Line line) {
        return line.getData().trim().matches("cancel( [a-zA-Z]*)?");
    }

    @Override
    public Cancel parse(Block superBlock, Line line) {
        if (canParse(line)) {
            Tokenizer tokenizer = new Tokenizer(line.getData().trim());
            tokenizer.nextToken();
            Token nextToken = tokenizer.nextToken();
            if (nextToken.getType().equals(TokenTypes.EMPTY)) {
                // Cancel the main event
                return new Cancel(superBlock, line.getIndentLevel());
            } else {
                // Cancel a linked event
                EventType type = EventTypes.valueOf(nextToken.getValue());
                if (type != null)
                    return new Cancel(superBlock, line.getIndentLevel(), type);
            }
        }
        throw new InvalidParseStringException(line.getData() + " could not be parsed as a cancel!");
    }

}
