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
