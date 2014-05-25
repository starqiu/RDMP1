#! /usr/bin/Rscript --vanilla
# 淘宝天池竞赛
# Author: starqiu
############################################################################
setwd('/home/starqiu/workspace/RDMP1/src/main/java/ustc/sse/r')
library(scatterplot3d)
#训练集
draw.train <- function(){
	train <- read.table('train.txt',
	          header = F,
	          dec = '.',
	          col.names = c("user","item","pref"),
	          na.strings = c('XXXXXX'))
	  
	png("train.png")
		attach(train)
			opar <- par(no.readonly = TRUE)
			par(mfrow = c(4,1))
			hist(pref,main="Hist of pref")
			plot(user,item,main="Scatterplot of user vs. item")
			abline(lm(user~item))
			plot(user,pref,main="Scatterplot of user vs. pref")
			abline(lm(user~pref))
			boxplot(pref,main="Boxplot of pref")
			par(opar)
		detach(train)
	dev.off()

}
draw.train()

#同现矩阵
draw.step2 <- function(){
	step2 <- read.table('step2Mr.txt',
			header = F,
			dec = '.',
			col.names = c("k.item","item1","item2","freq"),
			na.strings = c('XXXXXX'))
	
	png("step2.png")
		attach(step2)
			opar <- par(no.readonly = TRUE)
			par(mfrow = c(1,1)) 
    	s3d <- scatterplot3d(item1,item2,freq,
                           pch=16,
                           highlight.3d=TRUE,
    	                     col.axis="blue",
    	                     col.grid="lightblue",
                           type="h",
                           main="the scatterplot3d of cooccurrence matrix")
	    fit <- lm(freq ~ item1 + item2)
      s3d$plane3d(fit,lty.box = "solid")
      par(opar)
		detach(step2)
	dev.off()
	
}
draw.step2()

#评分矩阵
draw.train2 <- function(){
	train2 <- read.table('train2Mr.txt',
			header = F,
			dec = '.',
			col.names = c("k.user","user","item","pref"),
			na.strings = c('XXXXXX'))
	
	png("train2.png")
		attach(train2)
			opar <- par(no.readonly = TRUE)
			par(mfrow = c(4,1))
			hist(pref,main="Hist of pref")
			plot(user,item,main="Scatterplot of user vs. item")
			#abline(lm(user~item))
			plot(user,pref,main="Scatterplot of user vs. pref")
			#abline(lm(user~pref))
			boxplot(pref,main="Boxplot of pref")
			par(opar)
		detach(train2)
	dev.off()
	
}
draw.train2()

#推荐结果
draw.result <- function(){
  result <- read.table('resultMr.txt',
                       header = F,
                       dec = '.',
                       col.names = c("k.user","user","item","pref"),
                       na.strings = c('XXXXXX'))
  
  png("result.png")
	  attach(result)
		  opar <- par(no.readonly = TRUE)
		  par(mfrow = c(4,1))
		  hist(pref,main="Hist of pref")
		  plot(user,item,main="Scatterplot of user vs. item")
		  abline(lm(user~item))
		  plot(user,pref,main="Scatterplot of user vs. pref")
		  abline(lm(user~pref))
		  boxplot(pref,main="Boxplot of pref")
		  par(opar)
	  detach(result)
  dev.off()
  
}
draw.result()

#校验集
draw.validate <- function(){
	validate <- read.table('validate.txt',
			header = F,
			dec = '.',
			col.names = c("user","item","pref"),
			na.strings = c('XXXXXX'))
	
	png("validate.png")
		attach(validate)
			opar <- par(no.readonly = TRUE)
			par(mfrow = c(4,1))
			hist(pref,main="Hist of pref")
			plot(user,item,main="Scatterplot of user vs. item")
			#abline(lm(user~item))
			plot(user,pref,main="Scatterplot of user vs. pref")
			#abline(lm(user~pref))
			boxplot(pref,main="Boxplot of pref")
			par(opar)
		detach(validate)
	dev.off()
	
}
draw.validate()