package org.adde0109.ambassador.bungee.protocol;

import dev.simplix.protocolize.api.Direction;
import dev.simplix.protocolize.api.listener.AbstractPacketListener;
import dev.simplix.protocolize.api.listener.PacketReceiveEvent;
import dev.simplix.protocolize.api.listener.PacketSendEvent;
import net.md_5.bungee.api.ProxyServer;

public class DownstreamLoginPayloadHandler {


  public static class LoginPayloadRequestListener extends AbstractPacketListener<EnhancedLoginPayloadRequest> {

    public LoginPayloadRequestListener() {
      super(EnhancedLoginPayloadRequest.class, Direction.DOWNSTREAM,0);
    }

    @Override
    public void packetReceive(PacketReceiveEvent<EnhancedLoginPayloadRequest> packetReceiveEvent) {
      ProxyServer.getInstance().getPluginManager().callEvent(new ServerLoginPayloadEvent());
    }

    @Override
    public void packetSend(PacketSendEvent<EnhancedLoginPayloadRequest> packetSendEvent) {}
  }

  public static class LoginPayloadResponseListener extends AbstractPacketListener<EnhancedLoginPayloadResponse> {

    public LoginPayloadResponseListener() {
      super(EnhancedLoginPayloadResponse.class, Direction.DOWNSTREAM, 0);
    }

    @Override
    public void packetReceive(PacketReceiveEvent<EnhancedLoginPayloadResponse> packetReceiveEvent) {}

    @Override
    public void packetSend(PacketSendEvent<EnhancedLoginPayloadResponse> packetSendEvent) {

    }
  }
}
