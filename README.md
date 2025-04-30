## Tourwise - Smart Tourist Guide for New York City 

### Live Demo

[http://tourwise.site](http://tourwise.site)

------

###  Project Overview

The Smart Tourist Guide for New York City is an innovative plan designed to enhance the travel experience for tourists by providing real-time information about Points of Interest (POI), focusing on recommending times to visit when these locations are least crowded. This project leverages multiple datasets and advanced technologies to deliver personalized, data-driven recommendations, ensuring tourists can explore the city efficiently and comfortably.



------

### Tech Stack

- **UI Design**:  Figma, Photoshop
  
- **Frontend**:  React + TypeScript , Google Maps Platform, Google Charts, Google Fonts, Material UI, Ant Design
  
- **Backend**:  Spring Boot (Java), RESTful APIs

- **Scraper**:  Spring Boot microservice (Java)

- **Database**: AWS RDS (PostgreSQL 17.4)

- **Model Training**:  Python (XGBoost)

- **Build Tool**:  Gradle (used in backend and scraper)

- **Deployment & Infrastructure**:  Docker, Docker Compose, Nginx, GitHub Actions,  Hetzner Cloud (CX22), Namecheap


------

###  System Architecture

![image-20250429204046910](docs/images/system_arch.png)

- **Frontend Service** (React): 
- **Backend Service** (Spring Boot): 
- **Scraper Service** (Spring Boot): 

Each service runs in its own Docker container, orchestrated with Docker Compose for simplified deployment, scaling, and maintenance

In addition to the services, the platform relies on a centralized database component:

- **AWS RDS MySQL**: Provides reliable and scalable persistent storage for all application data.

  

------

### Key Features

#### ● Plan Trips in Manhattan, NYC

Users can plan their travel within the next 30 days in Manhattan, New York City. On the landing page, users can select a start and end date for their trip.

<img src="docs/images/landing2.png" alt="Image with shadow" style="box-shadow: 2px 2px 10px rgba(0, 0, 0, 0.3); border: none;">



#### ● Explore Popular Attractions in Manhattan

Browse nearly 200 well-known attractions in Manhattan. 

<img src="docs/images/attractions.png" alt="Image with shadow" style="box-shadow: 2px 2px 10px rgba(0, 0, 0, 0.3); border: none;">



Users can search for attractions via the sidebar, apply category filters, or sort by popularity, rating, and whether the attraction is free.

<img src="docs/images/filter.png" alt="Image with shadow" style="box-shadow: 2px 2px 10px rgba(0, 0, 0, 0.3); border: none;">



Clicking an attraction card reveals additional details.

<img src="docs/images/attraction_details.png" alt="Image with shadow" style="box-shadow: 2px 2px 10px rgba(0, 0, 0, 0.3); border: none;">



#### ●  View Upcoming Events in Manhattan

See events happening in the next 30 days. Events can be searched, filtered by type or whether they are free, and expanded to view full details.

<img src="docs/images/events.png" alt="Image with shadow" style="box-shadow: 2px 2px 10px rgba(0, 0, 0, 0.3); border: none;">



#### ● Interactive Map View

Users can collapse the sidebar to view all attractions or events directly on the map, along with Dublin's public bike stations.

<img src="docs/images/map.png" alt="Image with shadow" style="box-shadow: 2px 2px 10px rgba(0, 0, 0, 0.3); border: none;">



#### **● **Add Attractions and Events to Your Plan

Users can add their favorite attractions and events to a personal list. Items can be removed or cleared at any time.

<img src="docs/images/list.png" alt="Image with shadow" style="box-shadow: 2px 2px 10px rgba(0, 0, 0, 0.3); border: none;">



#### ● Generate Optimized Itinerary Using ML-Based Routing Algorithms

After clicking "Generate Plan," the system creates a personalized travel itinerary using intelligent routing algorithms that are designed based on machine learning models. These models predict the future busyness levels of different taxi zones, enabling the itinerary to optimize routes and timings to avoid congestion. Users can inspect predicted congestion levels across taxi zones and interact with charts for detailed hourly insights.

<img src="docs/images/schedule.png" alt="Image with shadow" style="box-shadow: 2px 2px 10px rgba(0, 0, 0, 0.3); border: none;">



#### ● Save Your Travel Plans

Users can register and log in to persistently save their travel plans.

<img src="docs/images/signup.png" alt="Image with shadow" style="box-shadow: 2px 2px 10px rgba(0, 0, 0, 0.3); border: none;">



#### ●  User Profile Management

In the user profile page, users can update their password and access previously saved travel plans.

<img src="docs/images/profile.png" alt="Image with shadow" style="box-shadow: 2px 2px 10px rgba(0, 0, 0, 0.3); border: none;">

<img src="docs/images/myplans.png" alt="Image with shadow" style="box-shadow: 2px 2px 10px rgba(0, 0, 0, 0.3); border: none;">



#### **● **Responsive Design for All Devices

The application is fully responsive and offers an optimal experience across all devices, including desktop, tablet (both orientations), and mobile.

![image-20250429184349487](docs/images/responsive.png)

------

