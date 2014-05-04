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
rows <-nrow(train)
pref<-rep.int(0,rows)
train<-data.frame(train$user_id,train$brand_id,train$type,pref)
names(train)<-c("user_id","brand_id","type","pref")

#map and reduce
library('rhdfs')
hdfs.init()
library('rmr2') 
#使用rmr的hadoop格式，hadoop是默认设置。
rmr.options(backend = 'hadoop')

#把数据集存入HDFS
train.hdfs<- to.dfs(keyval(train$user_id,train))
from.dfs(train.hdfs)

#计算每个用户对有操作商品的评分，初始为0

mr.train.pref <-mapreduce(
  input = train.hdfs,
  map = function(k,v){
    #(点击：0 购买：1 收藏：2 购物车：3）
    v$pref <- switch(EXPR = v$type+1，
                     3 ,
                     20 ,
                     10 ,
                     20 )
    keyval(k,v)
  })
train.path<- to.dfs(train)
#train.pref.path<-mr.train.pref(input=train.path)
library(plyr)

#计算用户列表
usersUnique<-function(){
  users<-unique(train$user_id)
  users[order(users)]
}

#计算商品列表方法
itemsUnique<-function(){
  items<-unique(train$brand_id)
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
data<-ddply(train,.(user_id,brand_id,pref),summarize,idx=index(brand_id)) 
data
#同现矩阵
cooccurrence<-function(data){
  n<-length(items)
  co<-matrix(rep(0,n*n),nrow=n)
  for(u in users){
    idx<-index(data$brand_id[which(data$user_id==u)])
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
  topn<-data.frame(user=rep(udata$user_id[1],length(idx)),item=items[idx],val=r[idx])

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
  udata<-data[which(data$user_id==users[i]),]
  recommendation<-rbind(recommendation,recommend(udata,co,0)) 
} 

recommendation

#write data into hdfs
data.validate.path<- to.dfs(data.validate)
from.dfs(data.validate.path)

alimr = function(input ,output = NULL, pattern =" ",sep = ","){
  to.map = function(.,cols){
    cols <- strsplit(cols,"")
    keyval(sapply(cols,"[",2),sapply(cols,"[",3))
  }
  to.reduce = function(userid,brandids){
    keyval(userid,paste(brandids,collapse = ","))
  }
  mapreduce(input = input,output = output, 
            map = to.map, reduce = to.reduce, combine = T)
}

data.res.path <- alimr(input =data.validate.path)


#export data into file
write.table(data.validate, file="result.txt", quote=FALSE, sep="\t")
#sink()

#test the result 
#summary(tbdata)
