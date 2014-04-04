#! /usr/bin/Rscript --vanilla
# 淘宝天池竞赛
# Author: starqiu
###############################################################################
library(RODBC)
myconn<-odbcConnect("tmailcontest",uid="root",pwd="xing")
tb.data<-sqlQuery(myconn,"select * from tmail_firstseason")
close(myconn)

summary(tb.data)
