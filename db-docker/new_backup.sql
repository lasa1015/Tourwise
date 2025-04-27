--
-- PostgreSQL database dump
--

-- Dumped from database version 17.4 (Debian 17.4-1.pgdg120+2)
-- Dumped by pg_dump version 17.4

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: daily_forecast_data; Type: TABLE; Schema: public; Owner: myuser
--

CREATE TABLE public.daily_forecast_data (
    id uuid NOT NULL,
    dt bigint NOT NULL,
    fetch_time timestamp(6) without time zone,
    humidity double precision NOT NULL,
    pressure double precision NOT NULL,
    rain double precision NOT NULL,
    snow double precision NOT NULL,
    speed double precision NOT NULL,
    temp_day double precision,
    weather_description character varying(255),
    weather_icon character varying(255),
    weather_main character varying(255),
    clouds double precision,
    feel_day double precision,
    feel_eve double precision,
    feel_morn double precision,
    feel_night double precision,
    sunrise bigint,
    sunset bigint,
    temp_eve double precision,
    temp_max double precision,
    temp_min double precision,
    temp_morn double precision,
    temp_night double precision,
    weather_id integer,
    deg double precision
);


ALTER TABLE public.daily_forecast_data OWNER TO myuser;

--
-- Name: events; Type: TABLE; Schema: public; Owner: myuser
--

CREATE TABLE public.events (
    id uuid NOT NULL,
    address character varying(255),
    attending_count integer,
    category character varying(255),
    city character varying(255),
    combined_category character varying(255),
    description character varying(2000),
    event_site_url character varying(1000),
    fetch_time timestamp(6) without time zone,
    image_url character varying(1000),
    interested_count integer,
    is_canceled boolean,
    is_free boolean,
    is_official boolean,
    latitude double precision,
    longitude double precision,
    name character varying(512),
    state character varying(255),
    time_end character varying(255),
    time_start character varying(255),
    zip_code character varying(255),
    version bigint
);


ALTER TABLE public.events OWNER TO myuser;

--
-- Name: itinerary_saved; Type: TABLE; Schema: public; Owner: myuser
--

CREATE TABLE public.itinerary_saved (
    id bigint NOT NULL,
    user_id bigint NOT NULL,
    start_date date NOT NULL,
    end_date date NOT NULL
);


ALTER TABLE public.itinerary_saved OWNER TO myuser;

--
-- Name: itinerary_saved_id_seq; Type: SEQUENCE; Schema: public; Owner: myuser
--

CREATE SEQUENCE public.itinerary_saved_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.itinerary_saved_id_seq OWNER TO myuser;

--
-- Name: itinerary_saved_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: myuser
--

ALTER SEQUENCE public.itinerary_saved_id_seq OWNED BY public.itinerary_saved.id;


--
-- Name: itinerary_saved_items; Type: TABLE; Schema: public; Owner: myuser
--

CREATE TABLE public.itinerary_saved_items (
    id bigint NOT NULL,
    itinerary_id bigint NOT NULL,
    item_id integer,
    event_id uuid,
    is_event boolean NOT NULL,
    start_time timestamp(6) without time zone NOT NULL,
    end_time timestamp(6) without time zone NOT NULL,
    CONSTRAINT itinerary_saved_items_check CHECK ((((is_event = true) AND (event_id IS NOT NULL) AND (item_id IS NULL)) OR ((is_event = false) AND (item_id IS NOT NULL) AND (event_id IS NULL))))
);


ALTER TABLE public.itinerary_saved_items OWNER TO myuser;

--
-- Name: itinerary_saved_items_id_seq; Type: SEQUENCE; Schema: public; Owner: myuser
--

CREATE SEQUENCE public.itinerary_saved_items_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.itinerary_saved_items_id_seq OWNER TO myuser;

--
-- Name: itinerary_saved_items_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: myuser
--

ALTER SEQUENCE public.itinerary_saved_items_id_seq OWNED BY public.itinerary_saved_items.id;


--
-- Name: itinerary_saved_user_id_seq; Type: SEQUENCE; Schema: public; Owner: myuser
--

CREATE SEQUENCE public.itinerary_saved_user_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.itinerary_saved_user_id_seq OWNER TO myuser;

--
-- Name: itinerary_saved_user_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: myuser
--

ALTER SEQUENCE public.itinerary_saved_user_id_seq OWNED BY public.itinerary_saved.user_id;


--
-- Name: users; Type: TABLE; Schema: public; Owner: myuser
--

CREATE TABLE public.users (
    id bigint NOT NULL,
    email character varying(255) NOT NULL,
    password character varying(255) NOT NULL,
    created_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP,
    salt character varying(255) NOT NULL
);


ALTER TABLE public.users OWNER TO myuser;

--
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: myuser
--

CREATE SEQUENCE public.users_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.users_id_seq OWNER TO myuser;

--
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: myuser
--

ALTER SEQUENCE public.users_id_seq OWNED BY public.users.id;


--
-- Name: weather_data; Type: TABLE; Schema: public; Owner: myuser
--

CREATE TABLE public.weather_data (
    id uuid NOT NULL,
    all_clouds integer,
    cod integer,
    dt bigint,
    fetch_time timestamp(6) without time zone,
    feels_like double precision,
    humidity integer,
    pressure integer,
    temperature double precision,
    temp_max double precision,
    temp_min double precision,
    rain_1h double precision,
    rain_3h double precision,
    snow_1h double precision,
    snow_3h double precision,
    sunrise bigint,
    sunset bigint,
    timezone integer,
    visibility integer,
    weather_description character varying(255),
    weather_icon character varying(255),
    weather_id integer,
    weather_main character varying(255),
    wind_deg integer,
    wind_gust double precision,
    wind_speed double precision
);


ALTER TABLE public.weather_data OWNER TO myuser;

--
-- Name: itinerary_saved id; Type: DEFAULT; Schema: public; Owner: myuser
--

ALTER TABLE ONLY public.itinerary_saved ALTER COLUMN id SET DEFAULT nextval('public.itinerary_saved_id_seq'::regclass);


--
-- Name: itinerary_saved user_id; Type: DEFAULT; Schema: public; Owner: myuser
--

ALTER TABLE ONLY public.itinerary_saved ALTER COLUMN user_id SET DEFAULT nextval('public.itinerary_saved_user_id_seq'::regclass);


--
-- Name: itinerary_saved_items id; Type: DEFAULT; Schema: public; Owner: myuser
--

ALTER TABLE ONLY public.itinerary_saved_items ALTER COLUMN id SET DEFAULT nextval('public.itinerary_saved_items_id_seq'::regclass);


--
-- Name: users id; Type: DEFAULT; Schema: public; Owner: myuser
--

ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq'::regclass);


--
-- Data for Name: daily_forecast_data; Type: TABLE DATA; Schema: public; Owner: myuser
--

