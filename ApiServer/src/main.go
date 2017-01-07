package main

import (
	_ "ApiServer/routers"
	"github.com/astaxie/beego"
)

func main() {
	beego.Run()
}

