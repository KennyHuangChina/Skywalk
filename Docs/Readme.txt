************************************************************************************
*
*	让 Git log 漂亮
*
1. git config --global alias.lg "log --color --graph --pretty=format:'%Cred%h%Creset -%C(yellow)%d%Creset %s %Cgreen (%cr) %C(bold blue)<%an>%Creset' --abbrev-commit --"

2. git lg

************************************************************************************
*
*	Setup 运行环境
*
1. 修改 hostname 为 ub1604-skywalk-dev。注意，Windows 中访问 "\\ub1604-skywalk-"，访问 “\\ub1604-skywalk-dev” 会失败

2. 将 db.sql 文件复制到 \\ub1604-skywalk-\share

3. cd ~/share

4. mysql -u root -proot

5. DROP DATABASE rtdb;

6. CREATE DATABASE IF NOT EXISTS rtdb DEFAULT CHARSET utf8 COLLATE utf8_general_ci;

7. exit

8. mysql -u root -proot rtdb < db.sql (将测试数据导入数据库)