COPY public.daily_forecast_data (id, dt, fetch_time, humidity, pressure, rain, snow, speed, temp_day, weather_description, weather_icon, weather_main, clouds, feel_day, feel_eve, feel_morn, feel_night, sunrise, sunset, temp_eve, temp_max, temp_min, temp_morn, temp_night, weather_id, deg) FROM stdin;
53d421d8-d474-4638-8f1c-041ff4243e8b	1744387200	2025-04-11 15:10:58.3248	88	1020	9.5	0	9.81	279.72	light rain	10d	Rain	99	275.3	274.18	274.42	273.62	1744366959	1744414234	279.2	279.72	278.77	279.02	278.77	500	51
8ea075cc-eb55-43cd-b019-dbfaf59db828	1744473600	2025-04-11 15:10:58.3248	79	1019	2.43	0	11.13	280.3	light rain	10d	Rain	92	275.67	275.11	272.34	272.84	1744453265	1744500697	279.99	280.3	277.88	277.88	278.44	500	23
bf6064ef-3ba4-4f6f-b04f-9afc7eae8352	1744560000	2025-04-11 15:10:58.3248	68	1014	1.98	0	11.58	282.13	light rain	10d	Rain	73	278.14	278.15	272.05	276.42	1744539571	1744587160	281.68	282.13	277.93	277.93	279.8	500	12
00ca8219-8a56-41e3-82f2-c2b32d60369b	1744646400	2025-04-11 15:10:58.3248	58	1016	0	0	6.48	285.99	few clouds	02d	Clouds	22	284.84	283.75	278.08	283.26	1744625879	1744673623	284.5	285.99	279.8	280.22	283.86	801	196
c06ea171-9c77-4ad0-bc77-3cd3fc4948bd	1744732800	2025-04-11 15:10:58.3248	61	1008	1.4	0	8.24	285.25	light rain	10d	Rain	78	284.11	279.33	284.04	275.65	1744712187	1744760086	282.71	285.25	279.77	284.5	279.77	500	291
b49f744c-9858-4e0c-b6c1-2e31d4d0c51d	1744819200	2025-04-11 15:10:58.3248	53	1015	0	0	7.59	281.34	scattered clouds	03d	Clouds	33	277.73	276.78	273.7	273.38	1744798495	1744846550	280.22	281.34	277.12	278.34	277.12	802	293
fa06f27a-05e7-44ff-82e1-0263f4959b06	1744905600	2025-04-11 15:10:58.3248	43	1022	0	0	4.69	282.63	sky is clear	01d	Clear	4	280.41	279.69	272.08	278.08	1744884805	1744933013	282.2	282.63	276.09	276.09	280.77	800	290
dc278ab1-e351-4dc3-a4c3-f7bc50ee6f3f	1744992000	2025-04-11 15:10:58.3248	78	1018	3.08	0	11.01	284.76	light rain	10d	Rain	61	284.01	285.21	278.26	285.24	1744971115	1745019476	285.73	285.73	280.77	281.44	285.52	500	194
76706b18-43db-4add-9371-c30d1d12534c	1745078400	2025-04-11 15:10:58.3248	57	1017	5.21	0	10.31	287.91	light rain	10d	Rain	30	286.93	286	285.68	285.27	1745057426	1745105940	286.9	287.91	285.52	285.97	285.71	500	206
9e40d263-d438-423d-8265-053c7c576457	1745164800	2025-04-11 15:10:58.3248	70	1018	0.54	0	6.87	292.48	light rain	10d	Rain	42	292.3	291.12	286.48	289.5	1745143738	1745192403	291.15	292.48	285.71	286.62	289.49	500	235
625b5a03-bc26-4d2b-91da-1b79923cd22a	1745251200	2025-04-11 15:10:58.3248	58	1021	0.59	0	6.87	294.47	light rain	10d	Rain	5	294.17	291.99	288.95	289.73	1745230051	1745278867	292.15	294.47	289.03	289.03	289.67	500	235
c5efe650-21e9-408c-82cf-5da8c7bb8281	1745337600	2025-04-11 15:10:58.3248	65	1018	0.59	0	6.57	293.79	light rain	10d	Rain	37	293.61	292.28	289.65	290.38	1745316365	1745365331	292.2	293.79	289.62	289.62	290.14	500	242
988039e5-c752-4bfc-9cd0-360e23f30e6e	1745424000	2025-04-11 15:10:58.3248	94	1023	6.56	0	5.95	287.67	light rain	10d	Rain	75	287.63	283.78	289.01	280.69	1745402680	1745451794	284.17	290.14	283.01	288.92	283.01	500	103
07834d0a-a802-4bf9-8816-e5a1ad8ad68a	1745510400	2025-04-11 15:10:58.3248	77	1021	0.67	0	4.82	289.92	light rain	10d	Rain	77	289.66	288.91	283.56	287.42	1745488996	1745538258	288.93	289.92	283.01	283.94	287.43	500	143
b871b489-d255-4de1-b4d5-44f4fe18ef62	1745596800	2025-04-11 15:10:58.3248	55	1015	0	0	7.67	294.02	few clouds	02d	Clouds	21	293.6	290.68	287.92	283.96	1745575313	1745624722	291.25	294.02	284.95	288.07	284.95	801	331
343397f8-b75c-40e6-a73b-1ae40ccbbacd	1745683200	2025-04-11 15:10:58.3248	45	1023	0	0	7.67	287.01	few clouds	02d	Clouds	19	285.63	284.39	279.62	280.22	1745661631	1745711185	285.79	287.01	282.43	282.43	282.47	801	331
8c1019da-5362-43e9-be06-6e8560e51ef1	1745769600	2025-04-11 15:10:58.3248	39	1024	0	0	5.93	286.73	scattered clouds	03d	Clouds	34	285.16	283.76	278.91	282.73	1745747950	1745797649	284.93	286.73	281.35	281.35	283.59	802	194
b4041236-00fe-4977-a9df-2880423bc800	1745856000	2025-04-11 15:10:58.3248	79	1012	2.87	0	8.52	289.41	light rain	10d	Rain	62	289.15	287.46	284.04	283.63	1745834270	1745884113	287.68	289.41	283.59	284.59	284.67	500	220
c64955a9-ae98-4d45-b8e2-342fd9a16518	1745942400	2025-04-11 15:10:58.3248	45	1017	0.44	0	5.07	287.85	light rain	10d	Rain	33	286.55	284.42	280.55	282.43	1745920591	1745970576	285.56	287.85	283.02	283.02	283.63	500	5
e76f51bb-6820-4bce-b68b-bd2ea7bbd47c	1746028800	2025-04-11 15:10:58.3248	41	1017	1.36	0	5.39	288.57	light rain	10d	Rain	8	287.24	283.97	280.14	279.57	1746006913	1746057040	284.98	288.57	282.32	282.32	282.33	500	351
a3e6de1b-cd99-4253-92a4-0a8b712f2d81	1746115200	2025-04-11 15:10:58.3248	43	1015	1.69	0	8.32	284.09	light rain	10d	Rain	38	282.36	279.37	276.59	274.81	1746093237	1746143503	282.55	284.09	279.37	280.45	279.37	500	338
62b85f6e-fc71-4114-9738-2d0a9e3c36ff	1746201600	2025-04-11 15:10:58.3248	40	1017	0	0	8.32	284	few clouds	02d	Clouds	12	282.19	282.29	274.01	277.39	1746179562	1746229967	283.88	284	278.67	278.67	280.6	801	338
38fc96a1-63d4-4815-bb7f-2c3ff4918dec	1746288000	2025-04-11 15:10:58.3248	43	1018	0	0	5.4	287.41	sky is clear	01d	Clear	9	286.01	285.74	277.26	283.37	1746265888	1746316430	286.99	287.41	280.4	280.4	284.67	800	324
204f93e3-3f82-4ed7-8687-8d27b6801001	1746374400	2025-04-11 15:10:58.3248	57	1013	0.41	0	5.07	287.33	light rain	10d	Rain	82	286.29	285.9	282.52	284.3	1746352215	1746402893	286.64	287.33	283.47	283.47	284.93	500	302
241ebb19-3e4a-4b85-8959-606b425e739d	1746460800	2025-04-11 15:10:58.3248	43	1013	0	0	5.8	290.16	sky is clear	01d	Clear	7	289.04	287.66	284.46	282.98	1746438544	1746489356	288.88	290.16	284.46	285.19	284.46	800	328
2aeff6bc-42ef-4902-8543-4243981fa0e3	1746547200	2025-04-11 15:10:58.3248	36	1015	0	0	5.8	289.69	few clouds	02d	Clouds	24	288.34	287.34	281.82	285.92	1746524874	1746575818	288.5	289.69	283.22	283.22	286.73	801	328
1508ddd4-76cb-4687-be8f-a04086e9b8ef	1746633600	2025-04-11 15:10:58.3248	77	1002	9.91	0	8.73	292.6	light rain	10d	Rain	65	292.61	289.88	286.84	286.89	1746611206	1746662281	289.74	292.6	286.73	286.95	286.95	500	246
60d510e2-4881-4479-8381-93494ab84eaa	1746720000	2025-04-11 15:10:58.3248	57	1006	2.68	0	7.62	289.85	light rain	10d	Rain	80	289.06	285.76	285.24	283.4	1746697538	1746748743	286.47	289.85	284.63	285.57	284.63	500	341
f360ecde-fa60-4bd5-9584-2d81b188e945	1746806400	2025-04-11 15:10:58.3248	40	1008	0.16	0	7.62	287.45	light rain	10d	Rain	12	285.98	284.74	279.08	278.48	1746783873	1746835205	286.23	287.45	281.91	282.43	281.91	500	341
6be961d1-21ad-4333-a283-e88d60f563da	1746892800	2025-04-11 15:10:58.3248	46	1013	0	0	7.05	285.19	scattered clouds	03d	Clouds	40	283.65	283.24	277.35	279.19	1746870208	1746921666	284.7	285.19	280.85	280.85	281.49	802	320
\.


--
-- Data for Name: events; Type: TABLE DATA; Schema: public; Owner: myuser
--

