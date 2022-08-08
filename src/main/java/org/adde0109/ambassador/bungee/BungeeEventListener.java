package org.adde0109.ambassador.bungee;

import io.netty.buffer.Unpooled;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.adde0109.ambassador.bungee.protocol.UpstreamLoginPayloadHandler;

public class BungeeEventListener implements Listener {

  private final UpstreamLoginPayloadHandler packetHandler;
  BungeeEventListener(UpstreamLoginPayloadHandler packetHandler) {
    this.packetHandler = packetHandler;
  }

  @EventHandler
  public void onPreLoginEvent(PreLoginEvent event) {
    packetHandler.sendPacket(event.getConnection(),"fml:handshake",Unpooled.EMPTY_BUFFER,(b) -> {
      byte[] array = b;
    });
  }
}
