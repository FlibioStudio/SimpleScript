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
package io.github.flibio.simplescript.parsing.line;

import java.util.ArrayList;
import java.util.List;

public class Line {

    private int indentLevel;
    private String data;

    protected Line(String data, int indentLevel) {
        this.indentLevel = indentLevel;
        this.data = data;
    }

    public static Line of(String rawData) {
        String data = rawData.replace("\t", "    ");
        double spaceCount = (double) data.indexOf(data.trim());
        double rawIndentLevel = spaceCount / 4;
        if ((rawIndentLevel % 1) == 0) {
            // Is a valid space count
            return new Line(data, (int) rawIndentLevel);
        } else {
            // Not a valid space count
            throw new InvalidIndentationException("Invalid indentation count found on line " + rawData.substring(0, 20)
                    + "... Please indent using 4 spaces or 1 tab!");
        }
    }

    public static List<Line> toLines(List<String> lines) {
        List<Line> toReturn = new ArrayList<>();
        lines.forEach(line -> {
            toReturn.add(Line.of(line));
        });
        return toReturn;
    }

    public int getIndentLevel() {
        return indentLevel;
    }

    public String getData() {
        return data;
    }
}
