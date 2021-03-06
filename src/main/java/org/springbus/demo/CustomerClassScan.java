package org.springbus.demo;


import org.jooq.meta.derby.sys.Sys;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class CustomerClassScan {


    //Ant模式通配符的Resource查找器
    ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

    //缓存MetadataReader工厂类
    MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);

    private final List<TypeFilter> includeFilters = new LinkedList<>();
    private final List<TypeFilter> excludeFilters = new LinkedList<>();

    public void loadClass(String basePackage){
        try{
            String packageClass=basePackage.replace(".", "/");
            //增加过滤条件，提取出有Component注解类的资源
            this.includeFilters.add(new AnnotationTypeFilter(Component.class, true, true));

            String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + packageClass+ "/**/*.class";
            //查找到一批资源
            Resource[] resources = resourcePatternResolver.getResources(pattern);
            for(Resource resource : resources){
                if(resource.isReadable()){
                    //获取资源的元数据读取器
                    MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);

                    //对资源进行过滤处理
                    if(isCandidateComponent(metadataReader)){
                        //从元数据中取得类名
                        String className = metadataReader.getAnnotationMetadata().getClassName();
                        //根据类名记载类
                        ClassLoader classLoader=  Thread.currentThread().getContextClassLoader();

                        boolean existis= ClassUtils.isPresent(className,classLoader);
                        if(existis) {
                            Class<?> clazz = ClassUtils.forName(className, classLoader);
                            System.out.println(clazz.getName());
                        }else{
                            System.out.println(" ===> no find "+className);
                        }
                    }
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private boolean isCandidateComponent(MetadataReader metadataReader) throws IOException {
        for (TypeFilter tf : this.excludeFilters) {
            if (tf.match(metadataReader, this.metadataReaderFactory)) {
                return false;
            }
        }
        for (TypeFilter tf : this.includeFilters) {
            if (tf.match(metadataReader, this.metadataReaderFactory)) {
                return true;
            }
        }
        return false;
    }
}
