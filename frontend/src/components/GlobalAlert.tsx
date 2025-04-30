import React from 'react';
import AlertModal from './AlertModal';
import { useAlertContext } from '../contexts/AlertContext';

const GlobalAlert = () => {
  const { showMissingDatesAlert, closeMissingDatesAlert } = useAlertContext();

  return (
    <AlertModal
      open={showMissingDatesAlert}
      onClose={closeMissingDatesAlert}
      message="Please set your trip dates at the top before adding items to your list."
      sx={{
        boxShadow: '0 2px 12px rgba(0, 0, 0, 0.2)',
        maxWidth: '400px',
        mx: 'auto',
      }}
    />
  );
};

export default GlobalAlert;
