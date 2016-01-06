export HADOOP_HOME=/usr/lib/hadoop
export MAHOUT_HOME=/usr/local/lib/mahout
export JAVA_HOME=/usr/lib/jvm/java-6-openjdk

export MAHOUT_JAR=$MAHOUT_HOME/mahout-core-0.7-job.jar

alias mahout=$MAHOUT_HOME/bin/mahout
alias hadoop='HADOOP_CLASSPATH=$MAHOUT_JAR hadoop'
