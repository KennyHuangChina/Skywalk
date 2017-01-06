#! /bin/bash

export GOPATH=$GOPATH:`pwd`
cd src
#bee run
go build -v -o ApiServer 

