/*
 * Copyright 2017 Aljoscha Grebe
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.almightyalpaca.jetbrains.plugins.discord.listeners;

import com.almightyalpaca.jetbrains.plugins.discord.components.DiscordIntegrationProjectComponent;
import com.intellij.openapi.editor.event.VisibleAreaEvent;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("Duplicates")
public class VisibleAreaListener implements com.intellij.openapi.editor.event.VisibleAreaListener
{
    @NotNull
    private static final Logger LOG = LoggerFactory.getLogger(VisibleAreaListener.class);

    @Override
    public void visibleAreaChanged(VisibleAreaEvent event)
    {
        LOG.trace("VisibleAreaListener#visibleAreaChanged({})", event);

        FileDocumentManager documentManager = FileDocumentManager.getInstance();

        Project project = event.getEditor().getProject();
        DiscordIntegrationProjectComponent component = DiscordIntegrationProjectComponent.getInstance(project);

        if (component != null)
            component.updateTimeAccessed(documentManager.getFile(event.getEditor().getDocument()));
    }
}
