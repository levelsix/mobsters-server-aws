include_recipe 'apt'

apt_repository 'rabbitmq-server' do
  uri 'http://www.rabbitmq.com/debian/'
  distribution node['lsb']['codename']
  components ['testing']['main']
  key 'http://www.rabbitmq.com/rabbitmq-signing-key-public.asc'
end

# --- Install packages we need ---
# package 'rabbitmq-server'

# cookbook-file '/etc/rabbitmq.conf'
template '/etc/rabbitmq.conf' do
  source 'rabbitmq.erb'
end

setup-user('rabbitmq')

service 'rabbitmq' do
  action :restart
  user 'rabbitmq'
end

