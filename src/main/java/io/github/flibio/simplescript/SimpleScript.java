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
package io.github.flibio.simplescript;

import com.google.inject.Inject;
import org.slf4j.Logger;

import io.github.flibio.simplescript.commands.ReloadCommand;
import io.github.flibio.simplescript.commands.SimpleScriptCommand;
import io.github.flibio.simplescript.parsing.FileResolver;
import io.github.flibio.utils.commands.CommandLoader;
import me.flibio.updatifier.Updatifier;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.EventManager;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.plugin.Plugin;

import java.io.File;

@Updatifier(repoName = "SimpleScript", repoOwner = "FlibioStudio", version = PluginInfo.VERSION)
@Plugin(id = PluginInfo.ID, name = PluginInfo.NAME, version = PluginInfo.VERSION, description = PluginInfo.DESCRIPTION)
public class SimpleScript {

    @Inject private Logger logger;

    private static SimpleScript access;

    @Listener
    public void onStart(GameInitializationEvent event) {
        access = this;
        FileResolver resolver = FileResolver.of(new File("config/simplescript/scripts")).get();
        Sponge.getEventManager().registerListeners(this, new Events(resolver));
        CommandLoader.registerCommands(this, "&cYou must be a {sourcetype} to use this command!",
                new SimpleScriptCommand(),
                new ReloadCommand());
    }

    public Logger getLogger() {
        return logger;
    }

    public static SimpleScript getInstance() {
        return access;
    }

    public static void reload(FileResolver resolver) {
        EventManager manager = Sponge.getEventManager();
        manager.unregisterPluginListeners(access);
        manager.registerListeners(access, new Events(resolver));
    }

}
