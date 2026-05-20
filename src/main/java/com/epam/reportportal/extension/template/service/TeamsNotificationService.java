package com.epam.reportportal.extension.template.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service for sending launch notifications to Microsoft Teams via Incoming Webhook.
 * Uses JDK HttpClient (no external deps).
 */
public class TeamsNotificationService {

  private static final Logger LOGGER = LoggerFactory.getLogger(TeamsNotificationService.class);
  private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
      .connectTimeout(Duration.ofSeconds(10))
      .build();

  /**
   * Sends launch results to Teams channel via webhook.
   */
  public void sendNotification(String webhookUrl, String launchName, String launchLink,
      int total, int passed, int failed, int skipped) {
    try {
      String json = buildAdaptiveCardJson(launchName, launchLink, total, passed, failed, skipped);
      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(webhookUrl))
          .timeout(Duration.ofSeconds(10))
          .header("Content-Type", "application/json")
          .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
          .build();
      HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
      if (response.statusCode() >= 200 && response.statusCode() < 300) {
        LOGGER.info("Teams notification sent successfully for launch: {}", launchName);
      } else {
        LOGGER.warn("Teams webhook returned status {} for launch: {}", response.statusCode(), launchName);
      }
    } catch (Exception e) {
      LOGGER.error("Failed to send Teams notification for launch: {}", launchName, e);
    }
  }

  private static String escapeJson(String s) {
    if (s == null) {
      return "";
    }
    return s.replace("\\", "\\\\")
        .replace("\"", "\\\"")
        .replace("\n", "\\n")
        .replace("\r", "\\r")
        .replace("\t", "\\t");
  }

  private static String buildAdaptiveCardJson(String launchName, String launchLink,
      int total, int passed, int failed, int skipped) {
    StringBuilder sb = new StringBuilder();
    sb.append("{\"type\":\"message\",\"summary\":\"Report Portal: Launch Results\",");
    sb.append("\"attachments\":[{\"contentType\":\"application/vnd.microsoft.card.adaptive\",");
    sb.append("\"contentUrl\":null,\"content\":{");
    sb.append("\"$schema\":\"http://adaptivecards.io/schemas/adaptive-card.json\",");
    sb.append("\"type\":\"AdaptiveCard\",\"version\":\"1.2\",");
    sb.append("\"body\":[");
    sb.append("{\"type\":\"TextBlock\",\"text\":\"Report Portal\",\"weight\":\"bolder\",\"size\":\"large\"},");
    sb.append("{\"type\":\"TextBlock\",\"text\":\"Launch: ").append(escapeJson(launchName)).append("\",\"weight\":\"default\",\"size\":\"default\"},");
    sb.append("{\"type\":\"FactSet\",\"facts\":[");
    sb.append("{\"title\":\"Passed\",\"value\":\"").append(passed).append("\"},");
    sb.append("{\"title\":\"Failed\",\"value\":\"").append(failed).append("\"},");
    sb.append("{\"title\":\"Skipped\",\"value\":\"").append(skipped).append("\"},");
    sb.append("{\"title\":\"Total\",\"value\":\"").append(total).append("\"}");
    sb.append("]}}");
    if (launchLink != null && !launchLink.isEmpty()) {
      sb.append(",{\"type\":\"TextBlock\",\"text\":\"[Open in Report Portal](")
          .append(escapeJson(launchLink)).append(")\",\"weight\":\"default\",\"size\":\"default\"}");
    }
    sb.append("]}}}]}");
    return sb.toString();
  }
}
