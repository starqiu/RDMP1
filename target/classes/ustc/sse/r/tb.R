#! /usr/bin/Rscript --vanilla
# 淘宝天池竞赛
# Author: starqiu
###############################################################################

#load data from MySQL
library(DBI)
library(RMySQL)
drv <- dbDriver("MySQL")
myconn<-dbConnect(drv,"tmailcontest","root","xing")
tbdata<-dbGetQuery(myconn,"select * from tmail_firstseason")
dbDisconnect(myconn)

#data split function
fun.data.split<- function(raw.data,begin.date,end.date,select=NULL){
  subset(raw.data,visit_datetime >=as.Date(begin.date) & visit_datetime < as.Date(end.date),select)
}

#seperate data into 2 pieces:training data,validating data
data.train <-fun.data.split(tbdata,"2013-04-15","2013-07-15",c("user_id","brand_id","type"))
data.validate <-fun.data.split(tbdata,"2013-07-15","2013-08-16",c("user_id","brand_id","type"))

#map and reduce
library('rhdfs')
hdfs.init()
library('rmr2') 

#write data into hdfs
small.ints <- to.dfs(1:10)
mapreduce(input = small.ints, map = function(k,v) cbind(v,v^2))

data.validate <- to.dfs(data.validate, output = '/tmp/tb/val.txt')

alimr = function(input ,output = NULL, pattern =" ",sep = ","){
  to.map = function(.,cols){
    keyval(cols["user_id"],cols["brand_id"])
  }
  to.reduce = function(userid,brandids){
    keyval(userid,paste(brandids,collapse = ","))
  }
  mapreduce(input = input,output = output, 
            map = to.map, reduce = to.reduce, combine = T)
}
data.res <-'/tmp/tb/res3.txt'
alimr(input =data.validate, output = data.res)


#export data into file
write.table(data.validate, file="result.txt", quote=FALSE, sep="\t")
#sink()







#test the result 
#summary(tbdata)

