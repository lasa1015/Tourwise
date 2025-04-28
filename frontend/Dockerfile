# 阶段一：build 前端代码
FROM node:hydrogen-alpine3.20 as build

WORKDIR /app

# 这里告诉 Docker，有一个变量是要传进来的
ARG REACT_APP_GOOGLE_MAPS_API_KEY

COPY package*.json ./
RUN npm install
COPY . .

# build 时注入 REACT_APP_GOOGLE_MAPS_API_KEY
RUN REACT_APP_GOOGLE_MAPS_API_KEY=$REACT_APP_GOOGLE_MAPS_API_KEY npm run build

# 阶段二：拷贝静态文件到 nginx
FROM nginx:alpine
COPY --from=build /app/build /usr/share/nginx/html
COPY nginx.conf /etc/nginx/nginx.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
