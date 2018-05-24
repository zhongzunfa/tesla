package   io.github.tesla.gateway.netty.transmit.flow;

import java.net.InetSocketAddress;

import io.github.tesla.gateway.netty.transmit.connection.ClientToProxyConnection;


public class FlowContext {
  private final InetSocketAddress clientAddress;

  public FlowContext(ClientToProxyConnection clientConnection) {
    super();
    this.clientAddress = clientConnection.getClientAddress();
  }

  public InetSocketAddress getClientAddress() {
    return clientAddress;
  }

}
