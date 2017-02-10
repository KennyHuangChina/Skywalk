****************************************************************************************************************************
*
*	让 Git log 漂亮
*
****************************************************************************************************************************
1. git config --global alias.lg "log --color --graph --pretty=format:'%Cred%h%Creset -%C(yellow)%d%Creset %s %Cgreen (%cr) %C(bold blue)<%an>%Creset' --abbrev-commit --"

2. git lg

****************************************************************************************************************************
*
*	Setup 运行环境
*
****************************************************************************************************************************
1. 修改 hostname 为 ub1604-skywalk-dev。注意，Windows 中访问 "\\ub1604-skywalk-"，访问 “\\ub1604-skywalk-dev” 会失败

2. 将 db.sql 文件复制到 \\ub1604-skywalk-\share

3. cd ~/share

4. mysql -u root -proot

5. DROP DATABASE rtdb;

6. CREATE DATABASE IF NOT EXISTS rtdb DEFAULT CHARSET utf8 COLLATE utf8_general_ci;

7. exit

8. mysql -u root -proot rtdb < db.sql (将测试数据导入数据库)

****************************************************************************************************************************
*
*	安装图形处理依赖库
*
****************************************************************************************************************************
1. cd $GOPATH/src
2. zgo lib 安装
	go get github.com/nutzam/zgo
	主要用了 z.FileType 来判断图片类型，或许可以改用标准库
3. 官方 graphics 库
	a) 标准的方式应该是 go get code.google.com/p/graphics-go/graphics。但是没有成功，错误：no go-import meta tags
	b) 在 Github 上找到一个 project：graphics-go，号称是 Automatically exported from code.google.com/p/graphics-go，
	比较了 issues 确认和 code.google.com/p/graphics-go/graphics 一样，get 这个 library，编译成功
