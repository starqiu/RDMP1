#! /usr/bin/Rscript --vanilla
# 
# Author: starqiu
###############################################################################
library('rhdfs')
hdfs.init()
library('rmr2')  
input<- '/tmp/wordcount/wordcount.txt'
output<- '/tmp/wordcount/out6'
wordcount = function(input, output = NULL, pattern = " "){
	
	wc.map = function(., lines) {
		keyval(unlist( strsplit( x = lines,split = pattern)),1)
	}
	
	wc.reduce =function(word, counts ) {
		keyval(word, sum(counts))
	}         
	
	mapreduce(input = input ,output = output, input.format = "text",
			map = wc.map, reduce = wc.reduce,combine = T)
}
wordcount(input,output)
