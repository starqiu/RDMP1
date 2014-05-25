#! /usr/bin/Rscript --vanilla
# 淘宝天池竞赛
# Author: starqiu
############################################################################
setwd('/home/starqiu/workspace/RDMP1/src/main/java/ustc/sse/r')
draw.train <- function(){
  train <- read.table('train.txt',
                      header = F,
                      dec = '.',
                      col.names = c("user","item","pref"),
                      na.strings = c('XXXXXX'))
  summary(train)  
  pdf("train.pdf")
  attach(train)
  opar <- par(no.readonly = TRUE)
  par(mfrow = c(3,1))
  plot(user,item,main="Scatterplot of user vs. item")
  abline(lm(user~item))
  plot(user,pref,main="Scatterplot of user vs. pref")
  abline(lm(user~pref))
  boxplot(pref,main="Boxplot of pref")
  par(opar)
  detach(train)
  dev.off()
  
}
#draw.train()
train <- read.table('train.txt',
                    header = F,
                    dec = '.',
                    col.names = c("user","item","pref"),
                    na.strings = c('XXXXXX'))
summary(train)  
pdf("train.pdf")
attach(train)
opar <- par(no.readonly = TRUE)
par(mfrow = c(3,1))
plot(train$user,train$item,main="Scatterplot of user vs. item")
abline(lm(user~item))
plot(user,pref,main="Scatterplot of user vs. pref")
abline(lm(user~pref))
boxplot(pref,main="Boxplot of pref")
par(opar)
#sink()
detach(train)
dev.off()