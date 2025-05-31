<!-- pull redis image  -->
docker pull redis:7.2

<!-- run redis container  -->
docker run --name redis -p 6379:6379 -d redis:7.2 redis-server --requirepass redispwd123

<!-- go into redis  -->
docker exec -it redis redis-cli

<!-- enter pwd -->
AUTH redispwd123

<!-- testing connection -->
PING 
and get response "PONG"
    


