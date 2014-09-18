# git '/home/ubuntu/git/mobsters-server' do
#   user 'ubuntu'
#   repository 'https://github.com/lvl6/mobsters-server.git'
#   destination '/home/ubuntu/git/mobsters-server'
#   action 'checkout'
#   enable_submodules true
# end

directory '/home/ubuntu/.m2' do
  owner 'ubuntu'
  group 'root'
  mode 00644
  action :create 
end

template '/home/ubuntu/.m2/settings.xml' do
  source 'maven_settings.erb'
end

script 'Build Toon Squad' do
  interpreter 'bash'
  user 'ubuntu'
  code <<-EOH
    cd /home/ubuntu/git/monsters-server
    mvn clean
    mvn -DskipTests=true install
  EOH
end

script 'Correct ownership for tomcat' do
  user 'root'
  interpreter 'bash'
  code <<-EOH
    chown -R ubuntu:ubuntu /var/lib/tomcat7/
    chown -R ubuntu:ubuntu /var/log/tomcat7/
    chown -R ubuntu:ubuntu /var/run/tomcat7/
  EOH
end

script 'Deploy toon squad war file' do
  user 'ubuntu'
  interpreter 'bash'
  code <<-EOH
    cp /home/ubuntu/.m2/repository/com/lvl6/mobsters/server/*/*war /var/run/tomcat7/webapps/ROOT.war
  EOH
end

service 'tomcat' do
  action :restart
end
