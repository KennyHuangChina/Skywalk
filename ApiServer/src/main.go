package main

import (
	_ "ApiServer/routers"
	"github.com/astaxie/beego"
	"github.com/astaxie/beego/plugins/cors"
)

func main() {
	if beego.BConfig.RunMode == "dev" {
		// beego.BConfig.DirectoryIndex = true
		// beego.BConfig.StaticDir["/swagger"] = "swagger"
	}
	beego.BConfig.WebConfig.Session.SessionOn = true
	beego.BConfig.WebConfig.Session.SessionName = "SKSession"
	beego.BConfig.WebConfig.Session.SessionAutoSetCookie = true
	beego.BConfig.WebConfig.Session.SessionGCMaxLifetime = 600

	switch beego.BConfig.RunMode {
	case "dev":
		beego.BConfig.WebConfig.Session.SessionDomain = ""
		beego.InsertFilter("*", beego.BeforeRouter, cors.Allow(&cors.Options{
			AllowOrigins:     []string{"http://:8080"},
			AllowHeaders:     []string{"Origin"},
			AllowMethods:     []string{"PUT", "GET", "OPTIONS", "POST", "DELETE"},
			AllowCredentials: true,
		}))
	case "prod":
		beego.BConfig.WebConfig.Session.SessionDomain = "cidanash.com"
		beego.InsertFilter("*", beego.BeforeRouter, cors.Allow(&cors.Options{
			AllowOrigins:     []string{"https://sshz.cidanash.com:9090"},
			AllowHeaders:     []string{"Origin"},
			AllowMethods:     []string{"PUT", "GET", "OPTIONS", "POST", "DELETE"},
			AllowCredentials: true,
		}))
	}

	beego.Run()
}
