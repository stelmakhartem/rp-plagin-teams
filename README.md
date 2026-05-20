# Report Portal Plugin for Microsoft Teams

Plugin for Report Portal that sends test launch results to Microsoft Teams channels via Incoming Webhook.

## Features

- Integration with Microsoft Teams Incoming Webhooks
- Adaptive Card notifications with launch statistics (passed, failed, skipped, total)
- Test connection command to verify webhook configuration
- UI settings form for webhook URL configuration

## Setup

### 1. Create Teams Incoming Webhook

1. In Microsoft Teams: open the channel where you want notifications
2. Click ⋯ (more options) → Connectors → Incoming Webhook
3. Add the webhook, give it a name (e.g. "Report Portal")
4. Copy the generated webhook URL

### 2. Install Plugin in Report Portal

1. Build the plugin: `gradlew build`
2. Plugin ZIP will be at `build/plugins/plugin-teams_integration-{version}.zip`
3. Upload the plugin in Report Portal: Admin → Plugins → Upload

### 3. Configure Integration

1. Go to Project Settings → Integrations
2. Find "Teams Plugin" and add integration
3. Set **webhookURL** parameter to your Teams webhook URL
4. Use "Test connection" to verify (sends a test notification)

## Launch Finished Notifications (LaunchFinishedNotificationEvent)

For automatic notifications when a launch finishes, add `service-api` dependency and the LaunchFinishedEventHandler. Report Portal 5.15.x publishes `LaunchFinishedNotificationEvent` when a launch completes.

**To add LaunchFinishedNotificationEvent support:**
1. Add dependency in `build.gradle`: `implementation 'com.epam.reportportal:service-api:5.13.2'`
2. Add `LaunchFinishedEventHandler` and `SenderCaseMatcher` (see [Slack plugin](https://github.com/reportportal/plugin-slack) as reference)
3. Register the listener in `TemplatePluginExtensionWithListener.initListeners()`
4. Configure in Report Portal: Project → Notifications → Add Teams rule (type: teams_integration, webhookURL in options)

## Build

- **Java**: JDK 11+
- **Node**: 20+ (for UI build)

```bash
./gradlew build
```

## Dependencies

- Report Portal plugin-api 5.14.1
- Report Portal commons-dao (for LaunchRepository when event support is added)
- Spring Boot 3.4.x

## License

Apache 2.0
