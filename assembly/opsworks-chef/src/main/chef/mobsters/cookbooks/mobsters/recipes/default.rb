# --- Install packages we need ---
# package 'build-essential'
# package 'git'
# package 'monit'
# package 'openjdk-7-jre-headless'
# package 'rsyslog'
# package 'maven'

# script "Install Dotfiles" do
#   interpreter "bash"
#   user "ubuntu"
#   code <<-EOH
#     source /usr/local/rvm/scripts/rvm
#     cd /home/ubuntu/dotfiles
#     rake install
#   EOH
# end

cron "ntpdate" do
  minute "1"
  hour "*"
  day "*"
  month "*"
  weekday "*"
  command "/usr/sbin/ntpdate-debian >/dev/null 2>&1"
end

git "/home/ubuntu/git/mobsters-server" do
  user "ubuntu"
  repository "https://github.com/lvl6/mobsters-server.git"
  destination "/home/ubuntu/git/mobsters-server"
  action "checkout"
  enable_submodules true
end

#cookbook_file '/home/ubuntu/.m2/settings.xml' do
template '/home/ubuntu/.m2/settings.xml' do
  source 'maven_settings.erb'
end

script "Build Toon Squad" do
  interpreter "bash"
  user "ubuntu"
  code <<-EOH
    cd /home/ubuntu/git/monsters-server
    mvn clean
    mvn -DskipTests=true install
  EOH
end

directory "/var/run/tomcat7" do
  owner "root"
  group "root"
  mode 00644
  action :create
end

directory "/var/log/tomcat7" do
  owner "ubuntu"
  group "ubuntu"
  action :create
end

script "Correct ownership for tomcat" do
  user root
  interpreter "bash"
  code <<-EOH
    chown -R ubuntu:ubuntu /var/run/tomcat7/
  EOH
end

script 'Deploy toon squad war file' do
  user ubuntu
  interpreter 'bash'
  code <<-EOH
    cp /home/ubuntu/.m2/repository/com/lvl6/mobsters/server/*/*war /var/run/tomcat7/webapps/ROOT.war
  EOH
end

#cookbook_file '/etc/rsyslog.conf'
# template '/etc/rsyslog.conf' do
#   source 'rsyslog.conf'
# end

#cookbook_file '/etc/rsyslog.d/30-tomcat.conf'
# template '/etc/rsyslog.d/30-tomcat.conf' do
#   source '30-tomcat.conf'
# end

#cookbook_file '/etc/logrotate.d/tomcat' do
template '/etc/logrotate.d/tomcat' do
  source 'logrotate_tomcat.erb'
end

service 'rsyslog' do
  action :restart
end

service 'tomcat7' do
  action :restart
end



