package org.adde0109.ambassador.bungee;

import dev.simplix.protocolize.api.PacketDirection;
import dev.simplix.protocolize.api.Protocol;
import dev.simplix.protocolize.api.Protocolize;
import net.md_5.bungee.api.plugin.Plugin;
import org.adde0109.ambassador.bungee.protocol.ServerLoginPayloadEvent;
import org.adde0109.ambassador.bungee.protocol.UpstreamLoginPayloadHandler;
import org.adde0109.ambassador.bungee.protocol.EnhancedLoginPayloadRequest;
import org.adde0109.ambassador.bungee.protocol.EnhancedLoginPayloadResponse;

public class AmbassadorPlugin extends Plugin {


  @Override
  public void onLoad() {

    Protocolize.protocolRegistration().registerPacket(EnhancedLoginPayloadResponse.MAPPINGS, Protocol.LOGIN, PacketDirection.SERVERBOUND, EnhancedLoginPayloadResponse.class);
    Protocolize.protocolRegistration().registerPacket(EnhancedLoginPayloadRequest.MAPPINGS, Protocol.LOGIN, PacketDirection.CLIENTBOUND,EnhancedLoginPayloadRequest.class);

    Protocolize.listenerProvider().registerListener(new UpstreamLoginPayloadHandler.LoginPayloadRequestListener());
    Protocolize.listenerProvider().registerListener(new UpstreamLoginPayloadHandler.LoginPayloadResponseListener());


  }

  @Override
  public void onEnable() {
    getProxy().getPluginManager().registerListener(this, new BungeeEventListener(new UpstreamLoginPayloadHandler()));
  }
}
