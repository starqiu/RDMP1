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
train <-fun.data.split(tbdata,"2013-04-15","2013-07-15",c("user_id","brand_id","type"))
data.validate <-fun.data.split(tbdata,"2013-07-15","2013-08-16",c("user_id","brand_id","type"))

#训练数据新增评分列，初始为0
library(plyr)
getpref <- function(type){
  tmp <- type + 1
  #(点击：0 购买：1 收藏：2 购物车：3）
  switch(tmp,1,26,10,26)
}
train <- ddply(train,.(user_id,brand_id,type),summarize,pref=getpref(type[1]))
names(train)<-c("user","item","type","pref")
train <- train[c("user","item","pref")]

#map and reduce
library('rhdfs')
hdfs.init()
library('rmr2') 
#使用rmr的hadoop格式，hadoop是默认设置。
rmr.options(backend = 'hadoop')

#把数据集存入HDFS
train.hdfs<- to.dfs(keyval(train$user_id,train))
from.dfs(train.hdfs)

#计算用户列表
usersUnique<-function(){
  users<-unique(train$user)
  users[order(users)]
}

#计算商品列表方法
itemsUnique<-function(){
  items<-unique(train$item)
  items[order(items)]
}

# 用户列表
users<-usersUnique() 
users

# 商品列表
items<-itemsUnique() 
items

#建立商品列表索引
index<-function(x) which(items %in% x)
data<-ddply(train,.(user,item,pref),summarize,idx=index(item)) 
data

#同现矩阵
cooccurrence<-function(data){
  n<-length(items)
  co<-matrix(rep(0,n*n),nrow=n)
  for(u in users){
    idx<-index(data$item[which(data$user==u)])
    m<-merge(idx,idx)
    for(i in 1:nrow(m)){
      co[m$x[i],m$y[i]]=co[m$x[i],m$y[i]]+1
    }
  }
  return(co)
}

#推荐算法
recommend<-function(udata=udata,co=coMatrix,num=0){
  n<-length(items)
  
  # all of pref
  pref<-rep(0,n)
  pref[udata$idx]<-udata$pref
  
  # 用户评分矩阵
  userx<-matrix(pref,nrow=n)
  
  # 同现矩阵*评分矩阵
  r<-co %*% userx
  
  # 推荐结果排序
  r[udata$idx]<-0
  idx<-order(r,decreasing=TRUE)
  topn<-data.frame(user=rep(udata$user[1],length(idx)),item=items[idx],val=r[idx])

# 推荐结果取前num个
if(num>0){
  topn<-head(topn,num)
}

#返回结果
return(topn)
}

#生成同现矩阵
co<-cooccurrence(data) 
co

#计算推荐结果
recommendation<-data.frame()
for(i in 1:length(users)){
  udata<-data[which(data$user==users[i]),]
  recommendation<-rbind(recommendation,recommend(udata,co,0)) 
} 

recommendation

#export data into file
write.table(recommendation, file="myresult.txt", quote=FALSE, sep="\t")
sink()