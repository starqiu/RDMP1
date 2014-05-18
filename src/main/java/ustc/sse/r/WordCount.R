#! /usr/bin/Rscript --vanilla
# 
# Author: starqiu
###############################################################################
library('rhdfs')
hdfs.init()
library('rmr2')  
input<- '/tmp/wordcount.txt'
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
output.hdfs <- wordcount(input)
write.table(from.dfs(output.hdfs), file="wordcount.txt", quote=FALSE, sep="\t",row.names = FALSE, col.names = FALSE)
