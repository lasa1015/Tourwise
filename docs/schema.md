## Schema Design



#### Table: `busyness_prediction`

This table stores predicted busyness index values for different taxi zones at specific time slots.
 It is primarily used to support itinerary optimization and visualization of crowdedness forecasts in the application.

| Column Name  | Data Type   | Description                                   |
| ------------ | ----------- | --------------------------------------------- |
| `id`         | `bigint`    | Primary key for each prediction entry.        |
| `busyness`   | `real`      | Predicted busyness index (range: 1–100).      |
| `created_at` | `timestamp` | Record creation timestamp.                    |
| `datetime`   | `timestamp` | The specific time this prediction applies to. |
| `taxi_zone`  | `integer`   | ID of the taxi zone being predicted.          |
| `updated_at` | `timestamp` | Record last update timestamp.                 |



#### Table: `daily_forecast_data`

This table stores processed daily weather forecast data collected from OpenWeather. It includes detailed metrics like temperature, humidity, precipitation, wind, and weather conditions, used in downstream prediction and planning features.

| Column Name           | Data Type    | Description                                              |
| --------------------- | ------------ | -------------------------------------------------------- |
| `id`                  | uuid         | Primary key for each weather record.                     |
| `dt`                  | int8         | Forecast timestamp in UNIX format.                       |
| `fetch_time`          | timestamp    | Time when the data was fetched.                          |
| `humidity`            | float8       | Humidity percentage.                                     |
| `pressure`            | float8       | Atmospheric pressure in hPa.                             |
| `rain`                | float8       | Rain volume (mm).                                        |
| `snow`                | float8       | Snow volume (mm).                                        |
| `speed`               | float8       | Wind speed (km/h).                                       |
| `temp_day`            | float8       | Daytime temperature (°C).                                |
| `weather_description` | varchar(255) | Textual description of the weather (e.g., "light rain"). |
| `weather_icon`        | varchar(255) | Icon code from OpenWeather.                              |
| `weather_main`        | varchar(255) | Main weather condition (e.g., "Rain", "Clear").          |
| `clouds`              | float8       | Cloud coverage percentage.                               |
| `feel_eve`            | float8       | Evening "feels like" temperature.                        |
| `feel_morn`           | float8       | Morning "feels like" temperature.                        |
| `feel_night`          | float8       | Night "feels like" temperature.                          |
| `sunrise`             | int8         | Sunrise time in UNIX format.                             |
| `sunset`              | int8         | Sunset time in UNIX format.                              |
| `temp_eve`            | float8       | Evening temperature.                                     |
| `temp_max`            | float8       | Maximum temperature.                                     |
| `temp_min`            | float8       | Minimum temperature.                                     |
| `temp_morn`           | float8       | Morning temperature.                                     |
| `temp_night`          | float8       | Night temperature.                                       |
| `weather_id`          | int4         | OpenWeather weather condition code.                      |
| `deg`                 | float8       | Wind direction in degrees.                               |



#### Table: `events`

This table stores information about local events collected from the Yelp API, including location, time, category, and engagement metrics. It is used to help users discover and select activities for their travel itineraries.



| Column Name         | Data Type     | Description                                            |
| ------------------- | ------------- | ------------------------------------------------------ |
| `id`                | uuid          | Unique identifier for each event.                      |
| `address`           | varchar(255)  | Street address of the event.                           |
| `attending_count`   | int4          | Number of people marked as attending.                  |
| `category`          | varchar(255)  | Main category of the event.                            |
| `city`              | varchar(255)  | City where the event is located.                       |
| `combined_category` | varchar(255)  | Combined category string (for broader classification). |
| `description`       | varchar(2000) | Detailed textual description of the event.             |
| `event_site_url`    | varchar(1000) | External URL to the event’s webpage.                   |
| `fetch_time`        | timestamp     | Timestamp of when this event data was fetched.         |
| `image_url`         | varchar(1000) | URL to the event’s image or poster.                    |
| `interested_count`  | int4          | Number of people interested in the event.              |
| `is_canceled`       | bool          | Whether the event is marked as canceled.               |
| `is_free`           | bool          | Indicates whether the event is free to attend.         |
| `is_official`       | bool          | Whether the event is official (from Yelp metadata).    |
| `latitude`          | float8        | Latitude coordinate of the event location.             |
| `longitude`         | float8        | Longitude coordinate of the event location.            |
| `name`              | varchar(512)  | Name of the event.                                     |
| `state`             | varchar(255)  | State in which the event is located.                   |
| `time_end`          | varchar(255)  | End time of the event (string format).                 |
| `time_start`        | varchar(255)  | Start time of the event (string format).               |
| `zip_code`          | varchar(255)  | ZIP code of the event’s location.                      |
| `version`           | int8          | Internal version number for tracking updates.          |



