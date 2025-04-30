import React from 'react';
import { Routes, Route } from 'react-router-dom';
import Header from './components/Header';
import HeaderHome from './components/Header_Home';
import HomePage from './pages/home/Home';
import Spots from './pages/spots/Spots';
import Events from './pages/events/Events';
import Schedule from './pages/schedule/Schedule';
import About from './pages/about/About';
import Dashboard from './pages/user/User';
import './App.css';

// Temporarily disable console log, warn and error
console.log = () => {};
console.warn = () => {};
console.error = () => {};


interface AppProps {
  selectedDates: [moment.Moment | null, moment.Moment | null] | null;
  onDateChange: (dates: [moment.Moment | null, moment.Moment | null] | null) => void;
  pathname: string;
}

const App: React.FC<AppProps> = ({ selectedDates, onDateChange, pathname }) => {
  const renderHeader = () => {
    if (pathname === '/' || pathname === '/about') {
      return <HeaderHome onDateChange={onDateChange} />;
    } else {
      return <Header onDateChange={onDateChange} />;
    }
  };

  return (
    <div className="app-container">
      {renderHeader()}
      <Routes>
        <Route path="/" element={<HomePage onDateChange={onDateChange} />} />
        <Route path="/spots/*" element={<Spots selectedDates={selectedDates} />} />
        <Route path="/events" element={<Events selectedDates={selectedDates} />} />
        <Route path="/schedule" element={<Schedule />} />
        <Route path="/about" element={<About />} />
        <Route path="/dashboard" element={<Dashboard />} />
      </Routes>
    </div>
  );
};

export default App;