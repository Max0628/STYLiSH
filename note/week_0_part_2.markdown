### week 0 part 2

homework
url : http://35.74.181.119

write nginx config in site-available and soft link to site-enable

```txt
server {
	listen 80; //nginx listen to server 80 port for http request
	server_name 35.74.181.119; //server host domain ( here is ip )

	location / {                // Forward request to 8080 spring boot app
	    proxy_pass http://localhost:8080/;
	    proxy_set_header Host $host;
	    proxy_set_header X-Real-IP $remote_addr;
	    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
	    proxy_set_header X-Forwarded-Proto $scheme;

	}
}
```

###

Commands

install java

```
sudo apt install openjdk-17-jdk
sudo apt install openjdk-17-jre
java -version
javac -version
```

link ec2 file to github repo

```
ssh-keygen -t ed25519 -C "maxchauo0628@gmail.com"
and copy public key to git hub <add key pair>
ssh -T git@github.com (test if success)
```

nginx

```
apt install nginx
sudo systemctl start nginx
sudo systemctl restart nginx
sudo systemctl status nginx
sudo nginx -t (check config syntax if correct)
```

mysql

```
sudo apt install mysql-server -y
sudo systemctl start mysql
sudo systemctl enable mysql
sudo mysql
SELECT user, host, authentication_string, plugin FROM mysql.user WHERE user='root';
ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'mysqlpwd123';//set pwd
FLUSH PRIVILEGES;
```

mysql table

```sql
CREATE TABLE stylish(
  id BIGINT UNSIGNED AUTO_INCREMENT,
  title VARCHAR(255),
  PRIMARY KEY(id)
);
```

run java app on background

```
nohup java -jar STYLiSH-0.0.1-SNAPSHOT.jar > output.log 2>&1 &
nohup java -jar w3p4worker-0.0.1-SNAPSHOT.jar > output.log 2>&1 &
```

list 8080 port info

```
lsof -i:8080
```

kill pid

```
kill <PID>
```
