package org.springbus;

import com.google.common.base.Strings;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Swagger;
import io.swagger.models.Tag;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponents;
import springfox.documentation.service.Documentation;
import springfox.documentation.spring.web.DocumentationCache;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.mappers.ServiceModelToSwagger2Mapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Strings.isNullOrEmpty;
import static springfox.documentation.swagger.common.HostNameProvider.componentsFrom;

public class ApiDocController {

    private static DocumentationCache documentationCache = new DocumentationCache();

    @RequestMapping("/v2/api-docs3")
    @ResponseBody
    public Swagger doc(DocumentationCache documentationCache, ServiceModelToSwagger2Mapper mapper,
                       HttpServletRequest servletRequest, HttpServletResponse httpServletResponse) {


        String groupName = Docket.DEFAULT_GROUP_NAME;

        Swagger swagger = null;

        Documentation documentation = documentationCache.documentationByGroup(groupName);
        if (documentation == null) {

        } else {
            swagger = mapper.mapDocumentation(documentation);
            UriComponents uriComponents = componentsFrom(servletRequest, swagger.getBasePath());
            swagger.basePath(Strings.isNullOrEmpty(uriComponents.getPath()) ? "/" : uriComponents.getPath());
            if (isNullOrEmpty(swagger.getHost())) {
                swagger.host(hostName(uriComponents));
            }
            List<Tag> tags = swagger.getTags();
            if (tags != null) {
                for (Tag t : tags) {

                }
            }
            Map<String, Path> pathMap = swagger.getPaths();
            if (pathMap != null) {
                Iterator<Path> pathIterator = pathMap.values().iterator();
                while (pathIterator.hasNext()) {
                    Path p = pathIterator.next();
                    Operation operation = p.getPost();
                    if (operation != null) {
                        List<String> operationTags = operation.getTags();
                        List<String> newTagList = new ArrayList<>();
                        if (!StringUtils.isEmpty(operationTags)) {
                            for (String name : operationTags) {

                            }
                        }
                        operation.setTags(newTagList);
                    }
                }
            }


        }
        return swagger;
    }

    private static String hostName(UriComponents uriComponents) {

        String host = uriComponents.getHost();
        int port = uriComponents.getPort();
        if (port > -1) {
            return String.format("%s:%d", host, port);
        }
        return host;

    }

}