COPY public.events (id, address, attending_count, category, city, combined_category, description, event_site_url, fetch_time, image_url, interested_count, is_canceled, is_free, is_official, latitude, longitude, name, state, time_end, time_start, zip_code, version) FROM stdin;
32e11a51-5ee1-4741-9f9e-06eabd738b0f	Good Shepherd-Faith Presbyterian Church	1	music	New York	Music	What is more enjoyable than hearing a lute song? Perhaps quartets of singers and lutes joining together in song! For our 2024-2025 series finale, TENET...	https://www.yelp.com/events/new-york-rosettes?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:10:59.824102	https://s3-media2.fl.yelpcdn.com/ephoto/-10H3otGjZLagk2QiSovIA/o.jpg	0	f	f	f	40.77432290000001	-73.9839099	Rosettes	NY	2025-04-26T20:00:00	2025-04-26T18:00:00	10023	0
75c83cbd-e207-4135-95fd-2872f226c2d0	881 7th Ave	1	music	New York	Music	Oratorio Society's thrilling 2024-25 season concludes on Monday, May 5, 2025 at 7:00 pm with the world premiere of Paul Moravec and Mark Campbell's All...	https://www.yelp.com/events/new-york-mendelssohn-moravec?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:10:59.833101	https://s3-media3.fl.yelpcdn.com/ephoto/dZdz9-pYEGx3lMb81pUTgQ/o.jpg	0	f	f	f	40.7651249	-73.9799155945393	Mendelssohn/Moravec	NY	2025-05-05T21:00:00	2025-05-05T19:00:00	10019	0
a8029d9b-cf6f-46f4-8479-6e0cc2a17c17	790 11th Ave	1	music	New York	Music	Don't miss an enchanting evening of jazz piano as Lynne Arriale brings her trio to Klavierhaus on Tuesday, May 6, 2025.\n\n"One of the most exciting pianists...	https://www.yelp.com/events/new-york-piano-jazz-series-lynne-arriale?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:10:59.846575	https://s3-media2.fl.yelpcdn.com/ephoto/QvThh9_VkrsCiJ5b1lIxww/o.jpg	0	f	f	f	40.768928	-73.992242	Piano Jazz Series: Lynne Arriale	NY	2025-05-06T21:30:00	2025-05-06T19:00:00	10019	0
2238913c-ea56-40bb-84c4-9d728584ffe6	881 7th Ave	1	music	New York	Music	Join The New York Pops for its 42nd Birthday Gala, Words and Music: Diane Warren, on Monday, April 28, 2025 at 7:00 p.m. in Stern Auditorium / Perelman...	https://www.yelp.com/events/new-york-words-and-music-diane-warren?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:10:59.851565	https://s3-media3.fl.yelpcdn.com/ephoto/m3qn62hKNxCJm9MrMSmTDQ/o.jpg	0	f	f	f	40.7651249	-73.9799155945393	Words and Music: Diane Warren	NY	2025-04-28T21:00:00	2025-04-28T19:00:00	10019	0
91dd063e-7185-4cd6-b6ab-3f2777ca271c	412 Broadway New York, NY 10013	1	performing-arts	New York	Art & Fashion	Amanda Selwyn Dance Theatre/Notes in Motion is thrilled to announce its Spring Session Youth Programming at their NEW HOME in downtown Manhattan! The new...	https://www.yelp.com/events/new-york-amanda-selwyn-dance-theatre-notes-in-motion-announces-spring-session-youth-classes-april-june-2025?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:10:59.859515	https://s3-media1.fl.yelpcdn.com/ephoto/Xrnj1aBowmPC8yio4Q75SA/o.jpg	0	f	f	f	40.7189067	-74.00176429999999	Amanda Selwyn Dance Theatre/Notes in Motion announces Spring Session Youth Classes: April- June 2025	NY	2025-04-22T11:00:00	2025-04-22T09:00:00	10013	0
9ee50d0b-8908-4dac-bcbd-d8a39777932e	10 South St	1	charities	New York	Other	New York Harbor Oyster Classic 5K\nMay 4, 2025\n9AM Start\nNew York Harbor - Governors Island\nGovernors Island is accessible by boat only - Directions...	https://www.yelp.com/events/new-york-new-york-harbor-oyster-classic-5k-3?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:10:59.863864	https://s3-media1.fl.yelpcdn.com/ephoto/l8mPHn9_YqTtGTh1wVNG9w/o.jpg	0	f	f	f	40.70111869999999	-74.01187879999999	New York Harbor Oyster Classic 5K	NY	2025-05-04T12:00:00	2025-05-04T09:00:00	10004	0
69cbd54d-c99b-4d87-a8f1-bb1085bf87f9	225 Madison Ave	1	music	New York	Music	Enjoy a midday interlude with the Morgan's noontime recitals. In partnership with Young Concert Artists, the series showcases a new generation of...	https://www.yelp.com/events/new-york-young-concert-artists-presents-erin-wagner-mezzo-soprano?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:10:59.86875	https://s3-media2.fl.yelpcdn.com/ephoto/B_F0FTYaclanWxsP8zsfJw/o.jpg	0	f	f	f	40.749242	-73.981397	Young Concert Artists Presents Erin Wagner, mezzo-soprano	NY	2025-04-24T14:00:00	2025-04-24T12:00:00	10016	0
dc7bf4cc-d6c0-4190-852b-d93d3bc0f7c4	129 W 67th St	1	music	New York	Music	The Rhoda Walker Teagle Concert\n\nProgram:\nFRANZ SCHUBERT  -  Sonatina in D Major Op. 137 No. 1\nBÉLA BARTÓK  -  Sonata for Solo Violin, Sz. 117\nELLEN TAAFFE...	https://www.yelp.com/events/new-york-young-concert-artists-presents-violinist-oliver-neubauer?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:10:59.874174	https://s3-media1.fl.yelpcdn.com/ephoto/ikkY0hYNx9Q2gqsB1sfvdA/o.jpg	0	f	f	f	40.7752559	-73.98298	Young Concert Artists Presents Violinist Oliver Neubauer	NY	2025-04-29T21:30:00	2025-04-29T19:30:00	10023	0
c69f1fd4-f55e-405f-9d7c-88a9637001c5	115 W 29th St, New York, NY 10001	1	performing-arts	New York	Art & Fashion	Theater Resources Unlimited (TRU) in association with Write Act Repertory, presents The TRU Virtual Audition Conference for Theater 2025 (ACT25) on...	https://www.yelp.com/events/new-york-theater-resources-unlimited-announces-tru-virtual-audition-conference-2025-on-zoom?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:10:59.878992	https://s3-media2.fl.yelpcdn.com/ephoto/jQZKimTCBU323vDwSNSrFg/o.jpg	0	f	f	f	40.7471158	-73.9908195	Theater Resources Unlimited announces TRU Virtual Audition Conference 2025 on Zoom	NY	2025-04-26T13:30:00	2025-04-26T11:30:00	10001	0
6c8eaa0d-444e-4bb7-a738-c8e9532547be	175 8th Avenue	1	performing-arts	New York	Art & Fashion	BODYTRAFFIC, Los Angeles' premier contemporary dance company, returns to The Joyce Theater with a dynamic program that celebrates the city's spirit of...	https://www.yelp.com/events/new-york-bodytraffic-los-angeles-at-the-joyce-theater?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:10:59.883388	https://s3-media2.fl.yelpcdn.com/ephoto/sWuN2ZtgRnbPoOa4bIlXEg/o.jpg	0	f	f	f	40.7428216	-74.00070459999999	BODYTRAFFIC (Los Angeles) at The Joyce Theater	NY	2025-04-15T21:30:00	2025-04-15T19:30:00	10011	0
3a8a1e53-a7bb-4fdb-9a14-4ff978091aed	219 W 19th St	1	performing-arts	New York	Art & Fashion	Amanda Selwyn Dance Theatre presents the World Premiere of Awaken as their 25th Anniversary Performance Season on Thursday - Saturday, May 8-10, 2025 at...	https://www.yelp.com/events/new-york-amanda-selwyn-dance-theatre-presents-world-premiere-of-awaken?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:10:59.887128	https://s3-media4.fl.yelpcdn.com/ephoto/XFKDQdhqjty--okj3rqWkg/o.jpg	0	f	f	f	40.7422303	-73.9982715	Amanda Selwyn Dance Theatre Presents World Premiere of Awaken	NY	2025-05-08T21:30:00	2025-05-08T19:30:00	10011	0
1e41e310-b13b-4010-a028-2ebef119ef0e	1 Times Sq	1	performing-arts	New York	Art & Fashion	The Broadway Green Alliance and Times Square Alliance are proud to announce that Broadway will once again raise its voice for the planet as the Broadway...	https://www.yelp.com/events/new-york-broadway-celebrates-earth-day-2025?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:10:59.893139	https://s3-media4.fl.yelpcdn.com/ephoto/qdm09iFww9Kfx5dM7Lq0fA/o.jpg	1	f	t	f	40.75638423808898	-73.98644725547547	Broadway Celebrates Earth Day 2025	NY	2025-04-26T15:00:00	2025-04-26T11:00:00	10036	0
41f811d2-f18d-4197-90f3-70e5ab65e71f	511A West 22nd St.,	1	visual-arts	New York	Art & Fashion	JoAnne Artman Gallery is pleased to present Endless Blue, a captivating solo exhibition of contemporary coastlines by acclaimed artist Todd Kenyon....	https://www.yelp.com/events/new-york-joanne-artman-gallery-presents-solo-art-exhibition-endless-blue-by-todd-kenyon?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:10:59.897539	https://s3-media2.fl.yelpcdn.com/ephoto/qGYnyJsLti7M9Kwz6IZV3w/o.jpg	0	f	t	f	40.7474283	-74.0052419	JoAnne Artman Gallery, presents Solo Art Exhibition:ENDLESS BLUE by Todd Kenyon	NY	2025-05-01T13:00:00	2025-05-01T11:00:00	10011	0
2a4acc64-096f-4aeb-b134-54774470eb53	22 Warren St	1	performing-arts	New York	Art & Fashion	America's LARGEST interactive true crime mystery dinner show is now playing in New York City, NY!\n\nAt The Dinner Detective Murder Mystery Dinner Show,...	https://www.yelp.com/events/new-york-the-dinner-detective-comedy-mystery-dinner-show-3?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:10:59.901703	https://s3-media2.fl.yelpcdn.com/ephoto/9dGKVePj9KzsuCopVEAjkw/o.jpg	0	f	f	f	40.7143282	-74.0077697	The Dinner Detective Comedy Mystery Dinner Show	NY	2025-04-12T21:00:00	2025-04-12T18:00:00	10007	0
6e63052a-8238-45b8-91e2-f6c8224af9be	101 Reade St	1	visual-arts	New York 	Art & Fashion	Satellite Collective presents SATELLITE TRIBECA at Mriya Gallery, 101 Reade Street, Tribeca, NYC from May 8-18, 2025, returning to the venue with two...	https://www.yelp.com/events/new-york-satellite-collectives-tribeca-show-2025?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:10:59.908448	https://s3-media2.fl.yelpcdn.com/ephoto/GTEVzA5cyPKEvsmW_VoS3w/o.jpg	0	f	t	f	40.71564439798422	-74.0082534	Satellite Collective's Tribeca Show 2025	NY	2025-05-08T21:00:00	2025-05-08T19:00:00	10013	0
f8560c29-1304-4280-ae43-7d2cd3c6a4ea	485 5th Ave At E 41st St	3	food-and-drink	New York	Food & Festival	The Pasta Tarot and Andaz 5th Avenue are uniting for an evening of captivating divination and delightful dining. Kicking off at The Bar Downstairs, A5A's...	https://www.yelp.com/events/new-york-andaz-5th-avenue-x-the-pasta-tarot-2?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:10:59.913396	https://s3-media4.fl.yelpcdn.com/ephoto/lnuFkjZsL-j1-B-fF8sXwQ/o.jpg	5	f	t	f	40.7528333	-73.9809527	Andaz 5th Avenue x The Pasta Tarot	NY	2025-04-11T18:00:00	2025-04-11T16:00:00	10017	0
f8602f15-ac10-4e35-85be-60f56cf69d5b	1071 5th Ave	1	performing-arts	New York	Art & Fashion	Works & Process presents New Jersey Ballet: Maria Kowroski and Harrison Ball, on April 14, 2025 at Guggenheim New York in the Peter B. Lewis Theater, 1071...	https://www.yelp.com/events/new-york-works-and-process-presents-new-jersey-ballet-maria-kowroski-and-harrison-ball?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:10:59.917788	https://s3-media3.fl.yelpcdn.com/ephoto/r4tAHTnfZVwunzFtuLbfig/o.jpg	0	f	f	f	40.7831815	-73.9588052	Works & Process Presents New Jersey Ballet: Maria Kowroski and Harrison Ball	NY	2025-04-14T21:00:00	2025-04-14T19:00:00	10128	0
0384d150-80b2-4f6a-9055-568c1cb18554	425 Lafayette St	1	music	New York	Music	Rising cabaret  talent AVIVA makes her Joe's Pub debut with "VIVACITY" - where passion, creativity, and magic come alive - on Friday April 11 @ 7:00pm....	https://www.yelp.com/events/new-york-aviva-to-perform-vivacity-new-solo-show-joes-pub?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:10:59.923131	https://s3-media2.fl.yelpcdn.com/ephoto/XuPdD9kjmW7tey23nD7XCA/o.jpg	0	f	f	f	40.7289276	-73.9917262	AVIVA To Perform "VIVACITY" -  New Solo Show @ Joe's Pub	NY	2025-04-11T21:00:00	2025-04-11T19:00:00	10003	0
2fdbfe70-1a23-49d0-826c-3f335128e7f3	233 West St	1	charities	New York	Other	Join New York Cares, the largest volunteer network in the city, for the organization's 20th annual Spring Soiree which will celebrate the thousands of...	https://www.yelp.com/events/new-york-new-york-cares-20th-annual-spring-soiree-bright-lights-big-hearts?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:10:59.927378	https://s3-media4.fl.yelpcdn.com/ephoto/rc_75bLDqyEPh2B8gYFvFw/o.jpg	0	f	f	f	40.7213003	-74.01287119999999	New York Cares' 20th Annual Spring Soiree: Bright Lights, Big Hearts	NY	2025-04-29T21:30:00	2025-04-29T19:30:00	10013	0
451255de-aa70-47c3-849f-c0cb24c3949c	2 Renwick St	1	food-and-drink	New York	Food & Festival	Lindens (2 Renwick Street) at Arlo Soho is bringing back its popular, seasonal Dinner Party Series on Friday, April 11th, from 6:30pm to 8:30pm with Cheers...	https://www.yelp.com/events/new-york-lindens-cheers-to-spring-dinner-party-on-4-11?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:10:59.931325	https://s3-media1.fl.yelpcdn.com/ephoto/h-nqmMHJCpa_qb6Xpmcqog/o.jpg	0	f	f	f	40.72449719999999	-74.0084577	Lindens' Cheers to Spring Dinner Party on 4/11	NY	2025-04-11T20:30:00	2025-04-11T18:30:00	10013	0
c9ce5ed5-e701-4c4b-bc12-d9bdcc388ae7	1412 Broadway	1	food-and-drink	New York	Food & Festival	Enjoy Derby Day at Elsie Rooftop (1412 Broadway) during their Kentucky Derby Watch Party on Saturday, May 3rd, from 3 PM to 7 PM. This annual event invites...	https://www.yelp.com/events/new-york-kentucky-derby-watch-party-at-elsie-rooftop-on-5-3?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:10:59.936382	https://s3-media2.fl.yelpcdn.com/ephoto/YLwMIv2p1MlrIUCj8X1mLw/o.jpg	0	f	f	f	40.753615	-73.9865612	Kentucky Derby Watch Party at Elsie Rooftop on 5/3	NY	2025-05-03T17:00:00	2025-05-03T15:00:00	10018	0
a1b9c122-9885-41cd-a4ae-750f9505ef4a	17 Irving Pl	1	performing-arts	New York	Art & Fashion	NIGHT OF 1000 STEVIES - the largest and most beloved STEVIE NICKS fan event in the world - returns for its 33rd annual edition on May 3. NIGHT OF 1000...	https://www.yelp.com/events/new-york-the-jackie-factory-presents-night-of-1000-stevies-33-dances-of-rhiannon?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:10:59.94248	https://s3-media3.fl.yelpcdn.com/ephoto/9HLZW63198ERB40lkBXx0w/o.jpg	0	f	f	f	40.7348999	-73.9883747	The Jackie Factory Presents NIGHT OF 1000 STEVIES 33: Dances of Rhiannon	NY	2025-05-03T23:00:00	2025-05-03T20:00:00	10003	0
994a8867-b071-40de-97d5-9d7392c930ff	1071 5th Ave, New York, NY 10128	1	performing-arts	New York	Art & Fashion	Works & Process, a non-profit performing arts organization that champions artists and their creative process from studio to stage, announces its...	https://www.yelp.com/events/new-york-works-and-process-announces-residency-open-call-application-now-open-through-april-22-2025?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:10:59.947884	https://s3-media3.fl.yelpcdn.com/ephoto/c0S6FSM1k9qra8poe12KNg/o.jpg	0	f	f	f	40.7831815	-73.9588052	Works & Process Announces Residency Open Call Application Now Open through April 22, 2025	NY	2025-04-22T19:00:00	2025-04-22T17:00:00	10128	0
c9a46459-c53b-4207-961a-4ec135ace491	1395 Lexington Ave, New York, NY 10128	1	performing-arts	New York	Art & Fashion	The 92nd Street Y, New York announces the selected artists for Future Dance Festival 2025, the fourth edition of the popular choreographic festival. This...	https://www.yelp.com/events/new-york-92ny-harkness-dance-center-announces-future-dance-festival-2025-selected-artists?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:10:59.951884	https://s3-media3.fl.yelpcdn.com/ephoto/QbbgxHXA2aABVOmDPJCVmw/o.jpg	0	f	f	f	40.7829293	-73.9524223	92NY Harkness Dance Center Announces Future Dance Festival 2025 Selected Artists	NY	2025-04-11T21:00:00	2025-04-11T19:00:00	10128	0
13e901ba-8c71-4ab5-a464-d78930a00e7d	14 E 60th St	1	kids-family	New York	Kids & Family	Join Watson Adventures on a unique family scavenger hunt in Central Park!\n\nKids and adults work together to explore the park below 72nd Street. To score...	https://www.yelp.com/events/new-york-watson-adventures-secrets-of-central-park-scavenger-hunt-for-families-3?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:10:59.955887	https://s3-media1.fl.yelpcdn.com/ephoto/jCRK4c6VoiH6KFsBLvN2HA/o.jpg	0	f	f	f	40.7643025	-73.9717568	Watson Adventures' Secrets of Central Park Scavenger Hunt for Families	NY	2025-04-19T13:00:00	2025-04-19T11:00:00	10022	0
fc0126b3-2d0b-42de-89de-60d728d983c1	1251 5th Ave	1	other	New York	Other	Join Watson Adventures on a unique scavenger hunt in Central Park! See the best of the park below 72nd Street and discover places you might otherwise never...	https://www.yelp.com/events/new-york-watson-adventures-secrets-of-central-park-scavenger-hunt-46?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:10:59.960053	https://s3-media1.fl.yelpcdn.com/ephoto/Po7DG1RiTLyKEqw5aOAJ4w/o.jpg	0	f	f	f	40.78122724167864	-73.96651581384843	Watson Adventures' Secrets of Central Park Scavenger Hunt	NY	2025-04-26T16:00:00	2025-04-26T14:00:00	10029	0
8339ca2e-efff-42e6-9636-98e5fa325407	89 E 42nd St	1	other	New York	Other	Join Watson Adventures on a unique scavenger hunt for adults at Grand Central Terminal!\n\nDiscover hidden history, cool places, little-known passageways, and...	https://www.yelp.com/events/new-york-watson-adventures-secrets-of-grand-central-scavenger-hunt-118?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:10:59.964011	https://s3-media1.fl.yelpcdn.com/ephoto/5AEYOLE0s9ZoX1wI1ZPFoQ/o.jpg	0	f	f	f	40.752727697036576	-73.97723946032279	Watson Adventures' Secrets of Grand Central Scavenger Hunt	NY	2025-04-12T16:00:00	2025-04-12T14:00:00	10017	0
5f72216f-0300-4cda-990c-156fc9a03587	200 Central Park W	1	other	New York	Other	Join Watson Adventures on a murder mystery scavenger hunt for adults at the Museum of Natural History! \n\nSomeone, or some "thing", has been bumping off...	https://www.yelp.com/events/new-york-watson-adventures-murder-at-the-museum-of-natural-history-scavenger-hunt-112?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:10:59.967396	https://s3-media1.fl.yelpcdn.com/ephoto/bHhpt9W2cTq6Fa_eU0Eb0Q/o.jpg	0	f	f	f	40.78138382121182	-73.97399475214957	Watson Adventures' Murder at the Museum of Natural History Scavenger Hunt	NY	2025-04-19T16:00:00	2025-04-19T14:00:00	10024	0
4a64c751-fa78-48e2-b176-1ad76206fb9d	1395 Lexington Ave, New York, NY 10128	1	music	New York	Music	The 92nd Street Y, New York presents Grammy Award-winning pianist Brad Mehldau in his only NYC concert appearance of the season with music from his latest...	https://www.yelp.com/events/new-york-the-92nd-street-y-new-york-presents-brad-mehldau-piano-apr%C3%A8s-faur%C3%A9?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:10:59.9718	https://s3-media3.fl.yelpcdn.com/ephoto/aazzZu1jpxjGb-KnkmEC8Q/o.jpg	0	f	f	f	40.7829293	-73.9524223	The 92nd Street Y, New York Presents Brad Mehldau, piano: Après Fauré	NY	2025-04-23T21:30:00	2025-04-23T19:30:00	10128	0
6ade945c-5ec7-4800-9f23-81f6f98a4799	1412 Broadway	1	food-and-drink	New York	Food & Festival	Head to Elsie Rooftop & Penthouse (1412 Broadway, Penthouse Level, Between 39th and 40th Streets, New York, NY) on Wednesday, April 16th from 6:30 pm to 9...	https://www.yelp.com/events/new-york-experience-an-epic-mixology-class-at-elsie-rooftop-and-penthouse-on-4-16?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:10:59.975566	https://s3-media1.fl.yelpcdn.com/ephoto/MC61RPGKxROLLWi383e0pg/o.jpg	0	f	f	f	40.753615	-73.9865612	Experience an Epic Mixology Class at Elsie Rooftop & Penthouse on 4/16	NY	2025-04-16T20:30:00	2025-04-16T18:30:00	10018	0
0a846c2a-7bc4-4964-a870-49b787a87e41	140 W 17th St	1	performing-arts	New York	Art & Fashion	Mimi Garrard of Mimi Garrard Dance Theatre and Collaborators present Dark and Light on Saturday, April 26, 2025 at 2pm at The Rubin Museum, 150 W. 17th...	https://www.yelp.com/events/new-york-mimi-garrard-and-collaborators-present-dark-and-light?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:10:59.979421	https://s3-media4.fl.yelpcdn.com/ephoto/cMJz1QGtpUInqKq79drr6A/o.jpg	0	f	f	f	40.7399603	-73.9973455	Mimi Garrard and Collaborators present Dark and Light	NY	2025-04-26T16:00:00	2025-04-26T14:00:00	10011	0
6bfd7681-1c18-4fed-8e72-5eef08a51e6a	210 Rivington St	1	visual-arts	New York	Art & Fashion	Harman Projects is pleased to announce Dream Wranglers a solo exhibition by artist Alex Garant. \n\n\nOpening Night Reception:\n\nSaturday, April 12th\n\n6pm to...	https://www.yelp.com/events/new-york-alex-garant-dream-wranglers?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:10:59.983797	https://s3-media3.fl.yelpcdn.com/ephoto/2Jst-0APIjwfYiO5DhV8pg/o.jpg	0	f	t	f	40.7184699	-73.9826586	Alex Garant - Dream Wranglers	NY	2025-04-12T20:00:00	2025-04-12T18:00:00	10002	0
60fb0479-b0f2-49c9-a032-6b98effcb461	21 A Clinton St	1	nightlife	New York	Other	Comedian Brandon Collins (Drunk Black History) celebrates 4/20 and Easter Sunday with this wild and unpredictable comedy variety show.\n\nJoining Brandon...	https://www.yelp.com/events/new-york-hazy-eyes-open-lungs-cant-lose-a-4-20-comedy-variety-show?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:10:59.987046	https://s3-media1.fl.yelpcdn.com/ephoto/OFBagylpwucv-lOhcuY7Kg/o.jpg	0	f	f	f	40.7207414	-73.984434	Hazy Eyes, Open Lungs, Can't Lose (A 4/20 Comedy Variety Show)	NY	2025-04-20T18:00:00	2025-04-20T16:30:00	10002	0
c3f3a3e8-a46c-4577-9712-b4f14712e28b	1395 Lexington Ave, New York, NY 10128	1	performing-arts	New York	Art & Fashion	The 92nd Street Y, New York and Works & Process announces the first annual Uptown Rhythm Dance Festival, a multi-night performance and workshop program,...	https://www.yelp.com/events/new-york-92ny-harkness-dance-center-and-works-and-process-present-uptown-rhythm-dance-festival?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:10:59.990943	https://s3-media4.fl.yelpcdn.com/ephoto/IQwKLtHKobomT7OBuQv00Q/o.jpg	0	f	f	f	40.7829293	-73.9524223	92NY Harkness Dance Center and Works & Process present Uptown Rhythm Dance Festival	NY	2025-04-21T21:30:00	2025-04-21T19:30:00	10128	0
14d5528a-e9be-4c5b-a904-0adba84fadec	1071 5th Ave	1	performing-arts	New York	Art & Fashion	Works & Process presents The Metropolitan Opera: John Adams's Antony and Cleopatra on Monday, April 28, 2025 at Guggenheim New York in the Peter B. Lewis...	https://www.yelp.com/events/new-york-works-and-process-presents-the-metropolitan-opera-john-adamss-antony-and-cleopatra?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:10:59.994153	https://s3-media4.fl.yelpcdn.com/ephoto/M6y3aFseloQ4JKedseUjIg/o.jpg	0	f	f	f	40.7831815	-73.9588052	Works & Process Presents The Metropolitan Opera: John Adams's Antony and Cleopatra	NY	2025-04-28T22:00:00	2025-04-28T19:00:00	10128	0
0802679d-127b-4a5e-96ec-0bc26997a59c	175 8th Ave	1	performing-arts	New York	Art & Fashion	Join us for a captivating evening with BODYTRAFFIC, featuring Incense Burning on a Saturday Morning: The Maestro--a mesmerizing tribute to the artistry and...	https://www.yelp.com/events/new-york-bodytraffic-los-angeles-presents-an-exclusive-conversation-with-charles-m-blow-msnbc-political-analyst-juel-d-lane?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:10:59.998262	https://s3-media3.fl.yelpcdn.com/ephoto/0ti4cV9PlzPSCW7we8Dwfg/o.jpg	0	f	f	f	40.7428216	-74.0007046	BODYTRAFFIC (Los Angeles) presents an Exclusive Conversation with Charles M. Blow, MSNBC Political Analyst Juel D. Lane	NY	2025-04-18T21:30:00	2025-04-18T19:30:00	10011	0
a61a5f24-8ddf-401c-9fcf-20bff683d53b	173 Mulberry St	1	performing-arts	New York	Art & Fashion	Join our community of hilarious comedians (from HBO, Netflix, Comedy Central - and more!) for a night of heartwarming absurdity as they bring you their joke...	https://www.yelp.com/events/new-york-ha-or-nah-a-joke-lab-timeout-ny-pick-4?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:11:00.001478	https://s3-media3.fl.yelpcdn.com/ephoto/DGUaRko5wISTIXjzb35aGg/o.jpg	0	f	f	f	40.7201394	-73.9973091	Ha! or Nah!:  A Joke Lab *TimeOut NY PICK!*	NY	2025-04-17T22:00:00	2025-04-17T20:00:00	10013	0
500f0c27-c534-4e63-a7a8-49ab103d250f	1887 Broadway	1	music	New York	Music	The essence of punk music has always been impulsive, spontaneous, eclectic, audacious, and exciting. Those qualities also describe a great mixtape! Prepare...	https://www.yelp.com/events/new-york-american-songbook-mixtape-women-in-punk?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:11:00.011007	https://s3-media3.fl.yelpcdn.com/ephoto/ds3buI7G3erdmhu8Uai--g/o.jpg	0	f	t	f	40.77133388165931	-73.98300207571515	American Songbook: Mixtape: Women in Punk	NY	2025-04-11T21:30:00	2025-04-11T19:30:00	10023	0
74495145-785c-4d84-8756-c9d8d1e7a26f	1941 Broadway	1	music	New York	Music	Presented in association with Institut Ramon Llull\n\nThe varied traditions of Mediterranean folk music are reimagined through a modern, experimental lens in...	https://www.yelp.com/events/new-york-american-songbook-4132314?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:11:00.016934	https://s3-media2.fl.yelpcdn.com/ephoto/lUMqo9G8yfJToW5KqZyQLw/o.jpg	0	f	f	f	40.773821090030026	-73.98256406035841	American Songbook: 4132314	NY	2025-04-12T21:30:00	2025-04-12T19:30:00	10023	0
1bc25299-378b-47eb-ad44-1b496663cd2b	10 Lincoln Center Plz	1	music	New York	Music	It has been nearly a decade since the GRAMMY-nominated and Latin GRAMMY Award-winning emcee Ana Tijoux dropped her critically acclaimed album Vengo, a...	https://www.yelp.com/events/new-york-american-songbook-ana-tijoux?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:11:00.022545	https://s3-media4.fl.yelpcdn.com/ephoto/yCUsSFy8jGFNBTM95HkutA/o.jpg	0	f	f	f	40.7728417817459	-73.9826561195402	American Songbook: Ana Tijoux	NY	2025-04-15T21:30:00	2025-04-15T19:30:00	10023	0
7a58107f-56d1-4747-bc2f-ec919e40db95	2 Renwick St	1	food-and-drink	New York	Food & Festival	The Dinner Table will host an exclusive dining experience at Lindens (2 Renwick Street) at Arlo Soho on Thursday, April 24th, from 7-10 PM.  The special...	https://www.yelp.com/events/new-york-the-dinner-table-will-host-a-three-course-feast-at-lindens-on-4-24?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:11:00.027658	https://s3-media3.fl.yelpcdn.com/ephoto/f8R5VyKVMZNs1LdwaC7zEw/o.jpg	0	f	f	f	40.72449719999999	-74.0084577	The Dinner Table will Host a Three Course Feast at Lindens on 4/24	NY	2025-04-24T21:00:00	2025-04-24T19:00:00	10013	0
5bba9e2b-2134-41aa-bc54-1de3b3154f73	66 E 4th St	1	performing-arts	New York	Art & Fashion	Daniel Gwirtzman Dance Company presents the New York City premiere of e-Motion at the La MaMa Moves! Dance Festival on Friday, April 18th at 7:30pm,...	https://www.yelp.com/events/new-york-daniel-gwirtzman-dance-company-presents-e-motion-as-part-of-la-mama-moves-dance-festival-2025?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:11:00.03326	https://s3-media4.fl.yelpcdn.com/ephoto/4zLSY6aHoeepj49p3DSjJg/o.jpg	0	f	f	f	40.7264415	-73.9904678	Daniel Gwirtzman Dance Company presents e-Motion as part of La MaMa Moves! Dance Festival 2025	NY	2025-04-18T21:30:00	2025-04-18T19:30:00	10003	0
2b3e63d1-b487-482a-8d64-2965bb510744	1395 Lexington Ave, New York, NY 10128	1	music	New York	Music	The 92nd Street Y, New York (92NY), one of New York's leading cultural venues, presents Jordi Savall, viola da gamba & Hespèrion XXI: Music of Fire &...	https://www.yelp.com/events/new-york-jordi-savall-viola-da-gamba-and-hesp%C3%A8rion-xxi-music-of-fire-and-love?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:11:00.039946	https://s3-media4.fl.yelpcdn.com/ephoto/neCB4yzIgNIDvIcEwnMDwA/o.jpg	0	f	f	f	40.7829293	-73.9524223	Jordi Savall, viola da gamba & Hespèrion XXI: Music of Fire & Love	NY	2025-04-11T21:30:00	2025-04-11T19:30:00	10128	0
fe7c79e8-baa0-45a9-86f3-8dd0b6542b10	1535 Bdwy	1	kids-family	New York	Kids & Family	Spring into Easter this year at the New York Marriott Marquis for the Very Eggcellent Easter Brunch Buffet, filled with festive fun for every-bunny in the...	https://www.yelp.com/events/new-york-very-eggcellent-easter-brunch-at-the-new-york-marriott-marquis?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:11:00.044446	https://s3-media1.fl.yelpcdn.com/ephoto/gkBxyk4_NDqODRy8xL5LlA/o.jpg	0	f	f	f	40.7588096563822	-73.98583093226095	Very Eggcellent Easter Brunch at the New York Marriott Marquis	NY	2025-04-20T13:30:00	2025-04-20T10:00:00	10036	0
3a923af8-3989-464a-8b75-d2be48125191	1395 Lexington Ave, New York, NY 10128	1	music	New York	Music	The 92nd Street Y, New York (92NY), one of New York's leading cultural venues, presents Clayton Stephenson, piano on Friday, April 18, 2025 at 7:30pm ET at...	https://www.yelp.com/events/new-york-92ny-presents-clayton-stephenson-piano?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:11:00.047761	https://s3-media2.fl.yelpcdn.com/ephoto/tZt3-P_Wh40CDtgswqKnpA/o.jpg	0	f	f	f	40.7829293	-73.9524223	92NY presents Clayton Stephenson, piano	NY	2025-04-18T21:30:00	2025-04-18T19:30:00	10128	0
3f536846-71bc-4b13-af5f-fadc9b85d35f	983 1st Ave	1	nightlife	New York	Other	Beginner workshops are perfect for you to grab your friends, grab a drink and come make tiny trees! We're bringing the awesome art of bonsai out of the...	https://www.yelp.com/events/new-york-bonsai-workshop-at-the-greats-of-craft-2?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:11:00.05047	https://s3-media3.fl.yelpcdn.com/ephoto/cuiDGFOrytGO1hlJCab0nQ/o.jpg	0	f	f	f	40.7563734	-73.9646661	Bonsai Workshop at The Greats of Craft	NY	2025-04-28T20:30:00	2025-04-28T18:30:00	10022	0
161e6c18-73f3-4fe3-8a59-bdb41e9375a6	333 E 47th St	1	music	New York	Music	Hip Hop is a genre of music and culture born in the Bronx in the 1970s. It grew out of park jams and block parties where Black and Latino youth, artists and...	https://www.yelp.com/events/new-york-japanese-hip-hop-a-cross-cultural-conversation-featuring-coma-chi-asuka-and-meccagodzilla?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:11:00.053146	https://s3-media3.fl.yelpcdn.com/ephoto/2WXzY_eQYM4IBRqtrOzMkQ/o.jpg	0	f	f	f	40.752451796014	-73.968356064888	Japanese Hip Hop: A Cross-Cultural Conversation featuring COMA-CHI, ASUKA & MeccaGodZilla	NY	2025-04-22T21:00:00	2025-04-22T19:00:00	10017	0
1c3e3554-91fa-4de6-88be-27ea537c607f	299 Bowery	1	food-and-drink	New York	Food & Festival	KOBANO (299 Bowery) is set to deliver an unforgettable Japanese-Brazilian brunch experience that blends the vibrant flavors of both cuisines with the...	https://www.yelp.com/events/new-york-experience-the-carnival-brunch-at-kobano-on-4-13?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:11:00.057981	https://s3-media1.fl.yelpcdn.com/ephoto/WQjjEQgByaiXj9FqJdbWXg/o.jpg	0	f	f	f	40.72449222091526	-73.99216109999999	Experience the Carnival Brunch at KOBANO on 4/13	NY	2025-04-13T14:00:00	2025-04-13T12:00:00	10013	0
ab1d6432-be58-471c-bac9-7195aec24a3d	145 Bowery	1	other	New York	Other	Get ready for a night of mind-blowing illusions, electrifying music, and top-tier cocktails. Doors open at 8 PM, show starts at 9 PM--don't miss the magic!	https://www.yelp.com/events/new-york-magic-remixed-with-elliot-zimet-2?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:11:00.061452	https://s3-media2.fl.yelpcdn.com/ephoto/DaUvVsLFLN87diFoYLtxwA/o.jpg	0	f	f	f	40.71895689999999	-73.9944117	Magic Remixed with Elliot Zimet	NY	2025-04-23T22:00:00	2025-04-23T20:00:00	10002	0
775c7508-4bc6-49f5-961b-ac10dd9e7d05	145 Bowery	1	food-and-drink	New York	Food & Festival	One day only! Swing by to check out the viral sensation, Gnocchi on 9th, out front at Moxy LES! They'll be serving up their famous Gnocchi Alla Vodka with...	https://www.yelp.com/events/new-york-food-pop-up-gnocchi-on-9th?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:11:00.064374	https://s3-media1.fl.yelpcdn.com/ephoto/dx3-WXM7oapgFQWrwazvqQ/o.jpg	0	f	f	f	40.71895689999999	-73.9944117	Food Pop-Up: Gnocchi on 9th	NY	2025-04-26T17:00:00	2025-04-26T12:00:00	10002	0
bfddcd1b-f4c4-4929-9284-81d9f9b66545	145 Bowery	1	food-and-drink	New York	Food & Festival	This month's book is My Half LatinX Kitchen by Kiera Wright-Ruiz. A celebration of all of her identities, the book includes recipes from Ecuador, Peru,...	https://www.yelp.com/events/new-york-cookbook-club-my-half-latinx-kitchen-by-kiera-wright-ruiz?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:11:00.067276	https://s3-media1.fl.yelpcdn.com/ephoto/0OBosChjjS_9hZWMvNvXDA/o.jpg	0	f	f	f	40.71895689999999	-73.9944117	Cookbook Club: My Half LatinX Kitchen by Kiera Wright-Ruiz	NY	2025-04-27T15:00:00	2025-04-27T12:00:00	10002	0
8e35af05-0203-43c5-912a-2f2f733cab9a	11 West 53rd St	1	visual-arts	New York	Art & Fashion	The Museum of Modern Art announces Woven Histories: Textiles and Modern Abstraction from April 20 - September 13, 2025.\nTextiles touch every aspect of our...	https://www.yelp.com/events/new-york-the-museum-of-modern-art-presents-woven-histories-textiles-and-modern-abstraction-april-20-september-13-2025?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:11:00.07055	https://s3-media4.fl.yelpcdn.com/ephoto/XlzyuKhtREOs18v1SZix0Q/o.jpg	0	f	f	f	40.761441	-73.977625	THE MUSEUM OF MODERN ART PRESENTS Woven Histories: Textiles and Modern Abstraction APRIL 20 - SEPTEMBER 13, 2025	NY	2025-04-20T02:00:00	2025-04-20T00:00:00	10019	0
c3dc3718-16b2-4a14-a13f-8a49b290419c	11 West 53rd St	1	visual-arts	New York	Art & Fashion	The Museum of Modern Art announces the major film series The Lady at 100: Columbia Classic from the Locarno Film Festival from April 16 - May 24, 2025.\nIn...	https://www.yelp.com/events/new-york-the-museum-of-modern-art-presents-the-lady-at-100-columbia-classics-from-the-locarno-film-festival-april-16-may-24-2025?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:11:00.074239	https://s3-media1.fl.yelpcdn.com/ephoto/5diOLNDlqUbcJC6ET7dLfg/o.jpg	0	f	f	f	40.761441	-73.977625	THE MUSEUM OF MODERN ART PRESENTS The Lady at 100: Columbia Classics from the Locarno Film Festival APRIL 16 - MAY 24, 2025	NY	2025-04-16T02:00:00	2025-04-16T00:00:00	10019	0
ce0bcdac-b3d9-465c-94ee-34de4603270e	548 W 28th St	1	visual-arts	New York	Art & Fashion	FELLOW IMAGINARIES\nA Collaborative Installation by \nCAROLE d'INVERNO and SUSAN ROSTOW\nwith Incidental Music by BILL FRISELL\nApril 22 - May 10, 2025\n\nOpening...	https://www.yelp.com/events/new-york-fellow-imaginaries?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:11:00.077395	https://s3-media3.fl.yelpcdn.com/ephoto/kqzcirc-ur6AZuUbPUApKg/o.jpg	0	f	t	f	40.7516157	-74.00397579999999	fellow imaginaries	NY	2025-04-24T20:00:00	2025-04-24T17:00:00	10001	0
49fb3dfc-e6c2-4202-9b64-a8c45cb607a5	660 12th Ave, New York, NY 10019	1	other	New York	Other	The Children's Museum of Manhattan (CMOM) announces its annual gala honoree, Founding Board Chair and one of New York City's most influential philanthropic...	https://www.yelp.com/events/new-york-childrens-museum-of-manhattan-to-honor-founding-board-chair-laurie-m-tisch-at-2025-annual-gala-giant-steps?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:11:00.08091	https://s3-media1.fl.yelpcdn.com/ephoto/d5JnXc2pL7B0YBGT5dY2gA/o.jpg	0	f	f	f	40.7657202	-73.99719000000002	Children's Museum of Manhattan to Honor Founding Board Chair Laurie M. Tisch at 2025 Annual Gala, Giant Steps	NY	2025-04-24T20:30:00	2025-04-24T18:30:00	10019	0
3f68c146-c8d1-49fc-b12f-242731064723	112 E 11th St	1	other	New York	Other	We're opening up Suite Jane for an incredible comedy show featuring some of NYC's top comedic talents. Host Mark "Jiggy" Jigarjian is bringing the talent...	https://www.yelp.com/events/new-york-fresh-towels-a-comedy-show-20?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:11:00.084109	https://s3-media1.fl.yelpcdn.com/ephoto/ULmijgqibPRS5y8LyyUvvA/o.jpg	0	f	f	f	40.731601	-73.989509	Fresh Towels: A Comedy Show!	NY	2025-04-16T22:00:00	2025-04-16T20:00:00	10003	0
fab4092f-c90b-4475-823a-1a10dec07c5c	112 E 11th St	1	performing-arts	New York	Art & Fashion	Join us at Alphabet Bar for a night of naughty games with The Nuyorican Bombshell and her guest performers who are calling balls (for Bingo!). Drinks will...	https://www.yelp.com/events/new-york-burlesque-bingo-stripped-at-alphabet-bar-13?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:11:00.08685	https://s3-media2.fl.yelpcdn.com/ephoto/9PgCuZODOZ8sCPpfIGrLBg/o.jpg	0	f	t	f	40.731601	-73.989509	Burlesque Bingo - Stripped at Alphabet Bar	NY	2025-04-28T22:00:00	2025-04-28T20:00:00	10003	0
8b77bad9-3420-4c1a-8048-afc71fefb0a5	485 7th Ave	1	other	New York	Other	Blind contour is a method of drawing in which the artist draws a line and never looks at the paper, making every sketch unique.\nSketches are given on a...	https://www.yelp.com/events/new-york-blind-contour-sketches-35?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:11:00.089618	https://s3-media3.fl.yelpcdn.com/ephoto/FDX8bGAQsiRdfgAypdpIfg/o.jpg	0	f	t	f	40.752313414771116	-73.98912051048391	Blind Contour Sketches	NY	2025-04-11T19:00:00	2025-04-11T17:00:00	10018	0
8071ab8c-ffb2-4d40-bf28-ac96897f0f21	485 7th Ave	1	other	New York	Other	Get inked and/or gemmed while you have a blast at the Crystal Ink pop-up, the ultimate women-owned and operated tattoo parlor on 2/13! At this event, you...	https://www.yelp.com/events/new-york-tattoo-pop-up-with-crystal-ink-2?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:11:00.092435	https://s3-media2.fl.yelpcdn.com/ephoto/g9-TejjeEk7_HqE_qbi2EQ/o.jpg	0	f	t	f	40.752313414771116	-73.98912051048391	tattoo pop-up with crystal ink!	NY	2025-04-25T19:00:00	2025-04-25T17:00:00	10018	0
2c0ecdaf-1cdf-440e-80e8-7d912ffd847b	105 West 28th Street	1	performing-arts	New York	Art & Fashion	Get ready for an unforgettable night of Drag Bingo at Moxy Chelsea with the one and only Sparkle Monster! Join us for an evening filled with laughter,...	https://www.yelp.com/events/new-york-drag-bingo-with-sparkle-monster-24?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:11:00.097146	https://s3-media1.fl.yelpcdn.com/ephoto/a_lgt010Thd5BRvuiJl9Fg/o.jpg	0	f	t	f	40.7464345	-73.9909258	Drag Bingo with Sparkle Monster	NY	2025-04-14T23:00:00	2025-04-14T21:00:00	10001	0
c55b9c26-7540-43f6-a579-cda983794405	105 West 28th Street	1	music	New York	Music	Lobby Sounds with Marshal Herridge Duo\nJoin us for an unforgettable evening of live music at the Moxy Chelsea! Get ready to groove and sway as the talented...	https://www.yelp.com/events/new-york-lobby-sounds-with-marshal-herridge-duo-6?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:11:00.10004	https://s3-media1.fl.yelpcdn.com/ephoto/fczqpDybHBdD-31X94h6ug/o.jpg	0	f	t	f	40.7464345	-73.9909258	Lobby Sounds with Marshal Herridge Duo	NY	2025-04-16T22:00:00	2025-04-16T20:00:00	10001	0
6d7d5493-a0d4-4bdb-a3a6-0826044562c7	105 West 28th Street	1	performing-arts	New York	Art & Fashion	Come join us for a fabulous night of glitz, glamour, and fierce competition as we watch the finale of Ru Paul's Drag Race together! Hosted by Atomic Annie...	https://www.yelp.com/events/new-york-ru-paul-drag-race-finale-watch-party?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:11:00.103764	https://s3-media1.fl.yelpcdn.com/ephoto/0qb4Mv_cuempyMv5EIoPCw/o.jpg	0	f	f	f	40.7464345	-73.9909258	ru paul drag race finale watch party	NY	2025-04-18T22:00:00	2025-04-18T20:00:00	10001	0
47937bcc-a739-4f88-85ad-f8b0c61acae8	105 West 28th Street	1	other	New York	Other	Sip & Paint with Nina Molloy\nCome join us for a fun and creative evening at the Sip & Paint with Nina Molloy event! Get ready to unleash your inner artist...	https://www.yelp.com/events/new-york-sip-and-paint-with-nina-molloy-5?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:11:00.107655	https://s3-media3.fl.yelpcdn.com/ephoto/xaYZX7g9T4oL56L2Y_FHTg/o.jpg	0	f	f	f	40.7464345	-73.9909258	Sip & Paint with Nina Molloy	NY	2025-04-29T21:00:00	2025-04-29T19:00:00	10001	0
4642a517-af44-4c22-9f5a-71f5a3b56f7c	15 W 28th Street	1	kids-family	New York	Kids & Family	Game Changers is a vibrant, 90s pop-infused musical that transports audiences to the golden era of kid game shows. Packed with catchy tunes, the show...	https://www.yelp.com/events/new-york-game-changers-the-musical?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:11:00.110382	https://s3-media1.fl.yelpcdn.com/ephoto/HO4_a8wgFneQf4UldSbU0Q/o.jpg	0	f	f	f	40.7452061	-73.98823709999999	Game Changers The Musical	NY	2025-04-26T20:00:00	2025-04-26T19:00:00	10001	0
4c4297a9-d73a-4f7a-b3f6-33f2b3d889fe	621 WEST 46th St, BETWEEN 11th & 12th AV	1	nightlife	New York	Other	Brunch Dreams \nat\nThe Stafford Room\n621 West 46th Street\nNew York, NY 10036\n*\nDoors Open at 2pm\n*\nAll Day Brunch\nBrunch Seatings (2pm-4pm) OR (4pm-6pm),...	https://www.yelp.com/events/new-york-brunch-dreams-the-1-brunch-and-day-party-every-sunday-9?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:11:00.114585	https://s3-media2.fl.yelpcdn.com/ephoto/iyCjJDjE80tCteJyBy6b9Q/o.jpg	0	f	t	f	40.7640021	-73.99763589999999	Brunch Dreams - The #1 Brunch and Day Party Every Sunday	NY	2025-04-13T22:00:00	2025-04-13T14:00:00	10036	0
e4033d53-43ab-494d-a02e-d617077c9314	213 Water St	1	other	New York	Other	South Street Seaport Museum announces public programs and events that give the opportunity for an enriched experience in the new exhibition Maritime City....	https://www.yelp.com/events/new-york-south-street-seaport-museum-announces-public-programs-in-new-exhibition-maritime-city?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:11:00.11736	https://s3-media2.fl.yelpcdn.com/ephoto/JIajrN8Hd6-7nMmdISKYTg/o.jpg	0	f	t	f	40.707441	-74.0033792	South Street Seaport Museum Announces Public Programs in New Exhibition Maritime City	NY	2025-04-25T19:00:00	2025-04-25T17:00:00	10038	0
53b7378e-ec72-4e18-b85c-6587aaa1ade3	318 W 53rd St	1	performing-arts	New York	Art & Fashion	The Broadway Magic Hour, featuring master magicians Jim Vines and Carl Mercurio brings mind-boggling mysteries and hilarious comedy for all ages to the...	https://www.yelp.com/events/new-york-broadway-magic-hour-with-jim-vines-and-carl-mercurio?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:11:00.156101	https://s3-media3.fl.yelpcdn.com/ephoto/SPOGP_Qa7a9Qd9jTtKxvnQ/o.jpg	0	f	f	f	40.7643764	-73.9856153	Broadway Magic Hour with Jim Vines and Carl Mercurio	NY	2025-05-03T15:00:00	2025-05-03T14:00:00	10019	0
73ac1cab-e68f-482c-a851-5c222112243b	34 E 52nd St	1	food-and-drink	New York	Food & Festival	Fresco by Scotto, a beloved NYC institution since 1993 and renowned for its vibrant atmosphere, power lunches, hearty portions, and incredible service, will...	https://www.yelp.com/events/new-york-fresco-by-scotto-gets-kind-of-wild-with-food-network-star-katie-lee?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:11:00.120232	https://s3-media1.fl.yelpcdn.com/ephoto/kE1hnOj1NBaATaDsHH0Ggg/o.jpg	0	f	f	f	40.758896	-73.97444438329205	Fresco by Scotto Gets Kind of Wild with Food Network Star Katie Lee	NY	2025-04-11T22:00:00	2025-04-11T17:00:00	10022	0
ab05774e-4226-4bf6-aab9-f66fb4701c9a	1887 Broadway, New York, NY 10023	1	performing-arts	New York	Art & Fashion	EPIC Players Theatre, New York's Premiere Neurodiverse Theater Company, offers a sample of the 2025 EPIC Season in the Big Umbrella Festival on Saturday,...	https://www.yelp.com/events/new-york-epic-players-theatre-at-lincoln-centers-big-umbrella-festival?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:11:00.12355	https://s3-media4.fl.yelpcdn.com/ephoto/mCm0QHddZ4F6GYwNiylxWQ/o.jpg	0	f	t	f	40.7711387	-73.9826413	EPIC Players Theatre at Lincoln Center's Big Umbrella Festival	NY	2025-04-12T13:00:00	2025-04-12T11:00:00	10023	0
4f038b04-57ad-4c07-8a6b-67542e86f531		1	performing-arts	New York	Art & Fashion	A dependable haven for artists in isolation, Theater Resources Unlimited (TRU) is now in its fourth year of non-stop weekly Community Gatherings this...	https://www.yelp.com/events/new-york-theater-resources-unlimited-community-gathering-what-auditions-look-like-to-the-people-who-do-the-casting?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:11:00.127366	https://s3-media4.fl.yelpcdn.com/ephoto/X0mT9LElzLw4DyUQhIWqEQ/o.jpg	0	f	f	f	40.713	-74.0072	Theater Resources Unlimited Community Gathering: What Auditions Look Like to the People Who Do the Casting	NY	2025-04-11T19:00:00	2025-04-11T17:00:00		0
ba9634b7-0b39-45c2-bbb8-558e5c9f74a6	213 Water St	1	performing-arts	New York	Art & Fashion	South Street Seaport Museum announces Queer History: "Nauti" and Nice Comedy Show on Thursday, April 17, 2025 at 7pm at 213 Water Street. Tickets are $15...	https://www.yelp.com/events/new-york-south-street-seaport-museum-announces-queer-history-nauti-and-nice-comedy-show?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:11:00.130607	https://s3-media1.fl.yelpcdn.com/ephoto/mjHbmj902IGqGin5Dy4gzA/o.jpg	0	f	f	f	40.707441	-74.0033792	South Street Seaport Museum Announces Queer History: "Nauti" and Nice Comedy Show	NY	2025-04-17T21:00:00	2025-04-17T19:00:00	10038	0
31a26949-0524-465c-8334-2d5ac189d56b	1395 Lexington Ave, New York, NY 10128	1	music	New York	Music	The 92nd Street Y, New York (92NY), one of New York's leading cultural venues, presents Caroline Shaw, viola/vocals & Gabriel Kahane, piano/vocals in the...	https://www.yelp.com/events/new-york-92ny-presents-caroline-shaw-viola-vocals-and-gabriel-kahane-piano-vocals?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:11:00.133427	https://s3-media4.fl.yelpcdn.com/ephoto/VUxk1IH63PtnSfSpUyttUA/o.jpg	0	f	f	f	40.7829293	-73.9524223	92NY presents Caroline Shaw, viola/vocals & Gabriel Kahane, piano/vocals	NY	2025-04-25T21:30:00	2025-04-25T19:30:00	10128	0
8dfb5d0f-e069-43ac-8351-b568a072f8af	502 W 53rd Street	1	performing-arts	New York	Art & Fashion	EPIC Players Theatre, New York's Premiere Neurodiverse Theater Company, presents Seussical the Musical from May 8-18, 2025 at ART/NY The Mezzanine Theatre,...	https://www.yelp.com/events/new-york-epic-players-theatre-presents-seussical-the-musical-a-neuro-inclusive-production?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:11:00.136216	https://s3-media2.fl.yelpcdn.com/ephoto/mgPh5e1-seDNaY-rfTLO0g/o.jpg	0	f	f	f	40.7667022	-73.9909851	EPIC Players Theatre presents Seussical the Musical: A Neuro-Inclusive Production	NY	2025-05-08T21:00:00	2025-05-08T19:00:00	10019	0
71479dee-2010-46af-b2e2-dc1366a3f47d		1	other	New York	Other	South Street Seaport Museum announces the OneThrow Yo-Yo Contest on Friday, April 19, 2025, from 11:30am to 4:30pm, aboard the 1885 tall ship Wavertree....	https://www.yelp.com/events/new-york-south-street-seaport-museum-announces-onethrow-yo-yo-contest-aboard-wavertree?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:11:00.139702	https://s3-media1.fl.yelpcdn.com/ephoto/E0KmGZK9d3-viSaCEoS5EQ/o.jpg	0	f	t	f	40.709244	-74.002798	South Street Seaport Museum Announces OneThrow Yo-Yo Contest Aboard Wavertree	NY	2025-04-19T16:30:00	2025-04-19T11:30:00	10038	0
4e941617-ee8c-462f-99c5-832b7e8a4bfd	1650 Broadway	1	nightlife	New York	Other	Get ready to laugh at Stand Up, Downstairs At The Iridium, a live comedy event series right in the heart of Times Square. The Iridium is an iconic...	https://www.yelp.com/events/new-york-stand-up-downstairs-at-the-iridium-2?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:11:00.142999	https://s3-media1.fl.yelpcdn.com/ephoto/FXiHMfj7rkepy0l57rUSGg/o.jpg	0	f	f	f	40.76193158	-73.9835072	Stand Up, Downstairs At The Iridium	NY	2025-04-23T21:30:00	2025-04-23T19:30:00	10019	0
40015097-eae1-453b-9223-f20e522f6bb2	655 W 34th St	1	sports-active-life	New York	Sports & Active	Get ready for high-adrenaline test tracks and groundbreaking EVs as Stellantis Brands--including Jeep®, Ram Trucks, Dodge, Chrysler, and FIAT--bring...	https://www.yelp.com/events/new-york-experience-the-future-of-driving-jeep-ram-trucks-dodge-chrysler-and-fiat-at-the-2025-new-york-auto-show?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:11:00.146757	https://s3-media3.fl.yelpcdn.com/ephoto/TiiEVi_jGOMiG1rQLF33xg/o.jpg	0	f	f	f	40.757972290646	-74.00233975	Experience the Future of Driving: Jeep®, Ram Trucks, Dodge, Chrysler, and FIAT at the 2025 New York Auto Show	NY	2025-04-18T14:00:00	2025-04-18T12:00:00	10001	0
07e48f51-1329-4a28-a58d-301df4a4a038		1	other	New York	Other	South Street Seaport Museum announces the 2025 sailing season for the 1885 Schooner Pioneer beginning in May and running through October. Tickets range from...	https://www.yelp.com/events/new-york-south-street-seaport-museum-announces-2025-pioneer-sailing-season?adjust_creative=UsedPYIGlufC6qTJOAYe7g&utm_campaign=yelp_api_v3&utm_medium=api_v3_event_search&utm_source=UsedPYIGlufC6qTJOAYe7g	2025-04-11 14:11:00.152457	https://s3-media3.fl.yelpcdn.com/ephoto/8thjIEtcp2z7L6mKpsFvJg/o.jpg	0	f	f	f	40.713	-74.0072	South Street Seaport Museum Announces 2025 Pioneer Sailing Season	NY	2025-05-01T14:00:00	2025-05-01T12:00:00		0
\.


