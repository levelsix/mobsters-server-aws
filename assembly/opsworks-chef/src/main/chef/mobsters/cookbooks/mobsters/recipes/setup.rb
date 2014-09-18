template '/etc/profile.d/lvl6envvars.sh' do
  source 'lvl6envvars.erb'
end

# cron 'ntpdate' do
#   minute '1'
#   hour '*'
#   day '*'
#   month '*'
#   weekday '*'
#   command '/usr/sbin/ntpdate-debian >/dev/null 2>&1'
# end

if node[:opsworks][:instance][:layers].include? :rabbit
  apt_repository 'rabbitmq' do
	uri 'http://www.rabbitmq.com/debian/'
    distribution 'testing'
    components ['main']  
    deb_src true
    key rabbitmq-signing-key-public.asc
    action :add
  end
	
  package 'rabbitmq-server'
end
