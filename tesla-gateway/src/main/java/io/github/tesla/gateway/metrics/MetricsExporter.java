/*
 * Copyright 2014-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package io.github.tesla.gateway.metrics;

import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import io.prometheus.client.Histogram;
import io.prometheus.client.Summary;

/**
 * @author liushiming
 * @version PrometheusPublisher.java, v 0.0.1 2018年5月23日 下午3:53:26 liushiming
 */
public class MetricsExporter {

  private Counter totalRequestCounter;
  private Gauge inprogressRequestsGauge;
  private Histogram requestLatencyHistogram;
  private Summary requestLatencySummary;
  private Summary requestSizeSummary;
  private Summary responseSizeSummary;
  private Counter http1XXCounter;
  private Counter http2XXCounter;
  private Counter http3XXCounter;
  private Counter http4XXCounter;
  private Counter http5XXCounter;

  public MetricsExporter() {
    totalRequestCounter = Counter.build().name("requests_total").help("Requests total")
        .labelNames("method", "uri").register();
    inprogressRequestsGauge = Gauge.build().name("inprogress_requests").help("Inprogress Requests")
        .labelNames("method", "uri").register();
    requestLatencyHistogram =
        Histogram.build().labelNames("method").name("requests_latency_seconds")
            .help("Request latency in seconds.").labelNames("method", "uri").register();
    requestLatencySummary = Summary.build().quantile(0.1, 0.05).quantile(0.5, 0.05)
        .quantile(0.9, 0.01).quantile(0.99, 0.001).name("requests_latency").help("Request latency")
        .labelNames("method", "uri").register();
    requestSizeSummary = Summary.build().quantile(0.1, 0.05).quantile(0.5, 0.05).quantile(0.9, 0.01)
        .quantile(0.99, 0.001).name("request_size").help("Request size").register();
    responseSizeSummary =
        Summary.build().quantile(0.1, 0.05).quantile(0.5, 0.05).quantile(0.9, 0.01)
            .quantile(0.99, 0.001).name("response_size").help("Response size").register();
    http1XXCounter =
        Counter.build().name("http_1XX_requests_total").help("HTTP 1XX Status Codes").register();
    http2XXCounter =
        Counter.build().name("http_2XX_requests_total").help("HTTP 2XX Status Codes").register();
    http3XXCounter =
        Counter.build().name("http_3XX_requests_total").help("HTTP 3XX Status Codes").register();
    http4XXCounter =
        Counter.build().name("http_4XX_requests_total").help("HTTP 4XX Status Codes").register();
    http5XXCounter =
        Counter.build().name("http_5XX_requests_total").help("HTTP 5XX Status Codes").register();
  }

  public void incrementHttpStatusCodeCounters(int statusCode) {
    if (statusCode >= 100 && statusCode < 200) {
      http1XXCounter.inc();
    } else if (statusCode < 300) {
      http2XXCounter.inc();
    } else if (statusCode < 400) {
      http3XXCounter.inc();
    } else if (statusCode < 500) {
      http4XXCounter.inc();
    } else if (statusCode < 600) {
      http5XXCounter.inc();
    }
  }

  /**
   *********** Public Method **********
   */
  public Object requestStart(String method, String uri) {
    getInprogressRequestsGauge().labels(method, uri).inc();
    getTotalRequestCounter().labels(method, uri).inc();
    return new Object[] {getRequestLatencyHistogram().labels(method, uri).startTimer(),
        getRequestLatencySummary().labels(method, uri).startTimer()};
  }

  public void requestEnd(String method, String uri, int statusCode, Object object) {
    getInprogressRequestsGauge().labels(method, uri).dec();
    Object[] objects = (Object[]) object;
    ((Histogram.Timer) objects[0]).observeDuration();
    ((Summary.Timer) objects[1]).observeDuration();
    incrementHttpStatusCodeCounters(statusCode);
  }

  public void requestSize(int size) {
    getRequestSizeSummary().observe(size);
  }

  public void responseSize(int size) {
    getResponseSizeSummary().observe(size);
  }

  public Counter getTotalRequestCounter() {
    return totalRequestCounter;
  }

  public Gauge getInprogressRequestsGauge() {
    return inprogressRequestsGauge;
  }

  public Histogram getRequestLatencyHistogram() {
    return requestLatencyHistogram;
  }

  public Summary getRequestLatencySummary() {
    return requestLatencySummary;
  }

  public Summary getRequestSizeSummary() {
    return requestSizeSummary;
  }

  public Summary getResponseSizeSummary() {
    return responseSizeSummary;
  }

}