--
-- Data for Name: itinerary_saved; Type: TABLE DATA; Schema: public; Owner: myuser
--

COPY public.itinerary_saved (id, user_id, start_date, end_date) FROM stdin;
1	1	2025-04-15	2025-04-21
2	1	2025-04-15	2025-04-21
3	1	2025-04-16	2025-04-17
4	1	2025-04-16	2025-04-17
\.


--
-- Data for Name: itinerary_saved_items; Type: TABLE DATA; Schema: public; Owner: myuser
--

COPY public.itinerary_saved_items (id, itinerary_id, item_id, event_id, is_event, start_time, end_time) FROM stdin;
1	1	141	\N	f	2025-04-15 09:00:00	2025-04-15 11:00:00
2	1	\N	4835cdf5-0079-41e1-a212-9b57db542ae7	t	2025-04-16 20:00:00	2025-04-16 22:00:00
3	1	111	\N	f	2025-04-17 11:00:00	2025-04-17 13:00:00
4	2	141	\N	f	2025-04-15 09:00:00	2025-04-15 11:00:00
5	2	111	\N	f	2025-04-16 11:00:00	2025-04-16 13:00:00
6	3	\N	eb8a707e-3e5c-4d19-8c6e-7aca90c5c965	t	2025-04-16 00:00:00	2025-04-16 02:00:00
7	3	\N	aa729185-3a0b-441c-8123-85a19e531654	t	2025-04-16 18:30:00	2025-04-16 20:30:00
8	4	141	\N	f	2025-04-16 09:00:00	2025-04-16 11:00:00
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: myuser
--

