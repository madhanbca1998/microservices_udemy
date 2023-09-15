FROM node:alpine

WORKDIR /coin-flip-api

COPY package.json .

COPY package-lock.json .

RUN npm install

COPY server.js .
COPY counter.js .
COPY defaultMetrics.js .
COPY gauge.js .
COPY histogram.js .
COPY summary.js .

EXPOSE 8080

CMD ["node", "server.js"]