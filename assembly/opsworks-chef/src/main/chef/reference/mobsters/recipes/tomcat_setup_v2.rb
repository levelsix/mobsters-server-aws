include_recipe 'opsworks_java::tomcat_service'
include_recipe 'mobsters::rsyslog_service'

template '/etc/profile.d/lvl6envvars.sh' do
  source 'lvl6envvars.erb'
end

cron 'ntpdate' do
  minute '1'
  hour '*'
  day '*'
  month '*'
  weekday '*'
  command '/usr/sbin/ntpdate-debian >/dev/null 2>&1'
end

directory '/var/run/tomcat7' do
  owner 'root'
  group 'root'
  mode 00644
  action :create
end

directory '/var/lib/tomcat7' do
  owner 'root'
  group 'root'
  mode 00644
  action :create
end

directory '/var/log/tomcat7' do
  owner 'root'
  group 'root'
  mode 00644
  action :create
end

script 'Correct ownership for tomcat' do
  user 'root'
  interpreter 'bash'
  code <<-EOH
    chown -R ubuntu:ubuntu /var/run/tomcat7/
    chown -R ubuntu:ubuntu /var/lib/tomcat7/
    chown -R ubuntu:ubuntu /var/log/tomcat7/
  EOH
end

service 'tomcat' do
  action :restart
end

service 'rsyslog' do
  # service_name node['opsworks_java']['tomcat']['service_name']
  service_name 'rsyslog'

  case node[:platform_family]
  when 'debian'
    supports :restart => true, :reload => false, :status => true
  when 'rhel'
    supports :restart => true, :reload => true, :status => true
  when 'ubuntu'
    supports :restart => true, :reload => true, :status => true
  end

  action :nothing
end

template '/etc/logrotate.d/tomcat' do
  source 'logrotate_tomcat.erb'
  notifies :restart, 'service[rsyslog]'
end
