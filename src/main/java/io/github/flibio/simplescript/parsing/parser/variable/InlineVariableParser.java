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
package io.github.flibio.simplescript.parsing.parser.variable;

import io.github.flibio.simplescript.parsing.tokenizer.Token;
import io.github.flibio.simplescript.parsing.tokenizer.TokenTypes;
import io.github.flibio.simplescript.parsing.tokenizer.Tokenizer;
import io.github.flibio.simplescript.parsing.variable.Variable;
import io.github.flibio.simplescript.parsing.variable.VariableFunction;
import io.github.flibio.simplescript.parsing.variable.VariableProperties;
import io.github.flibio.simplescript.parsing.variable.VariableProperty;
import io.github.flibio.simplescript.parsing.variable.types.DefinedVariableType;
import io.github.flibio.simplescript.parsing.variable.types.DefinedVariableTypes;
import io.github.flibio.simplescript.parsing.variable.types.RuntimeVariableType;
import io.github.flibio.simplescript.parsing.variable.types.RuntimeVariableTypes;
import io.github.flibio.simplescript.parsing.variable.types.RuntimeVariableTypes.ParsedVarType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class InlineVariableParser {

    public static String getRegex() {
        return "(([a-zA-Z ]+)|(\".*\")|([-]?[0-9]+(\\.[0-9]+)?[ ]*)*)";
    }

    public static ParsedVariable
            parse(Tokenizer tokenizer, List<VariableFunction> functions, List<RuntimeVariableType<?>> types) {
        Tokenizer oTokenizer = new Tokenizer(tokenizer.getData());
        String variableString = "";
        ParsedVariable workingReturn = null;
        // Try to parse a variable
        Token token = tokenizer.nextToken();
        if (token.getValue().equalsIgnoreCase("the")) {
            token = tokenizer.nextToken();
            oTokenizer = new Tokenizer(tokenizer.getData());
        }
        int tries = 0;
        mainLoop: while (!token.getType().equals(TokenTypes.EMPTY)) {
            if (tries > 3) {
                break;
            }
            // Append the variable string
            variableString += token.getType().equals(TokenTypes.STRING) ? " \"" + token.getValue() + "\"" : " " + token.getValue();
            // Trim the string
            variableString = variableString.trim();
            // Check if the variable string is a defined type
            for (DefinedVariableType vType : DefinedVariableTypes.values()) {
                if (vType.getFunctions().containsAll((functions))) {
                    if (variableString.startsWith(vType.getId())) {
                        // The variable is a defined variable
                        String propertyString = variableString.replaceFirst(vType.getId(), "").trim();
                        // Check if the defined variable contains a property
                        for (VariableProperty<?> property : VariableProperties.values()) {
                            // Check if the type is on the list
                            if (types.size() > 0) {
                                if (!types.contains(property.getType())) {
                                    continue;
                                }
                            }
                            if (propertyString.startsWith(property.getId())) {
                                // The defined variable contains a property
                                String finalVarString = vType.getId() + " " + property.getId();
                                workingReturn = new ParsedVariable(finalVarString, convertTokenizer(oTokenizer, finalVarString));
                                token = tokenizer.nextToken();
                                continue mainLoop;
                            }
                        }
                        // The defined variable does not contain a property
                        workingReturn = new ParsedVariable(vType.getId(), convertTokenizer(oTokenizer, vType.getId()));
                        token = tokenizer.nextToken();
                        continue mainLoop;
                    }
                }
            }
            // Check if the variable string is a runtime type
            for (RuntimeVariableType<?> type : RuntimeVariableTypes.values()) {
                // Check if the type is on the list
                if (types.size() > 0) {
                    if (!types.contains(type)) {
                        continue;
                    }
                }
                if (type.isValid(variableString)) {
                    Optional<?> rOpt = type.parse(variableString);
                    if (rOpt.isPresent()) {
                        Object raw = rOpt.get();
                        if (raw instanceof ParsedVarType<?>) {
                            ParsedVarType<?> pType = (ParsedVarType<?>) raw;
                            workingReturn =
                                    new ParsedVariable(pType.getKey(), convertTokenizer(oTokenizer, variableString), new Variable(pType.getKey(),
                                            pType.getResult(), type));
                            token = tokenizer.nextToken();
                            continue mainLoop;
                        }
                    }
                }
            }
            // The string failed to be parsed
            token = tokenizer.nextToken();
        }
        // Return the last working variable if possible
        if (workingReturn != null)
            return workingReturn;
        // The variable could not be parsed
        throw new VariableException("'" + variableString + "' could not be parsed to a valid variable!");
    }

    public static ParsedVariable parse(Tokenizer oTokenizer, RuntimeVariableType<?>... types) {
        return parse(oTokenizer, Arrays.asList(), Arrays.asList(types));
    }

    public static ParsedVariable parse(Tokenizer oTokenizer, List<VariableFunction> functions, RuntimeVariableType<?>... types) {
        return parse(oTokenizer, functions, Arrays.asList(types));
    }

    private static Tokenizer convertTokenizer(Tokenizer tokenizer, String toSkip) {
        Tokenizer convertTokenizer = new Tokenizer(tokenizer.getData());
        String tokenData = convertTokenizer.getData();
        String targetData = tokenData.replaceFirst(toSkip, "").trim();
        while (!tokenData.equalsIgnoreCase(targetData)) {
            convertTokenizer.nextToken();
            tokenData = convertTokenizer.getData();
        }
        return convertTokenizer;
    }

    public static class ParsedVariable {

        private String result;
        private Tokenizer tokenizer;
        private List<Variable> variables = new ArrayList<>();

        public ParsedVariable(String result, Tokenizer tokenizer, Variable... vars) {
            this.result = result;
            this.tokenizer = tokenizer;
            this.variables = Arrays.asList(vars);
        }

        public String getResult() {
            return result;
        }

        public Tokenizer getTokenizer() {
            return tokenizer;
        }

        public List<Variable> getVariables() {
            return variables;
        }
    }

}