COPY public.users (id, email, password, created_at, salt) FROM stdin;
1	luosha1015@gmail.com	2a78a308a8c087537ae850ecc2d1801d751665197d7ab7280a9d72f896560d70	2025-04-10 20:38:42.18287	oPNKyn8B/S/QwiUtfmARMw==
\.


--
-- Data for Name: weather_data; Type: TABLE DATA; Schema: public; Owner: myuser
--

COPY public.weather_data (id, all_clouds, cod, dt, fetch_time, feels_like, humidity, pressure, temperature, temp_max, temp_min, rain_1h, rain_3h, snow_1h, snow_3h, sunrise, sunset, timezone, visibility, weather_description, weather_icon, weather_id, weather_main, wind_deg, wind_gust, wind_speed) FROM stdin;
24508c06-2b21-4632-a83f-f7a33073b74d	0	200	1717431315	2024-06-03 17:15:15.327959	301.26	42	1014	301.48	303.74	298.49	\N	\N	\N	\N	1717406754	1717460558	-14400	10000	clear sky	01d	800	Clear	0	0	3.09
4be00be3-b77f-4990-8b68-d1efc0a843c2	0	200	1717431116	2024-06-03 17:15:15.298506	301.23	42	1014	301.45	303.74	298.49	\N	\N	\N	\N	1717406754	1717460558	-14400	10000	clear sky	01d	800	Clear	0	0	3.09
c71d9255-4f33-4372-a6b2-2996dd741a4e	0	200	1717431116	2024-06-03 17:18:15.155451	301.23	42	1014	301.45	303.74	298.49	\N	\N	\N	\N	1717406754	1717460558	-14400	10000	clear sky	01d	800	Clear	0	0	3.09
cfaea117-c9d3-447b-83e4-4afec33845f2	0	200	1717431315	2024-06-03 17:21:15.175183	301.26	42	1014	301.48	303.74	298.49	\N	\N	\N	\N	1717406754	1717460558	-14400	10000	clear sky	01d	800	Clear	0	0	3.09
\.


