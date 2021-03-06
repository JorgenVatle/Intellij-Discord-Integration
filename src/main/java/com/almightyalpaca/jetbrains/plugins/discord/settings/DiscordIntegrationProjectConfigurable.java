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
package com.almightyalpaca.jetbrains.plugins.discord.settings;

import com.almightyalpaca.jetbrains.plugins.discord.components.DiscordIntegrationApplicationComponent;
import com.almightyalpaca.jetbrains.plugins.discord.components.DiscordIntegrationProjectComponent;
import com.almightyalpaca.jetbrains.plugins.discord.data.InstanceInfo;
import com.almightyalpaca.jetbrains.plugins.discord.data.ProjectInfo;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class DiscordIntegrationProjectConfigurable implements SearchableConfigurable
{
    private final DiscordIntegrationApplicationComponent applicationComponent = DiscordIntegrationApplicationComponent.getInstance();

    @NotNull
    private final Project project;
    @NotNull
    private final DiscordIntegrationApplicationSettings settingsProviderApplication;
    @NotNull
    private final DiscordIntegrationProjectSettings settingsProviderProject;
    @Nullable
    private DiscordIntegrationSettingsPanel panel;

    public DiscordIntegrationProjectConfigurable(@NotNull Project project)
    {
        this.project = project;

        this.settingsProviderApplication = DiscordIntegrationApplicationSettings.getInstance();
        this.settingsProviderProject = DiscordIntegrationProjectSettings.getInstance(project);
    }

    public static DiscordIntegrationProjectConfigurable getInstance(@NotNull Project project)
    {
        return ServiceManager.getService(project, DiscordIntegrationProjectConfigurable.class);
    }

    @NotNull
    public Project getProject()
    {
        return this.project;
    }

    @Nls
    @NotNull
    @Override
    public String getDisplayName()
    {
        return "Discord";
    }

    @NotNull
    @Override
    public String getHelpTopic()
    {
        return "Discord Integration Settings";
    }

    @NotNull
    @Override
    public JComponent createComponent()
    {
        if (this.panel == null)
            this.panel = new DiscordIntegrationSettingsPanel(this.settingsProviderApplication, this.settingsProviderProject);

        return this.panel.getRootPanel();
    }

    @Override
    public boolean isModified()
    {
        return this.panel != null && this.panel.isModified();
    }

    @Override
    public void apply()
    {
        if (this.panel != null)
        {
            this.panel.apply();

            InstanceInfo instance = this.applicationComponent.getInstanceInfo();
            DiscordIntegrationProjectComponent component = DiscordIntegrationProjectComponent.getInstance(this.settingsProviderProject.getProject());

            if (component != null)
            {
                ProjectInfo project = component.getProjectInfo();

                this.applicationComponent.updateData(data -> data.instanceSetSettings(System.currentTimeMillis(), instance, this.settingsProviderApplication.getSettings()));
                this.applicationComponent.updateData(data -> data.projectSetSettings(System.currentTimeMillis(), instance, project, this.settingsProviderProject.getSettings()));
            }
        }
    }

    @Override
    public void reset()
    {
        if (this.panel != null)
            this.panel.reset();
    }

    @Override
    public void disposeUIResources()
    {
        this.panel = null;
    }

    @NotNull
    @Override
    public String getId()
    {
        return "preferences.discord";
    }

    @Nullable
    @Override
    public Runnable enableSearch(String option)
    {
        return null;
    }
}

