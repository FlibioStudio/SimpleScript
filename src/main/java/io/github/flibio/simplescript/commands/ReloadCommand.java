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
package io.github.flibio.simplescript.commands;

import org.spongepowered.api.command.spec.CommandSpec;

import io.github.flibio.simplescript.SimpleScript;
import io.github.flibio.simplescript.parsing.FileResolver;
import io.github.flibio.utils.commands.AsyncCommand;
import io.github.flibio.utils.commands.BaseCommandExecutor;
import io.github.flibio.utils.commands.Command;
import io.github.flibio.utils.commands.ParentCommand;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandSpec.Builder;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.io.File;
import java.util.Optional;

@AsyncCommand
@ParentCommand(parentCommand = SimpleScriptCommand.class)
@Command(aliases = {"reload"}, permission = "simplescript.admin.command.reload")
public class ReloadCommand extends BaseCommandExecutor<CommandSource> {

    @Override
    public Builder getCommandSpecBuilder() {
        return CommandSpec.builder()
                .executor(this);
    }

    @Override
    public void run(CommandSource src, CommandContext args) {
        src.sendMessage(Text.of(TextColors.GREEN, "Reloading SimpleScripts..."));
        Optional<FileResolver> rOpt = FileResolver.of(new File("config/simplescript/scripts"));
        if (rOpt.isPresent()) {
            SimpleScript.reload(rOpt.get());
            src.sendMessage(Text.of(TextColors.GREEN, "Reload success!"));
        } else {
            src.sendMessage(Text.of(TextColors.RED, "Reload failed!"));
        }
    }
}
