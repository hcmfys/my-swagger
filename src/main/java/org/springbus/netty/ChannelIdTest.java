package org.springbus.netty;

import io.netty.channel.DefaultChannelId;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChannelIdTest {

    public  static  void main(String[] args) {
        DefaultChannelId defaultChannelId = DefaultChannelId.newInstance();

        System.out.println("defaultChannelId.asLongText()= " + defaultChannelId.asLongText());
        System.out.println("defaultChannelId.asShortText()= " + defaultChannelId.asShortText());
        log.info("defaultChannelId.toString()= " + defaultChannelId.toString());
    }
}