--
-- Name: itinerary_saved_id_seq; Type: SEQUENCE SET; Schema: public; Owner: myuser
--

SELECT pg_catalog.setval('public.itinerary_saved_id_seq', 4, true);


--
-- Name: itinerary_saved_items_id_seq; Type: SEQUENCE SET; Schema: public; Owner: myuser
--

SELECT pg_catalog.setval('public.itinerary_saved_items_id_seq', 8, true);


--
-- Name: itinerary_saved_user_id_seq; Type: SEQUENCE SET; Schema: public; Owner: myuser
--

SELECT pg_catalog.setval('public.itinerary_saved_user_id_seq', 1, false);


--
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: myuser
--

SELECT pg_catalog.setval('public.users_id_seq', 1, true);


--
-- Name: daily_forecast_data daily_forecast_data_pkey; Type: CONSTRAINT; Schema: public; Owner: myuser
--

ALTER TABLE ONLY public.daily_forecast_data
    ADD CONSTRAINT daily_forecast_data_pkey PRIMARY KEY (id);


--
-- Name: events events_pkey; Type: CONSTRAINT; Schema: public; Owner: myuser
--

ALTER TABLE ONLY public.events
    ADD CONSTRAINT events_pkey PRIMARY KEY (id);


--
-- Name: itinerary_saved_items itinerary_saved_items_pkey; Type: CONSTRAINT; Schema: public; Owner: myuser
--

