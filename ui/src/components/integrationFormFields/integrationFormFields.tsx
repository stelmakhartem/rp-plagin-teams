/* eslint-disable react/jsx-props-no-spreading */
import { IntegrationFormFieldsInterface } from 'extensionProps/common';
import { FC, useEffect } from 'react';
import { Field, WrappedFieldProps } from 'redux-form';

// #region agent log
const DEBUG_LOG = (msg: string, data: Record<string, unknown>) => {
  fetch('http://127.0.0.1:7632/ingest/6bcf9a31-bc2f-4ef2-b580-659f177ab832', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json', 'X-Debug-Session-Id': 'a7a39e' },
    body: JSON.stringify({
      sessionId: 'a7a39e',
      location: 'integrationFormFields.tsx',
      message: msg,
      data,
      timestamp: Date.now(),
    }),
  }).catch(() => {});
};
// #endregion

interface WebhookFieldProps extends WrappedFieldProps {
  components: IntegrationFormFieldsInterface['components'];
  disabled: boolean;
  updateMetaData: IntegrationFormFieldsInterface['updateMetaData'];
}

const WebhookFieldRender: FC<WebhookFieldProps> = ({
  input,
  meta,
  components,
  disabled,
  updateMetaData,
}) => {
  const { FieldElement, FieldText } = components;
  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    input.onChange(e);
    updateMetaData({ webhookURL: e.target.value });
  };

  const fieldElementProps = {
    input,
    meta,
    name: 'webhookURL',
    label: 'Webhook URL',
    description:
      'Create an Incoming Webhook in Teams (Channel → Connectors → Incoming Webhook), then paste the URL here.',
    disabled,
    isRequired: true,
  };

  const fieldTextProps = {
    value: input.value,
    onChange: handleChange,
    onBlur: input.onBlur,
    onFocus: input.onFocus,
  };

  return (
    <FieldElement {...fieldElementProps}>
      <FieldText {...fieldTextProps} />
    </FieldElement>
  );
};

const WebhookUrlField: FC<{
  components: IntegrationFormFieldsInterface['components'];
  disabled: boolean;
  updateMetaData: IntegrationFormFieldsInterface['updateMetaData'];
}> = ({ components, disabled, updateMetaData }) => (
  <Field
    name="webhookURL"
    component={WebhookFieldRender}
    props={{ components, disabled, updateMetaData }}
  />
);

export const IntegrationFormFields: FC<IntegrationFormFieldsInterface> = ({
  initialize,
  initialData,
  components,
  updateMetaData,
  disabled,
}) => {
  useEffect(() => {
    initialize(initialData);
  }, [initialize, initialData]);

  // #region agent log
  DEBUG_LOG('IntegrationFormFields rendered', {
    hypothesisId: 'B,D',
    hasFieldElement: !!components?.FieldElement,
    hasFieldText: !!components?.FieldText,
    hasComponents: !!components,
    disabled,
  });
  // #endregion

  if (!components?.FieldElement || !components?.FieldText) {
    return (
      <div>
        {/* prettier-ignore */}
        <p>
          Configure Microsoft Teams integration. Provide the webhook URL from Teams Incoming Webhook.
        </p>
        {/* eslint-disable-next-line jsx-a11y/label-has-associated-control */}
        <label htmlFor="teams-webhookURL">
          Webhook URL
          <Field
            name="webhookURL"
            component="input"
            type="url"
            placeholder="https://outlook.office.com/webhook/..."
            id="teams-webhookURL"
            disabled={disabled}
          />
        </label>
      </div>
    );
  }

  return (
    <div>
      {/* prettier-ignore */}
      <p>
        Configure Microsoft Teams integration: Create an Incoming Webhook in Teams (Channel →
        Connectors → Incoming Webhook), copy the webhook URL, and save it below.
      </p>
      <WebhookUrlField
        components={components}
        disabled={disabled}
        updateMetaData={updateMetaData}
      />
      <p>When a launch finishes, results will be sent to the configured Teams channel.</p>
    </div>
  );
};

export default IntegrationFormFields;
