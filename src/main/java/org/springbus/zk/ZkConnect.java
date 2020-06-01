package org.springbus.zk;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.kafka.common.protocol.types.Field;

import java.net.URLDecoder;
import java.util.List;
@Slf4j
public class ZkConnect {

    private static CuratorFramework client;

    public static CuratorFramework getClient() {
        client = CuratorFrameworkFactory.newClient("10.100.163.72:2181",
                new ExponentialBackoffRetry(1000, 3));
        client.start();
        return client;
    }

    public  static  void watchTreePath(CuratorFramework client,String path) throws Exception {
        TreeCache treeCache=new TreeCache(client,path);
        treeCache.getListenable().addListener((curatorFramework, event) -> {
            ChildData data = event.getData();
            if(data !=null){
                switch (event.getType()) {
                    case NODE_ADDED:
                        System.out.println("NODE_ADDED : "+ data.getPath() +"  数据:"+getString(data.getData()));
                        break;
                    case NODE_REMOVED:
                        System.out.println("NODE_REMOVED : "+ data.getPath() +"  数据:"+ getString(data.getData()));
                        break;
                    case NODE_UPDATED:
                        System.out.println("NODE_UPDATED : "+ data.getPath() +"  数据:"+ getString(data.getData()));
                        break;

                    default:
                        break;
                }
            }else{
                System.out.println( "data is null : "+ event.getType());
            }

        });
         treeCache.start();

    }

    private static   String getString(byte[] bytes){
        if(bytes!=null){
            return  new String(bytes);
        }return "";
    }

    /**
     *Cache 的三种实现 实践
     * Path Cache：监视一个路径下1）孩子结点的创建、2）删除，3）以及结点数据的更新。
     * 产生的事件会传递给注册的PathChildrenCacheListener。
     * Node Cache：监视一个结点的创建、更新、删除，并将结点的数据缓存在本地。
     * Tree Cache：Path Cache和Node Cache的“合体”，监视路径下的创建、更新、删除事件，并缓存路径下所有孩子结点的数据。
     *

     * @param client
     * @param path
     * @throws Exception
     */
    public  static  void watchPathChildrenCache(CuratorFramework client,String path) throws Exception {
        PathChildrenCache treeCache=new PathChildrenCache(client,path,false);
        treeCache.start();
        treeCache.getListenable() .addListener((curatorFramework, pathChildrenCacheEvent) -> {

            ChildData data = pathChildrenCacheEvent.getData();

            switch (pathChildrenCacheEvent.getType()) {
                case CHILD_ADDED:
                    System.out.println("CHILD_ADDED : " + data.getPath() + "  数据:" + getString(data.getData()));
                    break;
                case CHILD_REMOVED:
                    System.out.println("CHILD_REMOVED : " + data.getPath() + "  数据:" + getString(data.getData()));
                    break;
                case CHILD_UPDATED:
                    System.out.println("CHILD_UPDATED : " + data.getPath() + "  数据:" + getString(data.getData()));
                    break;

                default:
                    break;
            }
        });
        System.out.println("Register zk watcher successfully!");


    }
private static void getDirList(CuratorFramework client,String path) throws Exception {
        long t1=System.currentTimeMillis();
    System.out.println("path---->"+ URLDecoder.decode( path,"utf-8"));
      List<String> dirList=  client.getChildren().forPath(path);
      for(String dir : dirList) {
          getDirList(client, path+"/"+dir);
      }
    long t2=System.currentTimeMillis();
      log.info("cost time ="+ (t2-t1));
}

    public  static  void testListPath(String p) throws Exception {
        CuratorFramework client = getClient();
        getDirList(client, p);
    }

    public  static  void testCreate(String p) throws Exception {
        CuratorFramework client = getClient();
        new Thread(new Runnable(){


            @Override
            public void run() {
                try {


                    if (client.checkExists().forPath(p) == null) {
                        String path = client.create().creatingParentsIfNeeded().forPath(p);
                        System.out.println("create path=" + path);
                    } else {
                        System.out.println("exists   path=" + p);
                    }
                    watchPathChildrenCache(client, p);
                }catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();

    }


    public static void main(String[] args) throws Exception {
        long t1=System.currentTimeMillis();
        testListPath("/dubbo");
        long t2=System.currentTimeMillis();
        System.out.println(" all cost times ="+(t2-t1) /1000);

    }


}