ALTER TABLE ONLY public.itinerary_saved_items
    ADD CONSTRAINT itinerary_saved_items_pkey PRIMARY KEY (id);


--
-- Name: itinerary_saved itinerary_saved_pkey; Type: CONSTRAINT; Schema: public; Owner: myuser
--

ALTER TABLE ONLY public.itinerary_saved
    ADD CONSTRAINT itinerary_saved_pkey PRIMARY KEY (id);


--
-- Name: users users_email_key; Type: CONSTRAINT; Schema: public; Owner: myuser
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_email_key UNIQUE (email);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: myuser
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: weather_data weather_data_pkey; Type: CONSTRAINT; Schema: public; Owner: myuser
--

ALTER TABLE ONLY public.weather_data
    ADD CONSTRAINT weather_data_pkey PRIMARY KEY (id);


--
-- Name: idx_event_id; Type: INDEX; Schema: public; Owner: myuser
--

CREATE INDEX idx_event_id ON public.itinerary_saved_items USING btree (event_id);


--
-- Name: idx_item_id; Type: INDEX; Schema: public; Owner: myuser
--

CREATE INDEX idx_item_id ON public.itinerary_saved_items USING btree (item_id);


--
-- Name: idx_itinerary_id; Type: INDEX; Schema: public; Owner: myuser
--

CREATE INDEX idx_itinerary_id ON public.itinerary_saved_items USING btree (itinerary_id);


--
-- Name: itinerary_saved_items itinerary_saved_items_itinerary_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: myuser
--

ALTER TABLE ONLY public.itinerary_saved_items
    ADD CONSTRAINT itinerary_saved_items_itinerary_id_fkey FOREIGN KEY (itinerary_id) REFERENCES public.itinerary_saved(id);


--
-- Name: itinerary_saved itinerary_saved_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: myuser
--

ALTER TABLE ONLY public.itinerary_saved
    ADD CONSTRAINT itinerary_saved_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- PostgreSQL database dump complete
--

