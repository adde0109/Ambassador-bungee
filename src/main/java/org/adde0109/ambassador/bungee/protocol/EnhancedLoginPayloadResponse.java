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

public class EnhancedLoginPayloadResponse extends AbstractPacket {

  public final static List<ProtocolIdMapping> MAPPINGS = Arrays.asList(
          AbstractProtocolMapping.rangedIdMapping(MINECRAFT_1_13, MINECRAFT_LATEST, 0x02)
  );

  private int id;
  private boolean success;
  private ByteBuf data;

  public int getId() {
    return id;
  }

  public EnhancedLoginPayloadResponse() {

  }
  public boolean isSuccess() {
    return success;
  }

  public ByteBuf getData() {
    return data;
  }

  @Override
  public void read(ByteBuf buf, PacketDirection packetDirection, int protocolVersion) {
    this.id = ProtocolUtil.readVarInt(buf);
    this.success = buf.readBoolean();
    if (buf.isReadable()) {
      this.data = buf.readRetainedSlice(buf.readableBytes());
    } else {
      this.data = Unpooled.EMPTY_BUFFER;
    }
  }

  @Override
  public void write(ByteBuf buf, PacketDirection packetDirection, int protocolVersion) {
    ProtocolUtil.writeVarInt(buf,id);
    buf.writeBoolean(success);
    buf.writeBytes(data);
  }
}
