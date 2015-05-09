require('rmr2')
wordcount = function(input, output = NULL, pattern = "\\s+"){
    wc.map = 
      function(., lines) {
      keyval(unlist(strsplit(x = lines,split = pattern)), 1)
    }
    wc.reduce =
      function(word, counts ) {
        keyval(word, sum(counts))
      }
    mapreduce(
      input = input ,
      output = output,
      input.format = "text",
      map = wc.map,
      reduce = wc.reduce,
      combine = T
    )
}
out = wordcount('/user/rstudio/wc/10.txt','/user/rstudio/wc/output')
ret = from.dfs(out)
ret.df = as.data.frame(ret, stringAsFactors=T)
colnames(ret.df) = c('word', 'count')
head(ret.df)
write.table(ret.df, quote = FALSE, file="result.txt", row.names = FALSE)
