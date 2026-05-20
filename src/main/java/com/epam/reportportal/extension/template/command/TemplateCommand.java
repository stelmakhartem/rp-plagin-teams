package com.epam.reportportal.extension.template.command;

import com.epam.reportportal.extension.PluginCommand;
import com.epam.reportportal.extension.template.model.TeamsIntegrationParams;
import com.epam.reportportal.extension.template.service.TeamsNotificationService;
import com.epam.ta.reportportal.entity.integration.Integration;
import com.epam.ta.reportportal.entity.integration.IntegrationParams;
import java.util.Map;

/**
 * Teams plugin command for testing webhook connection.
 */
public class TemplateCommand implements PluginCommand<String> {

  private static final String TEST_CONNECTION = "testConnection";

  @Override
  public String getName() {
    return TEST_CONNECTION;
  }

  @Override
  public String executeCommand(Integration integration, Map<String, Object> params) {
    String webhookUrl = getWebhookUrl(integration, params);
    if (webhookUrl == null || webhookUrl.isEmpty()) {
      throw new IllegalArgumentException("Webhook URL is not configured");
    }
    TeamsNotificationService service = new TeamsNotificationService();
    service.sendNotification(webhookUrl, "Test Launch", null, 1, 1, 0, 0);
    return "OK";
  }

  private String getWebhookUrl(Integration integration, Map<String, Object> params) {
    if (params != null && params.containsKey(TeamsIntegrationParams.WEBHOOK_URL)) {
      Object url = params.get(TeamsIntegrationParams.WEBHOOK_URL);
      return url != null ? url.toString() : null;
    }
    IntegrationParams integrationParams = integration.getParams();
    if (integrationParams != null) {
      Map<String, Object> paramMap = integrationParams.getParams();
      if (paramMap != null) {
        Object url = paramMap.get(TeamsIntegrationParams.WEBHOOK_URL);
        return url != null ? url.toString() : null;
      }
    }
    return null;
  }
}
