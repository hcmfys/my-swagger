//模拟发送http请求
const http = require("http");
http.request('http://www.baidu.com',function (req) {
    console.log(req)
})


