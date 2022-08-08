package org.adde0109.ambassador.bungee.protocol;

import dev.simplix.protocolize.api.PacketDirection;
import dev.simplix.protocolize.api.mapping.AbstractProtocolMapping;
import dev.simplix.protocolize.api.mapping.ProtocolIdMapping;
import dev.simplix.protocolize.api.packet.AbstractPacket;
import dev.simplix.protocolize.api.util.ProtocolUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.Arrays;
import java.util.List;

import static dev.simplix.protocolize.api.util.ProtocolVersions.*;

public class EnhancedLoginPayloadRequest extends AbstractPacket {
  public final static List<ProtocolIdMapping> MAPPINGS = Arrays.asList(
          AbstractProtocolMapping.rangedIdMapping(MINECRAFT_1_13, MINECRAFT_LATEST, 0x04)
  );

  private int id;
  private String channel;
  private ByteBuf data;

  public EnhancedLoginPayloadRequest() {

  }

  public EnhancedLoginPayloadRequest(int id, String channel, ByteBuf data) {
    this.id = id;
    this.channel = channel;
    this.data = data;
  }

  @Override
  public void read(ByteBuf buf, PacketDirection packetDirection, int protocolVersion) {
    id = ProtocolUtil.readVarInt(buf);
    channel = ProtocolUtil.readString(buf);

    if (buf.isReadable()) {
      data = buf.readRetainedSlice(buf.readableBytes());
    } else {
      data = Unpooled.EMPTY_BUFFER;
    }
  }

  @Override
  public void write(ByteBuf buf, PacketDirection packetDirection, int protocolVersion) {
    ProtocolUtil.writeVarInt(buf,id);
    if (channel == null) {
      throw new IllegalStateException("Channel is not specified");
    }
    ProtocolUtil.writeString(buf, channel);
    buf.writeBytes(data);
  }
}
