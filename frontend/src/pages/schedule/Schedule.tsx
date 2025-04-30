import React, { useContext, useState, useEffect, useRef } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import moment from "moment";
import {
  Box,
  Button,
  Stack,
  Typography,
} from "@mui/material";

import { NAVBAR_HEIGHT, LEFT_WIDTH } from "../../utils/constants";
import Btn_List from "../../components/list/Btn_List";
import Btn_Close_Left from "../../components/Btn_Close_Left";
import List from "../../components/list/List";
import Map_Schedule from "../../components/schedule/Map_Schedule";
import ScheduleCard from "../../components/schedule/ScheduleCard";
import WeatherComponent from "../../components/schedule/WeatherComponent";
import SaveButton from "../../components/schedule/SaveButton";
import AlertModal from "../../components/AlertModal";

import { ListContext } from "../../contexts/ListContext";
import { useAuth } from "../../contexts/AuthContext";
import { useLastUpdatedContext } from "../../contexts/LastUpdatedContext";
import {
  useUpdateLeftWidth,
  useUpdateNavbarHeight,
} from "../../utils/useResponsiveSizes";

const Schedule: React.FC = () => {
  /* ---------- context ---------- */
  const {
    showList,
    toggleList,
    closeList,
    isLeftPanelVisible,
    toggleLeftPanel,
    planData,
    selectedDates,
  } = useContext(ListContext);

  const navigate  = useNavigate();
  const location  = useLocation();
  const { isLoggedIn } = useAuth();
  const { setLastUpdated } = useLastUpdatedContext();

  /* ---------- local state ---------- */
  const [currentDate, setCurrentDate] = useState<string | null>(null);
  const [events, setEvents]           = useState<any[]>([]);
  const [weather, setWeather]         = useState<any | null>(null);
  const [loadingWeather, setLoadingWeather] = useState(false);
  const [busynessData, setBusynessData] = useState<any | null>(null);
  const [selectedTime, setSelectedTime] = useState<string | null>(null);
  const [selectedEvent, setSelectedEvent] = useState<any | null>(null);
  const [alertOpen, setAlertOpen]     = useState(false);
  const [alertMessage, setAlertMessage] = useState("");

  /* ---------- helpers ---------- */
  const prevDatesRef = useRef<
    [moment.Moment | null, moment.Moment | null] | null
  >(null);

  useUpdateLeftWidth();
  useUpdateNavbarHeight();

  /* ---------- 路由守卫：日期无效或改变就跳 /spots ---------- */
  useEffect(() => {
    const datesInvalid =
      !selectedDates || !selectedDates[0] || !selectedDates[1];

    const datesChanged = (() => {
      const prev = prevDatesRef.current;
      if (!prev || !selectedDates) return false;
      const [p0, p1] = prev;
      const [c0, c1] = selectedDates;
      return !(p0?.isSame(c0, "day") && p1?.isSame(c1, "day"));
    })();

    if (location.pathname === "/schedule" && (datesInvalid || datesChanged)) {
      navigate("/spots", { replace: true });
    }

    prevDatesRef.current = selectedDates;
  }, [selectedDates, location.pathname, navigate]);

  /* ---------- planData → currentDate & events ---------- */
  useEffect(() => {
    if (!planData || Object.keys(planData).length === 0) {
      setCurrentDate(null);
      setEvents([]);
      return;
    }
    const first = Object.keys(planData)[0];
    setCurrentDate(first);
    setEvents(planData[first] || []);
    fetchWeather(first);
  }, [planData]);

  /* ---------- currentDate → events & weather ---------- */
  useEffect(() => {
    if (!currentDate || !planData) return;
    setEvents(planData[currentDate] || []);
    fetchWeather(currentDate);
    setSelectedEvent(null);
  }, [currentDate, planData]);

  /* ---------- events → 选中第一个 startTime ---------- */
  useEffect(() => {
    if (events.length === 0) return;
    handleStartTimeClick(events[0].startTime);
  }, [events]);

  const formatDayOfWeek = (date: string) => moment(date).format("ddd");

  const fetchWeather = async (date: string) => {
    setLoadingWeather(true);
    try {
      const res  = await fetch(`/api/weather/by_date/${date}`);
      const data = await res.json();
      setWeather(data[0]);
    } catch (err) {
      console.error("Failed to fetch weather", err);
    }
    setLoadingWeather(false);
  };

  const fetchBusynessData = async (date: string) => {
    try {
      const res = await fetch(
        `/api/busyness/predict_all_sort_by_date_range?startDate=${date}&endDate=${date}`,
        {
          method : "POST",
          headers: { "Content-Type": "application/json" },
          body   : JSON.stringify({ startDate: date, endDate: date }),
        }
      );
      if (!res.ok) throw new Error(await res.text());
      const data = await res.json();
      setBusynessData(data);
    } catch (err) {
      console.error("Failed to fetch busyness", err);
    }
  };

  const handleDateChange = (date: string) => setCurrentDate(date);

  const handleStartTimeClick = async (startTime: string) => {
    setSelectedTime(startTime);
    await fetchBusynessData(moment(startTime).format("YYYY-MM-DD"));
  };


  const triggerAvatarClick = () => {
    const btn = document.getElementById("avatarButton");
    if (btn) btn.click();
  };

  const handleOpenAlert  = (msg: string) => {
    setAlertMessage(msg);
    setAlertOpen(true);
  };
  const handleCloseAlert = () => setAlertOpen(false);

  const handleSavePlan = async () => {
    if (!planData || !selectedDates || !selectedDates[0] || !selectedDates[1]) {
      console.error("Plan data or dates missing");
      return;
    }
    if (localStorage.getItem("planData") === JSON.stringify(planData)) {
      handleOpenAlert("Plan already saved !");
      return;
    }
    try {
      const payload = {
        planData,
        startDate: selectedDates[0].format("YYYY-MM-DD"),
        endDate:   selectedDates[1].format("YYYY-MM-DD"),
        token:     localStorage.getItem("token"),
      };
      const res = await fetch("/api/itinerary/save", {
        method : "POST",
        headers: { "Content-Type": "application/json" },
        body   : JSON.stringify(payload),
      });
      if (!res.ok) throw new Error(await res.text());
      localStorage.setItem("planData", JSON.stringify(planData));
      setLastUpdated(new Date());
      handleOpenAlert("Plan has been saved in your account.");
    } catch (err) {
      console.error("Save plan failed", err);
    }
  };

  const handleSaveClick = () => {
    if (!isLoggedIn) triggerAvatarClick();
    else handleSavePlan();
  };

  /* ---------- guard ---------- */
  if (!planData || !currentDate) {
    return (
      <div style={{ paddingTop: NAVBAR_HEIGHT + 20, textAlign: "center" }}>
        No plan available. Please pick dates on the Spots page and generate a
        schedule.
      </div>
    );
  }

  /* ---------- render ---------- */
  return (
    <div style={{ display: "flex", flexDirection: "column" }}>
      {isLeftPanelVisible && (
        <div
          style={{
            width: LEFT_WIDTH,
            padding: "0.5vw 2vw 0 2vw",
            marginTop: NAVBAR_HEIGHT,
            height: `calc(100vh - ${NAVBAR_HEIGHT})`,
            display: "flex",
            flexDirection: "column",
            background: "white",
            zIndex: 5,
          }}
        >
          {/* header 日期 + 天气 */}
          <Box mb={0}>
            <Stack direction="row" justifyContent="space-between" alignItems="center">
              <Typography variant="h6" sx={{ fontFamily: "Lexend", fontSize: { xs: 14, sm: 16, md: 20 } }}>
                {moment(currentDate).format("Do MMMM YYYY, dddd")}
              </Typography>
              <WeatherComponent weather={weather} loadingWeather={loadingWeather} />
            </Stack>
          </Box>

          {/* 日期按钮 + 保存 */}
          <Stack direction="row" justifyContent="space-between">
          <Stack direction="row" spacing={1} mb={3}>

            {Object.keys(planData).map((date) => (
              <Button
                key={date}
                onClick={() => handleDateChange(date)}
                style={{
                  backgroundColor: date === currentDate ? "orange" : "#f8f8f8",
                  color: date === currentDate ? "#fff" : "#888",
                  display: "flex",
                  flexDirection: "column",
                  alignItems: "center",
                  justifyContent: "center",
                }}
                sx={{
                  minWidth: { xs: "43px", sm: "55px", md: "60px" },
                  minHeight: { xs: "40px", sm: "60px", md: "65px" },
                  padding: { xs: "10px 8px", sm: "8px 10px", md: "8px 16px" },
                  borderRadius: { xs: "12px", sm: "18px", md: "20px" },
                }}
              >
                <Typography
                  variant="caption"
                  style={{
                    fontWeight: "normal",
                    fontFamily: "Lexend",
                    lineHeight: 1,
                  }}
                >
                  {formatDayOfWeek(date)}
                </Typography>
                <Typography
                  variant="body1"
                  style={{
                    fontWeight: 400,
                    fontFamily: "Lexend",
                    lineHeight: 1,
                  }}
                  sx={{ fontSize: { xs: "1.3em", sm: "1.4em", md: "1.5em" } }}
                >
                  {moment(date).format("DD")}
                </Typography>
              </Button>
            ))}
            
                   
          </Stack>

            <SaveButton isLoggedIn={isLoggedIn} handleSaveClick={handleSaveClick} />
          
          
          </Stack>

          {/* 行程卡片列表 */}
          <div style={{ flexGrow: 1, overflowY: "scroll" }}>
            {events.map((item, idx) => (
              <ScheduleCard
                key={item.id}
                {...item}
                index={idx + 1}
                highlightedStartTime={selectedTime}
                onStartTimeClick={handleStartTimeClick}
              />
            ))}
          </div>

          <AlertModal
            open={alertOpen}
            onClose={handleCloseAlert}
            title="Information"
            message={alertMessage}
          />
        </div>
      )}

      {/* 地图区域 */}
      <div
        style={{
          position: "fixed",
          top: NAVBAR_HEIGHT,
          right: 0,
          width: isLeftPanelVisible ? `calc(100% - ${LEFT_WIDTH})` : "100%",
          height: `calc(100vh - ${NAVBAR_HEIGHT})`,
        }}
      >
        <Map_Schedule
          events={events}
          busynessData={busynessData}
          selectedTime={selectedTime}
          selectedEvent={selectedEvent}
          setSelectedEvent={setSelectedEvent}
          showList={showList}
          isLeftPanelVisible={isLeftPanelVisible}
        />
      </div>

      {/* 浮动按钮 */}
      <Btn_List onClick={toggleList} />
      {showList && <List onClose={closeList} />}
      <Btn_Close_Left onClick={toggleLeftPanel} />
    </div>
  );
};

export default Schedule;
