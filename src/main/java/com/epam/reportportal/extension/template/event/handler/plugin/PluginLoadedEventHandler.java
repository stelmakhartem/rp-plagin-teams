package com.epam.reportportal.extension.template.event.handler.plugin;

import com.epam.reportportal.extension.event.PluginEvent;
import com.epam.reportportal.extension.template.event.handler.EventHandler;
import com.epam.ta.reportportal.dao.IntegrationRepository;
import com.epam.ta.reportportal.dao.IntegrationTypeRepository;

/**
 * Handler for plugin load event. Integration is available per project only;
 * no default global integration is created.
 */
public class PluginLoadedEventHandler implements EventHandler<PluginEvent> {

  @SuppressWarnings("unused")
  private final IntegrationTypeRepository integrationTypeRepository;
  @SuppressWarnings("unused")
  private final IntegrationRepository integrationRepository;

  public PluginLoadedEventHandler(IntegrationTypeRepository integrationTypeRepository,
      IntegrationRepository integrationRepository) {
    this.integrationTypeRepository = integrationTypeRepository;
    this.integrationRepository = integrationRepository;
  }

  @Override
  public void handle(PluginEvent event) {
    // Do not create default global integration. Integration is available per project only.
    // Users add it via Project Settings > Integrations > Teams Plugin > Add Project integration.
  }
}
