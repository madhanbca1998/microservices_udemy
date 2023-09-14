const client = require('prom-client');
const express = require('express');
const app = express();
// Create a Registry to register the metrics
const register = new client.Registry();
client.collectDefaultMetrics({
    app: 'coin-flip-app',
    prefix: 'node_',
    timeout: 10000,
    gcDurationBuckets: [0.1, 0.2, 0.3, 1, 2, 5],
    register
});

const httpRequestTimer = new client.Histogram({
    name: 'http_request_duration_seconds',
    help: 'Duration of HTTP requests in seconds',
    labelNames: ['method', 'route', 'code'],
    buckets: [0.1, 0.3, 0.5, 0.7, 1, 3, 5, 7, 10] // 0.1 to 10 seconds
  });
  register.registerMetric(httpRequestTimer);

  const createDelayHandler = async (req, res) => {
    if ((Math.floor(Math.random() * 100)) === 0) {
      throw new Error('Internal Error')
    }
    // Generate number between 3-6, then delay by a factor of 1000 (miliseconds)
    const delaySeconds = Math.floor(Math.random() * (6 - 3)) + 3
    await new Promise(res => setTimeout(res, delaySeconds * 1000))
    res.end('Slow url accessed!');
  };
  app.get('/metrics', async (req, res) => {
    // Start the HTTP request timer, saving a reference to the returned method
    const end = httpRequestTimer.startTimer();
    // Save reference to the path so we can record it when ending the timer
    const route = req.route.path;
      
    res.setHeader('Content-Type', register.contentType);
    res.send(await register.metrics());
  
    // End timer and add labels
    end({ route, code: res.statusCode, method: req.method });
  });
  
  // 
  app.get('/slow', async (req, res) => {
    const end = httpRequestTimer.startTimer();
    const route = req.route.path;
    await createDelayHandler(req, res);
    end({ route, code: res.statusCode, method: req.method });
  });


  app.get('/', async (req, res) => {
    const end = httpRequestTimer.startTimer();
    const route = req.route.path;
     res.end("Fast url is accessed");
     end({ route, code: res.statusCode, method: req.method });
  });


  app.get('/flip-coins', async (req, res) => {
    const end = httpRequestTimer.startTimer();
    const route = req.route.path;
    const times=req.query.times;
    if(times && times > 0){
      let heads=0;
      let tails=0;
      for(let i=0;i<times;i++){
let randomNumber=Math.random()
if(randomNumber < 0.5){
  heads++;
}
else{
  tails++;
}
      }
      res.json({heads,tails});
    end({ route, code: res.statusCode, method: req.method });

    }
    else{
      res.end("Choose greater than zero");

    }
  
  });


app.listen(8080, () => console.log('Server is running on http://localhost:8080,'));