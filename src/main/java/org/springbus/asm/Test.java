package org.springbus.asm;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springbus.mapper.UrlMapping;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.management.DescriptorKey;

public class Test {


    @Value(value ="测试")
    @UrlMapping("/operator/order/editDeliveryInfo")

    @ApiOperation(value = "编辑发货信息xxxxxx", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam( allowMultiple = true ,name = "editDeliveryInfoDTOList", value = "List<EditDeliveryInfoDTO>对象", required = true, defaultValue ="null", dataType = "List<EditDeliveryInfoDTO>",
                    dataTypeClass = Test.class,
                    paramType = "body")
    })
    public  void doIt( @RequestParam  @RequestBody  int  a){

    }

    public static void main(String[] args) {
        Test  t=new Test() ;
        t.doIt(1);
        System.out.println(t);
    }
}
