# OpenTelemetry-SpringBoot-Sample

This Repo should provide a simple example of how you can use OpenTelemetry to transfer your Logs, Traces and Metrics to the Grafana Stack (Prometheus, Loki and Tempo). It should be used for testing out new functionalities that should help to meet the goal of Cloud Observability.

## Setup

1. Navigate to the grafana-stack-podman folder and run `podman play kube grafana-stack.yaml`
2. Start the SpringBoot application
3. Open http://localhost:3000
4. Enjoy :)
