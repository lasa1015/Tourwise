import React, { useContext } from 'react';
import Map from '../../components/events/Map_Events';
import './schedule.css';
import { LEFT_PADDING, LEFT_WIDTH, NAVBAR_HEIGHT } from '../../constants';
import Btn_List from '../../components/list/Btn_List';
import List from '../../components/list/List';
import { ListContext } from '../../contexts/ListContext';
import Btn_Close_Left from '../../components/Btn_Close_Left';
import ScheduleCard from '../../components/schedule/ScheduleCard'; // 导入 ScheduleCard 组件

const Schedule: React.FC = () => {
  const { showList, toggleList, closeList, isLeftPanelVisible, toggleLeftPanel, planData } = useContext(ListContext);

  return (
    <div className="schedule" style={{ display: 'flex' }}>
      {isLeftPanelVisible && (
        <div
          className="left"
          style={{
            width: LEFT_WIDTH,
            padding: LEFT_PADDING,
            marginTop: NAVBAR_HEIGHT,
            height: `calc(100vh - ${NAVBAR_HEIGHT})`,
            overflowY: 'auto',
          }}
        >
          {/* <h2>Plan</h2> */}
          {planData && planData.map((item) => (
            <ScheduleCard
              key={item.id}
              id={item.id}
              name={item.name}
              startTime={item.startTime}
              endTime={item.endTime}
              latitude={item.latitude}
              longitude={item.longitude}
              busyness={item.busyness}
              event={item.event}
            />
          ))}
        </div>
      )}
      <div className="map" style={{ position: 'fixed', top: NAVBAR_HEIGHT, right: 0, width: isLeftPanelVisible ? `calc(100% - ${LEFT_WIDTH})` : '100%', height: `calc(100vh - ${NAVBAR_HEIGHT})` }}>
        <Map events={[]} />
      </div>

      <Btn_List onClick={toggleList} />
      {showList && <List onClose={closeList} />}

      <Btn_Close_Left onClick={toggleLeftPanel} />
    </div>
  );
};

export default Schedule;
