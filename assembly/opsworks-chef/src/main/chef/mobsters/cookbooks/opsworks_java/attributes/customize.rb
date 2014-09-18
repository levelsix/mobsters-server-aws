normal[:opsworks_java][:jvm_version] = "7"
normal[:opsworks_java][:jvm_options] = "-server -d64 -Djava.awt.headless=true -XX:+UseConcMarkSweepGC -XX:+CMSIncrementalMode"
normal[:opsworks_java][:java_app_server] = "tomcat" 
normal[:opsworks_java][:java_app_server_version] = "7.0" 
normal[:opsworks_java][:tomcat][:port] = 8080
normal[:opsworks_java][:tomcat][:threadpool_max_threads] = 400
normal[:opsworks_java][:tomcat][:threadpool_min_spare_threads] = 8
normal[:opsworks_java][:tomcat][:use_threadpool] = "true"
normal[:opsworks_java][:tomcat][:java_opts] = "-server -d64 -Dspring.profiles.default=staging -Djava.awt.headless=true -XX:+UseConcMarkSweepGC -XX:+CMSIncrementalMode -Xms756m -Xmx1536m"