#### Table: `itinerary_saved`

This table stores metadata for user-saved itineraries, including the associated user and the planned date range.
 It acts as the parent table for detailed itinerary items saved in `itinerary_saved_items`.

| Column Name  | Data Type | Description                                    |
| ------------ | --------- | ---------------------------------------------- |
| `id`         | `bigint`  | Primary key of the saved itinerary.            |
| `user_id`    | `bigint`  | Foreign key referencing the user who saved it. |
| `start_date` | `date`    | Start date of the itinerary.                   |
| `end_date`   | `date`    | End date of the itinerary.                     |



#### Table: `itinerary_saved_items`

This table stores detailed entries for each item included in a saved itinerary. Each row represents either an event or a weather-related activity, along with its scheduled start and end time. It supports mixed itinerary composition and is referenced by the main `itinerary_saved` table.

| Column Name    | Data Type | Description                                                  |
| -------------- | --------- | ------------------------------------------------------------ |
| `id`           | bigserial | Primary key. Unique ID for each saved item entry.            |
| `itinerary_id` | int8      | Foreign key referencing the itinerary this item belongs to.  |
| `item_id`      | int4      | Internal ID for the item (used for sorting or identifying non-event entries). |
| `event_id`     | uuid      | Optional. If the item is an event, this stores the corresponding `events.id`. |
| `is_event`     | bool      | Whether this item is an event (`true`) or another type (e.g. weather segment). |
| `start_time`   | timestamp | Scheduled start time for the item.                           |
| `end_time`     | timestamp | Scheduled end time for the item.                             |



#### Table: `users`

This table stores user authentication and account metadata, including login credentials and registration timestamps. It is used to manage login, authorization, and user-specific itinerary data.

| Column Name  | Data Type    | Description                                                  |
| ------------ | ------------ | ------------------------------------------------------------ |
| `id`         | bigserial    | Primary key. Auto-incremented unique ID for each user.       |
| `email`      | varchar(255) | User's email address. Must be unique.                        |
| `password`   | varchar(255) | Hashed password string.                                      |
| `created_at` | timestamp    | Timestamp of account creation. Defaults to `CURRENT_TIMESTAMP`. |
| `salt`       | varchar(255) | Random salt value used for hashing the password.             |



#### Table: `weather_data`

This table stores detailed weather condition records fetched from the OpenWeather API at various times. It supports itinerary generation, event filtering, and predictive analytics by providing granular environmental data for specific timestamps.



| Column Name           | Data Type    | Description                                                 |
| --------------------- | ------------ | ----------------------------------------------------------- |
| `id`                  | uuid         | Primary key. Unique identifier for the weather record.      |
| `all_clouds`          | int4         | Cloudiness percentage (0–100).                              |
| `city`                | int4         | Internal city code used in OpenWeather API.                 |
| `dt`                  | int8         | UNIX timestamp of the weather record.                       |
| `fetch_time`          | timestamp    | When this record was retrieved from the API.                |
| `feels_like`          | float8       | Perceived temperature considering humidity and wind.        |
| `humidity`            | int4         | Humidity percentage.                                        |
| `pressure`            | int4         | Atmospheric pressure in hPa.                                |
| `temperature`         | float8       | Actual temperature.                                         |
| `temp_max`            | float8       | Maximum temperature forecasted.                             |
| `temp_min`            | float8       | Minimum temperature forecasted.                             |
| `rain_1h`             | float8       | Rain volume for the last 1 hour, in mm.                     |
| `rain_3h`             | float8       | Rain volume for the last 3 hours, in mm.                    |
| `snow_1h`             | float8       | Snow volume for the last 1 hour, in mm.                     |
| `snow_3h`             | float8       | Snow volume for the last 3 hours, in mm.                    |
| `sunrise`             | int8         | UNIX timestamp of sunrise time.                             |
| `sunset`              | int8         | UNIX timestamp of sunset time.                              |
| `timezone`            | int4         | Timezone offset in seconds from UTC.                        |
| `visibility`          | int4         | Visibility distance in meters.                              |
| `weather_description` | varchar(255) | Text description of weather condition (e.g., "light rain"). |
| `weather_icon`        | varchar(255) | Icon code provided by OpenWeather for visual display.       |
| `weather_id`          | int4         | OpenWeather condition ID.                                   |
| `weather_main`        | varchar(255) | General category of weather (e.g., "Rain", "Clear").        |
| `wind_deg`            | int4         | Wind direction in degrees.                                  |
| `wind_gust`           | float8       | Wind gust speed in m/s.                                     |
| `wind_speed`          | float8       | Wind speed in m/s.                                          |