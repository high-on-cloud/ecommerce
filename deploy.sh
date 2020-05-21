#functions
kill_processes(){
  sh ./kill_pids.sh
}

start_processes(){
  #deploy jars hardway
nohup java -jar discovery-service/target/*.jar > logs/discovery-service.log 2>&1 &
echo $! >> save_pid.txt
echo "Discovery service started $!"
echo "waiting for 30s to complete discovery service startup process"
sleep 30s
nohup java -jar cloud-config-server/target/*.jar > logs/cloud-config-server.log 2>&1 &
echo $! >> save_pid.txt
echo "cloud-config-server service started $!"
echo "waiting for 30s to complete discovery service startup process"
sleep 30s
nohup java -jar gateway-service/target/*.jar > logs/gateway-service.log 2>&1 &
echo $! >> save_pid.txt
echo "gateway-service service started $!"
echo "waiting for 10s to complete discovery service startup process"
sleep 10s
nohup java -jar hystrix-dashboard/target/*.jar > logs/hystrix-dashboard.log 2>&1 &
echo $! >> save_pid.txt
echo "hystrix-dashboard started $!"
nohup java -jar order-service/target/*.jar > logs/order-service.log 2>&1 &
echo $! >> save_pid.txt
echo "order started $!"
nohup java -jar payment-service/target/*.jar > logs/payment-service.log 2>&1 &
echo $! >> save_pid.txt
echo "payment started $!"
#curl -sSL https://zipkin.io/quickstart.sh | bash -s java -jar zipkin.jar > logs/zipkin-service.log 2>&1 &
#echo "zipkin started $!"


echo "Stack deployment completed"
}


kill_processes

#if build is passes then repackage
if [ "build" == "$1" ]; then
  mvn clean package -DskipTests
else
  echo 'Using existing jars'
fi

#remove save pids
rm save_pid.txt

#create logs directory
mkdir -p logs

start_processes

