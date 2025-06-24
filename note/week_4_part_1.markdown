# ubuntu 開機自動啟動 stylish.service & nginx
/etc/systemd/system/stylish.service
```txt
[Unit]
Description=STYLiSH Spring Boot Application
After=network.target
Wants=nginx.service

[Service]
Type=simple
User=ubuntu
WorkingDirectory=/home/ubuntu
ExecStart=/bin/bash -c 'source /home/ubuntu/.bashrc && java -jar /home/ubuntu/STYLiSH-0.0.1-SNAPSHOT.jar'
Restart=on-failure
RestartSec=5s
StandardOutput=append:/home/ubuntu/output.log
StandardError=append:/home/ubuntu/output.log

[Install]
WantedBy=multi-user.target
```

# 查看日誌
journalctl -u stylish -f

# 啟用服務自動啟動
sudo systemctl enable stylish
sudo systemctl enable nginx

# 啟動服務（如果尚未運行）
sudo systemctl start stylish
sudo systemctl start nginx

# 重啟服務
sudo systemctl restart stylish

# 確認服務是否有自動啟動
systemctl is-enabled stylish
systemctl is-enabled nginx

# 確認服務狀態
systemctl status stylish
systemctl status nginx

#清除本地ssh連線
ssh-keygen -R 18.180.115.129