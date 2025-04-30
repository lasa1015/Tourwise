import React from 'react';
import { Box, Alert, Button } from '@mui/material';
import { InfoRounded } from '@mui/icons-material';

interface AlertModalProps {
  open: boolean;
  onClose: () => void;
  title?: string;
  message: string;
  sx?: object; // 允许外部自定义样式
}

const AlertModal: React.FC<AlertModalProps> = ({ open, onClose, title, message, sx }) => {
  if (!open) return null;

  return (
    <Box
      sx={{
        position: 'fixed',
        top: 0,
        left: 0,
        width: '100vw',
        height: '100vh',
        backgroundColor: 'rgba(0, 0, 0, 0.25)', // 灰色背景，视觉更聚焦
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        zIndex: 1300,
      }}
    >
      <Alert
        severity="info"
        icon={<InfoRounded />}
        sx={{
          width: '350px',
          backgroundColor: 'white',
          boxShadow: '0 4px 16px rgba(0, 0, 0, 0.15)', // 更柔和阴影
          color: 'gray',
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
          textAlign: 'center',
          padding: '16px',
          ...sx, // 允许外部传样式
        }}
      >
        {title && <strong style={{ marginBottom: 8 }}>{title}</strong>}
        <div style={{ padding: '5px' }}>{message}</div>

        <Box sx={{ mt: 2, display: 'flex', justifyContent: 'center', width: '100%' }}>
          <Button
            variant="contained"
            sx={{
              borderRadius: 20,
              color: 'white',
              px: 3,
              boxShadow: 0,
              '&:hover': { boxShadow: 0 },
              '&:active': { boxShadow: 0 },
            }}
            onClick={onClose}
          >
            OK
          </Button>
        </Box>
      </Alert>
    </Box>
  );
};

export default AlertModal;
