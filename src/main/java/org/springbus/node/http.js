const http = require("http");
sever = http.createServer((r, q) => {
    console.log(r.url);
    console.log( '1、客户端请求url：' + r.url );
    console.log( '2、http版本：' + r.httpVersion );
    console.log( '3、http请求方法：' + r.method );
    console.log( '4、http请求头部' + JSON.stringify(r.headers) );
    const t=r.headers
    t['url']=r.url;

    r.on('aborted', function(){
        console.log('2、客户端请求aborted');
    });

    r.on('close', function(){
        console.log('3、客户端请求close');
    });


    q.end(JSON.stringify(t));
});
sever.listen(8090);


