﻿# KafkaConsumer

本项目由maven构建，使用的时候可以根据自己所使用kafka的版本不同，去pom文件里面修改所kafka的版本，maven依赖可以从http://mvnrepository.com/里面获得

提供两种从kafka得到数据的方式

1、直接从kafka得到数据，不是按照offset主动拉取的，偏移等信息的控制由kafka server端控制

2、提供从kafka按照offset得到数据，client端可以更加灵活的去控制如何读取数据，方便错误回溯

额外支持得到当前kafka对应的topic以及分区下的最大的offset

其中配置文件为：./conf/sysconfig.properties，需要注意的是当使用offset获得数据的时候，需要的是kafka的ip以及端口；如果自动获
得kafka数据的时候，即使用high level的方式，让kafka自动push的时候，需要的是zookeeper的ip以及端口

关于配置文件的每一个参数的含义在里面均有详细的说明在此不过多叙述

