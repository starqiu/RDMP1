#! /usr/bin/Rscript --vanilla
# 
# Author: starqiu
###############################################################################
library(Cairo)
library(png)
library(ggplot2)
Cairo(file='/dev/null')

qplot(rnorm(5000))# your plot# hidden stuff in Cairo
i =Cairo:::.image(dev.cur())
r =Cairo:::.ptr.to.raw(i$ref,0, i$width * i$height *4)
dim(r)= c(4, i$width, i$height)# RGBA planes# have to swap the red & blue components for some reason
r[c(1,3),,]= r[c(3,1),,]# now use the png library
p = writePNG(r, raw())# raw PNG bytes