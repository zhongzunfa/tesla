package   io.github.tesla.gateway.netty.transmit.flow;

import io.github.tesla.gateway.netty.transmit.ConnectionState;
import io.github.tesla.gateway.netty.transmit.connection.ProxyConnection;
import io.github.tesla.gateway.netty.transmit.support.ProxyConnectionLogger;

import io.netty.util.concurrent.Future;


public abstract class ConnectionFlowStep {
  private final ProxyConnectionLogger LOG;
  private final ProxyConnection<?> connection;
  private final ConnectionState state;


  public ConnectionFlowStep(ProxyConnection<?> connection, ConnectionState state) {
    super();
    this.connection = connection;
    this.state = state;
    this.LOG = connection.getLOG();
  }

  public ProxyConnection<?> getConnection() {
    return connection;
  }

  public ConnectionState getState() {
    return state;
  }

  public boolean shouldSuppressInitialRequest() {
    return false;
  }

  public boolean shouldExecuteOnEventLoop() {
    return true;
  }

  @SuppressWarnings("rawtypes")
  public abstract Future execute();

  public void onSuccess(ConnectionFlow flow) {
    flow.advance();
  }

  void read(ConnectionFlow flow, Object msg) {
    LOG.debug("Received message while in the middle of connecting: {}", msg);
  }

  @Override
  public String toString() {
    return state.toString();
  }

}
