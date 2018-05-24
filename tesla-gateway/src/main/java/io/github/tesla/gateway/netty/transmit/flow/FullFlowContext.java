package   io.github.tesla.gateway.netty.transmit.flow;

import io.github.tesla.gateway.netty.transmit.connection.ClientToProxyConnection;
import io.github.tesla.gateway.netty.transmit.connection.ProxyToServerConnection;


public class FullFlowContext extends FlowContext {
  private final String serverHostAndPort;

  public FullFlowContext(ClientToProxyConnection clientConnection,
      ProxyToServerConnection serverConnection) {
    super(clientConnection);
    this.serverHostAndPort = serverConnection.getServerHostAndPort();
  }

  public String getServerHostAndPort() {
    return serverHostAndPort;
  }

}
