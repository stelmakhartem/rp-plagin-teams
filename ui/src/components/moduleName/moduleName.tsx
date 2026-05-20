import { IntegrationSettingsInterface } from 'extensionProps/common';
import { FC } from 'react';
import { BaseFieldProps, Field } from 'redux-form';

// #region agent log
const DEBUG_LOG = (msg: string, data: Record<string, unknown>) => {
  fetch('http://127.0.0.1:7632/ingest/6bcf9a31-bc2f-4ef2-b580-659f177ab832', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json', 'X-Debug-Session-Id': 'a7a39e' },
    body: JSON.stringify({
      sessionId: 'a7a39e',
      location: 'moduleName.tsx',
      message: msg,
      data,
      timestamp: Date.now(),
    }),
  }).catch(() => {});
};
// #endregion

const TeamsFormFields: FC<BaseFieldProps> = () => (
  <div>
    <p>
      Configure Microsoft Teams integration: Create an Incoming Webhook in Teams (Channel →
      Connectors → Incoming Webhook), copy the webhook URL, and save it below.
    </p>
    <div>
      {/* eslint-disable-next-line jsx-a11y/label-has-associated-control */}
      <label htmlFor="teams-webhookURL">
        Webhook URL
        <Field
          name="webhookURL"
          component="input"
          type="url"
          placeholder="https://outlook.office.com/webhook/..."
          id="teams-webhookURL"
        />
      </label>
    </div>
    <p>When a launch finishes, results will be sent to the configured Teams channel.</p>
  </div>
);

export const ModuleName = ({
  data,
  components,
  onUpdate,
  goToPreviousPage,
  isGlobal,
}: IntegrationSettingsInterface) => {
  const IntegrationSettings = components?.IntegrationSettings;
  const params = data?.integrationParameters as unknown as Record<string, string> | undefined;
  const isEmptyConfiguration = !params?.webhookURL;
  // #region agent log
  DEBUG_LOG('ModuleName rendered', {
    hypothesisId: 'A,C',
    hasComponents: !!components,
    hasIntegrationSettings: !!IntegrationSettings,
    isGlobal,
    dataKeys: data ? Object.keys(data) : [],
    hasIntegrationParams: !!data?.integrationParameters,
  });
  // #endregion

  if (!IntegrationSettings) {
    return (
      <div>
        <h3>Teams Plugin</h3>
        <p>
          Configure webhook URL in integration parameters (webhookURL). Create Incoming Webhook in
          Teams: Channel → Connectors → Incoming Webhook.
        </p>
      </div>
    );
  }

  return (
    <IntegrationSettings
      data={data}
      goToPreviousPage={goToPreviousPage}
      onUpdate={onUpdate}
      editAuthConfig={{
        content: <span>Teams Webhook Configuration</span>,
        onClick: () => {},
      }}
      isGlobal={isGlobal}
      formFieldsComponent={TeamsFormFields}
      formKey="teams-integration"
      isEmptyConfiguration={isEmptyConfiguration}
    />
  );
};
