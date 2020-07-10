const http = require("http");
const options = {
    method: "get",
    host: "www.baidu.com",
    path: "/"
};
let client = http.request(options, function (res) {
//console.log(res);
   // res.pipe(process.stdout);
});
client.on("socket", function (s) {
    console.log(s);
});
client.on("pipe" ,function(src) {
    console.log(src);
});

client.end();



