const { createProxyMiddleware } = require('http-proxy-middleware');

console.log('setupProxy.js loaded!');      // 启动时应当打印

module.exports = app => {
  app.use(
    '/api',
    createProxyMiddleware({


      /** 一定换成 127.0.0.1，别再写 localhost */
      target: 'http://127.0.0.1:8080',
      changeOrigin: true,
      ws: true,

      /* 如果接口确实慢，可以把超时时间调大一点，例如 60 秒 */
      timeout: 60000,        // 与浏览器的连接
      proxyTimeout: 60000,   // 与后端的连接
    })
  );
};
