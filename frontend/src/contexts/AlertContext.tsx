import React, { createContext, useState, ReactNode, useContext } from 'react';

interface AlertContextProps {
  showMissingDatesAlert: boolean;
  openMissingDatesAlert: () => void;
  closeMissingDatesAlert: () => void;
}

const AlertContext = createContext<AlertContextProps | undefined>(undefined);

export const AlertProvider = ({ children }: { children: ReactNode }) => {
  const [showMissingDatesAlert, setShowMissingDatesAlert] = useState(false);

  const openMissingDatesAlert = () => setShowMissingDatesAlert(true);
  const closeMissingDatesAlert = () => setShowMissingDatesAlert(false);

  return (
    <AlertContext.Provider value={{ showMissingDatesAlert, openMissingDatesAlert, closeMissingDatesAlert }}>
      {children}
    </AlertContext.Provider>
  );
};

export const useAlertContext = () => {
  const context = useContext(AlertContext);
  if (!context) {
    throw new Error('useAlertContext must be used within an AlertProvider');
  }
  return context;
};
