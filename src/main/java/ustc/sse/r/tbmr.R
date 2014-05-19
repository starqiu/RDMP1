#! /usr/bin/Rscript --vanilla
# 淘宝天池竞赛
# Author: starqiu
###############################################################################

#load data from MySQL
library(DBI)
library(RMySQL)
drv <- dbDriver("MySQL")
myconn<-dbConnect(drv,"tmailcontest","root","xing")
tbdata<-dbGetQuery(myconn,"select * from tmail_firstseason limit 10")
#tbdata<-dbGetQuery(myconn,"select * from tmail_firstseason")
dbDisconnect(myconn)

#data split function
fun.data.split<- function(raw.data,begin.date,end.date,select=NULL){
  subset(raw.data,visit_datetime >=as.Date(begin.date) & visit_datetime < as.Date(end.date),select)
}

#seperate data into 2 pieces:training data,validating data
train <-fun.data.split(tbdata,"2013-04-15","2013-07-15",c("user_id","brand_id","type"))
validate <-fun.data.split(tbdata,"2013-07-15","2013-08-16",c("user_id","brand_id","type"))
rm(tbdata)

#训练数据新增评分列，初始为0
library(plyr)
getpref <- function(type){
  #(点击：0 购买：1 收藏：2 购物车：3）
  switch(type + 1,1,26,10,26)
}
train <- ddply(train,.(user_id,brand_id,type),summarize,pref=getpref(type[1]))
names(train)<-c("user","item","type","pref")
train <- train[c("user","item","pref")]
write.table(train, file="train.txt", quote=FALSE, sep="\t", row.names = FALSE, col.names = FALSE)
#map and reduce
library('rhdfs')
hdfs.init()
library('rmr2') 
#使用rmr的hadoop格式，hadoop是默认设置。
rmr.options(backend = 'hadoop')

#把数据集存入HDFS
train.hdfs<- to.dfs(keyval(train$user,train))
from.dfs(train.hdfs)
rm(train)

#STEP 1, 建立物品的同现矩阵
# 1) 按用户分组，得到所有物品出现的组合列表。
train.mr<-mapreduce(
  train.hdfs, 
  map = function(k, v) {
    keyval(k,v$item)
  }
  ,reduce=function(k,v){
    m<-merge(v,v)
    keyval(m$x,m$y)
  }
)
from.dfs(train.mr)
write.table(from.dfs(train.mr), file="trainMr.txt", quote=FALSE, sep="\t",row.names = FALSE, col.names = FALSE)

# 2) 对物品组合列表进行计数，建立物品的同现矩阵
step2.mr<-mapreduce(
  train.mr,
  map = function(k, v) {
    d<-data.frame(k,v)
    d2<-ddply(d,.(k,v),count)
    
    key<-d2$k
    val<-d2
    keyval(key,val)
  }
)
rm(train.mr)
from.dfs(step2.mr)
write.table(from.dfs(step2.mr), file="step2Mr.txt", quote=FALSE, sep="\t",row.names = FALSE, col.names = FALSE)

# 2. 建立用户对物品的评分矩阵

train2.mr<-mapreduce(
  train.hdfs, 
  map = function(k, v) {
    #df<-v[which(v$user==3),]
    df<-v
    key<-df$item
    val<-data.frame(item=df$item,user=df$user,pref=df$pref)
    keyval(key,val)
  }
)
rm(train.hdfs)
from.dfs(train2.mr)
write.table(from.dfs(train2.mr), file="train2Mr.txt", quote=FALSE, sep="\t",row.names = FALSE, col.names = FALSE)

#3. 合并同现矩阵 和 评分矩阵
eq.hdfs<-equijoin(
  left.input=step2.mr, 
  right.input=train2.mr,
  map.left=function(k,v){
    keyval(k,v)
  },
  map.right=function(k,v){
    keyval(k,v)
  },
  reduce = function(k,v){
    keyval(v$k.l,v)
  },
  outer = c("left")
)
rm(step2.mr)
rm(train2.mr)
from.dfs(eq.hdfs)
capture.output(from.dfs(eq.hdfs), file="eqhdfs.txt")
#write.table(from.dfs(eq.hdfs), file="eqhdfs.txt", quote=FALSE, sep="\t",row.names = FALSE, col.names = FALSE)

#4. 计算推荐结果列表
cal.mr<-mapreduce(
  input=eq.hdfs,
  map=function(k,v){
    val<-v
    na<-is.na(v$user.r)
    if(length(which(na))>0) val<-v[-which(is.na(v$user.r)),]
    keyval(val$k.l,val)
  }
  ,reduce=function(k,v){
    val<-ddply(v,.(k.l,v.l,user.r),summarize,v=freq.l*pref.r)
    keyval(val$k.l,val)
  }
)
rm(eq.hdfs)
from.dfs(cal.mr)
write.table(from.dfs(cal.mr), file="calMr.txt", quote=FALSE, sep="\t",row.names = FALSE, col.names = FALSE)

#5. 按输入格式得到推荐评分列表
result.mr<-mapreduce(
  input=cal.mr,
  map=function(k,v){
    keyval(v$user.r,v)
  }
  ,reduce=function(k,v){
    val<-ddply(v,.(user.r,v.l),summarize,v=sum(v))
    val2<-val[order(val$v,decreasing=TRUE),]
    names(val2)<-c("user","item","pref")
    keyval(val2$user,val2)
  }
)
rm(cal.mr)
from.dfs(result.mr)
#export data into file
write.table(from.dfs(result.mr), file="resultMr.txt", quote=FALSE, sep="\t",row.names = FALSE, col.names = FALSE)
rm(result.mr)
sink()
