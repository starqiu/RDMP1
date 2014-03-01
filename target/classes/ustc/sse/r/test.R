#! /usr/bin/Rscript --vanilla
a<-1:10
print(a)
args <- commandArgs(TRUE)
# paste(c("I", "like", args[1], "and", args[2], "!"), collapse = " ")
print(args)
area<-function(r){pi*r^2}