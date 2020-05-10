#create logs directory
mkdir -p logs
#deploy jars hardway
nohup java -jar discovery-service/target/*.jar > logs/discovery-service.log 2>&1 &
sleep 30s
echo "Discovery service started"
nohup java -jar cloud-config-server/target/*.jar > logs/cloud-config-server.log 2>&1 &
sleep 30s
echo "cloud-config-server started"
nohup java -jar gateway-service/target/*.jar > logs/gateway-service.log 2>&1 &
sleep 10s
echo "gateway-service started"
nohup java -jar hystrix-dashboard/target/*.jar > logs/hystrix-dashboard.log 2>&1 &
echo "hystrix-dashboard started"
nohup java -jar order-service/target/*.jar > logs/order-service.log 2>&1 &
echo "order started"
nohup java -jar payment-service/target/*.jar > logs/payment-service.log 2>&1 &
echo "payment started"
nohup java -jar zipkin.jar > logs/zipkin-service.log 2>&1 &
echo "zipkin started"
