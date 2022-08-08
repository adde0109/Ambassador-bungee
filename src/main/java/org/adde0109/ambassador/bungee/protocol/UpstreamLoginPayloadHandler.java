package org.adde0109.ambassador.bungee.protocol;

import dev.simplix.protocolize.api.Direction;
import dev.simplix.protocolize.api.PacketDirection;
import dev.simplix.protocolize.api.Protocol;
import dev.simplix.protocolize.api.Protocolize;
import dev.simplix.protocolize.api.listener.AbstractPacketListener;
import dev.simplix.protocolize.api.listener.PacketReceiveEvent;
import dev.simplix.protocolize.api.listener.PacketSendEvent;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.protocol.DefinedPacket;
import space.vectrix.flare.fastutil.Int2ObjectSyncMap;

import java.security.SecureRandom;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class UpstreamLoginPayloadHandler {
  private final SecureRandom random = new SecureRandom();
  private static final Int2ObjectMap<Consumer<byte[]>> ongoingTransactions = Int2ObjectSyncMap.hashmap();
  private static Supplier<EnhancedLoginPayloadRequest> outgoingPacketSupplier;


  public static class LoginPayloadRequestListener extends AbstractPacketListener<EnhancedLoginPayloadRequest> {

    public LoginPayloadRequestListener() {
      super(EnhancedLoginPayloadRequest.class,Direction.UPSTREAM,0);
    }

    @Override
    public void packetReceive(PacketReceiveEvent<EnhancedLoginPayloadRequest> packetReceiveEvent) {}

    @Override
    public void packetSend(PacketSendEvent<EnhancedLoginPayloadRequest> packetSendEvent) {
      packetSendEvent.packet(outgoingPacketSupplier.get());
    }
  }

  public static class LoginPayloadResponseListener extends AbstractPacketListener<EnhancedLoginPayloadResponse> {

    public LoginPayloadResponseListener() {
      super(EnhancedLoginPayloadResponse.class,Direction.UPSTREAM,0);
    }

    @Override
    public void packetReceive(PacketReceiveEvent<EnhancedLoginPayloadResponse> packetReceiveEvent) {
      EnhancedLoginPayloadResponse packet = packetReceiveEvent.packet();
      if (ongoingTransactions.containsKey(packet.getId()))
        ongoingTransactions.remove(packetReceiveEvent.packet().getId()).accept((packet.isSuccess()) ? ByteBufUtil.getBytes(packet.getData()) : null);
    }

    @Override
    public void packetSend(PacketSendEvent<EnhancedLoginPayloadResponse> packetSendEvent) {}
  }

  public void sendPacket(PendingConnection connection, String channel, ByteBuf data, Consumer<byte[]> consumer) {
    DefinedPacket definedPacket = (DefinedPacket) Protocolize.protocolRegistration().createPacket(EnhancedLoginPayloadRequest.class,
            Protocol.LOGIN, PacketDirection.CLIENTBOUND,connection.getVersion());
    int r;
    do {
      r = random.nextInt();
    } while (ongoingTransactions.containsKey(r));
    int finalR = r;
    outgoingPacketSupplier = () -> new EnhancedLoginPayloadRequest(finalR,channel, data);
    ongoingTransactions.put(finalR,consumer);
    connection.unsafe().sendPacket(definedPacket);
  }

}
