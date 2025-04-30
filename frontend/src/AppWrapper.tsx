
import React, { useState } from 'react';
import { useLocation } from 'react-router-dom';
import { ThemeProvider } from '@mui/material/styles';
import theme from './utils/theme';
import 'antd/dist/antd.css';
import { ListProvider } from './contexts/ListContext';
import { AuthProvider } from './contexts/AuthContext';
import { LastUpdatedProvider } from './contexts/LastUpdatedContext';
import { AlertProvider } from './contexts/AlertContext';
import App from './App';
import GlobalAlert from './components/GlobalAlert';

const AppWrapperInner: React.FC = () => {
  const location = useLocation();
  const [selectedDates, setSelectedDates] = useState<[moment.Moment | null, moment.Moment | null] | null>(null);

  // 包装 setSelectedDates，防止无意间跳转页面
  const handleDateChange = (dates: [moment.Moment | null, moment.Moment | null] | null) => {
    setSelectedDates(dates); // 只更新状态，不触发页面跳转
  };

  return (
    <ThemeProvider theme={theme}>
      <AuthProvider>
        <AlertProvider>
          <LastUpdatedProvider>
            <ListProvider>
              <App
                selectedDates={selectedDates}
                onDateChange={handleDateChange}
                pathname={location.pathname}
              />
              <GlobalAlert />
            </ListProvider>
          </LastUpdatedProvider>
        </AlertProvider>
      </AuthProvider>
    </ThemeProvider>
  );
};

export default AppWrapperInner;